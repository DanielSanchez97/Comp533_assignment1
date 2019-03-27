package simpleClient;

import java.nio.channels.SocketChannel;

import inputport.nio.manager.NIOManagerFactory;
import inputport.nio.manager.listeners.WriteBoundedBufferListener;

public class AWriteBufferListerner implements WriteBoundedBufferListener {


	@Override
	public void writeBufferIsEmpty(SocketChannel aSocketChannel) {
		
		NIOManagerFactory.getSingleton().enableReads(aSocketChannel);
	}

}
