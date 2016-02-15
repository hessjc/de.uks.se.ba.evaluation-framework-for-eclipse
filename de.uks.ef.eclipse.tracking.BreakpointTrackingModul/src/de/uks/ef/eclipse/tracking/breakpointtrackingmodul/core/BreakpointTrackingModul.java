package de.uks.ef.eclipse.tracking.breakpointtrackingmodul.core;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.report.configuration.ExtensionReportLoader;
import de.uks.ef.eclipse.report.model.EclipseChartReport;
import de.uks.ef.eclipse.report.model.EclipseReportModul;
import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.event.BreakpointAddedEvent;
import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.event.BreakpointChangedEvent;
import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.event.BreakpointRemovedEvent;
import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.listener.BreakpointListener;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingModul;

public class BreakpointTrackingModul extends EclipseTrackingModul
{
   public static String ID = "de.uks.ef.eclipse.tracking.breakpointtrackingmodul.core.BreakpointTrackingModul";

   private final String EXTENSION_POINT_BREAKPOINTTRACKINGMODUL = "de.uks.ef.eclipse.tracking.BreakpointTrackingModul"; //$NON-NLS-1$

   @Inject
   private ExtensionReportLoader extensionReportLoader;

   @Inject
   private BreakpointListener breakpointListener;

   @Override
   public String getId()
   {
      return ID;
   }

   @Override
   public void initReportModuls()
   {
      try
      {
         Collection<EclipseReportModul> reportModuls = extensionReportLoader
               .initialiseReportModul(EXTENSION_POINT_BREAKPOINTTRACKINGMODUL);
         Map<String, Collection<EclipseChartReport>> reports = extensionReportLoader
               .initaliseReporting(EXTENSION_POINT_BREAKPOINTTRACKINGMODUL);
         for (EclipseReportModul reportModul : reportModuls)
         {
            addReportModul(reportModul);

            Collection<EclipseChartReport> set = reports.get(reportModul.getClass().getSimpleName());

            for (EclipseChartReport report : set)
            {
               reportModul.addReport(report);
               report.setReportModul(reportModul);
            }
         }
      }
      catch (CoreException e1)
      {
         e1.printStackTrace();
      }
   }

   @Override
   public void initListener()
   {
      DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(breakpointListener);
   }

   @Override
   public void startTracking()
   {
      super.startTracking();
      initListener();
   }

   @Override
   public void stopTracking()
   {
      super.stopTracking();
      DebugPlugin.getDefault().getBreakpointManager().removeBreakpointListener(breakpointListener);
   }

   @Override
   public TrackingEvent parseEvent(String eventString)
   {
      String[] eventStrings = eventString.split(";");
      TrackingEvent event = null;
      switch (eventStrings[3])
      {
         case "BreakpointAddedEvent":
            event = BreakpointAddedEvent.parseEvent(eventString);
            break;
         case "BreakpointChangedEvent":
            event = BreakpointChangedEvent.parseEvent(eventString);
            break;
         case "BreakpointRemovedEvent":
            event = BreakpointRemovedEvent.parseEvent(eventString);
      }
      return event;
   }
}