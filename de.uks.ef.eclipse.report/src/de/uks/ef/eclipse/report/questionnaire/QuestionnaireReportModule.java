package de.uks.ef.eclipse.report.questionnaire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationSubStep;
import de.uks.ef.core.model.QuestionnaireEntry;
import de.uks.ef.core.model.Report;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.core.model.EclipseReport;
import de.uks.ef.eclipse.report.configuration.ReportService;
import de.uks.ef.eclipse.report.model.EclipseReportModul;
import de.uks.ef.eclipse.tracking.model.AnswerChangedEvent;

@Creatable
public class QuestionnaireReportModule extends EclipseReportModul
{
   @Inject
   private ReportService reportService;

   @Override
   public Collection<Report> getReports(Evaluation evaluation)
   {
      Collection<Report> reports = new ArrayList<Report>();
      for (EvaluationStep evaluationStep : evaluation.getEvaluationStep().values())
      {
         if (reportService.isInFilter(evaluationStep.getId()))
         {
            for (EvaluationSubStep subStep : evaluationStep.getEvaluationSubStep().values())
            {
               for (QuestionnaireEntry questionnaireEntry : subStep.getQuestionnaireEntries())
               {
                  reports.add(new ChoiceQuestionAnswersPieChartReport(evaluationStep, questionnaireEntry, this));
               }
            }
         }
      }
      return reports;
   }

   @Override
   public Collection<TrackingEvent> getFilteredEvents(EclipseReport eclipseReport)
   {
      Collection<TrackingEvent> filteredEvents = new ArrayList<TrackingEvent>();
      for (TrackingEvent trackingEvent : eclipseReport.getReportEvents())
      {
         if (trackingEvent instanceof AnswerChangedEvent && reportService.isFiltered(trackingEvent))
         {
            filteredEvents.add(trackingEvent);
         }
      }
      return filteredEvents;
   }
}