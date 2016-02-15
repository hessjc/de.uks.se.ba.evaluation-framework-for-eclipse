package de.uks.ef.core.databinding;

import java.beans.PropertyChangeListener;

public interface PropertyChangeModel
{
   public void addPropertyChangeListener(PropertyChangeListener listener);

   public void removePropertyChangeListener(PropertyChangeListener listener);

   public void firePropertyChange(String propertyName, Object oldValue, Object newValue);
}