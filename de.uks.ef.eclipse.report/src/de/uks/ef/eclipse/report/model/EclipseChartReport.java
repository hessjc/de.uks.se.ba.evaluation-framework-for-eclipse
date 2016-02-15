package de.uks.ef.eclipse.report.model;

import java.util.Collection;

import org.eclipse.birt.chart.model.Chart;

import de.uks.ef.core.model.Report;
import de.uks.ef.core.model.ReportModul;
import de.uks.ef.eclipse.core.model.EclipseReport;

public abstract class EclipseChartReport implements Report
{
   private ReportModul reportModul;

   private final boolean isAggregated;

   public EclipseChartReport(final boolean isAggregated)
   {
      this.isAggregated = isAggregated;
   }

   @Override
   public void setReportModul(ReportModul value)
   {
      reportModul = value;
   }

   @Override
   public ReportModul getReportModul()
   {
      return reportModul;
   }

   public boolean isAggregated()
   {
      return isAggregated;
   }

   public abstract Chart getChart(final Collection<EclipseReport> eclipseReport);
}
