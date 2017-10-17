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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.PluginView;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.InstancesListPresenter;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class InstanceColumnActionsGenerator implements ColumnGenerator
{

  /**
   * SerialUID
   */
  private static final long            serialVersionUID = 999142676656803840L;
  /**
   * The {@link InstancesListPresenter} of the column's table
   */
  private final InstancesListPresenter presenter;

  /**
   * Default constructor
   * 
   * @param pPresenter
   *          The {@link InstancesListPresenter} of the column's table
   */
  public InstanceColumnActionsGenerator(final InstancesListPresenter pPresenter)
  {
    super();
    presenter = pPresenter;

  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("serial")
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    final Locale locale = UI.getCurrent().getLocale();

    final Embedded editInstance = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_INSTANCE_EDIT));
    editInstance.setWidth(NovaForge.ACTION_ICON_SIZE);
    editInstance.setStyleName(NovaForge.BUTTON_IMAGE);
    editInstance.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_INSTANCE_ACTIONS_EDIT));
    editInstance.addClickListener(new MouseEvents.ClickListener()
    {
      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        pSource.select(pItemId);
        presenter.editInstanceClicked((UUID) pItemId);
      }
    });

    final Embedded adminInstance = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_INSTANCE_ADMIN));
    adminInstance.setWidth(NovaForge.ACTION_ICON_SIZE);
    adminInstance.setStyleName(NovaForge.BUTTON_IMAGE);
    adminInstance.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_INSTANCE_ACTIONS_CONFIGURE));
    adminInstance.addClickListener(new MouseEvents.ClickListener()
    {
      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        pSource.select(pItemId);
        presenter.adminInstanceClicked((UUID) pItemId);
      }
    });
    // If plugin has no admin view, disable the admin button
    List<PluginView> views;
    try
    {
      views = PluginsModule.getPluginsManager().getPluginService(presenter.getPluginID()).getMetadata()
          .getPluginViews();
      boolean hasAdminView = false;
      for (final PluginView view : views)
      {
        if (PluginViewEnum.ADMINISTRATION.equals(view.getViewId()))
        {
          hasAdminView = true;
        }
      }
      if (!hasAdminView)
      {
        adminInstance.setEnabled(false);
        adminInstance.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
            Messages.PLUGINSMANAGEMENT_ADMIN_INSTANCE_UNABLE));
      }
    }
    catch (final Exception e1)
    {
      adminInstance.setEnabled(false);
      adminInstance.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
          Messages.PLUGINSMANAGEMENT_ADMIN_INSTANCE_UNABLE));
      Notification.show(
          PluginsModule.getPortalMessages().getMessage(locale, Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
          PluginsModule.getPortalMessages().getMessage(locale,
              Messages.PLUGINSMANAGEMENT_ERROR_LOADING_INSTANCES_ADMINVIEW, pItemId.toString()),
          Type.ERROR_MESSAGE);
    }
    final Embedded projectsInstance = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_LIST_SUB2));
    projectsInstance.setWidth(NovaForge.ACTION_ICON_SIZE);
    projectsInstance.setStyleName(NovaForge.BUTTON_IMAGE);
    projectsInstance.addClickListener(new MouseEvents.ClickListener()
    {
      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        pSource.select(pItemId);
        presenter.showProjectsClicked((UUID) pItemId);
      }
    });

    final Embedded deleteInstance = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_INSTANCE_TRASH));
    deleteInstance.setWidth(NovaForge.ACTION_ICON_SIZE);
    deleteInstance.setStyleName(NovaForge.BUTTON_IMAGE);
    deleteInstance.addClickListener(new MouseEvents.ClickListener()
    {
      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        pSource.select(pItemId);
        presenter.showDeleteInstanceWindow((UUID) pItemId);
      }
    });

    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.addComponent(editInstance);
    actionsLayout.addComponent(adminInstance);
    actionsLayout.addComponent(projectsInstance);
    actionsLayout.addComponent(deleteInstance);
    actionsLayout.setComponentAlignment(editInstance, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(adminInstance, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(projectsInstance, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(deleteInstance, Alignment.MIDDLE_CENTER);
    actionsLayout.setSizeUndefined();

    long appNumber;
    try
    {
      appNumber = PluginsModule.getPluginsManager().getPluginService(presenter.getPluginID())
          .getToolInstanceProvisioningService().countApplications((UUID) pItemId);
      if (appNumber > 0)
      {
        projectsInstance.setEnabled(true);
        projectsInstance.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
            Messages.PLUGINSMANAGEMENT_INSTANCE_ACTIONS_PROJECTS));
        deleteInstance.setEnabled(false);
        deleteInstance.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
            Messages.PLUGINSMANAGEMENT_DELETE_INSTANCE_UNABLE));
      }
      else
      {
        projectsInstance.setEnabled(false);
        projectsInstance.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
            Messages.PLUGINSMANAGEMENT_PROJECTS_INSTANCE_UNABLE));
        deleteInstance.setEnabled(true);
        deleteInstance.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
            Messages.PLUGINSMANAGEMENT_INSTANCE_ACTIONS_DELETE));
      }
    }
    catch (final Exception e)
    {
      deleteInstance.setEnabled(false);
      deleteInstance.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
          Messages.PLUGINSMANAGEMENT_DELETE_INSTANCE_UNABLE));
      Notification.show(
          PluginsModule.getPortalMessages().getMessage(locale, Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
          PluginsModule.getPortalMessages().getMessage(locale,
              Messages.PLUGINSMANAGEMENT_ERROR_LOADING_INSTANCES_APPLICATIONS, pItemId.toString()),
          Type.ERROR_MESSAGE);
    }

    return actionsLayout;
  }

}
