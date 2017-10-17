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
package org.novaforge.forge.ui.distribution.client.presenter.commons;

import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.distribution.client.properties.ErrorCodeMapping;
import org.novaforge.forge.ui.distribution.shared.exceptions.DistributionServiceException;
import org.novaforge.forge.ui.distribution.shared.exceptions.ErrorEnumeration;

/**
 * @author lamirang
 */
public class ErrorManagement
{

   public static void displayErrorMessage(final Throwable caught)
   {
      if ((caught instanceof DistributionServiceException)
            && (((DistributionServiceException) caught).getCode() != null))
      {
         ErrorEnumeration code = ((DistributionServiceException) caught).getCode();

         InfoDialogBox info = new InfoDialogBox(ErrorCodeMapping.getLocalizedMessage(code));
         info.show();
      }
      else
      {
         InfoDialogBox info = new InfoDialogBox(
               ErrorCodeMapping.getLocalizedMessage(ErrorEnumeration.TECHNICAL_ERROR));
         info.show();
      }
   }

   /*
    * public static void displayMultipleErrorMessage(final Throwable caught, final String pGlobalMessage) { if
    * ((caught instanceof DistributionServiceException) && (((DistributionServiceException)
    * caught).getError().isEmpty() == false)) { List<ErrorEnumeration> codes = ((DistributionServiceException)
    * caught).getErrors(); StringBuilder errorMessage = new StringBuilder();
    * errorMessage.append(pGlobalMessage); for (ErrorEnumeration error : codes) {
    * errorMessage.append("<br/> - "); errorMessage.append(ErrorMapping.getMessage(error)); } InfoDialogBox
    * info = new InfoDialogBox(errorMessage.toString()); info.show(); } else { InfoDialogBox info = new
    * InfoDialogBox(ErrorMapping.getMessage(ErrorEnumeration.TECHNICAL_ERROR));
    * info.getDialogPanel().center(); info.getDialogPanel().show(); } }
    */
}
