package br.ufla.dcc.mu.packet;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.event.ApplicationPacket;
import br.ufla.dcc.mu.utils.Alarm;

public class AlarmPacket extends ApplicationPacket {

	private Alarm alarm;
	
	public AlarmPacket(Address sender, NodeId receiver, Alarm alarm) {
		super(sender, receiver);
		this.alarm = alarm;
	}
	
	public void setAlarm(Alarm alarm){
		this.alarm = alarm;
	}

	public Alarm getAlarm() {
		return alarm;
	}
}
