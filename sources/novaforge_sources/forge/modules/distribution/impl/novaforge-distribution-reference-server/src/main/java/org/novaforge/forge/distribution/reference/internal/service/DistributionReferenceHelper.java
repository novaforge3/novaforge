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
package org.novaforge.forge.distribution.reference.internal.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.novaforge.forge.core.plugins.data.ActionType;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.data.ItemReferenceDTO;
import org.novaforge.forge.distribution.reference.model.ApplicationItemReferences;
import org.novaforge.forge.distribution.register.domain.ForgeDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author salvat-a
 */
public class DistributionReferenceHelper
{

	/**
	 * @param forgeDTO
	 *          the DTO object containing the forge configuration
	 * @return the list of the forge's children
	 */
	static Set<ForgeDTO> getChildrenForges(final ForgeDTO forgeDTO)
	{
		final Set<ForgeDTO> children = forgeDTO.getChildren();
		final Set<ForgeDTO> subchildren = new HashSet<ForgeDTO>();
		for (final ForgeDTO childDTO : children)
		{
			subchildren.addAll(getChildrenForges(childDTO));
		}
		children.addAll(subchildren);
		return children;
	}

	/**
	 * @param applicationUri
	 *          the application URI
	 * @param itemReferences
	 *          the list of application item references
	 * @return the application item references object matching the input URI
	 */
	static ApplicationItemReferences findApplicationItemReferences(final String applicationUri,
	    final List<ApplicationItemReferences> itemReferences)
	{
		if ((applicationUri == null) || (itemReferences == null))
		{
			return null;
		}

		return (ApplicationItemReferences) CollectionUtils.find(itemReferences, new Predicate()
		{
			@Override
			public boolean evaluate(final Object o)
			{
				final ApplicationItemReferences elem = (ApplicationItemReferences) o;

				return applicationUri.equalsIgnoreCase(elem.getNodeUri());
			}
		});
	}

	/**
	 * Makes the diff between the application items reference and application items target.
	 * 
	 * @param ref
	 *          the application items reference.
	 * @param target
	 *          the application items target.
	 * @return the list of item DTO containing the item reference and an action.
	 */
	static List<ItemDTO> makeDiffItems(final ApplicationItemReferences ref,
	    final ApplicationItemReferences target)
	{
		final List<ItemDTO> diff = new ArrayList<ItemDTO>();

		if ((ref == null) || (target == null))
		{
			return diff;
		}

		final List<ItemReferenceDTO> referenceDTOs = ref.getItemReferences();
		final List<ItemReferenceDTO> targetDTOs = target.getItemReferences();

		diff.addAll(makeToDeleteItems(referenceDTOs, targetDTOs));

		for (final ItemReferenceDTO referenceDTO : referenceDTOs)
		{
			final String referenceId = referenceDTO.getReferenceId();
			final ItemReferenceDTO found = findItemReference(referenceId, targetDTOs);

			final ItemDTO item = new ItemDTO();
			item.setReference(referenceDTO);
			ActionType action = ActionType.NONE;

			if (found != null)
			{
				if (!found.getModificationComparator().equalsIgnoreCase(referenceDTO.getModificationComparator()))
				{
					action = ActionType.UPDATE;
				}
			}
			else
			{
				action = ActionType.CREATE;
			}
			item.setAction(action);

			if (item.getAction() != ActionType.NONE)
			{
				diff.add(item);
			}
		}
		return diff;
	}

	static List<ItemDTO> makeToDeleteItems(final List<ItemReferenceDTO> references,
	    final List<ItemReferenceDTO> targets)
	{
		final List<ItemDTO> toDelete = new ArrayList<ItemDTO>();
		for (final ItemReferenceDTO target : targets)
		{
			if (findItemReference(target.getReferenceId(), references) == null)
			{
				toDelete.add(new ItemDTO(target, ActionType.DELETE));
			}
		}
		return toDelete;
	}

	static ItemReferenceDTO findItemReference(final String referenceId,
	    final List<ItemReferenceDTO> itemReferences)
	{
		if ((referenceId == null) || (itemReferences == null))
		{
			return null;
		}

		return (ItemReferenceDTO) CollectionUtils.find(itemReferences, new Predicate()
		{
			@Override
			public boolean evaluate(final Object o)
			{
				final ItemReferenceDTO elem = (ItemReferenceDTO) o;
				return elem.getReferenceId().equalsIgnoreCase(referenceId);
			}
		});
	}
}
