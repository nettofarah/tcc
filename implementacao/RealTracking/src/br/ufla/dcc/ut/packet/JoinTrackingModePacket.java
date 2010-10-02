package br.ufla.dcc.ut.packet;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.event.ApplicationPacket;

public class JoinTrackingModePacket extends ApplicationPacket {

	private double timeToTrack;
	
	public JoinTrackingModePacket(Address sender, NodeId receiver, double timeToTrack) {
		super(sender, receiver);
		// TODO Auto-generated constructor stub
		this.timeToTrack = timeToTrack;
	}

	
	public double getTimeToTrack(){
		return this.timeToTrack;
	}
}
