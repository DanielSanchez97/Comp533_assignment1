package inputport.rpc.duplex;

import java.util.HashMap;
import java.util.Map;

import examples.mvc.local.duplex.ACounter;
import inputport.rpc.GIPCRegistry;
import port.AnAbstractPortLauncher;
import port.PortAccessKind;
import port.PortKind;
import port.PortMessageKind;

public class AnAbstractDuplexRPCServerPortLauncher extends AnAbstractDuplexRPCPortLauncher {


//public class AnAbstractDuplexRPCServerPortLauncher extends AnAbstractPortLauncher implements GIPCRegistry  {
	protected Map<String, Object> registry = new HashMap();
	public AnAbstractDuplexRPCServerPortLauncher(String aServerName,
			String aServerPort) {
		super (aServerName, aServerPort);
	}	
//	@Override
//	protected PortAccessKind getPortAccessKind() {
//		return PortAccessKind.DUPLEX;
//	}	
	public AnAbstractDuplexRPCServerPortLauncher() {
	}	
	@Override
	public DuplexRPCServerInputPort getRPCServerPort() {
		return (DuplexRPCServerInputPort) getMainPort();
	}
	@Override
	public PortKind getPortKind() {
		return PortKind.SERVER_INPUT_PORT;
	}
//	@Override
//	protected PortMessageKind getPortMessageKind() {
//		return PortMessageKind.RPC;
//	}
//	@Override
//	protected void registerRemoteObjects() {		
//		for (String aKey:registry.keySet()) {
//			register(aKey, registry.get(aKey));
//		}
//	}
//	@Override
//	public void rebindAndExport(String aName, Object anObject) {
//		rebind(aName, anObject);
//		launch();
//	}
//	
//	public void rebind(String aName, Object anObject) {
//		registry.put(aName, anObject);		
//	}
//
//	public void exportObjects() {
//		launch();
//	}	
}
