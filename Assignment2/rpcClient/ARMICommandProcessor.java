package rpcClient;

import java.rmi.RemoteException;

import assignments.util.inputParameters.SimulationParametersListener;
import rpcClient.RMIClient.Broadcast;
import rpcClient.RMIClient.IPC;
import util.interactiveMethodInvocation.IPCMechanism;
import util.trace.Tracer;
import util.trace.port.PerformanceExperimentEnded;
import util.trace.port.PerformanceExperimentStarted;

public class ARMICommandProcessor implements RMICommandProcessor, SimulationParametersListener {
	private ARMIClient client;
	
	@Override
	public void Initialize(ARMIClient client) {
		this.client = client;
	}

	@Override
	public void runCommand(String command) throws RemoteException {
		this.client.runCommand(command);
	}

	@Override
	public void setIPC(IPC newMode) throws RemoteException {
		this.client.setIPC(newMode);
	}

	@Override
	public void setBroadcast(Broadcast newMode) throws RemoteException {
		this.client.setBroadcast(newMode);
	}
	
	public void simulationCommand(String aCommand) {
		this.client.processCommand(aCommand);
	}
	
	public void ipcMechanism(IPCMechanism newValue) {
		switch (newValue) {
		case RMI:
			this.client.processIPC(IPC.RMI);
			break;

		case NIO:
			this.client.processIPC(IPC.NIO);
		default:
			break;
		}
	}
	
	public void broadcastMetaState(boolean newValue) {
		this.client.processMetaState(newValue);
	}
	
	public void atomicBroadcast(boolean newValue) {
		if(newValue) {
			this.client.processBroadcast(Broadcast.Atomic);
		}
		else {
			this.client.processBroadcast(Broadcast.NonAtomic);
		}
	
	}
	
	@Override
	public void setMetaState(boolean newvalue) {
		this.client.setMetaState(newvalue);
	}
	
	@Override
	public void localProcessingOnly(boolean newValue) {
		this.client.setLocal(newValue);
		System.out.println("changed local value");
	}
	
	@Override
	public void experimentInput() {
		long start = System.nanoTime();
		PerformanceExperimentStarted.newCase(this, start,1000);
		
		for(int i=0; i<500; i++) {
			simulationCommand("take 1");
			simulationCommand("give 1");
		}
		
		
		long finish = System.nanoTime();
		PerformanceExperimentEnded.newCase(this, start, finish, (finish-start), 1000);
	
	}
	

	@Override
	public void trace(boolean newValue) {
		Tracer.showInfo(newValue);
	}

}
