package dynamicInput;

import java.util.Timer;

import assignments.util.inputParameters.SimulationParametersListener;
import simpleClient.ASimpleNIOClient;
import util.trace.port.PerformanceExperimentEnded;
import util.trace.port.PerformanceExperimentStarted;

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

	
	@Override
	public void simulationCommand(String aCommand) {
		this.client.getCommandProcessor().setInputString(aCommand);
	}
	
	@Override
	public void quit(int aCode) {
		System.exit(aCode);
	}
	
	@Override
	public void experimentInput() {
		
		long start = System.nanoTime();
		PerformanceExperimentStarted.newCase(this, start,1000);
		//move 50 -50
		//take 1
		//move 300 0 twice
		//give 1
		//take 1
		//move -300 0 twice
		//give 1 
		//take 1
		this.simulationCommand("move 50 -50");
		for(int i=0; i<100; i++) {
			this.client.getCommandProcessor().setInputString("take 1");
			this.client.getCommandProcessor().setInputString("move 300 0");
			this.client.getCommandProcessor().setInputString("move 300 0");
			this.client.getCommandProcessor().setInputString("give 1");
			this.client.getCommandProcessor().setInputString("take 1");
			this.client.getCommandProcessor().setInputString("move -300 0");
			this.client.getCommandProcessor().setInputString("move -300 0");
			this.client.getCommandProcessor().setInputString("give 1");
			this.client.getCommandProcessor().setInputString("take 1");
		}
		long finish = System.nanoTime();
		PerformanceExperimentEnded.newCase(this, start, finish, (finish-start), 1000);
	}
	
}
