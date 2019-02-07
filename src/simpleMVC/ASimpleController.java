package simpleMVC;

import java.util.Scanner;

public class ASimpleController implements SimpleController {

	SimpleModel model;
	Scanner scanner = new Scanner(System.in);
	
	public ASimpleController(SimpleModel aModel) {
		model = aModel;
	}
	
	@Override
	public void processInput() {
		// TODO Auto-generated method stub
		while(true) {
			System.out.println("Enter a new value for the data");
			String anInput = scanner.nextLine();
			
			model.setValue(anInput);
		}
		
	}
	
}
