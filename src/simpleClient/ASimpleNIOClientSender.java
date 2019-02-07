package simpleClient;

import java.beans.PropertyChangeEvent;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import inputport.nio.manager.NIOManagerFactory;

public class ASimpleNIOClientSender implements SimpleNIOClientSender{
	SocketChannel socketChannel;
	String clientName;
	
	public ASimpleNIOClientSender(SocketChannel aSocketChannel, String aClientName) {
		socketChannel = aSocketChannel;
		clientName = aClientName;
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		// TODO Auto-generated method stub
		if (!anEvent.getPropertyName().equals("Data")) return;
		ByteBuffer aMeaningByteBuffer = ByteBuffer.wrap((clientName + ":" + anEvent.getNewValue()).getBytes());
		NIOManagerFactory.getSingleton().write(socketChannel, aMeaningByteBuffer);
		System.out.println("sent to the server: "+ anEvent.getNewValue()+" \n ");
	}
	
	
}
