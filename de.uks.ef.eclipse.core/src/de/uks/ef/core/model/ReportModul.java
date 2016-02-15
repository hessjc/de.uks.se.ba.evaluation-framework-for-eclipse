package de.uks.ef.core.model;

import java.util.Collection;
import java.util.Set;

import de.uks.ef.eclipse.core.model.EclipseReport;

public interface ReportModul
{
   /**
    * <pre>
    *           0..*     0..1
    * ReportModul ------------------------- TrackingModul
    *           reportModul        &lt;       trackingModul
    * </pre>
    */
   public void setTrackingModul(TrackingModul trackingModul);

   public TrackingModul getTrackingModul();

   /**
    * <pre>
    *           0..1     0..*
    * ReportModul ------------------------- Report
    *           reportModul        &gt;       report
    * </pre>
    * 
    * @param evaluation
    */
   public Collection<Report> getReports(Evaluation evaluation);

   public Collection<TrackingEvent> getFilteredEvents(EclipseReport eclipseReport);
}