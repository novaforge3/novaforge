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
package org.novaforge.forge.distribution.reporting.internal.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.novaforge.forge.distribution.reporting.client.ForgeReportingClient;
import org.novaforge.forge.distribution.reporting.domain.ProjectDTO;
import org.novaforge.forge.distribution.reporting.domain.UserDTO;
import org.novaforge.forge.distribution.reporting.domain.ihm.ForgeViewDTO;
import org.novaforge.forge.distribution.reporting.domain.ihm.OrganizationViewDTO;
import org.novaforge.forge.distribution.reporting.domain.ihm.ProfilViewDTO;
import org.novaforge.forge.distribution.reporting.exceptions.ForgeReportingException;
import org.novaforge.forge.distribution.reporting.services.ForgeReportingService;

import java.util.List;
import java.util.UUID;

/**
 * @author Bilet-jc
 */
public class ForgeReportingClientImpl implements ForgeReportingClient
{
	private static final Log    LOGGER                = LogFactory.getLog(ForgeReportingClientImpl.class);
	private static final String REPORTING_END_POINT = "/cxf/ReportingService";
	private String              serviceReferentielUrl = "http://localhost:8181";

	@Override
	public List<ForgeViewDTO> getForgeView(final UUID pForgeId) throws ForgeReportingException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getForgeView forgeId : " + pForgeId);
		}

		return getForgeReportingManager().getForgeView(pForgeId);
	}

	private ForgeReportingService getForgeReportingManager()
	{

		ForgeReportingService client = null;

		final ClassLoader theGoodOne = ClientProxyFactoryBean.class.getClassLoader();
		final ClassLoader theOldOne = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(theGoodOne);

		try
		{
			final ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
			factory.setServiceClass(ForgeReportingService.class);
			factory.setAddress(serviceReferentielUrl + REPORTING_END_POINT);
			client = (ForgeReportingService) factory.create();
		}
		finally
		{
			Thread.currentThread().setContextClassLoader(theOldOne);
		}
		return client;
	}

	@Override
	public List<ProfilViewDTO> getProfilView(final UUID pForgeId) throws ForgeReportingException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProfilView forgeId : " + pForgeId);
		}
		return getForgeReportingManager().getProfilView(pForgeId);
	}

	@Override
	public List<OrganizationViewDTO> getOrganizationView(final UUID pForgeId) throws ForgeReportingException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrganizationView forgeId : " + pForgeId);
		}
		return getForgeReportingManager().getOrganizationView(pForgeId);
	}

	@Override
	public void storeForgeData(final UUID pForgeId, final List<ProjectDTO> pProjects, final List<UserDTO> pUsers)
	    throws ForgeReportingException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("storing forge data forgeId: " + pForgeId);
		}
		getForgeReportingManager().storeForgeData(pForgeId, pProjects, pUsers);
	}

	public void setServiceReferentielUrl(final String serviceReferentielUrl)
	{
		this.serviceReferentielUrl = serviceReferentielUrl;
	}
}
