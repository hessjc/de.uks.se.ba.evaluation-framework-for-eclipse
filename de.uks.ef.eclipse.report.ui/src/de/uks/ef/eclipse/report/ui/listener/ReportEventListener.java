package de.uks.ef.eclipse.report.ui.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.log.Logger;

import de.uks.ef.eclipse.report.configuration.ReportService;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class ReportEventListener implements PropertyChangeListener
{
   @Inject
   private Logger LOGGER;

   @Inject
   private ReportService reportService;

   @PostConstruct
   public void init()
   {
      LOGGER.debug("ReportPropertyChangeListener injected.");
   }

   @Override
   public void propertyChange(PropertyChangeEvent evt)
   {
      if (evt.getPropertyName().equals("evaluationFinished")) reportService.writeReport();
   }
}
