package simpleMVC;

import java.beans.PropertyChangeEvent;

public class ASimpleView implements SimpleView{

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		System.out.println("Label: " + evt.getPropertyName() + " Value: " + evt.getOldValue());
		System.out.println("/n");
	}

}
