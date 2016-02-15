package de.uks.ef.eclipse.report.ui.parts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.Report;
import de.uks.ef.core.model.ReportModul;
import de.uks.ef.core.model.TrackingModul;
import de.uks.ef.eclipse.core.model.EclipseReport;
import de.uks.ef.eclipse.report.configuration.ReportService;
import de.uks.ef.eclipse.report.core.EclipseMultiBarChartReport;
import de.uks.ef.eclipse.report.core.EclipsePieChartReport;
import de.uks.ef.eclipse.report.model.EclipseChartReport;

@SuppressWarnings("restriction")
public class ReportOverviewPart
{
   private static final String FILTER_HOOKS = "Filter Hooks";

   private ComboViewer comboEvaluationViewer;
   private ComboViewer comboUserViewer;
   private ListViewer reportList;
   private Composite chartComposite;

   private Button aggregateCheckbox;

   public void createComposite(Composite parent, final ReportService reportService, Logger LOGGER)
   {
      parent.setLayout(new FillLayout(SWT.VERTICAL));

      Composite compositeMain = new Composite(parent, SWT.NONE);
      GridLayout gl_compositeMain = new GridLayout(5, false);
      compositeMain.setLayout(gl_compositeMain);

      initEvaluationCombo(compositeMain, reportService);

      final Combo combo = new Combo(compositeMain, SWT.NONE);
      combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
      comboUserViewer = new ComboViewer(combo);

      comboUserViewer.setContentProvider(new IStructuredContentProvider() {

         @Override
         public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
         {

         }

         @Override
         public void dispose()
         {
         }

         @Override
         public Object[] getElements(Object inputElement)
         {
            Collection<EclipseReport> reports = (Collection<EclipseReport>)inputElement;
            return reports.toArray();
         }
      });

      comboUserViewer.setLabelProvider(new LabelProvider() {
         @Override
         public String getText(Object element)
         {
            if (element instanceof EclipseReport)
            {
               String userid = ((EclipseReport)element).getUserid();
               if (userid.isEmpty())
               {
                  return element.toString();
               }
               return userid;
            }
            return super.getText(element);
         }
      });

      Button btnNewButton = new Button(compositeMain, SWT.NONE);
      btnNewButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
      btnNewButton.setText("Generate");

      aggregateCheckbox = new Button(compositeMain, SWT.CHECK);
      aggregateCheckbox.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
      aggregateCheckbox.setText("Aggregate all");

      final Composite leftPane = new Composite(compositeMain, SWT.NONE);
      leftPane.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2));
      leftPane.setLayout(new GridLayout());

      final Tree filterTree = new Tree(leftPane, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
      try
      {
         filterTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
         filterTree.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event)
            {
               if (event.detail == SWT.CHECK && event.item != null)
               {
                  HashSet<String> filters = reportService.getReportHelper().getFilters();
                  if (reportService.isInFilter(event.item.getData("id").toString()))
                  {
                     filters.remove(event.item.getData("id").toString());
                     TreeItem treeItem = (TreeItem)event.item;
                     for (TreeItem item : treeItem.getItems())
                     {
                        item.setChecked(false);
                        filters.remove(item.getData("id").toString());
                     }
                  }
                  else
                  {
                     filters.add(event.item.getData("id").toString());
                     TreeItem treeItem = (TreeItem)event.item;
                     for (TreeItem item : treeItem.getItems())
                     {
                        item.setChecked(true);
                        filters.add(item.getData("id").toString());
                     }
                  }
                  updateReports();
               }
            }
         });
      }
      catch (Exception e)
      {

      }

      chartComposite = new Composite(compositeMain, SWT.NONE);
      chartComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
      chartComposite.setLayout(new FillLayout());

      reportList = new ListViewer(leftPane);
      reportList.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
      reportList.setContentProvider(ArrayContentProvider.getInstance());
      reportList.setLabelProvider(new LabelProvider() {
         @Override
         public String getText(Object element)
         {
            if (element instanceof Report)
            {
               return ((Report)element).getTitle();
            }
            return super.getText(element);
         }
      });

      reportList.addSelectionChangedListener(new ISelectionChangedListener() {

         @Override
         public void selectionChanged(SelectionChangedEvent event)
         {
            if (event.getSelection() instanceof IStructuredSelection)
            {
               IStructuredSelection selection = (IStructuredSelection)event.getSelection();
               Report report = (Report)selection.getFirstElement();

               if (aggregateCheckbox.getSelection())
               {
                  Collection<EclipseReport> reportsForTitle = new ArrayList<EclipseReport>();
                  Object[] elements = ((IStructuredContentProvider)comboUserViewer.getContentProvider())
                        .getElements(comboUserViewer.getInput());
                  for (Object object : elements)
                  {
                     reportsForTitle.add((EclipseReport)object);
                  }
                  drawChart(report, reportsForTitle);
               }
               else
               {

                  EclipseReport eclipseReport = (EclipseReport)comboUserViewer.getStructuredSelection()
                        .getFirstElement();
                  drawChart(report, Collections.singleton(eclipseReport));
               }

            }

         }
      });

      comboEvaluationViewer.addSelectionChangedListener(new ISelectionChangedListener() {

         @Override
         public void selectionChanged(SelectionChangedEvent event)
         {
            ISelection selection = event.getSelection();
            if (selection instanceof IStructuredSelection)
            {
               Evaluation evaluation = (Evaluation)((IStructuredSelection)selection).getFirstElement();
               if (evaluation != null)
               {
                  reportService.getReportHelper().initFilters(evaluation);

                  comboUserViewer.setInput(reportService.getReports(evaluation));
                  comboUserViewer.refresh();
               }
               fillFilters(filterTree, reportService, evaluation);
            }
         }
      });

      comboUserViewer.addSelectionChangedListener(new ISelectionChangedListener() {

         @Override
         public void selectionChanged(SelectionChangedEvent event)
         {
            ISelection selection = event.getSelection();
            if (selection instanceof IStructuredSelection)
            {
               EclipseReport report = (EclipseReport)((IStructuredSelection)selection).getFirstElement();
            }
         }
      });

      btnNewButton.addSelectionListener(new SelectionAdapter() {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            updateReports();
         }
      });

      parent.layout();
   }

   private void initEvaluationCombo(final Composite parent, final ReportService reportService)
   {
      final Combo comboEvaluation = new Combo(parent, SWT.NONE);
      comboEvaluation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
      comboEvaluationViewer = new ComboViewer(comboEvaluation);

      comboEvaluationViewer.setContentProvider(new IStructuredContentProvider() {

         @Override
         public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
         {

         }

         @Override
         public void dispose()
         {
         }

         @Override
         public Object[] getElements(Object inputElement)
         {
            return ((ReportService)reportService).listEvaluations().keySet().toArray();
         }
      });

      comboEvaluationViewer.setLabelProvider(new LabelProvider() {
         @Override
         public String getText(Object element)
         {
            if (element instanceof Evaluation)
            {
               return ((Evaluation)element).getId();
            }
            return super.getText(element);
         }
      });

      comboEvaluationViewer.setInput(reportService);
   }

   private void fillFilters(final Tree filterTree, final ReportService reportService, final Evaluation evaluation)
   {
      TreeItem root = new TreeItem(filterTree, SWT.NONE);
      root.setText(evaluation.getName());
      root.setData("id", evaluation.getId());
      root.setChecked(reportService.isInFilter(evaluation.getId()));

      TreeItem hook = new TreeItem(filterTree, SWT.NONE);
      hook.setText(FILTER_HOOKS);
      hook.setData("id", "hook");
      hook.setExpanded(true);
      hook.setChecked(reportService.isInFilter(hook.getData("id").toString()));

      for (EvaluationStep evaluationStep : evaluation.getEvaluationStep().values())
      {
         TreeItem item = new TreeItem(root, SWT.NONE);
         item.setText(evaluationStep.getName());
         item.setData("id", evaluationStep.getId());
         item.setExpanded(true);
         item.setChecked(reportService.isInFilter(evaluationStep.getId()));
      }

      root.setExpanded(true);
   }

   public class MultiBarChartReportPaintListener extends ReportPaintListener<ChartWithAxes>
   {

      public MultiBarChartReportPaintListener(ScrolledComposite scrollable, IDeviceRenderer renderer,
            ChartWithAxes chart)
      {
         super(scrollable, renderer, chart);
      }

      @Override
      protected Bounds getBounds(Composite composite)
      {
         Series[] series = chart.getSeries(0);
         Series series2 = series[0];
         Object values = series2.getDataSet().getValues();
         Rectangle rectangle = composite.getClientArea();
         int width = 0;
         if (values instanceof Collection<?>)
         {
            int size = ((Collection)values).size();
            width = 700 * size;
         }
         if (rectangle.width > width)
         {
            width = rectangle.width;
         }
         return BoundsImpl.create(rectangle.x, rectangle.y, width, rectangle.height);
      }
   }

   public class PieChartReportPaintListener extends ReportPaintListener<Chart>
   {

      public PieChartReportPaintListener(ScrolledComposite scrollable, IDeviceRenderer renderer, Chart chart)
      {
         super(scrollable, renderer, chart);
      }

   }

   public abstract class ReportPaintListener<C extends Chart> implements PaintListener
   {
      private IDeviceRenderer renderer;
      protected C chart;
      private ScrolledComposite sc;

      public ReportPaintListener(ScrolledComposite scrollable, final IDeviceRenderer renderer,
            final C chart)
      {
         this.sc = scrollable;
         this.renderer = renderer;
         this.chart = chart;
      }

      public void paintControl(PaintEvent event)
      {
         renderer.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, event.gc);
         Composite scrollable = (Composite)event.getSource();
         Bounds bounds = getBounds(scrollable);
         sc.setMinSize((int)bounds.getWidth(), (int)bounds.getHeight());
         bounds.scale(72d / renderer.getDisplayServer().getDpiResolution());
         Generator generator = Generator.instance();

         try
         {
            generator.render(renderer, generator.build(renderer.getDisplayServer(), chart, bounds, null, null));
         }
         catch (ChartException e)
         {

         }
      }

      protected Bounds getBounds(Composite composite)
      {
         Rectangle rectangle = composite.getBounds();
         return BoundsImpl.create(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
      }
   }

   public void updateReports()
   {
      Collection<Report> allReports = new ArrayList<Report>();
      EclipseReport report = (EclipseReport)comboUserViewer.getStructuredSelection().getFirstElement();
      for (TrackingModul trackingModul : report.getTrackingModuls())
      {
         for (ReportModul reportModul : trackingModul.getReportModul())
         {
            allReports.addAll(reportModul.getReports(report.getEvaluation()));
         }
      }
      reportList.setInput(allReports);
   }

   private void drawChart(Report rep, Collection<EclipseReport> eclipseReport)
   {
      try
      {
         for (Control control : chartComposite.getChildren())
         {
            control.dispose();
         }
         final PluginSettings pluginSettings = PluginSettings.instance();
         final IDeviceRenderer renderer = pluginSettings.getDevice("dv.SWT");

         ScrolledComposite scrolledComposite = new ScrolledComposite(chartComposite, SWT.H_SCROLL);
         scrolledComposite.setExpandHorizontal(true);
         scrolledComposite.setExpandVertical(true);

         EclipseChartReport rp = (EclipseChartReport)rep;

         Canvas cCenter = new Canvas(scrolledComposite, SWT.BORDER);
         cCenter.setSize(100, 100);
         scrolledComposite.setContent(cCenter);
         if (rp instanceof EclipseMultiBarChartReport)
         {
            cCenter.addPaintListener(
                  new MultiBarChartReportPaintListener(scrolledComposite, renderer,
                        (ChartWithAxes)rp.getChart(eclipseReport)));
         }
         else if (rp instanceof EclipsePieChartReport)
         {
            cCenter
                  .addPaintListener(
                        new PieChartReportPaintListener(scrolledComposite, renderer, rp.getChart(eclipseReport)));
         }
         chartComposite.layout();
      }
      catch (ChartException e)
      {
         e.printStackTrace();
      }
   }
}