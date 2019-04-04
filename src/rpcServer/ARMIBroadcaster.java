package rpcServer;



import java.util.HashMap;
import java.util.Map;

import consensus.ProposalFeedbackKind;

import java.rmi.RemoteException;

import rpcClient.RMIClient.IPC;
import rpcClient.RMICommandProcessor;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.trace.port.consensus.ProposalAcceptRequestReceived;
import util.trace.port.consensus.ProposalAcceptRequestSent;
import util.trace.port.consensus.ProposalAcceptedNotificationReceived;
import util.trace.port.consensus.ProposalLearnedNotificationReceived;
import util.trace.port.consensus.ProposalLearnedNotificationSent;
import util.trace.port.consensus.RemoteProposeRequestReceived;
import util.trace.port.consensus.communication.CommunicationStateNames;

public class ARMIBroadcaster implements RMIBroadcaster,CommunicationStateNames {
	private Map<Integer, RMICommandProcessor> callbacks;
	private int id =0;
	private rpcClient.RMIClient.Broadcast s_Broadcast; //current Broadcast state 
	private ARMIServer server;
	private ConsensusAlgorithm alg;

	@Override
	public void Initialize(ARMIServer server) {
		// TODO Auto-generated method stub
		callbacks = new HashMap<Integer, RMICommandProcessor>();
		this.server = server;
		this.server.setAtomic(rpcClient.RMIClient.Broadcast.Atomic);
		
		s_Broadcast =  rpcClient.RMIClient.Broadcast.NonAtomic;
		alg = ConsensusAlgorithm.CENTRALIZED_SYNCHRONOUS;
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
		boolean value = true; 
		
		if(alg == ConsensusAlgorithm.CENTRALIZED_SYNCHRONOUS) {
			ProposalFeedbackKind rval;
			for(Integer i: callbacks.keySet()) {
				ProposalAcceptRequestSent.newCase(this, CommunicationStateNames.IPC_MECHANISM, -1, state);
				
				if(!callbacks.get(i).voteIPC(state)) {
					value = false;	
					rval = ProposalFeedbackKind.ACCESS_DENIAL;
				}
				else {
					rval = ProposalFeedbackKind.SUCCESS;
				}
				ProposalAcceptedNotificationReceived.newCase(this, CommunicationStateNames.IPC_MECHANISM, -1, state, rval);
			}
		}
		
		IPC retval;
		
		retval = value ? state : null;
		
		
		for(Integer i: callbacks.keySet()) {
			ProposalLearnedNotificationSent.newCase(this, CommunicationStateNames.IPC_MECHANISM,-1, state);
			callbacks.get(i).setIPC(retval);
		}
	}

	@Override
	public synchronized void setBroadcast(rpcClient.RMIClient.Broadcast state) throws RemoteException {
		RemoteProposeRequestReceived.newCase(this, CommunicationStateNames.BROADCAST_MODE,-1, state);
		
		boolean value = true; 
		
		if(alg == ConsensusAlgorithm.CENTRALIZED_SYNCHRONOUS) {
			ProposalFeedbackKind rval;
			for(Integer i: callbacks.keySet()) {
				ProposalAcceptRequestSent.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, state);
				if(!callbacks.get(i).voteBroadcast(state)) {
					value = false;
					rval = ProposalFeedbackKind.ACCESS_DENIAL;
				}
				else {
					rval = ProposalFeedbackKind.SUCCESS;
				}
				
				ProposalAcceptedNotificationReceived.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, state, rval);
			}
		}
		
		rpcClient.RMIClient.Broadcast retval;
		retval = value ? state : null;
		
		for(Integer i: callbacks.keySet()) {
			ProposalLearnedNotificationSent.newCase(this, CommunicationStateNames.BROADCAST_MODE,-1, state);
			callbacks.get(i).setBroadcast(retval);
		}
		
		if(value) {
			this.s_Broadcast = state;
			this.server.setAtomic(state);
		}
		else {
			//keep that same
		}
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

	@Override
	public void setAlg(ConsensusAlgorithm newAlg) throws RemoteException {
		this.alg = newAlg;
		System.out.println("using "+newAlg+ " algorithm");
		
	}

	
}
