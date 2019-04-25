package Assignment4;

import inputport.InputPort;
import inputport.datacomm.duplex.DuplexInputPort;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import inputport.rpc.duplex.LocalRemoteReferenceTranslator;
import util.trace.port.rpc.RemoteCallReceivedReturnValue;
import util.trace.port.rpc.RemoteCallWaitingForReturnValue;

public class MyDuplexSentCallCompleter2 extends MyDuplexSentCallCompleter {
	DuplexInputPort Myport;
	
	public MyDuplexSentCallCompleter2(InputPort anInputPort, LocalRemoteReferenceTranslator aRemoteHandler) {
		super(anInputPort, aRemoteHandler);
		Myport = (DuplexInputPort) anInputPort;
	}
	
	@Override
	protected Object getReturnValueOfRemoteProcedureCall(String aRemoteEndPoint, Object aMessage){
		RemoteCallWaitingForReturnValue.newCase(this);
		ReceiveReturnMessage<Object> retval;
		
		Object val;
		while(true) {
			val =  super.waitForReturnValue(aRemoteEndPoint);
			RemoteCallReceivedReturnValue.newCase(this, val);
			if(val instanceof ReceiveReturnMessage) {
				
				retval = (ReceiveReturnMessage) val;
				break;
			}
			
		}
		
		//ReceivedObjectTransformed.newCase(this, val, temp);
		return retval.getMessage();
	}
}
