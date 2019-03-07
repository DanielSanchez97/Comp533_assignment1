package rpcClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClient extends Remote{
	enum IPC { NIO, RMI;}
	enum Broadcast {Atomic, NonAtomic, Local;}
	
	public void Initialize(int rPORT, int sPort, String host ,String name) throws RemoteException;

	
}
