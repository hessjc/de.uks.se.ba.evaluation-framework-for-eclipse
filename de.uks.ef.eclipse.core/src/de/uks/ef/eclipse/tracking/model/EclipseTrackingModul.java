package de.uks.ef.eclipse.tracking.model;

import java.util.HashSet;
import java.util.Set;

import de.uks.ef.core.model.ReportModul;
import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.core.model.TrackingManager;
import de.uks.ef.core.model.TrackingModul;

public abstract class EclipseTrackingModul implements TrackingModul
{
   private TrackingManager trackingManager;

   @Override
   public void setTrackingManager(TrackingManager value)
   {
      this.trackingManager = value;
   }

   @Override
   public TrackingManager getTrackingManager()
   {
      return this.trackingManager;
   }

   private Set<TrackingEvent> trackingEvent;

   @Override
   public Set<TrackingEvent> getTrackingEvent()
   {
      if (trackingEvent == null)
      {
         trackingEvent = new HashSet<TrackingEvent>();
      }
      return this.trackingEvent;
   }

   public void addTrackingEvent(TrackingEvent trackingEvent)
   {
      getTrackingEvent().add(trackingEvent);
   }

   private Set<ReportModul> reportModul;

   @Override
   public Set<ReportModul> getReportModul()
   {
      if (reportModul == null)
      {
         reportModul = new HashSet<ReportModul>();
      }
      return reportModul;
   }

   public void addReportModul(ReportModul reportModul)
   {
      getReportModul().add(reportModul);
   }

   public void removeReportModuls()
   {
      reportModul = new HashSet<ReportModul>();
   }

   private boolean started = false;

   public void setStarted(boolean started)
   {
      this.started = started;
   }

   @Override
   public boolean isStarted()
   {
      return started;
   }

   @Override
   public void startTracking()
   {
      setStarted(true);
   }

   @Override
   public void stopTracking()
   {
      setStarted(false);
   }

   public abstract void initReportModuls();

   public abstract void initListener();

   public abstract TrackingEvent parseEvent(String eventString);
}
