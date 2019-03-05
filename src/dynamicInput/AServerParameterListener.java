package dynamicInput;

import assignments.util.inputParameters.SimulationParametersListener;
import simpleServer.ASimpleNIOServer;
import util.trace.Tracer;

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
	
	@Override
	public void quit(int aCode) {
		System.exit(aCode);
	}
	@Override
	public void trace(boolean newValue) {
		Tracer.showInfo(newValue);
	}
	
}
