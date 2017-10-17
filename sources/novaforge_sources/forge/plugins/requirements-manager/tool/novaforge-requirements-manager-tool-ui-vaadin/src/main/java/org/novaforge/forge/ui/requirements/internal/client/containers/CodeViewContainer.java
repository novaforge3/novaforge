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
package org.novaforge.forge.ui.requirements.internal.client.containers;

import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import org.novaforge.forge.tools.requirements.common.model.IResourceOOCode;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Gauthier Cart
 */
public class CodeViewContainer extends HierarchicalContainer
{

  /**
   * Default serial UID
   */
  private static final long serialVersionUID = -5580498840453825418L;

  /**
   * Default constructor. It will initialize user item property
   * 
   * @see UserItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public CodeViewContainer()
  {
    super();
    addContainerProperty(CodeViewItemProperty.VERSION_IN_REQUIREMENT.getPropertyId(), Integer.class, null);
    addContainerProperty(CodeViewItemProperty.REQUIREMENT_NAME.getPropertyId(), String.class, null);
    addContainerProperty(CodeViewItemProperty.REQUIREMENT_DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(CodeViewItemProperty.REQUIREMENT_TYPE.getPropertyId(), String.class, null);
    addContainerProperty(CodeViewItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(CodeViewItemProperty.CLASS.getPropertyId(), List.class, null);
    addContainerProperty(CodeViewItemProperty.VERSION_IN_CODE.getPropertyId(), Integer.class, null);
    addContainerProperty(CodeViewItemProperty.STATUS.getPropertyId(), RequirementStatus.class, null);
    addContainerProperty(TestViewItemProperty.STATUSID.getPropertyId(), String.class, null);
    addContainerProperty(CodeViewItemProperty.REPOSITORY.getPropertyId(), String.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Set the given requirements set into container
   * 
   * @param pRequirement
   *          the {@link Set}<{@linkIRequirement}> to set
   */
  public void setRequirements(final Set<IRequirement> pRequirement)
  {
    removeAllContainerFilters();
    removeAllItems();

    for (IRequirement requirement : pRequirement)
    {
      final Object itemID = addItem(requirement, null);
      setChildren(itemID, requirement.getChildren());
    }
  }

  private void setChildren(final Object pParentId, final Set<IRequirement> pChildren)
  {
    if (pChildren != null && !pChildren.isEmpty())
    {
      setChildrenAllowed(pParentId, true);
      for (IRequirement child : pChildren)
      {
        final Object childId = addItem(child, pParentId);
        setParent(childId, pParentId);
        setChildren(childId, child.getChildren());
      }
    }
    else
    {
      setChildrenAllowed(pParentId, false);
    }
  }

  private Object addItem(final IRequirement requirement, final Object pParentId)
  {
    final Object itemID = addItem();
    getItem(itemID).getItemProperty(CodeViewItemProperty.ID.getPropertyId()).setValue(
        requirement.getId().toString());
    getItem(itemID).getItemProperty(CodeViewItemProperty.REQUIREMENT_NAME.getPropertyId()).setValue(
        requirement.getName());
    getItem(itemID).getItemProperty(CodeViewItemProperty.REQUIREMENT_DESCRIPTION.getPropertyId()).setValue(
        requirement.getDescription());
    getItem(itemID).getItemProperty(CodeViewItemProperty.REQUIREMENT_TYPE.getPropertyId()).setValue(
        requirement.getType());
    getItem(itemID).getItemProperty(CodeViewItemProperty.VERSION_IN_REQUIREMENT.getPropertyId()).setValue(
        requirement.findLastRequirementVersion().getCurrentVersion());

    // Careful ! The version in requirement column is the only one to be a simple column, not an homebrew
    // column. So it's the only one that can trigger the valueChangerLister.
    // It would be better to add a listener on the other visible column (name,status, class)
    getItem(itemID).getItemProperty(CodeViewItemProperty.VERSION_IN_REQUIREMENT.getPropertyId()).setValue(
        requirement.findLastRequirementVersion().getCurrentVersion());

    // Build the list of classes linked with the requirements
    final IRequirementVersion versionInCode = getLatestVersionInCode(requirement.getRequirementVersions());
    if (versionInCode != null)
    {
      final List<String> resourcesName = new ArrayList<String>();
      for (IResourceOOCode resourceOOCode : versionInCode.getResourcesOOCode())
      {
        resourcesName.add(resourceOOCode.getName());
      }
      getItem(itemID).getItemProperty(CodeViewItemProperty.CLASS.getPropertyId()).setValue(resourcesName);
      getItem(itemID).getItemProperty(CodeViewItemProperty.VERSION_IN_CODE.getPropertyId()).setValue(
          versionInCode.getCurrentVersion());
      if (versionInCode.getCurrentVersion() >= requirement.findLastRequirementVersion().getCurrentVersion())
      {
        getItem(itemID).getItemProperty(CodeViewItemProperty.STATUS.getPropertyId()).setValue(
            RequirementStatus.CODED);
        getItem(itemID).getItemProperty(CodeViewItemProperty.STATUSID.getPropertyId()).setValue(
            RequirementStatus.CODED.getId());
      }
      else
      {
        getItem(itemID).getItemProperty(CodeViewItemProperty.STATUS.getPropertyId()).setValue(
            RequirementStatus.NOT_UPDATED);
        getItem(itemID).getItemProperty(CodeViewItemProperty.STATUSID.getPropertyId()).setValue(
            RequirementStatus.NOT_UPDATED.getId());
      }
    }
    else
    {
      getItem(itemID).getItemProperty(CodeViewItemProperty.STATUS.getPropertyId()).setValue(
          RequirementStatus.NOT_CODED);
      getItem(itemID).getItemProperty(CodeViewItemProperty.STATUSID.getPropertyId()).setValue(
          RequirementStatus.NOT_CODED.getId());
    }
    if (requirement.getDirectory() == null && pParentId != null)
    {
      getItem(itemID).getItemProperty(CodeViewItemProperty.REPOSITORY.getPropertyId()).setValue(
          getItem(pParentId).getItemProperty(CodeViewItemProperty.REPOSITORY.getPropertyId()).getValue());
    }
    else
    {
      getItem(itemID).getItemProperty(CodeViewItemProperty.REPOSITORY.getPropertyId()).setValue(
          requirement.getDirectory().getRepository().getURI());
    }

    return itemID;
  }

  private IRequirementVersion getLatestVersionInCode(final Set<IRequirementVersion> pVersions)
  {
    IRequirementVersion result = null;
    for (IRequirementVersion version : pVersions)
    {
      Set<IResourceOOCode> resourcesOOCode = version.getResourcesOOCode();
      if (resourcesOOCode != null && !resourcesOOCode.isEmpty()
          && (result == null || result.getCurrentVersion() < version.getCurrentVersion()))
      {
        result = version;
      }
    }
    return result;
  }

}
