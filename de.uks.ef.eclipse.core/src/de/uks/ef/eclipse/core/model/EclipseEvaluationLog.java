package de.uks.ef.eclipse.core.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import de.uks.ef.core.model.EvaluationLog;
import de.uks.ef.core.model.EvaluationLogEvent;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class EclipseEvaluationLog implements EvaluationLog
{
   @Inject
   private Logger LOGGER;

   public EclipseEvaluationLog()
   {

   }

   @PostConstruct
   private void init()
   {
      LOGGER.debug("EclipseEvaluationLog injected.");
   }

   private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

   @Override
   public void addPropertyChangeListener(PropertyChangeListener listener)
   {
      changeSupport.addPropertyChangeListener(listener);
   }

   @Override
   public void removePropertyChangeListener(PropertyChangeListener listener)
   {
      changeSupport.removePropertyChangeListener(listener);
   }
   
   @Override
   public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      changeSupport.firePropertyChange(propertyName, oldValue, newValue);
   }

   private HashMap<String, List<EvaluationLogEvent>> evaluationLogEvent;

   @Override
   public HashMap<String, List<EvaluationLogEvent>> getEvaluationLogEvent()
   {
      if (this.evaluationLogEvent == null)
      {
         this.evaluationLogEvent = new LinkedHashMap<String, List<EvaluationLogEvent>>();
      }
      return this.evaluationLogEvent;
   }

   @Override
   public void addEvaluationLogEvent(EvaluationLogEvent evaluationLogEvent)
   {
      List<EvaluationLogEvent> list = getEvaluationLogEvent().get(evaluationLogEvent.getEvaluation());

      if (list == null)
      {
         getEvaluationLogEvent().put(evaluationLogEvent.getEvaluation(), new ArrayList<EvaluationLogEvent>());
         list = getEvaluationLogEvent().get(evaluationLogEvent.getEvaluation());
      }
      list.add(evaluationLogEvent);
      firePropertyChange("addEvaluationLogEvent", null, evaluationLogEvent);
   }
}