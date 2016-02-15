package de.uks.ef.eclipse.core.configuration;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;

import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.utils.DateHelper;
import de.uks.ef.eclipse.core.listener.EvaluationStateListener;
import de.uks.ef.eclipse.core.listener.time.event.StartStepEvent;
import de.uks.ef.eclipse.core.listener.time.event.StopStepEvent;
import de.uks.ef.eclipse.tracking.configuration.TrackingService;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class StepService
{
   @Inject
   private Logger LOGGER;
   
   @Inject
   private TrackingService trackingService;

   private int currentRunningEvaluationStepIndex = 0;

   private EvaluationStep currentRunningEvaluationStep;

   private HashSet<EvaluationStateListener> listeners;

   private PropertyChangeSupport changes;

   public StepService()
   {
      listeners = new HashSet<EvaluationStateListener>();
      changes = new PropertyChangeSupport(this);
   }

   @PostConstruct
   private void init()
   {
      LOGGER.info("StepService injected.");
   }

   public int getCurrentRunningEvaluationStepIndex()
   {
      return currentRunningEvaluationStepIndex;
   }

   public void setCurrentRunningEvaluationStepIndex(int stepIndex)
   {
      this.currentRunningEvaluationStepIndex = stepIndex;
   }

   public EvaluationStep getCurrentRunningEvaluationStep()
   {
      return currentRunningEvaluationStep;
   }

   public void setCurrentRunningEvaluationStep(EvaluationStep evaluationStep)
   {
      this.currentRunningEvaluationStep = evaluationStep;
   }

   public HashSet<EvaluationStateListener> getListeners()
   {
      return listeners;
   }

   public void setListeners(HashSet<EvaluationStateListener> listeners)
   {
      this.listeners = listeners;
   }

   public void addPropertyChangeListener(PropertyChangeListener l)
   {
      List<PropertyChangeListener> list = Arrays.asList(changes.getPropertyChangeListeners());
      if (list.contains(l) == false) changes.addPropertyChangeListener(l);
   }

   public void removePropertyChangeListener(PropertyChangeListener l)
   {
      List<PropertyChangeListener> list = Arrays.asList(changes.getPropertyChangeListeners());
      if (list.contains(l) == true) changes.removePropertyChangeListener(l);
   }

   public void stopExecutionTime(EvaluationStep evaluationStep)
   {
      trackingService.addTrackingEvent(evaluationStep.getEvaluation(), new StopStepEvent(evaluationStep.getId(), DateHelper.getDate()));
      
      changes.firePropertyChange("StepStopped", evaluationStep, true);
      LOGGER.debug(evaluationStep.getName() + " stopped.");
   }

   public void startExecutionTime(EvaluationStep evaluationStep)
   {
      trackingService.addTrackingEvent(evaluationStep.getEvaluation(), new StartStepEvent(evaluationStep.getId(), DateHelper.getDate()));
      
      changes.firePropertyChange("StepStarted", evaluationStep, true);
      LOGGER.debug(evaluationStep.getName() + " started.");
   }

   public void initalisePlugInListener()
   {
      JUnitCore.addTestRunListener(new TestRunListener() {
         @Override
         public void sessionStarted(ITestRunSession session)
         {
            changes.firePropertyChange("ValidationStarted", currentRunningEvaluationStep, true);
         }

         @Override
         public void sessionFinished(final ITestRunSession session)
         {
            Result result = session.getTestResult(true);
            if (result.equals(Result.OK))
            {
               changes.firePropertyChange("ValidationSucceeded", currentRunningEvaluationStep, true);
            }
            else
            {
               changes.firePropertyChange("ValidationFailured", currentRunningEvaluationStep, true);
            }
            JUnitCore.removeTestRunListener(this);
         }
      });
   }
}