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

import com.vaadin.data.Item;
import com.vaadin.data.util.DefaultItemSorter;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.UI;
import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.IDirectory;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.requirements.internal.client.repository.components.RepositoryType;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.io.File;
import java.util.Set;

public class TreeContainer extends HierarchicalContainer
{

  /**
	 * 
	 */
  private static final long serialVersionUID = -39499472696469287L;

  /**
   * Default constructor. It will initialize Requirement item property
   * 
   * @see RequirementItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public TreeContainer()
  {
    super();
    addContainerProperty(TreeItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(TreeItemProperty.LABEL.getPropertyId(), String.class, null);
    addContainerProperty(TreeItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(TreeItemProperty.REPOSITORY.getPropertyId(), String.class, null);
    addContainerProperty(TreeItemProperty.ISREQUIREMENT.getPropertyId(), Boolean.class, null);
    addContainerProperty(TreeItemProperty.PATH.getPropertyId(), String.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Add {@link IRepository} into container
   * 
   * @param pRequests
   *          {@link IRepository} to add
   */
  public void setRepositories(final Set<IRepository> pRepositories)
  {
    removeAllContainerFilters();
    removeAllItems();

    for (final IRepository repository : pRepositories)
    {
      if (RepositoryType.OBEO.equals(RepositoryType.get(repository.getType())))
      {
        addObeoRepository(repository);
      }
      else if (RepositoryType.EXCEL.equals(RepositoryType.get(repository.getType())))
      {
        addExcelRepository(repository);
      }
    }

    sort(new Object[] { TreeItemProperty.LABEL.getPropertyId() }, new boolean[] { true });
  }

  private void addObeoRepository(final IRepository pRepository)
  {

    try
    {
      final Set<IDirectory> directories = RequirementsModule.getRequirementManagerService()
          .loadAllRootDirectoryTreesByRepository(pRepository);
      if ((directories != null) && !directories.isEmpty())
      {
        final String itemID = pRepository.getURI();
        final Item repositoryItem = addItem(itemID);
        getItem(itemID).getItemProperty(TreeItemProperty.LABEL.getPropertyId())
            .setValue(pRepository.getURI());
        getItem(itemID).getItemProperty(TreeItemProperty.DESCRIPTION.getPropertyId()).setValue(
            pRepository.getDescription());
        getItem(itemID).getItemProperty(TreeItemProperty.ID.getPropertyId()).setValue(pRepository.getURI());
        getItem(itemID).getItemProperty(TreeItemProperty.ISREQUIREMENT.getPropertyId()).setValue(false);
        getItem(itemID).getItemProperty(TreeItemProperty.PATH.getPropertyId()).setValue(pRepository.getURI());

        setChildrenItemsForDirectories(pRepository.getProject().getProjectId(), repositoryItem, directories);
      }
    }
    catch (final RequirementManagerServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }

  }

  private void addExcelRepository(final IRepository pRepository)
  {

    try
    {
      final Set<IDirectory> directories = RequirementsModule.getRequirementManagerService()
          .loadAllRootDirectoryTreesByRepository(pRepository);
      if ((directories != null) & !directories.isEmpty())
      {
        final String itemID = pRepository.getURI();
        final Item repositoryItem = addItem(itemID);
        // Substrings only to get Excel file name
        String uriClean = pRepository.getURI();
        final File excelFile = new File(itemID);
        if ((excelFile != null) && !StringUtils.isBlank(excelFile.getName()))
        {
          uriClean = excelFile.getName();
        }
        getItem(itemID).getItemProperty(TreeItemProperty.LABEL.getPropertyId()).setValue(uriClean);
        getItem(itemID).getItemProperty(TreeItemProperty.DESCRIPTION.getPropertyId()).setValue(
            pRepository.getDescription());
        getItem(itemID).getItemProperty(TreeItemProperty.ID.getPropertyId()).setValue(
            pRepository.getId().toString());
        getItem(itemID).getItemProperty(TreeItemProperty.REPOSITORY.getPropertyId()).setValue(
            pRepository.getURI());
        getItem(itemID).getItemProperty(TreeItemProperty.ISREQUIREMENT.getPropertyId()).setValue(false);
        getItem(itemID).getItemProperty(TreeItemProperty.PATH.getPropertyId()).setValue(uriClean);

        for (final IDirectory rootDir : directories)
        {
          setChildrenItemsForExcelRepository(pRepository.getProject().getProjectId(), itemID,
              rootDir.getRequirements());
        }
      }
    }
    catch (final RequirementManagerServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  private void setChildrenItemsForExcelRepository(final String pRepositoryId, final Object pParentItemId,
      final Set<IRequirement> pRequirements)
  {
    if ((pRequirements != null) && !pRequirements.isEmpty())
    {
      setChildrenAllowed(pParentItemId, true);
      for (final IRequirement requirement : pRequirements)
      {
        // final String requirementID = requirement.getId().toString();
        final Object requirementID = addItem();
        getItem(requirementID).getItemProperty(TreeItemProperty.ID.getPropertyId()).setValue(
            requirement.getId().toString());
        getItem(requirementID).getItemProperty(TreeItemProperty.LABEL.getPropertyId()).setValue(
            requirement.getName());
        getItem(requirementID).getItemProperty(TreeItemProperty.DESCRIPTION.getPropertyId()).setValue(
            requirement.getRationale());
        getItem(requirementID).getItemProperty(TreeItemProperty.REPOSITORY.getPropertyId()).setValue(
            pRepositoryId);
        getItem(requirementID).getItemProperty(TreeItemProperty.ISREQUIREMENT.getPropertyId()).setValue(true);
        getItem(requirementID).getItemProperty(TreeItemProperty.PATH.getPropertyId()).setValue(
            getItem(pParentItemId).getItemProperty(TreeItemProperty.PATH.getPropertyId()).getValue()
                + requirement.getName());

        setParent(requirementID, pParentItemId);

        setChildrenItemsForExcelRepository(pRepositoryId, requirementID, requirement.getChildren());
      }
    }
    else
    {
      setChildrenAllowed(pParentItemId, false);
    }

  }

  /**
   * Set the children items for a {@link Set} of {@link IDirectory}
   * 
   * @param pProjectId
   *          The project ID
   * @param pParentItemId
   *          The parent id of the children to set
   * @param pDirectories
   *          The children items
   */
  private void setChildrenItemsForDirectories(final String pProjectId, final Item pParentItem,
      final Set<IDirectory> pDirectories)
  {
    if ((pDirectories != null) && !pDirectories.isEmpty())
    {
      setChildrenAllowed(pParentItem.getItemProperty(TreeItemProperty.ID.getPropertyId()).getValue(), true);
    }
    else
    {
      setChildrenAllowed(pParentItem.getItemProperty(TreeItemProperty.ID.getPropertyId()).getValue(), false);
    }
    for (final IDirectory directory : pDirectories)
    {
      // Add directory
      final String directoryItemID = directory.getReference();
      final Item directoryItem = addItem(directoryItemID);
      getItem(directoryItemID).getItemProperty(TreeItemProperty.LABEL.getPropertyId()).setValue(
          directory.getName());
      getItem(directoryItemID).getItemProperty(TreeItemProperty.ID.getPropertyId()).setValue(directoryItemID);
      getItem(directoryItemID).getItemProperty(TreeItemProperty.DESCRIPTION.getPropertyId()).setValue(
          directory.getDescription());
      getItem(directoryItemID).getItemProperty(TreeItemProperty.REPOSITORY.getPropertyId()).setValue(
          pProjectId);
      getItem(directoryItemID).getItemProperty(TreeItemProperty.ISREQUIREMENT.getPropertyId())
          .setValue(false);
      getItem(directoryItemID).getItemProperty(TreeItemProperty.PATH.getPropertyId())
          .setValue(
              pParentItem.getItemProperty(TreeItemProperty.PATH.getPropertyId()).getValue()
                  + directory.getName());
      // Define parent for this item
      setParent(directoryItemID, pParentItem.getItemProperty(TreeItemProperty.ID.getPropertyId()).getValue());
      // Add children directories
      setChildrenItemsForDirectories(pProjectId, directoryItem, directory.getChildrenDirectories());
      // Add children requirements
      final Set<IRequirement> requirements = directory.getRequirements();
      if (((requirements != null) && !requirements.isEmpty())
          || ((directory.getChildrenDirectories() != null) && !directory.getChildrenDirectories().isEmpty()))
      {
        setChildrenAllowed(directoryItemID, true);
      }
      else
      {
        setChildrenAllowed(directoryItemID, false);
      }
      for (final IRequirement requirement : requirements)
      {
        final String requirementID = requirement.getId().toString();
        addItem(requirementID);
        getItem(requirementID).getItemProperty(TreeItemProperty.ID.getPropertyId()).setValue(requirementID);
        getItem(requirementID).getItemProperty(TreeItemProperty.LABEL.getPropertyId()).setValue(
            requirement.getName());
        getItem(requirementID).getItemProperty(TreeItemProperty.DESCRIPTION.getPropertyId()).setValue(
            requirement.getRationale());
        getItem(requirementID).getItemProperty(TreeItemProperty.REPOSITORY.getPropertyId()).setValue(
            pProjectId);
        getItem(requirementID).getItemProperty(TreeItemProperty.ISREQUIREMENT.getPropertyId()).setValue(true);
        getItem(requirementID).getItemProperty(TreeItemProperty.PATH.getPropertyId()).setValue(
            directoryItem.getItemProperty(TreeItemProperty.PATH.getPropertyId()).getValue()
                + requirement.getName());

        setParent(requirementID, directoryItemID);
        setChildrenAllowed(requirementID, false);

      }
    }
  }
}
