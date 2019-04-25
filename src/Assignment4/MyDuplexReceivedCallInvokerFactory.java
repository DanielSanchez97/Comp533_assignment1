package Assignment4;

import inputport.datacomm.duplex.DuplexInputPort;
import inputport.rpc.DuplexReceivedCallInvokerFactory;
import inputport.rpc.RPCRegistry;
import inputport.rpc.duplex.ADuplexReceivedCallInvoker;
import inputport.rpc.duplex.DuplexReceivedCallInvoker;
import inputport.rpc.duplex.LocalRemoteReferenceTranslator;

public class MyDuplexReceivedCallInvokerFactory implements DuplexReceivedCallInvokerFactory{

	@Override
	public DuplexReceivedCallInvoker createDuplexReceivedCallInvoker(LocalRemoteReferenceTranslator aRemoteHandler,
			DuplexInputPort<Object> aReplier, RPCRegistry anRPCRegistry) {
		// TODO Auto-generated method stub
		return new MyDuplexRecievedCallInvoker(aRemoteHandler, aReplier, anRPCRegistry);
	}

}
