package br.ufla.dcc.ut.command.packet;

import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.event.user.BeaconPacket;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.node.Node;
import br.ufla.dcc.ut.command.impl.PacketHandler;
import br.ufla.dcc.ut.node.RegularNode;
import br.ufla.dcc.ut.uav.packet.PheromonePacket;
import br.ufla.dcc.ut.utils.Pheromone;

@PacketHandler(PheromonePacket.class)
public class PheromonePacketCommand extends PacketCommand {

	@Override
	public void execute() {
		Node node = this.applicationLayer.getNode();
		String nodeName = node.getNodeName();
		
		System.out.println();
		
		if(nodeName.equals("REGULAR")){
			this.regularNodeExecution();
		}
	}
	
	
	private void regularNodeExecution(){

		if (packet.getSender().getId() != this.applicationLayer.getId()){

			String nodeName = this.applicationLayer.getNode().getNodeName();
			RegularNode regularNode = (RegularNode) this.applicationLayer;
			

			int uav = packet.getSender().getId().asInt();

			Position p1 = SimulationManager.getAllNodes().get(packet.getSender().getId()).getPosition();
			Position p2 = this.applicationLayer.getNode().getPosition();

			double d = p1.getDistance(p2);
			d = d/10;//Scales 
			d = Math.floor(d);
			double ferValue = 1/(Math.pow(2, d)); //Inverse Exponential.. calculates the value

			Pheromone p = regularNode.pheromone.get(uav);
			if (p==null){
				p = new Pheromone(uav);
				regularNode.pheromone.put(uav,p);
			}  
			SimulationManager.logNodeState(regularNode.getId(), "ComFer", "int", "2");
			p.set(ferValue);   
			p.update_view(regularNode.getId());

		}
	}

}
