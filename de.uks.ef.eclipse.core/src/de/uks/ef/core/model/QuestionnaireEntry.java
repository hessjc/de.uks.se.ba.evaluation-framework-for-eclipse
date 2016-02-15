package de.uks.ef.core.model;

public interface QuestionnaireEntry extends Element
{
   public String getQuestion();

   public void setQuestion(String question);

   public String getCurrentAnswer();

   public void setCurrentAnswer(String currentAnswer);

   public void setType(String type);

   public String getType();

   public String getQuestionId();

   public void setQuestionId(final String questionId);

   public String getId();

   public void setId(final String id);

   public void reset();
}
