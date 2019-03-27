package simpleMVC;

import java.beans.PropertyChangeEvent;

public class ASimpleView implements SimpleView{

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getOldValue() == null) {
			System.out.println("Label: " + evt.getPropertyName() + " Value: " + evt.getOldValue());
		}
		else {
			System.out.println("OLD:   Label: " + evt.getPropertyName() + " Value: " + evt.getOldValue());
			System.out.println("NEW:   Label: " + evt.getPropertyName() + " Value: " + evt.getNewValue());
		}
		
	}

}
