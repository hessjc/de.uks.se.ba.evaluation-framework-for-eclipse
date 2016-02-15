package de.uks.ef.eclipse.tracking.commandtrackingmodul.core;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.report.configuration.ExtensionReportLoader;
import de.uks.ef.eclipse.report.model.EclipseChartReport;
import de.uks.ef.eclipse.report.model.EclipseReportModul;
import de.uks.ef.eclipse.tracking.commandtrackingmodul.event.NotHandledEvent;
import de.uks.ef.eclipse.tracking.commandtrackingmodul.event.PostExecuteFailureEvent;
import de.uks.ef.eclipse.tracking.commandtrackingmodul.event.PostExecuteSuccessEvent;
import de.uks.ef.eclipse.tracking.commandtrackingmodul.event.PreExecuteEvent;
import de.uks.ef.eclipse.tracking.commandtrackingmodul.listener.CommandListener;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingModul;

@Creatable
@Singleton
public class CommandTrackingModul extends EclipseTrackingModul
{
   public static String ID = "de.uks.ef.eclipse.tracking.commandtrackingmodul.core.CommandTrackingModul"; //$NON-NLS-1$

   private final String EXTENSION_POINT_COMMANDTRACKINGMODUL = "de.uks.ef.eclipse.tracking.CommandTrackingModul"; //$NON-NLS-1$

   @Inject
   private ExtensionReportLoader extensionReportLoader;

   @Inject
   private CommandListener exec;

   @Override
	public String getId() {
		return ID;
	}
   
   @Override
   public void initReportModuls()
   {
      try
      {
    	  Collection<EclipseReportModul> reportModuls = extensionReportLoader
               .initialiseReportModul(EXTENSION_POINT_COMMANDTRACKINGMODUL);
         Map<String, Collection<EclipseChartReport>> reports = extensionReportLoader
               .initaliseReporting(EXTENSION_POINT_COMMANDTRACKINGMODUL);
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
      try
      {
         ICommandService service = (ICommandService)PlatformUI.getWorkbench().
               getService(ICommandService.class);
         service.addExecutionListener(exec);
      }
      catch (Exception e)
      {
         System.out.println(e);
      }
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
      ICommandService service = (ICommandService)PlatformUI.getWorkbench().
            getService(ICommandService.class);
      service.removeExecutionListener(exec);
   }

   @Override
   public TrackingEvent parseEvent(String eventString)
   {
      String[] eventStrings = eventString.split(";");
      TrackingEvent event = null;
      switch (eventStrings[3])
      {
         case "PostExecuteFailureEvent":
            event = PostExecuteFailureEvent.parseEvent(eventString);
            break;
         case "PostExecuteSuccessEvent":
            event = PostExecuteSuccessEvent.parseEvent(eventString);
            break;
         case "PreExecuteEvent":
            event = PreExecuteEvent.parseEvent(eventString);
            break;
         case "NotHandledEvent":
            event = NotHandledEvent.parseEvent(eventString);
            break;
      }
      return event;
   }
}