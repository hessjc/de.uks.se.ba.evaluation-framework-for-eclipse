package de.uks.ef.eclipse.core.ui.utils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

public final class EclipsePartHelper
{
   public static final String DE_UKS_EF_ECLIPSE_VIEW_EVALUATION_PART = "de.uks.ef.eclipse.evaluation.part";
   public static final String DE_UKS_EF_ECLIPSE_VIEW_REPORT_PART = "de.uks.ef.eclipse.report.part";
   public static final String DE_UKS_EF_ECLIPSE_VIEW_TRACKING_PART = "de.uks.ef.eclipse.tracking.part";

   public static List<String> getConstants()
   {
      Field[] interfaceFields = EclipsePartHelper.class.getFields();
      List<String> constants = new LinkedList<String>();
      for (Field f : interfaceFields)
      {
         constants.add(f.getName());
      }
      return constants;
   }

   public static void refreshPart(UISynchronize sync, final EPartService partService, EModelService modelService, MApplication application)
   {

      List<MPart> parts = modelService.findElements(application, null, MPart.class, null);

      for (final MPart part : parts)
      {
         if (part.getElementId().equals(EclipsePartHelper.DE_UKS_EF_ECLIPSE_VIEW_EVALUATION_PART))
         {
            sync.syncExec(new Runnable() {
               @Override
               public void run()
               {
                  try
                  {
                     partService.hidePart(part, true);
                     partService.showPart(part, PartState.ACTIVATE);
                  }
                  catch (Exception e)
                  {

                  }
               };
            });
         }
      }
   }
}