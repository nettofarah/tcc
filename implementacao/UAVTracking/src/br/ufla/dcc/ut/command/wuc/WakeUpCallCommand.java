package br.ufla.dcc.ut.command.wuc;

import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.node.ApplicationLayer;
import br.ufla.dcc.ut.command.Command;

public abstract class WakeUpCallCommand implements Command {

	protected ApplicationLayer applicationLayer;
	protected WakeUpCall wakeUpCall;
	
	public void setApplicationLayer(ApplicationLayer applicationLayer) {
		this.applicationLayer = applicationLayer;
	}
	public void setWakeUpCall(WakeUpCall wakeUpCall) {
		this.wakeUpCall = wakeUpCall;
	}
}
