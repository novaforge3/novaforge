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
package org.novaforge.forge.ui.portal.internal.privatemodule.client.project.components;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalSpace;
import org.novaforge.forge.portal.models.PortalURI;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.List;
import java.util.UUID;

/**
 * This component describes a specific {@link HierarchicalContainer} used to build project navigation tree.
 * 
 * @author Guillaume Lamirand
 */
public class NavigationTreeContainer extends HierarchicalContainer
{

  /**
   * Serial version id for Serialization
   */
  private static final long serialVersionUID = 3439109343778568310L;

  /**
   * Default constructor. It will initialize tree item property
   * 
   * @see TreeItemProperty
   * @see HierarchicalContainer#HierarchicalContainer()
   */
  public NavigationTreeContainer()
  {
    super();
    addContainerProperty(TreeItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(TreeItemProperty.UUID.getPropertyId(), UUID.class, null);
    addContainerProperty(TreeItemProperty.CAPTION.getPropertyId(), String.class, null);
    addContainerProperty(TreeItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(TreeItemProperty.MODULE_ID.getPropertyId(), String.class, null);
    addContainerProperty(TreeItemProperty.URL.getPropertyId(), String.class, null);
    addContainerProperty(TreeItemProperty.ICON.getPropertyId(), Resource.class, null);
    addContainerProperty(TreeItemProperty.STATUS.getPropertyId(), ApplicationStatus.class, null);
    addContainerProperty(TreeItemProperty.AVAILABILITY.getPropertyId(), Boolean.class, null);
    addContainerProperty(TreeItemProperty.INTERNAL.getPropertyId(), Boolean.class, null);

  }

  /**
   * Add spaces and their children into container
   * 
   * @param pSpaces
   *          the spaces to add
   * @param pAapplication
   *          the vaadin application
   */
  public void setSpaces(final List<PortalSpace> pSpaces)
  {
    removeAllItems();
    for (final PortalSpace portalSpace : pSpaces)
    {
      // Don't manage System spaces
      if (RealmType.SYSTEM.equals(portalSpace.getRealmType()))
      {
        continue;
      }
      final List<PortalApplication> applications = portalSpace.getApplications();
      if (!applications.isEmpty())
      {
        final String spaceId = portalSpace.getId();
        addItem(spaceId);
        getContainerProperty(spaceId, TreeItemProperty.CAPTION.getPropertyId()).setValue(
            portalSpace.getName());
        getContainerProperty(spaceId, TreeItemProperty.DESCRIPTION.getPropertyId()).setValue(
            portalSpace.getDescription());
        getContainerProperty(spaceId, TreeItemProperty.ID.getPropertyId()).setValue(portalSpace.getId());
        for (final PortalApplication app : applications)
        {
          if ((!ApplicationStatus.CREATE_ON_ERROR.equals(app.getStatus())) && (!ApplicationStatus.DELETE_ON_ERROR
                                                                                    .equals(app.getStatus()))
                  && (!ApplicationStatus.DELETE_IN_PROGRESS.equals(app.getStatus())))
          {
            final String appId = app.getUniqueId().toString();
            addItem(appId);
            setParent(appId, spaceId);
            setChildrenAllowed(appId, false);
            getContainerProperty(appId, TreeItemProperty.ID.getPropertyId()).setValue(app.getId());
            getContainerProperty(appId, TreeItemProperty.UUID.getPropertyId()).setValue(app.getUniqueId());
            getContainerProperty(appId, TreeItemProperty.CAPTION.getPropertyId()).setValue(app.getName());
            getContainerProperty(appId, TreeItemProperty.DESCRIPTION.getPropertyId()).setValue(
                app.getDescription());
            if (app.getStatus() != null)
            {
              getContainerProperty(appId, TreeItemProperty.STATUS.getPropertyId()).setValue(app.getStatus());
            }
            getContainerProperty(appId, TreeItemProperty.AVAILABILITY.getPropertyId()).setValue(
                app.isAvailable());
            if (app.isAvailable())
            {
              if (app.getIcon() != null)
              {
                getContainerProperty(appId, TreeItemProperty.ICON.getPropertyId()).setValue(app.getIcon());
              }
              final PortalURI portalURI = app.getPortalURI();
              if (portalURI != null)
              {
                if (portalURI.isInternalModule())
                {
                  getContainerProperty(appId, TreeItemProperty.INTERNAL.getPropertyId()).setValue(true);
                  getContainerProperty(appId, TreeItemProperty.MODULE_ID.getPropertyId()).setValue(
                      portalURI.getModuleId());
                }
                else
                {
                  getContainerProperty(appId, TreeItemProperty.URL.getPropertyId()).setValue(
                      portalURI.getAbsoluteURL().toExternalForm());
                }
              }
            }
            else
            {
              getContainerProperty(appId, TreeItemProperty.ICON.getPropertyId()).setValue(
                  new ThemeResource(NovaForgeResources.ICON_ERROR));
            }
          }
        }
      }
    }
    sort(new Object[] { TreeItemProperty.CAPTION.getPropertyId() }, new boolean[] { true });
  }
}
