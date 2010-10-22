package br.ufla.dcc.ut.command.packet;

import java.util.ArrayList;
import java.util.List;

import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.node.Node;
import br.ufla.dcc.ut.command.impl.PacketHandler;
import br.ufla.dcc.ut.node.RegularNode;
import br.ufla.dcc.ut.node.packet.DisseminationAgentPacket;
import br.ufla.dcc.ut.statistics.Statistics;
import br.ufla.dcc.ut.utils.SenderUtils;

@PacketHandler(DisseminationAgentPacket.class)
public class DisseminationAgentPacketCommand extends PacketCommand {

	private SenderUtils senderUtils;
	
	@Override
	public void execute() {
		String nodeName = this.applicationLayer.getNode().getNodeName();
		
		this.senderUtils = new SenderUtils(this.applicationLayer);
		
		if(nodeName.equals("REGULAR")){
			RegularNode node = (RegularNode) this.applicationLayer;
			
			if(!node.hasPheromone()){
				this.disseminate();
			}else{
				node.checkPheromoneAndSendAlarm();
			}
			
		}
	}

	
	private void disseminate(){
		RegularNode node = (RegularNode) this.applicationLayer;
		DisseminationAgentPacket pak = (DisseminationAgentPacket) this.packet;
		
		SimulationManager.logNodeState(node.getId(), "Agent", "int", String.valueOf(20));

		List<Node> neighbors = node.getNode().getNeighbors();
		List<NodeId> neighborsId = new ArrayList<NodeId>();
		for (Node node2 : neighbors) {
			neighborsId.add(node2.getId());
		}
		
		List<NodeId> neighborsThatGottenThePacket = pak.getIdNeigh();
		
		NodeId chosen = node.chooseAgentDestination(neighborsId, neighborsThatGottenThePacket);
		

		List<NodeId> nodesThatShouldBeAvoided = new ArrayList<NodeId>(); 
		nodesThatShouldBeAvoided.addAll(neighborsId);
		nodesThatShouldBeAvoided.add(node.getId());

		if ((pak.getHop()+1)%20!=0)
		{
			nodesThatShouldBeAvoided.addAll(neighborsThatGottenThePacket);
		}
		if (chosen!=null && pak.getHop()<50){
			DisseminationAgentPacket novo = new DisseminationAgentPacket(node.getSender(), chosen, nodesThatShouldBeAvoided,pak.getHop()+1);
			this.senderUtils.sendDelayed(novo, Math.random()*100);
			Statistics.numberOfMessagesSent++;
			Statistics.numberOfHopsToFindUAV++;
			Statistics.envolvedNode(node.getId().asInt());
		}
	}
}
