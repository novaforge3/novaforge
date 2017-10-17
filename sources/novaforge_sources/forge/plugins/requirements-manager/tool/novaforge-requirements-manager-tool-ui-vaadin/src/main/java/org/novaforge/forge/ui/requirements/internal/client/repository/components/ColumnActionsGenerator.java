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
package org.novaforge.forge.ui.requirements.internal.client.repository.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.client.repository.view.RepositoryPresenter;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class ColumnActionsGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long         serialVersionUID = 405743508976694042L;
  /**
   * The {@link RepositoryPresenter} associated
   */
  private final RepositoryPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the repository table
   */
  public ColumnActionsGenerator(final RepositoryPresenter pPresenter)
  {
    super();
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    final String repositoryURI = (String) pItemId;
    // Get Locale
    final Locale locale = UI.getCurrent().getLocale();
    // ACTION EDIT repository
    final Embedded editRepositoryIcon = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_LIST_EDIT));
    editRepositoryIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    editRepositoryIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    editRepositoryIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    editRepositoryIcon.setDescription(RequirementsModule.getPortalMessages().getMessage(locale,
        Messages.REPOSITORY_ACTIONS_EDIT));
    editRepositoryIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -8691205561106114469L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.showEditRepository(repositoryURI);
      }
    });

    // ACTION DELETE repository
    final Embedded deleteRepositoryIcon = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_LIST_TRASH));
    deleteRepositoryIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    deleteRepositoryIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    deleteRepositoryIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    deleteRepositoryIcon.setDescription(RequirementsModule.getPortalMessages().getMessage(locale,
        Messages.REPOSITORY_ACTIONS_DELETE));
    deleteRepositoryIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -5423653524204805218L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.onDeleteRepositoryAction(repositoryURI);

      }
    });

    // Add buttons to layout
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.addComponent(editRepositoryIcon);
    actionsLayout.addComponent(deleteRepositoryIcon);
    actionsLayout.setComponentAlignment(editRepositoryIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(deleteRepositoryIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setSizeUndefined();

    return actionsLayout;
  }
}
