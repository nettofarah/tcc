package br.ufla.dcc.ut.node;

import java.util.logging.Logger;

import br.ufla.dcc.grubix.simulator.event.Initialize;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.StartSimulation;
import br.ufla.dcc.grubix.simulator.event.TrafficGeneration;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.node.ApplicationLayer;
import br.ufla.dcc.ut.command.Command;
import br.ufla.dcc.ut.command.CommandFactory;
import br.ufla.dcc.ut.command.impl.ReflectionCommandFactory;
import br.ufla.dcc.ut.uav.UAV;
import br.ufla.dcc.ut.utils.Converter;

public class GenericApplicationLayer extends ApplicationLayer {
	
	private CommandFactory commandFactory = new ReflectionCommandFactory();

	private static int overallSentPackets = 0;
	private static int overallReceivedPackets = 0;
	private int receivedPackets = 0;
	

	private static final Logger LOGGER = Logger.getLogger(UAV.class.getName());
	
	public void processEvent(Initialize init) {
		this.commandFactory.buildFactory(this);
		SimulationManager.logNodeState(this.node.getId(), "TypeOfNode", "int", Converter.convertNodeNameToType(this.node));
	}

	@Override
	public void lowerSAP(Packet packet) {
		try {
			Command packetCommand = commandFactory.getPacketCommand(packet);
			packetCommand.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		overallReceivedPackets++;
		receivedPackets++;
	}
	
	public void processWakeUpCall(WakeUpCall wuc){
		try {
			Command wakeUpCallCommand = commandFactory.getWakeUpCallCommand(wuc);
			wakeUpCallCommand.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getPacketTypeCount() {
		return 1;
	}

	@Override
	public void processEvent(TrafficGeneration tg) {
		// TODO Auto-generated method stub
		
	}
}
