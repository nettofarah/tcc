package br.ufla.dcc.ut.utils;

import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;

/**
 * 
 * @author Tales Heimfarth, Ivayr Farah Netto
 *
 */

public class Pheromone {
	
	private double value;
	private NodeId id;
	private final double increment = 0.1;
	private final double decrement = 0.001;
	
	public void update_view (NodeId id)
	{
		SimulationManager.logNodeState(id,"Pheromone"+this.id.asInt(), "float", String.valueOf(value));
	}
	
	public Pheromone (NodeId uav)
	{
		this.value = 0.0;
		this.id = uav;
	}
	
	public void set(double d){
		if (this.value < d)
			this.value = d;
	}
	
	public double get(){
		return this.value;
	}
	
	
	public void increase ()
	{
		this.increase(this.increment);
	}
	
	public void increase (double v)
	{
		this.value+=v;
		if (this.value>1.0)
		{
			this.value = 1.0;
		}
	}
	
	public void evaporate ()
	{
		this.evaporate(this.decrement);
	}
	
	public void evaporate (double v)
	{
		this.value-=v;
		if (this.value<0.0)
			this.value = 0.0;
	}
	
	public Pheromone clone(){
		Pheromone clone = new Pheromone(this.id);
		clone.value = this.value;
		
		return clone;
	}
	
}
