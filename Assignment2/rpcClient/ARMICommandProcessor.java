package rpcClient;

import java.rmi.RemoteException;

import assignments.util.inputParameters.SimulationParametersListener;
import rpcClient.RMIClient.Broadcast;
import rpcClient.RMIClient.IPC;
import util.interactiveMethodInvocation.IPCMechanism;

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
		if(newValue) {
			this.client.processBroadcast(Broadcast.Atomic);
		}
		else {
			this.client.processBroadcast(Broadcast.NonAtomic);
		}
	
	}

}
