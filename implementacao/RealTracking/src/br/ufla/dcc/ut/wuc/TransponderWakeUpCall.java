package br.ufla.dcc.ut.wuc;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;

public class TransponderWakeUpCall extends WakeUpCall {

	public TransponderWakeUpCall(Address sender) {
		super(sender);
		// TODO Auto-generated constructor stub
	}

	public TransponderWakeUpCall(Address sender, double delay) {
		super(sender,delay);
	}

}
