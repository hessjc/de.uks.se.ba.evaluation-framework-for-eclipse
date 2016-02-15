package de.uks.ef.eclipse.report.questionnaire;

import javax.inject.Inject;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.core.utils.Constants;
import de.uks.ef.eclipse.tracking.model.AnswerChangedEvent;
import de.uks.ef.eclipse.tracking.model.EclipseTrackingModul;

public class QuestionnaireTrackingModule extends EclipseTrackingModul
{
   @Inject
   private QuestionnaireReportModule reportModule;

   @Override
   public void initReportModuls()
   {
      addReportModul(reportModule);
   }

   @Override
   public void initListener()
   {

   }

   @Override
   public String getId()
   {
      return Constants.QUESTIONNAIRE;
   }

   @Override
   public TrackingEvent parseEvent(String eventString)
   {
      String[] eventStrings = eventString.split(";");
      TrackingEvent event = null;
      switch (eventStrings[3])
      {
         case "AnswerChangedEvent":
            event = AnswerChangedEvent.parseEvent(eventString);
            break;
      }
      return event;
   }
}
