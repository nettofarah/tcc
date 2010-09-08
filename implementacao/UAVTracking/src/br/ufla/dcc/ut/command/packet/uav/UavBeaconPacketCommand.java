package br.ufla.dcc.ut.command.packet.uav;

import br.ufla.dcc.ut.command.impl.PacketHandler;
import br.ufla.dcc.ut.command.packet.PacketCommand;
import br.ufla.dcc.ut.uav.packet.UavBeaconPacket;

@PacketHandler(UavBeaconPacket.class)
public class UavBeaconPacketCommand extends PacketCommand {

	@Override
	public void execute() {
		System.out.println("Hello!! I am an uavBeaconPacket (= (= ");
	}
	

}
