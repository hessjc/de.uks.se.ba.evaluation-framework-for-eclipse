package de.uks.ef.eclipse.tracking.parttrackingmodul.core;

import de.uks.ef.eclipse.tracking.model.EclipseTrackingEvent;

public abstract class PartTrackingEvent extends EclipseTrackingEvent
{
   public PartTrackingEvent(final String timestamp, final String stepId)
   {
      super(timestamp, stepId);
   }

   protected String part;

   protected String getPart()
   {
      return part;
   }

   @Override
   protected String getTrackingModulID()
   {
      return PartTrackingModul.ID;
   }
}