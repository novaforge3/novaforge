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
package org.novaforge.forge.ui.dashboard.internal.client.tab.content.component;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

public class WidgetHeader extends HorizontalLayout
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 389047786828433325L;
  private final Button      refresh;
  private Button            settings;

  public WidgetHeader(final boolean pHasAdminRights)
  {
    addStyleName(NovaForge.DASHBOARD_PORTLET_HEADER);
    refresh = new NativeButton();
    refresh.setStyleName(NovaForge.DASHBOARD_HEADER_REFRESH);
    addComponent(refresh);

    if (pHasAdminRights)
    {
      settings = new NativeButton();
      settings.setStyleName(NovaForge.DASHBOARD_HEADER_SETTINGS);
      addComponent(settings);
    }
  }

  /**
   * Returns the image used for refresh action
   * 
   * @return the refresh {@link Button}
   */
  public Button getRefresh()
  {
    return refresh;
  }

  /**
   * Returns the image used for settings action
   * 
   * @return the settings {@link Button}
   */
  public Button getSettings()
  {
    return settings;
  }

}
