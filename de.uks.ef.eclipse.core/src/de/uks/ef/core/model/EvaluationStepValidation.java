package de.uks.ef.core.model;

public interface EvaluationStepValidation
{
   /**
    * <pre>
    *           0..*     0..1
    * EvaluationStepValidation ------------------------- EvaluationStepConfiguration
    *           evaluationStepValidation        &gt;       evaluationStepConfiguration
    * </pre>
    */
   public void setEvaluationStepConfiguration(EvaluationStepConfiguration value);

   public EvaluationStepConfiguration getEvaluationStepConfiguration();
}