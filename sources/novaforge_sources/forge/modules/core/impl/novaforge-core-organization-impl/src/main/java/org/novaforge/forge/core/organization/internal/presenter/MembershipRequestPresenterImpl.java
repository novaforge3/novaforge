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
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.internal.model.AuthorizationResourceImpl;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.MembershipRequestPresenter;
import org.novaforge.forge.core.organization.services.MembershipRequestService;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.List;
import java.util.Set;

/**
 * This is an implementation of {@link MembershipPresenter}
 * 
 * @author Guillaume Lamirand
 * @see MembershipRequestPresenter
 */
public class MembershipRequestPresenterImpl implements MembershipRequestPresenter
{

  /**
   * Reference to {@link MembershipRequestService} service injected by the
   * container
   */
  private MembershipRequestService membershipRequestService;

  /**
   * Reference to {@link SecurityDelegate} service injected by the container
   */
  private SecurityDelegate         securityDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createRequest(final String pProjectId, final String pMessage, final boolean pSendMail)
      throws ProjectServiceException
  {
    final String username = securityDelegate.getCurrentUser();
    membershipRequestService.createRequest(pProjectId, username, pMessage, pSendMail);

  }

  /**
   * {@inheritDoc}
   * 
   * @throws ProjectServiceException
   */
  @Override
  public List<MembershipRequest> getHistoryRequests(final String pProjectId) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(MembershipRequest.class,
        PermissionAction.READ));
    return membershipRequestService.getHistoryRequests(pProjectId);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ProjectServiceException
   */
  @Override
  public List<MembershipRequest> getInProcessRequests(final String pProjectId) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(MembershipRequest.class,
        PermissionAction.READ));
    return membershipRequestService.getInProcessRequests(pProjectId);

  }

  /**
   * {@inheritDoc}
   * 
   * @throws ProjectServiceException
   */
  @Override
  public List<MembershipRequest> getInProcessRequestsForUser(final String pUserLogin)
      throws ProjectServiceException
  {
    return membershipRequestService.getInProcessRequestsForUser(pUserLogin);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void validateRequest(final String pProjectId, final String pUserLogin, final Set<String> pRoles,
      final String pDefaultRoleName, final boolean pSendMail) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(MembershipRequest.class,
        PermissionAction.READ, PermissionAction.UPDATE));
    final String username = securityDelegate.getCurrentUser();
    membershipRequestService.validateRequest(pProjectId, pUserLogin, pRoles, pDefaultRoleName, username,
        pSendMail);
    securityDelegate.clearPermissionCached(pUserLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void invalidateRequest(final String pProjectId, final String pUserLogin, final String pMesssage,
      final boolean pSendMail) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(MembershipRequest.class,
        PermissionAction.READ, PermissionAction.UPDATE));
    membershipRequestService.invalidateRequest(pProjectId, pUserLogin, pMesssage, pSendMail);

  }

  /**
   * Use by container to inject {@link MembershipRequestService} implementation
   * 
   * @param pMembershipRequestService
   *          the membershipRequestService to set
   */
  public void setMembershipRequestService(final MembershipRequestService pMembershipRequestService)
  {
    membershipRequestService = pMembershipRequestService;
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
