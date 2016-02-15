package de.uks.ef.core.model;

import java.util.Set;

public interface TrackingModul
{
/**
    * <pre>
    *           0..1     0..1
    * TrackingModul ------------------------- TrackingManager
    *           trackingModul        &lt;       trackingManager
    * </pre>
    */
   public void setTrackingManager(TrackingManager value);
   
   public TrackingManager getTrackingManager();
   
   /**
    * <pre>
    *           0..1     0..*
    * TrackingModul ------------------------- TrackingEvent
    *           trackingModul        &gt;       trackingEvent
    * </pre>
    */
   public Set<TrackingEvent> getTrackingEvent();

   /**
    * <pre>
    *           0..1     0..*
    * TrackingModul ------------------------- ReportModul
    *           trackingModul        &gt;       reportModul
    * </pre>
    */
   public Set<ReportModul> getReportModul();

   public boolean isStarted();

   public void startTracking();

   public void stopTracking();

	public String getId();
}