package simpleDemo;

import assignments.util.mainArgs.ClientArgsProcessor;
import simpleClient.*;
import util.trace.port.nio.NIOTraceUtility;

public class AliceSimpleClient {
	public static void main(String[] args) {
		NIOTraceUtility.setTracing();
		System.out.println(ClientArgsProcessor.getServerHost(args));
		//System.out.println(ClientArgsProcessor.getServerPort(args));
		ASimpleNIOClient.launchClient(ClientArgsProcessor.getServerHost(args), 
				ClientArgsProcessor.getServerPort(args), 
				"Alice");
		
	
	}
}
