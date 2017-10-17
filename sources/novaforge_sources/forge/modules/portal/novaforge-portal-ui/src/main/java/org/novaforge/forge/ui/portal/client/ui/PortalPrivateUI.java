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
package org.novaforge.forge.ui.portal.client.ui;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.core.security.cas.CasSecurityUrl;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalContext.KEY;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalModuleService;
import org.novaforge.forge.ui.portal.event.AuthentificationChangedEvent.Status;
import org.novaforge.forge.ui.portal.osgi.OSGiHelper;
import org.novaforge.forge.ui.portal.shared.PortalContentUri;

import java.util.Map;

/**
 * This {@link UI} will referrer to private content
 * 
 * @author Guillaume Lamirand
 */
public class PortalPrivateUI extends AbstractPortalUI
{

  /**
   * Default serial version id
   */
  private static final long serialVersionUID = -2881139985491701737L;

  /**
   * {@inheritDoc}
   */
  @Override
  public void close()
  {
    super.close();
    if (VaadinSession.getCurrent() != null)
    {
      // Invalidate underlying session instead if login info is stored
      // there
      VaadinSession.getCurrent().close();

      // Redirect to avoid keeping the removed UI open in the browser
      final CasSecurityUrl casSecurityUrl = OSGiHelper.getCasSecurityUrl();
      if (casSecurityUrl != null)
      {
        final StringBuilder logoutUrl = new StringBuilder();
        final String caslogout = casSecurityUrl.getCasLogout();
        if (StringUtils.isNotBlank(caslogout))
        {
          logoutUrl.append(caslogout);
        }
        else
        {
          logoutUrl.append(PortalContentUri.PUBLIC.getUri());
        }
        Page.getCurrent().setLocation(logoutUrl.toString());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setPortalContent()
  {
    final PortalContext privateContext = OSGiHelper.getPortalModuleService().buildContext(eventBus,
        getLocale(), null);
    final PortalModule privateModule = OSGiHelper.getPortalModuleService().getModule(
        PortalModuleId.PRIVATE.getId());
    final PortalComponent privateComponent = privateModule.createComponent(privateContext);
    portalLayout.addContent(privateComponent.getComponent());
    privateComponent.init();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean refreshPortalContent(final Map<KEY, String> pMap, final String pModuleId)
  {
    boolean isMain = false;
    // Build module context
    final PortalModuleService portalModuleService = OSGiHelper.getPortalModuleService();
    if ((portalModuleService != null) && (PortalModuleId.PRIVATE.getId().equals(pModuleId)))
    {
      final PortalContext buildContext = portalModuleService.buildContext(eventBus, getLocale(), pMap);
      // Initialize and add the footer content
      final PortalModule privateModule = portalModuleService.getModule(PortalModuleId.PRIVATE.getId());
      final PortalComponent privateComponent = privateModule.createComponent(buildContext);
      if (Status.AUTHENTIFICATED.equals(authStatus))
      {
        portalLayout.addContent(privateComponent.getComponent());
      }
      if (!PortalModuleId.UNAVAILABLE.getId().equals(privateModule.getId()))
      {
        modulesQueue.add(privateComponent);
      }
      isMain = true;
    }
    return isMain;
  }
}
