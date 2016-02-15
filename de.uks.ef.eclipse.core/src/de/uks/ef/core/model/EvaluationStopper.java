package de.uks.ef.core.model;

public interface EvaluationStopper
{
   /**
    * <pre>
    *           0..1     0..1
    * EvaluationStopper ------------------------- EvaluationConfiguration
    *           evaluationStopper        &gt;   evaluationConfiguration
    * </pre>
    */
   public EvaluationConfiguration getEvaluationConfiguration();

   public void setEvaluationConfiguration(EvaluationConfiguration evaluationConfiguration);
}