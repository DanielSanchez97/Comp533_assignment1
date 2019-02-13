package simpleClient;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.MiscAssignmentUtils;
import stringProcessors.HalloweenCommandProcessor;




public class ASimpleClientReciever implements SimpleClientReciever{
	private ArrayBlockingQueue<ByteBuffer> readBuffer;
	public static final String READ_THREAD_NAME = "Read Thread";
	private ByteBuffer current; 
	private HalloweenCommandProcessor simulation;
	private String clientName;
	private Boolean isAtomic;
	
	public ASimpleClientReciever( String cName, Boolean isAtmoic, ArrayBlockingQueue<ByteBuffer> buffer) {
		this.readBuffer = buffer;
		this.clientName = cName;
		this.isAtomic = isAtmoic;
	}
	
	public void setSimulation(HalloweenCommandProcessor simulation) {
		
		if(this.simulation == null) {
			this.simulation = simulation;
		}
	}
	
	public HalloweenCommandProcessor getSimulation() {
		return this.simulation;
	}
	
	
	@Override
	public void socketChannelRead(SocketChannel aSocketChannel, ByteBuffer aMessage, int aLength) {
		ByteBuffer copy = MiscAssignmentUtils.deepDuplicate(aMessage);
		
		if(copy.position() == 0) {
			current = ByteBuffer.allocate(copy.capacity());
		
		}
		
		
		current.put(copy);
		//make a copy because NIO manager uses a single Kernel buffer that can be overwritten before it is passed to the next thread
		
		if(current.remaining() == 0) {
			try {
				this.readBuffer.add(current);
					
				
			}
			catch (IllegalStateException e) {
				System.out.println(" read buffer filled in the reciever listnerer \n");
				e.printStackTrace();
			} 
		}
		
	}

}
