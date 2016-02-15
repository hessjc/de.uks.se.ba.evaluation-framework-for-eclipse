package de.uks.ef.eclipse.tracking.model;

import java.util.Map.Entry;

import de.uks.ef.core.model.EvaluationConfiguration;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.core.model.TrackingManager;
import de.uks.ef.core.model.TrackingModul;
import de.uks.ef.eclipse.core.model.EclipseEvaluationManager;

public abstract class EclipseTrackingEvent implements TrackingEvent
{
   private final String timestamp;
   private String stepId;

   public EclipseTrackingEvent(final String timestamp, final String stepId)
   {
      this.timestamp = timestamp;
      this.stepId = stepId;
   }

   public String getTimestamp()
   {
      return timestamp;
   }

   @Override
   public String getEncoding()
   {
      return getTimestamp() + ";" + getTrackingModulID() + ";" + stepId;
   }

   protected abstract String getTrackingModulID();

   /**
    * <pre>
    *           0..*     0..1
    * TrackingEvent ------------------------- TrackingModul
    *           trackingEvent        &lt;       trackingModul
    * </pre>
    */
   private TrackingModul trackingModul;

   @Override
   public void setTrackingModul(final TrackingModul trackingModul)
   {
      this.trackingModul = trackingModul;
   }

   @Override
   public TrackingModul getTrackingModul()
   {
      return this.trackingModul;
   }

   /**
    * <pre>
    *           0..*     0..1
    * TrackingEvent ------------------------- TrackingManager
    *           trackingEvent        &lt;       trackingManager
    * </pre>
    */
   private TrackingManager trackingManager;

   @Override
   public void setTrackingManager(final TrackingManager trackingManager)
   {
      this.trackingManager = trackingManager;
   }

   @Override
   public TrackingManager getTrackingManager()
   {
      return this.trackingManager;
   }

   @Override
   public String getStepId()
   {
      return stepId;
   }

   @Override
   public EvaluationStep geEvaluationStep()
   {
      for (EvaluationConfiguration configuration : EclipseEvaluationManager.get().getEvaluationConfiguration())
      {
         for (Entry<String, EvaluationStep> entry : configuration.getEvaluation().getEvaluationStep().entrySet())
         {
            if (stepId.equals(entry.getKey()))
            {
               return entry.getValue();
            }
         }
      }
      return null;
   }
}
