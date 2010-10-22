package br.ufla.dcc.ut.command.packet;

import br.ufla.dcc.ut.command.impl.PacketHandler;
import br.ufla.dcc.ut.node.RegularNode;
import br.ufla.dcc.ut.node.packet.JoinTrackingModePacket;

@PacketHandler(JoinTrackingModePacket.class)
public class JoinTrackingModePacketCommand extends PacketCommand {

	@Override
	public void execute() {
		RegularNode node = (RegularNode) this.applicationLayer;
		JoinTrackingModePacket packet = (JoinTrackingModePacket) this.packet;
		double timeToTrack = packet.getTimeToTrack();
		
		node.joinTrackingMode(timeToTrack);
	}

}
