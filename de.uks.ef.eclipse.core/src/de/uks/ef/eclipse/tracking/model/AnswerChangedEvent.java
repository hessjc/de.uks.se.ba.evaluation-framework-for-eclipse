package de.uks.ef.eclipse.tracking.model;

import de.uks.ef.core.model.QuestionnaireEntry;
import de.uks.ef.core.utils.DateHelper;
import de.uks.ef.eclipse.core.utils.Constants;

public class AnswerChangedEvent extends CoreTrackingEvent
{
   private final String questionId;
   private final String answer;

   public AnswerChangedEvent(final String timestamp, final String stepId, final String questionId,
         final String answer)
   {
      super(timestamp, stepId);
      this.questionId = questionId;
      this.answer = answer;
   }

   public AnswerChangedEvent(final QuestionnaireEntry newValue, final String stepId)
   {
      this(DateHelper.getDate(), stepId, newValue.getQuestionId(), newValue.getCurrentAnswer());
   }

   public String getQuestionId()
   {
      return questionId;
   }

   public String getAnswer()
   {
      return answer;
   }

   @Override
   public String getEncoding()
   {
      return super.getEncoding() + ";" + AnswerChangedEvent.class.getSimpleName() + ";" + questionId + ";" + answer;
   }

   public static AnswerChangedEvent parseEvent(final String eventString)
   {
      final String[] eventStrings = eventString.split(";");
      return new AnswerChangedEvent(eventStrings[0], eventStrings[2], eventStrings[4], eventStrings[5]);
   }

   @Override
   protected String getTrackingModulID()
   {
      return Constants.QUESTIONNAIRE;
   }
}
