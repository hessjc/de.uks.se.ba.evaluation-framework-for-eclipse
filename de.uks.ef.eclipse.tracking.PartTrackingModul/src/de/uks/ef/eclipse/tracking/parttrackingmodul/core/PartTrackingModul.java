package de.uks.ef.eclipse.tracking.parttrackingmodul.core;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.report.configuration.ExtensionReportLoader;
import de.uks.ef.eclipse.report.model.EclipseChartReport;
import de.uks.ef.eclipse.report.model.EclipseReportModul;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingModul;
import de.uks.ef.eclipse.tracking.parttrackingmodul.event.PartActivatedEvent;
import de.uks.ef.eclipse.tracking.parttrackingmodul.event.PartDeactivatedEvent;
import de.uks.ef.eclipse.tracking.parttrackingmodul.event.PartVisibleEvent;
import de.uks.ef.eclipse.tracking.parttrackingmodul.listener.PartListener;

public class PartTrackingModul extends EclipseTrackingModul
{
   public static String ID = "de.uks.ef.eclipse.tracking.parttrackingmodul.core.PartTrackingModul"; //$NON-NLS-1$

   private final String EXTENSION_POINT_PARTTRACKINGMODUL = "de.uks.ef.eclipse.tracking.PartTrackingModul"; //$NON-NLS-1$

   @Inject
   private ExtensionReportLoader extensionReportLoader;

   @Inject
   private EPartService partService;

   @Inject
   private PartListener partListener;

   @Override
   public String getId()
   {
      return ID;
   }

   public IPartListener getPartListener()
   {
      return partListener;
   }

   @Override
   public void initReportModuls()
   {
      try
      {
         Collection<EclipseReportModul> reportModuls = extensionReportLoader
               .initialiseReportModul(EXTENSION_POINT_PARTTRACKINGMODUL);
         Map<String, Collection<EclipseChartReport>> reports = extensionReportLoader
               .initaliseReporting(EXTENSION_POINT_PARTTRACKINGMODUL);
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
      partService.addPartListener(getPartListener());
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
      partService.removePartListener(getPartListener());
   }

   @Override
   public TrackingEvent parseEvent(String eventString)
   {
      String[] eventStrings = eventString.split(";");
      TrackingEvent event = null;
      switch (eventStrings[3])
      {
         case "PartActivatedEvent":
            event = PartActivatedEvent.parseEvent(eventString);
            break;
         case "PartDeactivatedEvent":
            event = PartDeactivatedEvent.parseEvent(eventString);
            break;
         case "PartVisibleEvent":
            event = PartVisibleEvent.parseEvent(eventString);
            break;
      }
      return event;
   }
}