package Assignment4;

import java.util.concurrent.ArrayBlockingQueue;

import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import util.trace.port.objects.ReceivedMessageQueued;

public class MyServerReceiveListener extends MyReceiveListener {
	private String source;
	
	public MyServerReceiveListener(String source, ArrayBlockingQueue<AReceiveReturnMessage<Object>> buffer ) {
		super(buffer);
		this.source = source;
	}
	
	@Override
	public void messageReceived(String aSourceName, Object aMessage) {
		if(aSourceName.equals(source)) {
			super.messageReceived(aSourceName, aMessage);
		}
	}
}
