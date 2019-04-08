package simpleClient;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.inputParameters.ASimulationParametersController;
import assignments.util.mainArgs.ClientArgsProcessor;
import dynamicInput.AClientParameterListener;
import graphics.HalloweenSimulation;
import inputport.nio.manager.NIOManagerFactory;
import inputport.nio.manager.factories.classes.AReadingWritingConnectCommandFactory;
import inputport.nio.manager.factories.selectors.ConnectCommandFactorySelector;
import main.BeauAndersonFinalProject;
import stringProcessors.HalloweenCommandProcessor;
import util.annotations.Tags;
import util.interactiveMethodInvocation.SimulationParametersController;
import util.tags.DistributedTags;
import util.trace.Tracer;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.port.nio.NIOTraceUtility;

//@Tags({DistributedTags.CLIENT})
public class ASimpleNIOClient implements SimpleNIOClient{
	String clientName;
	SimpleNIOClientSender simpleClientSender;

	SocketChannel socketChannel;
	ASimpleClientReciever reciever;

	ArrayBlockingQueue<ByteBuffer> readBuffer;
	public static final String READ_THREAD_NAME = "Read Thread";
	AClientReaderThread aReadThread;
	
	
	HalloweenCommandProcessor commandInput;
	HalloweenSimulation simulation;
	private boolean isAtomic = false;
	private boolean isLocal = false;
	
	public ASimpleNIOClient(String aClientName) {
		clientName = aClientName;
	}
	
	
	protected void setFactories() {		
		ConnectCommandFactorySelector.setFactory(new AReadingWritingConnectCommandFactory());
		//AcceptCommandFactorySelector.setFactory(new AReadingAcceptCommandFactory());
	}
	
	@Override
	public void initialize(String aServerHost, int aServerPort) {
		readBuffer = new ArrayBlockingQueue<ByteBuffer>(2000);
		setFactories();
		socketChannel = createSocketChannel();
		createCommunicationObjects();

		connectToServer(aServerHost, aServerPort);
		
		createSimulation();
		createReadThread();
		Tracer.showInfo(false);
		
		//commandInput.setConnectedToSimulation(!this.isAtomic);
		//launchConsole();
		
	}


	protected void createReadThread() {
		aReadThread = new AClientReaderThread(this.readBuffer, commandInput);
		aReadThread.setName(READ_THREAD_NAME);
		aReadThread.setAtomic(isAtomic);
		aReadThread.start();
	}

	
	protected void createSimulation() {
		commandInput = BeauAndersonFinalProject.createSimulation("client", 0, 0, 400, 765, 0, 0);
		
		
		commandInput.addPropertyChangeListener(simpleClientSender);
			

		if(reciever.getSimulation() == null) {
			reciever.setSimulation(commandInput);
		}
		
		simpleClientSender.setLocal(this.isLocal);
		
	}

	
	protected void addServerListeners(SocketChannel asocketChannel) {
	
		
		reciever = new ASimpleClientReciever( this.clientName, this.isAtomic, this.readBuffer);
		
		if(this.commandInput != null) {
			reciever.setSimulation(this.commandInput);
		}
		
		addReadListeners(asocketChannel);
		//NIOManagerFactory.getSingleton().addWriteBoundedBufferListener(asocketChannel, writeBufferLiserner);
		System.out.println("added read listener");
		
	}
	
	public void addReadListeners(SocketChannel asocketChannel) {
		NIOManagerFactory.getSingleton().addReadListener(asocketChannel,
				reciever);
	}
	

	protected SocketChannel createSocketChannel() {
		try {
			SocketChannel retVal = SocketChannel.open();
			return retVal;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void connectToServer(String aServerHost, int aServerPort) {
//		createCommunicationObjects();
		// no listeners need to be registered, assuming writes go through
		connectToSocketChannel(aServerHost, aServerPort);

	}

	protected void connectToSocketChannel(String aServerHost, int aServerPort) {
		try {
			InetAddress aServerAddress = InetAddress.getByName(aServerHost);
			NIOManagerFactory.getSingleton().connect(socketChannel,
					aServerAddress, aServerPort, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connected(SocketChannel theSocketChannel) {
	
		//NIOManagerFactory.getSingleton().enableReads(socketChannel);
		addServerListeners(theSocketChannel);
		System.out.println("Ready to send messages to server");
	
	}

	@Override
	public void notConnected(SocketChannel theSocketChannel, Exception e) {
		System.err.println("Could not connect:" + theSocketChannel);
		if (e != null)
		   e.printStackTrace();
	}
	
	protected void createCommunicationObjects() {
		createSender();
		
	}
	
	protected void createSender() {
		simpleClientSender = new ASimpleNIOClientSender(socketChannel,
				clientName);
	}
	
	protected void launchConsole() {
		AClientParameterListener dynamicInput = new AClientParameterListener(this);
		SimulationParametersController aSimulationParametersController = 
				new ASimulationParametersController();
		 
		aSimulationParametersController.addSimulationParameterListener(dynamicInput);
		
		aSimulationParametersController.processCommands();
	}
	
	public static void launchClient(String aServerHost, int aServerPort,
			String aClientName, String atomic) {
		/*
		 * Put these two in your clients also
		 */
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		SimpleNIOClient aClient = new ASimpleNIOClient(
				aClientName);
		aClient.initialize(aServerHost, aServerPort);
	}

	
	
	public static void main(String[] args) {
		
		launchClient(ClientArgsProcessor.getServerHost(args),
				ClientArgsProcessor.getServerPort(args),
				ClientArgsProcessor.getClientName(args),
				ClientArgsProcessor.getHeadless(args));
		
	}


	@Override
	public void setAtomic(Boolean value) {
		this.isAtomic = value;
		
		if(value) {
			commandInput.setConnectedToSimulation(false);
		}
		else {
			commandInput.setConnectedToSimulation(true);
		}
	}
	
	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
		
		simpleClientSender.setLocal(this.isLocal);
		aReadThread.setAtomic(this.isLocal);
	}


	@Override
	public Boolean getAtomic() {
		// TODO Auto-generated method stub
		return this.isAtomic;
	}
	
	public HalloweenCommandProcessor getCommandProcessor() {
		return this.commandInput;
	}

	public HalloweenSimulation getSimulation() {
		return this.simulation;
	}



	
}
