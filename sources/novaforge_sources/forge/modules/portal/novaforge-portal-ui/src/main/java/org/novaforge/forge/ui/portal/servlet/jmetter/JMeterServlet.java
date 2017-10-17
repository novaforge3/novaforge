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
package org.novaforge.forge.ui.portal.servlet.jmetter;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import org.jsoup.select.Elements;

import javax.servlet.ServletException;

/**
 * This servlet is defined in order to redefine some stuff about Vaadin Framework
 * 
 * @author Guillaume Lamirand
 */
public class JMeterServlet extends VaadinServlet
{

  /**
   * Serial version uid
   */
  private static final long serialVersionUID = -1438295278785392219L;

  public JMeterServlet()
  {
    System.setProperty(getPackageName() + "." + "disable-xsrf-protection", "true");
  }

  private String getPackageName()
  {
    String pkgName;
    final Package pkg = this.getClass().getPackage();
    if (pkg != null)
    {
      pkgName = pkg.getName();
    }
    else
    {
      final String className = this.getClass().getName();
      pkgName = new String(className.toCharArray(), 0, className.lastIndexOf('.'));
    }
    return pkgName;
  }

  @Override
  protected void servletInitialized() throws ServletException
  {
    super.servletInitialized();
    getService().addSessionInitListener(new SessionInitListener()
    {

      private static final long serialVersionUID = 1L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void sessionInit(final SessionInitEvent event)
      {
        event.getSession().addBootstrapListener(new BootstrapListener()
        {

          private static final long serialVersionUID = 1L;

          /**
           * {@inheritDoc}
           */
          @Override
          public void modifyBootstrapFragment(final BootstrapFragmentResponse response)
          {
            // Nothing to do
          }

          /**
           * {@inheritDoc}
           */
          @Override
          public void modifyBootstrapPage(final BootstrapPageResponse response)
          {
            final Elements element = response.getDocument().head()
                .getElementsByAttributeValue("http-equiv", "X-UA-Compatible");
            if (element != null)
            {
              element.removeAttr("content");
              element.attr("content", "IE=Edge,chrome=1");
            }
            else
            {
              response.getDocument().head().appendElement("meta").attr("http-equiv", "X-UA-Compatible")
                  .attr("content", "IE=Edge,chrome=1");
            }
          }
        });
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected VaadinServletService createServletService(final DeploymentConfiguration deploymentConfiguration)
      throws ServiceException
  {
    final JMeterService jMeterService = new JMeterService(this, deploymentConfiguration);
    jMeterService.init();
    return jMeterService;
  }
}
