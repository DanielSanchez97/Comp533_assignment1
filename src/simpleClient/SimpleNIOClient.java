package simpleClient;

import inputport.nio.manager.listeners.SocketChannelConnectListener;

public interface SimpleNIOClient extends SocketChannelConnectListener {
	public void createModel();
	public void createUI();
	public void connectToServer(String aServerHost, int aServerPort);
	public void initialize(String aServerHost, int aServerPort, Boolean isAtomic); 
	public void setAtmoic(Boolean value);
	public Boolean getAtmomic();
}
