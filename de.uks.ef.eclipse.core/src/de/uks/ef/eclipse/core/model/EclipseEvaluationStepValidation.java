package de.uks.ef.eclipse.core.model;

import de.uks.ef.core.model.EvaluationStepConfiguration;
import de.uks.ef.core.model.EvaluationStepValidation;

public class EclipseEvaluationStepValidation implements EvaluationStepValidation
{
   private EvaluationStepConfiguration evaluationStepConfiguration;

   @Override
   public EvaluationStepConfiguration getEvaluationStepConfiguration()
   {
      return evaluationStepConfiguration;
   }

   @Override
   public void setEvaluationStepConfiguration(EvaluationStepConfiguration evaluationStepConfiguration)
   {
      this.evaluationStepConfiguration = evaluationStepConfiguration;
   }
}
