package br.ufla.dcc.mu.packet;

import java.util.Map;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.event.Packet;

public class TrailPropagationPacket extends Packet {

	private Map<NodeId,Double> flavorsToPropagate;
	
	public TrailPropagationPacket(Address sender, NodeId receiver, Map<NodeId,Double> flavorsToPropagate) {
		super(sender, receiver);
		this.flavorsToPropagate = flavorsToPropagate;
	}

	public Map<NodeId,Double> getFlavorsToPropagate() {
		return flavorsToPropagate;
	}
}
