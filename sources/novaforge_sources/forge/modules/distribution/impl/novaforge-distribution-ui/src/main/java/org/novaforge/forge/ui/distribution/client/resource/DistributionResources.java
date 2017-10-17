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
package org.novaforge.forge.ui.distribution.client.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

/**
 * @author lamirang
 */
public interface DistributionResources extends ClientBundle
{
   @Source("Distribution.css")
   ProjectComitCss css();

   @ImageOptions(height = 16, width = 16)
   @Source("img/warning.png")
   ImageResource warning();

   @ImageOptions(height = 20, width = 20)
   @Source("img/warning.png")
   ImageResource warning2();

   @ImageOptions(height = 16, width = 16)
   @Source("img/ok.png")
   ImageResource ok();

   @ImageOptions(height = 16, width = 16)
   @Source("img/ko.png")
   ImageResource ko();

   @Source("img/big_loading.gif")
   ImageResource bigLoading();

   interface ProjectComitCss extends CssResource
   {
      String important();

      String borderCell();

      String actionPanel();

      String zoneTitle();

      String zoneTitle2();

      String zonePanel();

      String titleList();

      String bottomButonPanel();

      String subZoneTitle();

      String labelTB();

      String cellTable();

      String cellPanel();

      String zoneTable();

      String emptyLabel();

      String infoGrid();

      String labelCell();
      String gridLabel();

      String gridInfo();

      String gridTB();

      String gridRowPair();

      String center();

      String textInvalid();

      String text();

      String formZone();

      String warningMessage();

   }

}
