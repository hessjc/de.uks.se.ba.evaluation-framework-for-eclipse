package de.uks.ef.eclipse.core.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.core.model.TrackingModul;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingModul;

public class EclipseReport {
	private String header;

	private String body;

	private Set<TrackingModul> trackingModuls;

	private LinkedHashSet<TrackingEvent> reportEvents;

	private String userid = "";

	private Evaluation evaluation;

	public EclipseReport() {
		trackingModuls = new HashSet<TrackingModul>();
		reportEvents = new LinkedHashSet<TrackingEvent>();
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Evaluation getEvaluation() {
		return evaluation;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Set<TrackingModul> getTrackingModuls() {
		if (trackingModuls == null) {
			trackingModuls = new HashSet<TrackingModul>();
		}
		return trackingModuls;
	}

	public void addTrackingModul(TrackingModul trackingModul) {
		getTrackingModuls().add(trackingModul);
	}

	public LinkedHashSet<TrackingEvent> getReportEvents() {
		if (reportEvents == null) {
			reportEvents = new LinkedHashSet<TrackingEvent>();
		}
		return reportEvents;
	}

	public void addTrackingEvent(TrackingEvent trackingEvent) {
		getReportEvents().add(trackingEvent);
	}

	public void initReportModuls() {
		for (TrackingModul trackingModul : getTrackingModuls()) {
			EclipseTrackingModul modul = (EclipseTrackingModul) trackingModul;
			modul.removeReportModuls();
			modul.initReportModuls();
		}
	}

	// TODO:
	public String transformHeader() {

		// writer.write("File created: " + DateHelper.getDate());
		// writer.newLine();
		// writer.write("evaluation: " +
		// evaluationService.getCurrentRunningEvaluation().getName());
		// writer.newLine();
		// writer.write("starttime: "
		// +
		// DateHelper.formatDate(evaluationService.getCurrentRunningEvaluation().getStartTime()));
		// writer.newLine();
		// writer.write("endtime: "
		// +
		// DateHelper.formatDate(evaluationService.getCurrentRunningEvaluation().getEndTime()));
		// writer.newLine();
		// writer.write("time overall: "
		// +
		// Float.valueOf(((evaluationService.getCurrentRunningEvaluation().getEndTime().getTime()
		// -
		// evaluationService.getCurrentRunningEvaluation().getStartTime().getTime()))
		// / 1000) + "[s]");
		// writer.newLine();
		// writer.write("evaluationsteps by time:");
		// writer.newLine();
		// for (EvaluationStep evaluationStep :
		// evaluationService.getCurrentRunningEvaluation().getEvaluationStep())
		// {
		// writer.write(" -> " + evaluationStep.getName() + ": "
		// + evaluationService.getEvaluationTime(evaluationStep) + "[s] in "
		// + evaluationStep.getEvaluationStepExecutionTime().size() + "
		// visits");
		// writer.newLine();
		//
		// for (Step step : evaluationStep.getStep())
		// {
		// writer.write(" ----> " + step.getName() + " done: " +
		// step.getDoneTime());
		// writer.newLine();
		// }
		// }

		return "";
	}

	public void setEvaluation(Evaluation currentEvaluation) {
		evaluation = currentEvaluation;
	}
}
