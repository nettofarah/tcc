package br.ufla.dcc.ut.event;

import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.node.Node;

public class Alarm{
	
	private Position position;
	private double timeStamp;
	private int type;
	
	
	private static int id_counter = 0;
	
	private int id;
	
	public Position getPosition() {
		return position;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	public double getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(double timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Alarm(Position position, double timeStamp, int type) {
		super();
		this.position = position;
		this.timeStamp = timeStamp;
		this.type = type;
		this.id = id_counter++;
	}
	
	public Alarm(Position position, double timeStamp, int type, int ida) {
		super();
		this.position = position;
		this.timeStamp = timeStamp;
		this.type = type;
		this.id = ida;
	}
}