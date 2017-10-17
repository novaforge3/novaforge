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
package org.novaforge.forge.plugins.scm.svn.internal.servlets;

import org.junit.Test;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.plugins.scm.svn.services.SVNConfigurationService;

import javax.servlet.ServletException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Guillaume Lamirand
 */
public class SVNRepositoryViewerServletTest
{
	@Test
	public void redirectUrl() throws ServletException
	{

		final ToolInstance tool = mock(ToolInstance.class);
		when(tool.getAlias()).thenReturn("/svn-default");
		final InstanceConfiguration instance = mock(InstanceConfiguration.class);
		when(instance.getToolProjectId()).thenReturn("forge_project");
		when(instance.getToolInstance()).thenReturn(tool);
		final SVNConfigurationService SVNConfigurationService = mock(SVNConfigurationService.class);
		when(SVNConfigurationService.getSvnWebBrowserAlias()).thenReturn("/svnwebclient");
		when(SVNConfigurationService.getSvnWebBrowserBaseUrl()).thenReturn("http://localhost:9000/");
		final SVNRepositoryViewerServlet aliasMatcher = new SVNRepositoryViewerServlet(SVNConfigurationService,
		    null);
		final URL redirectUrl = aliasMatcher.getRedirectUrl(instance);
		assertEquals("http://localhost:9000/svn-default/svnwebclient?location=forge_project",
		    redirectUrl.toExternalForm());
	}
}
