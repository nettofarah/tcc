package br.ufla.dcc.mu.packet;

import java.util.Map;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.event.Packet;

public class PheromonePacket extends Packet {

	private NodeId uavId;
	private Map<NodeId, Double> friendlyFlavors;
	
	public PheromonePacket(Address sender, NodeId receiver, Map<NodeId,Double> friendlyFlavors) {
		super(sender, receiver);
		this.uavId = sender.getId();
		this.friendlyFlavors = friendlyFlavors;
	}

	
	public NodeId getUavId(){
		return this.uavId;
	}


	public Map<NodeId, Double> getFriendlyFlavors() {
		return friendlyFlavors;
	}
	
}
