package de.uks.ef.eclipse.tracking.commandtrackingmodul.event;

import de.uks.ef.eclipse.tracking.commandtrackingmodul.core.CommandTrackingEvent;

public class PreExecuteEvent extends CommandTrackingEvent {
	public PreExecuteEvent(final String timestamp, final String stepId, final String commandId) {
		super(timestamp, stepId);
		this.commandId = commandId;
	}

	@Override
	public String getEncoding() {
		return super.getEncoding() + ";PreExecuteEvent;" + commandId;
	}

	public static PreExecuteEvent parseEvent(final String eventString) {
		final String[] eventStrings = eventString.split(";");
		return new PreExecuteEvent(eventStrings[0], eventStrings[2], eventStrings[4]);
	}
}