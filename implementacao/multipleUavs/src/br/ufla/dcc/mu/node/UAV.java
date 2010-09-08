package br.ufla.dcc.mu.node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.ufla.dcc.grubix.simulator.LayerException;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.StartSimulation;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.mu.packet.PheromonePacket;
import br.ufla.dcc.mu.packet.TrailPropagationPacket;
import br.ufla.dcc.mu.wuc.LayPheromoneWakeUpCall;

public class UAV extends GenericNode {
	
	private static final int LAY_PHEROMONE_DELAY = 800;
	private static final double FRIENDLY_PHEROMONES_DECREASE_RATE = .1;
	
	public static  int RECEIVED_FROM_NODE = 0;
	
	private Map<NodeId,Double> friendlyPheromones = new HashMap<NodeId, Double>();

	public void processEvent(StartSimulation start){
		this.layPheromone();
	}
	
	@Override
	public void lowerSAP(Packet packet) throws LayerException {
		if(packet instanceof TrailPropagationPacket)
			this.propagateTrails( (TrailPropagationPacket) packet);
	}

	public void processWakeUpCall(WakeUpCall wuc){
		super.processWakeUpCall(wuc);
		
		if (wuc instanceof LayPheromoneWakeUpCall)
			this.layPheromone();
	}
	
	private void layPheromone(){
		this.decreaseFriendlyPheromones(FRIENDLY_PHEROMONES_DECREASE_RATE);
		
		Packet pheromonePacket = new PheromonePacket(this.getSender(), NodeId.ALLNODES,this.friendlyPheromones);
		this.sendPacket(pheromonePacket);
		
		WakeUpCall whenLayPheromoneAgain = new LayPheromoneWakeUpCall(this.getSender(), LAY_PHEROMONE_DELAY);
		this.sendEventSelf(whenLayPheromoneAgain);
	}
	
	private void decreaseFriendlyPheromones(double rate) {
		Set<NodeId> flavorsToRemove = new HashSet<NodeId>();
		
		Set<NodeId> friendlyFlavors = this.friendlyPheromones.keySet();
		for (NodeId flavor : friendlyFlavors) {
			Double ammount = this.friendlyPheromones.get(flavor);
			ammount -= rate;
			
			if (ammount <= 0){
				flavorsToRemove.add(flavor);
			}else{
				this.friendlyPheromones.put(flavor, ammount);
			}
		}
		
		for (NodeId flavor : flavorsToRemove) {
			this.friendlyPheromones.remove(flavor);
		}
	}

	private void propagateTrails(TrailPropagationPacket packet){
		SimulationManager.logNodeState(this.getId(), "asked to propagate trails", "int", "10");
		this.updateFriendlyPheromones(packet.getFlavorsToPropagate());
		RECEIVED_FROM_NODE++;
	}
	
	private void updateFriendlyPheromones(Map<NodeId,Double> trails){
		Set<NodeId> flavors = trails.keySet();
		for (NodeId flavor : flavors) {
			
			Double currentAmmount = this.friendlyPheromones.get(flavor);
			if (currentAmmount == null) currentAmmount = .0;
			Double ammountToStore = currentAmmount;
			
			Double receivedAmmount = trails.get(flavor);
			if (receivedAmmount > currentAmmount)
				ammountToStore = receivedAmmount;
			
			this.friendlyPheromones.put(flavor, ammountToStore);
		}
	}
}
