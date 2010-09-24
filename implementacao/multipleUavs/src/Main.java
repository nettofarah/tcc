import br.ufla.dcc.grubix.simulator.kernel.Simulator;
import br.ufla.dcc.mu.node.RegularNode;
import br.ufla.dcc.mu.node.UAV;

public class Main {

	public static void main(String[] args) {
		String path = "application.xml";
		args = new String[1];
		args[0] = path;
		Simulator.main(args);
		
		
		
		
		System.out.println(RegularNode.SENT_TO_UAV + "   " +  UAV.RECEIVED_FROM_NODE);
	}
	
	
	
}