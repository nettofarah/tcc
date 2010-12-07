package br.ufla.dcc.mu.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import com.apple.eawt.Application;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.LayerException;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.StartSimulation;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.node.ApplicationLayer;
import br.ufla.dcc.grubix.simulator.node.Layer;
import br.ufla.dcc.grubix.simulator.node.Node;
import br.ufla.dcc.mu.packet.AlarmPacket;
import br.ufla.dcc.mu.packet.PheromonePacket;
import br.ufla.dcc.mu.packet.TrailPropagationPacket;
import br.ufla.dcc.mu.utils.Alarm;
import br.ufla.dcc.mu.utils.Pheromone;
import br.ufla.dcc.mu.utils.Transponder;
import br.ufla.dcc.mu.wuc.MeasureWakeUpCall;
import br.ufla.dcc.mu.wuc.PheromoneDecreaseWakeUpCall;

public class RegularNode extends GenericNode {
	
	private static final int PHEROMONE_DECREASE_TIME = 600;
	public static  int SENT_TO_UAV = 0;
	
	private static final int MEASURE_INTERVAL = 600;
	private static final double TRANSPONDER_RANGE = 8.0; 
	
	public static boolean isTracking = false;
	
	private Map<NodeId, Pheromone> storedPheromones = new HashMap<NodeId, Pheromone>();
	
	private Random random = new Random();
	private Transponder transponder;
	
	public void processEvent(StartSimulation start){
		this.decreasePheromone();
		this.setupTransponder();
		this.scheduleMeasure();
	}
	
	private void scheduleMeasure() {
		WakeUpCall wuc = new MeasureWakeUpCall(getSender(), MEASURE_INTERVAL);
		this.sendEventSelf(wuc);
	}
	

	private void setupTransponder() {
		this.transponder = new Transponder();
		this.transponder.setMe(this.getNode());
		this.transponder.setAllNodes(SimulationManager.getAllNodes().values());
	}

	public void processWakeUpCall(WakeUpCall wuc){
		super.processWakeUpCall(wuc);
		
		if (wuc instanceof PheromoneDecreaseWakeUpCall)
			this.decreasePheromone();
		
		if (wuc instanceof MeasureWakeUpCall)
			this.measureEnvironment();
	}
	

	private void measureEnvironment() {
		List<Node> nodesInTheRange = this.transponder.getNodesInTheRange(TRANSPONDER_RANGE);
		for (Node node : nodesInTheRange) {
			if (node.getNodeName().equals("INTRUDER")){
				if (!isTracking)
					this.warnNeighborsAbout(node);
			}
		}
		this.scheduleMeasure();
	}

	private void warnNeighborsAbout(Node node) {
		Intruder intruder = (Intruder) node.getLayer(getLayerType());
		NodeId uavForThisKindOfEvent = this.getUAVsAsList().get(intruder.getKind());
		
		boolean containsThePheromoneFlavor = this.storedPheromones.containsKey(uavForThisKindOfEvent);
		
		if(containsThePheromoneFlavor){
			Alarm alarm = new Alarm(this.storedPheromones.get(uavForThisKindOfEvent), uavForThisKindOfEvent, node.getPosition(), SimulationManager.getInstance().getCurrentTime());
			this.forwardAlarm(alarm);
			
			isTracking = true;
		}
	}

	private void forwardAlarm(Alarm alarm) {
		SimulationManager.logNodeState(this.getNode().getId(), "Forwarding", "int", "40");
		Packet packet = new AlarmPacket(this.getSender(), NodeId.ALLNODES, alarm);
		this.sendPacket(packet);
	}

	private List<NodeId> getUAVsAsList(){
		List<NodeId> uavs = new ArrayList<NodeId>();
		
		SortedMap<NodeId, Node> allNodes = SimulationManager.getAllNodes();
		for (NodeId id : allNodes.keySet()) {
			Node node = allNodes.get(id);
			Layer layer = node.getLayer(getLayerType());
			if (layer instanceof UAV)
				uavs.add(id);
		}
		return uavs;
	}
	
	@Override
	public void lowerSAP(Packet packet) throws LayerException {
		if (packet instanceof PheromonePacket)
			this.processPheromonePacket((PheromonePacket) packet);
		
		if (packet instanceof AlarmPacket)
			this.processAlarmPacket((AlarmPacket) packet);
	}

	

	private void processAlarmPacket(AlarmPacket packet) {
		Alarm receivedAlarm = packet.getAlarm();
		NodeId flavor = receivedAlarm.getFlavor();
		boolean containsThisFlavor = this.storedPheromones.containsKey(flavor);
		
		if(containsThisFlavor){
			Pheromone myPheromone = this.storedPheromones.get(flavor);
			if (myPheromone.get() > receivedAlarm.getPheromone().get()){
				this.updateAndForwardAlarm(receivedAlarm,myPheromone);
			}
		}
	}

	private void updateAndForwardAlarm(Alarm receivedAlarm, Pheromone pheromone) {
		Alarm updateAlarm = new Alarm(pheromone, receivedAlarm.getFlavor(), receivedAlarm.getPosition(), receivedAlarm.getTimeStamp());
		this.forwardAlarm(updateAlarm);
	}

	private void processPheromonePacket(PheromonePacket packet){
		NodeId uav = packet.getUavId();
		this.storePheromone(uav);
		
		List<NodeId> otherTrails = this.otherTrails(uav);
		if(otherTrails.size() > 0 && random.nextInt(10) > 7)
			this.propagateTrails(uav,otherTrails);
		
		Map<NodeId, Double> friendlyPheromones = packet.getFriendlyFlavors();
		this.storeFriendlyPheromones(friendlyPheromones, packet.getSender());
	}
	
	private void storeFriendlyPheromones(Map<NodeId, Double> friendlyPheromones, Address propagator) {
		for (NodeId flavor : friendlyPheromones.keySet()) {
			Pheromone currentPheromone = storedPheromones.get(flavor);
			if (currentPheromone == null) currentPheromone = new Pheromone(flavor);
			
			Double receivedAmmount = friendlyPheromones.get(flavor);
			
			if (receivedAmmount > currentPheromone.get() ){
				this.storePheromone(flavor, propagator.getId(), receivedAmmount);
			}
		}
	}

	
	private void storePheromone(NodeId flavor, NodeId propagator, double rate){
		Position uavPosition = SimulationManager.getAllNodes().get(propagator).getPosition();
		Position myPosition = this.getNode().getPosition();

		double distance = myPosition.getDistance(uavPosition);
		distance = distance/10;//Scales 
		distance = Math.floor(distance);
		double pheromoneAmmount = 1/(Math.pow(2, distance)); //Inverse Exponential.. calculates the value
		
		pheromoneAmmount = pheromoneAmmount * rate;

		Pheromone pheromone = this.storedPheromones.get(flavor);
		if (pheromone==null){
			pheromone = new Pheromone(flavor);
			this.storedPheromones.put(flavor,pheromone);
		}  
				
		pheromone.set(pheromoneAmmount);   
		pheromone.update_view(this.getId());
	}
	
	private void storePheromone(NodeId flavor){
		this.storePheromone(flavor, flavor, 1);
	}
	
	private void propagateTrails(NodeId propagator, List<NodeId> otherTrails){
		Map<NodeId,Double> trailsWithAmmount = new HashMap<NodeId, Double>();
		for (NodeId flavor : otherTrails) {
			Pheromone pheromone = this.storedPheromones.get(flavor);
			trailsWithAmmount.put(flavor, pheromone.get());
		}
		
		Packet trailPropagationPacket = new TrailPropagationPacket(this.getSender(), NodeId.ALLNODES, trailsWithAmmount);
		this.sendDelayed(trailPropagationPacket, random.nextInt(30));
		SENT_TO_UAV++;
	}
	

	private List<NodeId> otherTrails(NodeId receivedFlavor){
		List<NodeId> otherTrails = new ArrayList<NodeId>();
		Set<NodeId> flavors = this.storedPheromones.keySet();
		
		for (NodeId flavor : flavors) {
			if (flavor.asInt() != receivedFlavor.asInt())
				otherTrails.add(flavor);
		}
		
		return otherTrails;
	}
	
	private void decreasePheromone(){
		
		List<NodeId> flavorsToRemove = new ArrayList<NodeId>();
		
		Set<NodeId> flavors = this.storedPheromones.keySet();
		for (NodeId flavor : flavors) {
			Pheromone pheromone = this.storedPheromones.get(flavor);
			pheromone.update_view(this.getId());
			pheromone.evaporate();
			
			//Removes this flavor of pheromone set.. it no longer exists
			if(pheromone.get() == 0.0){
				flavorsToRemove.add(flavor);
			}
		}
		
		for (NodeId nodeId : flavorsToRemove) {
			this.storedPheromones.remove(nodeId);
		}
		
		PheromoneDecreaseWakeUpCall bw = new PheromoneDecreaseWakeUpCall(this.getSender(),PHEROMONE_DECREASE_TIME);
		this.sendEventSelf(bw);
	}
	
}
