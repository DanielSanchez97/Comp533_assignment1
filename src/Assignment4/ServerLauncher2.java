package Assignment4;

import examples.gipc.counter.customization.ACustomCounterServer;
import examples.gipc.counter.customization.FactorySetterFactory;
import util.annotations.Tags;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;

@Tags (util.annotations.Comp533Tags.EXPLICIT_RECEIVE_SERVER)
public class ServerLauncher2 {
	public static void main (String[] args) {
		util.trace.port.objects.ObjectTraceUtility.setTracing();
		util.trace.port.rpc.RPCTraceUtility.setTracing();
		util.trace.port.rpc.RPCTraceUtility.setTracing();
		GIPCRPCTraceUtility.setTracing();
		FactorySetterFactory.setSingleton(new MyFactorySetter2());
	
		ACustomCounterServer.launch();
	}
}
