package dynamicInput;

import assignments.util.inputParameters.SimulationParametersListener;
import simpleClient.ASimpleNIOClient;

public class AClientParameterListener implements SimulationParametersListener{
	ASimpleNIOClient client;
	
	public AClientParameterListener(ASimpleNIOClient client) {
		this.client = client;
	}
	
	
	@Override
	public void atomicBroadcast(boolean newValue) {
		
		this.client.setAtomic(newValue);
		System.out.println("changed atomic value");
	}
	
	@Override
	public void localProcessingOnly(boolean newValue) {
		this.client.setLocal(newValue);
		System.out.println("changed local value");
	}
	
}
