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
package org.novaforge.forge.tools.deliverymanager.ui.client.ressources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

/**
 * @author BILET-JC
 */
public interface DeliveryManagementRessources extends ClientBundle
{
   /**
    * The styles used in this widget.
    */
   @Source("DeliveryManagementCss.css")
   Style css();

   @ImageOptions(height = 40, width = 40)
   @Source("img/warning.png")
   ImageResource warning();

   @ImageOptions(height = 40, width = 40)
   @Source("img/ok.png")
   ImageResource ok();

   @ImageOptions(height = 20, width = 20)
   @Source("img/edit.png")
   ImageResource edit();

   @ImageOptions(height = 20, width = 20)
   @Source("img/edit.png")
   ImageResource editDisabled();

   @ImageOptions(height = 20, width = 20)
   @Source("img/ko.png")
   ImageResource koSmall();

   @ImageOptions(height = 40, width = 40)
   @Source("img/ko.png")
   ImageResource ko();

   @Source("img/remove.png")
   @ImageOptions(height = 16, width = 16)
   ImageResource delete();

   @Source("img/add.png")
   @ImageOptions(height = 16, width = 16)
   ImageResource add();

   @ImageOptions(height = 20, width = 20)
   @Source("img/download_gray.png")
   ImageResource downloadGray();

   @ImageOptions(height = 20, width = 20)
   @Source("img/download.png")
   ImageResource download();

   @ImageOptions(height = 20, width = 20)
   @Source("img/generate.png")
   ImageResource generate();

   @ImageOptions(height = 20, width = 20)
   @Source("img/locked.png")
   ImageResource lock();

   @ImageOptions(height = 20, width = 20)
   @Source("img/locked-grey.png")
   ImageResource lockDisabled();

   @Source("img/small_loading.gif")
   ImageResource smallLoading();

   interface Style extends CssResource
   {
      String infoAction();

      String contentDirectoryCell();

      String cellTable();

      String zoneTable();

      String zonePanel();

      String templateGrid();

      String templateGridTitle();

   }

}
