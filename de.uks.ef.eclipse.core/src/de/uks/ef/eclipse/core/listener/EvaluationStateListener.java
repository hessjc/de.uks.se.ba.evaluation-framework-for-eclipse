package de.uks.ef.eclipse.core.listener;

public interface EvaluationStateListener
{
   public void evaluationShown();

   public void evaluationStarted();

   public void evaluationStepped();

   public void evaluationFinished();
}
