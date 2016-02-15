package de.uks.ef.eclipse.tracking.breakpointtrackingmodul.report;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.core.model.EclipseReport;
import de.uks.ef.eclipse.report.configuration.ReportService;
import de.uks.ef.eclipse.report.model.EclipseReportModul;
import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.core.BreakpointReportModul;
import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.core.BreakpointTrackingEvent;

public class CountBreakpointModul extends EclipseReportModul implements BreakpointReportModul
{
   @Inject
   private IEclipseContext context;

   private ReportService reportService;

   public CountBreakpointModul()
   {
      this.reportService = ContextInjectionFactory.make(ReportService.class, context);
   }

   @Override
   public Collection<TrackingEvent> getFilteredEvents(EclipseReport eclipseReport)
   {
      Collection<TrackingEvent> filteredEvents = new ArrayList<TrackingEvent>();
      for (TrackingEvent trackingEvent : eclipseReport.getReportEvents())
      {
         if (trackingEvent instanceof BreakpointTrackingEvent && reportService.isFiltered(trackingEvent))
         {
            filteredEvents.add(trackingEvent);
         }
      }
      return filteredEvents;
   }
}