package regsitry;

import java.rmi.registry.LocateRegistry;
import java.util.Scanner;


import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;



public class ARMIRegistry {

	public ARMIRegistry(int ServerPort) {
		/*FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		GIPCRPCTraceUtility.setTracing();*/
		try{
			//System.out.println(ServerPort);
			LocateRegistry.createRegistry(ServerPort);
		
			//Scanner sc = new Scanner(System.in);
			//sc.next();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
