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
package org.novaforge.forge.ui.memberships.internal.client.containers;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Node;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This component describes a specific {@link HierarchicalContainer} used to build application roles mapping
 * tree.
 * 
 * @author Guillaume Lamirand
 */
public class RolesMappingContainer extends HierarchicalContainer
{

  /**
   * Serial version id for Serialization
   */
  private static final long serialVersionUID = 3439109343778568310L;

  /**
   * Default constructor. It will initialize tree item property
   * 
   * @see RolesMappingItemProperty
   * @see HierarchicalContainer#HierarchicalContainer()
   */
  public RolesMappingContainer()
  {
    super();
    addContainerProperty(RolesMappingItemProperty.NODE.getPropertyId(), Node.class, null);
    addContainerProperty(RolesMappingItemProperty.CAPTION.getPropertyId(), String.class, null);
    addContainerProperty(RolesMappingItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(RolesMappingItemProperty.ICON.getPropertyId(), Resource.class, null);
    addContainerProperty(RolesMappingItemProperty.AVAILABILITY.getPropertyId(), Boolean.class, null);
    addContainerProperty(RolesMappingItemProperty.ROLES.getPropertyId(), Set.class, null);
    addContainerProperty(RolesMappingItemProperty.ROLE_MAPPED.getPropertyId(), String.class, null);

  }

  /**
   * Add spaces and their children into container
   * 
   * @param allSpaces
   *          the spaces to add
   * @param pCurrentRole
   *          the selected role
   * @param pEditMode
   *          if true plugin is contacted to retrieve existing mapping
   * @param pApplication
   *          the vaadin application
   * @throws PluginManagerException
   *           thrown if errors occured when getting plugin information
   * @throws PluginServiceException
   *           thrown if errors occured when getting plugin information
   */
  public void setApplications(final Map<Space, List<ProjectApplication>> allSpaces,
      final String pCurrentRole, final boolean pEditMode) throws PluginManagerException,
      PluginServiceException
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

      final String spaceItemId = space.getName();
      addItem(spaceItemId);

      getContainerProperty(spaceItemId, RolesMappingItemProperty.CAPTION.getPropertyId()).setValue(
          space.getName());
      getContainerProperty(spaceItemId, RolesMappingItemProperty.DESCRIPTION.getPropertyId()).setValue(
          space.getDescription());
      getContainerProperty(spaceItemId, RolesMappingItemProperty.NODE.getPropertyId()).setValue(space);
      for (final ProjectApplication app : entry.getValue())
      {
        final String appId = app.getPluginInstanceUUID().toString();
        addItem(appId);
        setParent(appId, space.getName());
        setChildrenAllowed(appId, false);
        getContainerProperty(appId, RolesMappingItemProperty.NODE.getPropertyId()).setValue(app);
        getContainerProperty(appId, RolesMappingItemProperty.CAPTION.getPropertyId()).setValue(app.getName());
        getContainerProperty(appId, RolesMappingItemProperty.DESCRIPTION.getPropertyId()).setValue(
            app.getDescription());

        final boolean availablePlugin = MembershipsModule.getPluginsManager().isAvailablePlugin(
            app.getPluginUUID().toString());
        getContainerProperty(appId, RolesMappingItemProperty.AVAILABILITY.getPropertyId()).setValue(
            availablePlugin);
        if (availablePlugin)
        {
          if (ApplicationStatus.ACTIVE.equals(app.getStatus()))
          {
            final PluginService pluginService = MembershipsModule.getPluginsManager().getPluginService(
                app.getPluginUUID().toString());

            getContainerProperty(appId, RolesMappingItemProperty.ROLES.getPropertyId()).setValue(
                pluginService.findRoles());

            if (pEditMode)
            {
              final Map<String, String> rolesMapping = pluginService.getRolesMapping(appId);
              if (rolesMapping.containsKey(pCurrentRole))
              {
                getContainerProperty(appId, RolesMappingItemProperty.ROLE_MAPPED.getPropertyId()).setValue(
                    rolesMapping.get(pCurrentRole));
              }
            }
          }
          if (ApplicationStatus.CREATE_ON_ERROR.equals(app.getStatus()))
          {
            getContainerProperty(appId, RolesMappingItemProperty.ICON.getPropertyId()).setValue(
                new ThemeResource(NovaForgeResources.ICON_BLOCKED_ROUND));
          }
          else
          {
            final byte[] pluginIcon = MembershipsModule.getPluginsManager()
                .getPluginService(app.getPluginUUID().toString()).getPluginIcon();

            if (pluginIcon != null)
            {
              final StreamResource buildImageResource = ResourceUtils.buildImageResource(pluginIcon,
                  app.getName());
              getContainerProperty(appId, RolesMappingItemProperty.ICON.getPropertyId()).setValue(
                  buildImageResource);
            }
          }
        }
        else
        {
          getContainerProperty(appId, RolesMappingItemProperty.ICON.getPropertyId()).setValue(
              new ThemeResource(NovaForgeResources.ICON_ERROR));
        }
      }

    }
    sort(new Object[] { RolesMappingItemProperty.CAPTION.getPropertyId() }, new boolean[] { true });
  }
}
