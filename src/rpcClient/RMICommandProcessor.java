package rpcClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rpcClient.RMIClient.Broadcast;
import rpcClient.RMIClient.IPC;

public interface RMICommandProcessor extends Remote {
	public void Initialize(ARMIClient client) throws RemoteException;
	public void runCommand(String command) throws RemoteException;
	public void setIPC(IPC newMode) throws RemoteException;
	public void setBroadcast(Broadcast newMode) throws RemoteException;
	public void setMetaState(boolean newValue) throws RemoteException;
	public boolean voteBroadcast(Broadcast newMode) throws RemoteException;
	public boolean voteIPC(IPC newMode) throws RemoteException;
	public void print() throws RemoteException;
}
