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
package org.novaforge.forge.ui.applications.internal.client.associations.components;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.CompositionType;
import org.novaforge.forge.core.organization.model.Node;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.plugins.categories.Association;
import org.novaforge.forge.core.plugins.categories.AssociationType;
import org.novaforge.forge.core.plugins.categories.Category;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.ui.applications.internal.client.associations.AssociationsPresenter;
import org.novaforge.forge.ui.applications.internal.client.associations.models.CompatibleComposition;
import org.novaforge.forge.ui.applications.internal.client.global.components.ItemProperty;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This component describes a specific {@link HierarchicalContainer} used to build association tree table.
 * 
 * @author Guillaume Lamirand
 */
public class AssociationsContainer extends HierarchicalContainer
{

  /**
   * Serial version id for Serialization
   */
  private static final long           serialVersionUID = 3439109343778568310L;
  /**
   * The presenter associated
   */
  private final AssociationsPresenter presenter;

  /**
   * Default constructor. It will initialize tree item property
   * 
   * @param pPresenter
   *          represents the associated presenter which will be called on actions
   * @see ItemProperty
   * @see HierarchicalContainer#HierarchicalContainer()
   */
  public AssociationsContainer(final AssociationsPresenter pPresenter)
  {
    super();
    presenter = pPresenter;
    addContainerProperty(ItemProperty.NODE.getPropertyId(), Node.class, null);
    addContainerProperty(ItemProperty.CAPTION.getPropertyId(), String.class, null);
    addContainerProperty(ItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(ItemProperty.ICON.getPropertyId(), Resource.class, null);
    addContainerProperty(ItemProperty.AVAILABILITY.getPropertyId(), Boolean.class, null);
    addContainerProperty(ItemProperty.ENABLE.getPropertyId(), Boolean.class, null);
    addContainerProperty(ItemProperty.SETUP.getPropertyId(), Boolean.class, null);
    addContainerProperty(ItemProperty.COMPATIBLE.getPropertyId(), Boolean.class, null);
    addContainerProperty(ItemProperty.ASSOCIATIONS.getPropertyId(), List.class,
        new ArrayList<CompatibleComposition>());

  }

  /**
   * Will initialize the container with given parameters
   * 
   * @param pProjectApplication
   *          the source {@link ProjectApplication}
   * @param allSpaces
   *          the project {@link Space} and their {@link ProjectApplication}
   * @throws PluginManagerException
   */
  public void setDatas(final ProjectApplication pProjectApplication,
      final Map<Space, List<ProjectApplication>> allSpaces) throws PluginManagerException
  {
    // Clean datasource
    removeAllItems();

    // Browse all project spaces and their applications
    final Set<Entry<Space, List<ProjectApplication>>> spacesWithApp = allSpaces.entrySet();

    for (final Entry<Space, List<ProjectApplication>> entry : spacesWithApp)
    {
      // Add space to datasource
      final Space space = entry.getKey();
      if (RealmType.SYSTEM.equals(space.getRealmType()))
      {
        continue;
      }

      addItem(space.getName());
      getContainerProperty(space.getName(), ItemProperty.CAPTION.getPropertyId()).setValue(space.getName());
      getContainerProperty(space.getName(), ItemProperty.NODE.getPropertyId()).setValue(space);

      // Browse all project applications
      for (final ProjectApplication app : entry.getValue())
      {
        // Do not add application if its the current one
        if (!app.getPluginInstanceUUID().equals(pProjectApplication.getPluginInstanceUUID()))
        {
          // Add applicaton property
          final boolean isAvailable = addProjectApplication(space, app);
          if (isAvailable)
          {
            // Add applicaton's associations property
            addCompatibleAssociation(pProjectApplication, app);
          }
        }
      }

    }
    sort(new Object[] { ItemProperty.CAPTION.getPropertyId() }, new boolean[] { true });

  }

  private boolean addProjectApplication(final Space space, final ProjectApplication app)
      throws PluginManagerException
  {
    final String appId = app.getPluginInstanceUUID().toString();
    addItem(appId);
    setParent(appId, space.getName());
    setChildrenAllowed(appId, false);
    getContainerProperty(appId, ItemProperty.NODE.getPropertyId()).setValue(app);
    getContainerProperty(appId, ItemProperty.CAPTION.getPropertyId()).setValue(app.getName());
    getContainerProperty(appId, ItemProperty.DESCRIPTION.getPropertyId()).setValue(app.getDescription());

    final boolean availablePlugin = ApplicationsModule.getPluginsManager().isAvailablePlugin(
        app.getPluginUUID().toString());
    final boolean active = (ApplicationStatus.ACTIVE.equals(app.getStatus()));
    getContainerProperty(appId, ItemProperty.AVAILABILITY.getPropertyId())
        .setValue(active && availablePlugin);
    if (availablePlugin)
    {
      final byte[] pluginIcon = ApplicationsModule.getPluginsManager()
          .getPluginService(app.getPluginUUID().toString()).getPluginIcon();

      if (pluginIcon != null)
      {
        final StreamResource buildImageResource = ResourceUtils.buildImageResource(pluginIcon, app.getName());
        getContainerProperty(appId, ItemProperty.ICON.getPropertyId()).setValue(buildImageResource);
      }
    }
    else
    {
      getContainerProperty(appId, ItemProperty.ICON.getPropertyId()).setValue(
          new ThemeResource(NovaForgeResources.ICON_ERROR));
    }
    return availablePlugin;
  }

  @SuppressWarnings("unchecked")
  private void addCompatibleAssociation(final ProjectApplication pCurrentApp, final ProjectApplication pApp)
      throws PluginManagerException
  {
    // Retrieve association for current application
    final CategoryDefinitionService currentDefinitionService = ApplicationsModule
        .getCategoryDefinitionService(pCurrentApp.getPluginUUID());
    final List<Association> associations = currentDefinitionService.getAssociations();

    // Retrieve association for other application
    final CategoryDefinitionService otherDefinitionService = ApplicationsModule
        .getCategoryDefinitionService(pApp.getPluginUUID());

    // FIXME Temporarily filter Requirements/Gitlab association
    if (currentDefinitionService.getCategory().equals(Category.REQUIREMENTMANAGEMENT)
        && otherDefinitionService.getCategory().equals(Category.SCM))
    {
      final PluginMetadata pluginMetadata = ApplicationsModule.getPluginsManager().getPluginMetadataByUUID(
          pApp.getPluginUUID().toString());
      if (pluginMetadata.getType().equals("Gitlab"))
      {
        return;
      }
    }

    final List<Association> otherAssociations = otherDefinitionService.getAssociations();

    final String appId = pApp.getPluginInstanceUUID().toString();
    for (final Association association : associations)
    {
      final List<Association> compatibleAssociations = currentDefinitionService
          .getCompatibleAssociations(association);
      if ((AssociationType.NOTIFICATION.equals(association.getType())))
      {
        final List<Association> actions = getAssociationsByType(AssociationType.ACTION, otherAssociations);
        for (final Association action : actions)
        {
          final List<CompatibleComposition> targeAssociations = (List<CompatibleComposition>) getContainerProperty(
              appId, ItemProperty.ASSOCIATIONS.getPropertyId()).getValue();
          targeAssociations.add(new CompatibleComposition(pApp.getPluginInstanceUUID(),
              CompositionType.NOTIFICATION, association, action));
          getContainerProperty(appId, ItemProperty.SETUP.getPropertyId()).setValue(true);
          getContainerProperty(appId, ItemProperty.COMPATIBLE.getPropertyId()).setValue(true);
        }
      }
      else if ((AssociationType.REQUEST.equals(association.getType())))
      {
        final List<Association> replies = getAssociationsByType(AssociationType.REPLY, otherAssociations);
        for (final Association compatible : compatibleAssociations)
        {
          for (final Association reply : replies)
          {
            if (compatible.equals(reply))
            {
              final List<CompatibleComposition> targeAssociations = (List<CompatibleComposition>) getContainerProperty(
                  appId, ItemProperty.ASSOCIATIONS.getPropertyId()).getValue();
              targeAssociations.add(new CompatibleComposition(pApp.getPluginInstanceUUID(),
                  CompositionType.REQUEST_DATA, association, reply));
              getContainerProperty(appId, ItemProperty.COMPATIBLE.getPropertyId()).setValue(true);
              break;
            }
          }

        }
      }
      else if ((AssociationType.SEND.equals(association.getType())))
      {
        final List<Association> receives = getAssociationsByType(AssociationType.RECEIVE, otherAssociations);
        for (final Association compatible : compatibleAssociations)
        {
          for (final Association receive : receives)
          {
            if (compatible.equals(receive))
            {
              final List<CompatibleComposition> targeAssociations = (List<CompatibleComposition>) getContainerProperty(
                  appId, ItemProperty.ASSOCIATIONS.getPropertyId()).getValue();
              targeAssociations.add(new CompatibleComposition(pApp.getPluginInstanceUUID(),
                  CompositionType.SEND_DATA, association, receive));
              getContainerProperty(appId, ItemProperty.COMPATIBLE.getPropertyId()).setValue(true);
              break;
            }
          }

        }

      }
    }

  }

  private List<Association> getAssociationsByType(final AssociationType pType,
      final List<Association> pAssociations)
  {
    final List<Association> result = new ArrayList<Association>();
    for (final Association an : pAssociations)
    {
      if (an.getType().equals(pType))
      {
        result.add(an);
      }
    }
    return result;
  }

  /**
   * Will set up existing {@link Composition} into container
   * 
   * @param pCompositions
   *          the existing {@link Composition}
   */
  public void setExistingCompositions(final List<Composition> pCompositions)
  {
    for (final Composition composition : pCompositions)
    {
      final String appId = composition.getTarget().getPluginInstanceUUID().toString();
      final Item item = getItem(appId);
      if (item != null)
      {
        getContainerProperty(appId, ItemProperty.ENABLE.getPropertyId()).setValue(true);
      }
    }
  }

}
