package de.uks.ef.eclipse.core.ui.listener;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.utils.FileHelper;
import de.uks.ef.eclipse.core.configuration.EvaluationService;

public class SendPerMailListener implements Listener {
	EvaluationService evaluationService;
	private String userId;

	public SendPerMailListener(EvaluationService evaluationService, String userId) {
		this.evaluationService = evaluationService;
		this.userId = userId;
	}

	@Override
	public void handleEvent(Event event) {
		Evaluation currentRunningEvaluation = evaluationService.getCurrentRunningEvaluation();
		String mailto = "mailto:" + enc(currentRunningEvaluation.getEvaluationConfiguration().getEmail()) + "?subject="
				+ enc(currentRunningEvaluation.getId());
		File reportFile = new File(FileHelper.REPORT_DIRECTORY + "eID."
				+ evaluationService.getCurrentRunningEvaluation().getId() + ";uID." + userId + ".txt");
		mailto += "&attachment=" + reportFile.getAbsolutePath();
		Program.launch(mailto);
	}

	private String enc(String p) {
		if (p == null)
			p = "";
		try {
			return URLEncoder.encode(p, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
	}
}
