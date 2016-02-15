package de.uks.ef.eclipse.tracking.breakpointtrackingmodul.listener;

import javax.inject.Inject;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.IBreakpointListener;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;

import de.uks.ef.core.utils.DateHelper;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.configuration.StepService;
import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.event.BreakpointAddedEvent;
import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.event.BreakpointChangedEvent;
import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.event.BreakpointRemovedEvent;
import de.uks.ef.eclipse.tracking.configuration.TrackingService;

@Creatable
public class BreakpointListener implements IBreakpointListener
{
   @Inject
   TrackingService trackingService;

   @Inject
   EvaluationService evaluationService;

   @Inject
   StepService stepService;

   @Inject
   IEclipseContext context;

   @Override
   public void breakpointAdded(IBreakpoint breakpoint)
   {
      try
      {
         String desc = getBreakpointDescription(breakpoint);
         trackingService.addTrackingEvent(evaluationService.getCurrentRunningEvaluation(),
               new BreakpointAddedEvent(DateHelper.getDate(), stepService.getCurrentRunningEvaluationStep().getId(),
                     breakpoint.getClass().getSimpleName(),
                     breakpoint.getMarker().getResource().getFullPath().toString(), desc));
      }
      catch (Exception e)
      {
      }
   }

   @Override
   public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta)
   {
      try
      {
         String desc = getBreakpointDescription(breakpoint);
         trackingService.addTrackingEvent(evaluationService.getCurrentRunningEvaluation(),
               new BreakpointRemovedEvent(DateHelper.getDate(), stepService.getCurrentRunningEvaluationStep().getId(),
                     breakpoint.getClass().getSimpleName(),
                     breakpoint.getMarker().getResource().getFullPath().toString(), desc));
      }
      catch (Exception e)
      {
      }
   }

   @Override
   public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta)
   {
      try
      {
         String desc = getBreakpointDescription(breakpoint);
         trackingService.addTrackingEvent(evaluationService.getCurrentRunningEvaluation(),
               new BreakpointChangedEvent(DateHelper.getDate(), stepService.getCurrentRunningEvaluationStep().getId(),
                     breakpoint.getClass().getSimpleName(),
                     breakpoint.getMarker().getResource().getFullPath().toString(), desc));
      }
      catch (Exception e)
      {
      }
   }

   private String getBreakpointDescription(IBreakpoint breakpoint)
   {
      String desc;
      try
      {
         Object attribute = breakpoint.getMarker()
               .getAttribute("com.yattasolutions.objectbrowser.VisualDebuggerBreakpoint.label");
         if (attribute != null)
         {
            desc = attribute.toString();
         }
         else
         {
            desc = "";
         }
      }
      catch (CoreException e)
      {
         desc = "";
      }
      return desc;
   }
}