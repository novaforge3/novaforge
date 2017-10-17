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

import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.internal.model.AuthorizationResourceImpl;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This is an implementation of {@link ApplicationPresenter}
 * 
 * @author Guillaume Lamirand
 */
public class ApplicationPresenterImpl implements ApplicationPresenter
{
  /**
   * Reference to {@link ApplicationService} service injected by the container
   */
  private ApplicationService applicationService;
  /**
   * Reference to {@link SecurityDelegate} service injected by the container
   */
  private SecurityDelegate   securityDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.ADD_APPLICATION)
  public ProjectApplication addApplication(@HistorizableParam(label = "projectId") final String pProjectId,
      @HistorizableParam(label = "space uri") final String pParentNodeUri, @HistorizableParam(
          label = "application Label") final String pApplicationLabel, final String pDescription,
      @HistorizableParam(label = "plugin UUID") final UUID pPluginUUID,
      final Map<String, String> pRolesMapping) throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class,
        PermissionAction.CREATE));
    final String username = securityDelegate.getCurrentUser();
    return applicationService.addApplication(pProjectId, pParentNodeUri, pApplicationLabel, pDescription,
        pPluginUUID, pRolesMapping, username);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplication updateDescription(final String pProjectId, final String pApplicationUri,
                                              final String pDescription) throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class,
                                                                             PermissionAction.UPDATE));
    return applicationService.updateDescription(pApplicationUri, pDescription);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.REMOVE_APPLICATION)
  public void removeApplication(@HistorizableParam(label = "projectId") final String pProjectId,
                                @HistorizableParam(label = "application URI") final String pApplicationUri)
      throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class, PermissionAction.READ,
                                                                             PermissionAction.DELETE));
    final String username = securityDelegate.getCurrentUser();
    applicationService.deleteApplication(pProjectId, pApplicationUri, username);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplication getApplication(final String pProjectId, final String pApplicationUri)
      throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class,
        PermissionAction.READ));
    return applicationService.getApplication(pApplicationUri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplication getApplication(final String pProjectId, final UUID pInstanceUUID)
      throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class,
        PermissionAction.READ));
    return applicationService.getApplication(pInstanceUUID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.GET_ALL_APPLICATIONS)
  public List<ProjectApplication> getAllProjectApplications(@HistorizableParam(label = "projectId")
                                                            final String pProjectId) throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class, PermissionAction.READ));
    final List<ProjectApplication> applications = applicationService.getAllProjectApplications(pProjectId);
    return doFilter(pProjectId, applications);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.GET_ALL_APPLICATIONS)
  public List<ProjectApplication> getAllProjectApplications(@HistorizableParam(label = "projectId")
                                                            final String pProjectId, final String... pPluginsUUID)
      throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class,
        PermissionAction.READ));
    final List<ProjectApplication> applications = applicationService.getAllProjectApplications(pProjectId,
                                                                                               pPluginsUUID);
    return doFilter(pProjectId, applications);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getRoleMapping(final String pProjectId, final String pApplicationUri)
      throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class,
        PermissionAction.READ));
    return applicationService.getRoleMapping(pApplicationUri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.UPDATE_APPLICATION)
  public void updateRoleMapping(@HistorizableParam(label = "projectId") final String pProjectId,
                                @HistorizableParam(label = "application URI") final String pApplicationUri,
                                final Map<String, String> pRoleMapping) throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class, PermissionAction.READ,
                                                                             PermissionAction.UPDATE));
    final String username = securityDelegate.getCurrentUser();
    applicationService.updateRoleMapping(pProjectId, pApplicationUri, pRoleMapping, username);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectApplication> getAllSpaceApplications(final String pSpaceNodeUri, final String pProjectId)
      throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Application.class, PermissionAction.READ));
    final List<ProjectApplication> applications = applicationService.getAllSpaceApplications(pSpaceNodeUri, pProjectId);
    return doFilter(pProjectId, applications);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addApplicationAccessToRole(final String pProjectId, final String pRoleName,
      final String pApplicationUri, final PermissionAction... pActions) throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Role.class, PermissionAction.CREATE,
                                                                             PermissionAction.UPDATE));
    applicationService.addApplicationAccessToRole(pProjectId, pRoleName, pApplicationUri, pActions);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateApplicationAccessToRole(final String pProjectId, final String pRoleName,
                                            final String pApplicationUri, final PermissionAction... pActions)
      throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Role.class,
        PermissionAction.READ, PermissionAction.UPDATE));
    applicationService.updateApplicationAccessToRole(pProjectId, pRoleName, pApplicationUri, pActions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ProjectApplication> getApplicationAccessForRole(final String pProjectId, final String pRoleName)
      throws ApplicationServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Role.class,
        PermissionAction.READ));
    return applicationService.getApplicationAccessForRole(pProjectId, pRoleName);
  }

  /**
   * This method filters the nodes in function of the permissions associated for
   * the current user
   *
   * @param pProjectId
   * @param pNodes
   *
   * @return
   */
  private List<ProjectApplication> doFilter(final String pProjectId, final List<ProjectApplication> pApplications)
  {
    final List<ProjectApplication> filteredApplications = new ArrayList<ProjectApplication>();

    if (pApplications != null)
    {
      for (final ProjectApplication application : pApplications)
      {

        if (securityDelegate.isPermitted(pProjectId, application.getPluginInstanceUUID().toString(),
                                         PermissionAction.READ))
        {
          filteredApplications.add(application);
        }

      }
    }
    return filteredApplications;
  }

  /**
   * @param applicationService
   *          the applicationService to set
   */
  public void setApplicationService(final ApplicationService applicationService)
  {
    this.applicationService = applicationService;
  }

  /**
   * @param securityDelegate
   *          the securityDelegate to set
   */
  public void setSecurityDelegate(final SecurityDelegate securityDelegate)
  {
    this.securityDelegate = securityDelegate;
  }

}
