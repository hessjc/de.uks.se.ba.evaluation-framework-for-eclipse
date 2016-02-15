package de.uks.ef.eclipse.tracking.commandtrackingmodul.event;

import de.uks.ef.eclipse.tracking.commandtrackingmodul.core.CommandTrackingEvent;

public class NotHandledEvent extends CommandTrackingEvent
{
   private final String exceptionMessage;

   public NotHandledEvent(final String timestamp, final String stepId, final String commandId, final String exceptionMessage)
   {
      super(timestamp, stepId);
      this.commandId = commandId;
      this.exceptionMessage = exceptionMessage;
   }

   @Override
   public String getEncoding()
   {
      return super.getEncoding() + ";NotHandledEvent;" + commandId + ";" + exceptionMessage;
   }

   public static NotHandledEvent parseEvent(final String eventString)
   {
      final String[] eventStrings = eventString.split(";");
      return new NotHandledEvent(eventStrings[0], eventStrings[2], eventStrings[4], eventStrings[5]);
   }
}