package de.uks.ef.eclipse.core.ui.handler;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPageService;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;

import de.uks.ef.core.model.Evaluation;
import de.uks.ef.eclipse.core.ui.utils.EclipsePartHelper;

public class EclipseMenuHandler extends AbstractHandler
{
   private static final String ERROR_STOP_EVALUATION_TITLE = "first stop evaluation";

   private static final String ERROR_STOP_EVALUATION_TEXT = "Before you can start another evaluation you have to stop the current running evaluation.";

   @PostConstruct
   private void postCreation(final UISynchronize sync, final IPageService pageService)
   {
      pageService.addPerspectiveListener(new IPerspectiveListener() {

         @Override
         public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId)
         {
         }

         @Override
         public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective)
         {
            findOrOpenViewPart(sync);
            refreshPart();
         }
      });
   }

   @Execute
   public void execute(UISynchronize sync, final MMenuItem menuItem, MWindow mWindow)
   {
      findOrOpenViewPart(sync);
      Evaluation newEvaluation = evaluationService.findEvaluation(menuItem.getElementId());
      if (!evaluationService.evaluationIsStopped())
      {
         if (newEvaluation == evaluationService.getCurrentRunningEvaluation())
         {
            refreshPart();
         }
         else
         {
            MessageDialog.openInformation(new Shell(),
                  ERROR_STOP_EVALUATION_TITLE, ERROR_STOP_EVALUATION_TEXT);
         }
      }
      else
      {
         evaluationService.setCurrentRunningEvaluation(newEvaluation);

         refreshPart();
      }
   }

   private void findOrOpenViewPart(UISynchronize sync)
   {
      placeholder = findPlaceholder(EclipsePartHelper.DE_UKS_EF_ECLIPSE_VIEW_EVALUATION_PART);

      if (placeholder == null)
      {
         sync.syncExec(new Runnable() {
            @Override
            public void run()
            {
               placeholder = partService
                     .createSharedPart(EclipsePartHelper.DE_UKS_EF_ECLIPSE_VIEW_EVALUATION_PART);
            };
         });
      }
   }
}