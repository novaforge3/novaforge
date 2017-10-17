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
package org.novaforge.forge.ui.portal.servlet;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinServletService;
import org.novaforge.forge.ui.portal.client.i18n.PortalSystemMessagesProvider;
import org.novaforge.forge.ui.portal.osgi.OSGiHelper;
import org.novaforge.forge.ui.portal.servlet.handlers.ModulesRequestHandler;
import org.novaforge.forge.ui.portal.servlet.handlers.RedirectRequestHandler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * This class redefine how application url is retrieved and also system message provider
 * 
 * @author Guillaume Lamirand
 */
public class PortalServletService extends VaadinServletService
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 1207207566639338449L;

  /**
   * Default constructor
   * 
   * @param pServlet
   *          the source servlet
   * @param pDeploymentConfiguration
   *          the deployment configuration
   * @throws ServiceException
   *           thrown on any errors
   */
  public PortalServletService(final VaadinServlet pServlet,
      final DeploymentConfiguration pDeploymentConfiguration) throws ServiceException
  {
    super(pServlet, pDeploymentConfiguration);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected List<RequestHandler> createRequestHandlers() throws ServiceException
  {
    final List<RequestHandler> handlers = super.createRequestHandlers();
    handlers.add(6, new ModulesRequestHandler());
    handlers.add(new RedirectRequestHandler());
    return handlers;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected URL getApplicationUrl(final VaadinRequest request) throws MalformedURLException
  {

    final URL reqURL = OSGiHelper.getForgeConfigurationService().getPublicUrl();
    String servletPath = "";
    if (request.getAttribute("javax.servlet.include.servlet_path") != null)
    {
      // this is an include request
      servletPath = request.getAttribute("javax.servlet.include.context_path").toString()
          + request.getAttribute("javax.servlet.include.servlet_path");
    }
    else
    {
      servletPath = request.getContextPath() + ((VaadinServletRequest) request).getServletPath();
    }
    if ((servletPath.length() == 0) || (servletPath.charAt(servletPath.length() - 1) != '/'))
    {
      servletPath = servletPath + "/";
    }
    return new URL(reqURL, servletPath);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SystemMessagesProvider getSystemMessagesProvider()
  {
    return PortalSystemMessagesProvider.get();
  }

}
