package br.ufla.dcc.ut.packet;

import java.util.ArrayList;
import java.util.List;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.event.ApplicationPacket;

public class DisseminationAgentPacket extends ApplicationPacket {
	
	//Goodness da regiao;
	private double RegionGoodness;
	//Lista de vizinhos do vizinho que mandou;
	private List<NodeId> IdNeigh;
	
	private int hopCount;
	
	
	public List<NodeId> getIdNeigh() {
		return IdNeigh;
	}


	public DisseminationAgentPacket(Address sender, NodeId receiver, List<NodeId> IdNeigh, int hc) {
		super(sender, receiver);
		this.IdNeigh = new ArrayList<NodeId>(); 
		this.IdNeigh.addAll(IdNeigh);
		this.hopCount = hc;
	}	


	public int getHop() {
		return hopCount;
	}


	public double getRegionGoodness() {
		return RegionGoodness;
	}

	public void setRegionGoodness(double regionGoodness) {
		RegionGoodness = regionGoodness;
	}

}
