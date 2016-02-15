package de.uks.ef.core.model;

public interface EvaluationSender
{
   /**
    * <pre>
    *           0..1     0..1
    * EvaluationSender ------------------------- EvaluationConfiguration
    *           evaluationSender        &gt;       evaluationConfiguration
    * </pre>
    */
   public EvaluationConfiguration getEvaluationConfiguration();

   public void setEvaluationConfiguration(EvaluationConfiguration evaluationConfiguration);

   public void sendToUrl();
}