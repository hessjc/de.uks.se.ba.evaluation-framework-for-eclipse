package de.uks.ef.eclipse.core.ui.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import de.uks.ef.core.model.EvaluationLogEvent;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationSubStep;
import de.uks.ef.core.model.ProjectImport;
import de.uks.ef.core.utils.DateHelper;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.model.EclipseEvaluationLog;
import de.uks.ef.eclipse.core.model.EclipseEvaluationLogEvent;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class EvaluationLogListener implements PropertyChangeListener
{
   @Inject
   private Logger LOGGER;

   @Inject
   private EclipseEvaluationLog evaluationLog;

   @Inject
   private EvaluationService evaluationService;

   @PostConstruct
   public void init()
   {
      LOGGER.debug("EvaluationLogListener injected.");
   }

   @Override
   public void propertyChange(final PropertyChangeEvent event)
   {
      switch (event.getPropertyName())
      {
         case "ProjectImported":
            projectImported(event);
            break;
         case "ProjectExists":
            projectExists(event);
            break;
         case "FileNotSupported":
            fileNotSupported(event);
            break;
         case "StepStarted":
            stepStarted(event);
            break;
         case "StepStopped":
            stepStopped(event);
            break;
         case "NoValidationAvailable":
            noValidationAvailable(event);
            break;
         case "ValidationStarted":
            validationStarted(event);
            break;
         case "ValidationSucceeded":
            validationSucceeded(event);
            break;
         case "ValidationFailured":
            validationFailured(event);
            break;
      }
   }

   private EvaluationLogEvent createEvaluationLogEvent(final String evaluation, final String description)
   {
      final EvaluationLogEvent evaluationLogEvent = new EclipseEvaluationLogEvent();
      evaluationLogEvent.setDescription(DateHelper.getDate() + ": " + description);
      evaluationLogEvent.setEvaluation(evaluation);
      return evaluationLogEvent;
   }

   private void projectImported(final PropertyChangeEvent event)
   {
      final EvaluationLogEvent evaluationLogEvent = createEvaluationLogEvent(((EvaluationStep)event.getOldValue())
            .getEvaluation().toString(), event.getNewValue() + " imported.");
      evaluationLog.addEvaluationLogEvent(evaluationLogEvent);
   }

   private void projectExists(final PropertyChangeEvent event)
   {
      final EvaluationLogEvent evaluationLogEvent = createEvaluationLogEvent(((EvaluationStep)event.getOldValue())
            .getEvaluation().toString(), event.getNewValue() + " is already existing.");
      evaluationLog.addEvaluationLogEvent(evaluationLogEvent);
   }

   private void fileNotSupported(final PropertyChangeEvent event)
   {
      final EvaluationLogEvent evaluationLogEvent = createEvaluationLogEvent(((EvaluationStep)event.getOldValue())
            .getEvaluation().toString(), event.getNewValue() + "\" is no supported File Extension.");
      evaluationLog.addEvaluationLogEvent(evaluationLogEvent);
   }

   private void stepStarted(final PropertyChangeEvent event)
   {
      final EvaluationStep evaluationStep = (EvaluationStep)event.getOldValue();
      final EvaluationLogEvent evaluationLogEvent = createEvaluationLogEvent(((EvaluationStep)event.getOldValue())
            .getEvaluation().toString(), "\"" + evaluationStep.getName() + "\" started.");
      evaluationLog.addEvaluationLogEvent(evaluationLogEvent);

      for (final Entry<String, ProjectImport> importedProjects : evaluationStep
            .getEvaluationStepConfiguration().getEvaluationStepImportProject().entrySet())
      {
         final ProjectImport importedProject = importedProjects.getValue();
         evaluationService.importProject(importedProject);
      }
   }

   private void stepStopped(final PropertyChangeEvent event)
   {
      final EvaluationStep evaluationStep = (EvaluationStep)event.getOldValue();
      final EvaluationLogEvent evaluationLogEvent = createEvaluationLogEvent(((EvaluationStep)event.getOldValue())
            .getEvaluation().toString(), "\"" + evaluationStep.getName() + "\" stopped.");
      evaluationLog.addEvaluationLogEvent(evaluationLogEvent);
   }

   private void noValidationAvailable(final PropertyChangeEvent event)
   {
      final EvaluationStep evaluationStep = (EvaluationStep)event.getOldValue();
      final EvaluationLogEvent evaluationLogEvent = createEvaluationLogEvent(((EvaluationStep)event.getOldValue())
            .getEvaluation().toString(), "No validation for \"" + evaluationStep.getName() + "\" available.");
      evaluationLog.addEvaluationLogEvent(evaluationLogEvent);
   }

   private void validationStarted(final PropertyChangeEvent event)
   {
      final EvaluationStep evaluationStep = (EvaluationStep)event.getOldValue();
      final EvaluationLogEvent evaluationLogEvent = createEvaluationLogEvent(((EvaluationStep)event.getOldValue())
            .getEvaluation().toString(), "Validation of \"" + evaluationStep.getName() + "\" started.");
      evaluationLog.addEvaluationLogEvent(evaluationLogEvent);
   }

   private void validationSucceeded(final PropertyChangeEvent event)
   {
      final EvaluationStep evaluationStep = (EvaluationStep)event.getOldValue();

      final EvaluationLogEvent evaluationLogEvent = createEvaluationLogEvent(((EvaluationStep)event.getOldValue())
            .getEvaluation().toString(), "Validation of \"" + evaluationStep.getName() + "\" succeeded.");
      evaluationLog.addEvaluationLogEvent(evaluationLogEvent);
   }

   private void validationFailured(final PropertyChangeEvent event)
   {
      final EvaluationStep evaluationStep = (EvaluationStep)event.getOldValue();
      final EvaluationLogEvent evaluationLogEvent = createEvaluationLogEvent(((EvaluationStep)event.getOldValue())
            .getEvaluation().toString(), "Validation of \"" + evaluationStep.getName() + "\" failured.");
      evaluationLog.addEvaluationLogEvent(evaluationLogEvent);
   }
}