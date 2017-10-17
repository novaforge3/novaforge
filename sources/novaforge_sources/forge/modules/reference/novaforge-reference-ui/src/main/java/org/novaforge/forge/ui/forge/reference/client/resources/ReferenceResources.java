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
package org.novaforge.forge.ui.forge.reference.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * @author lamirang
 */
public interface ReferenceResources extends ClientBundle
{
  @Source("Reference.css")
  ManageProjectCss style();

  @Source("add.png")
  @ImageOptions(height = 16, width = 16)
  ImageResource addButton();

  @ImageOptions(height = 20, width = 20)
  @Source("download_gray.png")
  ImageResource download_gray();

  @ImageOptions(height = 20, width = 20)
  @Source("download.png")
  ImageResource download();

  @Source("remove.png")
  @ImageOptions(height = 16, width = 16)
  ImageResource deleteButton();

  @Source("big_loading.gif")
  ImageResource bigLoading();

  @Source("small_loading.gif")
  ImageResource smallLoading();

  @Source("refresh.png")
  ImageResource refresh();

  @ImageOptions(height = 64, width = 64)
  @Source("warning.png")
  ImageResource warning();

  @Source("arrow_top.png")
  ImageResource arrow_top();

  @Source("arrow_bottom.png")
  ImageResource arrow_bottom();

  /**
   * The background used for selected items.
   */
  @ImageOptions(repeatStyle = RepeatStyle.Both, flipRtl = true)
  ImageResource cellTreeSelectedBackground();

  @ImageOptions(height = 40, width = 40)
  @Source("img/ok.png")
  ImageResource ok();

  @ImageOptions(height = 40, width = 40)
  @Source("img/ko.png")
  ImageResource ko();

  @ImageOptions(height = 20, width = 20)
  @Source("img/ko.png")
  ImageResource ko_small();

  interface ManageProjectCss extends CssResource
  {
    String important();

    String borderCell();

    String scrollBorderCell();

    String red();

    String blue();

    String actionPanel();

    String zoneTitle();

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

    String menuLabel();

    String menuLabelSelected();

    String gridInfo();

    String gridTB();

    String gridRowPair();

    String center();

    String leftPanelContent();

    String importantRed();

  }

}
