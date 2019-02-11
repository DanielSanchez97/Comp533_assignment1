package simpleClient;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.MiscAssignmentUtils;




public class ASimpleClientReciever implements SimpleClientReciever{
	private ArrayBlockingQueue<ByteBuffer> readBuffer;
	public static final String READ_THREAD_NAME = "Read Thread";
	private ByteBuffer current; 
	
	public ASimpleClientReciever() {
		this.readBuffer = new ArrayBlockingQueue<ByteBuffer>(500);
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
				if(this.readBuffer.add(current) ){
					AClientReaderThread t1 = new AClientReaderThread(this.readBuffer, aSocketChannel);
					t1.setName(READ_THREAD_NAME);
					t1.run();
					//non blocking add to the buffer and spawn a read thread to read and process the input
				}
			}
			catch (IllegalStateException e) {
				System.out.println(" read buffer filled in the reciever listnerer \n");
				e.printStackTrace();
			} 
		}
		
	}

}
