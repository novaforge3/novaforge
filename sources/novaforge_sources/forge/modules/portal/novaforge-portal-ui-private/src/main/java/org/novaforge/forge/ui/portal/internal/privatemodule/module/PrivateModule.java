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
package org.novaforge.forge.ui.portal.internal.privatemodule.module;

import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.portal.services.PortalModuleService;
import org.novaforge.forge.portal.services.PortalService;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.WorkspacePresenter;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.WorkspaceView;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.WorkspaceViewImpl;

/**
 * @author Guillaume Lamirand
 */
public class PrivateModule implements PortalModule
{

  private static PortalService       PORTAL_SERVICE;
  private static PortalMessages      PORTAL_MESSAGES;
  private static PortalModuleService PORTAL_MODULE_SERVICE;
  private static ProjectPresenter    PROJECT_PRESENTER;

  /**
   * @return the {@link PortalService}
   */
  public static PortalService getPortalService()
  {
    return PORTAL_SERVICE;
  }

  /**
   * Use by container to inject {@link PortalService}
   *
   * @param pPortalService
   *          the portalService to set
   */
  public void setPortalService(final PortalService pPortalService)
  {
    PORTAL_SERVICE = pPortalService;
  }

  /**
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
   * @return the {@link PortalModuleService}
   */
  public static PortalModuleService getPortalModuleService()
  {
    return PORTAL_MODULE_SERVICE;
  }

  /**
   * Use by container to inject {@link PortalModuleService}
   *
   * @param pPortalModuleService
   *          the portalModuleService to set
   */
  public void setPortalModuleService(final PortalModuleService pPortalModuleService)
  {
    PORTAL_MODULE_SERVICE = pPortalModuleService;
  }

  /**
   * @return the {@link ProjectPresenter}
   */
  public static ProjectPresenter getProjectPresenter()
  {
    return PROJECT_PRESENTER;
  }

  /**
   * Use by container to inject {@link ProjectPresenter}
   *
   * @param pProjectPresenter
   *          the projectPresenter to set
   */
  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    PROJECT_PRESENTER = pProjectPresenter;
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
    return PortalModuleId.PRIVATE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalComponent createComponent(final PortalContext pPortalContext)
  {
    final WorkspaceView      view      = new WorkspaceViewImpl();
    return new WorkspacePresenter(view, pPortalContext);
  }

}
