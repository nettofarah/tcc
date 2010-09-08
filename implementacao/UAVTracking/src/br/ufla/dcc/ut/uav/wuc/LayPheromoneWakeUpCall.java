package br.ufla.dcc.ut.uav.wuc;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;

public class LayPheromoneWakeUpCall extends WakeUpCall {

	public LayPheromoneWakeUpCall(Address sender, double delay){
		super(sender,delay);
	}
	
	public LayPheromoneWakeUpCall(Address sender) {
		super(sender);
		// TODO Auto-generated constructor stub
	}

}
