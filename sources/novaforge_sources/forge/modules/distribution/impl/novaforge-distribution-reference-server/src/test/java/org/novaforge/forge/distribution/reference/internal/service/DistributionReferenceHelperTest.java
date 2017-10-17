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
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.core.plugins.data.ActionType;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.data.ItemReferenceDTO;
import org.novaforge.forge.distribution.reference.model.ApplicationItemReferences;
import org.novaforge.forge.distribution.register.domain.ForgeDTO;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author salvat-a
 */
public class DistributionReferenceHelperTest extends AbstractDistributionTest
{

	private ItemReferenceDTO          itemRef1;
	private ItemReferenceDTO          itemRef2;
	private ItemReferenceDTO          itemRef3;
	private ItemReferenceDTO          itemRef4;
	private ItemReferenceDTO          itemRef5;
	private ItemReferenceDTO          itemRef6;

	private ApplicationItemReferences applicationItemRef1;
	private ApplicationItemReferences applicationItemRef2;
	private ApplicationItemReferences applicationItemRef3;
	private ApplicationItemReferences applicationItemRef4;

	private ItemReferenceDTO          itemTarget1;
	private ItemReferenceDTO          itemTarget2;
	private ItemReferenceDTO          itemTarget3;

	private ApplicationItemReferences applicationItemTarget;

	@Before
	public void setUp()
	{
		itemRef1 = new ItemReferenceDTO("projet-referentiel:page1", "2--123456", "PAGE");
		itemRef2 = new ItemReferenceDTO("projet-referentiel:attachment1.zip", "1--56484156", "ATTACHMENT");
		itemRef3 = new ItemReferenceDTO("projet-referentiel:page3", "18--456", "PAGE");
		itemRef4 = new ItemReferenceDTO("projet-referentiel:page4", "7--97782456", "PAGE");
		itemRef5 = new ItemReferenceDTO("projet-referentiel:attachment2.jpg", "3--3345648167", "ATTACHMENT");
		itemRef6 = new ItemReferenceDTO("projet-referentiel:start", "8--59273", "PAGE");

		applicationItemRef1 = new ApplicationItemReferences();
		applicationItemRef1.setItemReferences(Arrays.asList(itemRef1, itemRef2, itemRef3));
		applicationItemRef1.setNodeUri("reference/shared applications/Wiki Referentiel 1");

		applicationItemRef2 = new ApplicationItemReferences();
		applicationItemRef2.setItemReferences(Arrays.asList(itemRef4, itemRef5));
		applicationItemRef2.setNodeUri("reference/shared applications/Wiki Referentiel 2");

		applicationItemRef3 = new ApplicationItemReferences();
		applicationItemRef3.setNodeUri("reference/shared applications/Wiki Referentiel 3");

		applicationItemRef4 = new ApplicationItemReferences();
		applicationItemRef4.setItemReferences(Collections.singletonList(itemRef6));
		applicationItemRef4.setNodeUri("reference/shared applications/Wiki Referentiel 4");

		itemTarget1 = new ItemReferenceDTO("projet-referentiel:attachment1.zip", "1--56444156", "ATTACHMENT");
		itemTarget2 = new ItemReferenceDTO("projet-referentiel:page3", "17--456", "PAGE");
		itemTarget3 = new ItemReferenceDTO("projet-referentiel:page28", "2--1456", "PAGE");

		applicationItemTarget = new ApplicationItemReferences();
		applicationItemTarget.setItemReferences(Arrays.asList(itemTarget1, itemTarget2, itemTarget3));
		applicationItemTarget.setNodeUri("reference/shared applications/Wiki Referentiel 1");
	}

	@Test
	public void getForgeUrl() throws MalformedURLException
	{
		final ForgeDTO forge = new ForgeDTO();
		forge.setLabel("rootForge");
		final String httpServerName = "myHost";
		final int httpServerPort = 9000;
		forge.setForgeUrl(new URL("http", httpServerName, httpServerPort, ""));

		final String url = forge.getForgeUrl().toExternalForm();
		assertEquals("http://myHost:9000", url);

		final ForgeDTO forge2 = new ForgeDTO();
		forge2.setLabel("rootForge2");
		final String httpServerName2 = "myHost2";
		final int httpServerPort2 = 80;
		forge2.setForgeUrl(new URL("http", httpServerName2, httpServerPort2, ""));

		final String url2 = forge2.getForgeUrl().toExternalForm();
		assertEquals("http://myHost2:80", url2);
	}

	@Test
	public void getChildrenForges()
	{
		final Set<ForgeDTO> children = DistributionReferenceHelper.getChildrenForges(forgeDTO);
		assertNotNull(children);
		assertEquals(7, children.size());
	}

	@Test
	public void findApplicationItemReferences()
	{
		final List<ApplicationItemReferences> itemReferencesList = new ArrayList<ApplicationItemReferences>();
		itemReferencesList.addAll(Arrays.asList(applicationItemRef1, applicationItemRef2, applicationItemRef3,
		    applicationItemRef4));

		String nodeUri = "reference/shared applications/Wiki Referentiel 3";
		ApplicationItemReferences result = DistributionReferenceHelper.findApplicationItemReferences(nodeUri,
		    itemReferencesList);
		assertNotNull(result);
		assertEquals(nodeUri, result.getNodeUri());

		nodeUri = "pouet pouet";
		result = DistributionReferenceHelper.findApplicationItemReferences(nodeUri, itemReferencesList);
		assertNull(result);

		result = DistributionReferenceHelper.findApplicationItemReferences(null, itemReferencesList);
		assertNull(result);

		result = DistributionReferenceHelper.findApplicationItemReferences(nodeUri, null);
		assertNull(result);

		result = DistributionReferenceHelper.findApplicationItemReferences(null, null);
		assertNull(result);
	}

	@Test
	public void makeDiffItems()
	{
		List<ItemDTO> diff = DistributionReferenceHelper
		    .makeDiffItems(applicationItemRef1, applicationItemTarget);
		assertNotNull(diff);
		assertEquals(4, diff.size());
		assertEquals(ActionType.CREATE, findItemDTO(itemRef1.getReferenceId(), diff).getAction());
		assertEquals(ActionType.UPDATE, findItemDTO(itemRef2.getReferenceId(), diff).getAction());
		assertEquals(ActionType.UPDATE, findItemDTO(itemRef3.getReferenceId(), diff).getAction());
		assertEquals(ActionType.DELETE, findItemDTO(itemTarget3.getReferenceId(), diff).getAction());

		diff = DistributionReferenceHelper.makeDiffItems(applicationItemRef2, applicationItemTarget);
		assertNotNull(diff);
		assertEquals(5, diff.size());
		assertEquals(ActionType.CREATE, findItemDTO(itemRef4.getReferenceId(), diff).getAction());
		assertEquals(ActionType.CREATE, findItemDTO(itemRef5.getReferenceId(), diff).getAction());
		assertEquals(ActionType.DELETE, findItemDTO(itemTarget1.getReferenceId(), diff).getAction());
		assertEquals(ActionType.DELETE, findItemDTO(itemTarget2.getReferenceId(), diff).getAction());
		assertEquals(ActionType.DELETE, findItemDTO(itemTarget3.getReferenceId(), diff).getAction());

		diff = DistributionReferenceHelper.makeDiffItems(applicationItemRef3, applicationItemTarget);
		assertNotNull(diff);
		assertEquals(3, diff.size());
		assertEquals(ActionType.DELETE, findItemDTO(itemTarget1.getReferenceId(), diff).getAction());
		assertEquals(ActionType.DELETE, findItemDTO(itemTarget2.getReferenceId(), diff).getAction());
		assertEquals(ActionType.DELETE, findItemDTO(itemTarget3.getReferenceId(), diff).getAction());

		diff = DistributionReferenceHelper.makeDiffItems(applicationItemRef4, applicationItemTarget);
		assertNotNull(diff);
		assertEquals(4, diff.size());
		assertEquals(ActionType.CREATE, findItemDTO(itemRef6.getReferenceId(), diff).getAction());
		assertEquals(ActionType.DELETE, findItemDTO(itemTarget1.getReferenceId(), diff).getAction());
		assertEquals(ActionType.DELETE, findItemDTO(itemTarget2.getReferenceId(), diff).getAction());
		assertEquals(ActionType.DELETE, findItemDTO(itemTarget3.getReferenceId(), diff).getAction());

		diff = DistributionReferenceHelper.makeDiffItems(null, applicationItemTarget);
		assertNotNull(diff);
		assertEquals(0, diff.size());

		diff = DistributionReferenceHelper.makeDiffItems(applicationItemRef1, null);
		assertNotNull(diff);
		assertEquals(0, diff.size());

		diff = DistributionReferenceHelper.makeDiffItems(null, null);
		assertNotNull(diff);
		assertEquals(0, diff.size());
	}

	private ItemDTO findItemDTO(final String referenceId, final List<ItemDTO> itemDTOs)
	{
		return (ItemDTO) CollectionUtils.find(itemDTOs, new Predicate()
		{
			@Override
			public boolean evaluate(final Object o)
			{
				final ItemDTO dto = (ItemDTO) o;
				return dto.getReference().getReferenceId().equalsIgnoreCase(referenceId);
			}
		});
	}
}
