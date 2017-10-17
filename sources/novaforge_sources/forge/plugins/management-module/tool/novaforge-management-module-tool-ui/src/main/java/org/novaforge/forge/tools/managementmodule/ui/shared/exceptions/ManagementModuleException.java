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
package org.novaforge.forge.tools.managementmodule.ui.shared.exceptions;

import java.util.ArrayList;
import java.util.List;


/**
 * @author lamirang
 */
public class ManagementModuleException extends Exception
{

   /**
    * 
    */
   private static final long serialVersionUID = -225413377340406771L;
   private List<ErrorEnumeration> errorsList       = new ArrayList<ErrorEnumeration>();

   public ManagementModuleException()
   {
      // Used for serialization
   }

   public ManagementModuleException(final ErrorEnumeration pErrs)
   {
	   
      super();
      errorsList.add(pErrs);
//      System.out.println(ManagementModuleConstants.ETP_DENOMINATOR);  
   }

   public ManagementModuleException(final List<ErrorEnumeration> pErrs)
   {
      super();
      errorsList = pErrs;
   }

   public ManagementModuleException(final List<ErrorEnumeration> pErrs, final Throwable pCause)
   {
      super(pCause);
      errorsList = pErrs;
   }

   public ManagementModuleException(final ErrorEnumeration pErrs, final Throwable pCause)
   {
      super(pCause);
      errorsList.add(pErrs);
   }

   public ManagementModuleException(final String pMessage, final Throwable pCause)
   {
      super(pMessage, pCause);
   }

   public ManagementModuleException(final List<ErrorEnumeration> pErrs, final String pMessage,
         final Throwable pCause)
   {
      super(pMessage, pCause);
      errorsList = pErrs;
   }

   public ManagementModuleException(final ErrorEnumeration pErrs, final String pMessage,
         final Throwable pCause)
   {
      super(pMessage, pCause);
      errorsList.add(pErrs);
   }

   public ManagementModuleException(final List<ErrorEnumeration> pErrs, final String pMessage)
   {
      super(pMessage);
      errorsList = pErrs;
   }

   public ManagementModuleException(final ErrorEnumeration pErrs, final String pMessage)
   {
      super(pMessage);
      errorsList.add(pErrs);
   }

   public ErrorEnumeration getCode()
   {
      if (!this.errorsList.isEmpty())
      {
         return this.errorsList.get(0);

      }
      else
      {

         return null;
      }
   }

   public List<ErrorEnumeration> getErrors()
   {
      return this.errorsList;
   }
   
   
}
