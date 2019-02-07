package simpleServerDemo.Servers;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class ASimpleServerReceiver implements SimpleServerReceiver{

	@Override
	public void socketChannelRead(SocketChannel aSocketChannel,
			ByteBuffer aMessage, int aLength) {
		
		System.out.println(new String("Message " + aMessage.array()));
		
		//String aMeaning = new String(aMessage.array(), aMessage.position(),aLength);
		//System.out.println("Meaning of Life from " + aMeaning);
		//TODO do something with the read input
		
	}
}
