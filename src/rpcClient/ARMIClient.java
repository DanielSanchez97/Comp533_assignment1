package rpcClient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

import assignments.util.inputParameters.ASimulationParametersController;
import assignments.util.mainArgs.ClientArgsProcessor;
import consensus.ProposalFeedbackKind;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import rpcServer.RMIBroadcaster;
import simpleClient.ASimpleNIOClient;
import util.annotations.Tags;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.SimulationParametersController;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.rmi.RMIObjectLookedUp;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;
import util.trace.port.PerformanceExperimentStarted;
import util.tags.DistributedTags;
import util.trace.port.consensus.*;


@Tags({DistributedTags.CLIENT, DistributedTags.RMI, DistributedTags.GIPC, DistributedTags.NIO})

public class ARMIClient implements RMIClient, CommunicationStateNames{
	private static final String LOOKUP = "Broadcast";
	private ARMICommandProcessor commandProcessor;
	private RMIBroadcaster broadcaster;
	private RMIBroadcaster meta_broadcaster;
	private int id;
	
	private Broadcast s_Broadcast;
	private IPC s_IPC;
	private Broadcast last_Broadcast;
	private IPC last_IPC;
	
	private ASimpleNIOClient NIOclient;
	private Boolean metaState = true;
	private int sleepTime =0;
	private boolean isLocal = false;
	private boolean vote = true;
	private Remote RMI_PBroadcaster;
	private Object GIPC_PBroadcaster;
	private String s_id;
	

	public ARMIClient() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void Initialize(int rPORT, int sPORT, String host ,String name, String rHost, boolean vote, int GIPCPort) {
		
		
		try {
			
			Registry rmiRegistry = LocateRegistry.getRegistry(rHost, rPORT);
			RMIRegistryLocated.newCase(this,rHost, rPORT, rmiRegistry);
			
			commandProcessor = new ARMICommandProcessor();
			commandProcessor.Initialize(this); //take boolean as param todo
			UnicastRemoteObject.exportObject(commandProcessor,0);
			
			
			RMI_PBroadcaster = rmiRegistry.lookup(LOOKUP);
			broadcaster = (RMIBroadcaster) RMI_PBroadcaster;
			RMIObjectLookedUp.newCase(this, broadcaster, LOOKUP, rmiRegistry);
			id = broadcaster.Register(commandProcessor);
			
			
			s_id = name;
			GIPCRegistry gipcRegistry = GIPCLocateRegistry.getRegistry(host, GIPCPort, s_id);
			GIPC_PBroadcaster = gipcRegistry.lookup(RMIBroadcaster.class, LOOKUP);
		
			//gipcRegistry.rebind(s_id, commandProcessor);
			
			broadcaster = (RMIBroadcaster) GIPC_PBroadcaster;
			broadcaster.GIPCRegister(commandProcessor, s_id);
			
			broadcaster= (RMIBroadcaster)  RMI_PBroadcaster;
			meta_broadcaster = (RMIBroadcaster) RMI_PBroadcaster;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(id);
		s_Broadcast = Broadcast.Atomic;
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
		ProposalLearnedNotificationReceived.newCase(this,  CommunicationStateNames.COMMAND, -1, command);
		//System.out.println(command);
		
		ProposedStateSet.newCase(this,  CommunicationStateNames.COMMAND, -1, command);
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
		
		if(state == null) {
			s_IPC = last_IPC;
		}
		else {
			s_IPC = state;
		
		
			switch (state) {
				case RMI:
					broadcaster = (RMIBroadcaster) RMI_PBroadcaster;
					NIOclient.setLocal(true); //so commands wont be wrote twice via NIO
					NIOclient.getCommandProcessor().setConnectedToSimulation(true);
					break;
		
				case NIO:
					NIOclient.setLocal(false);//commands written using buffers
					if(s_Broadcast == Broadcast.Atomic) {
						NIOclient.getCommandProcessor().setConnectedToSimulation(false);
					}
					break;
				
				case GIPC:
					broadcaster = (RMIBroadcaster) GIPC_PBroadcaster;
					NIOclient.setLocal(true); //so commands wont be wrote twice via NIO
					NIOclient.getCommandProcessor().setConnectedToSimulation(true);
					break;
					
				default:
					break;
			}
		}
	}
	
	public void setBroadcast(Broadcast state) {
		ProposalLearnedNotificationReceived.newCase(this,  CommunicationStateNames.BROADCAST_MODE, -1, state);
		//System.out.println(state.toString());
		this.setMetaState(true);
		ProposedStateSet.newCase(this,  CommunicationStateNames.BROADCAST_MODE, -1, state);
		
		if(state == null) {
			s_Broadcast = last_Broadcast;
		}
		else {
			s_Broadcast = state;
		}
		
		switch (state) {
			case Atomic:
				NIOclient.setAtomic(true);
				if(s_IPC == IPC.RMI) {
					//NIOclient.getCommandProcessor().setConnectedToSimulation(true);
				}
				break;
	
			case NonAtomic:
				NIOclient.setAtomic(false);
				
			default:
				break;
		}
		
	}
	
	public void processCommand(String command) {
		if((this.s_IPC == null) || (this.s_Broadcast == null)) {
			ActionWhileEnablingProposalIsPending.newCase(this, CommunicationStateNames.COMMAND, -1, command);
			System.out.println("Failed to process command: '"+command+"' because the client is not stable");
			return;
		}
			
		if(isLocal) {
			NIOclient.getCommandProcessor().setInputString(command);
			return;
		}
		
		util.misc.ThreadSupport.sleep(sleepTime);
		
		switch (s_IPC) {
		case GIPC:
			try {
				switch (s_Broadcast) {
					case Atomic:
						//do nothing because the move will be processed when it is returned from server
						break;
						
					case NonAtomic:
						//process command locally before sending it to broadcast
						NIOclient.getCommandProcessor().setInputString(command);
						break;
						
					default:
						break;
				}
				
				RemoteProposeRequestSent.newCase(this,  CommunicationStateNames.COMMAND, -1, command);
				broadcaster.GIPCBroadcast(s_id,command);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			break;
			
			
		case RMI:
			try {
				switch (s_Broadcast) {
					case Atomic:
						//do nothing because the move will be processed when it is returned from server		
						break;
						
					case NonAtomic:
						//process command locally before sending it to broadcast
						NIOclient.getCommandProcessor().setInputString(command);
						break;
						
					default:
						break;
				}
				
				RemoteProposeRequestSent.newCase(this,  CommunicationStateNames.COMMAND, -1, command);
				broadcaster.Broadcast(command, id);
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case NIO:
			RemoteProposeRequestSent.newCase(this,  CommunicationStateNames.COMMAND, -1, command);
			NIOclient.getCommandProcessor().setInputString(command);
			break;
			
		default:
			break;
		}
		
	}
	
	public void processIPC(IPC state) {
		if(!this.metaState) {
			ProposedStateSet.newCase(this,  CommunicationStateNames.IPC_MECHANISM, -1, state);
			s_IPC = state;
			
			switch (state) {
				case RMI:
					broadcaster = (RMIBroadcaster) RMI_PBroadcaster;
					NIOclient.setLocal(true); //so commands wont be wrote twice via NIO
					NIOclient.getCommandProcessor().setConnectedToSimulation(true);
					break;
		
				case NIO:
					NIOclient.setLocal(false);//commands written using buffers
					if(s_Broadcast == Broadcast.Atomic) {
						NIOclient.getCommandProcessor().setConnectedToSimulation(false);
					}
					break;
				
				case GIPC:
					broadcaster = (RMIBroadcaster) GIPC_PBroadcaster;
					NIOclient.setLocal(true); //so commands wont be wrote twice via NIO
					NIOclient.getCommandProcessor().setConnectedToSimulation(true);
					break;
					
				default:
					break;
			}
			return;
		}
		
		util.misc.ThreadSupport.sleep(sleepTime);
		ProposalMade.newCase(this, CommunicationStateNames.IPC_MECHANISM, -1, state);
		try {
			RemoteProposeRequestSent.newCase(this,  CommunicationStateNames.IPC_MECHANISM, -1, state);
			meta_broadcaster.setIPC(state);
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
			meta_broadcaster.setBroadcast(state);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setVote(boolean value) {
		this.vote = value;
	}
	
	public void setMetaState(boolean value) {
		this.metaState = value;
	}
	
	public void processMetaState(boolean value) {
		if(!value) {
			try {
				meta_broadcaster.setMetaState(value);
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
	
	public boolean voteBroadcast(Broadcast proposed) {
		ProposalAcceptRequestReceived.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, proposed);
		this.last_Broadcast = this.s_Broadcast;
		this.s_Broadcast = null;
		ProposalFeedbackKind retval;
		
		 retval = this.vote ? ProposalFeedbackKind.SUCCESS : ProposalFeedbackKind.ACCESS_DENIAL;
		
		
		ProposalAcceptedNotificationSent.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, proposed, retval);
		return this.vote;
	}
	
	public boolean voteIPC(IPC proposed) {
		ProposalAcceptRequestReceived.newCase(this, CommunicationStateNames.IPC_MECHANISM, -1, proposed);
		this.last_IPC = this.s_IPC;
		this.s_IPC = null;
		
		ProposalFeedbackKind retval;
		
		retval = this.vote ? ProposalFeedbackKind.SUCCESS :  ProposalFeedbackKind.ACCESS_DENIAL;
		
		ProposalAcceptedNotificationSent.newCase(this, CommunicationStateNames.IPC_MECHANISM, -1, proposed, retval);
		return this.vote;
	}

	public void setAlg(ConsensusAlgorithm alg) {
		try {
			broadcaster.setAlg(alg);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void main(String[] args) {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		GIPCRPCTraceUtility.setTracing();
		
		ARMIClient aCLient = new ARMIClient();
		
		aCLient.Initialize(ClientArgsProcessor.getRegistryPort(args), ClientArgsProcessor.getServerPort(args),ClientArgsProcessor.getServerHost(args),
						   ClientArgsProcessor.getClientName(args), ClientArgsProcessor.getRegistryHost(args), true, ClientArgsProcessor.getGIPCPort(args));
	}
}
