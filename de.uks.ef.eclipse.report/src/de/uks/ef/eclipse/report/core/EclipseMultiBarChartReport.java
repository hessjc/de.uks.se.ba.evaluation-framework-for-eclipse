package de.uks.ef.eclipse.report.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.core.model.EclipseReport;
import de.uks.ef.eclipse.report.configuration.ReportService;
import de.uks.ef.eclipse.report.model.EclipseChartReport;

public abstract class EclipseMultiBarChartReport extends EclipseChartReport
{
   @SuppressWarnings("unused")
   @Inject
   private ReportService reportService;

   public abstract Map<String, Map<String, Integer>> getData(final Collection<TrackingEvent> events);

   public EclipseMultiBarChartReport()
   {
      this(true);
   }

   public EclipseMultiBarChartReport(final boolean isAggregated)
   {
      super(isAggregated);
   }
   
   @Override
   public Chart getChart(Collection<EclipseReport> eclipseReports)
   {
      Map<String, Map<String, Integer>> aggregatedBarData = new LinkedHashMap<String, Map<String, Integer>>();
      for (EclipseReport eclipseReport : eclipseReports)
      {
         Map<String, Map<String, Integer>> data = getData(getReportModul().getFilteredEvents(eclipseReport));
         for (Entry<String, Map<String, Integer>> entry : data.entrySet())
         {
            Map<String, Integer> barData = aggregatedBarData.get(entry.getKey());
            if (barData == null)
            {
               barData = new HashMap<String, Integer>();
               aggregatedBarData.put(entry.getKey(), barData);
            }

            for (Entry<String, Integer> innerEntry : entry.getValue().entrySet())
            {
               Integer count = barData.get(innerEntry.getKey());
               if (count == null)
               {
                  count = 0;
               }
               barData.put(innerEntry.getKey(), count + innerEntry.getValue());
            }
         }
      }
      if (!isAggregated())
      {
         for (Entry<String, Map<String, Integer>> entry : aggregatedBarData.entrySet())
         {
            Map<String, Integer> innerMap = entry.getValue();
            for (Entry<String, Integer> innerEntry : innerMap.entrySet())
            {
               innerMap.put(innerEntry.getKey(), innerEntry.getValue() / eclipseReports.size());
            }
         }
      }
      return create(getTitle(), aggregatedBarData);
   }

   public static Chart create(final String title, final Map<String, Map<String, Integer>> data)
   {
      ChartWithAxes cwaBar = ChartWithAxesImpl.create();

      try
      {
         cwaBar.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
         cwaBar.setType("Multi-Bar Chart"); //$NON-NLS-1$
         cwaBar.setSubType("Side-by-side"); //$NON-NLS-1$

         // Plot
         cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
         cwaBar.getBlock().getOutline().setVisible(true);

         // Title
         cwaBar.getTitle().getLabel().getCaption().setValue(title);

         // Legend
         Legend legend = cwaBar.getLegend();
         legend.setItemType(LegendItemType.SERIES_LITERAL);
         legend.getText().getFont().setSize(10);
         legend.setWrappingSize(200);

         // X-Axis
         Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
         xAxisPrimary.setType(AxisType.TEXT_LITERAL);
         xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
         xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

         // Y-Axis
         Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
         yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
         yAxisPrimary.setType(AxisType.LINEAR_LITERAL);

         ArrayList<String> categoryList = new ArrayList<String>();
         Set<String> events = new HashSet<String>();

         for (Entry<String, Map<String, Integer>> entry : data.entrySet())
         {
            categoryList.add(entry.getKey());
            for (Entry<String, Integer> map : entry.getValue().entrySet())
            {
               events.add(map.getKey());
            }
         }

         List<BarSeries> barSeries = new LinkedList<BarSeries>();

         for (String event : events)
         {
            ArrayList<Integer> valueList = new ArrayList<Integer>();

            for (Entry<String, Map<String, Integer>> entry : data.entrySet())
            {
               Integer count = entry.getValue().get(event);
               if (count != null)
               {
                  valueList.add(count);
               }
               else
               {
                  valueList.add(0);
               }
            }

            NumberDataSet orthoValues = NumberDataSetImpl.create(valueList);

            BarSeries bs = (BarSeries)BarSeriesImpl.create();
            bs.setDataSet(orthoValues);
            bs.getLabel().setVisible(true);
            bs.setRiser(RiserType.RECTANGLE_LITERAL);
            bs.setSeriesIdentifier(event);

            barSeries.add(bs);
         }

         // Data Set
         TextDataSet categoryValues = TextDataSetImpl.create(categoryList);

         // Add two series to one series definition
         SeriesDefinition sdY = SeriesDefinitionImpl.create();
         yAxisPrimary.getSeriesDefinitions().add(sdY);
         for (BarSeries barSerie : barSeries)
         {
            sdY.getSeries().add(barSerie);
         }

         // X-Series
         Series seCategory = SeriesImpl.create();
         seCategory.setDataSet(categoryValues);

         SeriesDefinition sdX = SeriesDefinitionImpl.create();
         sdX.getSeriesPalette().shift(0);
         xAxisPrimary.getSeriesDefinitions().add(sdX);
         sdX.getSeries().add(seCategory);

         // Rotate the chart
         cwaBar.setRotation(Rotation3DImpl.create(new Angle3D[] {
               Angle3DImpl.create(-10, 25, 0)
         }));

      }
      catch (Exception e)
      {

      }
      return cwaBar;
   }
}