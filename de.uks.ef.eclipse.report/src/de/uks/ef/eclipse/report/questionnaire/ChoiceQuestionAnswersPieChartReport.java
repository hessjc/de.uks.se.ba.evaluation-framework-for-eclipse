package de.uks.ef.eclipse.report.questionnaire;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationSubStep;
import de.uks.ef.core.model.QuestionnaireEntry;
import de.uks.ef.core.model.ReportModul;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.report.core.EclipseMultiBarChartReport;
import de.uks.ef.eclipse.tracking.model.AnswerChangedEvent;

public class ChoiceQuestionAnswersPieChartReport extends EclipseMultiBarChartReport
{
   private QuestionnaireEntry questionnaireEntry;
   private EvaluationStep step;

   public ChoiceQuestionAnswersPieChartReport(final EvaluationStep step, final QuestionnaireEntry questionnaireEntry,
         final ReportModul reportModul)
   {
      this.step = step;
      this.questionnaireEntry = questionnaireEntry;
      setReportModul(reportModul);
   }

   @Override
   public String getTitle()
   {
      return questionnaireEntry.getQuestion();
   }

   @Override
   public Map<String, Map<String, Integer>> getData(Collection<TrackingEvent> events)
   {
      Map<String, Map<String, Integer>> data = new LinkedHashMap<String, Map<String, Integer>>();
      for (TrackingEvent event : events)
      {
         if (event instanceof AnswerChangedEvent)
         {
            AnswerChangedEvent ace = (AnswerChangedEvent)event;
            EvaluationStep evaluationStep = ace.geEvaluationStep();

            String questionId = ace.getQuestionId();

            if (!questionnaireEntry.getQuestionId().equals(questionId) || !step.equals(ace.geEvaluationStep()))
            {
               continue;
            }

            String[] splittedQuestionId = questionId.split(":");

            EvaluationSubStep evaluationSubStep = evaluationStep.getEvaluationSubStep().get(splittedQuestionId[1]);
            QuestionnaireEntry questionnaireEntry = evaluationSubStep.getQuestionnaireEntries()
                  .get(Integer.parseInt(splittedQuestionId[2]));
            String uniqueQuestionName = questionnaireEntry.getQuestion() + " ("
                  + evaluationStep.getId().substring(evaluationStep.getId().lastIndexOf("-") + 1) + ":"
                  + splittedQuestionId[1] + ":"
                  + splittedQuestionId[2] + ")";
            Map<String, Integer> answerCountMap = new LinkedHashMap<String, Integer>();
            answerCountMap.put(ace.getAnswer(), 1);
            data.put(uniqueQuestionName, answerCountMap);
         }
      }
      return data;
   }
}