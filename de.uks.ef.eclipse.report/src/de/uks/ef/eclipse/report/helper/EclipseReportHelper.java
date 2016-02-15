package de.uks.ef.eclipse.report.helper;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.core.model.TrackingModul;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.model.EclipseEvaluation;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingModul;

@Creatable
@Singleton
public class EclipseReportHelper {

	@Inject
	private EvaluationService evaluationService;
	
	private String currentSelectedReport = "";

	private String userid = "";

	private HashSet<String> filters;

	public EclipseReportHelper() {
		filters = new HashSet<String>();
	}

	public void setCurrentSelectedReport(String currentSelectedReport) {
		this.currentSelectedReport = currentSelectedReport;
	}

	public String getCurrentSelectedReport() {
		return currentSelectedReport;
	}

	public HashSet<String> getFilters() {
		return filters;
	}

	public void setFilters(HashSet<String> filters) {
		this.filters = filters;
	}

	public void initFilters(final Evaluation evaluation ) {
		filters = new HashSet<String>();
		filters.add(evaluation.getId());
		for (EvaluationStep evaluationStep : evaluation.getEvaluationStep().values()) {
			filters.add(evaluationStep.getId());
		}
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
}
