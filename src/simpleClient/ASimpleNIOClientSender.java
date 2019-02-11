package simpleClient;

import java.beans.PropertyChangeEvent;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import inputport.nio.manager.NIOManagerFactory;
import stringProcessors.HalloweenCommandProcessor;

public class ASimpleNIOClientSender implements SimpleNIOClientSender{
	SocketChannel socketChannel;
	String clientName;
	HalloweenCommandProcessor simulation;
	
	public ASimpleNIOClientSender(SocketChannel aSocketChannel, String aClientName) {
		socketChannel = aSocketChannel;
		clientName = aClientName;
		
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		// TODO Auto-generated method stub
		if (!anEvent.getPropertyName().equals("InputString")) return;
		ByteBuffer aMeaningByteBuffer = ByteBuffer.wrap((clientName + ":" + anEvent.getNewValue()).getBytes());
		NIOManagerFactory.getSingleton().write(socketChannel, aMeaningByteBuffer);
		
	}
	
	
}
