package br.ufla.dcc.ut.command.wuc;

import java.util.Set;

import br.ufla.dcc.ut.command.impl.WakeUpCallHandler;
import br.ufla.dcc.ut.node.RegularNode;
import br.ufla.dcc.ut.node.wuc.PheromoneDecreaseWakeUp;
import br.ufla.dcc.ut.utils.Pheromone;

@WakeUpCallHandler(PheromoneDecreaseWakeUp.class)
public class PheromoneDecreaseWakeUpCommand extends WakeUpCallCommand {

	@Override
	public void execute() {
		RegularNode node = (RegularNode) this.applicationLayer;
		
		Set<Integer> flavors = node.pheromone.keySet();
		for (Integer flavor : flavors) {
			Pheromone pheromone = node.pheromone.get(flavor);
			pheromone.update_view(node.getId());
			pheromone.evaporate();
			
			//Removes this flavor of pheromone set.. it no longer exists
			if(pheromone.get() == 0.0){
				node.pheromone.remove(flavor);
			}
		}
		
		PheromoneDecreaseWakeUp bw = new PheromoneDecreaseWakeUp(node.getSender(),400);
		node.sendEventSelf(bw);
		
	}

}
