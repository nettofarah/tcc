import br.ufla.dcc.grubix.simulator.kernel.Simulator;

public class Main {

	public static void main(String[] args) {
		String path = "application.xml";
		args = new String[1];
		args[0] = path;
		System.out.println("START!");
		
		Simulator.main(args);
		
		System.out.println("END!");
	}

	
}