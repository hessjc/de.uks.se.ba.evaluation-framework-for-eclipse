package de.uks.ef.eclipse.report.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.eclipse.core.model.EclipseReport;
import de.uks.ef.eclipse.report.model.EclipseChartReport;

public abstract class EclipsePieChartReport extends EclipseChartReport
{
   public abstract Map<String, Integer> getData(final Collection<TrackingEvent> events);

   public EclipsePieChartReport()
   {
      this(false);
   }

   public EclipsePieChartReport(final boolean isAggregated)
   {
      super(isAggregated);
   }

   @Override
   public Chart getChart(final Collection<EclipseReport> eclipseReports)
   {
      Map<String, Integer> aggregatedPieData = new HashMap<String, Integer>();
      for (EclipseReport eclipseReport : eclipseReports)
      {
         Map<String, Integer> data = getData(getReportModul().getFilteredEvents(eclipseReport));
         for (Entry<String, Integer> entry : data.entrySet())
         {
            Integer count = aggregatedPieData.get(entry.getKey());
            if (count == null)
            {
               count = 0;
            }
            aggregatedPieData.put(entry.getKey(), count + entry.getValue());
         }
      }
      if (!isAggregated())
      {
         for (Entry<String, Integer> entry : aggregatedPieData.entrySet())
         {
            aggregatedPieData.put(entry.getKey(), entry.getValue() / eclipseReports.size());
         }
      }
      return create(getTitle(), aggregatedPieData);
   }

   public static Chart create(String title, Map<String, Integer> data)
   {
      ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();
      try
      {
         cwoaPie.setType("Pie Chart");
         cwoaPie.setSubType("Standard Pie Chart");

         cwoaPie.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
         cwoaPie.getBlock().setBackground(ColorDefinitionImpl.WHITE());
         cwoaPie.getTitle().getLabel().getCaption().setValue(title);

         Legend lg = cwoaPie.getLegend();
         LineAttributes lia = lg.getOutline();
         lg.getText().getFont().setSize(10);
         lg.setWrappingSize(225);

         lia.setStyle(LineStyle.SOLID_LITERAL);
         lg.getInsets().set(10, 5, 0, 0);
         lg.getOutline().setVisible(false);
         lg.setAnchor(Anchor.NORTH_LITERAL);

         // CREATE THE CATEGORY SERIES
         Series seCategory = SeriesImpl.create();

         // CREATE THE PRIMARY DATASET
         PieSeries ls = (PieSeries)PieSeriesImpl.create();
         ls.setSliceOutline(ColorDefinitionImpl.CREAM());
         ls.getLabel().setVisible(true);

         ArrayList<String> legendList = new ArrayList<String>();
         ArrayList<Integer> valueList = new ArrayList<Integer>();

         for (Entry<String, Integer> entry : data.entrySet())
         {
            legendList.add(entry.getKey());
            valueList.add(entry.getValue());
         }

         TextDataSet categoryValues = TextDataSetImpl.create(legendList);
         NumberDataSet orthogonalValues = NumberDataSetImpl.create(valueList);

         Series categorySeries = SeriesImpl.create();
         categorySeries.setDataSet(categoryValues);

         Series lineSeries = LineSeriesImpl.create();
         lineSeries.setDataSet(orthogonalValues);

         ls.setDataSet(orthogonalValues);

         SeriesDefinition sdX = SeriesDefinitionImpl.create();
         sdX.getSeriesPalette().shift(0); // SET THE COLORS IN THE PALETTE
         sdX.getSeries().add(categorySeries);

         SeriesDefinition sdY = SeriesDefinitionImpl.create();
         sdY.getSeriesPalette().shift(1); // SET THE COLORS IN THE PALETTE
         sdY.setQuery(QueryImpl.create(""));
         sdX.getSeriesDefinitions().add(sdY);
         sdX.getSeries().add(categorySeries);

         sdX.getSeries().add(seCategory);
         sdY.getSeries().add(ls);

         cwoaPie.getSeriesDefinitions().add(sdX);
      }
      catch (Exception e)
      {
      }
      return cwoaPie;
   }
}