package de.uks.ef.eclipse.tracking.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import de.uks.ef.core.model.EvaluationManager;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.core.model.TrackingManager;
import de.uks.ef.core.model.TrackingModul;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class EclipseTrackingManager implements TrackingManager
{
   @Inject
   private Logger LOGGER;

   public EclipseTrackingManager()
   {
   }

   @PostConstruct
   public void init()
   {
      LOGGER.debug("EclipseTrackingManager injected.");
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

   private EvaluationManager evaluationManager;

   @Override
   public EvaluationManager getEvaluationManager()
   {
      return this.evaluationManager;
   }

   public void setEvaluationManager(EvaluationManager value)
   {
      this.evaluationManager = value;
   }

   private Map<String, TrackingModul> trackingModul;

   @Override
   public Map<String, TrackingModul> getTrackingModul()
   {
      if (this.trackingModul == null)
      {
         this.trackingModul = new HashMap<String, TrackingModul>();
      }
      return this.trackingModul;
   }

   @Override
   public void addTrackingModul(TrackingModul trackingModul)
   {
      getTrackingModul().put(trackingModul.getId(), trackingModul);
   }

   private Map<String, List<TrackingEvent>> trackingEvents;

   @Override
   public Map<String, List<TrackingEvent>> getTrackingEvent()
   {
      if (this.trackingEvents == null)
      {
         this.trackingEvents = new HashMap<String, List<TrackingEvent>>();
      }
      return trackingEvents;
   }

   @Override
   public void addTrackingEvent(String evaluationId, TrackingEvent trackingEvent)
   {
      List<TrackingEvent> list = getTrackingEvent().get(evaluationId);
      if (list == null)
      {
         list = new LinkedList<>();
         getTrackingEvent().put(evaluationId, list);
      }
      list.add(trackingEvent);
   }
}