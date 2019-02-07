package simpleMVC;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import util.trace.bean.AddedPropertyChangeListener;
import util.trace.bean.NotifiedPropertyChangeEvent;
import util.trace.bean.SetProperty;

public class ASimpleModel implements SimpleModel{
	
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	protected String value = "";
	
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener aListener) {
	
		AddedPropertyChangeListener.newCase(this, aListener);
		propertyChangeSupport.addPropertyChangeListener(aListener);
	}

	@Override
	public String getValue() {
		
		return value;
	}

	@Override
	public void setValue(String newValue) {
		SetProperty.newCase(this,"Data", newValue);
		String oldValue = value;
		value = newValue;
		PropertyChangeEvent anEvent = new PropertyChangeEvent(this, "Data", oldValue, newValue);
		NotifiedPropertyChangeEvent.newCase(this, anEvent, propertyChangeSupport.getPropertyChangeListeners());
		propertyChangeSupport.firePropertyChange(anEvent);
	
	}
	
}
