package Assignment4;

import java.awt.List;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import examples.gipc.counter.customization.ACustomDuplexObjectServerInputPort;
import inputport.datacomm.duplex.DuplexServerInputPort;
import inputport.datacomm.duplex.object.ADuplexObjectServerInputPort;
import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import util.trace.port.objects.ReceivedMessageDequeued;
import util.trace.port.objects.ReceivedMessageQueueCreated;
import util.trace.port.objects.ReceivedMessageQueued;

public class MyCustomDuplexObjectServerInputPort extends ACustomDuplexObjectServerInputPort{
	HashMap<String, MyServerReceiveListener> listeners;
	
	public MyCustomDuplexObjectServerInputPort(
			DuplexServerInputPort<ByteBuffer> aBBDuplexServerInputPort) {
		super(aBBDuplexServerInputPort);
		listeners = new HashMap<String, MyServerReceiveListener>();
	}
	
	
	//TODO
	@Override
	public ReceiveReturnMessage<Object> receive(String aSource)  {
		System.err.println("Receive started");
		ReceiveReturnMessage<Object> retVal = null;
		if(!listeners.containsKey(aSource)) {
			listeners.put(aSource, new MyServerReceiveListener(aSource, new ArrayBlockingQueue<AReceiveReturnMessage<Object>>(200)));
			ReceivedMessageQueueCreated.newCase(this, listeners.get(aSource).getBuffer());
			super.addReceiveListener(listeners.get(aSource));
		}
		
		try {
			retVal = listeners.get(aSource).getBuffer().take();
			ReceivedMessageDequeued.newCase(this,listeners.get(aSource).getBuffer(), retVal);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println (aSource + "<-" + retVal);
		return retVal;
	}

	
}
