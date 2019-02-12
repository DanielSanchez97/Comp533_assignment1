package simpleServer;




import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import assignments.util.inputParameters.ASimulationParametersController;
import assignments.util.mainArgs.ServerArgsProcessor;
import dynamicInput.AServerParameterListener;
import inputport.nio.manager.NIOManagerFactory;
import inputport.nio.manager.factories.classes.AReadingAcceptCommandFactory;

import inputport.nio.manager.factories.selectors.AcceptCommandFactorySelector;
import util.annotations.Tags;
import util.interactiveMethodInvocation.SimulationParametersController;
import util.tags.DistributedTags;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.nio.SocketChannelBound;

@Tags(DistributedTags.SERVER)
public class ASimpleNIOServer  implements SimpleNIOServer {
	SimpleServerReceiver simpleServerReceiver;
	ServerSocketChannel serverSocketChannel;
	List<SocketChannel> channels = new ArrayList<>();
	private boolean isAtomic = false;
	
	public ASimpleNIOServer() {
		
	}
	
	public void initialize(int aServerPort) {
		
		setFactories();
		serverSocketChannel = createSocketChannel(aServerPort);
		createCommunicationObjects();
		makeServerConnectable(aServerPort);
		launchConsole();
		
	}
	
	protected void setFactories() {
		AcceptCommandFactorySelector.setFactory(new AReadingAcceptCommandFactory());
		//ConnectCommandFactorySelector.setFactory(new AReadingWritingConnectCommandFactory());
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
		simpleServerReceiver = new ASimpleServerReceiver(this);
	}
	
	protected void launchConsole() {
		AServerParameterListener dynamicInput = new AServerParameterListener(this);
		SimulationParametersController aSimulationParametersController = 
				new ASimulationParametersController(); 
		aSimulationParametersController.addSimulationParameterListener(dynamicInput);
		
		aSimulationParametersController.processCommands();
	}

	@Override
	public void socketChannelAccepted(ServerSocketChannel aServerSocketChannel, SocketChannel aSocketChannel) {
		
		addListeners(aSocketChannel);
	}
	
	protected void addReadListener(SocketChannel aSocketChannel) {
		NIOManagerFactory.getSingleton().addReadListener(aSocketChannel,
				simpleServerReceiver);
	}
	
	protected void addListeners(SocketChannel aSocketChannel) {
		addWriteBufferListener(aSocketChannel);
		addReadListener(aSocketChannel);		
	}
	
	protected void addWriteBufferListener(SocketChannel aSocketChannel) {
		if(channels.add(aSocketChannel)) {
			//channel added to list of channels
		}
		else {
			System.err.println("Channel already added");
		}
	}
	
	public void Broadcast(SocketChannel aSocketChannel, ByteBuffer message) {
		//parameter is the original channel that the message to be broadcasted was
		System.out.println("calling broadcast");
		 for (SocketChannel socketChannel : channels) {
			if(socketChannel.equals(aSocketChannel) && !isAtomic) {
				continue;
			}
			
			//System.out.println( message.toString() );
			
			ByteBuffer aMessage = ByteBuffer.wrap( message.array());
		
			NIOManagerFactory.getSingleton().write(socketChannel, aMessage);
		}
		
	}
	
	public void setAtomic(boolean atomic) {
		this.isAtomic = atomic;
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
