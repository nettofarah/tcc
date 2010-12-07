package br.ufla.dcc.mu.node;

import java.util.Random;

import br.ufla.dcc.grubix.simulator.LayerException;
import br.ufla.dcc.grubix.simulator.event.Packet;
import br.ufla.dcc.grubix.simulator.event.StartSimulation;

public class Intruder extends GenericNode {
	
	@Override
	public void lowerSAP(Packet packet) throws LayerException {
		// TODO Auto-generated method stub
	}

	public int getKind() {
		return new Random().nextInt(2);
	}
}
