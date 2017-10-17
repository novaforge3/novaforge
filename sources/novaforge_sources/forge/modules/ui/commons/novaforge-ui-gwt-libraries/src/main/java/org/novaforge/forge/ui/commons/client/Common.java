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
/**
 * 
 */
package org.novaforge.forge.ui.commons.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import org.gwtwidgets.client.util.SimpleDateFormat;
import org.novaforge.forge.ui.commons.client.properties.CommonsMessages;
import org.novaforge.forge.ui.commons.client.resources.CommonsResources;

/**
 * @author BILET-JC
 */
public final class Common
{
   /**
    * Define empty text ""
    */
   public static final String            EMPTY_STRING = "";
   /**
    * Define space text " "
    */
   public static final String            SPACE_STRING = " ";
   /**
    * Define application uuid parameter
    */
   public static final String            UUID_PARAMETER = "uuid";
   private static final CommonsMessages  messages  = GWT.create(CommonsMessages.class);
   private static final CommonsResources resources = GWT.create(CommonsResources.class);
   private static final SimpleDateFormat dateFormat   = new SimpleDateFormat("yyyy-MM-dd HH:mm");

   /**
    * Validate a String
    * 
    * @param toBeTest
    * @return true if the String is valid
    */
   public static boolean validateStringNotNull(final String toBeTest)
   {
      boolean result = true;

      if ((toBeTest == null) || toBeTest.isEmpty() || "".equalsIgnoreCase(toBeTest)
            || " ".equalsIgnoreCase(toBeTest))
      {
         result = false;
      }
      return result;
   }

   /**
    * @return the dateFormat
    */
   public static SimpleDateFormat getDateFormat()
   {
      return dateFormat;
   }

   /**
    * Returns commons labels of all applications
    * 
    * @return the generic
    */
   public static CommonsMessages getMessages()
   {
      return messages;
   }

   /**
    * Returns commons labels of all applications
    * 
    * @return the generic
    */
   public static CommonsResources getResources()
   {
      return resources;
   }

   public static boolean isDebugMode()
   {
      boolean isDebug = false;
      final String parameter = Window.Location.getParameter("gwt.codesvr");
      if (parameter != null)
      {
         isDebug = true;
      }
      return isDebug;
   }

}
