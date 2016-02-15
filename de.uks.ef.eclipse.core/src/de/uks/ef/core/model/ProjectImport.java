package de.uks.ef.core.model;

public interface ProjectImport
{
   public String getName();

   public void setName(String name);

   public String getProject();

   public void setProject(String project);

   /**
    * <pre>
    *           0..10..1
    * EvaluationStepImportProject ------------------------- EvaluationStepConfiguration
    *           evaluationStepImportProject        &gt;       evaluationStepConfiguration
    * </pre>
    */
   public EvaluationStepConfiguration getEvaluationStepConfiguration();

   public void setEvaluationStepConfiguration(EvaluationStepConfiguration evaluationStepConfiguration);
}