package simpleServer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.port.nio.NIOTraceUtility;



public class AReaderThread extends Thread {

	private ArrayBlockingQueue<ByteBuffer> readBuffer;
	private SocketChannel channel;
	private ASimpleNIOServer server;

	public AReaderThread(ArrayBlockingQueue<ByteBuffer> readBuffer, SocketChannel aSocketChannel, ASimpleNIOServer server) {
		this.readBuffer = readBuffer;
		this.channel = aSocketChannel;
		this.server = server;
	}
	
	@Override
	public void run() {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		while(true) {
			try {
				ByteBuffer message = this.readBuffer.take();
				
				this.server.Broadcast(this.channel, message);
			} catch (InterruptedException e) {
				
				
			}
		}
		
		
		
	}
	
	
	
	

}
