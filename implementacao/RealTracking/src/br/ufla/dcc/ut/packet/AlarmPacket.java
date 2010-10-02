/**
 * 
 */
package br.ufla.dcc.ut.packet;


import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.event.ApplicationPacket;
import br.ufla.dcc.ut.event.Alarm;




/**
 * This class is used to send the alarms issued by the ground nodes when they detect an 
 * enemy. 
 * @author Edison Pignaton de Freitas
 * 
 *
 */
public class AlarmPacket extends ApplicationPacket {
	
	
	/**
	 * The payload of the an AlarmRespondPacket is composed by:
	 * 0: the location (x,y) where the sensor that issued the alarm is located
	 * 1: a timestamp in which the alarm was issued 
	 *    fields 0 and 1 compose the alarm identifier
	 * 2: number of targets detected 
	 * 3: an array with the type of the detected targets
	 */
	private Alarm alarm;
	
	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	/**
	 * Class constructor
	 * @param sender
	 * @param receiver
	 */
	public AlarmPacket(Address sender, NodeId receiver){
		super(sender, receiver);
	}
}
