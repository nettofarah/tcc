package br.ufla.dcc.ut.zmess;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.LayerType;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.ut.command.Command;
import br.ufla.dcc.ut.command.CommandFactory;
import br.ufla.dcc.ut.command.impl.ReflectionCommandFactory;
import br.ufla.dcc.ut.uav.UAV;
import br.ufla.dcc.ut.uav.packet.PheromonePacket;
import br.ufla.dcc.ut.uav.wuc.LayPheromoneWakeUpCall;

public class Test {

	public static void main(String[] args) throws Exception {
		
		UAV uav = new UAV();
		
		CommandFactory factory = new ReflectionCommandFactory();
		factory.buildFactory(uav);
		
		LayPheromoneWakeUpCall wuc = new LayPheromoneWakeUpCall(new Address(new NodeId(), LayerType.AIR));
		
		Command wakeUpCallCommand = factory.getWakeUpCallCommand(wuc);
		
		wakeUpCallCommand.execute();
		
		/***************************************************************************************************************/
		
		PheromonePacket pkt = new PheromonePacket(new Address(new NodeId(), LayerType.AIR), new NodeId());
		
		Command packetCommand = factory.getPacketCommand(pkt);
		packetCommand.execute();
	}
	
}
