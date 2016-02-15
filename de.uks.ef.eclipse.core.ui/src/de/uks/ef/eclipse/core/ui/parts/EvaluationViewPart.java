package de.uks.ef.eclipse.core.ui.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.uks.ef.core.utils.PreferenceHelper;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.configuration.StepService;
import de.uks.ef.eclipse.core.listener.EvaluationStateListener;
import de.uks.ef.eclipse.core.model.EclipseEvaluationLog;
import de.uks.ef.eclipse.core.ui.listener.EvaluationLogListener;
import de.uks.ef.eclipse.report.configuration.ReportService;
import de.uks.ef.eclipse.report.ui.listener.ReportEventListener;

@SuppressWarnings("restriction")
public class EvaluationViewPart implements EvaluationStateListener {
	@Inject
	private Logger LOGGER;

	@Inject
	private StepService stepService;

	@Inject
	private EvaluationService evaluationService;

	@Inject
	private ReportService reportService;

	@Inject
	private EclipseEvaluationLog evaluationLog;

	@Inject
	private EvaluationLogListener evaluationLogListener;

	@Inject
	private ReportEventListener reportEventListener;

	@Inject
	private PreferenceHelper preferenceHelper;

	private Composite composite;

	private EvaluationStartPart evaluationStartPart;

	private EvaluationStepPart evaluationStepPart;

	private EvaluationOverviewPart evaluationOverviewPart;

	@PostConstruct
	public void createComposite(final Composite parent) {
		composite = parent;
		evaluationService.registerEvaluationStateListener(this);
		evaluationShown();
	}

	@Override
	public void evaluationShown() {
		clearComposite();
		evaluationStartPart = new EvaluationStartPart();
		evaluationStartPart.createComposite(composite, evaluationService, LOGGER);

		stepService.addPropertyChangeListener(evaluationLogListener);
		evaluationService.addPropertyChangeListener(evaluationLogListener);

		LOGGER.debug("EvaluationStartPart shown.");
	}

	@Override
	public void evaluationStarted() {
		clearComposite();
		evaluationStepPart = new EvaluationStepPart();
		evaluationStepPart.createComposite(composite, evaluationService, stepService, evaluationLog);

		evaluationService.addPropertyChangeListener(reportEventListener);

		LOGGER.debug("EvaluationStepPart started.");
	}

	@Override
	public void evaluationStepped() {
		clearComposite();
		evaluationStepPart = new EvaluationStepPart();
		evaluationStepPart.createComposite(composite, evaluationService, stepService, evaluationLog);

		LOGGER.debug("EvaluationStepPart stepped.");
	}

	@Override
	public void evaluationFinished() {
		clearComposite();
		evaluationOverviewPart = new EvaluationOverviewPart();
		evaluationOverviewPart.createComposite(composite, evaluationService, reportService, preferenceHelper);

		evaluationService.removePropertyChangeListener(reportEventListener);
		evaluationService.removePropertyChangeListener(evaluationLogListener);
		stepService.removePropertyChangeListener(evaluationLogListener);

		LOGGER.debug("EvaluationOverviewPart shown.");
	}

	private void clearComposite() {
		if (!composite.isDisposed()) {
			for (final Control control : composite.getChildren()) {
				control.dispose();
			}
		}
	}

	public Composite getComposite() {
		return composite;
	}

	@PreDestroy
	private void cleanup() {
		evaluationService.unregisterEvaluationStateListener(this);
		evaluationService.removePropertyChangeListener(reportEventListener);
		evaluationService.removePropertyChangeListener(evaluationLogListener);
		stepService.removePropertyChangeListener(evaluationLogListener);
		LOGGER.debug("EvaluationViewPart destroyed.");
	}
}