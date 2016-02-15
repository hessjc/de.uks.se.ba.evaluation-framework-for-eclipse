package de.uks.ef.eclipse.tracking.commandtrackingmodul.core;

import de.uks.ef.eclipse.tracking.model.EclipseTrackingEvent;

public abstract class CommandTrackingEvent extends EclipseTrackingEvent
{
   public CommandTrackingEvent(final String timestamp, final String stepId)
   {
      super(timestamp, stepId);
   }

   protected String commandId;

   protected String getCommandId()
   {
      return commandId;
   }

   @Override
   protected String getTrackingModulID()
   {
      return CommandTrackingModul.ID;
   }
}