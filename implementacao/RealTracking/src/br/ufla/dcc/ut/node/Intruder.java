package br.ufla.dcc.ut.node;

import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;

public class Intruder extends GenericNode {

	@Override
	public void lowerSAP(Packet packet) {
		//do nothing
	}
	
	@Override
	public void processWakeUpCall(WakeUpCall wuc){
		//do nothing
	}

}
