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
package org.novaforge.forge.distribution.reference.internal.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.distribution.reference.client.ReferentielSynchroClient;
import org.novaforge.forge.distribution.reference.exception.ReferenceServiceException;
import org.novaforge.forge.distribution.reference.model.ApplicationItem;
import org.novaforge.forge.distribution.reference.model.ReferenceProject;
import org.novaforge.forge.distribution.reference.model.ReferenceTemplates;
import org.novaforge.forge.distribution.reference.model.SynchronizationResult;
import org.novaforge.forge.distribution.reference.model.TemplateSynchroResult;
import org.novaforge.forge.distribution.reference.model.ToolsSynchronizationReport;
import org.novaforge.forge.distribution.reference.service.SynchonizationService;

/**
 * @author rols-p
 */
public class ReferentielSynchroClientImpl implements ReferentielSynchroClient
{

	private static final Log  log                   = LogFactory.getLog(ReferentielSynchroClientImpl.class);

	/**
	 * Timeout in ms after which the client will failed if the response of the webservice is not retreived.
	 */
	private static final long WS_CONNECTION_TIMEOUT = 30 * 1000;

	/**
	 * Timeout in ms after which the client will failed if the response of the webservice is not retreived.
	 */
	private static final long WS_RECEIVED_TIMEOUT   = 3 * 60 * 60 * 1000;

	@Override
	@Historization(type = EventType.UPDATE_REFERENCE_PROJECT, returnLabel = "Result")
	public SynchronizationResult updateReferenceProject(final ReferenceProject refProjectDTO,
																											@HistorizableParam(label = "forgeUrl") final String forgeUrl)
			throws ReferenceServiceException
	{
		if (log.isDebugEnabled())
		{
			log.debug(String.format("calling updateReferenceProject service for the forge uri=%s",
															buildServiceUri(forgeUrl)));
		}
		final SynchonizationService synchonizationService = getReferentielSynchroService(forgeUrl);
		return synchonizationService.updateReferenceProject(refProjectDTO);
	}

	private String buildServiceUri(final String forgeUrl)
	{
		return String.format("%s/cxf/%s", forgeUrl, SynchonizationService.REF_SYNC_SERVICE_NAME);
	}

	/**
	 * {@inheritDoc}
	 */
	SynchonizationService getReferentielSynchroService(final String forgeUrl)
	{
		SynchonizationService synchService = null;
		final ClassLoader theGoodOne = ClientProxyFactoryBean.class.getClassLoader();
		final ClassLoader theOldOne = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(theGoodOne);

		try
		{
			final ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
			factory.setServiceClass(SynchonizationService.class);
			factory.setAddress(buildServiceUri(forgeUrl));
			synchService = (SynchonizationService) factory.create();

			final Client proxy = ClientProxy.getClient(synchService);
			final HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
			final HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
			httpClientPolicy.setConnectionTimeout(WS_CONNECTION_TIMEOUT);
			httpClientPolicy.setReceiveTimeout(WS_RECEIVED_TIMEOUT);
			conduit.setClient(httpClientPolicy);
		}
		finally
		{
			Thread.currentThread().setContextClassLoader(theOldOne);
		}
		return synchService;
	}

	@Override
	@Historization(type = EventType.UPDATE_TEMPLATES, returnLabel = "Result")
	public TemplateSynchroResult updateTemplates(final ReferenceTemplates refTemplate, @HistorizableParam(
	    label = "forgeUrl") final String forgeUrl) throws ReferenceServiceException
	{
		if (log.isDebugEnabled())
		{
			log.debug(String.format("calling updateTemplates service for the forge uri=%s",
			    buildServiceUri(forgeUrl)));
		}
		final SynchonizationService synchonizationService = getReferentielSynchroService(forgeUrl);
		return synchonizationService.updateTemplates(refTemplate);
	}

	@Override
	@Historization(type = EventType.GET_REFERENCE_PROJECT, returnLabel = "Result")
	public ReferenceProject getReferenceProject(@HistorizableParam(label = "forgeUrl") final String forgeUrl)
	    throws ReferenceServiceException
	{
		if (log.isDebugEnabled())
		{
			log.debug(String.format("calling getReferenceProject service for the forge uri=%s",
			    buildServiceUri(forgeUrl)));
		}
		final SynchonizationService synchonizationService = getReferentielSynchroService(forgeUrl);
		return synchonizationService.getReferenceProject();
	}

	@Override
	@Historization(type = EventType.UPDATE_APPLICATION_DATA, returnLabel = "Result")
	public boolean updateApplicationItem(final ApplicationItem applicationItem, @HistorizableParam(
	    label = "forgeUrl") final String forgeUrl) throws ReferenceServiceException
	{
		if (log.isDebugEnabled())
		{
			log.debug(String.format("calling updateApplicationItem service for the forge uri=%s",
			    buildServiceUri(forgeUrl)));
		}
		final SynchonizationService synchonizationService = getReferentielSynchroService(forgeUrl);
		return synchonizationService.updateApplicationItem(applicationItem);
	}

	@Override
	@Historization(type = EventType.SEND_REFERENCE_TOOLS_REPORT, returnLabel = "Result")
	public void sendReferenceToolsUpdateReport(final ToolsSynchronizationReport report, final String forgeUrl)
	    throws ReferenceServiceException
	{
		if (log.isDebugEnabled())
		{
			log.debug(String.format("calling updateApplicationItem service for the forge uri=%s",
			    buildServiceUri(forgeUrl)));
		}
		final SynchonizationService synchonizationService = getReferentielSynchroService(forgeUrl);
		synchonizationService.sendReferenceToolsUpdateReport(report);
	}
}
