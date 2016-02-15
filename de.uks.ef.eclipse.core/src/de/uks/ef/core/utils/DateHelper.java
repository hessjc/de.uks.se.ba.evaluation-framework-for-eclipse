package de.uks.ef.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateHelper
{
   public static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy/HH:mm:ss");

   public static String formatDate(Date date)
   {
      return formatter.format(date);
   }

   public static Date formatStringToDate(String date)
   {
      try
      {
         return formatter.parse(date);
      }
      catch (ParseException e)
      {
         e.printStackTrace();
         return null;
      }
   }

   public static String getDate()
   {
      return formatter.format(new Date());
   }
}
