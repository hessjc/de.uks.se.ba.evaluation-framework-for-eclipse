package de.uks.ef.eclipse.core.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import de.uks.ef.core.model.Element;

public class EclipseElement implements Element {
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	@Override
	public void addListener(final PropertyChangeListener propertyChangeListener) {
		for (PropertyChangeListener listener : propertyChangeSupport.getPropertyChangeListeners()) {
			if (listener == propertyChangeListener) {
				return;
			}
		}
		propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}

	@Override
	public void fireNewValue(final String id, final Object newValue) {
		propertyChangeSupport.firePropertyChange(new PropertyChangeEvent(this, id, null, newValue));
	}
}
