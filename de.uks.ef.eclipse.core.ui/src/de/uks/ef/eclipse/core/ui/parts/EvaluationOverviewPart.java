package de.uks.ef.eclipse.core.ui.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.uks.ef.core.utils.PreferenceHelper;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.ui.listener.ReportShowListener;
import de.uks.ef.eclipse.core.ui.listener.SendPerMailListener;
import de.uks.ef.eclipse.core.ui.listener.SaveReportListener;
import de.uks.ef.eclipse.report.configuration.ReportService;

public class EvaluationOverviewPart
{
   private static final String SAVE_REPORT_TEXT = "Save report file";
   private static final String SEND_PER_EMAIL_TEXT = "Send via email";
   private static final String OPEN_REPORT_FILE_TEXT = "Open report";

   private Label evaluationLabel;
   private Button saveReportFileButton;
   private Label evaluationStepDescriptionText;
   private Button sendPerMailButton;
   private Button showReportButton;

   public void createComposite(final Composite parent, final EvaluationService evaluationService,
         final ReportService reportService, final PreferenceHelper preferenceHelper)
   {
      parent.setLayout(new GridLayout(4, false));

      evaluationLabel = new Label(parent, SWT.NONE);
      evaluationLabel.setText(evaluationService.getCurrentRunningEvaluation().getName());
      evaluationLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

      showReportButton = new Button(parent, SWT.NONE);
      showReportButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
      showReportButton.setText(OPEN_REPORT_FILE_TEXT);

      showReportButton.addListener(SWT.Selection, new ReportShowListener(reportService));

      sendPerMailButton = new Button(parent, SWT.NONE);
      sendPerMailButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
      sendPerMailButton.setText(SEND_PER_EMAIL_TEXT);
      sendPerMailButton.addListener(SWT.Selection,
            new SendPerMailListener(evaluationService, preferenceHelper.userID()));

      saveReportFileButton = new Button(parent, SWT.PUSH);
      final GridData gd_saveReportFileButton = new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1);
      saveReportFileButton.setLayoutData(gd_saveReportFileButton);
      saveReportFileButton.setText(SAVE_REPORT_TEXT);
      saveReportFileButton.addListener(SWT.Selection,
            new SaveReportListener(evaluationService, preferenceHelper.userID()));

      evaluationStepDescriptionText = new Label(parent, SWT.BORDER);
      final GridData gd_evaluationStepDescriptionText = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
      gd_evaluationStepDescriptionText.widthHint = 350;
      gd_evaluationStepDescriptionText.heightHint = 61;
      evaluationStepDescriptionText.setLayoutData(gd_evaluationStepDescriptionText);
      parent.layout();
   }
}