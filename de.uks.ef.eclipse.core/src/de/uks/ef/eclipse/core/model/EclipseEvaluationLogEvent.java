package de.uks.ef.eclipse.core.model;

import de.uks.ef.core.model.EvaluationLog;
import de.uks.ef.core.model.EvaluationLogEvent;

public class EclipseEvaluationLogEvent implements EvaluationLogEvent
{
   private String evaluation;

   @Override
   public String getEvaluation()
   {
      return evaluation;
   }

   @Override
   public void setEvaluation(String evaluation)
   {
      this.evaluation = evaluation;
   }

   private String description;

   @Override
   public String getDescription()
   {
      return description;
   }

   @Override
   public void setDescription(String description)
   {
      this.description = description;
   }

   private EvaluationLog evaluationLog;

   @Override
   public EvaluationLog getEvaluationLog()
   {
      return evaluationLog;
   }

   @Override
   public void setEvaluationLog(EvaluationLog evaluationLog)
   {
      this.evaluationLog = evaluationLog;
   }
}