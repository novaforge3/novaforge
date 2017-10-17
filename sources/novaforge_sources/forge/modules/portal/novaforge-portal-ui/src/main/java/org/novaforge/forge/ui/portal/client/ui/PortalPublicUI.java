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

import com.vaadin.ui.UI;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalContext.KEY;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalModuleService;
import org.novaforge.forge.ui.portal.event.AuthentificationChangedEvent.Status;
import org.novaforge.forge.ui.portal.osgi.OSGiHelper;

import java.util.Map;

/**
 * This {@link UI} will referer to public content
 * 
 * @author Guillaume Lamirand
 */
public class PortalPublicUI extends AbstractPortalUI
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
    final PortalContext publicContext = OSGiHelper.getPortalModuleService().buildContext(eventBus,
        getLocale(), null);
    final PortalModule publicModule = OSGiHelper.getPortalModuleService().getModule(
        PortalModuleId.PUBLIC.getId());
    final PortalComponent publicComponent = publicModule.createComponent(publicContext);

    portalLayout.addContent(publicComponent.getComponent());
    publicComponent.init();

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
    if ((portalModuleService != null) && (PortalModuleId.PUBLIC.getId().equals(pModuleId)))
    {
      final PortalContext buildContext = portalModuleService.buildContext(eventBus, getLocale(), pMap);
      // Initialize and add the footer content
      final PortalModule publicModule = portalModuleService.getModule(PortalModuleId.PUBLIC.getId());
      final PortalComponent publicComponent = publicModule.createComponent(buildContext);
      if (Status.UNAUTHENTIFICATED.equals(authStatus))
      {
        portalLayout.addContent(publicComponent.getComponent());
      }
      if (!PortalModuleId.UNAVAILABLE.getId().equals(publicModule.getId()))
      {
        modulesQueue.add(publicComponent);
      }
      isMain = true;
    }
    return isMain;
  }

}
