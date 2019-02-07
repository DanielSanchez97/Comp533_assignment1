package simpleMVC;

import util.models.PropertyListenerRegisterer;

public interface SimpleModel extends PropertyListenerRegisterer{
	String getValue();
	void setValue(String newValue);
}
