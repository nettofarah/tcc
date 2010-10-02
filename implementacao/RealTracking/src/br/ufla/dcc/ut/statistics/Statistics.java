package br.ufla.dcc.ut.statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import br.ufla.dcc.ut.node.UAV;

public class Statistics {
	
	public static int executionNumber=1;
	
	public static int numberOfHopsToFindUAV = 0;
	public static int numberOfMessagesSent = 0;
	private static Set<Integer> envolvedNodes = new HashSet<Integer>();
	
	private static boolean alreadyExecuted = false;
	
	public static void envolvedNode (int nodeId)
	{
		envolvedNodes.add(nodeId);
	}
	
	public static void Generate ()
	{
		if (!alreadyExecuted)
		try {
			
		       FileWriter numberOfHopsFile = new FileWriter("num_hops.txt",true);
		       FileWriter numberOfMessagesFile = new FileWriter("num_messages.txt",true);
		       FileWriter envolvedNumberOfNodesFile = new FileWriter("num_envolved.txt",true);
		      
		       if (UAV.isWarned)
		       {
		         numberOfHopsFile.append(Integer.toString(numberOfHopsToFindUAV)+"\n");
		         numberOfMessagesFile.append(Integer.toString(numberOfMessagesSent)+"\n");
		         envolvedNumberOfNodesFile.append(Integer.toString(envolvedNodes.size())+"\n");
		         executionNumber++;
		         
		         System.out.println("hops > "+numberOfHopsToFindUAV);
		       }
		       
		       numberOfHopsFile.close();
		       numberOfMessagesFile.close();
		       envolvedNumberOfNodesFile.close();
		} catch (IOException e)
		{
			System.out.println("Problems with statistic file");
		}
		
		alreadyExecuted = true;
	}

}
