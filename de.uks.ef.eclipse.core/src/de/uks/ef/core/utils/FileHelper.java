package de.uks.ef.core.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

public final class FileHelper
{
   public final static IWorkspace WORKSPACE = ResourcesPlugin.getWorkspace();

   public final static String REPORT_DIRECTORY = WORKSPACE.getRoot().getLocation().toString() + "/.metadata/.reports/"; //$NON-NLS-1$

   @SuppressWarnings("finally")
   public static String readFile(String file)
         throws IOException
   {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String everything = "";
      try
      {
         StringBuilder sb = new StringBuilder();
         String line = br.readLine();

         while (line != null)
         {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
         }
         everything = sb.toString();
      }
      finally
      {
         br.close();
         return everything;
      }
   }
}