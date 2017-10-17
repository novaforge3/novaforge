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

import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalContext.KEY;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.osgi.OSGiHelper;
import org.novaforge.forge.ui.portal.shared.PortalContentUri;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This {@link UI} will referer to recovery content
 * 
 * @author Guillaume Lamirand
 */
public class PortalRecoveryPwdUI extends AbstractPortalUI
{

  /**
   * Default serial version id
   */
  private static final long serialVersionUID = -2881139985491701737L;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setPortalContent()
  {
    final String pathInfo = VaadinService.getCurrentRequest().getPathInfo();
    final PortalModule module = OSGiHelper.getPortalModuleService().getModule(
        PortalModuleId.CHANGE_PWD.getId());

    final Map<PortalContext.KEY, String> map = new HashMap<PortalContext.KEY, String>();
    map.put(PortalContext.KEY.IN_SUBWINDOW, Boolean.TRUE.toString());
    final String ticket = getTicket(pathInfo);
    map.put(PortalContext.KEY.TICKET, ticket);
    map.put(PortalContext.KEY.DESCRIPTION,
        OSGiHelper.getPortalMessages().getMessage(getLocale(), Messages.CHANGEPWD_RECOVERY_DESCRIPTION));
    final PortalContext buildContext = OSGiHelper.getPortalModuleService().buildContext(eventBus,
        getLocale(), map);
    final PortalComponent presenter = module.createComponent(buildContext);

    portalLayout.addContent(presenter.getComponent());

  }

  /**
   * Return a ticket containing inside the relate URI
   * 
   * @param pRelativeUri
   *          source uri
   * @return ticket string
   */
  private String getTicket(final String pRelativeUri)
  {
    String ticket = null;
    final StringTokenizer token = new StringTokenizer(pRelativeUri, "/");
    if (token.countTokens() == 2)
    {
      if (token.nextToken().equals(PortalContentUri.RECOVERY_PASSWORD.getRelativeUri()))
      {
        ticket = token.nextToken();
      }
    }
    return ticket;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean refreshPortalContent(final Map<KEY, String> pMap, final String pModuleId)
  {
    return false;
  }

}