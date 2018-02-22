package assignments.util.mainArgs;

import java.rmi.registry.Registry;
import java.util.Arrays;

/** 
 * Example server args supported by this library:
 * 
 * 9001
 * 
 * 
 * Pass the main args of your server to the various methods of this class to
 * get the specified or defiult values * 
 *
 */

/**
 * Extracts the server port from argument #0,  if it exists, returns default
 * port otherwise
 */
public class ServerArgsProcessor {
	public static final int PORT_ARG_INDEX = 0;	
	public static final int REGISTRY_HOST_ARG_INDEX = 1;
	public static final int REGISTRY_PORT_ARG_INDEX = 2;


	public static String[] removeEmpty(String[] args) {
		return Arrays.stream(args).filter(s -> !s.isEmpty()).toArray(String[]::new);
	}
	
	public static int getServerPort(String[] args){
		return args.length > PORT_ARG_INDEX ?
				Integer.parseInt(args[PORT_ARG_INDEX]):
					ServerPort.SERVER_PORT;
	}	
	/**
	 * Extracts the server hostname from argument #0,  if it exists, returns default
	 * hostname (localhost) otherwise
	 */
	public static String getRegistryHost(String[] args){
		return args.length > REGISTRY_HOST_ARG_INDEX?
				args[REGISTRY_HOST_ARG_INDEX]:
				"localhost";
	}
	public static int getRegistryPort(String[] args){
		return args.length > REGISTRY_PORT_ARG_INDEX ?
				Integer.parseInt(args[REGISTRY_PORT_ARG_INDEX]):
					Registry.REGISTRY_PORT;
	}
}
