package br.ufla.dcc.ucr.wuc;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;

public class ScheduleCoverageWakeUpCall extends WakeUpCall {

	public ScheduleCoverageWakeUpCall(Address sender, double delay) {
		super(sender, delay);
	}

}
