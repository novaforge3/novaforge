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
package org.novaforge.forge.ui.applications.internal.client.global.components;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Node;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This component describes a specific {@link HierarchicalContainer} used to build project navigation tree.
 * 
 * @author Guillaume Lamirand
 */
public class ApplicationsContainer extends HierarchicalContainer
{

  /**
   * Serial version id for Serialization
   */
  private static final long serialVersionUID = 3439109343778568310L;

  /**
   * Logger component
   */
  private static final Log  LOGGER           = LogFactory.getLog(ApplicationsContainer.class);

  /**
   * Default constructor. It will initialize tree item property
   * 
   * @see ItemProperty
   * @see HierarchicalContainer#HierarchicalContainer()
   */
  public ApplicationsContainer()
  {
    super();
    addContainerProperty(ItemProperty.NODE.getPropertyId(), Node.class, null);
    addContainerProperty(ItemProperty.CAPTION.getPropertyId(), String.class, null);
    addContainerProperty(ItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(ItemProperty.ICON.getPropertyId(), Resource.class, null);
    addContainerProperty(ItemProperty.AVAILABILITY.getPropertyId(), Boolean.class, null);

  }

  /**
   * Add spaces and their children into container
   * 
   * @param allSpaces
   *          the spaces to add
   * @param pApplication
   *          the vaadin application
   */
  public void setSpaces(final Map<Space, List<ProjectApplication>> allSpaces)
  {
    removeAllItems();
    final Set<Entry<Space, List<ProjectApplication>>> spacesWithApp = allSpaces.entrySet();

    for (final Entry<Space, List<ProjectApplication>> entry : spacesWithApp)
    {
      final Space space = entry.getKey();

      // display only USER spaces
      if (RealmType.SYSTEM.equals(space.getRealmType()))
      {
        continue;
      }
      final String spaceItemId = space.getUri();
      addItem(spaceItemId);

      getContainerProperty(spaceItemId, ItemProperty.CAPTION.getPropertyId()).setValue(space.getName());
      getContainerProperty(spaceItemId, ItemProperty.DESCRIPTION.getPropertyId()).setValue(
          space.getDescription());
      getContainerProperty(spaceItemId, ItemProperty.NODE.getPropertyId()).setValue(space);
      for (final ProjectApplication app : entry.getValue())
      {
        final String appId = app.getPluginInstanceUUID().toString();
        addItem(appId);
        setParent(appId, spaceItemId);
        setChildrenAllowed(appId, false);
        getContainerProperty(appId, ItemProperty.NODE.getPropertyId()).setValue(app);
        getContainerProperty(appId, ItemProperty.CAPTION.getPropertyId()).setValue(app.getName());
        getContainerProperty(appId, ItemProperty.DESCRIPTION.getPropertyId()).setValue(app.getDescription());
        final boolean availablePlugin = ApplicationsModule.getPluginsManager().isAvailablePlugin(
            app.getPluginUUID().toString());
        getContainerProperty(appId, ItemProperty.AVAILABILITY.getPropertyId()).setValue(availablePlugin);
        if (availablePlugin)
        {
          if ((ApplicationStatus.CREATE_ON_ERROR.equals(app.getStatus()))
              || (ApplicationStatus.DELETE_ON_ERROR.equals(app.getStatus())))
          {
            getContainerProperty(appId, ItemProperty.ICON.getPropertyId()).setValue(
                new ThemeResource(NovaForgeResources.ICON_ERROR));
          }
          else
          {
            try
            {
              final byte[] pluginIcon = ApplicationsModule.getPluginsManager()
                  .getPluginService(app.getPluginUUID().toString()).getPluginIcon();

              if (pluginIcon != null)
              {
                final StreamResource buildImageResource = ResourceUtils.buildImageResource(pluginIcon,
                    app.getName());
                getContainerProperty(appId, ItemProperty.ICON.getPropertyId()).setValue(buildImageResource);
              }
            }
            catch (final PluginManagerException e)
            {
              LOGGER.warn(
                  String.format("Unable to retrieve plugin icon with [uuid=%s]", app.getPluginUUID()), e);
              getContainerProperty(appId, ItemProperty.ICON.getPropertyId()).setValue(
                  new ThemeResource(NovaForgeResources.ICON_ERROR));
            }
          }
        }
        else
        {
          getContainerProperty(appId, ItemProperty.ICON.getPropertyId()).setValue(
              new ThemeResource(NovaForgeResources.ICON_ERROR));
        }
      }

    }
    sort(new Object[] { ItemProperty.CAPTION.getPropertyId() }, new boolean[] { true });
  }
}
