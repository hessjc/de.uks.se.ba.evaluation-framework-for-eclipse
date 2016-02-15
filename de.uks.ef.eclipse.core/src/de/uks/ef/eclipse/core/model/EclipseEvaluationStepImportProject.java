package de.uks.ef.eclipse.core.model;

import de.uks.ef.core.model.EvaluationStepConfiguration;

public class EclipseEvaluationStepImportProject extends EclipseProjectImport
{
   private EvaluationStepConfiguration evaluationStepConfiguration;

   @Override
   public void setEvaluationStepConfiguration(EvaluationStepConfiguration evaluationStepConfiguration)
   {
      this.evaluationStepConfiguration = evaluationStepConfiguration;
   }

   @Override
   public EvaluationStepConfiguration getEvaluationStepConfiguration()
   {
      return evaluationStepConfiguration;
   }
}