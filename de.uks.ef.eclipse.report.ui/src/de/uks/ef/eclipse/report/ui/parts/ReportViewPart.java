package de.uks.ef.eclipse.report.ui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.swt.widgets.Composite;

import de.uks.ef.eclipse.report.configuration.ReportService;

@SuppressWarnings("restriction")
public class ReportViewPart {
	private Composite composite;

	@Inject
	private Logger LOGGER;

	@Inject
	private ReportService reportService;

	private ReportOverviewPart reportOverviewPart;

	@PostConstruct
	public void createComposite(Composite parent) {
		this.composite = parent;
		reportOverviewPart = new ReportOverviewPart();
		reportOverviewPart.createComposite(this.composite, reportService, LOGGER);
		reportService.parseAllReports();
	}

	public Composite getComposite() {
		return composite;
	}
}
