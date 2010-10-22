package br.ufla.dcc.ut.node;

import br.ufla.dcc.grubix.simulator.LayerException;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.event.Initialize;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.ut.packet.PheromonePacket;
import br.ufla.dcc.ut.wuc.LayPheromoneWakeUpCall;


public class UAV extends GenericNode {

	public static boolean isWarned = false;
	
	@Override
	public void processEvent(Initialize init) {
		super.processEvent(init);	
		this.sendEventSelf(new LayPheromoneWakeUpCall(getSender(), 100));
	}
	
	public void processWakeUpCall(WakeUpCall wuc){
		if (wuc instanceof LayPheromoneWakeUpCall)
			this.layPheromone();
	}
	
	
	public void layPheromone(){
		Packet pheromonePacket = new PheromonePacket(this.getSender(), NodeId.ALLNODES);
		this.sendPacket(pheromonePacket);
		
		this.sendEventSelf(new LayPheromoneWakeUpCall(this.getSender(), Math.random()*200));
	}

	@Override
	public void lowerSAP(Packet packet) throws LayerException {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
