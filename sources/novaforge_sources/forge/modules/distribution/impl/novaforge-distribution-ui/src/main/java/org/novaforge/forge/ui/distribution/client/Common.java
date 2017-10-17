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
package org.novaforge.forge.ui.distribution.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoTypeEnum;
import org.novaforge.forge.ui.distribution.client.properties.DistributionMessage;
import org.novaforge.forge.ui.distribution.client.properties.ErrorCodeMapping;
import org.novaforge.forge.ui.distribution.client.resource.DistributionResources;
import org.novaforge.forge.ui.distribution.shared.exceptions.DistributionServiceException;
import org.novaforge.forge.ui.distribution.shared.exceptions.ErrorEnumeration;

/**
 * @author BILET-JC
 *
 */
public final class Common
{
   public static final DistributionMessage   MESSAGES       = GWT.create(DistributionMessage.class);
   public static final DistributionResources RESOURCE       = GWT.create(DistributionResources.class);
   public static final String                         URL_REGEX       = "^[a-zA-Z0-9._-]+$";
   public static final String                         EMPTY_TEXT     = "";
   /**
    * Level central for a forge (the maximum level) Could be a mother forge Unique forge
    */
   public final static int                            CENTRAL        = 0;
   /**
    * Level zonal for a forge (the medium level) Could be a mother forge
    */
   public final static int                            ZONAL          = 1;
   /**
    * Level local for a forge (the minimum level) Could NOT be a mother forge
    */
   public final static int                            LOCAL          = 2;
   public final static int                            SUSPENDED      = -2;
   /**
    * Level orphan for a forge (the level by default)
    */
   public final static int                            ORPHAN         = -1;
   public static final DateTimeFormat        DATE_FORMAT      = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
   public static int     PAGE_SIZE        = 10;
   public static Integer IMG_BUTTON_SIZE  = 10;
   public static int     TEXT_AREA_WIDTH  = 30;
   public static int     TEXT_AREA_HEIGHT = 5;

   public static void displayErrorMessage(final Throwable caught)
   {
      if ((caught instanceof DistributionServiceException)
            && (((DistributionServiceException) caught).getCode() != null))
      {
         ErrorEnumeration code = ((DistributionServiceException) caught).getCode();

         InfoDialogBox info = new InfoDialogBox(ErrorCodeMapping.getLocalizedMessage(code),
               InfoTypeEnum.WARNING);
         info.show();
      }
      else
      {
         InfoDialogBox info = new InfoDialogBox(
               ErrorCodeMapping.getLocalizedMessage(ErrorEnumeration.TECHNICAL_ERROR), InfoTypeEnum.WARNING);
         info.show();
      }
   }

   /**
    * set the parameter to upper case
    * 
    * @param filter
    * @return
    */
   public static String allCarUp(String txt)
   {
      return txt.toUpperCase();
   }

   /**
    * set the first letter of the parameter to upper case
    * 
    * @param loginLabel
    * @return
    */
   public static String firstCarUp(String txt)
   {
      return txt.replaceFirst(".", (txt.charAt(0) + "").toUpperCase());
   }

   public static void setPageSize(int pPageSize)
   {
      PAGE_SIZE = pPageSize;
   }



   public static String getLevelLabel(int pForgeLevel)
   {
      String ret;
      switch (pForgeLevel)
      {
      case CENTRAL:
         ret = MESSAGES.centralEnum();
         break;
      case ZONAL:
         ret = MESSAGES.zonalEnum();
         break;
      case LOCAL:
         ret = MESSAGES.localEnum();
         break;
      case SUSPENDED:
         ret = MESSAGES.suspendedEnum();
         break;
      case ORPHAN:
         ret = MESSAGES.orphanEnum();
         break;
      default:
         ret = MESSAGES.eLevel();
         break;
      }
      return ret;
   }


}
