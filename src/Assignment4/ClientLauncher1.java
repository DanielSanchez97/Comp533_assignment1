package Assignment4;

import util.annotations.Tags;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import examples.gipc.counter.customization.ACustomCounterClient;
import examples.gipc.counter.customization.ATracingFactorySetter;
import examples.gipc.counter.customization.FactorySetterFactory;
import examples.gipc.counter.layers.AMultiLayerCounterClient1;

@Tags (util.annotations.Comp533Tags.EXPLICIT_RECEIVE_CLIENT1)
public class ClientLauncher1 extends AMultiLayerCounterClient1 {
	public static void main (String[] args) {
		util.trace.port.objects.ObjectTraceUtility.setTracing();
		GIPCRPCTraceUtility.setTracing();
		FactorySetterFactory.setSingleton(new MyFactorySetter());
		
		ACustomCounterClient.launch(CLIENT1_NAME);
	}
}
