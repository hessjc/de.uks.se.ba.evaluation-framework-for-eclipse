package de.uks.ef.core.model;

import java.beans.PropertyChangeListener;

public interface Element
{
   public void addListener(PropertyChangeListener propertyChangeListener);

   public void fireNewValue(final String id, final Object newValue);
}
