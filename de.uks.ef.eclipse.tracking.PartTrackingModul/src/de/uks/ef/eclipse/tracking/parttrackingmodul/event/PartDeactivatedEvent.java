package de.uks.ef.eclipse.tracking.parttrackingmodul.event;

import de.uks.ef.eclipse.tracking.parttrackingmodul.core.PartTrackingEvent;

public class PartDeactivatedEvent extends PartTrackingEvent
{
   public PartDeactivatedEvent(final String timestamp, final String stepId, final String part)
   {
      super(timestamp, stepId);
      this.part = part;
   }

   @Override
   public String getEncoding()
   {
      return super.getEncoding() + ";PartDeactivatedEvent;" + part;
   }

   public static PartDeactivatedEvent parseEvent(final String eventString)
   {
      final String[] eventStrings = eventString.split(";");
      return new PartDeactivatedEvent(eventStrings[0], eventStrings[2], eventStrings[4]);
   }
}