package de.uks.ef.eclipse.core.model;

import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.Map;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationStepConfiguration;
import de.uks.ef.core.model.EvaluationSubStep;

public class EclipseEvaluationStep extends EclipseElement implements EvaluationStep
{
   private String id = "";
   private String name;
   private String description;
   private boolean optional;
   private EvaluationStepConfiguration evaluationStepConfiguration;
   private Map<String, EvaluationSubStep> evaluationSubStep;
   private Evaluation evaluation;

   public boolean isOptional()
   {
      return optional;
   }

   public void setOptional(boolean optional)
   {
      this.optional = optional;
   }

   @Override
   public String getId()
   {
      if (id.equals(""))
      {
         return this.toString();
      }
      else
      {
         return id;
      }
   }

   @Override
   public void setId(final String id)
   {
      this.id = id;
   }

   @Override
   public String getName()
   {
      return this.name;
   }

   @Override
   public void setName(final String name)
   {
      this.name = name;
   }

   @Override
   public String getDescription()
   {
      return this.description;
   }

   @Override
   public void setDescription(final String description)
   {
      this.description = description;
   }

   public EclipseEvaluationStep(final String id)
   {
      this.id = id;
   }

   public EclipseEvaluationStep()
   {

   }

   @Override
   public EvaluationStepConfiguration getEvaluationStepConfiguration()
   {
      return this.evaluationStepConfiguration;
   }

   @Override
   public void setEvaluationStepConfiguration(final EvaluationStepConfiguration evaluationStepConfiguration)
   {
      this.evaluationStepConfiguration = evaluationStepConfiguration;
   }

   @Override
   public Map<String, EvaluationSubStep> getEvaluationSubStep()
   {
      if (this.evaluationSubStep == null)
      {
         this.evaluationSubStep = new LinkedHashMap<String, EvaluationSubStep>();
      }
      return this.evaluationSubStep;
   }

   @Override
   public void addEvaluationSubStep(final EvaluationSubStep evaluationSubStep)
   {
      getEvaluationSubStep().put(evaluationSubStep.getId(), evaluationSubStep);
   }

   @Override
   public Evaluation getEvaluation()
   {
      return this.evaluation;
   }

   @Override
   public void setEvaluation(final Evaluation evaluation)
   {
      this.evaluation = evaluation;
   }

   @Override
   public void addListener(final PropertyChangeListener propertyChangeListener)
   {
      super.addListener(propertyChangeListener);
      for (final EvaluationSubStep evaluationSubStep : getEvaluationSubStep().values())
      {
         evaluationSubStep.addListener(propertyChangeListener);
      }
   }

   @Override
   public void reset()
   {
      for (EvaluationSubStep subStep : getEvaluationSubStep().values())
      {
         subStep.reset();
      }
   }
}