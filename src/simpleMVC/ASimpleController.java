package simpleMVC;

import java.util.Scanner;

public class ASimpleController implements SimpleController {

	SimpleModel model;
	Scanner scanner = new Scanner(System.in);
	private Boolean listen = true;
	
	public ASimpleController(SimpleModel aModel) {
		model = aModel;
	}
	
	public void setListen(Boolean listen) {
		this.listen = listen;
	}
	
	@Override
	public void processInput() {
		// TODO Auto-generated method stub
		while(listen) {
			System.out.println("Enter a new value for the data");
			String anInput = scanner.nextLine();
			
			listen = false;
			model.setValue(anInput);
		}
		
	}
	
}
