package br.ufla.dcc.ucr.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.ufla.dcc.grubix.simulator.LayerException;
import br.ufla.dcc.grubix.simulator.LayerType;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.StartSimulation;
import br.ufla.dcc.grubix.simulator.event.WakeUpCall;
import br.ufla.dcc.grubix.simulator.event.user.BeaconPacket;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.node.Layer;
import br.ufla.dcc.grubix.simulator.node.Node;
import br.ufla.dcc.ucr.packet.CoverageAlarmPacket;
import br.ufla.dcc.ucr.statistics.Statistics;
import br.ufla.dcc.ucr.utils.NodeUtils;
import br.ufla.dcc.ucr.wuc.CheckCoverageWakeUpCall;
import br.ufla.dcc.ucr.wuc.OccasionalyDestroySomeSensorsWakeUpCall;

public class RegularNode extends GenericNode {
	
	private List<String> sensors = new ArrayList<String>();
	private static double deffectRate = .1;
	
	private static Integer[] deffectGroup = {1,2,3,4,5,6,7,8,9,10};
	
	private static int eventuallyDisasterRate = 5000;
	private static int checkCoverageRate = 2000;
	private static double minCoverageAllowed = .9;
	
	private boolean underMinCoverage = false;

	
	public void processEvent(StartSimulation start){
		this.initSensors();
		
		this.logNodeState();
		
		WakeUpCall wuc = new OccasionalyDestroySomeSensorsWakeUpCall(this.getSender(), eventuallyDisasterRate);
		this.sendEventSelf(wuc);
		
		WakeUpCall ccWuc = new CheckCoverageWakeUpCall(this.getSender(), checkCoverageRate);
		this.sendEventSelf(ccWuc);
	}
	
	private void initSensors() {
		for (int i=0; i<100; i++){
			this.sensors.add("S"+i);
		}
	}

	private void logNodeState(){
		NodeId id = this.node.getId();
		
		SimulationManager.logNodeState(id, "SensorsState", "float", String.valueOf(this.getCoverage()));
		
		if(this.underMinCoverage)
			SimulationManager.logNodeState(id, "Under Minimal Coverage", "int", "1");
	}
	
	private double getCoverage(){
		double sizeAsDouble = (double) this.sensors.size();
		Double coverage = sizeAsDouble / 100;
		
		return coverage;
	}

	public void processWakeUpCall(WakeUpCall wuc){
		if (wuc instanceof OccasionalyDestroySomeSensorsWakeUpCall)
			this.occasionalyDeffectSomeSensors();
		
		if(wuc instanceof CheckCoverageWakeUpCall)
			this.checkCoverage();
	}

	@Override
	public void lowerSAP(Packet packet) throws LayerException {
		if(packet instanceof BeaconPacket)
			this.processBeaconPacket();
	}
	
	
	private void processBeaconPacket() {
		if(underMinCoverage)
			Statistics.logCovered(getId());
	}

	private void occasionalyDeffectSomeSensors(){
		double probabilityOfDeffectOccur = Math.random();
		//boolean deffectOcurred = deffectRate > probabilityOfDeffectOccur;
		List<Integer> asList = Arrays.asList(deffectGroup);
		boolean deffectOcurred = asList.contains(this.getId().asInt());
			
		if(deffectOcurred){
			this.destroySomeSensors();
		}
		
		this.logNodeState();
	}

	private void destroySomeSensors() {
		boolean isTheAnySensorLeft = this.sensors.size() > 0;
		if(isTheAnySensorLeft){
			this.reallyDestroySensors(50);
		}
	}

	private void reallyDestroySensors(int deffectAmmount) {
		for (int i=0; i<deffectAmmount; i++){
			this.sensors.remove(0);
		}
		Statistics.logProblem(getId());
	}
	
	private void checkCoverage(){
		if (this.getCoverage() < minCoverageAllowed && !this.underMinCoverage){
			this.underMinCoverage = true;
			this.logNodeState();
			
			this.sendCoverageAlarm();
		}
				
		WakeUpCall ccWuc = new CheckCoverageWakeUpCall(this.getSender(), checkCoverageRate);
		this.sendEventSelf(ccWuc);
	}
	

	private void sendCoverageAlarm() {
		List<Node> uavs = NodeUtils.findUavs();
		if(uavs.size() > 0){
			this.sendCovarageAlarmPacket(uavs.get(0));
		}
	}
	
	private void sendCovarageAlarmPacket(Node receiver){
		Position position = this.getNode().getPosition();
		Packet coverageAlarmPacket = new CoverageAlarmPacket(this.getSender(), receiver.getId(), position, this.getCoverage());
		//this.sendPacket(coverageAlarmPacket);
		
		Layer applicationLayer =  receiver.getLayer(LayerType.APPLICATION);
		try {
			applicationLayer.lowerSAP(coverageAlarmPacket);
		} catch (LayerException e) {
			//Wrong way.. but is more practical
			e.printStackTrace();
		}
	}
}
