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
package org.novaforge.forge.plugins.cms.spip.internal;

import org.junit.Test;
import org.mockito.Mockito;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.plugins.cms.spip.services.SpipConfigurationService;

import java.net.URL;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * @author rols-p
 */
public class SpipCategoryServiceTest
{

	private static final String BASE_URL        = "http://localhost/";
	private static final String INSTANCE_ID     = "instanceId";
	private static final String TOOL_PROJECT_ID = "spip";
	private static final Locale DEFAULT_LOCALE  = new Locale("fr");

	private static final String SITE_URL        = "http://localhost/spip/spip";

	private static final String MSG_FR          = "Vous pouvez acc\u00E9der \u00E0 ce site web via l'url %s et \u00E0 sa gestion en vous connectant \u00E0 la forge.";

	@Test
	public void getApplicationAccessInfo() throws Exception
	{
		final URL value = new URL(BASE_URL);
		final ToolInstance toolInstance = Mockito.mock(ToolInstance.class);
		Mockito.when(toolInstance.getBaseURL()).thenReturn(value);

		final InstanceConfiguration instanceConfig = Mockito.mock(InstanceConfiguration.class);
		Mockito.when(instanceConfig.getToolProjectId()).thenReturn(TOOL_PROJECT_ID);
		Mockito.when(instanceConfig.getToolInstance()).thenReturn(toolInstance);

		final InstanceConfigurationDAO daoRemote = Mockito.mock(InstanceConfigurationDAO.class);
		Mockito.when(daoRemote.findByInstanceId(INSTANCE_ID)).thenReturn(instanceConfig);

		final SpipConfigurationService spipConfigurationService = Mockito.mock(SpipConfigurationService.class);
		Mockito.when(spipConfigurationService.getSiteUrl(value, TOOL_PROJECT_ID)).thenReturn(SITE_URL);
		final SpipCategoryServiceImpl svc = new SpipCategoryServiceImpl();
		svc.setInstanceConfigurationDAO(daoRemote);
		svc.setPluginConfigurationService(spipConfigurationService);

		System.out.println(spipConfigurationService.getClientURL(toolInstance.getBaseURL(), TOOL_PROJECT_ID));
		final String message = svc.getApplicationAccessInfo(INSTANCE_ID, DEFAULT_LOCALE);

		assertEquals(String.format(MSG_FR, SITE_URL), message);

	}
}
