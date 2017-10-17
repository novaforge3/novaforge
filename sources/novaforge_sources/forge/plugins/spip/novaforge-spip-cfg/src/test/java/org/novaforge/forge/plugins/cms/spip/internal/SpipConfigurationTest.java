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
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * @author rols-p,Guillaume Lamirand
 */
public class SpipConfigurationTest
{
	private static final String ENDPOINT = "http://localhost/spip/test2_spip1/ecrire/nf_spipconnect.php";
	private static final String SITE_URL = "http://localhost/spip/test2_spip1";

	@Test
	public void getUrl() throws MalformedURLException, PluginServiceException
	{
		final SpipConfigurationImpl spips = new SpipConfigurationImpl();

		URL baseUrl = new URL("http://localhost/");
		final PluginConfigurationService pluginConfig = Mockito.mock(PluginConfigurationService.class);
		Mockito.when(pluginConfig.getClientURL(baseUrl)).thenReturn(
		    "http://localhost/spip/@spipProject@/ecrire/nf_spipconnect.php");
		spips.setPluginConfigurationService(pluginConfig);

		final String clientURL = spips.getClientURL(baseUrl, "test2_spip1");
		assertEquals(ENDPOINT, clientURL);
	}

	@Test
	public void getSiteUrl() throws MalformedURLException, PluginServiceException
	{
		final SpipConfigurationImpl spips = new SpipConfigurationImpl();

		final String clientURL = spips.getSiteUrl(new URL("http://localhost/"), "test2_spip1");
		assertEquals(SITE_URL, clientURL);
	}
}
