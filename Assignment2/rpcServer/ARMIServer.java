package rpcServer;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;



import assignments.util.mainArgs.ServerArgsProcessor;
import simpleServer.ASimpleNIOServer;
import simpleServer.SimpleNIOServer;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;
import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER, DistributedTags.RMI,DistributedTags.NIO})
public class ARMIServer implements RMIServer {
	private static final String NAME = "Broadcast";
	
	protected SimpleNIOServer NIOServer;
	
	protected RMIBroadcaster broadcaster;
	
	public ARMIServer() {
		
	}
	
	public void initialize(int rPORT, int nPort) {
		NIOServer = new ASimpleNIOServer();
		NIOServer.initialize(nPort);
		try {
			
			Registry rmiRegistry = LocateRegistry.getRegistry("127.0.0.1", rPORT);
			
	
			broadcaster = new ARMIBroadcaster();
			broadcaster.Initialize(this);
			UnicastRemoteObject.exportObject(broadcaster, 0);
			rmiRegistry.rebind(NAME,broadcaster);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
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

		ARMIServer aServer = new ARMIServer();
		aServer.initialize(ServerArgsProcessor.getRegistryPort(args), ServerArgsProcessor.getServerPort(args));
	}
}
