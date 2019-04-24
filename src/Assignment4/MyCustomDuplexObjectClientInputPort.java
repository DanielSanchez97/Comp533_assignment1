package Assignment4;

import java.awt.TrayIcon.MessageType;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

import bus.uigen.BoundedBuffer;
import examples.gipc.counter.customization.ACustomDuplexObjectClientInputPort;
import examples.gipc.counter.customization.ACustomReceiveNotifier;
import inputport.datacomm.ReceiveRegistrarAndNotifier;
import inputport.datacomm.duplex.DuplexClientInputPort;
import inputport.datacomm.duplex.object.ADuplexObjectClientInputPort;
import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import util.session.AReceivedMessage;
import util.trace.port.objects.ReceivedMessageDequeued;
import util.trace.port.objects.ReceivedMessageQueueCreated;
import util.trace.port.objects.ReceivedMessageQueued;

public class MyCustomDuplexObjectClientInputPort  extends ACustomDuplexObjectClientInputPort{
	MyReceiveListener listener;
	ArrayBlockingQueue<AReceiveReturnMessage<Object>> buffer;
	
	public MyCustomDuplexObjectClientInputPort(
			DuplexClientInputPort<ByteBuffer> aBBClientInputPort) {
		super(aBBClientInputPort);
		buffer = new ArrayBlockingQueue<AReceiveReturnMessage<Object>>(200);
		ReceivedMessageQueueCreated.newCase(this, buffer);
		
		listener = new MyReceiveListener(buffer);
		super.addReceiveListener(listener);
	}
	
	/**
	 * Changes the notifier that invokes receive listeners
	 */
	@Override
	protected ReceiveRegistrarAndNotifier<Object> createReceiveRegistrarAndNotifier() {
		return new ACustomReceiveNotifier();
	}
	
	/**
	 * Simply traces the sends dispatched to the sueprclass, does nothing more
	 */
	@Override
	public void send(String aDestination, Object aMessage) {
		System.out.println (aDestination + "<-" + aMessage);
		super.send(aDestination, aMessage);	
	}

	//TODO
	@Override
	public ReceiveReturnMessage<Object> receive(String aSource)  {
		AReceiveReturnMessage<Object> aMessage = null;
		boolean waiting = true;
		
		while(waiting) {
			try{
				aMessage = buffer.take();
				ReceivedMessageDequeued.newCase(this, buffer, aMessage);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			if(aMessage.getSource().equals(aSource)) {
				waiting = false;
			}
			
		}
		
		return null;
	}


	
}
