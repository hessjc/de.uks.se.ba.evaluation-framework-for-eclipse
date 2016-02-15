package de.uks.ef.core.model;

import java.util.Date;
import java.util.List;

public interface EvaluationSubStep extends Element
{
   public String getId();

   public void setId(String id);

   public String getName();

   public void setName(String value);

   public String getDescription();

   public void setDescription(String value);

   /**
    * <pre>
    *           0..10..1
    * EvaluationSubStep ------------------------- EvaluationStep
    *           evaluationSubStep        &gt;       evaluationStep
    * </pre>
    */
   public EvaluationStep getEvaluationStep();

   public void setEvaluationStep(EvaluationStep value);

   public List<QuestionnaireEntry> getQuestionnaireEntries();

   public void addQuestionnaireEntry(QuestionnaireEntry questionnaireEntry);

   public void reset();
}