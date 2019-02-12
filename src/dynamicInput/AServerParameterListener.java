package dynamicInput;

import assignments.util.inputParameters.SimulationParametersListener;
import simpleServer.ASimpleNIOServer;

public class AServerParameterListener  implements SimulationParametersListener {
	ASimpleNIOServer server;
	
	
	public AServerParameterListener(ASimpleNIOServer server) {
		this.server = server;
	}
	
	
	@Override
	public void atomicBroadcast(boolean newValue) {
		
		this.server.setAtomic(newValue);
		System.out.println("changed atomic value");
	}
}
