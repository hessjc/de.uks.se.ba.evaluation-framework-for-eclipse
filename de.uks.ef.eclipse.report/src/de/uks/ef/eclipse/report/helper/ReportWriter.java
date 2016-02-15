package de.uks.ef.eclipse.report.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationSubStep;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.core.utils.Constants;
import de.uks.ef.core.utils.FileHelper;
import de.uks.ef.core.utils.PreferenceHelper;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.tracking.configuration.TrackingService;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class ReportWriter
{
   private static final String FILE_USER_ID = ";uID.";
   private static final String FILE_EVALUATION_ID = "eID.";

   @Inject
   private Logger LOGGER;

   @Inject
   private EvaluationService evaluationService;

   @Inject
   private TrackingService trackingService;

   @Inject
   private EclipseReportHelper reportHelper;

   @Inject
   private PreferenceHelper preferenceHelper;

   @PostConstruct
   public void init()
   {
      LOGGER.info("ReportWriter injected.");
   }

   public String writeReport()
   {
      BufferedWriter writer = null;
      try
      {
         new File(FileHelper.REPORT_DIRECTORY).mkdirs();
         writer = createFile();
         reportHelper.setCurrentSelectedReport(
               FILE_EVALUATION_ID + evaluationService.getCurrentRunningEvaluation().getId() + FILE_USER_ID
                     + preferenceHelper.userID() + ".txt");
         writeHeader(writer);
         writeBody(writer);
      }
      finally
      {
         try
         {
            writer.close();
            LOGGER.info("Report file written.");
            return reportHelper.getCurrentSelectedReport();
         }
         catch (Exception e)
         {
            LOGGER.error("Report file not written.");
         }
      }
      return "";
   }

   private BufferedWriter createFile()
   {
      try
      {
         return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FileHelper.REPORT_DIRECTORY
               + FILE_EVALUATION_ID + evaluationService.getCurrentRunningEvaluation().getId() + FILE_USER_ID
               + preferenceHelper.userID() + ".txt"), "utf-8"));
      }
      catch (UnsupportedEncodingException e)
      {
         e.printStackTrace();
      }
      catch (FileNotFoundException e)
      {
         e.printStackTrace();
      }
      return null;
   }

   private void writeHeader(BufferedWriter writer)
   {
      try
      {
         Evaluation evaluation = evaluationService.getCurrentRunningEvaluation();
         writer.write(Constants.HEADER_BEGINNING_SEPERATOR);
         writer.newLine();
         if (!evaluation.getEvaluationConfiguration().isOptionalIdentity())
         {
            writer.write(Constants.USER_ID + ":" + preferenceHelper.userID());
            writer.newLine();
         }
         writer.write(Constants.EVALUATION_ID + ":" + evaluation.getId() + ";" + evaluation.getName() + ";");
         for (Entry<String, EvaluationStep> evaluationStep : evaluationService.getCurrentRunningEvaluation()
               .getEvaluationStep().entrySet())
         {
            writer.write(evaluationStep.getValue().getId() + ",");
         }
         writer.newLine();
         for (Entry<String, EvaluationStep> evaluationStep : evaluationService.getCurrentRunningEvaluation()
               .getEvaluationStep().entrySet())
         {
            writer.write(Constants.EVALUATIONSTEP_ID + ":" + evaluationStep.getValue().getId() + ";"
                  + evaluationStep.getValue().getName() + ";");
            for (Entry<String, EvaluationSubStep> step : evaluationStep.getValue().getEvaluationSubStep()
                  .entrySet())
            {
               writer.write(step.getValue().getId() + ",");
            }
            writer.write(";");
            writer.newLine();
            for (Entry<String, EvaluationSubStep> step : evaluationStep.getValue().getEvaluationSubStep()
                  .entrySet())
            {
               writer.write(Constants.STEP_ID + ":" + step.getValue().getId() + ";" + step.getValue().getName());
               writer.newLine();
            }
         }
         writer.write(Constants.HEADER_ENDING_SEPERATOR);
         writer.newLine();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   private void writeBody(BufferedWriter writer)
   {
      try
      {
         List<TrackingEvent> list = trackingService.getTrackingManager().getTrackingEvent()
               .get(evaluationService.getCurrentRunningEvaluation().getId());
         writer.write(Constants.BODY_BEGINNING_SEPERATOR);
         writer.newLine();
         if (list != null)
         {
            for (TrackingEvent event : list)
            {
               writer.write(event.getEncoding());
               writer.newLine();
            }
         }
         writer.write(Constants.BODY_ENDING_SEPERATOR);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}