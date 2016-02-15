package de.uks.ef.eclipse.tracking.commandtrackingmodul.event;

import de.uks.ef.eclipse.tracking.commandtrackingmodul.core.CommandTrackingEvent;

public class PostExecuteSuccessEvent extends CommandTrackingEvent
{
   public PostExecuteSuccessEvent(final String timestamp, final String stepId, final String commandId)
   {
      super(timestamp, stepId);
      this.commandId = commandId;
   }

   @Override
   public String getEncoding()
   {
      return super.getEncoding() + ";PostExecuteSuccessEvent;" + commandId;
   }

   public static PostExecuteSuccessEvent parseEvent(final String eventString)
   {
      final String[] eventStrings = eventString.split(";");
      return new PostExecuteSuccessEvent(eventStrings[0], eventStrings[2], eventStrings[4]);
   }
}