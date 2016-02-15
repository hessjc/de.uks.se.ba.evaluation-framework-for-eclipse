package de.uks.ef.eclipse.core.model;

import java.util.LinkedHashMap;
import java.util.Map;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationConfiguration;
import de.uks.ef.core.model.EvaluationStep;

public class EclipseEvaluation implements Evaluation
{
   private String id = "";

   @Override
   public String getId()
   {
      if (id.equals("")) return this.toString();
      else return id;
   }

   @Override
   public void setId(String id)
   {
      this.id = id;
   }

   private String name;

   @Override
   public String getName()
   {
      return this.name;
   }

   @Override
   public void setName(String name)
   {
      this.name = name;
   }

   private String description;

   @Override
   public String getDescription()
   {
      return this.description;
   }

   @Override
   public void setDescription(String description)
   {
      this.description = description;
   }

   private boolean started;

   @Override
   public boolean isStarted()
   {
      return started;
   }

   @Override
   public void setStarted(boolean started)
   {
      this.started = started;
   }

   public EclipseEvaluation(String id)
   {
      this.id = id;
   }

   public EclipseEvaluation()
   {

   }

   private EvaluationConfiguration evaluationConfiguration;

   @Override
   public EvaluationConfiguration getEvaluationConfiguration()
   {
      return this.evaluationConfiguration;
   }

   @Override
   public void setEvaluationConfiguration(EvaluationConfiguration evaluationConfiguration)
   {
      this.evaluationConfiguration = evaluationConfiguration;
   }

   private Map<String, EvaluationStep> evaluationStep;

   @Override
   public Map<String, EvaluationStep> getEvaluationStep()
   {
      if (this.evaluationStep == null)
      {
         this.evaluationStep = new LinkedHashMap<String, EvaluationStep>();
      }
      return this.evaluationStep;
   }

   @Override
   public void addEvaluationStep(EvaluationStep evaluationStep)
   {
      getEvaluationStep().put(evaluationStep.getId(), evaluationStep);
   }

   @Override
   public void start()
   {
      setStarted(true);
   }

   @Override
   public void reset()
   {
      setStarted(false);
      for (EvaluationStep step : getEvaluationStep().values())
      {
         step.reset();
      }
   }
}