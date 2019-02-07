package simpleServerDemo.Servers;




import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import assignments.util.mainArgs.ServerArgsProcessor;
import examples.nio.manager.server.AMeaningOfLifeNIOServer;
import examples.nio.manager.server.AMeaningOfLifeServerReceiver;
import examples.nio.manager.server.MeaningOfLifeNIOServer;
import inputport.nio.manager.NIOManagerFactory;
import inputport.nio.manager.factories.classes.AReadingAcceptCommandFactory;
import inputport.nio.manager.factories.selectors.AcceptCommandFactorySelector;
import util.annotations.Tags;
import util.tags.DistributedTags;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.nio.SocketChannelBound;

public class ASimpleNIOServer  implements SimpleNIOServer {
	SimpleServerReceiver simpleServerReceiver;
	ServerSocketChannel serverSocketChannel;
	
	public ASimpleNIOServer() {
		
	}
	
	public void initialize(int aServerPort) {
		setFactories();
		serverSocketChannel = createSocketChannel(aServerPort);
		createCommunicationObjects();
		makeServerConnectable(aServerPort);
		
	}
	
	protected void setFactories() {
		AcceptCommandFactorySelector.setFactory(new AReadingAcceptCommandFactory());
	}
	
	protected ServerSocketChannel createSocketChannel(int aServerPort) {
		try {
			ServerSocketChannel retVal = ServerSocketChannel.open();
			InetSocketAddress isa = new InetSocketAddress(aServerPort);
			retVal.socket().bind(isa);
			SocketChannelBound.newCase(this, retVal, isa);
			return retVal;

		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	protected void createCommunicationObjects() {
		createReceiver();
	}
	
	protected void createReceiver() {
		simpleServerReceiver = new ASimpleServerReceiver();
	}

	@Override
	public void socketChannelAccepted(ServerSocketChannel aServerSocketChannel, SocketChannel aSocketChannel) {
		// TODO Auto-generated method stub
		
	}
	
	protected void addReadListener(SocketChannel aSocketChannel) {
		NIOManagerFactory.getSingleton().addReadListener(aSocketChannel,
				simpleServerReceiver);
	}
	
	protected void addListeners(SocketChannel aSocketChannel) {
//		addWriteBufferListener(aSocketChannel);
		addReadListener(aSocketChannel);		
	}
	

	protected void makeServerConnectable(int aServerPort) {
		NIOManagerFactory.getSingleton().enableListenableAccepts(
				serverSocketChannel, this);
	}
	
	public static void main(String[] args) {
		FactoryTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		BeanTraceUtility.setTracing();// not really needed, but does not hurt
		SimpleNIOServer aServer = new ASimpleNIOServer();
		aServer.initialize(ServerArgsProcessor.getServerPort(args));

	}
}
