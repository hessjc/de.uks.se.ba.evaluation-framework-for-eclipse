package de.uks.ef.eclipse.core.model;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationConfiguration;
import de.uks.ef.core.model.EvaluationManager;
import de.uks.ef.core.model.EvaluationSender;
import de.uks.ef.core.model.EvaluationStopper;

public class EclipseEvaluationConfiguration implements EvaluationConfiguration {
	private String email;

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	private String url;

	@Override
	public String getUrl() {
		return this.url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	private boolean optionalIdentity;

	@Override
	public boolean isOptionalIdentity() {
		return this.optionalIdentity;
	}

	@Override
	public void setOptionalIdentity(boolean optionalIdentity) {
		this.optionalIdentity = optionalIdentity;
	}

	private Evaluation evaluation;

	@Override
	public Evaluation getEvaluation() {
		return this.evaluation;
	}

	@Override
	public void setEvaluation(Evaluation evaluation) {
		this.evaluation = evaluation;
	}

	private EvaluationManager evaluationManager;

	@Override
	public EvaluationManager getEvaluationManager() {
		return this.evaluationManager;
	}

	@Override
	public void setEvaluationManager(EvaluationManager evaluationManager) {
		this.evaluationManager = evaluationManager;
	}

	private EvaluationSender evaluationSender = new EclipseEvaluationSender();

	@Override
	public EvaluationSender getEvaluationSender() {
		return this.evaluationSender;
	}

	@Override
	public void setEvaluationSender(EvaluationSender evaluationSender) {
		this.evaluationSender = evaluationSender;
	}

	private EvaluationStopper evaluationStopper;

	@Override
	public EvaluationStopper getEvaluationStopper() {
		return evaluationStopper;
	}

	@Override
	public void setEvaluationStopper(EvaluationStopper evaluationStopper) {
		this.evaluationStopper = evaluationStopper;
	}
}