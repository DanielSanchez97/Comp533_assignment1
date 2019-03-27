package rpcClient;

import simpleClient.ASimpleNIOClient;

public class ACommandThread extends Thread{
	private ASimpleNIOClient client;
	private String command;
	
	public ACommandThread(ASimpleNIOClient client, String command) {
		// TODO Auto-generated constructor stub
		this.client = client;
		this.command =command;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		client.getCommandProcessor().setInputString(command);
		
	}

}
