package br.ufla.dcc.mu.movement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.event.Movement;
import br.ufla.dcc.grubix.simulator.kernel.Configuration;
import br.ufla.dcc.grubix.simulator.movement.MovementManager;
import br.ufla.dcc.grubix.simulator.movement.RandomWalk;
import br.ufla.dcc.grubix.simulator.node.Node;
import br.ufla.dcc.grubix.simulator.random.InheritRandomGenerator;
import br.ufla.dcc.grubix.simulator.random.RandomGenerator;
import br.ufla.dcc.grubix.xml.ShoXParameter;
import br.ufla.dcc.mu.utils.Transponder;



/** 
 * Manager to create random movement of the nodes avoinding colision among them.
 * Every node is moved a random distance between 0 and maxRange.
 * The direction is determined randomly between 0 and 360 degrees.
 * If a node is about to collide with another, the discard the next steps of their current movement, 
 *    and create a new movement. 
 * 
 * 
 * @author Edison and Tales
 * 
 */
public class ComposableRandomWalkColisionAvoidance extends MovementManager {
	/**
	 * Logger for the class SimulationManager.
	 */
	private static final Logger LOGGER = Logger.getLogger(RandomWalk.class.getName());
	
	/**
	 * The maximum movement range for one single step.
	 */
	@ShoXParameter(description = "The maximum movement range for one single step.", required = true)
	private double maxRange;
	
	/**
	 * The maximum time for one movement.
	 */
	@ShoXParameter(description = "The maximum time for one movement.", required = true)
	private double maxTimeForMove;
	
	/**
	 * random generator.
	 * By the default the global random generator is used.
	 */
	@ShoXParameter(description = "random generator", defaultClass = InheritRandomGenerator.class)
	private RandomGenerator random;
	
	/**
	 * Vector containing a list of moves for every node.
	 */
	private ArrayList<List<Movement>> moveLists; 
	
	/**
	 * Vector containing a list of moves for every node.
	 */
	private ArrayList directionInRad; 
	
	
	/** Constructor of the class RandomWalkColisionAvoidance. */
	public ComposableRandomWalkColisionAvoidance() {
	}

	/**
	 * responsible for localize nearest nodes and calculate distances ...
	 */	
	Transponder transponder = new Transponder();
	
    Node thisNode=null;
	
	
	/**
	 * @see br.ufla.dcc.grubix.simulator.movement.MovementManager#createMoves(java.util.Collection)
	 * Creates random moves for each node in the SIMULATION.
	 * Determines a distance and direction randomly.
	 * Afterwards calculates the new coordinates.
	 * 
	 * @param allNodes allNodes Collection containing all nodes to create moves for.
	 * @return Collection containing new moves.
	 */
	public final Collection<Movement> createMoves(Collection<Node> allNodes) {
		final Configuration configuration = Configuration.getInstance();
		
		Iterator<Node> iter = null;
		if (this.moveLists == null) {
			this.moveLists = new ArrayList<List<Movement>>(allNodes.size());
			
			for (int i = 0; i < allNodes.size(); i++) {
				this.moveLists.add(i, new LinkedList<Movement>());
			}
		}
		
		if (this.directionInRad == null) {
			this.directionInRad = new ArrayList<List<Double>>(allNodes.size());
			
			for (int i = 0; i < allNodes.size(); i++) {
				this.directionInRad.add(i, new Double(0));
			}
		}
		
		iter = allNodes.iterator();
		transponder.setAllNodes(allNodes);
		List<Movement> nextMoves = new LinkedList<Movement>();
		while (iter.hasNext()) {
			boolean flagRad = false;
			Node node = iter.next();
            
			transponder.setMe(node);

			List<Node> dentrodorange = transponder.getNodesInTheRange(10.0);

			Double distanceToNearest = transponder.getDistanceToNearest();
			Double Ang = transponder.getAngToNearest(); 

			int id = node.getId().asInt();

			List<Movement> list = this.moveLists.get(id - 1);
			//evaluate if the nearest node is less then 5 units from the current node (imminent collision)
			if(distanceToNearest < 5 ){
				list.clear();
				this.moveLists.get(id - 1).clear();
				flagRad = true;
			}
			if (list.isEmpty()) {
				//generate a new move consisting of several steps
				Position currentPos = node.getPosition();
				//LOGGER.debug("New moves for node " + id + " at position " + currentPos);
				//real movementrange is the hypothenusis of a triangle
				Double hypo = random.nextDouble() * maxRange;
				//determine direction
				Double rad = random.nextDouble() * 2 * Math.PI;
				
				// Change the angle in order to follow the oposite direction in case of imminent collision
				if (flagRad==true){
					rad = (Ang + (0.5 * Math.PI)) + random.nextDouble() * Math.PI; 
				}

				//calculate X and Y via cos and sin
				Double deltax = Math.cos(rad) * hypo;
				Double deltay = Math.sin(rad) * hypo;
				//calculate new coordinates and clamp to field
				Double newx = Math.max(0.0, Math.min(currentPos.getXCoord() + deltax, configuration.getXSize()));
				Double newy = Math.max(0.0, Math.min(currentPos.getYCoord() + deltay, configuration.getYSize()));
				//LOGGER.debug("Goal position (" + newx + ", " + newy + ")");
				//create a vector from current position to goal position
				double[] currentToGoal = new double[2];
				currentToGoal[0] = newx - currentPos.getXCoord();
				currentToGoal[1] = newy - currentPos.getYCoord();
				
				Double duration = random.nextDouble() * maxTimeForMove;
				double steps = Math.ceil(duration / configuration.getMovementTimeInterval());
				//LOGGER.debug("CurrentToGoal: (" + currentToGoal[0] + ", " + currentToGoal[1] 
				//    + "), duration:" + duration + ", steps:" + steps);

				//shorten the vector
				currentToGoal[0] /= steps;
				currentToGoal[1] /= steps;
				
				double nextx = currentPos.getXCoord();
				double nexty = currentPos.getYCoord();
				for (int i = 1; i <= steps; i++) {
					nextx += currentToGoal[0];
					nexty += currentToGoal[1];
					Position nextPos = new Position(nextx, nexty);
					Movement nextMove = new Movement(node, nextPos, 0);
					list.add(nextMove);
				}				
			}
			
			//retrieve next step for the current node
			nextMoves.add(list.remove(0));
		}
		return nextMoves;
	}
	
    public void sendCommand (String command, NodeId id, double value1) {

    }
}
