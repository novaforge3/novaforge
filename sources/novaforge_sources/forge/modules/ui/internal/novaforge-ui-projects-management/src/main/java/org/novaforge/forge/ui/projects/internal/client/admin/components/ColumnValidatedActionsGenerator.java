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
package org.novaforge.forge.ui.projects.internal.client.admin.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.projects.internal.client.admin.validated.ProjectsValidatedPresenter;
import org.novaforge.forge.ui.projects.internal.module.ProjectServices;

/**
 * @author Guillaume Lamirand
 */
public class ColumnValidatedActionsGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long                serialVersionUID = 8736106711161577296L;

  private final ProjectsValidatedPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the associated presenter
   */
  public ColumnValidatedActionsGenerator(final ProjectsValidatedPresenter pPresenter)
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
    // Add buttons to layout
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.setSpacing(true);
    if (pSource.getParent() != null)
    {
      // ACTION open project
      final Embedded openIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_OPEN4));
      openIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
      openIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
      openIcon.setStyleName(NovaForge.BUTTON_IMAGE);
      openIcon.setDescription(ProjectServices.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.PROJECT_ADMIN_VALIDATED_OPEN));
      openIcon.addClickListener(new MouseEvents.ClickListener()
      {

        /**
         * Serial version id
         */
        private static final long serialVersionUID = -2163520041301760959L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
        {
          pSource.select(pItemId);
          presenter.onClickActionOpen(pItemId);

        }
      });

      // ACTION edit project
      final Embedded editIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_PROJECT_EDIT));
      editIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
      editIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
      editIcon.setStyleName(NovaForge.BUTTON_IMAGE);
      editIcon.setDescription(ProjectServices.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.PROJECT_ADMIN_VALIDATED_EDIT));
      editIcon.addClickListener(new MouseEvents.ClickListener()
      {

        /**
         * serial version id
         */
        private static final long serialVersionUID = -1712699522491483659L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
        {
          pSource.select(pItemId);
          presenter.onClickActionEdit(pItemId);

        }
      });
      // ACTION delete project
      final Embedded deleteIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_PROJECT_TRASH));
      deleteIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
      deleteIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
      deleteIcon.setStyleName(NovaForge.BUTTON_IMAGE);
      deleteIcon.setDescription(ProjectServices.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.PROJECT_ADMIN_VALIDATED_DELETE));
      deleteIcon.addClickListener(new MouseEvents.ClickListener()
      {

        /**
         * serial version id
         */
        private static final long serialVersionUID = -1712699522491483659L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
        {
          pSource.select(pItemId);
          presenter.onClickActionDelete(pItemId);

        }
      });
      actionsLayout.addComponent(openIcon);
      actionsLayout.addComponent(editIcon);
      actionsLayout.addComponent(deleteIcon);
      actionsLayout.setComponentAlignment(openIcon, Alignment.MIDDLE_CENTER);
      actionsLayout.setComponentAlignment(editIcon, Alignment.MIDDLE_CENTER);
      actionsLayout.setComponentAlignment(deleteIcon, Alignment.MIDDLE_CENTER);
      actionsLayout.setSizeUndefined();
    }
    return actionsLayout;
  }
}
