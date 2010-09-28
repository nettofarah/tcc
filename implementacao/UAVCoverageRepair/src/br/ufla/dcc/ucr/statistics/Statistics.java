package br.ufla.dcc.ucr.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.ucr.node.UAV;



public class Statistics {

	private static Map<NodeId, Double> problemStarted = new HashMap<NodeId, Double>();
	private static Map<NodeId, Integer> nodesReached = new HashMap<NodeId, Integer>();
	private static Map<NodeId, Double> firstTimeReached = new HashMap<NodeId, Double>();
	
	private static Map<NodeId, Double> eventOcurred = new HashMap<NodeId, Double>();
	
	private static List<Double> cyclesTime = new ArrayList<Double>();
	
	//any time a nod has a problem
	public static void logProblem(NodeId id){
		problemStarted.put(id, getCurrentTime());
		System.out.println("Problem Found ("+id+") -> "+ getCurrentTime());
	}
	
	//any time a node is reached by an UAV
	public static void logCovered(NodeId id){		
		if (!nodesReached.containsKey(id)){
			firstTimeReached.put(id, getCurrentTime());
			System.out.println("FirstCovered ("+id+") -> "+ getCurrentTime());
		}
		incrementTime(id);
	}

	
	private static void incrementTime(NodeId id) {
		if (!nodesReached.containsKey(id)){
			nodesReached.put(id, 1);
		}else{
			nodesReached.put(id, nodesReached.get(id)+1);
		}
	}

	public static Map<NodeId, Double> getTimeForFirstCover(){
		Map<NodeId, Double> result = new HashMap<NodeId, Double>();
		
		for (NodeId id : problemStarted.keySet()){
			result.put(id, firstTimeReached.get(id) - problemStarted.get(id));
		}
		
		return result;
	}
	
	public static void startCycle(){
		cyclesTime.add(getCurrentTime());
		System.out.println("Cycle Started --> "+getCurrentTime());
	}
	
	public static List<Double> allCycles(){
		return cyclesTime;
	}
	
	private static double getCurrentTime(){
		return SimulationManager.getInstance().getCurrentTime();
	}
	
	public static Map<NodeId, Double> getCoverageRate(){
		Map<NodeId, Double> result = new HashMap<NodeId, Double>();
		
		for (NodeId id: firstTimeReached.keySet()){
			double firstTimeNodeReached = firstTimeReached.get(id);
			double finalTime = getCurrentTime();
			
			result.put(id, nodesReached.get(id) * UAV.BEACON_INTERVAL / (finalTime - firstTimeNodeReached));
		}
		
		return result;
	}
}
