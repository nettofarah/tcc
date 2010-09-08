package br.ufla.dcc.ut.command;

import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.node.ApplicationLayer;

public interface CommandFactory {

	public void buildFactory(ApplicationLayer applicationLayer);
	public Command getPacketCommand(Packet packet) throws  Exception;
	public Command getWakeUpCallCommand(WakeUpCall wakeUpCall) throws  Exception;
}
