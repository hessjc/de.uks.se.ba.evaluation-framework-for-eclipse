package de.uks.ef.eclipse.core.ui.listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import de.uks.ef.core.utils.FileHelper;
import de.uks.ef.eclipse.core.configuration.EvaluationService;

public class SaveReportListener implements Listener {

	EvaluationService evaluationService;
	private String userId;

	public SaveReportListener(EvaluationService evaluationService, String userId) {
		this.evaluationService = evaluationService;
		this.userId = userId;
	}

	@Override
	public void handleEvent(Event event) {
		Display display = event.display;
		Shell shell = display.getActiveShell();

		DirectoryDialog directoryDialog = new DirectoryDialog(shell);
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		directoryDialog.setFilterPath(workspace.getRoot().getLocation().toFile().getAbsolutePath());
		directoryDialog.setMessage("Please select a directory and click OK");

		String dir = directoryDialog.open();
		if (dir != null) {
			String evaluationFileName = "eID." + evaluationService.getCurrentRunningEvaluation().getId() + ";uID."
					+ userId + ".txt";
			File reportFile = new File(FileHelper.REPORT_DIRECTORY + evaluationFileName);
			try {
				Files.copy(reportFile.toPath(), new File(dir, evaluationFileName).toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
