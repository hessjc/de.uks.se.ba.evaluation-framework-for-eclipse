package de.uks.ef.eclipse.report.configuration;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.core.utils.DateHelper;
import de.uks.ef.core.utils.FileHelper;
import de.uks.ef.core.utils.PreferenceHelper;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.model.EclipseReport;
import de.uks.ef.eclipse.report.helper.EclipseReportHelper;
import de.uks.ef.eclipse.report.helper.ReportParser;
import de.uks.ef.eclipse.report.helper.ReportWriter;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class ReportService {
	@Inject
	private Logger LOGGER;

	@Inject
	private EclipseReportHelper reportHelper;

	@Inject
	private EvaluationService evaluationService;

	@Inject
	private ReportParser reportParser;

	@Inject
	private ReportWriter reportWriter;

	@Inject
	private PreferenceHelper preferenceHelper;

	private Map<String, EclipseReport> existingReports = new LinkedHashMap<String, EclipseReport>();

	@PostConstruct
	public void init() throws Exception {
		LOGGER.info("ReportService injected.");
	}

	public EclipseReportHelper getReportHelper() {
		return reportHelper;
	}

	public void openReport() throws IOException {
		Desktop.getDesktop()
				.open(new File(
						FileHelper.REPORT_DIRECTORY + "eID." + evaluationService.getCurrentRunningEvaluation().getId()
								+ ";uID." + preferenceHelper.userID() + ".txt"));
	}

	public void selectReport(String file, HashSet<String> selectedFilter) {
		reportHelper.setCurrentSelectedReport(file);
		reportHelper.setFilters(selectedFilter);
		existingReports.get(file);
	}

	public void writeReport() {
		reportWriter.writeReport();
	}

	public boolean isInFilter(String id) {
		return reportHelper.getFilters().contains(id) ? true : false;
	}

	public boolean isFiltered(TrackingEvent trackingEvent) {
		String dateString = trackingEvent.getEncoding().split(";")[0];
		Date date = DateHelper.formatStringToDate(dateString);
		HashSet<String> filters = reportHelper.getFilters();
		return filters.contains(trackingEvent.getStepId());
	}

	public Map<Evaluation, List<File>> listEvaluations() {

		Map<Evaluation, List<File>> evaluations = new LinkedHashMap<Evaluation, List<File>>();
		for (Entry<String, EclipseReport> entry : existingReports.entrySet()) {
			EclipseReport report = entry.getValue();
			Evaluation evaluation = report.getEvaluation();
			List<File> evs = evaluations.get(evaluation);
			if (evs == null) {
				evs = new ArrayList<File>();
				evaluations.put(evaluation, evs);
			}
			evs.add(new File(entry.getKey()));
		}
		return evaluations;
	}

	public List<String> listUser(String selectedEvaluation, File[] files) {
		List<String> users = new ArrayList<String>();
		for (File file : files) {
			String[] fileName = file.getName().split(";");
			if (fileName[0].equals(selectedEvaluation)) {
				users.add(fileName[1]);
			}
		}
		return users;
	}

	public void parseAllReports() {
		new Job("Parse all report") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				final File folder = new File(FileHelper.REPORT_DIRECTORY);

				File[] listFiles = folder.listFiles();
				if (listFiles != null) {

					for (File file : listFiles) {
						EclipseReport report = new EclipseReport();
						reportParser.parseReport(file.getAbsolutePath(), report);
						existingReports.put(file.getAbsolutePath(), report);
					}
				}
				return Status.OK_STATUS;
			}
		}.schedule(500);
	}

	public Collection<EclipseReport> getReports(final Evaluation evaluation) {
		Collection<EclipseReport> reports = new ArrayList<EclipseReport>();
		for (Entry<String, EclipseReport> entry : existingReports.entrySet()) {
			EclipseReport report = entry.getValue();
			Evaluation repEv = report.getEvaluation();
			if (repEv == evaluation) {
				reports.add(report);
			}
		}
		return reports;
	}
}