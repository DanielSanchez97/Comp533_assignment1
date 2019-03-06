package regsitry;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import org.junit.jupiter.api.Tags;


public class ARMIRegistryLauncher implements RMIRegistryLauncher{
	
	public static void main(String[] args) {
		try{
			LocateRegistry.createRegistry(SERVER_PORT);
		
			Scanner sc = new Scanner(System.in);
			sc.next();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
