package br.ufla.dcc.mu.wuc;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;

public class UnjoinPropagationModeWakeUpCall extends WakeUpCall {

	private NodeId nodeToStopPropagating;
	
	public UnjoinPropagationModeWakeUpCall(Address sender, double delay, NodeId nodeToStopPropagating) {
		super(sender, delay);
		this.nodeToStopPropagating = nodeToStopPropagating;
	}
	
	public NodeId getNodeToStopPropagating(){
		return this.nodeToStopPropagating;
	}

}
