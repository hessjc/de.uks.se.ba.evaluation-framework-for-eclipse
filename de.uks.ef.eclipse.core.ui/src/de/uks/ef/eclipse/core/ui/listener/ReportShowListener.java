package de.uks.ef.eclipse.core.ui.listener;

import java.io.IOException;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.uks.ef.eclipse.report.configuration.ReportService;

public class ReportShowListener implements Listener
{
   private ReportService reportService;

   public ReportShowListener(ReportService reportService)
   {
      this.reportService = reportService;
   }

   @Override
   public void handleEvent(Event event)
   {
      try
      {
         reportService.openReport();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}