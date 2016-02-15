package de.uks.ef.eclipse.core.ui.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import de.uks.ef.core.model.EvaluationSubStep;
import de.uks.ef.core.model.QuestionnaireEntry;
import de.uks.ef.eclipse.core.model.ChoiceQuestionnaireEntry;

public class QuestionnaireComposite extends Composite
{
   public static final int EVENT_SAVE = 255;

   private Composite questionnaireContent;

   private ScrolledComposite scrollable;

   public QuestionnaireComposite(final Composite parent)
   {
      super(parent, SWT.NONE);
      setLayout(new GridLayout(2, false));

      Label consoleLabel = new Label(this, SWT.WRAP);
      consoleLabel.setText("Questionnaire");
      consoleLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

      Button saveButton = new Button(this, SWT.NONE);
      saveButton.setText("Save");
      saveButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));

      saveButton.addSelectionListener(new SelectionListener() {

         @Override
         public void widgetSelected(SelectionEvent e)
         {
            for (final Control control : questionnaireContent.getChildren())
            {
               if (control instanceof QuestionnaireEntryComposite)
               {
                  final QuestionnaireEntry questionnaireEntry = ((QuestionnaireEntryComposite)control)
                        .getQuestionnaireEntry();
                  final String currentAnswer = ((QuestionnaireEntryComposite)control).getCurrentValue();
                  if (!currentAnswer.isEmpty())
                  {
                     questionnaireEntry.setCurrentAnswer(currentAnswer);
                  }
               }
            }

            QuestionnaireComposite.this.notifyListeners(EVENT_SAVE, new Event());
         }

         @Override
         public void widgetDefaultSelected(SelectionEvent e)
         {
         }
      });
      Composite temporary = new Composite(this, SWT.NONE);
      temporary.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
      temporary.setLayout(new FillLayout(SWT.VERTICAL));
      
      scrollable = new ScrolledComposite(temporary, SWT.H_SCROLL | SWT.V_SCROLL);
      scrollable.setExpandHorizontal(true);
      scrollable.setExpandVertical(true);
      questionnaireContent = new Composite(scrollable, SWT.NONE);
      questionnaireContent.setLayout(new GridLayout());
      scrollable.setContent(questionnaireContent);
   }

   public void update(final EvaluationSubStep subStep)
   {
      for (final Control control : questionnaireContent.getChildren())
      {
         control.dispose();
      }

      int count = 0;
      for (final QuestionnaireEntry questionnaireEntry : subStep.getQuestionnaireEntries())
      {
         switch (questionnaireEntry.getType())
         {
            case "String":
               new StringQuestionnaireEntryComposite(++count, questionnaireEntry);
               break;
            case "Integer":
               new IntegerQuestionnaireEntryComposite(++count, questionnaireEntry);
               break;
            case "StringArea":
               new StringAreaQuestionnaireEntryComposite(++count, questionnaireEntry);
               break;
            case "Choice":
               new ChoiceQuestionnaireEntryComposite(++count, questionnaireEntry);
               break;
            default:
               return;
         }
      }
      scrollable.setMinSize(questionnaireContent.computeSize(370, SWT.DEFAULT));
      questionnaireContent.layout();
      this.setRedraw(true);
   }

   private abstract class QuestionnaireEntryComposite extends Composite
   {
      protected final Label label;
      private final QuestionnaireEntry questionnaireEntry;

      public QuestionnaireEntryComposite(final int questionCount, final QuestionnaireEntry questionnaireEntry)
      {
         super(questionnaireContent, SWT.BORDER);
         this.questionnaireEntry = questionnaireEntry;
         setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));
         setLayout(new GridLayout(2, false));
         label = new Label(this, SWT.WRAP | SWT.LEFT);
         label.setText(questionCount + ".) " + questionnaireEntry.getQuestion());
      }

      protected abstract String getCurrentValue();

      public QuestionnaireEntry getQuestionnaireEntry()
      {
         return questionnaireEntry;
      }
   }

   private class ChoiceQuestionnaireEntryComposite extends QuestionnaireEntryComposite
   {

      private final Group choiceGroup;
      private String selectedChoice = null;

      public ChoiceQuestionnaireEntryComposite(final int questionCount,
            final QuestionnaireEntry questionnaireEntry)
      {
         super(questionCount, questionnaireEntry);

         choiceGroup = new Group(this, SWT.SHADOW_IN);
         choiceGroup.setLayout(new RowLayout(SWT.HORIZONTAL));
         label.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
         choiceGroup.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

         for (String choice : ((ChoiceQuestionnaireEntry)questionnaireEntry).getChoices())
         {
            newButton(choice, questionnaireEntry);
         }
      }

      private Button newButton(final String text, final QuestionnaireEntry questionnaireEntry)
      {
         Button button = new Button(choiceGroup, SWT.RADIO);
         button.setText(text);
         button.setSelection(Boolean.parseBoolean(questionnaireEntry.getCurrentAnswer()));
         button.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
               Button b = (Button)e.getSource();
               if (b.getSelection())
               {
                  selectedChoice = b.getText();
               }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {

            }
         });
         return button;
      }

      @Override
      protected String getCurrentValue()
      {
         if (selectedChoice == null)
         {
            return "";
         }
         return selectedChoice;
      }

   }

   private class IntegerQuestionnaireEntryComposite extends QuestionnaireEntryComposite
   {

      private final Spinner number;

      public IntegerQuestionnaireEntryComposite(final int questionCount, final QuestionnaireEntry questionnaireEntry)
      {
         super(questionCount, questionnaireEntry);

         number = new Spinner(this, SWT.BORDER);
         GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
         layoutData.heightHint = 30;
         label.setLayoutData(layoutData);
         number.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false, 1, 1));
         final String currentAnswer = questionnaireEntry.getCurrentAnswer();
         if (currentAnswer.isEmpty())
         {
            number.setSelection(0);
         }
         else
         {
            number.setSelection(Integer.parseInt(currentAnswer));
         }
      }

      @Override
      protected String getCurrentValue()
      {
         return "" + number.getSelection();
      }

   }

   private class StringQuestionnaireEntryComposite extends QuestionnaireEntryComposite
   {
      private final Text text;

      public StringQuestionnaireEntryComposite(final int questionCount, final QuestionnaireEntry questionnaireEntry)
      {
         super(questionCount, questionnaireEntry);

         text = new Text(questionnaireContent, SWT.SINGLE | SWT.BORDER);
         label.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
         text.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
         text.setText(questionnaireEntry.getCurrentAnswer());
      }

      @Override
      protected String getCurrentValue()
      {
         return text.getText();
      }

   }

   private class StringAreaQuestionnaireEntryComposite extends QuestionnaireEntryComposite
   {
      private final Text text;

      public StringAreaQuestionnaireEntryComposite(final int questionCount, final QuestionnaireEntry questionnaireEntry)
      {
         super(questionCount, questionnaireEntry);

         text = new Text(this, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
         label.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
         GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
         layoutData.heightHint = 150;
         text.setLayoutData(layoutData);
         text.setText(questionnaireEntry.getCurrentAnswer());
         setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
      }

      @Override
      protected String getCurrentValue()
      {
         return text.getText();
      }

   }
}
