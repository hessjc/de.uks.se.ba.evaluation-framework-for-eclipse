package de.uks.ef.core.model;

public interface EvaluationLogEvent
{
   public String getEvaluation();

   public void setEvaluation(String evaluation);

   public String getDescription();

   public void setDescription(String value);

   /**
    * <pre>
    *           0..10..1
    * EvaluationLogEvent ------------------------- EvaluationLog
    *           evaluationLogEvent        &gt;       evaluationLog
    * </pre>
    */
   public EvaluationLog getEvaluationLog();

   public void setEvaluationLog(EvaluationLog evaluationLog);
}