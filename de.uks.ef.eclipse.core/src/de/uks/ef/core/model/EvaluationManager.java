package de.uks.ef.core.model;

import java.util.Set;

public interface EvaluationManager
{
   public String getEvaluationProjectName();

   public void setEvaluationProjectName(String evaluationprojectName);

   /**
    * <pre>
    *           0..1     0..*
    * EvaluationManager ------------------------- EvaluationConfiguration
    *           evaluationManager        &lt;       evaluationConfiguration
    * </pre>
    */
   public Set<EvaluationConfiguration> getEvaluationConfiguration();

   public void addEvaluationConfiguration(EvaluationConfiguration evaluationConfiguration);

   public Evaluation findEvaluation(String elementId);

   /**
    * <pre>
    *           0..1     0..1
    * EvaluationManager ------------------------- TrackingManager
    *           evaluationManager        &lt;       trackingManager
    * </pre>
    */
   public TrackingManager getTrackingManager();
}