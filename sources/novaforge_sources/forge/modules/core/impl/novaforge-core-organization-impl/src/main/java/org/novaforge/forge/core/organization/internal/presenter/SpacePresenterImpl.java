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
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.internal.model.AuthorizationResourceImpl;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.AuthorizationResource;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.core.organization.services.SpaceService;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This is an implementation of {@link SpacePresenter}
 * 
 * @author sbenoist
 * @author Guillaume Lamirand
 * @see SpacePresenter
 */
public class SpacePresenterImpl implements SpacePresenter
{
  /**
   * Reference to {@link SpaceService} service injected by the container
   */
  private SpaceService     spaceService;
  /**
   * Reference to {@link SecurityDelegate} service injected by
   * the container
   */
  private SecurityDelegate securityDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Space> getAllSpaces(final String pProjectId) throws SpaceServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Space.class, PermissionAction.READ));
    return spaceService.getAllSpaces(pProjectId);
  }

  /**
   * {@inheritDoc}
   *
   * @throws SpaceServiceException
   */
  @Override
  public Map<Space, List<ProjectApplication>> getAllSpacesWithApplications(final String pProjectId)
      throws SpaceServiceException
  {
    final List<AuthorizationResource> res = new ArrayList<AuthorizationResource>();
    res.add(new AuthorizationResourceImpl(Space.class, PermissionAction.READ));
    res.add(new AuthorizationResourceImpl(Application.class, PermissionAction.READ));
    securityDelegate.checkResources(pProjectId, res);

    final Map<Space, List<ProjectApplication>> returnSpaces = new HashMap<Space, List<ProjectApplication>>();
    final Map<Space, List<ProjectApplication>> allSpacesWithApplications = spaceService
                                                                               .getAllSpacesWithApplications(pProjectId);
    if (allSpacesWithApplications != null)
    {
      final Set<Entry<Space, List<ProjectApplication>>> entrySet = allSpacesWithApplications.entrySet();
      for (final Entry<Space, List<ProjectApplication>> entry : entrySet)
      {
        returnSpaces.put(entry.getKey(), doFilter(pProjectId, entry.getValue()));
      }
    }
    return returnSpaces;
  }

  /**
   * This method filters the nodes in function of the permissions associated for
   * the current user
   *
   * @param pProjectId
   * @param pNodes
   * @return
   */
  private List<ProjectApplication> doFilter(final String pProjectId,
      final List<ProjectApplication> pApplications)
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
   * {@inheritDoc}
   */
  @Override
  public Space getSpace(final String pProjectId, final String pSpaceNodeUri) throws SpaceServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Space.class,
        PermissionAction.READ));
    return spaceService.getSpace(pProjectId, pSpaceNodeUri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space updateSpace(final String pProjectId, final String pOldName, final Space pSpace)
      throws SpaceServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Space.class, PermissionAction.READ,
                                                                             PermissionAction.UPDATE));
    return spaceService.updateSpace(pProjectId, pOldName, pSpace);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSpace(final String pProjectId, final String pSpaceNodeUri) throws SpaceServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Space.class, PermissionAction.READ,
                                                                             PermissionAction.DELETE));
    spaceService.deleteSpace(pProjectId, pSpaceNodeUri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space addSpace(final String pProjectId, final Space pSpace) throws SpaceServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Space.class, PermissionAction.CREATE));
    return spaceService.addSpace(pProjectId, pSpace);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Space newSpace()
  {
    return spaceService.newSpace();
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

  /**
   * Use by container to inject {@link SpaceService} implementation
   * 
   * @param pSpaceService
   *          the spaceService to set
   */
  public void setSpaceService(final SpaceService pSpaceService)
  {
    spaceService = pSpaceService;
  }

}
