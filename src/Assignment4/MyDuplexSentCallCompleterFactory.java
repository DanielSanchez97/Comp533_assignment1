package Assignment4;

import inputport.rpc.duplex.DuplexRPCInputPort;
import inputport.rpc.duplex.DuplexSentCallCompleter;
import inputport.rpc.duplex.DuplexSentCallCompleterFactory;
import inputport.rpc.duplex.LocalRemoteReferenceTranslator;

public class MyDuplexSentCallCompleterFactory  implements DuplexSentCallCompleterFactory {

	@Override
	public DuplexSentCallCompleter createDuplexSentCallCompleter(DuplexRPCInputPort anInputPort,
			LocalRemoteReferenceTranslator aTranslator) {
		// TODO Auto-generated method stub
		return new MyDuplexSentCallCompleter2(anInputPort,aTranslator);
	}
	
	

}
