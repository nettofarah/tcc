package br.ufla.dcc.ut.intruder;

import br.ufla.dcc.grubix.simulator.LayerException;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.TrafficGeneration;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.node.ApplicationLayer;
import br.ufla.dcc.ut.command.Command;
import br.ufla.dcc.ut.node.GenericApplicationLayer;

public class Intruder extends GenericApplicationLayer {

	@Override
	public void lowerSAP(Packet packet) {
		//do nothing
	}
	
	@Override
	public void processWakeUpCall(WakeUpCall wuc){
		//do nothing
	}

}
