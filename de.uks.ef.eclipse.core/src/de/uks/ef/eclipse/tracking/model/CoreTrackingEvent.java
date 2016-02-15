package de.uks.ef.eclipse.tracking.model;

import de.uks.ef.eclipse.core.utils.Constants;

public abstract class CoreTrackingEvent extends EclipseTrackingEvent
{
   public CoreTrackingEvent(final String timestamp, final String stepId)
   {
      super(timestamp, stepId);
   }

}
