package de.uks.ef.eclipse.core.configuration;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Creatable;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationConfiguration;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationStepConfiguration;
import de.uks.ef.core.model.EvaluationStepExecutable;
import de.uks.ef.core.model.EvaluationSubStep;
import de.uks.ef.core.model.ProjectImport;
import de.uks.ef.core.model.QuestionnaireEntry;
import de.uks.ef.eclipse.core.model.ChoiceQuestionnaireEntry;
import de.uks.ef.eclipse.core.model.EclipseEvaluation;
import de.uks.ef.eclipse.core.model.EclipseEvaluationConfiguration;
import de.uks.ef.eclipse.core.model.EclipseEvaluationManager;
import de.uks.ef.eclipse.core.model.EclipseEvaluationStep;
import de.uks.ef.eclipse.core.model.EclipseEvaluationStepConfiguration;
import de.uks.ef.eclipse.core.model.EclipseEvaluationStepImportProject;
import de.uks.ef.eclipse.core.model.EclipseEvaluationStepValidationJUnit;
import de.uks.ef.eclipse.core.model.EclipseEvaluationSubStep;
import de.uks.ef.eclipse.core.model.EclipseQuestionnaireEntry;

@Creatable
@Singleton
public class EvaluationLoader
{
   @Inject
   private EclipseEvaluationManager evaluationManager;

   private static final String EXTENSION_POINT_EVALUATIONMANAGER = "de.uks.ef.EvaluationManager"; //$NON-NLS-1$

   private static final String CONFIGURATION_EMAIL_ATTRIBUTE = "email"; //$NON-NLS-1$
   private static final String CONFIGURATION_URL_ATTRIBUTE = "url"; //$NON-NLS-1$
   private static final String CONFIGURATION_OPTIONALIDENTITY_ATTRIBUTE = "optionalIdentity"; //$NON-NLS-1$

   private static final String EVALUATION_NAME_ATTRIBUTE = "name"; //$NON-NLS-1$
   private static final String EVALUATION_DESCRIPTION_ATTRIBUTE = "description"; //$NON-NLS-1$
   private static final String EVALUATION_SHORTNAME_ATTRIBUTE = "shortName"; //$NON-NLS-1$

   private static final String EVALUATIONSTEP_NAME_ATTRIBUTE = "name"; //$NON-NLS-1$
   private static final String EVALUATIONSTEP_DESCRIPTION_ATTRIBUTE = "description"; //$NON-NLS-1$

   private static final String EVALUATIONSTEP_IMPORT_PROJECT_NAME_ATTRIBUTE = "name"; //$NON-NLS-1$
   private static final String EVALUATIONSTEP_IMPORT_PROJECT_PROJECT_ATTRIBUTE = "project"; //$NON-NLS-1$

   private static final String EVALUATIONSTEP_VALIDATION_JUNIT_NAME_ATTRIBUTE = "name"; //$NON-NLS-1$
   private static final String EVALUATIONSTEP_VALIDATION_JUNIT_PROJECT_ATTRIBUTE = "project"; //$NON-NLS-1$

   private static final String EVALUATIONSUBSTEP_NAME_ATTRIBUTE = "name"; //$NON-NLS-1$
   private static final String EVALUATIONSUBSTEP_DESCRIPTION_ATTRIBUTE = "description"; //$NON-NLS-1$

   public Set<EvaluationConfiguration> initialiseEvaluation(final EvaluationService evaluationService)
   {
      final IExtensionRegistry registry = Platform.getExtensionRegistry();
      if (registry == null)
      {
         return Collections.emptySet();
      }

      final IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_EVALUATIONMANAGER);

      for (final IExtension extension : point.getExtensions())
      {
         /*
          * Project Name for EvaluatinStepValidation and EvaluationStepImport
          * (important usage)
          */
         for (final IConfigurationElement configurationElement : extension.getConfigurationElements())
         {
            /* Configuration attributes */
            final String configurationEmail = configurationElement.getAttribute(CONFIGURATION_EMAIL_ATTRIBUTE);
            final String configurationURL = configurationElement.getAttribute(CONFIGURATION_URL_ATTRIBUTE);
            final boolean configurationOptionalIdentity = Boolean
                  .parseBoolean(configurationElement.getAttribute(CONFIGURATION_OPTIONALIDENTITY_ATTRIBUTE));

            /* Creates automatically EvaluationConfiguration */
            final EclipseEvaluationConfiguration configuration = new EclipseEvaluationConfiguration();
            configuration.setEmail(configurationEmail);
            configuration.setUrl(configurationURL);
            configuration.setOptionalIdentity(configurationOptionalIdentity);
            configuration.setEvaluationManager(evaluationManager);
            evaluationManager.addEvaluationConfiguration(configuration);

            for (final IConfigurationElement evaluationElement : configurationElement.getChildren())
            {
               /* Evaluation attributes */
               final String evaluationName = evaluationElement.getAttribute(EVALUATION_NAME_ATTRIBUTE);
               final String evaluationDescription = evaluationElement
                     .getAttribute(EVALUATION_DESCRIPTION_ATTRIBUTE);

               /* Creates Evaluation (for specified Extensions) */
               final Evaluation evaluation = new EclipseEvaluation();
               configuration.setEvaluation(evaluation);
               evaluation.setId(evaluationElement.getContributor().getName() + "-"
                     + evaluationElement.getAttribute(EVALUATION_SHORTNAME_ATTRIBUTE));
               evaluation.setName(evaluationName);
               evaluation.setDescription(evaluationDescription);
               evaluation.setEvaluationConfiguration(configuration);
               configuration.setEvaluation(evaluation);

               for (final IConfigurationElement evaluationStepElement : evaluationElement.getChildren())
               {
                  /* EvaluationStep attributes */
                  final String evaluationStepName = evaluationStepElement
                        .getAttribute(EVALUATIONSTEP_NAME_ATTRIBUTE);
                  final String evaluationStepDescription = evaluationStepElement
                        .getAttribute(EVALUATIONSTEP_DESCRIPTION_ATTRIBUTE);

                  /* Creates EvaluationStep (for specified Extensions) */
                  final EvaluationStep evaluationStep = new EclipseEvaluationStep();
                  evaluationStep.setId(evaluation.getId() + "-" + evaluation.getEvaluationStep().size());
                  evaluationStep.setOptional(Boolean.parseBoolean(evaluationStepElement.getAttribute("optional")));

                  /* Creates automatically EvaluationStepConfiguration */
                  final EvaluationStepConfiguration evaluationStepConfiguration = new EclipseEvaluationStepConfiguration();
                  evaluationStepConfiguration.setEvaluationStep(evaluationStep);

                  evaluationStep.setName(evaluationStepName);
                  evaluationStep.setDescription(evaluationStepDescription);
                  evaluationStep.setEvaluationStepConfiguration(evaluationStepConfiguration);
                  evaluationStep.setEvaluation(evaluation);

                  evaluation.addEvaluationStep(evaluationStep);

                  for (final IConfigurationElement evaluationSubStepElement : evaluationStepElement
                        .getChildren())
                  {
                     if (evaluationSubStepElement.getName().equals("EvaluationStepConfiguration"))
                     {
                        for (final IConfigurationElement evaluationStepItem : evaluationSubStepElement
                              .getChildren())
                        {
                           if (evaluationStepItem.getName().equals("EvaluationStepImports"))
                           {
                              for (final IConfigurationElement evaluationStepImport : evaluationStepItem
                                    .getChildren())
                              {
                                 if (evaluationStepImport.getName().equals("EvaluationStepImportProject"))
                                 {
                                    /*
                                     * EvaluationStepImportProject
                                     * attributes
                                     */
                                    /*
                                     * Creates
                                     * EvaluationStepImportProject
                                     * (imported Eclipse-Project)
                                     * (for specified Extensions)
                                     */
                                    final ProjectImport evaluationStepImportProject = new EclipseEvaluationStepImportProject();
                                    evaluationStepImportProject.setName(evaluationStepImport
                                          .getAttribute(EVALUATIONSTEP_IMPORT_PROJECT_NAME_ATTRIBUTE));
                                    evaluationStepImportProject.setProject(evaluationStepImport
                                          .getAttribute(EVALUATIONSTEP_IMPORT_PROJECT_PROJECT_ATTRIBUTE));
                                    evaluationStepImportProject
                                          .setEvaluationStepConfiguration(evaluationStepConfiguration);

                                    evaluationStepConfiguration
                                          .addEvaluationStepImportProject(evaluationStepImportProject);
                                 }
                              }
                           }
                           else if (evaluationStepItem.getName().equals("EvaluationStepValidations"))
                           {
                              for (final IConfigurationElement evaluationStepValidation : evaluationStepItem
                                    .getChildren())
                              {
                                 if (evaluationStepValidation.getName()
                                       .equals("EvaluationStepValidationJUnit"))
                                 {
                                    /*
                                     * EvaluationStepValidationJUnit
                                     * attributes
                                     */
                                    final String evaluationStepValidationProjectname = evaluationStepValidation
                                          .getAttribute(EVALUATIONSTEP_VALIDATION_JUNIT_NAME_ATTRIBUTE);

                                    /*
                                     * Creates
                                     * EvaluationStepValidationJUnit
                                     * (imported Eclipse-Project)
                                     * (for specified Extensions)
                                     */
                                    final EclipseEvaluationStepValidationJUnit junitValidation = new EclipseEvaluationStepValidationJUnit();
                                    junitValidation.setName(evaluationStepValidationProjectname);
                                    junitValidation.setName(evaluationStepValidation.getAttribute(
                                          EVALUATIONSTEP_VALIDATION_JUNIT_PROJECT_ATTRIBUTE));
                                    junitValidation
                                          .setEvaluationStepConfiguration(evaluationStepConfiguration);
                                    evaluationStepConfiguration
                                          .addEvaluationStepValidation(junitValidation);
                                 }
                              }
                           }
                           else if (evaluationStepItem.getName().equals("EvaluationStepPreparation"))
                           {
                              try
                              {
                                 EvaluationStepExecutable prepration = (EvaluationStepExecutable)evaluationStepItem
                                       .createExecutableExtension("class");
                                 evaluationStepConfiguration.addEvaluationStepPreparation(prepration);
                              }
                              catch (CoreException e)
                              {
                                 e.printStackTrace();
                              }
                           }
                           else if (evaluationStepItem.getName().equals("EvaluationStepCleanup"))
                           {
                              try
                              {
                                 EvaluationStepExecutable prepration = (EvaluationStepExecutable)evaluationStepItem
                                       .createExecutableExtension("class");
                                 evaluationStepConfiguration.addEvaluationStepCleanup(prepration);
                              }
                              catch (CoreException e)
                              {
                                 e.printStackTrace();
                              }
                           }
                        }
                     }
                     else
                     {
                        /* EvaluationSubStep attributes */
                        final String evaluationSubStepName = evaluationSubStepElement
                              .getAttribute(EVALUATIONSUBSTEP_NAME_ATTRIBUTE);
                        final String evaluationSubStepDescription = evaluationSubStepElement
                              .getAttribute(EVALUATIONSUBSTEP_DESCRIPTION_ATTRIBUTE);

                        /*
                         * Creates EvaluationSubStep (for specified
                         * Extensions)
                         */
                        final EvaluationSubStep evaluationSubStep = new EclipseEvaluationSubStep();
                        evaluationSubStep.setName(evaluationSubStepName);
                        evaluationSubStep.setId("" + evaluationStep.getEvaluationSubStep().size());
                        evaluationSubStep.setDescription(evaluationSubStepDescription);
                        evaluationSubStep.setEvaluationStep(evaluationStep);

                        for (final IConfigurationElement questionnaireEntryItem : evaluationSubStepElement
                              .getChildren())
                        {
                           QuestionnaireEntry questionnaireEntry = null;
                           String name = questionnaireEntryItem.getName();
                           if (name.equals("QuestionnaireEntry"))
                           {

                              String type = questionnaireEntryItem.getAttribute("type");
                              questionnaireEntry = new EclipseQuestionnaireEntry();
                              questionnaireEntry.setType(type);
                           }
                           else if (name.equals("ChoiceQuestionnaireEntry"))
                           {
                              questionnaireEntry = new ChoiceQuestionnaireEntry();
                              ((ChoiceQuestionnaireEntry)questionnaireEntry)
                                    .setChoices(questionnaireEntryItem.getAttribute("choices").split(","));
                              questionnaireEntry.setType("Choice");
                           }
                           if (questionnaireEntry != null)
                           {
                              questionnaireEntry.setId("" + evaluationSubStep.getQuestionnaireEntries().size());
                              questionnaireEntry.setQuestionId(
                                    evaluation.getId() + ":" + evaluationSubStep.getId() + ":"
                                          + questionnaireEntry.getId());
                              questionnaireEntry.setQuestion(questionnaireEntryItem.getAttribute("question"));

                              evaluationSubStep.addQuestionnaireEntry(questionnaireEntry);
                           }
                        }

                        evaluationStep.addEvaluationSubStep(evaluationSubStep);
                     }
                  }
               }
            }
         }
      }
      return evaluationManager.getEvaluationConfiguration();
   }
}