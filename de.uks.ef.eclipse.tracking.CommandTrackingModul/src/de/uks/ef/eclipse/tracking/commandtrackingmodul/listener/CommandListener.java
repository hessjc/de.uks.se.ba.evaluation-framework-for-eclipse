package de.uks.ef.eclipse.tracking.commandtrackingmodul.listener;

import javax.inject.Inject;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;

import de.uks.ef.core.utils.DateHelper;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.configuration.StepService;
import de.uks.ef.eclipse.tracking.commandtrackingmodul.event.NotHandledEvent;
import de.uks.ef.eclipse.tracking.commandtrackingmodul.event.PostExecuteFailureEvent;
import de.uks.ef.eclipse.tracking.commandtrackingmodul.event.PostExecuteSuccessEvent;
import de.uks.ef.eclipse.tracking.commandtrackingmodul.event.PreExecuteEvent;
import de.uks.ef.eclipse.tracking.configuration.TrackingService;

@Creatable
public class CommandListener implements IExecutionListener {
	@Inject
	TrackingService trackingService;

	@Inject
	EvaluationService evaluationService;

	@Inject
	IEclipseContext context;
	
	@Inject
	StepService stepService;

	@Override
	public void notHandled(String commandId, NotHandledException exception) {
//		trackingService.addTrackingEvent(evaluationService.getCurrentRunningEvaluation(),
//				new NotHandledEvent(DateHelper.getDate(), stepService.getCurrentRunningEvaluationStep().getId(), commandId, exception.getMessage()));
	}

	@Override
	public void postExecuteFailure(String commandId, ExecutionException exception) {
		try
      {
         trackingService.addTrackingEvent(evaluationService.getCurrentRunningEvaluation(),
         		new PostExecuteFailureEvent(DateHelper.getDate(), stepService.getCurrentRunningEvaluationStep().getId(), commandId, exception.getMessage()));
      }
      catch (Exception e)
      {
      }
	}

	@Override
	public void postExecuteSuccess(String commandId, Object returnValue) {
		try
      {
         trackingService.addTrackingEvent(evaluationService.getCurrentRunningEvaluation(),
         		new PostExecuteSuccessEvent(DateHelper.getDate(), stepService.getCurrentRunningEvaluationStep().getId(), commandId));
      }
      catch (Exception e)
      {
      }
	}

	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
		try
      {
         trackingService.addTrackingEvent(evaluationService.getCurrentRunningEvaluation(),
         		new PreExecuteEvent(DateHelper.getDate(), stepService.getCurrentRunningEvaluationStep().getId(), commandId));
      }
      catch (Exception e)
      {
      }
	}
}