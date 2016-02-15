package de.uks.ef.core.model;

import java.util.HashMap;
import java.util.List;

import de.uks.ef.core.databinding.PropertyChangeModel;

public interface EvaluationLog extends PropertyChangeModel
{
   /**
    * <pre>
    *           0..1     0..*
    * EvaluationLog ------------------------- EvaluationLogEvent
    *           evaluationLog        &lt;       evaluationLogEvent
    * </pre>
    */
   public HashMap<String, List<EvaluationLogEvent>> getEvaluationLogEvent();

   public void addEvaluationLogEvent(EvaluationLogEvent evaluationLogEvent);
}