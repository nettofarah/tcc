package br.ufla.dcc.ut.command.wuc.uav;

import br.ufla.dcc.ut.command.impl.WakeUpCallHandler;
import br.ufla.dcc.ut.command.wuc.WakeUpCallCommand;
import br.ufla.dcc.ut.uav.wuc.LayPheromoneWakeUpCall;

@WakeUpCallHandler(LayPheromoneWakeUpCall.class)
public class LayPheromoneWakeUpCallCommand extends WakeUpCallCommand {

	@Override
	public void execute() {
		System.out.println("Hey, I am a concrete wakeupcall handler! ;)");
	}

}
