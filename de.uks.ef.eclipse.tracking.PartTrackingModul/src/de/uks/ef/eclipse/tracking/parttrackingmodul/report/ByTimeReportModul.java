package de.uks.ef.eclipse.tracking.parttrackingmodul.report;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.core.model.EclipseReport;
import de.uks.ef.eclipse.report.configuration.ReportService;
import de.uks.ef.eclipse.report.model.EclipseReportModul;
import de.uks.ef.eclipse.tracking.parttrackingmodul.core.PartReportModul;
import de.uks.ef.eclipse.tracking.parttrackingmodul.core.PartTrackingEvent;

public class ByTimeReportModul extends EclipseReportModul implements PartReportModul
{
   @Inject
   private IEclipseContext context;

   private ReportService reportService;

   public ByTimeReportModul()
   {
      this.reportService = ContextInjectionFactory.make(ReportService.class, context);
   }

   @Override
   public Collection<TrackingEvent> getFilteredEvents(EclipseReport eclipseReport)
   {
      Collection<TrackingEvent> filteredEvents = new ArrayList<TrackingEvent>();
      for (TrackingEvent trackingEvent : eclipseReport.getReportEvents())
      {
         if (trackingEvent instanceof PartTrackingEvent && reportService.isFiltered(trackingEvent))
         {
            filteredEvents.add(trackingEvent);
         }
      }
      return filteredEvents;
   }
}