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
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.ui.portal.data.util.DefaultComparator;
import org.novaforge.forge.ui.requirements.internal.client.testview.components.TestRequirementDTO;

import java.util.Set;

/** @author Gauthier Cart */
public class TestViewContainer extends HierarchicalContainer
{

  /** Default Serial UID */
  private static final long serialVersionUID = -5580498840453825418L;

  /**
   * Default constructor. It will initialize TestView item property
   * 
   * @see TestViewItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public TestViewContainer()
  {
    super();
    addContainerProperty(TestViewItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(TestViewItemProperty.REQUIREMENT_NAME.getPropertyId(), String.class, null);
    addContainerProperty(TestViewItemProperty.REQUIREMENT_DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(TestViewItemProperty.REQUIREMENT_TYPE.getPropertyId(), String.class, null);
    addContainerProperty(TestViewItemProperty.VERSION_IN_REQUIREMENT.getPropertyId(), Integer.class, null);
    addContainerProperty(TestViewItemProperty.TEST_NAME.getPropertyId(), String.class, null);
    addContainerProperty(TestViewItemProperty.VERSION_IN_TEST.getPropertyId(), Integer.class, null);
    addContainerProperty(TestViewItemProperty.STATUSID.getPropertyId(), String.class, null);
    addContainerProperty(TestViewItemProperty.STATUS.getPropertyId(), RequirementStatus.class, null);
    addContainerProperty(TestViewItemProperty.REPOSITORY.getPropertyId(), String.class, null);

    setItemSorter(new DefaultItemSorter(new DefaultComparator()));
  }

  /**
   * Clean the container and add {@link IRequirement}
   * 
   * @param pRepository
   *          the source repository of the requirements
   * @param pTestRequirements
   *          Set<IRequirement> {@link IRequirement} to add
   */
  public void setRequirements(final IRepository pRepository, final Set<TestRequirementDTO> pTestRequirements)
  {
    removeAllContainerFilters();
    removeAllItems();
    for (final TestRequirementDTO testRequirement : pTestRequirements)
    {
      final Object itemId = addItem(testRequirement, pRepository);

      setChildren(pRepository, itemId, testRequirement.getChildren());
    }
    sort(new Object[] { TestViewItemProperty.REQUIREMENT_NAME.getPropertyId() }, new boolean[] { true });
  }

  /**
   * Add {@link IRequirement} into an existing container
   * 
   * @param pSourceRepository
   *          the source repository of the requirements
   * @param pRequirementsToAdd
   *          Set<IRequirement> {@link IRequirement} to add
   */
  public void addAllRequirements(final IRepository pSourceRepository,
      final Set<TestRequirementDTO> pRequirementsToAdd)
  {
    for (final TestRequirementDTO requirementToAdd : pRequirementsToAdd)
    {
      final Object itemId = addItem(requirementToAdd, pSourceRepository);
      setChildren(pSourceRepository, itemId, requirementToAdd.getChildren());
    }
    sort(new Object[] { TestViewItemProperty.REQUIREMENT_NAME.getPropertyId() }, new boolean[] { true });
  }

  private void setChildren(final IRepository pRepository, final Object pParentId,
      final Set<TestRequirementDTO> pChildren)
  {
    if ((pChildren != null) && (!pChildren.isEmpty()))
    {
      setChildrenAllowed(pParentId, true);
      for (final TestRequirementDTO child : pChildren)
      {
        final Object childId = addItem(child, pRepository);
        setParent(childId, pParentId);
        setChildren(pRepository, childId, child.getChildren());
      }
    }
    else
    {
      setChildrenAllowed(pParentId, false);
    }
  }

  @SuppressWarnings("unchecked")
  private Object addItem(final TestRequirementDTO pTestRequirement, final IRepository pRepository)
  {
    final Object itemId = addItem();
    final Item item = getItem(itemId);
    item.getItemProperty(TestViewItemProperty.ID.getPropertyId()).setValue(
        pTestRequirement.getRequirement().getId().toString());
    item.getItemProperty(TestViewItemProperty.REQUIREMENT_NAME.getPropertyId()).setValue(
        pTestRequirement.getRequirement().getName());
    item.getItemProperty(TestViewItemProperty.REQUIREMENT_DESCRIPTION.getPropertyId()).setValue(
        pTestRequirement.getRequirement().getDescription());
    item.getItemProperty(TestViewItemProperty.REQUIREMENT_TYPE.getPropertyId()).setValue(
        pTestRequirement.getRequirement().getType());
    item.getItemProperty(TestViewItemProperty.VERSION_IN_REQUIREMENT.getPropertyId()).setValue(
        pTestRequirement.getRequirementVersion().getCurrentVersion());
    if (pTestRequirement.getTest() != null)
    {
      item.getItemProperty(TestViewItemProperty.TEST_NAME.getPropertyId()).setValue(
          pTestRequirement.getTest().getReference());
      if (pTestRequirement.getRequirementVersionInTest() != null)
      {
        item.getItemProperty(TestViewItemProperty.VERSION_IN_TEST.getPropertyId()).setValue(
            pTestRequirement.getRequirementVersionInTest().getCurrentVersion());
        if (pTestRequirement.getRequirementVersion().getCurrentVersion() == pTestRequirement
            .getRequirementVersionInTest().getCurrentVersion())
        {
          item.getItemProperty(TestViewItemProperty.STATUS.getPropertyId())
              .setValue(RequirementStatus.TESTED);
          item.getItemProperty(TestViewItemProperty.STATUSID.getPropertyId()).setValue(
              RequirementStatus.TESTED.getId());
        }
        else
        {
          item.getItemProperty(TestViewItemProperty.STATUS.getPropertyId()).setValue(
              RequirementStatus.NOT_UPDATED);
          item.getItemProperty(TestViewItemProperty.STATUSID.getPropertyId()).setValue(
              RequirementStatus.NOT_UPDATED.getId());
        }
      }
    }
    else
    {
      item.getItemProperty(TestViewItemProperty.STATUS.getPropertyId())
          .setValue(RequirementStatus.NOT_TESTED);
      item.getItemProperty(TestViewItemProperty.STATUSID.getPropertyId()).setValue(
          RequirementStatus.NOT_TESTED.getId());
    }
    item.getItemProperty(TestViewItemProperty.REPOSITORY.getPropertyId()).setValue(pRepository.getURI());

    return itemId;
  }
}
