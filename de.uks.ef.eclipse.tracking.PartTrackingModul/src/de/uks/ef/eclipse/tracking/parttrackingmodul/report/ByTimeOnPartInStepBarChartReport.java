package de.uks.ef.eclipse.tracking.parttrackingmodul.report;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.report.core.EclipseMultiBarChartReport;

public class ByTimeOnPartInStepBarChartReport extends EclipseMultiBarChartReport
{
   @Override
   public String getTitle()
   {
      return "Commands by Count in Steps BarChart";
   }

   @Override
   public Map<String, Map<String, Integer>> getData(final Collection<TrackingEvent> events)
   {
      Map<String, Map<String, Integer>> commands = new LinkedHashMap<String, Map<String, Integer>>();

      for (TrackingEvent event : events)
      {
         EvaluationStep evaluationStep = event.geEvaluationStep();

         Map<String, Integer> map = commands.get(evaluationStep.getName());

         if (map == null)
         {
            map = new LinkedHashMap<String, Integer>();
         }

         String commandName = event.getEncoding().split("\\;")[3];

         Integer count;
         if (map.get(commandName) != null)
         {
            count = map.get(commandName).intValue() + 1;
         }
         else
         {
            count = 1;
         }
         map.put(commandName, count);
         commands.put(evaluationStep.getName(), map);
      }
      return commands;
   }
}