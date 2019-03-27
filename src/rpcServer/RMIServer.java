package rpcServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServer extends Remote{

	public void initialize(int rPORT, int nPort, String host) throws RemoteException;

	
	
}
