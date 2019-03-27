package simpleClient;

import java.beans.PropertyChangeListener;

public interface SimpleNIOClientSender extends PropertyChangeListener{
	public void setLocal(boolean local);
}
