package br.ufla.dcc.ut.node;

import java.util.HashMap;
import java.util.Map;

import br.ufla.dcc.grubix.simulator.LayerException;
import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.ut.packet.PheromonePacket;
import br.ufla.dcc.ut.utils.Pheromone;
import br.ufla.dcc.ut.utils.Transponder;


public class RegularNode extends GenericNode {
	
	public Map<Integer,Pheromone> pheromone = new HashMap<Integer,Pheromone>();
	
	public Transponder transponder;
	public double transponderRange = 8.0;
	
    
    
    @Override
	public void lowerSAP(Packet packet) throws LayerException {
		if (packet instanceof PheromonePacket)
			this.storePheromone((PheromonePacket) packet);
	}
		
    
	public void storePheromone(PheromonePacket packet){
		if (packet.getSender().getId() != getId()){
		
			NodeId uavId = packet.getSender().getId();
			int uav = uavId.asInt();
			
			Position p1 = SimulationManager.getAllNodes().get(uavId).getPosition();
			Position p2 = this.getNode().getPosition();

			double d = p1.getDistance(p2);
			d = d/10;//Scales 
			d = Math.floor(d);
			double ferValue = 1/(Math.pow(2, d)); //Inverse Exponential.. calculates the value

			Pheromone p = this.pheromone.get(uav);
			if (p==null){
				p = new Pheromone(uavId);
				this.pheromone.put(uav,p);
			}  
			SimulationManager.logNodeState(this.getId(), "ComFer", "int", "2");
			p.set(ferValue);   
			p.update_view(this.getId());

		}
	}
	
	
    public boolean hasPheromone(){
    	return !this.pheromone.isEmpty();
    }

	
}
