package de.uks.ef.eclipse.report.time;

import javax.inject.Inject;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.core.listener.time.event.StartStepEvent;
import de.uks.ef.eclipse.core.listener.time.event.StopStepEvent;
import de.uks.ef.eclipse.core.utils.Constants;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingModul;

public class TimeTrackingModule extends EclipseTrackingModul {

	@Inject
	private TimeReportModule reportModule;

	@Override
	public void initReportModuls() {
		addReportModul(reportModule);
	}

	@Override
	public void initListener() {

	}

	@Override
	public String getId() {
		return Constants.TIME_TRACKING;
	}

	@Override
	public TrackingEvent parseEvent(String eventString) {
		String[] eventStrings = eventString.split(";");
		TrackingEvent event = null;
		switch (eventStrings[3]) {
		case "StartStepEvent":
			event = StartStepEvent.parseEvent(eventString);
			break;
		case "StopStepEvent":
			event = StopStepEvent.parseEvent(eventString);
			break;
		}
		return event;
	}
}
