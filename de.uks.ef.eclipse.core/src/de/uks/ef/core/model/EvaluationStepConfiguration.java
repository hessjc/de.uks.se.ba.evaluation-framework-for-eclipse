package de.uks.ef.core.model;

import java.util.Map;
import java.util.Set;

public interface EvaluationStepConfiguration {
	/**
	 * <pre>
	 *           0..1     0..1
	 * EvaluationStepConfiguration ------------------------- EvaluationStep
	 *           evaluationStepConfiguration        &lt;       evaluationStep
	 * </pre>
	 */
	public EvaluationStep getEvaluationStep();

	public void setEvaluationStep(EvaluationStep evaluationStep);

	/**
	 * <pre>
	 *           0..1     0..1* EvaluationStepConfiguration ------------------------- EvaluationStepImportProject
	 *           evaluationStepConfiguration        &lt;       evaluationStepImportProject
	 * </pre>
	 */
	public Map<String, ProjectImport> getEvaluationStepImportProject();

	public void addEvaluationStepImportProject(ProjectImport evaluationStepImportProject);

	/**
	 * <pre>
	 *           0..1     0..*
	 * EvaluationStepConfiguration ------------------------- EvaluationStepValidation
	 *           evaluationStepConfiguration        &lt;       evaluationStepValidation
	 * </pre>
	 */
	public Set<EvaluationStepValidation> getEvaluationStepValidation();

	public void addEvaluationStepValidation(EvaluationStepValidation evaluationStepValidation);

	public Set<EvaluationStepExecutable> getEvaluationStepPreparations();

	public void addEvaluationStepPreparation(final EvaluationStepExecutable evaluationStepPreparation);

	public Set<EvaluationStepExecutable> getEvaluationStepCleanups();

	public void addEvaluationStepCleanup(final EvaluationStepExecutable evaluationStepPreparation);
}