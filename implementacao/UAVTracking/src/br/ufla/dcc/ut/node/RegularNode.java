package br.ufla.dcc.ut.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.event.StartSimulation;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.node.Node;
import br.ufla.dcc.ut.node.event.Alarm;
import br.ufla.dcc.ut.node.event.EnemyAlarm;
import br.ufla.dcc.ut.node.packet.AlarmPacket;
import br.ufla.dcc.ut.node.packet.DisseminationAgentPacket;
import br.ufla.dcc.ut.node.packet.JoinTrackingModePacket;
import br.ufla.dcc.ut.node.wuc.LeaveTrackingModeWakeUpCall;
import br.ufla.dcc.ut.node.wuc.PheromoneDecreaseWakeUp;
import br.ufla.dcc.ut.node.wuc.TransponderWakeUpCall;
import br.ufla.dcc.ut.statistics.Statistics;
import br.ufla.dcc.ut.utils.Pheromone;
import br.ufla.dcc.ut.utils.Transponder;

public class RegularNode extends GenericApplicationLayer {

	//Describes from which UAV comes the pheromone
	public Map<Integer,Pheromone> pheromone = new HashMap<Integer,Pheromone>();
	
	public Transponder transponder;
	public double transponderRange = 8.0;
	
	public static boolean trackingMode = false;
	
	private boolean tMode = false;
	
	private Random rand = new Random();
	
    private int getRandom(int limit){
    	if (limit<1)
    		return limit;
    	
    	return this.rand.nextInt(limit);
    }
	
    public boolean hasPheromone(){
    	return !this.pheromone.isEmpty();
    }
    
	public void processEvent(StartSimulation start){
		this.setupTransponder();
		
		PheromoneDecreaseWakeUp wuc = new PheromoneDecreaseWakeUp(this.getSender(),100 * Math.random());
		this.sendEventSelf(wuc);
		
		TransponderWakeUpCall twuc = new TransponderWakeUpCall(this.getSender(), 1000* Math.random());
		this.sendEventSelf(twuc);
	}
	
	private void setupTransponder(){
		this.transponder = new Transponder();
		this.transponder.setMe(this.getNode());
		this.transponder.setAllNodes(SimulationManager.getAllNodes().values());
	}
	
	
	
	public void alertNeighbors(){
		SimulationManager.logNodeState(this.getNode().getId(), "Sensed", "int", "1");
		
		if (!this.trackingMode){
			trackingMode = true;
			//this.joinTrackingMode(500);
			
			if(!this.pheromone.isEmpty()){
				this.checkPheromoneAndSendAlarm();
			}else{
				this.createAndSendAgent();
			}
		}
		//this.askNeighborsToJoinTrackingMode(500);
	}

	public void checkPheromoneAndSendAlarm(){
		Position position = this.getNode().getPosition();
		double timeStamp = this.getNode().getCurrentTime();
		
		Set<Integer> keySet = this.pheromone.keySet();
		for (Integer key : keySet) {
			Pheromone pheromone = this.pheromone.get(key);
			EnemyAlarm alarm = new EnemyAlarm(position, timeStamp, 0, pheromone.get(), key);
			
			AlarmPacket ap = new AlarmPacket(this.getSender(), NodeId.ALLNODES);
			ap.setAlarm(alarm);
			this.sendPacket(ap);
			Statistics.numberOfMessagesSent++;
			Statistics.envolvedNode(this.node.getId().asInt());
		}
	}
	
	private void createAndSendAgent(){
		List<Node> neighbors = this.node.getNeighbors();
		List<NodeId> neighborsId = new ArrayList<NodeId>();
		for (Node node : neighbors) {
			neighborsId.add(node.getId());
		}
		
		List<NodeId> avoid = new ArrayList<NodeId>();
  		avoid.add(this.node.getId());
  		
		NodeId chosen = this.chooseAgentDestination(neighborsId, avoid);
		neighborsId.add(this.node.getId());
		
		DisseminationAgentPacket dapkt = new DisseminationAgentPacket(this.getSender(), chosen, neighborsId,0);
		this.sendPacket(dapkt);
		Statistics.envolvedNode(this.node.getId().asInt());
		
		SimulationManager.logNodeState(this.node.getId(), "Agent", "int", String.valueOf(4));
	}
	
	public NodeId chooseAgentDestination(List<NodeId> candidates, List<NodeId> tryToAvoid){
    	List<NodeId> possibleDestinations = new ArrayList<NodeId>(candidates);
    	possibleDestinations.removeAll(tryToAvoid);
    	
    	if (possibleDestinations.isEmpty()){
    		possibleDestinations.addAll(tryToAvoid);
    	}
    	
    	return possibleDestinations.get(this.getRandom(possibleDestinations.size()-1)); 
    }
	
	public void joinTrackingMode(){
		this.joinTrackingMode(200);
	}
	
	public void joinTrackingMode(double duration){
		this.tMode = true;
		
		LeaveTrackingModeWakeUpCall ltmWuc = new LeaveTrackingModeWakeUpCall(sender, duration);
		this.sendEventSelf(ltmWuc);
		
		SimulationManager.logNodeState(this.node.getId(), "Tracking", "int", String.valueOf(30));
	}
	
	public void leaveTrackingMode(){
		this.tMode = false;
		
		SimulationManager.logNodeState(this.node.getId(), "Not-Tracking", "int", String.valueOf(50));
	}
	
	public boolean isTracking(){
		return this.tMode;
	}
	
	public void askNeighborsToJoinTrackingMode(double timeToTrack){
		JoinTrackingModePacket packet = new JoinTrackingModePacket(this.getSender(), NodeId.ALLNODES, timeToTrack);
		this.sendPacket(packet);
	}
	
	public void askNeighborsToJoinTrackingMode(){
		this.askNeighborsToJoinTrackingMode(200);
	}
	
}
