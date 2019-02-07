package simpleServerDemo.Servers;



import assignments.util.mainArgs.ServerPort;
import inputport.nio.manager.listeners.SocketChannelAcceptListener;


public interface SimpleNIOServer extends ServerPort, SocketChannelAcceptListener {
	public void initialize(int aServerPort); 
}
