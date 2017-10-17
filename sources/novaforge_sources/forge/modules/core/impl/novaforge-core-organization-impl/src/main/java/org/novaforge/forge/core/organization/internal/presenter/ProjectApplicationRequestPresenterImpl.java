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
package org.novaforge.forge.core.organization.internal.presenter;

import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.internal.model.AuthorizationResourceImpl;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.ProjectApplicationRequest;
import org.novaforge.forge.core.organization.presenters.ProjectApplicationRequestPresenter;
import org.novaforge.forge.core.organization.services.ProjectApplicationRequestService;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of {@link ProjectApplicationRequestPresenter}
 * 
 * @author Guillaume Lamirand
 * @see ProjectApplicationRequestPresenter
 */
public class ProjectApplicationRequestPresenterImpl implements ProjectApplicationRequestPresenter
{

  /**
   * Reference to {@link SecurityDelegate} service injected by the container
   */
  private ProjectApplicationRequestService projectApplicationRequestService;
  /**
   * Reference to {@link SecurityDelegate} service injected by the container
   */
  private SecurityDelegate                 securityDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public void addRequest(final String pLogin, final String pProjectId, final UUID pInstanceId,
      final Map<String, String> pRoleMapping) throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class,
        PermissionAction.CREATE));
    projectApplicationRequestService.addRequest(pLogin, pProjectId, pInstanceId, pRoleMapping);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ApplicationServiceException
   */
  @Override
  public void deleteRequest(final String pProjectId, final UUID pInstanceId)
      throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class,
        PermissionAction.DELETE));
    projectApplicationRequestService.deleteRequest(pProjectId, pInstanceId);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectApplicationRequest> getByPluginUUID(final UUID pPluginUUID)
      throws ApplicationServiceException
  {

    return projectApplicationRequestService.getByPluginUUID(pPluginUUID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRequest(final String pProjectId, final UUID pPluginUUID)
      throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class,
        PermissionAction.READ));
    return projectApplicationRequestService.hasRequest(pProjectId, pPluginUUID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRequest(final ProjectApplicationRequest pProjectApplicationRequest,
      final UUID pToolInstanceUUID) throws ApplicationServiceException
  {
    projectApplicationRequestService.handleRequest(pProjectApplicationRequest, pToolInstanceUUID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleOldestRequest(final UUID pToolInstanceUUID, final UUID pPluginUUID)
      throws ApplicationServiceException
  {
    projectApplicationRequestService.handleOldestRequest(pToolInstanceUUID, pPluginUUID);

  }

  /**
   * Use by container to inject {@link ProjectApplicationRequestService} implementation
   * 
   * @param pProjectApplicationRequestService
   *          the projectApplicationRequestService to set
   */
  public void setProjectApplicationRequestService(
      final ProjectApplicationRequestService pProjectApplicationRequestService)
  {
    projectApplicationRequestService = pProjectApplicationRequestService;
  }

  /**
   * Use by container to inject {@link SecurityDelegate} implementation
   * 
   * @param pSecurityDelegate
   *          the securityDelegate to set
   */
  public void setSecurityDelegate(final SecurityDelegate pSecurityDelegate)
  {
    securityDelegate = pSecurityDelegate;
  }
}
