package br.ufla.dcc.ucr.node.data;

import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.Position;

public class CoverageInformation implements Comparable<CoverageInformation> {

	private NodeId senderId;
	private Position position;
	private double coverage;
	
	private CoverageInfoComparator compareStrategy = new SortByDistanceFromOrigin();
	
	public CoverageInformation(NodeId senderId, Position position,double coverage) {
		super();
		this.position = position;
		this.senderId = senderId;
		this.coverage = coverage;
	}



	public Position getPosition() {
		return position;
	}


	public NodeId getSenderId() {
		return senderId;
	}


	public double getCoverage() {
		return coverage;
	}

	
	public double distanceFromOrigin(){
		return this.position.getDistance(new Position(0, 0));
	}


	@Override
	public int compareTo(CoverageInformation other) {
		return this.compareStrategy.compare(this, other);
	}

}
