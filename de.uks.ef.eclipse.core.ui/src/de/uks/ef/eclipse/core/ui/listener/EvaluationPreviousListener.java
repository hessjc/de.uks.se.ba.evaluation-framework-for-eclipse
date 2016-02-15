package de.uks.ef.eclipse.core.ui.listener;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.uks.ef.eclipse.core.configuration.EvaluationService;

public class EvaluationPreviousListener implements Listener
{
   private EvaluationService evaluationService;

   public EvaluationPreviousListener(EvaluationService evaluationService)
   {
      this.evaluationService = evaluationService;
   }

   @Override
   public void handleEvent(Event event)
   {
      evaluationService.previousEvaluationStep();
   }
}