package de.uks.ef.core.model;

import java.util.Map;

public interface Evaluation
{
   public String getId();

   public void setId(String id);

   public String getName();

   public void setName(String name);

   public String getDescription();

   public void setDescription(String description);

   public boolean isStarted();

   public void setStarted(boolean started);

   /**
    * <pre>
    *           0..1     0..1
    * Evaluation ------------------------- EvaluationConfiguration
    *           evaluation        &gt;       evaluationConfiguration
    * </pre>
    */
   public EvaluationConfiguration getEvaluationConfiguration();

   public void setEvaluationConfiguration(EvaluationConfiguration evaluationConfiguration);

   /**
    * <pre>
    *           0..1     0..1* Evaluation ------------------------- EvaluationStep
    *           evaluation        &lt;       evaluationStep
    * </pre>
    */
   public Map<String, EvaluationStep> getEvaluationStep();

   public void addEvaluationStep(EvaluationStep evaluationStep);

   public void start();

   public void reset();
}