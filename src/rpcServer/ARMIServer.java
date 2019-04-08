package rpcServer;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;



import assignments.util.mainArgs.ServerArgsProcessor;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import regsitry.ARMIRegistry;
import rpcClient.RMICommandProcessor;
import simpleServer.ASimpleNIOServer;
import simpleServer.SimpleNIOServer;
import util.trace.Tracer;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.rmi.RMIObjectRegistered;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER, DistributedTags.RMI, DistributedTags.GIPC, DistributedTags.NIO})
public class ARMIServer implements RMIServer {
	private static final String NAME = "Broadcast";
	
	protected SimpleNIOServer NIOServer;
	
	protected RMIBroadcaster broadcaster;
	private GIPCRegistry gipcRegistry;
	
	public ARMIServer() {
		
	}
	
	public void initialize(int rPORT, int nPort, String registryHost, int GipcPort) {
		ARMIRegistry r = new ARMIRegistry(rPORT);
		
		
		NIOServer = new ASimpleNIOServer();
		NIOServer.initialize(nPort);
		NIOServer.setAtomic(true); //start in non atomic mode
		
		try {
			
			Registry rmiRegistry = LocateRegistry.getRegistry(registryHost, rPORT);
			RMIRegistryLocated.newCase(this, registryHost, rPORT, rmiRegistry);
	
			broadcaster = new ARMIBroadcaster();
			broadcaster.Initialize(this);
			UnicastRemoteObject.exportObject(broadcaster, 0);
			RMIObjectRegistered.newCase(this, NAME,broadcaster , rmiRegistry);
			rmiRegistry.rebind(NAME,broadcaster);
			
			//GIPC 
			gipcRegistry = GIPCLocateRegistry.createRegistry(GipcPort);
			gipcRegistry.rebind(NAME, broadcaster);
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Tracer.showInfo(false);
		
		
	}
	
	public RMICommandProcessor getCommandProcessor(String id) throws RemoteException {
		RMICommandProcessor proc = (RMICommandProcessor) gipcRegistry.lookupCaller(RMICommandProcessor.class, id);
		return proc;
	}
	
	public void setAtomic(rpcClient.RMIClient.Broadcast state) {
		switch (state) {
		case Atomic:
			NIOServer.setAtomic(true);
			break;
			
		case NonAtomic:
			NIOServer.setAtomic(false);
			break;
			
		default:
			break;
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

	
		
		ARMIServer aServer = new ARMIServer();
		
		aServer.initialize(ServerArgsProcessor.getRegistryPort(args), ServerArgsProcessor.getServerPort(args), 
						  ServerArgsProcessor.getRegistryHost(args), ServerArgsProcessor.getGIPCServerPort(args));
	}
}
