package simpleClient;

import inputport.nio.manager.listeners.SocketChannelConnectListener;

public interface SimpleNIOClient extends SocketChannelConnectListener {

	public void connectToServer(String aServerHost, int aServerPort);
	public void initialize(String aServerHost, int aServerPort); 
	public void setAtomic(Boolean value);
	public Boolean getAtomic();
}
