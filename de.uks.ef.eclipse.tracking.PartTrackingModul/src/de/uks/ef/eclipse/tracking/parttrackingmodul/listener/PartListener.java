package de.uks.ef.eclipse.tracking.parttrackingmodul.listener;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;

import de.uks.ef.core.utils.DateHelper;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.configuration.StepService;
import de.uks.ef.eclipse.tracking.configuration.TrackingService;
import de.uks.ef.eclipse.tracking.parttrackingmodul.event.PartActivatedEvent;
import de.uks.ef.eclipse.tracking.parttrackingmodul.event.PartDeactivatedEvent;
import de.uks.ef.eclipse.tracking.parttrackingmodul.event.PartVisibleEvent;

@Creatable
@Singleton
public class PartListener implements IPartListener {
	@Inject
	TrackingService trackingService;

	@Inject
	EvaluationService evaluationService;
	
	@Inject
	StepService stepService;

	@Override
	public void partActivated(MPart part) {
		try
      {
         trackingService.addTrackingEvent(evaluationService.getCurrentRunningEvaluation(),
         		new PartActivatedEvent(DateHelper.getDate(), stepService.getCurrentRunningEvaluationStep().getId(), part.getElementId()));
      }
      catch (Exception e)
      {
      }
	}

	@Override
	public void partBroughtToTop(MPart part) {

	}

	@Override
	public void partDeactivated(MPart part) {
		try
      {
         trackingService.addTrackingEvent(evaluationService.getCurrentRunningEvaluation(),
         		new PartDeactivatedEvent(DateHelper.getDate(), stepService.getCurrentRunningEvaluationStep().getId(), part.getElementId()));
      }
      catch (Exception e)
      {
      }
	}

	@Override
	public void partHidden(MPart part) {

	}

	@Override
	public void partVisible(MPart part) {
		try
      {
         trackingService.addTrackingEvent(evaluationService.getCurrentRunningEvaluation(),
         		new PartVisibleEvent(DateHelper.getDate(), stepService.getCurrentRunningEvaluationStep().getId(), part.getElementId()));
      }
      catch (Exception e)
      {
      }
	}
}