/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.tools.managementmodule.services;

import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.util.Calendar;
import java.util.Date;

/**
 * Class containing Util methods
 * @author FALSQUELLE-E
 *
 */
public class Util {
   
   private static final float EPSILON = 0.0001f;

   /** 
    * private constructor
    */
   private Util(){
     
   }
   
   /**
    * Return true if and only if value2 - value1 < 10^-4
    * 
    * @param value1
    *            first parameter to compare
    * @param value2
    *            second parameter to compare
    * @return true if value2 - value1 < 10^-4
    */
   public static boolean isEqual(float value1, float value2) {
      return Math.abs(value2 - value1) < EPSILON;
   }
   
   /**
    * returns the last Monday before a date with hours, minutes, seconds and
    * milliseconds cleared
    * 
    * @param date
    * @return a Date
    */
   public static Date getLastMonday(Date date) {
      Calendar previousMonday = Calendar.getInstance();
      previousMonday.setTime(date);
      while (previousMonday.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
         previousMonday.add(Calendar.DATE, -1);
      }
      clearTime(previousMonday);
      return previousMonday.getTime();
   }

   /**
    * Clear hours, minutes, seconds and milliseconds Fields of a calendar.
    *
    * @param c
    */
   public static void clearTime(Calendar c)
   {
      c.set(Calendar.HOUR_OF_DAY, 0);
      c.set(Calendar.MINUTE, 0);
      c.set(Calendar.SECOND, 0);
      c.set(Calendar.MILLISECOND, 0);
   }

   /**
    * Clear hours, minutes, seconds and milliseconds of a date.
    *
    * @param date
    * @return the previous date without hours, minutes, seconds and
    *         milliseconds
    */
   public static Date clearTime(Date date) {
      Calendar c = Calendar.getInstance();
      c.setTime(date);
      clearTime(c);
      return c.getTime();
   }

   /**
    * returns the following Sunday a date with hours, minutes, seconds and
    * milliseconds cleared
    * 
    * @param date
    * @return a Date
    */
   public static Date getFollowingSunday(Date date) {
      Calendar nextSunday = Calendar.getInstance();
      nextSunday.setTime(date);
      while (nextSunday.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
         nextSunday.add(Calendar.DATE, 1);
      }
      clearTime(nextSunday);
      return nextSunday.getTime();
   }

//   Unused...
//   /**
//    * Return a string which has numberDecimal decimals numbers
//    * Use for CSV export
//    * @param value
//    * @return String
//    */
//   public static Float floatFormat(Float value, int numberDecimal) {
//      double multidivicatoseur = Math.pow(10, numberDecimal);
//      double d = Math.floor(value * multidivicatoseur + 0.5);
//      return new Float(((float) (d)) / multidivicatoseur);
//   }

   
   /**
    * Generate Metadata for reports
    * @param iterationId
    * @param discipline
    * @param iteration
    * @param lot
    * @param lotParent
    * @return
    * @throws ManagementModuleException
    */
   public static String generateMetadatas(Discipline discipline, Iteration iteration, boolean showIterations,
         Lot lot, boolean showLot, Lot lotParent, boolean showLotParent) throws ManagementModuleException {
      
      final StringBuilder sb = new StringBuilder();
      
      if (showLot){
         if (lot == null){
            sb.append("<b>Lot</b> : Tous<br />");
         }else{
            sb.append("<b>Lot</b> : ").append(lot.getName()).append("<br />");
         }
      }
      if (showLotParent){
         if (lotParent == null){
            sb.append("<b>Lot parent</b> : Tous<br />");
         }else{
            sb.append("<b>Lot parent</b> : ").append(lotParent.getName()).append("<br />");
         }
      }
      if (showIterations) {
         if (iteration != null){
            sb.append("<b>Iteration</b> : ").append(iteration.getLabel()).append("<br />");
         }
      }else{
         sb.append("<b>Iterations</b> : Toutes <br />");
      }

      if (discipline == null){
         sb.append("<b>Disciplines</b> : Toutes");
      }else{
         sb.append("<b>Discipline</b> : ").append(discipline.getName());
      }
      
      return sb.toString();
   }
}
