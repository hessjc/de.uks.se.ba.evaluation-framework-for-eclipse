package de.uks.ef.eclipse.core.ui.menus;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;

import de.uks.ef.core.model.EvaluationConfiguration;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.model.EclipseEvaluationManager;

public class EvaluationCreationItem
{
   @SuppressWarnings("unused")
   @Inject
   private EvaluationService evaluationService;

   @Inject
   private EclipseEvaluationManager evaluationManager;

   @AboutToShow
   public void aboutToShow(final List<MMenuElement> items)
   {
      for (EvaluationConfiguration evaluationConfiguration : evaluationManager.getEvaluationConfiguration())
      {
         MDirectMenuItem dynamicItem = MMenuFactory.INSTANCE
               .createDirectMenuItem();
         dynamicItem.setLabel(evaluationConfiguration.getEvaluation()
               .getName());
         dynamicItem.setElementId(evaluationConfiguration.getEvaluation().getId());
         dynamicItem
               .setContributorURI("platform:/plugin/de.uks.ef.eclipse.core.ui");
         dynamicItem
               .setContributionURI(
               "bundleclass://de.uks.ef.eclipse.core.ui/de.uks.ef.eclipse.core.ui.handler.EclipseMenuHandler");
         items.add(dynamicItem);
      }
   }

   @AboutToHide
   public void aboutToHide()
   {

   }
}