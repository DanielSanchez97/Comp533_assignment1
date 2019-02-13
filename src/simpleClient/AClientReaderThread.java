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
	
	private int length;
	private Boolean isAtomic;

	public AClientReaderThread(ArrayBlockingQueue<ByteBuffer> readBuffer, SocketChannel aSocketChannel, 
							   HalloweenCommandProcessor simulation) {
		this.readBuffer = readBuffer;
		this.channel = aSocketChannel;
		this.simulation = simulation;
		
	}
	

	
	public void setAtomic(Boolean value) {
		this.isAtomic = value;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				ByteBuffer message = this.readBuffer.take();
				System.out.println(this.getName());
				System.out.println(new String("New data is " + message.toString()) );
				
				
				
				String tempCommand = new String(message.array());
				
				String command = tempCommand.substring(tempCommand.indexOf(":")+1);
				
				System.out.println(command);
				
				if(isAtomic) {
					this.simulation.setConnectedToSimulation(true);
				}
				
				this.simulation.processCommand(command);
				
				if(isAtomic) {
					this.simulation.setConnectedToSimulation(false);
				}
				
				
			} catch (InterruptedException e) {
				
				
			}
		}
		
		
		
	}
}
