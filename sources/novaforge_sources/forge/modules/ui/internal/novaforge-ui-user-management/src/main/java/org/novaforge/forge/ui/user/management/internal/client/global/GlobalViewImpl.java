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
package org.novaforge.forge.ui.user.management.internal.client.global;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.SideBarLayout;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class GlobalViewImpl extends SideBarLayout implements GlobalView
{

  /**
   * Default serial version UID
   */
  private static final long   serialVersionUID        = -569936718818055045L;
  private static final String ADMINUSERS_KEY          = "adminUsers";
  private static final String BLACKLISTUSERS_KEY      = "blacklistUsers";
  private static final String MANAGESECURITYRULES_KEY = "manageSecurityRules";
  /**
   * The admin users button
   */
  private final Button        adminUsers;
  /**
   * The blacklist users button
   */
  private final Button        blacklistUsers;
  /**
   * The manage security rules button
   */
  private final Button        manageSecurityRules;

  /**
   * Default constructor.
   */
  public GlobalViewImpl()
  {
    super();

    adminUsers = addButton(ADMINUSERS_KEY, new ThemeResource(NovaForgeResources.ICON_USER_VALIDATE));
    blacklistUsers = addButton(BLACKLISTUSERS_KEY, new ThemeResource(NovaForgeResources.ICON_USER_BLOCKED));
    manageSecurityRules = addButton(MANAGESECURITYRULES_KEY, new ThemeResource(NovaForgeResources.ICON_LOCK));
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
        AdminModule.getPortalMessages().getMessage(pLocale, Messages.USERMANAGEMENT_ADMIN_MENU_TITLE));
    adminUsers.setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
        Messages.USERMANAGEMENT_ADMIN_MENU_USERS));
    blacklistUsers.setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
        Messages.USERMANAGEMENT_ADMIN_MENU_BLACKLIST));
    manageSecurityRules.setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
        Messages.USERMANAGEMENT_ADMIN_MENU_SECURITY));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAdminUsers()
  {
    return adminUsers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getBlackListUsers()
  {
    return blacklistUsers;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getManageSecurityRules()
  {
    return manageSecurityRules;
  }

}
