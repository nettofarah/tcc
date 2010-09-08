package br.ufla.dcc.ut.command.wuc;

import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.ut.command.impl.WakeUpCallHandler;
import br.ufla.dcc.ut.node.RegularNode;
import br.ufla.dcc.ut.statistics.Statistics;
import br.ufla.dcc.ut.uav.UAV;
import br.ufla.dcc.ut.uav.packet.PheromonePacket;
import br.ufla.dcc.ut.uav.wuc.LayPheromoneWakeUpCall;

@WakeUpCallHandler(LayPheromoneWakeUpCall.class)
public class LayPheromoneWakeUpCallCommand extends WakeUpCallCommand {

	@Override
	public void execute() {
		PheromonePacket packet = new PheromonePacket(this.applicationLayer.getSender(), NodeId.ALLNODES);
		this.applicationLayer.sendPacket(packet);
		Statistics.numberOfMessagesSent++;
		
		boolean isWarned = UAV.isWarned;
		if(!isWarned){
			LayPheromoneWakeUpCall wuc = new LayPheromoneWakeUpCall(this.applicationLayer.getSender(),100);
			this.applicationLayer.sendEventSelf(wuc);			
		}
		
	}

}
