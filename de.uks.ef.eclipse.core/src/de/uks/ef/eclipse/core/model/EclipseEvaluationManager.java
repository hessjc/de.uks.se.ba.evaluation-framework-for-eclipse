package de.uks.ef.eclipse.core.model;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationConfiguration;
import de.uks.ef.core.model.EvaluationManager;
import de.uks.ef.core.model.TrackingManager;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingManager;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class EclipseEvaluationManager implements EvaluationManager {
	private static EclipseEvaluationManager INSTANCE;

	public static final EclipseEvaluationManager get() {
		return INSTANCE;
	}
	
	@Inject
	private Logger LOGGER;

	private Set<EvaluationConfiguration> evaluationConfiguration;
	
	@Inject
	private EclipseTrackingManager trackingManager;
	
	private String evaluationProjectName;

	@PostConstruct
	public void init() {
		INSTANCE = this;
		LOGGER.debug("EclipseEvaluationManager injected.");
	}

	@Override
	public Set<EvaluationConfiguration> getEvaluationConfiguration() {
		if (this.evaluationConfiguration == null) {
			this.evaluationConfiguration = new HashSet<EvaluationConfiguration>();
		}
		return this.evaluationConfiguration;
	}

	@Override
	public void addEvaluationConfiguration(EvaluationConfiguration evaluationConfiguration) {
		getEvaluationConfiguration().add(evaluationConfiguration);
	}

	@Override
	public Evaluation findEvaluation(String elementId) {
		for (EvaluationConfiguration evaluationConfig : evaluationConfiguration) {
			if (evaluationConfig.getEvaluation().getId().equals(elementId))
				return evaluationConfig.getEvaluation();
		}
		return null;
	}

	@Override
	public TrackingManager getTrackingManager() {
		return trackingManager;
	}

	@Override
	public String getEvaluationProjectName() {
		return evaluationProjectName;
	}

	@Override
	public void setEvaluationProjectName(String evaluationProjectName) {
		this.evaluationProjectName = evaluationProjectName;
	}
}