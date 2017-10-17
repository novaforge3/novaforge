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
package org.novaforge.forge.ui.portal.client.component.widgetset.client.tabsheetimproved.shared;

import com.vaadin.shared.AbstractComponentState;

/**
 * @author Guillaume Lamirand
 */
public class TabsheetImprovedState extends AbstractComponentState
{
  public static final  String PRIMARY_STYLE_NAME = "v-tabsheetimproved";
  /**
   * Serial version id
   */
  private static final long   serialVersionUID   = -4445489783444242462L;
  /**
   * Index of the component when switching focus - not related to Tabsheet
   * tabs.
   */
  public int tabIndex;
  public java.util.List<TabImprovedState> tabs        = new java.util.ArrayList<TabImprovedState>();
  /**
   * true to show the tab bar, false to only show the contained component
   */
  public boolean                          tabsVisible = true;
  /**
   * the key of the currently selected tab
   */
  public String selected;
  /**
   * <code>true</code> to show the plus button, <code>false</code> to only show the tabs
   */
  public boolean plusButtonVisible = false;

  {
    primaryStyleName = PRIMARY_STYLE_NAME;
  }

}
