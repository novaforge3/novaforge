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
package org.novaforge.forge.tools.managementmodule.ui.client.ressources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

public interface ManagementModuleRessources extends ClientBundle {

	@Source("ManagementModule.css")
	ManagementModuleCss css();

	@Source("add.png")
	@ImageOptions(height = 16, width = 16)
	ImageResource addButton();

	@Source("remove.png")
	@ImageOptions(height = 16, width = 16)
	ImageResource deleteButton();

	@Source("big_loading.gif")
	ImageResource bigLoading();

	@Source("small_loading.gif")
	ImageResource smallLoading();

	@Source("refresh.png")
	ImageResource refresh();

	@ImageOptions(height = 32, width = 32)
	@Source("ok.png")
	ImageResource ok();

	@ImageOptions(height = 32, width = 32)
	@Source("error.png")
	ImageResource error();

	@ImageOptions(height = 32, width = 32)
	@Source("warning.png")
	ImageResource warning();

	@Source("arrow_top.png")
	ImageResource arrow_top();

	@Source("arrow_bottom.png")
	ImageResource arrow_bottom();

	@Source("update.png")
	ImageResource update();

	@Source("ko.png")
	ImageResource delete();

	@ImageOptions(height = 16, width = 16)
	@Source("viewmag+.png")
	ImageResource details();

	@ImageOptions(height = 128, width = 128)
	@Source("katuberling.png")
	ImageResource patate();

	interface ManagementModuleCss extends CssResource
	{
		String important();

		String borderCell();

		String actionPanel();

		String infoPanel();

		String zoneTitle();

		String zonePanel();

		String titleList();

		String titleInfo();

		String bottomButonPanel();

		String subZoneTitle();

		String labelTB();

		String cellTable();

		String cellPanel();

		String zoneTable();

		String zoneTableLeft();

		String emptyLabel();

		String infoGrid();

		String labelCell();

		String gridLabel();

		String gridLabelRight();

		String gridInfo();

		String gridTB();

		String gridRowPair();

		String center();

		String chargePlanMainTable();

		String disabledPanel();

		String disabledLink();

		/**
		 * Acronym style
		 *
		 * @return the acronym css string
		 */
		String acronymLabel();

		/**
		 * Get the anchor style from css
		 *
		 * @return the anchor css string
		 */
		String anchor();

		/**
		 * Get the subtitle style from css
		 *
		 * @return the subTitle css string
		 */
		String subTitle();
	}
}
