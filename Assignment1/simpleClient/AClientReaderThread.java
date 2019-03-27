package simpleClient;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import stringProcessors.HalloweenCommandProcessor;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.port.nio.NIOTraceUtility;



public class AClientReaderThread extends Thread{
	private ArrayBlockingQueue<ByteBuffer> readBuffer;
	private HalloweenCommandProcessor simulation;


	private Boolean isAtomic;

	public AClientReaderThread(ArrayBlockingQueue<ByteBuffer> readBuffer, HalloweenCommandProcessor simulation) {
		this.readBuffer = readBuffer;
		
		this.simulation = simulation;
		
	}
	

	
	public void setAtomic(Boolean value) {
		this.isAtomic = value;
	}
	
	@Override
	public void run() {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		while(true) {
			try {
				ByteBuffer message = this.readBuffer.take();
			
				String tempCommand = new String(message.array());
				
				String command = tempCommand.substring(tempCommand.indexOf(":")+1);
				
				
				
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
