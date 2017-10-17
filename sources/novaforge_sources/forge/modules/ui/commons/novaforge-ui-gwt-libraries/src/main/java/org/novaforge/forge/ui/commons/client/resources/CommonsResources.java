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
package org.novaforge.forge.ui.commons.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

/**
 * @author lamirang
 */
public interface CommonsResources extends ClientBundle
{
   /**
    * The styles used in this widget.
    */
   @Source("CommonsCss.css")
   Style css();

   @ImageOptions(height = 40, width = 40)
   @Source("img/warning.png")
   ImageResource warning();

   @ImageOptions(height = 40, width = 40)
   @Source("img/ok.png")
   ImageResource ok();

   @Source("img/info.png")
   ImageResource info();

   @ImageOptions(height = 20, width = 20)
   @Source("img/ko.png")
   ImageResource ko_small();

   @ImageOptions(height = 40, width = 40)
   @Source("img/ko.png")
   ImageResource ko();

   @Source("img/remove.png")
   @ImageOptions(height = 16, width = 16)
   ImageResource delete();

   @Source("img/add.png")
   @ImageOptions(height = 16, width = 16)
   ImageResource add();

   @Source("img/small_loading.gif")
   ImageResource smallLoading();

   interface Style extends CssResource
   {
      String zoneTitle();

      String actionPanel();

      String zonePanel();

      String cellTable();

      String zoneTable();

      String cellPanel();

      String bottomButonPanel();

      String important();

      String importantRed();

      String red();

      String orange();

      String emptyLabel();

      String gridLabel();

      String gridTB();

      String gridInfo();

      String infoGrid();

      String infoGridAuto();

      String subZoneTitle();

      String borderCell();

      String titleList();

      String gridRowPair();

      String labelTB();

      String labelCell();

      String center();

      String textCenter();

   }

}
