package de.uks.ef.eclipse.report.helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationSubStep;
import de.uks.ef.core.utils.Constants;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.model.EclipseEvaluationStep;
import de.uks.ef.eclipse.core.model.EclipseEvaluationStepConfiguration;
import de.uks.ef.eclipse.core.model.EclipseEvaluationSubStep;
import de.uks.ef.eclipse.core.model.EclipseReport;
import de.uks.ef.eclipse.tracking.configuration.TrackingService;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingEvent;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingModul;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class ReportParser
{
   @Inject
   private Logger LOGGER;

   @Inject
   private TrackingService trackingService;

   @Inject
   private EvaluationService evaluationService;

   @PostConstruct
   public void init()
   {
      LOGGER.info("ReportParser injected.");
   }

   public void parseReport(String reportFile, EclipseReport report)
   {
      BufferedReader br = null;
      try
      {
         br = new BufferedReader(new FileReader(reportFile));
         StringBuilder header = new StringBuilder();
         StringBuilder body = new StringBuilder();
         boolean headerFlag = true;
         String line = br.readLine();

         Evaluation currentEvaluation = null;

         while (line != null)
         {
            if (headerFlag)
            {
               if (line.endsWith(Constants.HEADER_ENDING_SEPERATOR)
                     || line.endsWith(Constants.HEADER_BEGINNING_SEPERATOR))
               {
                  if (line.endsWith(Constants.HEADER_ENDING_SEPERATOR))
                  {
                     headerFlag = false;
                  }
               }
               else
               {
                  String[] splittedLine = line.split(";");
                  switch (splittedLine[0].split(":")[0])
                  {
                     case Constants.USER_ID:
                        report.setUserid(splittedLine[0].substring(4));
                        break;
                     case Constants.EVALUATION_ID:
                        currentEvaluation = parseEvaluation(splittedLine);
                        report.setEvaluation(currentEvaluation);
                        break;
                     case Constants.EVALUATIONSTEP_ID:
                        parseEvaluationStep(splittedLine, currentEvaluation);
                        break;
                     case Constants.STEP_ID:
                        parseStep(splittedLine, currentEvaluation);
                        break;
                     default:
                        LOGGER.error("No correct header information");
                        break;
                  }

                  header.append(line);
                  header.append(System.lineSeparator());
               }
            }
            else if (!line.equals(""))
            {
               if (line.endsWith(Constants.BODY_ENDING_SEPERATOR)
                     || line.endsWith(Constants.BODY_BEGINNING_SEPERATOR))
               {

               }
               else
               {
                  String[] result = line.split(";");

                  EclipseTrackingModul trackingModul = (EclipseTrackingModul)trackingService.getTrackingManager()
                        .getTrackingModul().get(result[1]);

                  if (trackingModul != null)
                  {
                     try
                     {
                        report.addTrackingModul(trackingModul);
                        EclipseTrackingEvent event = (EclipseTrackingEvent)trackingModul.parseEvent(line);
                        report.addTrackingEvent(event);
                     }
                     catch (Exception e)
                     {
                        LOGGER.error(e.getMessage());
                     }
                  }
                  else
                     LOGGER.info("TrackingModul: " + result[1] + " not found.");
                  body.append(line);
                  body.append(System.lineSeparator());
               }
            }
            line = br.readLine();
         }
         report.setBody(body.toString());
         report.setHeader(header.toString());
         // FIXME reportHelper.initFilters();
         report.initReportModuls();
      }

      catch (FileNotFoundException e)
      {
         e.printStackTrace();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      finally
      {
         try
         {
            br.close();
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
         LOGGER.info("Report file successfully parsed.");
      }
   }

   private Evaluation parseEvaluation(String[] splittedLine)
   {
      Evaluation evaluation = evaluationService
            .findEvaluation(splittedLine[0].substring(splittedLine[0].indexOf(":") + 1));
      if (evaluation.getName() == null)
      {
         evaluation.setName(splittedLine[1]);
      }
      if (evaluation.getEvaluationStep().isEmpty())
      {
         String[] evSteps = splittedLine[3].split(",");
         for (int i = 0; i < evSteps.length; i++)
         {
            EvaluationStep evaluationStep = new EclipseEvaluationStep(evSteps[i]);
            evaluationStep.setEvaluationStepConfiguration(new EclipseEvaluationStepConfiguration());
            evaluation.addEvaluationStep(evaluationStep);
         }
      }
      return evaluation;
   }

   private void parseEvaluationStep(String[] splittedLine, Evaluation currentEvaluation)
   {
      String stepId = splittedLine[0].substring(splittedLine[0].indexOf(":") + 1);
      EvaluationStep evaluationStep = currentEvaluation.getEvaluationStep().get(stepId);
      if (evaluationStep.getName() == null)
      {
         evaluationStep.setName(splittedLine[1]);
      }
      if (evaluationStep.getEvaluationSubStep().isEmpty())
      {
         String[] steps = splittedLine[2].split(",");
         for (int i = 0; i < steps.length; i++)

         {
            evaluationStep.addEvaluationSubStep(new EclipseEvaluationSubStep(steps[i]));
         }
      }
   }

   private void parseStep(String[] splittedLine, Evaluation currentEvaluation)
   {
      for (Entry<String, EvaluationStep> evaluationStep : currentEvaluation.getEvaluationStep().entrySet())
      {
         EvaluationSubStep step = evaluationStep.getValue().getEvaluationSubStep()
               .get(splittedLine[0].split(":")[1]);
         if (step != null)
         {
            step.setName(splittedLine[1]);
         }
      }
   }
}