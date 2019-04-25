package Assignment4;

import inputport.datacomm.duplex.DuplexInputPort;
import inputport.rpc.RPCRegistry;
import inputport.rpc.duplex.ADuplexReceivedCallInvoker;
import inputport.rpc.duplex.LocalRemoteReferenceTranslator;

public class MyDuplexRecievedCallInvoker extends ADuplexReceivedCallInvoker {

	public MyDuplexRecievedCallInvoker(LocalRemoteReferenceTranslator aLocalRemoteReferenceTranslator,
			DuplexInputPort<Object> aReplier, RPCRegistry theRPCRegistry) {
		super(aLocalRemoteReferenceTranslator, aReplier, theRPCRegistry);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void handleProcedureReturn(String sender, Exception e) {
		replier.reply(
				sender,
				super.createRPCReturnValue(null));
		return;
	}
}
