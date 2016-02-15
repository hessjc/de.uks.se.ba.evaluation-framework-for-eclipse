package de.uks.ef.eclipse.core.ui.listener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationSubStep;
import de.uks.ef.core.model.QuestionnaireEntry;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.configuration.StepService;

public class EvaluationContinueListener implements Listener
{
   private static final String ERROR_ANSWER_ALL_QUESTIONS_TITLE = "Answer all questions";
   private static final String ERROR_ANSWER_ALL_QUESTIONS_TEXT = "Answer all questions to continue";

   private StepService stepService;

   private EvaluationService evaluationService;

   public EvaluationContinueListener(StepService stepService, EvaluationService evaluationService)
   {
      this.stepService = stepService;
      this.evaluationService = evaluationService;
   }

   @Override
   public void handleEvent(Event event)
   {
      EvaluationStep evaluationStep = stepService.getCurrentRunningEvaluationStep();
      if (!evaluationStep.isOptional())
      {
         boolean hasUnansweredQuestions = false;
         for (EvaluationSubStep evaluationSubStep : evaluationStep.getEvaluationSubStep().values())
         {
            for (QuestionnaireEntry entry : evaluationSubStep.getQuestionnaireEntries())
            {
               String answer = entry.getCurrentAnswer();
               if (answer == null || answer.isEmpty())
               {
                  hasUnansweredQuestions = true;
                  break;
               }
            }
         }
         if (hasUnansweredQuestions)
         {
            MessageDialog.openInformation(event.display.getActiveShell(), ERROR_ANSWER_ALL_QUESTIONS_TITLE,
                  ERROR_ANSWER_ALL_QUESTIONS_TEXT);
            return;
         }
      }
      evaluationService.nextEvaluationStep();
   }
}