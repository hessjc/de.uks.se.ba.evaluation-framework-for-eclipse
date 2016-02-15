package de.uks.ef.core.model;

import java.util.List;
import java.util.Map;

import de.uks.ef.core.databinding.PropertyChangeModel;

public interface TrackingManager extends PropertyChangeModel
{
   public Map<String, List<TrackingEvent>> getTrackingEvent();

   public void addTrackingEvent(String evaluationId, TrackingEvent trackingEvent);

   /**
    * <pre>
    *           0..1     0..1
    * TrackingManager ------------------------ EvaluationManager
    *           trackingManager        &lt;       evaluationManager
    * </pre>
    */
   public EvaluationManager getEvaluationManager();

   public void setEvaluationManager(EvaluationManager evaluationManager);

   /**
    * <pre>
    *           0..1     0..1    * TrackingManager ------------------------- TrackingModul
    *           trackingManager        &gt;       trackingModul
    * </pre>
    */
   public Map<String, TrackingModul> getTrackingModul();

   public void addTrackingModul(TrackingModul trackingModul);
}