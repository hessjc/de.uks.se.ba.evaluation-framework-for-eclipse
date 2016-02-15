package de.uks.ef.eclipse.core.ui.parts;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Table;

import de.uks.ef.core.model.EvaluationLog;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationSubStep;
import de.uks.ef.core.model.QuestionnaireEntry;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.configuration.StepService;
import de.uks.ef.eclipse.core.ui.listener.EvaluationContinueListener;
import de.uks.ef.eclipse.core.ui.listener.EvaluationPreviousListener;
import de.uks.ef.eclipse.core.ui.utils.SWTHelper;

public class EvaluationStepPart
{
   private static final String BACK_BUTTON_EVALUATION_BACK_TEXT = "Back";
   private static final String VALIDATION_BUTTON_EVALUATION_VALIDATE_TEXT = "Validate";
   private static final String CONTINUE_BUTTON_NEXT_EVALUATION_STEP_TEXT = "Next";
   private static final String CONTINUE_BUTTON_FINISH_EVALUATION_TEXT = "Finish";
   private static final String BUTTON_RESTART = "Restart";
   private static final String TABLEVIEWER_COL_SECOND_EVALUATION_SUBSTEP_TEXT = "Description of the selected substep";
   private static final String TABLEVIEWER_COL_FIRST_EVALUATION_SUBSTEP_TEXT = "Available substeps";
   private static final String EVALUATION_STEP_NAME = " - Step: ";
   private static final String EVALUATION_NAME = "Evaluation: ";

   private TableViewer stepTableViewer;

   public void createComposite(final Composite parent, final EvaluationService evaluationService,
         final StepService stepService, final EvaluationLog evaluationLog)
   {
      parent.setBackgroundMode(SWT.INHERIT_FORCE);
      parent.setLayout(new GridLayout(3, false));

      final Label evaluationText = new Label(parent, SWT.WRAP);

      StringBuilder header = new StringBuilder();
      header.append(EVALUATION_NAME);
      header.append(evaluationService.getCurrentRunningEvaluation().getName());
      header.append(EVALUATION_STEP_NAME);
      EvaluationStep currentRunningEvaluationStep = stepService.getCurrentRunningEvaluationStep();
      if (currentRunningEvaluationStep.isOptional())
      {
         header.append("Optional: ");
      }
      header.append(currentRunningEvaluationStep.getName());

      evaluationText.setText(header.toString());
      evaluationText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
      final Font boldFont = new Font(evaluationText.getDisplay(), new FontData("Arial", 14, SWT.BOLD));
      evaluationText.setFont(boldFont);

      final ProgressBar progressBar = new ProgressBar(parent, SWT.WRAP);
      progressBar.setSelection(stepService.getCurrentRunningEvaluationStepIndex() + 1);
      progressBar.setMaximum(evaluationService.getCurrentRunningEvaluation().getEvaluationStep().size());

      progressBar.addPaintListener(new PaintListener() {

         @Override
         public void paintControl(final PaintEvent e)
         {
            final Color myColor = new Color(Display.getDefault(), 0, 0, 0);
            e.gc.setForeground(myColor);
            e.gc.drawString(
                  stepService.getCurrentRunningEvaluationStepIndex() + 1 + "/"
                        + evaluationService.getCurrentRunningEvaluation().getEvaluationStep().size(),
                  progressBar.getSize().x / 2, 2, true);
         }
      });
      progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 3, 1));

      final StyledText evaluationStepDescriptionText = new StyledText(parent, SWT.WRAP);
      SWTHelper.styledDescriptionText(evaluationStepDescriptionText,
            currentRunningEvaluationStep.getDescription());
      evaluationStepDescriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

      final Group stepGroup = new Group(parent, SWT.NONE);
      GridLayout stepLayout = new GridLayout();
      stepLayout.marginTop = 10;
      stepLayout.marginLeft = 10;
      stepLayout.marginBottom = 10;
      stepLayout.marginRight = 10;
      stepGroup.setLayout(stepLayout);
      stepGroup.setText("Susteps");
      stepGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

      stepTableViewer = new TableViewer(stepGroup, SWT.SINGLE | SWT.BORDER | SWT.WRAP);

      final TableViewerColumn colFirstName = new TableViewerColumn(stepTableViewer, SWT.NONE);
      colFirstName.getColumn().setResizable(false);
      colFirstName.getColumn().setWidth(350);
      colFirstName.getColumn().setText(TABLEVIEWER_COL_FIRST_EVALUATION_SUBSTEP_TEXT);
      colFirstName.setLabelProvider(new ColumnLabelProvider() {
         @Override
         public String getText(final Object element)
         {
            final EvaluationSubStep step = (EvaluationSubStep)element;
            return step.getName();
         }
      });

      final TableViewerColumn colOpenQuestions = new TableViewerColumn(stepTableViewer, SWT.NONE);
      colOpenQuestions.getColumn().setResizable(false);
      colOpenQuestions.getColumn().setText("");
      colOpenQuestions.getColumn().setWidth(20);
      colOpenQuestions.setLabelProvider(new StyledCellLabelProvider() {

         @Override
         protected void paint(Event event, Object element)
         {
            if (element instanceof EvaluationSubStep)
            {
               EvaluationSubStep step = (EvaluationSubStep)element;
               int x = event.x;
               int y = event.y;

               int height = event.height - 1;
               GC gc = event.gc;

               Color oldBackground = gc.getBackground();

               gc.setBackground(getColor(step));

               gc.fillRectangle(x, y, 20, height);

               gc.setBackground(oldBackground);
            }
            else
            {
               super.paint(event, element);
            }
         }

         public Color getColor(EvaluationSubStep step)
         {
            if (step.getQuestionnaireEntries().isEmpty())
            {
               return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
            }

            boolean hasUnansweredQuestions = false;
            for (QuestionnaireEntry entry : step.getQuestionnaireEntries())
            {
               String answer = entry.getCurrentAnswer();
               if (answer == null || answer.isEmpty())
               {
                  hasUnansweredQuestions = true;
                  break;
               }
            }
            if (hasUnansweredQuestions)
            {
               return Display.getDefault().getSystemColor(SWT.COLOR_RED);
            }
            else
            {
               return Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
            }
         }
      });

      final Group group = new Group(parent, SWT.NONE);
      GridLayout layout = new GridLayout();
      layout.marginTop = 10;
      layout.marginLeft = 10;
      layout.marginBottom = 10;
      layout.marginRight = 10;
      group.setLayout(layout);
      group.setText(TABLEVIEWER_COL_SECOND_EVALUATION_SUBSTEP_TEXT);
      group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

      final StyledText descriptionText = new StyledText(group, SWT.WRAP | SWT.BORDER | SWT.READ_ONLY);
      descriptionText.setEnabled(false);
      descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

      final GridData gd_console = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 3);
      gd_console.widthHint = 400;
      final QuestionnaireComposite questionnaireComposite = new QuestionnaireComposite(parent);
      questionnaireComposite.setLayoutData(gd_console);
      questionnaireComposite.addListener(QuestionnaireComposite.EVENT_SAVE, new Listener() {
         @Override
         public void handleEvent(Event event)
         {
            stepTableViewer.refresh();
         }
      });

      Table table = stepTableViewer.getTable();
      table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

      stepTableViewer.setContentProvider(new IStructuredContentProvider() {

         @Override
         public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
         {
         }

         @Override
         public void dispose()
         {
         }

         @Override
         public Object[] getElements(Object inputElement)
         {
            if (inputElement instanceof EvaluationStep)
            {
               EvaluationStep evaluation = (EvaluationStep)inputElement;
               return evaluation.getEvaluationSubStep().values().toArray();
            }
            return new Object[0];
         }
      });

      stepTableViewer.setInput(currentRunningEvaluationStep);

      final Composite buttonComposite = new Composite(parent, SWT.NONE);
      buttonComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
      buttonComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 4, 1));

      final Button backButton = new Button(buttonComposite, SWT.NONE);
      backButton.setText(BACK_BUTTON_EVALUATION_BACK_TEXT);
      if (stepService.getCurrentRunningEvaluationStepIndex() == 0)
      {
         backButton.setEnabled(false);
      }
      backButton.addListener(SWT.Selection, new EvaluationPreviousListener(evaluationService));

      final Button validationButton = new Button(buttonComposite, SWT.NONE);
      validationButton.setText(VALIDATION_BUTTON_EVALUATION_VALIDATE_TEXT);
      validationButton.addListener(SWT.Selection, new Listener() {
         @Override
         public void handleEvent(final Event event)
         {
            evaluationService.validateStep();
         }
      });

      final Button continueButton = new Button(buttonComposite, SWT.NONE);
      if (stepService.getCurrentRunningEvaluationStepIndex() == evaluationService.getCurrentRunningEvaluation()
            .getEvaluationStep().size() - 1)
      {
         continueButton.setText(CONTINUE_BUTTON_FINISH_EVALUATION_TEXT);
      }
      else
      {
         continueButton.setText(CONTINUE_BUTTON_NEXT_EVALUATION_STEP_TEXT);
      }
      continueButton.addListener(SWT.Selection, new EvaluationContinueListener(stepService, evaluationService));

      final Button stopButton = new Button(buttonComposite, SWT.PUSH);
      stopButton.setText(BUTTON_RESTART);
      stopButton.addListener(SWT.Selection, new Listener() {
         @Override
         public void handleEvent(final Event event)
         {
            evaluationService.stopEvaluation();
         }
      });

      stepTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

         @Override
         public void selectionChanged(final SelectionChangedEvent changedEvent)
         {
            final ISelection selection = changedEvent.getSelection();
            if (!selection.isEmpty() && selection instanceof IStructuredSelection)
            {
               final Object step = ((IStructuredSelection)selection).getFirstElement();
               if (step instanceof EvaluationSubStep)
               {
                  EvaluationSubStep subStep = (EvaluationSubStep)step;
                  questionnaireComposite.update(subStep);

                  SWTHelper.styledDescriptionText(descriptionText, subStep.getDescription());
               }
            }
         }
      });

      parent.layout();
   }
}