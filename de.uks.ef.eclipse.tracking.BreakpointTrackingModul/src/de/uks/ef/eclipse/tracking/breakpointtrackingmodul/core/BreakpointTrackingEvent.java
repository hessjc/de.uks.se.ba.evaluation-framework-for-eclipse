package de.uks.ef.eclipse.tracking.breakpointtrackingmodul.core;

import de.uks.ef.eclipse.tracking.model.EclipseTrackingEvent;

public abstract class BreakpointTrackingEvent extends EclipseTrackingEvent {
	protected final String description;
	protected final String className;
	protected final String fileName;

	public BreakpointTrackingEvent(final String timestamp, final String stepId, final String className,
			final String fileName, final String description) {
		super(timestamp, stepId);
		this.className = className;
		this.fileName = fileName;
		this.description = description;
	}

	protected String getClassName() {
		return className;
	}

	protected String getFileName() {
		return fileName;
	}

	public String getDescription() {
		return description;
	}

	@Override
	protected String getTrackingModulID() {
		return BreakpointTrackingModul.ID;
	}
}