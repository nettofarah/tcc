package br.ufla.dcc.ut.command.packet;

import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.node.ApplicationLayer;
import br.ufla.dcc.ut.command.Command;

public abstract class PacketCommand implements Command{

	protected ApplicationLayer applicationLayer;
	protected Packet packet;
	
	public void setApplicationLayer(ApplicationLayer applicationLayer) {
		this.applicationLayer = applicationLayer;
	}
	public void setPacket(Packet packet) {
		this.packet = packet;
	}
	
}
