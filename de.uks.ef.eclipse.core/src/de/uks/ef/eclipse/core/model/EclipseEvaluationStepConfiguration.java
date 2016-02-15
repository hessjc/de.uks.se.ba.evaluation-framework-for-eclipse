package de.uks.ef.eclipse.core.model;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationStepConfiguration;
import de.uks.ef.core.model.EvaluationStepExecutable;
import de.uks.ef.core.model.EvaluationStepValidation;
import de.uks.ef.core.model.ProjectImport;

public class EclipseEvaluationStepConfiguration implements EvaluationStepConfiguration {
	private EvaluationStep evaluationStep;
	private Map<String, ProjectImport> evaluationStepImportProject;
	private Set<EvaluationStepValidation> evaluationStepValidation;
	private Set<EvaluationStepExecutable> evaluationStepPreparations;
	private Set<EvaluationStepExecutable> evaluationStepCleanups;

	@Override
	public EvaluationStep getEvaluationStep() {
		return evaluationStep;
	}

	@Override
	public void setEvaluationStep(final EvaluationStep evaluationStep) {
		this.evaluationStep = evaluationStep;
	}

	@Override
	public Map<String, ProjectImport> getEvaluationStepImportProject() {
		if (this.evaluationStepImportProject == null) {
			this.evaluationStepImportProject = new LinkedHashMap<String, ProjectImport>();
		}
		return this.evaluationStepImportProject;
	}

	@Override
	public void addEvaluationStepImportProject(final ProjectImport evaluationStepImportProject) {
		getEvaluationStepImportProject().put(evaluationStepImportProject.getName(), evaluationStepImportProject);
	}

	@Override
	public Set<EvaluationStepValidation> getEvaluationStepValidation() {
		if (this.evaluationStepValidation == null) {
			this.evaluationStepValidation = new LinkedHashSet<EvaluationStepValidation>();
		}
		return evaluationStepValidation;
	}

	@Override
	public void addEvaluationStepValidation(final EvaluationStepValidation evaluationStepValidation) {
		getEvaluationStepValidation().add(evaluationStepValidation);
	}

	public Set<EvaluationStepExecutable> getEvaluationStepPreparations() {
		if (this.evaluationStepPreparations == null) {
			this.evaluationStepPreparations = new LinkedHashSet<EvaluationStepExecutable>();
		}
		return evaluationStepPreparations;
	}

	@Override
	public void addEvaluationStepPreparation(final EvaluationStepExecutable evaluationStepPreparation) {
		getEvaluationStepPreparations().add(evaluationStepPreparation);
	}
	
	@Override
	public Set<EvaluationStepExecutable> getEvaluationStepCleanups() {
		if (this.evaluationStepCleanups == null) {
			this.evaluationStepCleanups = new LinkedHashSet<EvaluationStepExecutable>();
		}
		return evaluationStepCleanups;
	}
	
	@Override
	public void addEvaluationStepCleanup(EvaluationStepExecutable evaluationStepPreparation) {
		getEvaluationStepCleanups().add(evaluationStepPreparation);
	}
}