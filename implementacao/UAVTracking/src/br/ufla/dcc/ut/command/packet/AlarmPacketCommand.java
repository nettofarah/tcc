package br.ufla.dcc.ut.command.packet;

import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.node.Node;
import br.ufla.dcc.ut.command.impl.PacketHandler;
import br.ufla.dcc.ut.node.RegularNode;
import br.ufla.dcc.ut.node.event.EnemyAlarm;
import br.ufla.dcc.ut.node.packet.AlarmPacket;
import br.ufla.dcc.ut.statistics.Statistics;
import br.ufla.dcc.ut.uav.UAV;
import br.ufla.dcc.ut.utils.Pheromone;
import br.ufla.dcc.ut.utils.SenderUtils;

@PacketHandler(AlarmPacket.class)
public class AlarmPacketCommand extends PacketCommand{

	private SenderUtils senderUtils;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		Node node = this.applicationLayer.getNode();
		String nodeName = node.getNodeName();
		
		this.senderUtils = new SenderUtils(this.applicationLayer);
		
		if(nodeName.equals("REGULAR")){
			this.regularNodeExecution();
		}
		
		if(nodeName.equals("UAV")){
			this.uavExecution();
		}
	}

	private void uavExecution() {
		SimulationManager.logNodeState(this.applicationLayer.getNode().getId(), "UAV_Warned", "int", "10");
		if (!UAV.isWarned)
		{
			AlarmPacket packet = (AlarmPacket) this.packet;
			EnemyAlarm alarm = (EnemyAlarm) packet.getAlarm();
			Statistics.numberOfHopsToFindUAV+= alarm.step;
		}
		UAV.isWarned = true;
	}

	private void regularNodeExecution(){
		RegularNode node = (RegularNode) this.applicationLayer;
		AlarmPacket packet = (AlarmPacket) this.packet;
				
		EnemyAlarm alarm = (EnemyAlarm) packet.getAlarm();
		int flavor = alarm.getPherId();
		double pherAmmount = alarm.getPherAmmount();
		
		Pheromone pheromone = node.pheromone.get(flavor);
		if (pheromone!=null && pheromone.get() > pherAmmount){ //checks if this node is abler than the sender to communicate the message
			//Forwards the packet
			alarm.setPherAmmount(pheromone.get());
			alarm.setTimeStamp(node.getNode().getCurrentTime());
			alarm.step++;
			
			AlarmPacket ap = new AlarmPacket(node.getSender(), NodeId.ALLNODES);
			ap.setAlarm(alarm);
			//node.sendPacket(ap);
			this.senderUtils.sendDelayed(ap, 100* Math.random());
			Statistics.numberOfMessagesSent++;
			Statistics.envolvedNode(node.getId().asInt());
			
			SimulationManager.logNodeState(node.getNode().getId(), "Warned", "int", String.valueOf(alarm.step));
		}
	}
}
