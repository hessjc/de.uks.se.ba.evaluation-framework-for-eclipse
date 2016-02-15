package de.uks.ef.eclipse.core.ui.parts;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.ui.listener.EvaluationStartListener;
import de.uks.ef.eclipse.core.ui.utils.SWTHelper;

@SuppressWarnings("restriction")
public class EvaluationStartPart
{
   private static final String START_EVALUATION_BUTTON_TEXT = "Start";
   private static final String NO_EVALUATION_SELECTED = "No evaluation selected.";

   private StyledText evaluationDescriptionText;
   private StyledText evaluationStepDescriptionText;
   private Button startButton;
   private ListViewer evaluationStepViewer;

   public void createComposite(final Composite parent, final EvaluationService evaluationService, final Logger LOGGER)
   {
      parent.setLayout(new GridLayout(2, false));
      final Label evaluationText = new Label(parent, SWT.WRAP | SWT.READ_ONLY);

      try
      {
         evaluationText.setText(evaluationService.getCurrentRunningEvaluation().getName());
         evaluationText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
         final Font boldFont = new Font(evaluationText.getDisplay(), new FontData("Arial", 14, SWT.BOLD));
         evaluationText.setFont(boldFont);

         evaluationStepViewer = new ListViewer(parent);

         evaluationStepViewer.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element)
            {
               final EvaluationStep evaluationStep = (EvaluationStep)element;
               return evaluationStep.getName();
            }
         });

         evaluationStepViewer.setContentProvider(new IStructuredContentProvider() {

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
               return ((Evaluation)inputElement).getEvaluationStep().values().toArray();
            }
         });

         evaluationStepViewer.setInput(evaluationService.getCurrentRunningEvaluation());

         final GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 4);
         gd_table.widthHint = 250;
         gd_table.minimumWidth = 250;
         gd_table.heightHint = 100;
         evaluationStepViewer.getList().setLayoutData(gd_table);

         evaluationStepViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event)
            {
               final IStructuredSelection selection = (IStructuredSelection)event.getSelection();
               final EvaluationStep evaluationStep = (EvaluationStep)selection.getFirstElement();
               SWTHelper.styledDescriptionText(evaluationStepDescriptionText, evaluationStep.getDescription());
            }
         });

         final Label evaluationDescLabel = new Label(parent, SWT.NONE);
         evaluationDescLabel.setText("Description of this evaluation");
         evaluationDescLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         evaluationDescriptionText = new StyledText(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.READ_ONLY
               | SWT.V_SCROLL);
         evaluationDescriptionText.setEnabled(false);
         SWTHelper.styledText(evaluationDescriptionText, evaluationService.getCurrentRunningEvaluation()
               .getDescription());
         evaluationDescriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

         final Label evaluationStepDescLabel = new Label(parent, SWT.NONE);
         evaluationStepDescLabel.setText("Description of the selected evaluation step");
         evaluationStepDescLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
         evaluationStepDescriptionText = new StyledText(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.READ_ONLY
               | SWT.V_SCROLL);
         evaluationStepDescriptionText.setEnabled(false);
         evaluationStepDescriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

         startButton = new Button(parent, SWT.PUSH);
         final GridData gd_startButton = new GridData(SWT.RIGHT, SWT.FILL, false, false, 2, 1);
         startButton.setLayoutData(gd_startButton);
         startButton.setText(START_EVALUATION_BUTTON_TEXT);
         startButton.addListener(SWT.Selection, new EvaluationStartListener(evaluationService));
      }
      catch (final Exception e)
      {
         LOGGER.info(NO_EVALUATION_SELECTED);
         evaluationText.setText(NO_EVALUATION_SELECTED);
      }
      parent.layout();
   }

}