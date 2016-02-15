package de.uks.ef.eclipse.core.ui.listener;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.uks.ef.eclipse.core.configuration.EvaluationService;

public class EvaluationStartListener implements Listener
{
   private EvaluationService evaluationService;

   public EvaluationStartListener(EvaluationService evaluationService)
   {
      this.evaluationService = evaluationService;
   }

   @Override
   public void handleEvent(Event event)
   {
      evaluationService.startEvaluation();
   }
}