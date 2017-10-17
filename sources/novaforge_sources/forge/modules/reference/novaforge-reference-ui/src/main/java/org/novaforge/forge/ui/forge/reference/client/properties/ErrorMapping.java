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
package org.novaforge.forge.ui.forge.reference.client.properties;

import com.google.gwt.core.client.GWT;
import org.novaforge.forge.ui.forge.reference.shared.exceptions.ErrorEnumeration;

public class ErrorMapping
{
   private static final ErrorMessage errorMessage = GWT.create(ErrorMessage.class);

	public static String getMessage(final ErrorEnumeration pError)
   {
      String returnMessage = "";
      switch (pError)
      {
         /*
          * FORGE ERRORS
          */
         /*
          * project
          */
         case ERR_VALIDATION_BEAN:
            returnMessage = errorMessage.ERR_VALIDATION_BEAN();
            break;

         case ERR_CHANGE_PROJECT_STATUS_STATUS_ALREADY_CHANGED:
            returnMessage = errorMessage.ERR_CHANGE_PROJECT_STATUS_STATUS_ALREADY_CHANGED();
            break;
            /*
             * Space
             */
         case ERR_CREATE_SPACE_NAME_ALREADY_EXIST:
            returnMessage = errorMessage.ERR_CREATE_SPACE_NAME_ALREADY_EXIST();
            break;
            /*
             * Application
             */
         case ERR_CREATE_APP_NAME_ALREADY_EXIST:
            returnMessage = errorMessage.ERR_CREATE_APP_NAME_ALREADY_EXIST();
            break;
            
         case ERR_CREATE_TEMPLATE_NAME_ALREADY_EXIST:
            returnMessage = errorMessage.ERR_CREATE_TEMPLATE_NAME_ALREADY_EXIST();
            break;
            
         case ERR_CREATE_TEMPLATE_ID_ALREADY_EXIST:
            returnMessage = errorMessage.ERR_CREATE_TEMPLATE_ID_ALREADY_EXIST();
            break;
            
         case TECHNICAL_ERROR:
         default:
            returnMessage = errorMessage.TECHNICAL_ERROR();

            break;
      }
      return returnMessage;
   }

}
