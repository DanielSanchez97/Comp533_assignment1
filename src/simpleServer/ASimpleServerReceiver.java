package simpleServer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.MiscAssignmentUtils;

public class ASimpleServerReceiver implements SimpleServerReceiver{
	private ArrayBlockingQueue<ByteBuffer> readBuffer;
	public static final String READ_THREAD_NAME = "Read Thread";
	private ByteBuffer current; 
	
	
	public ASimpleServerReceiver(ArrayBlockingQueue<ByteBuffer> readBuffer) {
		this.readBuffer = readBuffer;
	
	}
	
	
	
	@Override
	public void socketChannelRead(SocketChannel aSocketChannel,
			ByteBuffer aMessage, int aLength) {
		
		ByteBuffer copy = MiscAssignmentUtils.deepDuplicate(aMessage);
		
		if(copy.position() == 0) {
			current = ByteBuffer.allocate(copy.capacity());
		
		}
		
		
		current.put(copy);
		
		if(current.remaining() == 0) {
			
			try {
				this.readBuffer.add(current);
		
				
			}
			catch (IllegalStateException e) {
				
				e.printStackTrace();
			} 
		}
		
		
		
		//make a copy because NIO manager uses a single Kernel buffer that can be overwritten before it is passed to the next thread
		
		
	}
}
