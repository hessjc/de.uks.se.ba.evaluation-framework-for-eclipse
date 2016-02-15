package de.uks.ef.eclipse.tracking.breakpointtrackingmodul.event;

import de.uks.ef.eclipse.tracking.breakpointtrackingmodul.core.BreakpointTrackingEvent;

public class BreakpointRemovedEvent extends BreakpointTrackingEvent {
	public BreakpointRemovedEvent(final String timestamp, final String stepId, final String className,
			final String fileName, final String description) {
		super(timestamp, stepId, className, fileName, description);
	}

	@Override
	public String getEncoding() {
		return super.getEncoding() + ";BreakpointRemovedEvent;" + className + ";" + fileName + ";" + description;
	}

	public static BreakpointRemovedEvent parseEvent(final String eventString) {
		final String[] eventStrings = eventString.split(";");
		final String desc;
      if (eventStrings.length > 6)
      {
         desc = eventStrings[6];
      }
      else
      {
         desc = "";
      }
		return new BreakpointRemovedEvent(eventStrings[0], eventStrings[2], eventStrings[4], eventStrings[5], desc);
	}
}