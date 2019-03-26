package regsitry;

import java.rmi.registry.LocateRegistry;

import java.util.Scanner;
import util.annotations.Tags;
import util.tags.DistributedTags;


@Tags({DistributedTags.REGISTRY, DistributedTags.RMI})
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
