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
package org.novaforge.forge.ui.unavailable.internal.module;

import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.unavailable.internal.client.UnavailablePresenter;
import org.novaforge.forge.ui.unavailable.internal.client.UnavailableView;
import org.novaforge.forge.ui.unavailable.internal.client.UnavailableViewImpl;

/**
 * This module is used when a required module is unavailable.
 * 
 * @author Guillaume Lamirand
 */
public class UnavailableModule implements PortalModule
{

  /**
   * Contains reference to {@link PortalMessages} implementation
   */
  private static PortalMessages PORTAL_MESSAGES;

  /**
   * Get implementation of {@link PortalMessages}
   *
   * @return the {@link PortalMessages}
   */
  public static PortalMessages getPortalMessages()
  {
    return PORTAL_MESSAGES;
  }

  /**
   * Use by container to inject {@link PortalMessages}
   *
   * @param pPortalMessages
   *          the portalMessages to set
   */
  public void setPortalMessages(final PortalMessages pPortalMessages)
  {
    PORTAL_MESSAGES = pPortalMessages;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId()
  {
    return getPortalModuleId().getId();
  }

  /**
   * Return the {@link PortalModuleId} associated to the current module
   *
   * @return {@link PortalModuleId}
   */
  public static PortalModuleId getPortalModuleId()
  {
    return PortalModuleId.UNAVAILABLE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalComponent createComponent(final PortalContext pPortalContext)
  {
    final boolean small = (pPortalContext.getAttributes() != null) && (pPortalContext.getAttributes()
                                                                                     .containsKey(PortalContext.KEY.SMALL));
    final UnavailableView      view      = new UnavailableViewImpl(small);
    return new UnavailablePresenter(pPortalContext, view);
  }
}
