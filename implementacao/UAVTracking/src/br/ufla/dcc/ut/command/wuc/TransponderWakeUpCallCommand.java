package br.ufla.dcc.ut.command.wuc;

import java.util.List;

import br.ufla.dcc.grubix.simulator.node.Node;
import br.ufla.dcc.ut.command.impl.WakeUpCallHandler;
import br.ufla.dcc.ut.node.RegularNode;
import br.ufla.dcc.ut.node.wuc.TransponderWakeUpCall;

@WakeUpCallHandler(TransponderWakeUpCall.class)
public class TransponderWakeUpCallCommand extends WakeUpCallCommand {

	@Override
	public void execute() {
		RegularNode regularNode = (RegularNode) this.applicationLayer;
		
		List<Node> nodesInTheRange = regularNode.transponder.getNodesInTheRange(regularNode.transponderRange);
		for (Node node : nodesInTheRange) {
			if (node.getNodeName().equals("INTRUDER")){ //means that node is a intruder
				regularNode.alertNeighbors();
			}
		}
		
		boolean isTracking = RegularNode.trackingMode;
		
		if (!isTracking){
			TransponderWakeUpCall twuc = new TransponderWakeUpCall(regularNode.getSender(), 100*Math.random());
			regularNode.sendEventSelf(twuc);			
		}
	}

}
