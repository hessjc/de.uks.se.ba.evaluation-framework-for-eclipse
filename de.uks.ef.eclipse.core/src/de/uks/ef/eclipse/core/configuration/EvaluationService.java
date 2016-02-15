package de.uks.ef.eclipse.core.configuration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationConfiguration;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationStepExecutable;
import de.uks.ef.core.model.EvaluationStepValidation;
import de.uks.ef.core.model.ProjectImport;
import de.uks.ef.core.model.QuestionnaireEntry;
import de.uks.ef.core.utils.DateHelper;
import de.uks.ef.eclipse.core.listener.EvaluationStateListener;
import de.uks.ef.eclipse.core.listener.time.event.StartStepEvent;
import de.uks.ef.eclipse.core.listener.time.event.StopStepEvent;
import de.uks.ef.eclipse.core.model.EclipseEvaluation;
import de.uks.ef.eclipse.core.model.EclipseEvaluationManager;
import de.uks.ef.eclipse.core.model.EclipseEvaluationStepImportProject;
import de.uks.ef.eclipse.core.model.EclipseEvaluationStepValidationJUnit;
import de.uks.ef.eclipse.core.model.EclipseQuestionnaireEntry;
import de.uks.ef.eclipse.core.utils.EclipseProjectHelper;
import de.uks.ef.eclipse.tracking.configuration.TrackingService;
import de.uks.ef.eclipse.tracking.model.AnswerChangedEvent;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class EvaluationService implements PropertyChangeListener
{
   @Inject
   private Logger LOGGER;

   @Inject
   private EclipseEvaluationManager evaluationManager;

   @Inject
   private StepService stepService;

   @Inject
   private EvaluationLoader evaluationLoader;

   @Inject
   private TrackingService trackingService;

   private Evaluation currentRunningEvaluation;

   private final HashSet<EvaluationStateListener> listeners;

   private final PropertyChangeSupport changes;

   public EvaluationService()
   {
      listeners = new HashSet<EvaluationStateListener>();
      changes = new PropertyChangeSupport(this);
   }

   @PostConstruct
   private void init()
   {
      LOGGER.info("EvaluationService injected.");
      evaluationLoader.initialiseEvaluation(this);
   }

   public Evaluation getCurrentRunningEvaluation()
   {
      return currentRunningEvaluation;
   }

   public void setCurrentRunningEvaluation(final Evaluation evaluation)
   {
      evaluationManager.setEvaluationProjectName(evaluation.getName());
      if (this.currentRunningEvaluation != evaluation)
      {
         this.currentRunningEvaluation = evaluation;
         createEvaluationStartPart();
      }
   }

   public Evaluation findEvaluation(final String id)
   {
      for (final EvaluationConfiguration evaluationConfig : evaluationManager.getEvaluationConfiguration())
      {
         if (evaluationConfig.getEvaluation().getId().equals(id))
         {
            return evaluationConfig.getEvaluation();
         }
      }
      return new EclipseEvaluation(id);
   }

   public void showEvaluation()
   {
      resetEvaluationService();
      createEvaluationStartPart();
      LOGGER.info("Evaluation shown.");
   }

   public void startEvaluation()
   {
      resetEvaluationService();
      currentRunningEvaluation.start();
      startExecutionTime(getCurrentRunningEvaluation());
      
      EvaluationStep startEvaluationStep = stepService.getCurrentRunningEvaluationStep();
      for (EvaluationStepExecutable preparation : startEvaluationStep.getEvaluationStepConfiguration()
            .getEvaluationStepPreparations())
      {
         preparation.execute();
      }
      
      stepService.startExecutionTime(startEvaluationStep);

      trackingService.startTracking();

      for (final EvaluationStep evaluationStep : getCurrentRunningEvaluation().getEvaluationStep().values())
      {
         evaluationStep.addListener(this);
      }

      createEvaluationStepPart();
      LOGGER.info("Evaluation started.");
   }

   public void stopEvaluation()
   {
      currentRunningEvaluation.reset();
      stopExecutionTime(getCurrentRunningEvaluation());
      stepService.stopExecutionTime(stepService.getCurrentRunningEvaluationStep());

      trackingService.stopTracking();
      trackingService.getTrackingManager().getTrackingEvent().clear();

      showEvaluation();
      LOGGER.info("Evaluation stopped.");
   }

   public void finishEvaluation()
   {
      currentRunningEvaluation.setStarted(false);
      
      final ArrayList<EvaluationStep> linkedList = new ArrayList<EvaluationStep>(
            getCurrentRunningEvaluation().getEvaluationStep().values());

      EvaluationStep currentEvaluationStep = linkedList.get(stepService.getCurrentRunningEvaluationStepIndex());
      for (EvaluationStepExecutable cleanup : currentEvaluationStep.getEvaluationStepConfiguration()
            .getEvaluationStepCleanups())
      {
         cleanup.execute();
      }
      
      stopExecutionTime(getCurrentRunningEvaluation());
      stepService.stopExecutionTime(stepService.getCurrentRunningEvaluationStep());

      trackingService.stopTracking();

      changes.firePropertyChange("evaluationFinished", false, true);
      createEvaluationOverviewPart();
      LOGGER.info("Evaluation finished.");
   }

   private void resetEvaluationService()
   {
      stepService.setCurrentRunningEvaluationStepIndex(0);
      final LinkedList<EvaluationStep> list = new LinkedList<>(currentRunningEvaluation.getEvaluationStep().values());
      stepService.setCurrentRunningEvaluationStep(list.get(0));
      LOGGER.debug("Evaluation resetted.");
   }

   private void createEvaluationStartPart()
   {
      for (final EvaluationStateListener listener : listeners)
      {
         listener.evaluationShown();
      }
   }

   private void createEvaluationStepPart()
   {
      for (final EvaluationStateListener listener : listeners)
      {
         listener.evaluationStarted();
      }
   }

   private void createEvaluationOverviewPart()
   {
      for (final EvaluationStateListener listener : listeners)
      {
         listener.evaluationFinished();
      }
   }

   public void updateEvaluationStep()
   {
      for (final EvaluationStateListener listener : listeners)
      {
         listener.evaluationStepped();
      }
   }

   private boolean continueEvaluationStep()
   {
      if (stepService
            .getCurrentRunningEvaluationStepIndex() < getCurrentRunningEvaluation().getEvaluationStep().size()
                  - 1)
      {
         return true;
      }
      return false;
   }

   private boolean backwardsEvaluationStep()
   {
      if (stepService.getCurrentRunningEvaluationStepIndex() > 0)
      {
         return true;
      }
      return false;
   }

   public HashSet<EvaluationStateListener> getListeners()
   {
      return listeners;
   }

   public void registerEvaluationStateListener(final EvaluationStateListener evaluationStateListener)
   {
      listeners.add(evaluationStateListener);
   }

   public void unregisterEvaluationStateListener(final EvaluationStateListener evaluationStateListener)
   {
      listeners.remove(evaluationStateListener);
   }

   public void addPropertyChangeListener(final PropertyChangeListener l)
   {
      final List<PropertyChangeListener> list = Arrays.asList(changes.getPropertyChangeListeners());
      if (list.contains(l) == false)
      {
         changes.addPropertyChangeListener(l);
      }
   }

   public void removePropertyChangeListener(final PropertyChangeListener l)
   {
      final List<PropertyChangeListener> list = Arrays.asList(changes.getPropertyChangeListeners());
      if (list.contains(l) == true)
      {
         changes.removePropertyChangeListener(l);
      }
   }

   public void nextEvaluationStep()
   {
      if (continueEvaluationStep())
      {
         trackingService.addTrackingEvent(stepService.getCurrentRunningEvaluationStep().getEvaluation(),
               new StopStepEvent(stepService.getCurrentRunningEvaluationStep().getId(), DateHelper.getDate()));

         final ArrayList<EvaluationStep> linkedList = new ArrayList<EvaluationStep>(
               getCurrentRunningEvaluation().getEvaluationStep().values());

         EvaluationStep currentEvaluationStep = linkedList.get(stepService.getCurrentRunningEvaluationStepIndex());
         for (EvaluationStepExecutable cleanup : currentEvaluationStep.getEvaluationStepConfiguration()
               .getEvaluationStepCleanups())
         {
            cleanup.execute();
         }

         stepService.setCurrentRunningEvaluationStepIndex(stepService.getCurrentRunningEvaluationStepIndex() + 1);

         EvaluationStep nextEvaluationStep = linkedList.get(stepService.getCurrentRunningEvaluationStepIndex());
         stepService.setCurrentRunningEvaluationStep(nextEvaluationStep);

         for (EvaluationStepExecutable preparation : nextEvaluationStep.getEvaluationStepConfiguration()
               .getEvaluationStepPreparations())
         {
            preparation.execute();
         }

         stepService.startExecutionTime(stepService.getCurrentRunningEvaluationStep());

         updateEvaluationStep();
         LOGGER.debug("Next evaluation step.");
      }
      else
      {
         trackingService.addTrackingEvent(stepService.getCurrentRunningEvaluationStep().getEvaluation(),
               new StopStepEvent(stepService.getCurrentRunningEvaluationStep().getId(), DateHelper.getDate()));

         finishEvaluation();
      }
   }

   public void previousEvaluationStep()
   {
      if (backwardsEvaluationStep())
      {
         trackingService.addTrackingEvent(stepService.getCurrentRunningEvaluationStep().getEvaluation(),
               new StopStepEvent(stepService.getCurrentRunningEvaluationStep().getId(), DateHelper.getDate()));

         final ArrayList<EvaluationStep> linkedList = new ArrayList<EvaluationStep>(
               getCurrentRunningEvaluation().getEvaluationStep().values());

         EvaluationStep currentEvaluationStep = linkedList.get(stepService.getCurrentRunningEvaluationStepIndex());
         for (EvaluationStepExecutable cleanup : currentEvaluationStep.getEvaluationStepConfiguration()
               .getEvaluationStepCleanups())
         {
            cleanup.execute();
         }

         stepService.setCurrentRunningEvaluationStepIndex(stepService.getCurrentRunningEvaluationStepIndex() - 1);

         EvaluationStep nextEvaluationStep = linkedList.get(stepService.getCurrentRunningEvaluationStepIndex());
         stepService.setCurrentRunningEvaluationStep(nextEvaluationStep);

         for (EvaluationStepExecutable preparation : nextEvaluationStep.getEvaluationStepConfiguration()
               .getEvaluationStepPreparations())
         {
            preparation.execute();
         }

         stepService.startExecutionTime(stepService.getCurrentRunningEvaluationStep());

         updateEvaluationStep();
         LOGGER.debug("Previous evaluation step.");
      }
      else
      {
         stopEvaluation();
      }
   }

   private void startExecutionTime(final Evaluation evaluation)
   {
      trackingService.addTrackingEvent(evaluation, new StartStepEvent(evaluation.getId(), DateHelper.getDate()));
      LOGGER.debug(evaluation.getName() + " started.");
   }

   private void stopExecutionTime(final Evaluation evaluation)
   {
      trackingService.addTrackingEvent(evaluation, new StopStepEvent(evaluation.getId(), DateHelper.getDate()));
      LOGGER.debug(evaluation.getName() + " stopped.");
   }

   public boolean evaluationIsStopped()
   {
      final Evaluation currentRunningEvaluation = this.getCurrentRunningEvaluation();
      if (currentRunningEvaluation != null && currentRunningEvaluation.isStarted())
      {
         return false;
      }
      else
      {
         return true;
      }
   }

   public void importProject(final ProjectImport importedProject)
   {
      final String fileName = importedProject.getProject();
      boolean flag = false;

      final String[] splittedProjectName = fileName.split("\\.");

      switch (splittedProjectName[splittedProjectName.length - 1])
      {
         case "zip":
            flag = EclipseProjectHelper.importZipProject(fileName, getCurrentRunningEvaluation().getId(),
                  importedProject.getName());
            break;
         default:
            changes.firePropertyChange("FileNotSupported", stepService.getCurrentRunningEvaluationStep(), fileName);
            return;
      }

      if (flag)
      {
         changes.firePropertyChange("ProjectImported", stepService.getCurrentRunningEvaluationStep(), fileName);
      }
      else
      {
         changes.firePropertyChange("ProjectExists", stepService.getCurrentRunningEvaluationStep(),
               splittedProjectName[0]);
      }
   }

   @Inject
   IProgressMonitor monitor;

   public boolean validateStep()
   {
      final boolean flag = false;

      final Set<EvaluationStepValidation> validations = stepService.getCurrentRunningEvaluationStep()
            .getEvaluationStepConfiguration().getEvaluationStepValidation();

      if (validations == null)
      {
         changes.firePropertyChange("NoValidationAvailable", stepService.getCurrentRunningEvaluationStep(), true);
         return true;
      }

      stepService.initalisePlugInListener();

      for (final EvaluationStepValidation evaluationStepValidation : stepService.getCurrentRunningEvaluationStep()
            .getEvaluationStepConfiguration().getEvaluationStepValidation())
      {
         if (evaluationStepValidation instanceof EclipseEvaluationStepValidationJUnit)
         {
            final EclipseEvaluationStepValidationJUnit unitValidation = (EclipseEvaluationStepValidationJUnit)evaluationStepValidation;

            // Import TestProjects
            final EclipseEvaluationStepImportProject importProject = new EclipseEvaluationStepImportProject();
            importProject.setName(unitValidation.getName());
            importProject.setProject(unitValidation.getProject());

            importProject(importProject);

            final ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
            final ILaunchConfigurationType type = manager
                  .getLaunchConfigurationType("org.eclipse.jdt.junit.launchconfig");

            final String projectName = unitValidation.getName().substring(0, unitValidation.getName().length() - 4);

            try
            {
               final ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, projectName + "_Test");

               final IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

               final IResource[] resources = { project };
               workingCopy.setMappedResources(resources);

               workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                     "test." + projectName);
               workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, projectName);

               final IPath systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
               final IRuntimeClasspathEntry systemLibsEntry = JavaRuntime
                     .newRuntimeContainerClasspathEntry(systemLibsPath, IRuntimeClasspathEntry.STANDARD_CLASSES);
               final List<String> classpath = new ArrayList<String>();
               classpath.add(systemLibsEntry.getMemento());

               workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpath);
               workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, true);
               workingCopy.setAttribute("org.eclipse.jdt.junit.TEST_KIND", "org.eclipse.jdt.junit.loader.junit4");

               final ILaunchConfiguration config = workingCopy.doSave();
               config.launch(ILaunchManager.RUN_MODE, monitor);
            }
            catch (final CoreException e)
            {
               e.printStackTrace();
            }
         }
      }
      return flag;
   }

   @Override
   public void propertyChange(final PropertyChangeEvent evt)
   {
      if (EclipseQuestionnaireEntry.PROPERTY_CURRENT_ANSWER.equals(evt.getPropertyName()))
      {
         trackingService.addTrackingEvent(getCurrentRunningEvaluation(), new AnswerChangedEvent(
               (QuestionnaireEntry)evt.getSource(), stepService.getCurrentRunningEvaluationStep().getId()));
      }

   }
}