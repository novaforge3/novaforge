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
package org.novaforge.forge.ui.projects.internal.client.admin.global;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.SideBarLayout;
import org.novaforge.forge.ui.projects.internal.module.ProjectServices;

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

  private static final String PROJECTS_KEY     = "projects";
  private static final String REJECTED_KEY     = "rejected";
  private static final String REQUESTS_KEY     = "requests";
  private final Button        projects;
  private final Button        requests;
  private final Button        rejected;

  /**
   * Default constructor.
   */
  public GlobalViewImpl()
  {
    super();
    projects = addButton(PROJECTS_KEY, new ThemeResource(NovaForgeResources.ICON_PROJECT_VALIDATED));
    requests = addButton(REQUESTS_KEY, new ThemeResource(NovaForgeResources.ICON_PROJECT_WAITING));
    rejected = addButton(REJECTED_KEY, new ThemeResource(NovaForgeResources.ICON_PROJECT_BLOCKED));

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
        ProjectServices.getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_MENU_TITLE));
    projects.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_MENU_PROJECTS));
    requests.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_MENU_REQUESTS));
    rejected.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_MENU_REJECTED));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getProjects()
  {
    return projects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequests()
  {
    return requests;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRejected()
  {
    return rejected;
  }

}
