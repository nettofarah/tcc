import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import br.ufla.dcc.grubix.simulator.NodeId;
import br.ufla.dcc.grubix.simulator.kernel.Simulator;
import br.ufla.dcc.ucr.statistics.Statistics;

public class Main {

	public static void main(String[] args) {
		
		String path = "application.xml";
		args = new String[1];
		args[0] = path;
		Simulator.main(args);
		
		
		System.out.println("\n\n\n\n\n**********************************************************");
		
		Map<NodeId, Double> timeForFirstCover = Statistics.getTimeForFirstCover();
		for (NodeId id : timeForFirstCover.keySet()){
			System.out.println("Time Interval for First Covering ("+id+")  --> "+timeForFirstCover.get(id));
		}
		System.out.println("**********************************************************");
		
		
		System.out.println("\n\n\n\n\n**********************************************************");
		
		List<Double> allCycles = Statistics.allCycles();
		int i = 0;
		for (Double time: allCycles){
			System.out.println("Cycle ("+i+") started at --> "+time);
			i++;
		}
		System.out.println("**********************************************************");
		
		
		System.out.println("\n\n\n\n\n**********************************************************");
		
		String coverages = "";
		
		Map<NodeId, Double> coverageRate = Statistics.getCoverageRate();
		for (NodeId id : coverageRate.keySet()){
			System.out.println("Coverage Rate for ("+id+")  --> "+coverageRate.get(id));
			coverages += coverageRate.get(id)+",";
		}
		
		System.out.println("**********************************************************");
		
		File result = new File("result.csv");
		try {
			FileWriter fileWriter = new FileWriter(result,true);
			fileWriter.append(coverages+"\n");
			fileWriter.flush();
			fileWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}