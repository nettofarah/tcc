package br.ufla.dcc.ut.uav;

import java.util.logging.Logger;

import br.ufla.dcc.grubix.simulator.event.Finalize;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.StartSimulation;
import br.ufla.dcc.grubix.simulator.event.TrafficGeneration;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.node.ApplicationLayer;
import br.ufla.dcc.ut.command.Command;
import br.ufla.dcc.ut.command.CommandFactory;
import br.ufla.dcc.ut.command.impl.ReflectionCommandFactory;
import br.ufla.dcc.ut.node.GenericApplicationLayer;
import br.ufla.dcc.ut.statistics.Statistics;
import br.ufla.dcc.ut.uav.wuc.LayPheromoneWakeUpCall;
import br.ufla.dcc.ut.utils.Converter;

public class UAV extends GenericApplicationLayer {

	public static boolean isWarned = false;
	
	public void processEvent(StartSimulation start){
		LayPheromoneWakeUpCall wuc = new LayPheromoneWakeUpCall(this.getSender(),100 * Math.random());
		this.sendEventSelf(wuc);
	}
	
	public void processEvent(Finalize finalize) {	
		Statistics.Generate();
	}
}
