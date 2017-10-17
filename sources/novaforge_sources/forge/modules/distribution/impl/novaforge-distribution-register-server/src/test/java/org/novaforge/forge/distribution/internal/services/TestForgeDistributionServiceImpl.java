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
package org.novaforge.forge.distribution.internal.services;

import org.junit.Test;
import org.novaforge.forge.distribution.register.dao.ForgeDAO;
import org.novaforge.forge.distribution.register.dao.ForgeRequestDAO;
import org.novaforge.forge.distribution.register.domain.Forge;
import org.novaforge.forge.distribution.register.domain.ForgeDTO;
import org.novaforge.forge.distribution.register.domain.ForgeRequest;
import org.novaforge.forge.distribution.register.domain.ForgeRequestDTO;
import org.novaforge.forge.distribution.register.domain.RequestStatus;
import org.novaforge.forge.distribution.register.domain.RequestType;
import org.novaforge.forge.distribution.register.exceptions.ForgeDistributionException;
import org.novaforge.forge.distribution.register.server.entity.ForgeEntity;
import org.novaforge.forge.distribution.register.server.entity.ForgeRequestEntity;
import org.novaforge.forge.distribution.register.server.internal.services.ForgeDistributionServiceImpl;
import org.novaforge.forge.distribution.register.services.ForgeDistributionService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestForgeDistributionServiceImpl
{

	@Test
	public void addForge() throws ForgeDistributionException, MalformedURLException
	{
		// Given
		final ForgeDistributionServiceImpl forgeDistributionServiceImpl = new ForgeDistributionServiceImpl();

		final ForgeDAO mockForgeDAO = mock(ForgeDAO.class);
		forgeDistributionServiceImpl.setForgeDAO(mockForgeDAO);
		final UUID uuid = UUID.randomUUID();
		final String forgeDescription = "description";
		final String forgeLabel = "label";
		final String portalUri = "/portal";
		final String forgeAdminUrl = "http://localhost:8181";

		final Forge forge = new ForgeEntity();
		forge.setForgeId(uuid);
		forge.setLabel(forgeLabel);
		forge.setPortalUri(portalUri);
		forge.setForgeUrl(new URL(forgeAdminUrl));
		forge.setDescription(forgeDescription);
		forge.setForgeLevel(ForgeDistributionService.CENTRAL);

		final ForgeDTO newForgeDTO = new ForgeDTO();
		newForgeDTO.setForgeId(uuid);
		newForgeDTO.setDescription(forgeDescription);
		newForgeDTO.setLabel(forgeLabel);
		newForgeDTO.setPortalUri(portalUri);
		newForgeDTO.setForgeUrl(new URL(forgeAdminUrl));
		newForgeDTO.setForgeLevel(ForgeDistributionService.CENTRAL);

		// When
		when(mockForgeDAO.newForge()).thenReturn(forge);
		when(mockForgeDAO.save(forge)).thenReturn(forge);

		// Then
		final ForgeDTO newForge = forgeDistributionServiceImpl.addForge(newForgeDTO);
		assertEquals(newForge.getForgeId().toString(), uuid.toString());
		assertEquals(newForge.getDescription(), forgeDescription);
		assertEquals(newForge.getLabel(), forgeLabel);
		assertEquals(newForge.getPortalUri(), portalUri);
		assertEquals(newForge.getForgeUrl(), new URL(forgeAdminUrl));
		assertEquals(newForge.getForgeLevel(), ForgeDistributionService.CENTRAL);
	}

	@Test
	public void updateForgeDescription() throws ForgeDistributionException, MalformedURLException
	{
		// Given
		final ForgeDistributionServiceImpl forgeDistributionServiceImpl = new ForgeDistributionServiceImpl();

		final ForgeDAO mockForgeDAO = mock(ForgeDAO.class);
		forgeDistributionServiceImpl.setForgeDAO(mockForgeDAO);
		final UUID uuid = UUID.randomUUID();
		final String forgeDescription = "description";
		final String forgeLabel = "label";
		final String portalUri = "/portal";
		final String forgeAdminUrl = "http://localhost:8181";

		final Forge forge = new ForgeEntity();
		forge.setForgeId(uuid);
		forge.setLabel(forgeLabel);
		forge.setPortalUri(portalUri);
		forge.setForgeUrl(new URL(forgeAdminUrl));
		forge.setDescription(forgeDescription);
		forge.setForgeLevel(ForgeDistributionService.CENTRAL);

		final ForgeDTO newForgeDTO = new ForgeDTO();
		newForgeDTO.setForgeId(uuid);
		newForgeDTO.setDescription(forgeDescription);
		newForgeDTO.setLabel(forgeLabel);
		newForgeDTO.setPortalUri(portalUri);
		newForgeDTO.setForgeUrl(new URL(forgeAdminUrl));
		newForgeDTO.setForgeLevel(ForgeDistributionService.CENTRAL);

		// When
		when(mockForgeDAO.newForge()).thenReturn(forge);
		when(mockForgeDAO.findById(uuid)).thenReturn(forge);
		when(mockForgeDAO.update(forge)).thenReturn(forge);

		// Then
		final ForgeDTO newForge = forgeDistributionServiceImpl.updateForgeDescription(newForgeDTO);
		assertEquals(newForge.getForgeId().toString(), uuid.toString());
		assertEquals(newForge.getDescription(), forgeDescription);
		assertEquals(newForge.getLabel(), forgeLabel);
		assertEquals(newForge.getPortalUri(), portalUri);
		assertEquals(newForge.getForgeUrl(), new URL(forgeAdminUrl));
		assertEquals(newForge.getForgeLevel(), ForgeDistributionService.CENTRAL);
	}

	@Test
	public void getAvailableMotherForgesToSubscription() throws ForgeDistributionException
	{
		// Given
		final ForgeDistributionServiceImpl forgeDistributionServiceImpl = new ForgeDistributionServiceImpl();
		final List<Forge> forgesDirectoryMock = buildForgesDirectoryMock(1);
		final List<Forge> forgesDirectoryMock2 = buildForgesDirectoryMock(2);

		final ForgeDAO mockForgeDAO = mock(ForgeDAO.class);
		forgeDistributionServiceImpl.setForgeDAO(mockForgeDAO);

		// When
		when(mockForgeDAO.findByLevel(0)).thenReturn(forgesDirectoryMock);
		when(mockForgeDAO.findByLevel(1)).thenReturn(forgesDirectoryMock2);

		// Then
		final Collection<ForgeDTO> forgesDirectory = forgeDistributionServiceImpl
		    .getAvailableMotherForgesToSubscription();
		assertEquals(forgesDirectory.size(), forgesDirectoryMock.size() + forgesDirectoryMock2.size());
	}

	private List<Forge> buildForgesDirectoryMock(final int nbForge)
	{
		final List<Forge> forgesDirectoryMock = new ArrayList<Forge>();
		for (int i = 1; i <= nbForge; i++)
		{
			final Forge forge = new ForgeEntity();
			forge.setForgeId(UUID.randomUUID());
			forge.setLabel("label" + i);
			forge.setDescription("description" + i);
			forge.setForgeLevel(ForgeDistributionService.ORPHAN);
			forgesDirectoryMock.add(forge);
		}
		return forgesDirectoryMock;
	}

	@Test
	public void getForge() throws ForgeDistributionException
	{
		// Given
		final ForgeDistributionServiceImpl forgeDistributionServiceImpl = new ForgeDistributionServiceImpl();

		final ForgeDAO mockForgeDAO = mock(ForgeDAO.class);
		forgeDistributionServiceImpl.setForgeDAO(mockForgeDAO);
		final UUID uuid = UUID.randomUUID();

		// When
		when(mockForgeDAO.findById(uuid)).thenReturn(null);

		// Then
		final ForgeDTO forgeDTO = forgeDistributionServiceImpl.getForge(uuid.toString());
		assertEquals(forgeDTO, null);
	}

	@Test
	public void approveSubription_BecomeCentral() throws ForgeDistributionException, MalformedURLException
	{
		// Given
		final ForgeDistributionServiceImpl forgeDistributionServiceImpl = new ForgeDistributionServiceImpl();

		final String portalUri = "/portal";
		final String forgeAdminUrl = "http://localhost:8181";

		final Forge mother = new ForgeEntity();
		mother.setForgeId(UUID.randomUUID());
		mother.setLabel("mother");
		mother.setForgeLevel(ForgeDistributionService.ORPHAN);
		mother.setPortalUri(portalUri);
		mother.setForgeUrl(new URL(forgeAdminUrl));

		final Forge daughter = new ForgeEntity();
		daughter.setForgeId(UUID.randomUUID());
		daughter.setLabel("daughter");
		daughter.setForgeLevel(ForgeDistributionService.ORPHAN);
		daughter.setPortalUri(portalUri);
		daughter.setForgeUrl(new URL(forgeAdminUrl));

		final ForgeRequestDAO mockForgeRequestDAO = mock(ForgeRequestDAO.class);
		forgeDistributionServiceImpl.setForgeRequestDAO(mockForgeRequestDAO);
		final ForgeDAO mockForgeDAO = mock(ForgeDAO.class);
		forgeDistributionServiceImpl.setForgeDAO(mockForgeDAO);

		final ForgeRequest forgeRequest = new ForgeRequestEntity();
		final UUID forgeRequestId = UUID.randomUUID();
		forgeRequest.setForgeRequestId(forgeRequestId);
		forgeRequest.setSourceForge(daughter);
		forgeRequest.setDestinationForge(mother);
		forgeRequest.setRequestStatus(RequestStatus.IN_PROGRESS);
		forgeRequest.setRequestType(RequestType.SUBSCRIBE);

		// When
		when(mockForgeRequestDAO.findById(forgeRequestId)).thenReturn(forgeRequest);

		when(mockForgeDAO.update(daughter)).thenReturn(daughter);
		when(mockForgeDAO.update(mother)).thenReturn(mother);
		when(mockForgeRequestDAO.update(forgeRequest)).thenReturn(forgeRequest);

		// Then
		try
		{
			final ForgeRequestDTO forgeRequestReturn = forgeDistributionServiceImpl
			    .approveSubription(forgeRequestId.toString());
			assertEquals(forgeRequestReturn.getDestinationForge().getForgeLevel(), 0);
			assertEquals(forgeRequestReturn.getSourceForge().getForgeLevel(), 1);
		}
		catch (final Exception e)
		{
			// Exception is thrown for ws
		}
	}

	@Test
	public void approveSubription_BecomeZonal() throws ForgeDistributionException, MalformedURLException
	{
		// Given
		final ForgeDistributionServiceImpl forgeDistributionServiceImpl = new ForgeDistributionServiceImpl();

		final String portalUri = "/portal";
		final String forgeAdminUrl = "http://localhost:8181";

		final Forge central = new ForgeEntity();
		central.setForgeId(UUID.randomUUID());
		central.setLabel("central");
		central.setForgeLevel(ForgeDistributionService.CENTRAL);
		central.setPortalUri(portalUri);
		central.setForgeUrl(new URL(forgeAdminUrl));

		final Forge zonal = new ForgeEntity();
		zonal.setForgeId(UUID.randomUUID());
		zonal.setLabel("zonal");
		zonal.setForgeLevel(ForgeDistributionService.ZONAL);
		zonal.setParent(central);
		zonal.setPortalUri(portalUri);
		zonal.setForgeUrl(new URL(forgeAdminUrl));

		final Forge daughter = new ForgeEntity();
		daughter.setForgeId(UUID.randomUUID());
		daughter.setLabel("daughter");
		daughter.setForgeLevel(ForgeDistributionService.ORPHAN);
		daughter.setPortalUri(portalUri);
		daughter.setForgeUrl(new URL(forgeAdminUrl));

		final ForgeRequestDAO mockForgeRequestDAO = mock(ForgeRequestDAO.class);
		forgeDistributionServiceImpl.setForgeRequestDAO(mockForgeRequestDAO);
		final ForgeDAO mockForgeDAO = mock(ForgeDAO.class);
		forgeDistributionServiceImpl.setForgeDAO(mockForgeDAO);

		final ForgeRequest forgeRequest = new ForgeRequestEntity();
		final UUID forgeRequestId = UUID.randomUUID();
		forgeRequest.setForgeRequestId(forgeRequestId);
		forgeRequest.setSourceForge(daughter);
		forgeRequest.setDestinationForge(central);
		forgeRequest.setRequestStatus(RequestStatus.IN_PROGRESS);
		forgeRequest.setRequestType(RequestType.SUBSCRIBE);

		// When
		when(mockForgeRequestDAO.findById(forgeRequestId)).thenReturn(forgeRequest);

		when(mockForgeDAO.update(daughter)).thenReturn(daughter);
		when(mockForgeDAO.update(central)).thenReturn(central);
		when(mockForgeRequestDAO.update(forgeRequest)).thenReturn(forgeRequest);

		// Then
		final ForgeRequestDTO forgeRequestReturn = forgeDistributionServiceImpl.approveSubription(forgeRequestId
		    .toString());
		assertEquals(forgeRequestReturn.getDestinationForge().getForgeLevel(), 0);
		assertEquals(forgeRequestReturn.getSourceForge().getForgeLevel(), 1);
	}

	@Test
	public void approveSubription_BecomeLocal() throws ForgeDistributionException, MalformedURLException
	{
		// Given
		final ForgeDistributionServiceImpl forgeDistributionServiceImpl = new ForgeDistributionServiceImpl();

		final String portalUri = "/portal";
		final String forgeAdminUrl = "http://localhost:8181";

		final Forge central = new ForgeEntity();
		central.setForgeId(UUID.randomUUID());
		central.setLabel("central");
		central.setForgeLevel(ForgeDistributionService.CENTRAL);
		central.setPortalUri(portalUri);
		central.setForgeUrl(new URL(forgeAdminUrl));

		final Forge zonal = new ForgeEntity();
		zonal.setForgeId(UUID.randomUUID());
		zonal.setLabel("zonal");
		zonal.setForgeLevel(ForgeDistributionService.ZONAL);
		zonal.setParent(central);
		zonal.setPortalUri(portalUri);
		zonal.setForgeUrl(new URL(forgeAdminUrl));

		final Forge local = new ForgeEntity();
		local.setForgeId(UUID.randomUUID());
		local.setLabel("local");
		local.setForgeLevel(ForgeDistributionService.LOCAL);
		local.setParent(zonal);
		local.setPortalUri(portalUri);
		local.setForgeUrl(new URL(forgeAdminUrl));

		final Forge orphan = new ForgeEntity();
		orphan.setForgeId(UUID.randomUUID());
		orphan.setLabel("orphan");
		orphan.setForgeLevel(ForgeDistributionService.ORPHAN);
		orphan.setPortalUri(portalUri);
		orphan.setForgeUrl(new URL(forgeAdminUrl));

		final ForgeRequestDAO mockForgeRequestDAO = mock(ForgeRequestDAO.class);
		forgeDistributionServiceImpl.setForgeRequestDAO(mockForgeRequestDAO);
		final ForgeDAO mockForgeDAO = mock(ForgeDAO.class);
		forgeDistributionServiceImpl.setForgeDAO(mockForgeDAO);

		final ForgeRequest forgeRequest = new ForgeRequestEntity();
		final UUID forgeRequestId = UUID.randomUUID();
		forgeRequest.setForgeRequestId(forgeRequestId);
		forgeRequest.setSourceForge(orphan);
		forgeRequest.setDestinationForge(zonal);
		forgeRequest.setRequestStatus(RequestStatus.IN_PROGRESS);
		forgeRequest.setRequestType(RequestType.SUBSCRIBE);

		// When
		when(mockForgeRequestDAO.findById(forgeRequestId)).thenReturn(forgeRequest);

		when(mockForgeDAO.update(orphan)).thenReturn(orphan);
		when(mockForgeDAO.update(zonal)).thenReturn(zonal);
		when(mockForgeRequestDAO.update(forgeRequest)).thenReturn(forgeRequest);

		// Then
		final ForgeRequestDTO forgeRequestReturn = forgeDistributionServiceImpl.approveSubription(forgeRequestId
		    .toString());
		assertEquals(forgeRequestReturn.getDestinationForge().getForgeLevel(), 1);
		assertEquals(forgeRequestReturn.getSourceForge().getForgeLevel(), 2);
	}

	@Test
	public void approveSubription_Error_Level4() throws ForgeDistributionException, MalformedURLException
	{
		// Given
		final ForgeDistributionServiceImpl forgeDistributionServiceImpl = new ForgeDistributionServiceImpl();

		final String portalUri = "/portal";
		final String forgeAdminUrl = "http://localhost:8181";

		final Forge central = new ForgeEntity();
		central.setForgeId(UUID.randomUUID());
		central.setLabel("central");
		central.setForgeLevel(ForgeDistributionService.CENTRAL);
		central.setPortalUri(portalUri);
		central.setForgeUrl(new URL(forgeAdminUrl));

		final Forge zonal = new ForgeEntity();
		zonal.setForgeId(UUID.randomUUID());
		zonal.setLabel("zonal");
		zonal.setForgeLevel(ForgeDistributionService.ZONAL);
		zonal.setParent(central);
		zonal.setPortalUri(portalUri);
		zonal.setForgeUrl(new URL(forgeAdminUrl));

		final Forge local = new ForgeEntity();
		local.setForgeId(UUID.randomUUID());
		local.setLabel("local");
		local.setForgeLevel(ForgeDistributionService.LOCAL);
		local.setParent(zonal);
		local.setPortalUri(portalUri);
		local.setForgeUrl(new URL(forgeAdminUrl));

		final Forge orphan = new ForgeEntity();
		orphan.setForgeId(UUID.randomUUID());
		orphan.setLabel("orphan");
		orphan.setForgeLevel(ForgeDistributionService.ORPHAN);
		orphan.setPortalUri(portalUri);
		orphan.setForgeUrl(new URL(forgeAdminUrl));

		final ForgeRequestDAO mockForgeRequestDAO = mock(ForgeRequestDAO.class);
		forgeDistributionServiceImpl.setForgeRequestDAO(mockForgeRequestDAO);
		final ForgeDAO mockForgeDAO = mock(ForgeDAO.class);
		forgeDistributionServiceImpl.setForgeDAO(mockForgeDAO);

		final ForgeRequest forgeRequest = new ForgeRequestEntity();
		final UUID forgeRequestId = UUID.randomUUID();
		forgeRequest.setForgeRequestId(forgeRequestId);
		forgeRequest.setSourceForge(orphan);
		forgeRequest.setDestinationForge(local);
		forgeRequest.setRequestStatus(RequestStatus.IN_PROGRESS);
		forgeRequest.setRequestType(RequestType.SUBSCRIBE);

		// When
		when(mockForgeRequestDAO.findById(forgeRequestId)).thenReturn(forgeRequest);

		when(mockForgeDAO.update(orphan)).thenReturn(orphan);
		when(mockForgeDAO.update(local)).thenReturn(local);
		when(mockForgeRequestDAO.update(forgeRequest)).thenReturn(forgeRequest);

		// Then
		final ForgeRequestDTO forgeRequestReturn = forgeDistributionServiceImpl.approveSubription(forgeRequestId
		    .toString());
		assertEquals(forgeRequestReturn.getDestinationForge().getForgeLevel(), 2);
		assertEquals(forgeRequestReturn.getSourceForge().getForgeLevel(), -1);
	}

	// TODO correct this test
	// @Test
	public void approveUnsubscription_BecomeSuspended() throws ForgeDistributionException,
	    MalformedURLException
	{
		// Given
		final ForgeDistributionServiceImpl forgeDistributionServiceImpl = new ForgeDistributionServiceImpl();

		final String portalUri = "/portal";
		final String forgeAdminUrl = "http://localhost:8181";

		final Forge central = new ForgeEntity();
		central.setForgeId(UUID.randomUUID());
		central.setLabel("central");
		central.setForgeLevel(ForgeDistributionService.CENTRAL);
		central.setPortalUri(portalUri);
		central.setForgeUrl(new URL(forgeAdminUrl));

		final Forge zonal = new ForgeEntity();
		zonal.setForgeId(UUID.randomUUID());
		zonal.setLabel("zonal");
		zonal.setForgeLevel(ForgeDistributionService.ZONAL);
		zonal.setParent(central);
		zonal.setPortalUri(portalUri);
		zonal.setForgeUrl(new URL(forgeAdminUrl));

		final Forge local = new ForgeEntity();
		local.setForgeId(UUID.randomUUID());
		local.setLabel("local");
		local.setForgeLevel(ForgeDistributionService.LOCAL);
		local.setParent(zonal);
		local.setPortalUri(portalUri);
		local.setForgeUrl(new URL(forgeAdminUrl));

		final ForgeRequestDAO mockForgeRequestDAO = mock(ForgeRequestDAO.class);
		forgeDistributionServiceImpl.setForgeRequestDAO(mockForgeRequestDAO);
		final ForgeDAO mockForgeDAO = mock(ForgeDAO.class);
		forgeDistributionServiceImpl.setForgeDAO(mockForgeDAO);

		final ForgeRequest forgeRequest = new ForgeRequestEntity();
		final UUID forgeRequestId = UUID.randomUUID();
		forgeRequest.setForgeRequestId(forgeRequestId);
		forgeRequest.setSourceForge(local);
		forgeRequest.setDestinationForge(zonal);
		forgeRequest.setRequestStatus(RequestStatus.IN_PROGRESS);
		forgeRequest.setRequestType(RequestType.UNSUBSCRIBE);

		// When
		when(mockForgeRequestDAO.findById(forgeRequestId)).thenReturn(forgeRequest);

		// Then
		final ForgeRequestDTO forgeRequestReturn = forgeDistributionServiceImpl
		    .approveUnsubscription(forgeRequestId.toString());
		assertEquals(forgeRequestReturn.getDestinationForge().getForgeLevel(), 1);
		assertEquals(forgeRequestReturn.getSourceForge().getForgeLevel(), -2);
	}
}
