package br.ufla.dcc.mu.utils;

import br.ufla.dcc.grubix.simulator.node.Node;

public class Converter {

	public static String convertNodeNameToType(Node node){
		String nodeName = node.getNodeName();
		String result = "";
		if (nodeName.equals("UAV")){
			result = "1";
		}
		if (nodeName.equals("REGULAR")){
			result = "2";
		}
		
		return result;
	}
	
}
