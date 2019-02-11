package simpleClient;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import java.util.concurrent.ArrayBlockingQueue;

import stringProcessors.HalloweenCommandProcessor;



public class AClientReaderThread extends Thread{
	private ArrayBlockingQueue<ByteBuffer> readBuffer;
	private SocketChannel channel;
	private HalloweenCommandProcessor simulation;
	private String substring ;
	private String clientName;
	private int length;

	public AClientReaderThread(ArrayBlockingQueue<ByteBuffer> readBuffer, SocketChannel aSocketChannel, 
							   HalloweenCommandProcessor simulation, String cName) {
		this.readBuffer = readBuffer;
		this.channel = aSocketChannel;
		this.simulation = simulation;
		this.clientName = cName;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	@Override
	public void run() {
		try {
			ByteBuffer message = this.readBuffer.take();
			System.out.println(this.getName());
			System.out.println(new String("New data is " + message.toString()) );
			
			
			
			String tempCommand = new String(message.array(), 0, this.length);
			
			String command = tempCommand.substring(tempCommand.indexOf(":")+1);
			
			System.out.println(command);
			
			this.simulation.processCommand(command);
			
			
		} catch (InterruptedException e) {
			
			
		}
		
		
		
	}
}
