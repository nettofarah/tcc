package br.ufla.dcc.mu.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.LayerException;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.StartSimulation;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.mu.packet.PheromonePacket;
import br.ufla.dcc.mu.packet.TrailPropagationPacket;
import br.ufla.dcc.mu.utils.Pheromone;
import br.ufla.dcc.mu.wuc.PheromoneDecreaseWakeUpCall;

public class RegularNode extends GenericNode {
	
	private static final int PHEROMONE_DECREASE_TIME = 600;
	public static  int SENT_TO_UAV = 0;
	
	private Map<NodeId, Pheromone> storedPheromones = new HashMap<NodeId, Pheromone>();
	
	private Random random = new Random();
	
	public void processEvent(StartSimulation start){
		this.decreasePheromone();
	}
	
	public void processWakeUpCall(WakeUpCall wuc){
		super.processWakeUpCall(wuc);
		
		if (wuc instanceof PheromoneDecreaseWakeUpCall)
			this.decreasePheromone();
	}
	

	@Override
	public void lowerSAP(Packet packet) throws LayerException {
		if (packet instanceof PheromonePacket)
			this.processPheromonePacket((PheromonePacket) packet);
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
		//this.sendPacket(trailPropagationPacket);
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
