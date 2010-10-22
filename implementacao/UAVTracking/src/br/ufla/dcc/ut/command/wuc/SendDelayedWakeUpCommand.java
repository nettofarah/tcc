package br.ufla.dcc.ut.command.wuc;

import br.ufla.dcc.grubix.simulator.event.user.SendDelayedWakeUp;
import br.ufla.dcc.ut.command.impl.WakeUpCallHandler;

@WakeUpCallHandler(SendDelayedWakeUp.class)
public class SendDelayedWakeUpCommand extends WakeUpCallCommand {

	@Override
	public void execute() {
		SendDelayedWakeUp wuc = (SendDelayedWakeUp)this.wakeUpCall;
		this.applicationLayer.sendPacket(wuc.getPkt());
	}

}
