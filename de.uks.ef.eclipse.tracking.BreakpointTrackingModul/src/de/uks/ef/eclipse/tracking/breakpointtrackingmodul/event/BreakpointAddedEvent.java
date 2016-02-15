package de.uks.ef.eclipse.tracking.breakpointtrackingmodul.event;

import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.core.BreakpointTrackingEvent;

public class BreakpointAddedEvent extends BreakpointTrackingEvent
{
   public BreakpointAddedEvent(final String timestamp, final String stepId, final String className,
         final String fileName, final String description)
   {
      super(timestamp, stepId, className, fileName, description);
   }

   @Override
   public String getEncoding()
   {
      return super.getEncoding() + ";BreakpointAddedEvent;" + className + ";" + fileName + ";" + description;
   }

   public static BreakpointAddedEvent parseEvent(final String eventString)
   {
      final String[] eventStrings = eventString.split(";");
      final String desc;
      if (eventStrings.length > 6)
      {
         desc = eventStrings[6];
      }
      else
      {
         desc = "";
      }
      return new BreakpointAddedEvent(eventStrings[0], eventStrings[2], eventStrings[4], eventStrings[5], desc);
   }
}