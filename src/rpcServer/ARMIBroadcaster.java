package rpcServer;



import java.util.HashMap;
import java.util.Map;
import java.rmi.RemoteException;

import rpcClient.RMIClient.IPC;
import rpcClient.RMICommandProcessor;
import util.trace.port.consensus.ProposalLearnedNotificationSent;
import util.trace.port.consensus.RemoteProposeRequestReceived;
import util.trace.port.consensus.communication.CommunicationStateNames;

public class ARMIBroadcaster implements RMIBroadcaster,CommunicationStateNames {
	private Map<Integer, RMICommandProcessor> callbacks;
	private int id =0;
	private rpcClient.RMIClient.Broadcast s_Broadcast; //current Broadcast state 
	private ARMIServer server;

	@Override
	public void Initialize(ARMIServer server) {
		// TODO Auto-generated method stub
		callbacks = new HashMap<Integer, RMICommandProcessor>();
		this.server = server;
		this.server.setAtomic(rpcClient.RMIClient.Broadcast.Atomic);
		
		s_Broadcast =  rpcClient.RMIClient.Broadcast.Atomic;
	}

	@Override
	public synchronized void Broadcast(String command, int id) throws RemoteException {
	//	RemoteProposeRequestReceived.newCase(this, CommunicationStateNames.COMMAND,-1, command);
		switch (s_Broadcast) {
			case Atomic:
				for(Integer i: callbacks.keySet()) {
					//ProposalLearnedNotificationSent.newCase(this, CommunicationStateNames.COMMAND,-1, command);
					callbacks.get(i).runCommand(command);
					
				}
				break;
	
			case NonAtomic:
				for(Integer i: callbacks.keySet()) {
					if(i != id) {
						//ProposalLearnedNotificationSent.newCase(this, CommunicationStateNames.COMMAND,-1, command);
						callbacks.get(i).runCommand(command);
					}
				}
				break;
			
			case Local:
				break;
		}
	}

	@Override
	public synchronized void setIPC(IPC state) throws RemoteException {
		RemoteProposeRequestReceived.newCase(this, CommunicationStateNames.IPC_MECHANISM,-1, state);
		for(Integer i: callbacks.keySet()) {
			ProposalLearnedNotificationSent.newCase(this, CommunicationStateNames.IPC_MECHANISM,-1, state);
			callbacks.get(i).setIPC(state);
		}
	}

	@Override
	public synchronized void setBroadcast(rpcClient.RMIClient.Broadcast state) throws RemoteException {
		RemoteProposeRequestReceived.newCase(this, CommunicationStateNames.BROADCAST_MODE,-1, state);
		for(Integer i: callbacks.keySet()) {
			ProposalLearnedNotificationSent.newCase(this, CommunicationStateNames.BROADCAST_MODE,-1, state);
			callbacks.get(i).setBroadcast(state);
		}
		this.s_Broadcast = state;
		this.server.setAtomic(state);
	}

	@Override
	public synchronized int Register(RMICommandProcessor CommandTool) throws RemoteException {
		// TODO Auto-generated method stub
		
		if(callbacks.containsValue(CommandTool)) {
			return -1;
		}
		else {
			id++;
			callbacks.put(id, CommandTool);
			
		}
		
		return id;
	}
	@Override
	public synchronized void setMetaState(boolean newValue) throws RemoteException{
		for(Integer i: callbacks.keySet()) {
			callbacks.get(i).setMetaState(newValue);
		}
	}

}
