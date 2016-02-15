package de.uks.ef.eclipse.core.ui.handler;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import de.uks.ef.eclipse.core.configuration.EvaluationService;

public abstract class AbstractHandler
{
   @Inject
   protected EPartService partService;

   @Inject
   protected EvaluationService evaluationService;

   @Inject
   private EModelService modelService;

   @Inject
   private MApplication application;

   public MPlaceholder placeholder;

   protected MPlaceholder findPlaceholder(String placeholderId)
   {
      List<MPlaceholder> placeholders = modelService.findElements(application, null,
            MPlaceholder.class, null);
      for (MPlaceholder placeholder : placeholders)
      {
         if (placeholder.getElementId().equals(placeholderId)) return placeholder;
      }
      return null;
   }

   protected void refreshPart()
   {
      MPart part = (MPart)placeholder.getRef();
      if (!partService.isPartVisible(part))
      {
         partService.showPart(part, PartState.ACTIVATE);
      }
   }
}
