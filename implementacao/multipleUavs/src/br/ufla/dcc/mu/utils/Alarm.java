package br.ufla.dcc.mu.utils;

import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.Position;

public class Alarm {
	private Pheromone pheromone;
	private Position position;
	private double timeStamp;
	private NodeId flavor;
	
	public Alarm(Pheromone pheromone, NodeId flavor, Position position, double timeStamp){
		this.pheromone = pheromone;
		this.position = position;
		this.timeStamp = timeStamp;
		this.flavor = flavor;
	}
	
	public Pheromone getPheromone() {
		return pheromone;
	}
	public Position getPosition() {
		return position;
	}
	public double getTimeStamp() {
		return timeStamp;
	}
	
	public NodeId getFlavor(){
		return flavor;
	}
}
