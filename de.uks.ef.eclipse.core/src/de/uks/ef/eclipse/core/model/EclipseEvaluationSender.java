package de.uks.ef.eclipse.core.model;

import de.uks.ef.core.model.EvaluationConfiguration;
import de.uks.ef.core.model.EvaluationSender;

public class EclipseEvaluationSender implements EvaluationSender {
	private EvaluationConfiguration evaluationConfiguration;

	@Override
	public EvaluationConfiguration getEvaluationConfiguration() {
		return this.evaluationConfiguration;
	}

	@Override
	public void setEvaluationConfiguration(EvaluationConfiguration evaluationConfiguration) {
		this.evaluationConfiguration = evaluationConfiguration;
	}

	@Override
	public void sendToUrl() {
		System.out.println("report sent to server");
	}
}