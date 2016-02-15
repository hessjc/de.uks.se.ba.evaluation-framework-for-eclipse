package de.uks.ef.eclipse.core.model;

import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import de.uks.ef.core.model.EvaluationStep;
import de.uks.ef.core.model.EvaluationSubStep;
import de.uks.ef.core.model.QuestionnaireEntry;

public class EclipseEvaluationSubStep extends EclipseElement implements EvaluationSubStep
{
   private String id = "";
   private EvaluationStep evaluationStep;
   private String name;
   private String description;
   private final List<QuestionnaireEntry> questionnaireEntries = new LinkedList<QuestionnaireEntry>();

   public EclipseEvaluationSubStep()
   {

   }

   public EclipseEvaluationSubStep(final String id)
   {
      this.id = id;
   }

   @Override
   public String getId()
   {
      if (id.equals(""))
      {
         return this.toString();
      }
      else
      {
         return id;
      }
   }

   @Override
   public void setId(final String id)
   {
      this.id = id;
   }

   @Override
   public String getName()
   {
      return this.name;
   }

   @Override
   public void setName(final String value)
   {
      this.name = value;
   }

   @Override
   public String getDescription()
   {
      return this.description;
   }

   @Override
   public void setDescription(final String value)
   {
      this.description = value;
   }

   @Override
   public EvaluationStep getEvaluationStep()
   {
      return this.evaluationStep;
   }

   @Override
   public void setEvaluationStep(final EvaluationStep value)
   {
      this.evaluationStep = value;
   }

   @Override
   public void addQuestionnaireEntry(final QuestionnaireEntry questionnaireEntry)
   {
      questionnaireEntries.add(questionnaireEntry);
   }

   @Override
   public List<QuestionnaireEntry> getQuestionnaireEntries()
   {
      return questionnaireEntries;
   }

   @Override
   public void addListener(final PropertyChangeListener propertyChangeListener)
   {
      super.addListener(propertyChangeListener);
      for (final QuestionnaireEntry questionnaireEntry : getQuestionnaireEntries())
      {
         questionnaireEntry.addListener(propertyChangeListener);
      }
   }

   @Override
   public void reset()
   {
      for (QuestionnaireEntry questionnaireEntry : getQuestionnaireEntries())
      {
         questionnaireEntry.reset();
      }
   }
}