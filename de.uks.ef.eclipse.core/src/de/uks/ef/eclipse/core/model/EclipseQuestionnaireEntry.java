package de.uks.ef.eclipse.core.model;

import de.uks.ef.core.model.QuestionnaireEntry;

public class EclipseQuestionnaireEntry extends EclipseElement implements QuestionnaireEntry
{
   public static final String PROPERTY_CURRENT_ANSWER = "currentAnswer";

   private String questionId;
   private String question;
   private String currentAnswer = "";
   private String type;
   private String id;

   @Override
   public String getQuestion()
   {
      return question;
   }

   @Override
   public void setQuestion(final String question)
   {
      this.question = question;
   }

   @Override
   public String getCurrentAnswer()
   {
      return currentAnswer;
   }

   @Override
   public void setCurrentAnswer(final String currentAnswer)
   {
      if (!this.currentAnswer.equals(currentAnswer))
      {
         this.currentAnswer = currentAnswer;
         fireNewValue(PROPERTY_CURRENT_ANSWER, this.currentAnswer);
      }
   }

   @Override
   public String getType()
   {
      return type;
   }

   @Override
   public void setType(final String type)
   {
      this.type = type;
   }

   @Override
   public String getQuestionId()
   {
      return questionId;
   }

   @Override
   public void setQuestionId(final String questionId)
   {
      this.questionId = questionId;
   }

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }
   
   @Override
   public void reset()
   {
      currentAnswer = "";
   }
}
