package br.ufla.dcc.ucr.node;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import br.ufla.dcc.grubix.simulator.LayerException;
import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.kernel.Configuration;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.movement.MovementManager;
import br.ufla.dcc.grubix.simulator.node.user.MoveToCommand;
import br.ufla.dcc.ucr.node.data.CoverageInformation;
import br.ufla.dcc.ucr.packet.CoverageAlarmPacket;
import br.ufla.dcc.ucr.wuc.ScheduleCoverageWakeUpCall;
import br.ufla.dcc.ucr.wuc.VisitUncoveredAreaWakeUpCall;

public class old_UAV extends GenericNode {
	
	private static int SCHEDULLING_DELAY = 8000;
	private static int VISIT_AN_UNCOVERED_AREA_DELAY = 20000;

	private SortedSet<CoverageInformation> nodesToCover = new TreeSet<CoverageInformation>();
	private Set<CoverageInformation> coveringPath = new HashSet<CoverageInformation>();
	

	public void processWakeUpCall(WakeUpCall wuc){
		if(wuc instanceof ScheduleCoverageWakeUpCall)
			this.startCovering();
		
		if(wuc instanceof VisitUncoveredAreaWakeUpCall)
			this.visitNextUncoveredArea();
	}
	

	private void visitNextUncoveredArea() {
		boolean isThereAreaToVisit = this.coveringPath.size() > 0;
		if(isThereAreaToVisit){
			CoverageInformation nextToVisit = this.coveringPath.iterator().next();
			this.coveringPath.remove(nextToVisit);		
			
			this.visit(nextToVisit);
		}else{
			this.scheduleCoverage();
		}
	}

	

	private void visit(CoverageInformation nextToVisit) {
		Position targetArea = nextToVisit.getPosition();
		this.move(targetArea);
		
		SimulationManager.logNodeState(this.getId(), "Moving To", "int", nextToVisit.getSenderId()+"");
		
		this.scheduleACover();
	}

	private void move(Position target){
		MoveToCommand moveCommand = new MoveToCommand();
		moveCommand.setId(this.getId());
		
		moveCommand.setTarget(target);
		
		MovementManager movementManager = Configuration.getInstance().getMovementManager();
		movementManager.sendCommand(moveCommand);
	}

	private void scheduleCoverage() {
		WakeUpCall scwuc = new ScheduleCoverageWakeUpCall(this.getSender(),SCHEDULLING_DELAY * Math.random());	
		this.sendEventSelf(scwuc);
	}

	@Override
	public void lowerSAP(Packet packet) throws LayerException {
		if (packet instanceof CoverageAlarmPacket){
			CoverageAlarmPacket cPacket = (CoverageAlarmPacket) packet;
			this.cover(cPacket.getCoverageInfo());
		}
	}


	private void cover(CoverageInformation coverageInfo) {
		this.checkIfIsAlreadyCovering(coverageInfo);
		this.addNodeToCover(coverageInfo);
		
		if(isTimeToStartANewCovering()){
			this.scheduleCoverage();
		}
		
		SimulationManager.logNodeState(this.getId(), "CoverageList", "int", this.nodesToCover.size()+"");
	}
	
	private void checkIfIsAlreadyCovering(CoverageInformation coverInfo){
		CoverageInformation oldInfo = this.alreadyCoverThisNode(coverInfo);
		if(oldInfo != null)
			this.removeNodeToCover(oldInfo);
	}
	
	private void addNodeToCover(CoverageInformation coverInfo){
		this.nodesToCover.add(coverInfo);
		this.coveringPath.add(coverInfo);
	}
	
	private boolean isTimeToStartANewCovering(){
		boolean isTheFirstNodeToCover = this.nodesToCover.size() == 1;
		//boolean coveringPathOver = this.coveringPath.size() == 0;
		
		//return isTheFirstNodeToCover || coveringPathOver;
		return isTheFirstNodeToCover;
	}
	
	private void removeNodeToCover(CoverageInformation oldInfo){
		this.nodesToCover.remove(oldInfo);
		this.coveringPath.remove(oldInfo);
	}

	private CoverageInformation alreadyCoverThisNode(CoverageInformation coverageInfo){
		for (CoverageInformation cInfo : this.nodesToCover) {
			if (cInfo.getSenderId() == coverageInfo.getSenderId())
				return cInfo;
		}
		return null;
	}

	private void startCovering() {
		this.generateCoveringPath();
		this.scheduleACover();
	}
	


	private void scheduleACover() {
		WakeUpCall wuc = new VisitUncoveredAreaWakeUpCall(this.getSender(), VISIT_AN_UNCOVERED_AREA_DELAY * Math.random());
		this.sendEventSelf(wuc);
	}


	private void generateCoveringPath(){
		this.coveringPath.clear();
		this.coveringPath.addAll(this.nodesToCover);
	}
	
}
