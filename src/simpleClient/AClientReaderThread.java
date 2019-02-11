package simpleClient;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import java.util.concurrent.ArrayBlockingQueue;



public class AClientReaderThread extends Thread{
	private ArrayBlockingQueue<ByteBuffer> readBuffer;
	private SocketChannel channel;

	public AClientReaderThread(ArrayBlockingQueue<ByteBuffer> readBuffer, SocketChannel aSocketChannel) {
		this.readBuffer = readBuffer;
		this.channel = aSocketChannel;
		
	}
	
	@Override
	public void run() {
		try {
			ByteBuffer message = this.readBuffer.take();
			System.out.println(this.getName());
			System.out.println(new String("New data is " + message.toString()) );
			
			
		} catch (InterruptedException e) {
			
			
		}
		
		
		
	}
}
