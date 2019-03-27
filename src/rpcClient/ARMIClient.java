package rpcClient;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assignments.util.inputParameters.ASimulationParametersController;
import assignments.util.mainArgs.ClientArgsProcessor;
import rpcServer.RMIBroadcaster;
import simpleClient.ASimpleNIOClient;
import util.annotations.Tags;
import util.interactiveMethodInvocation.SimulationParametersController;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMIObjectLookedUp;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;
import util.trace.port.PerformanceExperimentStarted;
import util.tags.DistributedTags;
import util.trace.port.consensus.*;


@Tags({DistributedTags.CLIENT, DistributedTags.RMI, DistributedTags.NIO})
public class ARMIClient implements RMIClient, CommunicationStateNames{
	private static final String LOOKUP = "Broadcast";
	private ARMICommandProcessor commandProcessor;
	private RMIBroadcaster broadcaster;
	private int id;
	private Broadcast s_Broadcast;
	private IPC s_IPC;
	private ASimpleNIOClient NIOclient;
	private boolean metaState = true;
	private int sleepTime =0;
	private boolean isLocal = false;
	

	public ARMIClient() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void Initialize(int rPORT, int sPORT, String host ,String name, String rHost) {
		try {
			
			Registry rmiRegistry = LocateRegistry.getRegistry(rHost, rPORT);
			RMIRegistryLocated.newCase(this,rHost, rPORT, rmiRegistry);
			
			commandProcessor = new ARMICommandProcessor();
			commandProcessor.Initialize(this);
			UnicastRemoteObject.exportObject(commandProcessor,0);
			
			broadcaster = (RMIBroadcaster) rmiRegistry.lookup(LOOKUP);
			RMIObjectLookedUp.newCase(this, broadcaster, LOOKUP, rmiRegistry);
			id = broadcaster.Register(commandProcessor);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(id);
		s_Broadcast = Broadcast.NonAtomic;
		s_IPC = IPC.RMI;
		
		NIOclient = new ASimpleNIOClient(name);
		NIOclient.initialize(host, sPORT);
		NIOclient.setLocal(true); //default state is RMI so we don't want to transmit over NIO
		NIOclient.setAtomic(false);
		NIOclient.getCommandProcessor().setConnectedToSimulation(true);
		launchConsole();
		
		
	}
	
	protected void launchConsole() {
	
		SimulationParametersController aSimulationParametersController = 
				new ASimulationParametersController();
		 
		 aSimulationParametersController.addSimulationParameterListener(commandProcessor);
		
		 aSimulationParametersController.processCommands();
	}
	
	public void runCommand(String command) {
		//ProposalLearnedNotificationReceived.newCase(this,  CommunicationStateNames.COMMAND, -1, command);
		//System.out.println(command);
		
		//ProposedStateSet.newCase(this,  CommunicationStateNames.COMMAND, -1, command);
		ACommandThread thread = new ACommandThread(NIOclient, command);
		//NIOclient.getCommandProcessor().setInputString(command);
		thread.setName("main");
		thread.start();
	}
	
	public void setIPC(IPC state) {
		ProposalLearnedNotificationReceived.newCase(this,  CommunicationStateNames.IPC_MECHANISM, -1, state);
		//System.out.println(state.toString());
		this.setMetaState(true);
		ProposedStateSet.newCase(this,  CommunicationStateNames.IPC_MECHANISM, -1, state);
		s_IPC = state;
		switch (state) {
			case RMI:
				NIOclient.setLocal(true); //so commands wont be wrote twice via NIO
				NIOclient.getCommandProcessor().setConnectedToSimulation(true);
				break;
	
			case NIO:
				NIOclient.setLocal(false);//commands written using buffers
				if(s_Broadcast == Broadcast.Atomic) {
					NIOclient.getCommandProcessor().setConnectedToSimulation(false);
				}
				break;
				
			default:
				break;
		}
	}
	
	public void setBroadcast(Broadcast state) {
		ProposalLearnedNotificationReceived.newCase(this,  CommunicationStateNames.BROADCAST_MODE, -1, state);
		//System.out.println(state.toString());
		this.setMetaState(true);
		ProposedStateSet.newCase(this,  CommunicationStateNames.BROADCAST_MODE, -1, state);
		s_Broadcast = state;
		
		switch (state) {
		case Atomic:
			NIOclient.setAtomic(true);
			if(s_IPC == IPC.RMI) {
				NIOclient.getCommandProcessor().setConnectedToSimulation(true);
			}
			break;

		case NonAtomic:
			NIOclient.setAtomic(false);
			
		default:
			break;
		}
		
	}
	
	public void processCommand(String command) {
		if(isLocal) {
			NIOclient.getCommandProcessor().setInputString(command);
			return;
		}
		
		
		util.misc.ThreadSupport.sleep(sleepTime);
		
		switch (s_IPC) {
		case RMI:
			//ProposalMade.newCase(this, CommunicationStateNames.COMMAND, -1, command);
			//RemoteProposeRequestSent.newCase(this,  CommunicationStateNames.COMMAND, -1, command);
			RemoteProposeRequestSent.newCase(this,  CommunicationStateNames.COMMAND, -1, command);
			try {
				
				switch (s_Broadcast) {
					case Atomic:
						//do nothing because the move will be processed when it is returned from server
						
						break;
						
					case NonAtomic:
						//process command locally before sending it to broadcast
						//RemoteProposeRequestSent.newCase(this,  CommunicationStateNames.COMMAND, -1, command);
						NIOclient.getCommandProcessor().setInputString(command);
						break;
						
					default:
						break;
				}
			
				
				broadcaster.Broadcast(command, id);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case NIO:
			RemoteProposeRequestSent.newCase(this,  CommunicationStateNames.COMMAND, -1, command);
			NIOclient.getCommandProcessor().setInputString(command);
			
		default:
			break;
		}
		
	}
	
	public void processIPC(IPC state) {
		if(!this.metaState) {
			return;
		}
		util.misc.ThreadSupport.sleep(sleepTime);
		ProposalMade.newCase(this, CommunicationStateNames.IPC_MECHANISM, -1, state);
		try {
			RemoteProposeRequestSent.newCase(this,  CommunicationStateNames.IPC_MECHANISM, -1, state);
			broadcaster.setIPC(state);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void processBroadcast(Broadcast state) {
		if(!this.metaState) {
			return;
		}
		util.misc.ThreadSupport.sleep(sleepTime);
		ProposalMade.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, state);
		try {
			RemoteProposeRequestSent.newCase(this,  CommunicationStateNames.IPC_MECHANISM, -1, state);
			broadcaster.setBroadcast(state);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
	
		
		ARMIClient aCLient = new ARMIClient();
		aCLient.Initialize(ClientArgsProcessor.getRegistryPort(args), ClientArgsProcessor.getServerPort(args),ClientArgsProcessor.getServerHost(args),
						   ClientArgsProcessor.getClientName(args), ClientArgsProcessor.getRegistryHost(args));
	}
	
	public void setMetaState(boolean value) {
		this.metaState = value;
	}
	
	public void processMetaState(boolean value) {
		if(!value) {
			try {
				broadcaster.setMetaState(value);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else {
			setMetaState(value);
		}
	}
	
	public void setLocal(boolean newValue) {
		this.isLocal = newValue;
	}
	


}
