package br.ufla.dcc.ut.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.node.Node;

public class NodeUtils {

	//this is not the correct way of doing this, but in this problem we can use this shortcut
	public static List<Node> findUavs(){
		List<Node> uavs = new ArrayList<Node>();
		
		SortedMap<NodeId, Node> allNodes = SimulationManager.getAllNodes();
		Set<NodeId> ids = allNodes.keySet();
		
		for (NodeId nodeId : ids) {
			Node node = allNodes.get(nodeId);
			if (node.getNodeName().equals("UAV"))
				uavs.add(node);
		}
		
		return uavs;
	}
	
}
