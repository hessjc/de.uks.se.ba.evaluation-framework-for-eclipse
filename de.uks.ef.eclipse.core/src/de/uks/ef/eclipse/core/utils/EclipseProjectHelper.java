package de.uks.ef.eclipse.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.osgi.framework.Bundle;

import de.uks.ef.core.utils.FileHelper;

public final class EclipseProjectHelper
{
   public static boolean importZipProject(final String fileName, final String evaluationPluginId,
         final String projectName)
   {
      final IProjectDescription projectDescription = FileHelper.WORKSPACE.newProjectDescription(projectName);
      final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      final IProject project = root.getProject(projectName);

      try
      {
         if (project.exists())
         {
            project.delete(true, true, new NullProgressMonitor());
         }

         final Bundle evaluationBundle = Platform
               .getBundle(evaluationPluginId.substring(0, evaluationPluginId.indexOf("-")));
         final URL projectEntry = evaluationBundle.getEntry(fileName);
         final ZipInputStream zis = new ZipInputStream((InputStream)projectEntry.getContent());
         final File workspaceDir = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile();
         ZipEntry entry;
         while ((entry = zis.getNextEntry()) != null)
         {
            extractEntry(entry, workspaceDir, zis);
         }

         project.create(projectDescription, null);
         project.open(null);

         new Job("Refresh workspace") {

            @Override
            protected IStatus run(IProgressMonitor monitor)
            {
               try
               {
                  project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
                  project.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
               }
               catch (CoreException e)
               {
                  return Status.CANCEL_STATUS;
               }
               return Status.OK_STATUS;
            }
         }.schedule(2000);

         return true;
      }
      catch (final Exception e)
      {
         e.printStackTrace();
         return false;
      }
   }

   private static void extractEntry(final ZipEntry entry, final File outputDirectory, final InputStream is)
         throws IOException
   {
      final File extractedFile = new File(outputDirectory, entry.getName());
      FileOutputStream fos = null;
      try
      {
         if (!extractedFile.exists())
         {
            extractedFile.getParentFile().mkdirs();
            extractedFile.createNewFile();
         }
         fos = new FileOutputStream(extractedFile);
         final byte[] buf = new byte[8192];
         int length;
         while ((length = is.read(buf, 0, buf.length)) >= 0)
         {
            fos.write(buf, 0, length);
         }
      }
      catch (final IOException ioex)
      {
         fos.close();
      }
   }
}