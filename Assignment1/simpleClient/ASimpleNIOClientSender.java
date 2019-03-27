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
	boolean isLocal = false;
	
	public ASimpleNIOClientSender(SocketChannel aSocketChannel, String aClientName) {
		socketChannel = aSocketChannel;
		clientName = aClientName;
		
	}
	
	public void setLocal(boolean local) {
		isLocal = local;
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		// TODO Auto-generated method stub
		if (!anEvent.getPropertyName().equals("InputString") || isLocal) return;
		ByteBuffer aMeaningByteBuffer = ByteBuffer.wrap((clientName + ":" + anEvent.getNewValue()).getBytes());
		NIOManagerFactory.getSingleton().write(socketChannel, aMeaningByteBuffer);
		
	}
	
	
}
