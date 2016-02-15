package de.uks.ef.eclipse.core.ui.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;

import de.uks.ef.eclipse.core.ui.utils.EclipsePartHelper;

public class OpenReportViewHandler extends AbstractHandler
{

   @Execute
   public void execute(UISynchronize sync, final MMenuItem menuItem)
   {
      placeholder = findPlaceholder(EclipsePartHelper.DE_UKS_EF_ECLIPSE_VIEW_REPORT_PART);

      if (placeholder == null)
      {
         sync.syncExec(new Runnable() {
            @Override
            public void run()
            {
               placeholder = partService
                     .createSharedPart(EclipsePartHelper.DE_UKS_EF_ECLIPSE_VIEW_REPORT_PART);
            }
         });
      }
      refreshPart();
   }
}