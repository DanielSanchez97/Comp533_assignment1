package Assignment4;

import java.awt.TrayIcon.MessageType;
import java.nio.ByteBuffer;

import util.session.ReceivedMessage;

public interface MyCustomDuplexObjectInputPort {
	public void addToBuffer(String aSourceName, ByteBuffer aMessage);
}
