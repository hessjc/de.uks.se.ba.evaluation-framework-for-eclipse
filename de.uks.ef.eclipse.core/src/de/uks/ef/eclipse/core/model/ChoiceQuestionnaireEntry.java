package de.uks.ef.eclipse.core.model;

import java.util.ArrayList;
import java.util.Collection;

public class ChoiceQuestionnaireEntry extends EclipseQuestionnaireEntry {

	private Collection<String> choices;

	public Collection<String> getChoices() {
		return choices;
	}

	public void setChoices(String... configurationValues) {
		choices = new ArrayList<>();
		for (String configurationValue : configurationValues) {
			choices.add(configurationValue);
		}
	}
}
