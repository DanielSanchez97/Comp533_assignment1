package Assignment4;

import java.awt.TrayIcon.MessageType;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

import inputport.datacomm.ReceiveListener;
import inputport.datacomm.duplex.object.ADuplexObjectClientInputPort;
import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import util.trace.port.objects.ReceivedMessageDequeued;
import util.trace.port.objects.ReceivedMessageQueued;

public class MyReceiveListener implements ReceiveListener<Object> {
	private ArrayBlockingQueue<AReceiveReturnMessage<Object>> buffer;
	
	public MyReceiveListener(ArrayBlockingQueue<AReceiveReturnMessage<Object>> buffer ) {	
		this.buffer = buffer;
	}
	
	@Override
	public void messageReceived(String aSourceName, Object aMessage) {
		AReceiveReturnMessage<Object> Amessage = new AReceiveReturnMessage<Object>(aSourceName, aMessage);
		
		
		buffer.add(Amessage);
		ReceivedMessageQueued.newCase(this, buffer, Amessage);
				
	}
	
	public ArrayBlockingQueue<AReceiveReturnMessage<Object>> getBuffer(){
		return buffer;
	}

}
