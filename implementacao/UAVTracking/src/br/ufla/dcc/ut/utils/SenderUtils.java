package br.ufla.dcc.ut.utils;

import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.user.SendDelayedWakeUp;
import br.ufla.dcc.grubix.simulator.node.ApplicationLayer;

public class SenderUtils {
	
	private ApplicationLayer applicationLayer;
	
	public SenderUtils(ApplicationLayer applicationLayer){
		this.applicationLayer = applicationLayer;
	}

    public void sendDelayed(Packet pkt, double delay){
    	SendDelayedWakeUp w = new SendDelayedWakeUp(this.applicationLayer.getSender(),delay,pkt);
    	this.applicationLayer.sendEventSelf(w);
    }
	
}
