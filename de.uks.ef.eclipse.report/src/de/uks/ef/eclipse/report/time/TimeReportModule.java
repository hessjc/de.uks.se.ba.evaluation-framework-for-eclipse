package de.uks.ef.eclipse.report.time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.Report;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.core.listener.time.event.StartStepEvent;
import de.uks.ef.eclipse.core.listener.time.event.StopStepEvent;
import de.uks.ef.eclipse.core.model.EclipseReport;
import de.uks.ef.eclipse.report.configuration.ReportService;
import de.uks.ef.eclipse.report.model.EclipseReportModul;

@Creatable
public class TimeReportModule extends EclipseReportModul
{
   @Inject
   private ReportService reportService;

   public TimeReportModule()
   {
      addReport(new StepExecutionTimePieChartReport());
   }
   
   @Override
   public Collection<TrackingEvent> getFilteredEvents(EclipseReport eclipseReport)
   {
      Collection<TrackingEvent> filteredEvents = new ArrayList<TrackingEvent>();
      for (TrackingEvent trackingEvent : eclipseReport.getReportEvents())
      {
         if ((trackingEvent instanceof StartStepEvent || trackingEvent instanceof StopStepEvent)
               && reportService.isFiltered(trackingEvent))
         {
            filteredEvents.add(trackingEvent);
         }
      }
      return filteredEvents;
   }
}