package Assignment4;

import examples.gipc.counter.customization.ACustomCounterServer;
import examples.gipc.counter.customization.FactorySetterFactory;
import util.annotations.Tags;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;

@Tags (util.annotations.Comp533Tags.EXPLICIT_RECEIVE_SERVER)
public class ServerLauncher {
	public static void main (String[] args) {
		util.trace.port.objects.ObjectTraceUtility.setTracing();
		GIPCRPCTraceUtility.setTracing();
		FactorySetterFactory.setSingleton(new MyFactorySetter());
	
		ACustomCounterServer.launch();
	}
}
