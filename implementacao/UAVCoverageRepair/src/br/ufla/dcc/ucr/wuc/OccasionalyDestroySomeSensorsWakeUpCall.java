package br.ufla.dcc.ucr.wuc;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;

public class OccasionalyDestroySomeSensorsWakeUpCall extends WakeUpCall {

	public OccasionalyDestroySomeSensorsWakeUpCall(Address sender, double delay) {
		super(sender,delay);
	}

}
