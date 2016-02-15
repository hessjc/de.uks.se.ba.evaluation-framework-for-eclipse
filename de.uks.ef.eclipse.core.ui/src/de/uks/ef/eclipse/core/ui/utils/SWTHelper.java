package de.uks.ef.eclipse.core.ui.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;

public class SWTHelper
{
   public static void styledDescriptionText(StyledText styledText, String text)
   {
      styledText.setText("");
      Collection<StyleRange> activeRanges = new ArrayList<StyleRange>();

      String[] splitted = text.split(";");
      int offset = 0;
      String newFullLine = "";
      for (int i = 0; i < splitted.length; i++)
      {
         String line = splitted[i];
         if (!line.isEmpty())
         {
            while (line.contains("<b>"))
            {
               StyleRange styleRange = new StyleRange();
               int start = line.indexOf("<b>");
               int end = line.indexOf("</b>");
               line = line.replaceFirst("<b>", "");
               line = line.replaceFirst("</b>", "");
               styleRange.start = offset + start;
               styleRange.length = end - start - 3;
               styleRange.fontStyle = SWT.BOLD;
               activeRanges.add(styleRange);
            }

            newFullLine = line;
            newFullLine += System.lineSeparator();

         }
         else
         {
            newFullLine = System.lineSeparator();
         }

         offset += newFullLine.length();
         styledText.append(newFullLine);
      }
      for (StyleRange styleRange : activeRanges)
      {
         styledText.setStyleRange(styleRange);
      }
   }

   public static void styledText(StyledText styledText, String text)
   {
      String[] splitted = text.split(";");
      styledText.setText("");
      for (int i = 0; i < splitted.length; i++)
      {
         styledText.append(splitted[i]);
         styledText.append(System.lineSeparator());
      }
   }
}