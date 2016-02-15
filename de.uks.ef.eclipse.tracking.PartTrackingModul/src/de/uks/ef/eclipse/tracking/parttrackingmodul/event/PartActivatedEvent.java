package de.uks.ef.eclipse.tracking.parttrackingmodul.event;

import de.uks.ef.eclipse.tracking.parttrackingmodul.core.PartTrackingEvent;

public class PartActivatedEvent extends PartTrackingEvent
{
   public PartActivatedEvent(final String timestamp, final String stepId, final String part)
   {
      super(timestamp, stepId);
      this.part = part;
   }

   @Override
   public String getEncoding()
   {
      return super.getEncoding() + ";PartActivatedEvent;" + part;
   }

   public static PartActivatedEvent parseEvent(final String eventString)
   {
      final String[] eventStrings = eventString.split(";");
      return new PartActivatedEvent(eventStrings[0], eventStrings[2], eventStrings[4]);
   }
}