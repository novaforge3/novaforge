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
package org.novaforge.forge.ui.memberships.internal.client.global;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.client.component.SideBarLayout;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class GlobalViewImpl extends SideBarLayout implements GlobalView
{

  /**
   * Serialization id
   */
  private static final long   serialVersionUID = -569936718818055045L;
  private static final String USERS_KEY        = "users";
  private static final String GROUPS_KEY       = "groups";
  private static final String REQUESTS_KEY     = "requests";
  private static final String ROLES_KEY        = "roles";

  private final Button        users;
  private final Button        groups;
  private final Button        requests;
  private final Button        roles;

  /**
   * Default constructor.
   */
  public GlobalViewImpl()
  {
    super();
    users = addButton(USERS_KEY, new ThemeResource(NovaForgeResources.ICON_USER));
    groups = addButton(GROUPS_KEY, new ThemeResource(NovaForgeResources.ICON_GROUP));
    requests = addButton(REQUESTS_KEY, new ThemeResource(NovaForgeResources.ICON_USER_JOIN));
    roles = addButton(ROLES_KEY, new ThemeResource(NovaForgeResources.ICON_KEY));
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
        MembershipsModule.getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_GLOBAL_TITLE));
    users.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GLOBAL_USERS));
    groups.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GLOBAL_GROUPS));
    requests.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GLOBAL_REQUESTS));
    roles.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_GLOBAL_ROLES));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getUser()
  {
    return users;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getGroup()
  {
    return groups;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequest()
  {
    return requests;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRoles()
  {
    return roles;
  }

}
