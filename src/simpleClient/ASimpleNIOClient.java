package simpleClient;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;

import assignments.util.mainArgs.ClientArgsProcessor;

import inputport.nio.manager.NIOManagerFactory;

import inputport.nio.manager.factories.classes.AConnectCommandFactory;
import inputport.nio.manager.factories.classes.AReadingAcceptCommandFactory;
import inputport.nio.manager.factories.classes.AReadingWritingConnectCommandFactory;
import inputport.nio.manager.factories.selectors.AcceptCommandFactorySelector;
import inputport.nio.manager.factories.selectors.ConnectCommandFactorySelector;



import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import simpleMVC.ASimpleModel;
import simpleMVC.ASimpleView;
import simpleMVC.ASimpleController;
import simpleMVC.SimpleModel;
import simpleMVC.SimpleView;
import simpleMVC.SimpleController;

public class ASimpleNIOClient implements SimpleNIOClient{
	String clientName;
	SimpleNIOClientSender simpleClientSender;
	SimpleModel simpleModel;
	SimpleView simpleView;
	SimpleController simpleController;
	SocketChannel socketChannel;
	ASimpleClientReciever reciever;
	AWriteBufferListerner writeBufferLiserner = new AWriteBufferListerner();
	
	public ASimpleNIOClient(String aClientName) {
		clientName = aClientName;
	}
	
	protected void setFactories() {		
		ConnectCommandFactorySelector.setFactory(new AReadingWritingConnectCommandFactory());
		//AcceptCommandFactorySelector.setFactory(new AReadingAcceptCommandFactory());
	}
	
	public void initialize(String aServerHost, int aServerPort) {
		
		createModel();
		setFactories();
		socketChannel = createSocketChannel();
		createCommunicationObjects();
		addListeners();
		connectToServer(aServerHost, aServerPort);
		createUI();
	}
	
	protected void addListeners() {
		addModelListeners();	
	}
	
	protected void addServerListeners(SocketChannel asocketChannel) {
		
		reciever = new ASimpleClientReciever();
		addReadListeners(asocketChannel);
		NIOManagerFactory.getSingleton().addWriteBoundedBufferListener(asocketChannel, writeBufferLiserner);
		System.out.println("added read listener");
		
	}
	
	public void addReadListeners(SocketChannel asocketChannel) {
		NIOManagerFactory.getSingleton().addReadListener(asocketChannel,
				reciever);
	}
	
	protected void addModelListeners() {
		simpleModel.addPropertyChangeListener(simpleClientSender);
	}
	
	public void createModel() {
		simpleModel = new ASimpleModel();
	}
	
	public void createUI() {
		simpleView = new ASimpleView();
		simpleModel.addPropertyChangeListener(simpleView);
		simpleController = new ASimpleController(simpleModel);
		simpleController.processInput();
		
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
	
	public static void launchClient(String aServerHost, int aServerPort,
			String aClientName, Boolean atomic) {
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
				true);

	}

	
}
