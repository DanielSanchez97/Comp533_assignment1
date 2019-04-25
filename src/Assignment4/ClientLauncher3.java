package Assignment4;

import examples.gipc.counter.customization.ACustomCounterClient;
import examples.gipc.counter.customization.FactorySetterFactory;
import examples.gipc.counter.layers.AMultiLayerCounterClient1;
import util.annotations.Tags;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;

@Tags ({util.annotations.Comp533Tags.EXPLICIT_RECEIVE_CLIENT1})
public class ClientLauncher3 extends AMultiLayerCounterClient1  {
	
		public static void main (String[] args) {
			util.trace.port.objects.ObjectTraceUtility.setTracing();
			util.trace.port.rpc.RPCTraceUtility.setTracing();
			GIPCRPCTraceUtility.setTracing();
			FactorySetterFactory.setSingleton(new MyFactorySetter2());
			
			ACustomCounterClient.launch(CLIENT1_NAME);
		}
}
