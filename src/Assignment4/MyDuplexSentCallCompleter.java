package Assignment4;

import inputport.InputPort;
import inputport.datacomm.duplex.DuplexClientInputPort;
import inputport.datacomm.duplex.DuplexInputPort;
import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import inputport.rpc.duplex.ADuplexSentCallCompleter;
import inputport.rpc.duplex.LocalRemoteReferenceTranslator;
import inputport.rpc.duplex.RPCReturnValue;
import util.trace.port.rpc.ReceivedObjectTransformed;
import util.trace.port.rpc.RemoteCallReceivedReturnValue;
import util.trace.port.rpc.RemoteCallWaitingForReturnValue;


public class MyDuplexSentCallCompleter extends ADuplexSentCallCompleter{
	DuplexInputPort Myport;
	
	public MyDuplexSentCallCompleter(InputPort anInputPort, LocalRemoteReferenceTranslator aRemoteHandler) {
		super(anInputPort, aRemoteHandler);
		Myport = (DuplexInputPort) anInputPort;
		// TODO Auto-generated constructor stub
	}

	@Override 
	protected Object waitForReturnValue(String aRemoteEndPoint) {
		return Myport.receive(aRemoteEndPoint);
	}
	
	
	@Override
	protected Object getReturnValueOfRemoteFunctionCall (String aRemoteEndPoint, Object aMessage)  {
		//System.out.println("hello");
		RemoteCallWaitingForReturnValue.newCase(this);
		ReceiveReturnMessage<Object> retval;
		
		Object val;
		while(true) {
			val =  waitForReturnValue(aRemoteEndPoint);
			RemoteCallReceivedReturnValue.newCase(this, val);
			if(val instanceof ReceiveReturnMessage) {
				
				retval = (ReceiveReturnMessage) val;
				break;
			}
			
		}
		
		
		//ReceivedObjectTransformed.newCase(this, val, temp);
		return retval.getMessage();
	}
	
	@Override
	protected void returnValueReceived(String source, Object message) {
		//do nothing
	}
	
}
