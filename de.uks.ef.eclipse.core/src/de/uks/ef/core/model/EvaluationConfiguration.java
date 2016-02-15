package de.uks.ef.core.model;

public interface EvaluationConfiguration
{
   public String getEmail();

   public void setEmail(String email);

   public String getUrl();

   public void setUrl(String url);

   public boolean isOptionalIdentity();

   public void setOptionalIdentity(boolean optionalIdentity);

   /**
    * <pre>
    *           0..1     0..1
    * EvaluationConfiguration ------------------------- Evaluation
    *           evaluationConfiguration        &lt;       evaluation
    * </pre>
    */
   public Evaluation getEvaluation();

   public void setEvaluation(Evaluation evaluation);

   /**
    * <pre>
    *           0..*     0..1
    * EvaluationConfiguration ------------------------- EvaluationManager
    *           evaluationConfiguration        &gt;       evaluationManager
    * </pre>
    */
   public EvaluationManager getEvaluationManager();

   public void setEvaluationManager(EvaluationManager evaluationManager);

   /**
    * <pre>
    *           0..1     0..1
    * EvaluationConfiguration ------------------------- EvaluationSender
    *           evaluationConfiguration        &lt;       evaluationSender
    * </pre>
    */
   public EvaluationSender getEvaluationSender();

   public void setEvaluationSender(EvaluationSender value);

   /**
    * <pre>
    *           0..1     0..1
    * EvaluationConfiguration ------------------------- EvaluationStopper
    *           evaluationConfiguration        &lt;       evaluationStopper
    * </pre>
    */
   public EvaluationStopper getEvaluationStopper();

   public void setEvaluationStopper(EvaluationStopper value);
}