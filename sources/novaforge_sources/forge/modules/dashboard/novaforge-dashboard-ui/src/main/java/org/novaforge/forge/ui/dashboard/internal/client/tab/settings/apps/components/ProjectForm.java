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
package org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps.components;

import com.vaadin.data.Item;
import com.vaadin.ui.Form;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.dashboard.internal.client.container.ApplicationContainer;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.vaadin.addon.itemlayout.event.ItemClickListener;
import org.vaadin.addon.itemlayout.grid.ItemGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
@SuppressWarnings("deprecation")
public class ProjectForm extends Form
{
  /**
   * 
   */
  private static final long          serialVersionUID = -6411972554672932449L;
  private final ApplicationContainer container;
  private final ItemGrid             itemGrid;
  private String                     projectId;

  /**
   * Default constructor
   */
  public ProjectForm()
  {
    super();
    // Init the form
    setImmediate(true);
    setInvalidCommitted(false);
    setFooter(null);
    setSizeFull();

    // Init item grid
    container = new ApplicationContainer();
    itemGrid = new ItemGrid();
    itemGrid.setColumns(6);
    itemGrid.addStyleName(NovaForge.APPLICATION_ITEM_GRID);
    itemGrid.setImmediate(true);
    itemGrid.setSelectable(true);
    itemGrid.setMultiSelect(true);
    itemGrid.setContainerDataSource(container);
    itemGrid.setItemGenerator(new ApplicationItemGenerator());

    getLayout().addComponent(itemGrid);
  }

  /**
   * Adds the item click listener.
   * 
   * @param pListener
   *          the Listener to be added.
   */
  public void addItemClickListener(final ItemClickListener pListener)
  {
    itemGrid.addItemClickListener(pListener);
  }

  /**
   * Removes the item click listener.
   * 
   * @param pListener
   *          the Listener to be removed.
   */
  public void removeItemClickListener(final ItemClickListener pListener)
  {
    itemGrid.removeItemClickListener(pListener);
  }

  /**
   * Sets the project detail and applications available
   * 
   * @param pProject
   *          the project to set
   * @param pApps
   *          the applications to set
   */
  public void setData(final Project pProject, final List<ProjectApplication> pApps)
  {
    projectId = pProject.getElementId();
    setCaption(pProject.getName());
    setIcon(ResourceUtils.buildImageResource(pProject.getImage().getFile(), projectId));
    container.setApplications(pProject, pApps);
  }

  /**
   * Returns the project id attached to this component
   * 
   * @return the projectId
   */
  public String getProjectId()
  {
    return projectId;
  }

  /**
   * Returns list of selected applications id
   * 
   * @return list of selected applications id
   */
  public List<UUID> getSelectedApplications()
  {
    final List<UUID> selectedUUIDs = new ArrayList<UUID>();
    final Set<String> selectedItems = itemGrid.getSelectedItems();
    for (final String itemID : selectedItems)
    {
      selectedUUIDs.add(UUID.fromString(itemID));
    }
    return selectedUUIDs;
  }

  /**
   * Returns true if container has visible application depending on current filters setted
   * 
   * @return true if container has visible appication depending on current filters setted
   */
  public boolean hasVisibleApplication()
  {
    return !container.getItemIds().isEmpty();
  }

  /**
   * Returns the {@link Item}
   * 
   * @param pUUID
   *          application it
   * @return the {@link Item} found
   */
  public Item getApplicationItem(final UUID pUUID)
  {
    return container.getItem(pUUID);
  }

  /**
   * Returns the {@link ApplicationContainer}
   * 
   * @return the container
   */
  public ApplicationContainer getContainer()
  {
    return container;
  }

  /**
   * Returns if the grid is multi selectable
   *
   * @return the multiselectable value
   */
  public boolean isMultiSelectable()
  {
    return itemGrid.getState().multiSelectable;
  }

  /**
   * Sets if the grid is multi selectable
   *
   * @param pState
   *          true to enable multi selectable item
   */
  public void setMultiSelectable(final boolean pState)
  {
    itemGrid.setMultiSelect(pState);
  }

  /**
   * Select or unselect application
   * 
   * @param pAppId
   *          the application id
   * @param pSelected
   *          true to select, false unselect
   * @see org.novaforge.forge.ui.portal.client.component.CustomItemGrid#selectItem(java.lang.Object, boolean)
   */
  public void selectApplication(final UUID pAppId, final boolean pSelected)
  {
    if (pSelected)
    {
      itemGrid.selectItem(pAppId.toString(), false);
    }
    else
    {

      itemGrid.unselectItem(pAppId.toString(), false);
    }
  }
}
