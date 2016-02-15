package de.uks.ef.eclipse.tracking.commandtrackingmodul.report;

import java.io.ObjectInputStream.GetField;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.report.core.EclipsePieChartReport;
import de.uks.ef.eclipse.tracking.commandtrackingmodul.event.PostExecuteSuccessEvent;

public class SuccessfulCommandsPieChartReport extends EclipsePieChartReport
{
   @Override
   public String getTitle()
   {
      return "Successful commands";
   }

   @Override
   public Map<String, Integer> getData(Collection<TrackingEvent> events)
   {
      Map<String, Integer> commands = new HashMap<String, Integer>();
      for (TrackingEvent event : events)
      {
         if (event instanceof PostExecuteSuccessEvent)
         {
            Integer count;
            String commandType = event.getEncoding().split("\\;")[4];
            if (commands.get(commandType) != null)
            {
               count = commands.get(commandType).intValue() + 1;
            }
            else
            {
               count = 1;
            }
            commands.put(commandType, count);
         }
      }
      for (Entry<String, Integer> command : commands.entrySet())
      {
         commands.put(command.getKey(), command.getValue());
      }
      return commands;
   }
}