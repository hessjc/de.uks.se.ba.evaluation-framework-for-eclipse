package de.uks.ef.eclipse.tracking.parttrackingmodul.event;

import de.uks.ef.eclipse.tracking.parttrackingmodul.core.PartTrackingEvent;

public class PartVisibleEvent extends PartTrackingEvent
{
   public PartVisibleEvent(final String timestamp, final String stepId, final String part)
   {
      super(timestamp, stepId);
      this.part = part;
   }

   @Override
   public String getEncoding()
   {
      return super.getEncoding() + ";PartVisibleEvent;" + part;
   }

   public static PartVisibleEvent parseEvent(final String eventString)
   {
      final String[] eventStrings = eventString.split(";");
      return new PartVisibleEvent(eventStrings[0], eventStrings[2], eventStrings[4]);
   }
}