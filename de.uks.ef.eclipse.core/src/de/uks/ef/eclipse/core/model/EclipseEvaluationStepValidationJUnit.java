package de.uks.ef.eclipse.core.model;

import de.uks.ef.core.model.ProjectImport;

public class EclipseEvaluationStepValidationJUnit extends EclipseEvaluationStepValidation implements
      ProjectImport
{
   private String name;
   private String project;

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

   @Override
   public String getProject()
   {
      return project;
   }

   @Override
   public void setProject(final String project)
   {
      this.project = project;

   }
}