package br.ufla.dcc.ut.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import br.ufla.dcc.grubix.simulator.Position;
import br.ufla.dcc.grubix.simulator.node.Node;




public class Transponder {
       public Collection<Node> allNodes;
            
       Node thisNode=null;

   	/** Constructor of the class RandomWalkColisionAvoidance. */  
      public Transponder() {
       //    this.allNodes = SimulationManager.getAllNodes().values();
         }
  	/** Constructor of the class RandomWalkColisionAvoidance. */
      
       public Transponder (Node me) {
      //   this.allNodes = SimulationManager.getAllNodes().values();
         this.thisNode = me;
       }
       
       public void setMe(Node me){
    	  this.thisNode = me; 
       }
       public void setAllNodes(Collection<Node> all_Nodes){
    	   this.allNodes = all_Nodes;
       }
       
       /*
        * Retuns the nodes that are in the range of the current node
        * It serves for the implementation of the detection of enemies, for example
        */
       public List<Node> getNodesInTheRange (double range) {
    	       
    	       List<Node> NodesInTheRange = new ArrayList<Node>();
               Iterator<Node> iter = null;
               iter = allNodes.iterator();

               while (iter.hasNext()) {
                       Node node = iter.next();
                       if (node.getId().asInt()!=thisNode.getId().asInt())
                       {
                         double dis = this.getDistance(node);
                         if (dis<range)
                         {
                        	 NodesInTheRange.add(node);    
                        	
                         }
                       }
               }

               return NodesInTheRange;
       }
       
       /*
        * Retuns the nearest node of a node
        */
       public Node getNearestNode () {
               Iterator<Node> iter = null;
               iter = allNodes.iterator();

               double menor = 10000.0;
               Node near=null;
               while (iter.hasNext()) {
                       Node node = iter.next();
                       if (node.getId().asInt()!=thisNode.getId().asInt())
                       {
                         double dis = this.getDistance(node);
                         if (dis<menor)
                         {
                                 menor = dis;
                                 near = node;
                         }
                       }
               }

               return near;
       }
       
       /*
        * Retuns the nearest node of a node
        */
       public Node getNearestNode (int id) {
               Iterator<Node> iter = null;
               iter = allNodes.iterator();

               double menor = 10000.0;
               Node near=null;
               while (iter.hasNext()) {
                       Node node = iter.next();
                       if (node.getId().asInt()!=thisNode.getId().asInt()&& id < node.getId().asInt())
                       {
                         double dis = this.getDistance(node);
                         if (dis<menor)
                         {
                                 menor = dis;
                                 near = node;
                         }
                       }
               }

               return near;
       }       
       /*
        * Returns the distance between  two nodes
        */
       public double getDistance (Node n1) {
               Position p1,p2;
               p1=thisNode.getPosition();
               p2=n1.getPosition();

               return Position.getDistance(p1, p2);
       }
       
       /*
        * Returns the distance between this node and the nearest
        */
       public double getDistanceToNearest () {
    	   	   Node n = this.getNearestNode();
    	   	   if (n==null)
    	   		   return 100;
    	       Position p1,p2;
               p1=thisNode.getPosition();
               p2=n.getPosition();

               return Position.getDistance(p1, p2);
       }
       
       /*
        * Returns the distance between this node and the nearest
        */
       public double getDistanceToNearest (int id) {
    	   	   Node n = this.getNearestNode(id);
    	       Position p1,p2;
               p1=thisNode.getPosition();
               p2=n.getPosition();

               return Position.getDistance(p1, p2);
       }
       
       /*
        * Returns the arctg of the angle between the line that links the
        * nodes and the reference axe.
        */  
       
   	 public double getAng (Node n1) {

        double tg =0;

        double atg = 0;
        boolean inverso = false;

        Position p1,p2,paux;

        p1=thisNode.getPosition();

		p2=n1.getPosition();
		if (p1.getXCoord()>p2.getXCoord())
		{
			paux = p1;
			p1 = p2;
			p2=paux;
			inverso = true;
		}
		
		tg = (p2.getYCoord() - p1.getYCoord())/(p2.getXCoord() - p1.getXCoord());
		
		atg = Math.atan(tg);
		if (inverso)
		{
			atg = atg + Math.PI;
		}

        return atg;
     }
   	 
     /*
      * Returns the arctg of the angle between the line that links the
      * node and its nearest neighbor and the reference axe.
      */   
   	public double getAngToNearest () {

        double tg =0;

        double atg = 0;
        boolean inverso = false;

        Position p1,p2,paux;
        Node n = this.getNearestNode();
        if (n==null) return 0;
        
        p1=thisNode.getPosition();

		p2=n.getPosition();
		if (p1.getXCoord()>p2.getXCoord())
		{
			paux = p1;
			p1 = p2;
			p2=paux;
			inverso = true;
		}
		
		tg = (p2.getYCoord() - p1.getYCoord())/(p2.getXCoord() - p1.getXCoord());
		
		atg = Math.atan(tg);
		if (inverso)
		{
			atg = atg + Math.PI;
		}

        return atg;
     }
       
}