package br.ufla.dcc.mu.packet;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.event.Packet;

public class JoinPropagationModePacket extends Packet {

	private NodeId propagator;
	private int timeInThisMode;
	
	public JoinPropagationModePacket(Address sender, NodeId receiver, NodeId propagator, int timeInThisMode) {
		super(sender, receiver);
		this.propagator = propagator;
		this.timeInThisMode = timeInThisMode;
	}
	
	public NodeId getPropagator(){
		return this.propagator;
	}

	public int getTimeInThisMode(){
		return this.timeInThisMode;
	}
}
