import br.ufla.dcc.grubix.simulator.kernel.Simulator;
import br.ufla.dcc.ut.statistics.Statistics;
import br.ufla.dcc.ut.uav.UAV;

public class Main {

	public static void main(String[] args) {
		String path = "tracking.xml";
		args = new String[1];
		args[0] = path;
		Simulator.main(args);
	}
	
}
