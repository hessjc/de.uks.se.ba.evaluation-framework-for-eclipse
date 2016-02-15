package de.uks.ef.eclipse.core.listener.time.event;

import de.uks.ef.eclipse.core.utils.Constants;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingEvent;

public class StopStepEvent extends EclipseTrackingEvent {

	public static StopStepEvent parseEvent(final String eventString) {
		final String[] eventStrings = eventString.split(";");
		return new StopStepEvent(eventStrings[2], eventStrings[0]);
	}
	

	public StopStepEvent(String stepId, String stopTime) {
		super(stopTime, stepId);
	}

	@Override
	public String getEncoding() {
		return super.getEncoding() + ";StopStepEvent;";
	}

	@Override
	protected String getTrackingModulID() {
		return Constants.TIME_TRACKING;
	}

}
