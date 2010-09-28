package br.ufla.dcc.ucr.packet;

import br.ufla.dcc.grubix.simulator.Address;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.event.ApplicationPacket;
import br.ufla.dcc.ucr.node.data.CoverageInformation;

public class CoverageAlarmPacket extends ApplicationPacket {

	private CoverageInformation coverageInfo;

	public CoverageAlarmPacket(Address sender, NodeId receiver, Position position, double nodeCoverage) {
		super(sender, receiver);
		this.coverageInfo = new CoverageInformation(this.getSender().getId(), position, nodeCoverage);
	}

	public CoverageInformation getCoverageInfo() {
		return coverageInfo;
	}	
}
