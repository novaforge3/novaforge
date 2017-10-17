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
package org.novaforge.forge.plugins.bugtracker.jira.internal.servlets;

import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraConfigurationService;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;

/**
 * @author Gauthier Cart
 */
public class JiraBrowse
{
  public static final String       JIRA_ALIAS_SERVLET = "/jiraBrowse";

  private boolean                  isServletRegister;
  private JiraConfigurationService jiraConfigurationService;
  private InstanceConfigurationDAO instanceConfigurationDAO;
  private HttpService              httpService;

  /**
   * Init method
   * 
   * @throws PluginServiceException
   */
  public void init() throws PluginServiceException
  {
    // Register the servlet to httpService
    try
    {
      httpService.registerServlet(JIRA_ALIAS_SERVLET, new JiraBrowseServlet(jiraConfigurationService,
          instanceConfigurationDAO), null, null);
      isServletRegister = true;
    }
    catch (final ServletException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to register Jira browse servlet with [alias=%s]", JIRA_ALIAS_SERVLET), e);
    }
    catch (final NamespaceException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to register tool gadget descriptor servlet with [alias=%s]", JIRA_ALIAS_SERVLET), e);
    }
  }

  /**
   * Destroy method used by container
   */
  public void destroy()
  {
    if (isServletRegister)
    {
      httpService.unregister(JIRA_ALIAS_SERVLET);
      isServletRegister = false;
    }
  }

  /**
   * @param pIsServletRegister
   *          the isServletRegister to set
   */
  public void setServletRegister(final boolean pIsServletRegister)
  {
    isServletRegister = pIsServletRegister;
  }

  /**
   * @param pSvnConfigurationService
   *          the svnConfigurationService to set
   */
  public void setJiraConfigurationService(final JiraConfigurationService pJiraConfigurationService)
  {
    jiraConfigurationService = pJiraConfigurationService;
  }

  /**
   * @param pInstanceConfigurationDAO
   *          the instanceConfigurationDAO to set
   */
  public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
  {
    instanceConfigurationDAO = pInstanceConfigurationDAO;
  }

  /**
   * @param pHttpService
   *          the httpService to set
   */
  public void setHttpService(final HttpService pHttpService)
  {
    httpService = pHttpService;
  }
}
