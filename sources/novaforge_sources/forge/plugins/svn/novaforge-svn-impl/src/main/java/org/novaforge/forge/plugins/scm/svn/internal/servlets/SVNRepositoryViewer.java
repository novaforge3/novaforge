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

import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.scm.svn.services.SVNConfigurationService;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import javax.servlet.ServletException;

/**
 * @author sbenoist
 */
public class SVNRepositoryViewer
{

  public static final String       SVN_ALIAS_SERVLET = "/svnRepositoryViewer";

  private boolean                  isServletRegister = false;
  private SVNConfigurationService  svnConfigurationService;
  private InstanceConfigurationDAO instanceConfigurationDAO;
  private HttpService              httpService;

  /**
   * Init method
   * 
   * @throws PluginServiceException
   */
  public void init() throws PluginServiceException
  {
    // register the servlet to httpService
    try
    {
      httpService.registerServlet(SVN_ALIAS_SERVLET, new SVNRepositoryViewerServlet(svnConfigurationService,
          instanceConfigurationDAO), null, null);
      isServletRegister = true;
    }
    catch (final ServletException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to register SVN repository viewer servlet with [alias=%s]", SVN_ALIAS_SERVLET), e);
    }
    catch (final NamespaceException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to register tool gadget descriptor servlet with [alias=%s]", SVN_ALIAS_SERVLET), e);
    }
  }

  /**
   * Destroy method usd by container
   */
  public void destroy()
  {
    if (isServletRegister)
    {
      httpService.unregister(SVN_ALIAS_SERVLET);
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
  public void setSvnConfigurationService(final SVNConfigurationService pSvnConfigurationService)
  {
    svnConfigurationService = pSvnConfigurationService;
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
