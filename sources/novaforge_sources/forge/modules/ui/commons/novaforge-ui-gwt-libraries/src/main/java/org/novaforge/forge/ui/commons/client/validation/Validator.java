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
package org.novaforge.forge.ui.commons.client.validation;

import org.novaforge.forge.ui.commons.client.Common;

/**
 * A Validator used to validate text base widget
 * 
 * @author lamirang
 */
public interface Validator
{
   /**
    * @param pValue
    *           string to test
    * @return true if the parameter is valid
    */
   boolean isValid(final String pValue);

   /**
    * @return error message to display
    */
   String getErrorMessage();

   /**
    * Default implementation of {@link Validator}.
    * <p>
    * It will valid if the string given is not null or empty
    *
    * @author Guillaume Lamirand
    */
   class DefaultValidator implements Validator
   {

      /**
       * {@inheritDoc}
       */
      @Override
      public boolean isValid(final String pValue)
      {
         return Common.validateStringNotNull(pValue);
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public String getErrorMessage()
      {
         return Common.getMessages().defaultValidation();
      }

   }

   /**
    * Iimplementation of {@link Validator} for long texte.
    * <p>
    * It will valid if the string given is not null or empty and less long than 250.
    *
    * @author Guillaume Lamirand
    */
   class LongTextValidator implements Validator
   {

      /**
       * {@inheritDoc}
       */
      @Override
      public boolean isValid(final String pValue)
      {
         boolean valid = false;
         if ((Common.validateStringNotNull(pValue)) && (pValue.length() < 250))
         {
            valid = true;
         }
         return valid;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public String getErrorMessage()
      {
         return Common.getMessages().defaultValidation();
      }

   }

}
