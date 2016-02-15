package de.uks.ef.eclipse.core.listener.time.event;

import de.uks.ef.eclipse.core.utils.Constants;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingEvent;

public class StartStepEvent extends EclipseTrackingEvent
{
   public static StartStepEvent parseEvent(final String eventString)
   {
      final String[] eventStrings = eventString.split(";");
      return new StartStepEvent(eventStrings[2], eventStrings[0]);
   }

   public StartStepEvent(String stepId, String startTime)
   {
      super(startTime, stepId);
   }

   @Override
   public String getEncoding()
   {
      return super.getEncoding() + ";" + StartStepEvent.class.getSimpleName();
   }

   @Override
   protected String getTrackingModulID()
   {
      return Constants.TIME_TRACKING;
   }

}
