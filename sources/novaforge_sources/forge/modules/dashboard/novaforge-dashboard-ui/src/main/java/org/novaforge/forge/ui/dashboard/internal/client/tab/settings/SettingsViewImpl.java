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
package org.novaforge.forge.ui.dashboard.internal.client.tab.settings;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.client.component.SideBarLayout;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class SettingsViewImpl extends SideBarLayout implements SettingsView
{

  /**
   * Serialization id
   */
  private static final long   serialVersionUID = -569936718818055045L;
  private static final String DATASOURCE_KEY   = "datasource";
  private static final String PROPERTIES_KEY   = "properties";
  private final Button datasource;
  private final Button properties;

  /**
   * Default constructor.
   */
  public SettingsViewImpl()
  {
    super();
    addStyleName(NovaForge.DASHBOARD_SETTINGS);
    addStyleName(NovaForge.SIDEBAR_APPLICATION_CONTENT);
    datasource = addButton(DATASOURCE_KEY, new ThemeResource(NovaForgeResources.ICON_LIST_SUB));
    properties = addButton(PROPERTIES_KEY, new ThemeResource(NovaForgeResources.ICON_SETTINGS));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    getTitle().setValue(
        DashboardModule.getPortalMessages().getMessage(pLocale, Messages.DASHBOARD_SETTINGS_MENU_TITLE));
    datasource.setCaption(DashboardModule.getPortalMessages().getMessage(pLocale,
        Messages.DASHBOARD_SETTINGS_MENU_DATASOURCE));
    properties.setCaption(DashboardModule.getPortalMessages().getMessage(pLocale,
        Messages.DASHBOARD_SETTINGS_MENU_PROPERTIES));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getDataSource()
  {
    return datasource;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getProperties()
  {
    return properties;
  }

}
