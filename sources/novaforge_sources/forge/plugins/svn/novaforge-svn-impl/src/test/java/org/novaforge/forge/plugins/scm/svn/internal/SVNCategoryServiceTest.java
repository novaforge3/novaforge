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

package org.novaforge.forge.plugins.scm.svn.internal;

import junit.framework.TestCase;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNClientFacadeService;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNFacadeService;

import java.net.URL;
import java.util.Locale;

/**
 * @author rols-p
 */
public class SVNCategoryServiceTest extends TestCase
{

  /**
    * 
    */
  private static final String TOOL_PROJECT_ID = "TOOL_PROJECT_ID";
  /**
    * 
    */
  private static final String SVN_REPO_URL    = "http://vm-infra-10/svn/test2_svn";

  private static final Locale DEFAULT_LOCALE  = new Locale("fr");
  private static final String FR_MSG          = "Vous pouvez acc\u00E9der \u00E0 ce d\u00E9p\u00F4t de sources via un svn checkout [url=%s, identifiant,mot de passe=identifiant,mot de passe de la forge].";

  public void testGetAccessInfo() throws Exception
  {
    final SVNCategoryServiceImpl service = new SVNCategoryServiceImpl();
    final PluginConfigurationService pluginConfigurationService = Mockito
        .mock(PluginConfigurationService.class);
    Mockito.when(pluginConfigurationService.getClientURL(Matchers.any(URL.class))).thenReturn(SVN_REPO_URL);
    service.setPluginConfigurationService(pluginConfigurationService);
    final SVNClientFacadeService svnClientFacadeService = Mockito.mock(SVNClientFacadeService.class);
    final SVNFacadeService svnFacadeService = Mockito.mock(SVNFacadeService.class);
    Mockito.when(svnClientFacadeService.getSVNFacadeService(Matchers.anyString())).thenReturn(
        svnFacadeService);
    Mockito.when(svnClientFacadeService.getRepositoryUrl(svnFacadeService, TOOL_PROJECT_ID)).thenReturn(
        SVN_REPO_URL);
    final InstanceConfiguration instanceConf = Mockito.mock(InstanceConfiguration.class);
    Mockito.when(instanceConf.getToolProjectId()).thenReturn(TOOL_PROJECT_ID);
    final ToolInstance toolInstance = Mockito.mock(ToolInstance.class);
    Mockito.when(toolInstance.getBaseURL()).thenReturn(new URL(SVN_REPO_URL));

    Mockito.when(instanceConf.getToolInstance()).thenReturn(toolInstance);
    final InstanceConfigurationDAO daoRemote = Mockito.mock(InstanceConfigurationDAO.class);
    Mockito.when(daoRemote.findByInstanceId(Matchers.anyString())).thenReturn(instanceConf);

    service.setSvnClientFacadeService(svnClientFacadeService);
    service.setInstanceConfigurationDAO(daoRemote);

    final String message = service.getApplicationAccessInfo("instanceId", DEFAULT_LOCALE);
    assertEquals(String.format(FR_MSG, SVN_REPO_URL), message);
  }
}
