package de.uks.ef.eclipse.core.ui.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.uks.ef.core.model.EvaluationLog;
import de.uks.ef.core.model.EvaluationLogEvent;
import de.uks.ef.eclipse.core.configuration.EvaluationService;
import de.uks.ef.eclipse.core.model.EclipseEvaluationLog;

public class EvaluationLogView
{
   private ListViewer console;

   @PostConstruct
   public void createComposite(final Composite parent, final EvaluationService evaluationService,
         final EclipseEvaluationLog evaluationLog)
   {
      console = new ListViewer(parent, SWT.V_SCROLL);

      console.setLabelProvider(new LabelProvider() {
         @Override
         public String getText(final Object element)
         {
            final EvaluationLogEvent evaluationLogEvent = (EvaluationLogEvent)element;
            return evaluationLogEvent.getDescription();
         }
      });

      console.setContentProvider(new IStructuredContentProvider() {
         @Override
         protected Object clone() throws CloneNotSupportedException
         {
            return super.clone();
         }

         @Override
         public void dispose()
         {

         }

         @Override
         public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
         {

         }

         @Override
         public Object[] getElements(final Object inputElement)
         {
            final EvaluationLog evaluationLog = (EvaluationLog)inputElement;
            return evaluationLog.getEvaluationLogEvent()
                  .get(evaluationService.getCurrentRunningEvaluation().toString()).toArray();
         }
      });

      console.setInput(evaluationLog);

      evaluationLog.addPropertyChangeListener(new PropertyChangeListener() {
         @Override
         public void propertyChange(final PropertyChangeEvent evt)
         {
            Display.getDefault().syncExec(new Runnable() {
               @Override
               public void run()
               {
                  try
                  {
                     console.refresh();
                  }
                  catch (final Exception e)
                  {
                  }
                  console.reveal(evt.getNewValue());
               }
            });
         }
      });
   }
}
