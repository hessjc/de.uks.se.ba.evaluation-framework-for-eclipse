package de.uks.ef.eclipse.tracking.ui.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.uks.ef.core.model.TrackingEvent;
import de.uks.ef.core.model.TrackingManager;
import de.uks.ef.eclipse.tracking.configuration.TrackingService;

public class TrackingPart
{
   @PostConstruct
   public void createComposite(Composite parent, TrackingService trackingService)
   {
      parent.setLayout(new FillLayout(SWT.HORIZONTAL));

      final ListViewer listViewer = new ListViewer(parent, SWT.V_SCROLL);

      listViewer.setLabelProvider(new LabelProvider() {
         @Override
         public String getText(Object element)
         {
            TrackingEvent trackingEvent = (TrackingEvent)element;
            return trackingEvent.getEncoding();
         }
      });

      listViewer.setContentProvider(new IStructuredContentProvider() {
         @Override
         protected Object clone() throws CloneNotSupportedException
         {
            return super.clone();
         }

         @Override
         public void dispose()
         {

         }

         @Override
         public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
         {

         }

         @Override
         public Object[] getElements(Object inputElement)
         {
            TrackingManager trackingManager = (TrackingManager)inputElement;
            List<TrackingEvent> events = new ArrayList<TrackingEvent>();

            for (Entry<String, List<TrackingEvent>> modul : trackingManager.getTrackingEvent().entrySet())
            {
               events.addAll(modul.getValue());
            }
            return events.toArray();
         }
      });

      listViewer.setInput(trackingService.getTrackingManager());

      trackingService.getTrackingManager().addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent evt)
         {
            Display.getDefault().asyncExec(new Runnable() {
               public void run()
               {
                  try
                  {
                     listViewer.refresh();
                  }
                  catch (Exception e)
                  {
                  }
               }
            });
         }
      });

      parent.layout();
   }
}