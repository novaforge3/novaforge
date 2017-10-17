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

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;
import org.novaforge.forge.portal.services.PortalService;
import org.novaforge.forge.ui.portal.client.ui.PortalPrivateUI;
import org.novaforge.forge.ui.portal.client.ui.PortalPublicUI;
import org.novaforge.forge.ui.portal.client.ui.PortalRecoveryPwdUI;
import org.novaforge.forge.ui.portal.osgi.OSGiHelper;
import org.novaforge.forge.ui.portal.shared.PortalContentUri;

/**
 * @author Guillaume Lamirand
 */
public class PortalUIProvider extends UIProvider
{

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<? extends UI> getUIClass(final UIClassSelectionEvent pEvent)
  {
    Class<? extends UI>    returnUI  = null;
    final String           pathInfo  = pEvent.getRequest().getPathInfo();
    final PortalContentUri portalUri = PortalContentUri.getPortalUri(pathInfo);
    if (portalUri != null)
    {
      switch (portalUri)
      {
        case PRIVATE:
          returnUI = PortalPrivateUI.class;
          break;
        case RECOVERY_PASSWORD:
          returnUI = PortalRecoveryPwdUI.class;
          break;
        case PUBLIC:
        default:
          returnUI = PortalPublicUI.class;
          break;
      }
    }
    return returnUI;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTheme(final UICreateEvent pUICreateEvent)
  {
    final PortalService portalService = OSGiHelper.getPortalService();
    if (portalService != null)
    {
      return portalService.getTheme();
    }
    else
    {
      throw new RuntimeException("Unable to retrieve NovaForge Theme.");
    }
  }
}
