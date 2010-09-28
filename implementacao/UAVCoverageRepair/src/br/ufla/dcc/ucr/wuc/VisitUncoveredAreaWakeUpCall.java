package br.ufla.dcc.ucr.wuc;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;

public class VisitUncoveredAreaWakeUpCall extends WakeUpCall {

	public VisitUncoveredAreaWakeUpCall(Address sender, double delay) {
		super(sender, delay);
	}

}
