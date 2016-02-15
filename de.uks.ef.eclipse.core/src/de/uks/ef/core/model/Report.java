package de.uks.ef.core.model;

public interface Report
{
   /**
    * <pre>
    *           0..*     0..1
    * Report ------------------------- ReportModul
    *           report        &lt;       reportModul
    * </pre>
    */
   public void setReportModul(ReportModul value);

   public ReportModul getReportModul();

   public String getTitle();
}