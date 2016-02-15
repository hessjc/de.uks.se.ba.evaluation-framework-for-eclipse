package de.uks.ef.eclipse.report.model;

import java.util.ArrayList;
import java.util.Collection;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.Report;
import de.uks.ef.core.model.ReportModul;
import de.uks.ef.core.model.TrackingModul;

public abstract class EclipseReportModul implements ReportModul
{
   private TrackingModul trackingModul;

   @Override
   public TrackingModul getTrackingModul()
   {
      return trackingModul;
   }

   @Override
   public void setTrackingModul(TrackingModul trackingModul)
   {
      this.trackingModul = trackingModul;
   }

   private Collection<Report> reports = new ArrayList<Report>();

   @Override
   public Collection<Report> getReports(Evaluation evaluation)
   {
      return reports;
   }

   public void addReport(Report report)
   {
      reports.add(report);
      report.setReportModul(this);
   }
}
