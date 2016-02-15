package de.uks.ef.eclipse.report.time;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.core.utils.DateHelper;
import de.uks.ef.eclipse.core.listener.time.event.StartStepEvent;
import de.uks.ef.eclipse.core.listener.time.event.StopStepEvent;
import de.uks.ef.eclipse.report.core.EclipsePieChartReport;

public class StepExecutionTimePieChartReport extends EclipsePieChartReport
{
   @Override
   public String getTitle()
   {
      return "Execution time of each step";
   }

   public Map<String, Integer> getData(java.util.Collection<TrackingEvent> events)
   {
      Map<EvaluationStep, SortedSet<TrackingEvent>> timeSteps = new HashMap<EvaluationStep, SortedSet<TrackingEvent>>();
      for (TrackingEvent event : events)

      {
         final EvaluationStep ev;
         if (event instanceof StartStepEvent)
         {
            ev = event.geEvaluationStep();
         }
         else if (event instanceof StopStepEvent)
         {
            ev = event.geEvaluationStep();
         }
         else
         {
            ev = null;
         }

         if (ev != null)
         {
            SortedSet<TrackingEvent> set = timeSteps.get(ev);
            if (set == null)
            {
               set = new TreeSet<TrackingEvent>(new Comparator<TrackingEvent>() {

                  @Override
                  public int compare(TrackingEvent o1, TrackingEvent o2)
                  {
                     if (o1 == o2)
                     {
                        return 0;
                     }
                     Date date1 = DateHelper.formatStringToDate(o1.getTimestamp());
                     Date date2 = DateHelper.formatStringToDate(o2.getTimestamp());
                     if (date1.before(date2))
                     {
                        return -1;
                     }
                     else if (date2.before(date1))
                     {
                        return 1;
                     }

                     return 1;
                  }
               });
               timeSteps.put(ev, set);
            }
            set.add(event);
         }
      }

      Map<String, Integer> data = new HashMap<String, Integer>();

      for (Entry<EvaluationStep, SortedSet<TrackingEvent>> entry : timeSteps.entrySet())

      {
         EvaluationStep step = entry.getKey();
         SortedSet<TrackingEvent> times = entry.getValue();

         int secondCount = 0;
         Date startDate = null;
         for (TrackingEvent trackingEvent : times)
         {
            Date time = DateHelper.formatStringToDate(trackingEvent.getTimestamp());
            if (trackingEvent instanceof StartStepEvent)
            {
               startDate = time;
            }
            else if (trackingEvent instanceof StopStepEvent)
            {
               long difference = time.getTime() - startDate.getTime();
               secondCount += difference / 1000;
               startDate = null;
            }
         }

         data.put(step.getName(), secondCount);
      }
      return data;
   }
}