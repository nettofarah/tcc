package br.ufla.dcc.mu.movement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import br.ufla.dcc.grubix.simulator.event.Movement;
import br.ufla.dcc.grubix.simulator.kernel.Configuration;
import br.ufla.dcc.grubix.simulator.kernel.SimulationManager;
import br.ufla.dcc.grubix.simulator.movement.MovementManager;
import br.ufla.dcc.grubix.simulator.movement.NoMovement;
import br.ufla.dcc.grubix.simulator.node.Node;
import br.ufla.dcc.grubix.simulator.node.user.Command;
import br.ufla.dcc.grubix.simulator.node.user.MoveToCommand;
import br.ufla.dcc.grubix.simulator.node.user.StopCommand;

/**
 * 
 * Compose several movements
 * 
 * @author Tales, Edison
 * 
 */
public class ComposedMovement extends MovementManager {
	/**
	 * Logger for the class SimulationManager.
	 */
	private static final Logger LOGGER = Logger
			.getLogger(ComposedMovement.class.getName());

	private Vector<Collection<Node>> groupNodes = null;
	private Vector<MovementManager> movs = null;
	private Vector<Node> excluded = new Vector<Node>();

	/**
	 * Vector containing a list of moves for every node.
	 */
	private ArrayList<List<Movement>> moveLists;

	/** Constructor of the class RandomWalk. */
	public ComposedMovement() {
	}

	/**
	 * @see br.ufla.dcc.grubix.simulator.movement.MovementManager#createMoves(java.util.Collection)
	 *      Creates random moves for each node in the SIMULATION. Determines a
	 *      distance and direction randomly. Afterwards calculates the new
	 *      coordinates.
	 * 
	 * @param allNodes
	 *            allNodes Collection containing all nodes to create moves for.
	 * @return Collection containing new moves.
	 */
	public final Collection<Movement> createMoves(Collection<Node> allNodes) {
		int numberOfTypes = Configuration.getInstance().getNodeTypesCount();

		if (groupNodes == null) {
			groupNodes = new Vector<Collection<Node>>();
			movs = new Vector<MovementManager>();

			Map<String, Integer> nameAndAmmount = Configuration.getInstance()
					.getNameAndAmmount();
			Set<String> types = nameAndAmmount.keySet();

			for (String nType : types) {
				Collection<Node> group = new ArrayList<Node>();
				Iterator<Node> iter = null;
				iter = allNodes.iterator();
				while (iter.hasNext()) {
					Node node = iter.next();
					if (node.getNodeName().equals(nType)) // é do meu tipo
					{
						group.add(node);
					}
				}
				groupNodes.add(group);

				for (String stype : types) {
					if (stype.equals("REGULAR")) {
						movs.add(new NoMovement());
					}
					if (stype.equals("UAV")) {
						RandomWalkColisionAvoidance rwca = new RandomWalkColisionAvoidance();
						rwca.speed = rwca.speed * 1.5;
						movs.add(rwca);
					}
					if (stype.equals("INTRUDER")) {
						RandomWalkColisionAvoidance rwca = new RandomWalkColisionAvoidance();
						rwca.speed = rwca.speed / 5;
						movs.add(rwca);
					}
				}
			}

		}

		Collection<Movement> nextMoves = new ArrayList<Movement>();

		for (Collection<Node> cnode : groupNodes) {
			cnode.removeAll(excluded);
		}

		for (int type = 0; type < numberOfTypes; type++) {
			Collection<Movement> nextMovesaux = movs.get(type).createMoves(
					groupNodes.get(type));
			nextMoves.addAll(nextMovesaux);
		}

		return nextMoves;
	}

	public void sendCommand(Command cmd) {
		if (cmd instanceof MoveToCommand) {
			for (MovementManager mov : movs) {
				mov.sendCommand(cmd);
			}
		}
		if (cmd instanceof StopCommand) {
			for (Node aux : SimulationManager.getAllNodes().values()) {
				if (aux.getId().asInt() == ((StopCommand) cmd).id.asInt()) {
					if (!excluded.contains(aux)) {
						excluded.add(aux);
					}
				}
			}

		}
	}

}
