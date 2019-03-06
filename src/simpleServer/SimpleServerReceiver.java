package simpleServer;

import inputport.nio.manager.listeners.SocketChannelReadListener;

public interface SimpleServerReceiver extends SocketChannelReadListener{
	public void setListening(boolean value);
}
