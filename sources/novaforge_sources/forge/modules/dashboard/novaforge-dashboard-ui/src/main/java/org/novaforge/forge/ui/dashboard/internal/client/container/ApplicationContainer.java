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
package org.novaforge.forge.ui.dashboard.internal.client.container;

import com.google.common.base.Strings;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
/**
 * @author Guillaume Lamirand
 */
public class ApplicationContainer extends IndexedContainer
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -209979530940749915L;

  /**
   * Logger component
   */
  private static final Log  LOGGER           = LogFactory.getLog(ApplicationContainer.class);

  /**
   * Default constructor. It will initialize application item properties
   * 
   * @see ApplicationItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public ApplicationContainer()
  {
    super();
    addContainerProperty(ApplicationItemProperty.INSTANCE_UUID.getPropertyId(), UUID.class, null);
    addContainerProperty(ApplicationItemProperty.PLUGIN_UUID.getPropertyId(), UUID.class, null);
    addContainerProperty(ApplicationItemProperty.ICON.getPropertyId(), Resource.class, null);
    addContainerProperty(ApplicationItemProperty.NAME.getPropertyId(), String.class, null);
    addContainerProperty(ApplicationItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(ApplicationItemProperty.PROJECT_ID.getPropertyId(), String.class, null);
    addContainerProperty(ApplicationItemProperty.PROJECT_NAME.getPropertyId(), String.class, null);
    addContainerProperty(ApplicationItemProperty.PROJECT_ICON.getPropertyId(), Resource.class, null);
  }

  /**
   * Clear the container filter and items and
   * adds the given list to the container.
   * 
   * @param pProjectId
   *          the project id
   * @param pApps
   *          list of application to add
   */
  public void setApplications(final Project pProject, final List<ProjectApplication> pApps)
  {
    removeAllItems();
    removeAllContainerFilters();
    for (final ProjectApplication app : pApps)
    {

      final UUID appId = app.getPluginInstanceUUID();
      final boolean availablePlugin = DashboardModule.getPluginsManager().isAvailablePlugin(
          app.getPluginUUID().toString());
      if ((availablePlugin) && (ApplicationStatus.ACTIVE.equals(app.getStatus())))
      {
        addItem(appId);
        getContainerProperty(appId, ApplicationItemProperty.INSTANCE_UUID.getPropertyId()).setValue(appId);
        getContainerProperty(appId, ApplicationItemProperty.PLUGIN_UUID.getPropertyId()).setValue(
            app.getPluginUUID());
        getContainerProperty(appId, ApplicationItemProperty.PROJECT_ID.getPropertyId()).setValue(
            pProject.getProjectId());
        getContainerProperty(appId, ApplicationItemProperty.PROJECT_NAME.getPropertyId()).setValue(
            pProject.getName());
        getContainerProperty(appId, ApplicationItemProperty.NAME.getPropertyId()).setValue(app.getName());

        getContainerProperty(appId, ApplicationItemProperty.DESCRIPTION.getPropertyId()).setValue(
            app.getDescription());
        try
        {
          final byte[] pluginIcon = DashboardModule.getPluginsManager()
              .getPluginService(app.getPluginUUID().toString()).getPluginIcon();
          if (pluginIcon != null)
          {
            final StreamResource buildImageResource = ResourceUtils.buildImageResource(pluginIcon,
                app.getName());
            getContainerProperty(appId, ApplicationItemProperty.ICON.getPropertyId()).setValue(
                buildImageResource);
          }

          final byte[] projectIcon = pProject.getImage().getFile();
          if (projectIcon != null)
          {
            final StreamResource buildProjectImageResource = ResourceUtils.buildImageResource(projectIcon,
                pProject.getName());
            getContainerProperty(appId, ApplicationItemProperty.PROJECT_ICON.getPropertyId()).setValue(
                buildProjectImageResource);
          }
        }
        catch (final PluginManagerException e)
        {
          LOGGER.warn(String.format("Unable to retrieve plugin icon with [uuid=%s]", app.getPluginUUID()), e);
          getContainerProperty(appId, ApplicationItemProperty.ICON.getPropertyId()).setValue(
              new ThemeResource(NovaForgeResources.ICON_ERROR));
        }
      }
    }
    sort(new Object[] { ApplicationItemProperty.NAME.getPropertyId() }, new boolean[] { true });

  }

  /**
   * Add given item to this container
   * 
   * @param pProjectId
   *          the project id
   * @param pApplicationItem
   *          the item to add
   */
  public void addApplication(final String pProjectId, final Item pApplicationItem)
  {
    final UUID appId = (UUID) pApplicationItem.getItemProperty(
        ApplicationItemProperty.INSTANCE_UUID.getPropertyId()).getValue();

    addItem(appId);
    for (final Object prop : getContainerPropertyIds())
    {
      final Object value = pApplicationItem.getItemProperty(prop).getValue();
      getContainerProperty(appId, prop).setValue(value);
    }
    getContainerProperty(appId, ApplicationItemProperty.PROJECT_ID.getPropertyId()).setValue(pProjectId);
  }

  /**
   * Remove the given item from this container
   * 
   * @param pApplicationId
   *          the id to remove the item
   */
  public void removeApplication(final UUID pApplicationId)
  {
    removeItem(pApplicationId);
  }

  /**
   * Check if the container contains any applications for the given project id
   * 
   * @param pProjectId
   *          the project if to check
   * @return true if the container contains at least one application for the project id given
   */
  public boolean containsApplication(final String pProjectId)
  {
    boolean containsApp = false;
    if (!Strings.isNullOrEmpty(pProjectId))
    {
      final List<Object> allItemIds = getAllItemIds();
      for (final Object itemId : allItemIds)
      {
        final Object projectId = getContainerProperty(itemId,
            ApplicationItemProperty.PROJECT_ID.getPropertyId()).getValue();
        if (pProjectId.equals(projectId))
        {
          containsApp = true;
          break;
        }
      }
    }
    return containsApp;
  }

  /**
   * Returns applications item id found for the given project id
   * 
   * @param pProjectId
   *          the project if
   * @return applications found for the given project id
   */
  public List<UUID> getApplications(final String pProjectId)
  {
    final List<UUID> uuidsList = new ArrayList<UUID>();
    if (!Strings.isNullOrEmpty(pProjectId))
    {
      final List<Object> allItemIds = getAllItemIds();
      for (final Object itemId : allItemIds)
      {
        final Object projectId = getContainerProperty(itemId,
            ApplicationItemProperty.PROJECT_ID.getPropertyId()).getValue();
        if (pProjectId.equals(projectId))
        {
          uuidsList.add((UUID) itemId);
        }
      }
    }
    return uuidsList;
  }
}
