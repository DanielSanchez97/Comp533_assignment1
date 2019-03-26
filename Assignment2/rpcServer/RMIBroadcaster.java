package rpcServer;


import java.rmi.Remote;
import java.rmi.RemoteException;

import rpcClient.RMICommandProcessor;




public interface RMIBroadcaster extends Remote {
	
	public void Initialize(ARMIServer server) throws RemoteException;
	
	public void Broadcast(String Command, int id) throws RemoteException;
	
	public void setIPC(rpcClient.RMIClient.IPC state) throws RemoteException;
	
	public void setBroadcast(rpcClient.RMIClient.Broadcast state) throws RemoteException;
	
	public int Register(RMICommandProcessor CommandTool) throws RemoteException;
		
	public void setMetaState(boolean newValue) throws RemoteException;
	
}
