package de.uks.ef.core.utils;

import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.extensions.Preference;
import org.osgi.service.prefs.BackingStoreException;

@SuppressWarnings("restriction")
@Creatable
public final class PreferenceHelper
{
   @Inject
   @Preference(nodePath = "de.uks.ef.evaluation")
   private IEclipsePreferences pref;

   public String userID()
   {
      if (pref.get("user.id", "not set").equals("not set"))
      {
         pref.put("user.id", UUID.randomUUID().toString());

         try
         {
            pref.flush();
         }
         catch (BackingStoreException e)
         {
            e.printStackTrace();
         }

      }
      return pref.get("user.id", "is set");
   }
}
