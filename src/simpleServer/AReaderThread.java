package simpleServer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;



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
		try {
			ByteBuffer message = this.readBuffer.take();
			
			
			
			this.server.Broadcast(this.channel, message);
			System.out.println(this.getName());
			System.out.println(new String("New data is " + message.toString()) );
			
			
		} catch (InterruptedException e) {
			
			
		}
		
		
		
	}
	
	
	
	

}
