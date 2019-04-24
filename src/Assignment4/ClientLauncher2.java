package Assignment4;

import examples.gipc.counter.customization.ACustomCounterClient;
import examples.gipc.counter.customization.ATracingFactorySetter;
import examples.gipc.counter.customization.FactorySetterFactory;
import examples.gipc.counter.layers.AMultiLayerCounterClient2;
import util.annotations.Tags;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;

@Tags (util.annotations.Comp533Tags.EXPLICIT_RECEIVE_CLIENT2)
public class ClientLauncher2 extends AMultiLayerCounterClient2 {
	public static void main (String[] args) {
		util.trace.port.objects.ObjectTraceUtility.setTracing() ;
		GIPCRPCTraceUtility.setTracing();
		FactorySetterFactory.setSingleton(new MyFactorySetter());
		
		ACustomCounterClient.launch(CLIENT2_NAME);
	}

}
