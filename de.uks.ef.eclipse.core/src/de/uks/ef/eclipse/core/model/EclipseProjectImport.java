package de.uks.ef.eclipse.core.model;

import de.uks.ef.core.model.EvaluationStepConfiguration;
import de.uks.ef.core.model.ProjectImport;

public class EclipseProjectImport implements ProjectImport
{
   private String project;

   public String getProject()
   {
      if (project.equals(""))
      {
         return this.toString();
      }
      else
      {
         return project;
      }
   }

   public void setProject(final String project)
   {
      this.project = project;
   }

   private String name;

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public void setName(final String name)
   {
      this.name = name;
   }

   private EvaluationStepConfiguration evaluationStepConfiguration;

   @Override
   public EvaluationStepConfiguration getEvaluationStepConfiguration()
   {
      return evaluationStepConfiguration;
   }

   @Override
   public void setEvaluationStepConfiguration(final EvaluationStepConfiguration evaluationStepConfiguration)
   {
      this.evaluationStepConfiguration = evaluationStepConfiguration;
   }
}