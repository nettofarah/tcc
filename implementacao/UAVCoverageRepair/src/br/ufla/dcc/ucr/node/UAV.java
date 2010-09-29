package br.ufla.dcc.ucr.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import br.ufla.dcc.grubix.simulator.LayerException;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.StartSimulation;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.event.user.BeaconPacket;
import br.ufla.dcc.grubix.simulator.kernel.Configuration;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.movement.MovementManager;
import br.ufla.dcc.grubix.simulator.node.Node;
import br.ufla.dcc.grubix.simulator.node.user.MoveToCommand;
import br.ufla.dcc.ucr.node.data.CoverageInformation;
import br.ufla.dcc.ucr.packet.CoverageAlarmPacket;
import br.ufla.dcc.ucr.statistics.Statistics;
import br.ufla.dcc.ucr.wuc.BeaconWakeUpCall;
import br.ufla.dcc.ucr.wuc.CheckEventWakeUpCall;
import br.ufla.dcc.ucr.wuc.CheckTargetReachedWakeUpCall;
import br.ufla.dcc.ucr.wuc.ScheduleCoverageWakeUpCall;

public class UAV extends GenericNode {
	
	private static int SCHEDULLING_DELAY = 10;//12000;
	private static int CHECK_LOCATION = 300;
	public static int BEACON_INTERVAL = 300;
	
	
	
	private SortedSet<CoverageInformation> nodesToCover = new TreeSet<CoverageInformation>();
	private List<CoverageInformation> coveringPath = new ArrayList<CoverageInformation>();
	
	private Position targetArea;
	
	public void processEvent(StartSimulation start){
		this.startSendingPackets();
		
		WakeUpCall checkEventWuc = new CheckEventWakeUpCall(this.getSender(), CHECK_EVENT_INTERVAL);
		this.sendEventSelf(checkEventWuc);
	}
	
	private void startSendingPackets() {
		Packet beacon = new BeaconPacket(this.getSender(), NodeId.ALLNODES);
		this.sendPacket(beacon);
		
		WakeUpCall wuc = new BeaconWakeUpCall(this.getSender(), BEACON_INTERVAL);
		this.sendEventSelf(wuc);
	}

	private void addNodeToCover(CoverageInformation coverInfo){
		this.nodesToCover.add(coverInfo);
	}
	

	private CoverageInformation alreadyCoverThisNode(CoverageInformation coverageInfo){
		for (CoverageInformation cInfo : this.nodesToCover) {
			if (cInfo.getSenderId() == coverageInfo.getSenderId())
				return cInfo;
		}
		return null;
	}

	

	private void checkIfIsAlreadyCovering(CoverageInformation coverInfo){
		CoverageInformation oldInfo = this.alreadyCoverThisNode(coverInfo);
		if(oldInfo != null)
			this.removeNodeToCover(oldInfo);
	}

	private void checkUntilReachDestination() {
		if (this.isDistanceAcceptable()){
			this.visitNextUncoveredArea();
		}else{
			WakeUpCall wuc = new CheckTargetReachedWakeUpCall(this.getSender(),CHECK_LOCATION);
			this.sendEventSelf(wuc);
		}
	}
	
	private boolean isDistanceAcceptable(){
		Position currentPosition = this.getNode().getPosition();
		double distance = currentPosition.getDistance(this.targetArea);
		
		return distance < 10;
	}

	private void cover(CoverageInformation coverageInfo) {
		//this.checkIfIsAlreadyCovering(coverageInfo);
		this.addNodeToCover(coverageInfo);
		
		if(isTimeToStartANewCovering()){
			this.scheduleCoverage();
		}
		
		//SimulationManager.logNodeState(this.getId(), "CoverageList", "int", this.nodesToCover.size()+"");
	}

	private void generateCoveringPath(){
		this.coveringPath.clear();
		this.coveringPath.addAll(this.nodesToCover);
		
		Collections.sort(coveringPath);
		
	/*	for (CoverageInformation cInfo : this.coveringPath) {
			SimulationManager.logNodeState(cInfo.getSenderId(), "Node to be covered", "int", "1");
		}*/
	}


	private boolean isTimeToStartANewCovering(){
		boolean isTheFirstNodeToCover = this.nodesToCover.size() == 1;
		return isTheFirstNodeToCover;
	}
	
	@Override
	public void lowerSAP(Packet packet) throws LayerException {
		if (packet instanceof CoverageAlarmPacket){
			CoverageAlarmPacket cPacket = (CoverageAlarmPacket) packet;
			this.cover(cPacket.getCoverageInfo());
		}
	}
	
	private void move(Position target){
		MoveToCommand moveCommand = new MoveToCommand();
		moveCommand.setId(this.getId());
		
		moveCommand.setTarget(target);
		
		MovementManager movementManager = Configuration.getInstance().getMovementManager();
		movementManager.sendCommand(moveCommand);
	}
	
	public void processWakeUpCall(WakeUpCall wuc){
		if(wuc instanceof ScheduleCoverageWakeUpCall)
			this.startCovering();
		
		if (wuc instanceof CheckTargetReachedWakeUpCall)
			this.checkUntilReachDestination();
		
		if (wuc instanceof BeaconWakeUpCall)
			this.startSendingPackets();
		
		if	(wuc instanceof CheckEventWakeUpCall)
			this.moveSomewhereFakingAnEvent();
	}
	
	

	private void removeNodeToCover(CoverageInformation oldInfo){
		this.nodesToCover.remove(oldInfo);
	}

	private void scheduleCoverage() {
		WakeUpCall scwuc = new ScheduleCoverageWakeUpCall(this.getSender(),SCHEDULLING_DELAY);	
		this.sendEventSelf(scwuc);
	}

	private void startCovering() {
		this.generateCoveringPath();
		this.visitNextUncoveredArea();
		
		Statistics.startCycle();
	}
	


	private void visit(CoverageInformation nextToVisit) {
		this.targetArea = nextToVisit.getPosition();
		this.move(this.targetArea);
		
		//SimulationManager.logNodeState(this.getId(), "Moving To", "float", nextToVisit.distanceFromOrigin()+"");
		
		this.checkUntilReachDestination();
	}


	private void visitNextUncoveredArea() {
		boolean isThereAreaToVisit = this.coveringPath.size() > 0;
		if(isThereAreaToVisit){			
			CoverageInformation nextToVisit = this.coveringPath.get(0);
			this.coveringPath.remove(nextToVisit);
						
			this.visit(nextToVisit);
		}else{
			this.scheduleCoverage();
		}
	}
	
	/***************************************************************************************************/
	/* Specific part for workaround */
		
		private static int CHECK_EVENT_INTERVAL = 6000*2;
		private Position eventPosition;
		/*
		 * This method is a kind of workaround for this problem.
		 * Here, we are simulating the occurrence of an event of interest.
		 * So, the UAV is doing something. In this case we are faking an event handling.
		 * 
		 * The justification for using it is the fact that the UAV might receive a mission,
		 * or any other kind of task while it is flying over an area.
		 */
		private void moveSomewhereFakingAnEvent() {
			SortedMap<NodeId, Node> allNodes = SimulationManager.getAllNodes();
			int nodeToVisitIndex = new Random().nextInt(allNodes.size());
			
			Node[] nodes = allNodes.values().toArray(new Node[0]);
			Node nodeToVisit = nodes[nodeToVisitIndex];
			CoverageInformation fakeCover = new CoverageInformation(nodeToVisit.getId(), nodeToVisit.getPosition(), 0,true);
			
			this.coveringPath.add(0, fakeCover);
			
			WakeUpCall checkEventWuc = new CheckEventWakeUpCall(this.getSender(), CHECK_EVENT_INTERVAL);
			this.sendEventSelf(checkEventWuc);
		}
	
	/* End of workaround*/
	/*****************************************************************************************************/
}
