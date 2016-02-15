package de.uks.ef.eclipse.tracking.breakpointtrackingmodul.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.report.core.EclipsePieChartReport;
import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.core.BreakpointReport;
import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.event.BreakpointAddedEvent;

public class CountBreakpointPieChartReport extends EclipsePieChartReport implements BreakpointReport
{
   @Override
   public String getTitle()
   {
      return "Created breakpoint types";
   }

   @Override
   public Map<String, Integer> getData(Collection<TrackingEvent> events)
   {
      Map<String, Integer> breakpoints = new HashMap<String, Integer>();
      for (TrackingEvent event : events)
      {
         if (event instanceof BreakpointAddedEvent)
         {
            Integer count;
            String breakpointType = event.getEncoding().split("\\;")[4];
            if (breakpoints.get(breakpointType) != null)
            {
               count = breakpoints.get(breakpointType).intValue() + 1;
            }
            else
            {
               count = 1;
            }
            breakpoints.put(breakpointType, count);
         }
      }
      for (Entry<String, Integer> command : breakpoints.entrySet())
      {
         breakpoints.put(command.getKey(), command.getValue());
      }
      return breakpoints;
   }
}