package de.uks.ef.core.model;

import java.util.Map;

public interface EvaluationStep extends Element
{
   public boolean isOptional();
   
   public void setOptional(final boolean optional);
   
   public String getId();

   public void setId(String id);

   public String getName();

   public void setName(String name);

   public String getDescription();

   public void setDescription(String description);

   /**
    * <pre>
    *           0..1     0..1
    * EvaluationStep ------------------------- EvaluationStepConfiguration
    *           evaluationStep        &gt;       evaluationStepConfiguration
    * </pre>
    */
   public EvaluationStepConfiguration getEvaluationStepConfiguration();

   public void setEvaluationStepConfiguration(EvaluationStepConfiguration evaluationStepConfiguration);

   /**
    * <pre>
    *           0..10..1
    * EvaluationStep ------------------------- Evaluation
    *           evaluationStep        &gt;       evaluation
    * </pre>
    */
   public Evaluation getEvaluation();

   public void setEvaluation(Evaluation evaluation);

   /**
    * <pre>
    *           0..1     0..1    * EvaluationStep ------------------------- EvaluationSubStep
    *           evaluationStep        &lt;       evaluationSubStep
    * </pre>
    */
   public Map<String, EvaluationSubStep> getEvaluationSubStep();

   public void addEvaluationSubStep(EvaluationSubStep evaluationSubStep);

   public void reset();
}