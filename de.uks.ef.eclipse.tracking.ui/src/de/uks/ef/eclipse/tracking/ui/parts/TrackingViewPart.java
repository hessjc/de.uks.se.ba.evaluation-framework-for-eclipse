package de.uks.ef.eclipse.tracking.ui.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import de.uks.ef.eclipse.tracking.configuration.TrackingService;
import de.uks.ef.eclipse.tracking.listener.TrackingStateListener;

public class TrackingViewPart implements TrackingStateListener
{
   private Composite composite;

   @Inject
   private TrackingService trackingService;

   @PostConstruct
   public void createComposite(Composite parent)
   {
      composite = parent;
      trackingService.registerTrackingStateListener(this);
      trackingShown();
   }

   @Override
   public void trackingShown()
   {
      Display.getDefault().asyncExec(new Runnable() {
         
         @Override
         public void run()
         {
            clearComposite();
            TrackingPart trackingPart = new TrackingPart();
            trackingPart.createComposite(composite, trackingService);
         }
      });
   }

   private void clearComposite()
   {
      if (!composite.isDisposed())
      {
         for (Control control : composite.getChildren())
         {
            control.dispose();
         }
      }
   }

   public Composite getComposite()
   {
      return composite;
   }

   @PreDestroy
   private void cleanup()
   {
      trackingService.unregisterTrackingStateListener(this);
   }
}