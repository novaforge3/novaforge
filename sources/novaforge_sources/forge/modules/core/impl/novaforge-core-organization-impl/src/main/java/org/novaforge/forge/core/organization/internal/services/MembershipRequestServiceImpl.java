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
package org.novaforge.forge.core.organization.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.dao.MembershipDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.dao.ProjectElementDAO;
import org.novaforge.forge.core.organization.dao.UserDAO;
import org.novaforge.forge.core.organization.delegates.MailDelegate;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.MailDelegateException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum;
import org.novaforge.forge.core.organization.model.enumerations.MembershipRequestStatus;
import org.novaforge.forge.core.organization.services.MembershipRequestService;
import org.novaforge.forge.core.organization.services.MembershipService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Implementation of {@link MembershipRequestService}
 * 
 * @author Guillaume Lamirand
 * @see MembershipRequestService
 */
public class MembershipRequestServiceImpl implements MembershipRequestService
{

  private static final Log LOG = LogFactory.getLog(MembershipRequestServiceImpl.class);
  /**
   * Reference to {@link ProjectDAO} service injected by the container
   */
  private ProjectDAO                projectDAO;
  /**
   * Reference to {@link ProjectElementDAO} service injected by the container
   */
  private ProjectElementDAO         projectElementDAO;
  /**
   * Reference to {@link UserDAO} service injected by the container
   */
  private UserDAO                   userDAO;
  /**
   * Reference to {@link MembershipService} service injected by the container
   */
  private MembershipService         membershipService;
  /**
   * Reference to {@link MembershipDAO} service injected by the container
   */
  private MembershipDAO             membershipDAO;
  /**
   * Reference to {@link ForgeConfigurationService} service injected by the container
   */
  private ForgeConfigurationService forgeConfigurationService;
  /**
   * Reference to {@link MailDelegate} service injected by the container
   */
  private MailDelegate              mailDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createRequest(final String pProjectId, final String pUserLogin, final String pMessage,
      final boolean pSendMail) throws ProjectServiceException
  {
    try
    {
      // Check the parameters
      checkParams(pProjectId, pUserLogin);

      // check if the user has already made a request for this project
      final List<MembershipRequest> pendingRequests = getInProcessRequests(pProjectId);
      for (final MembershipRequest membershipRequest : pendingRequests)
      {
        if (membershipRequest.getUser().getLogin().equals(pUserLogin))
        {
          throw new ProjectServiceException(
              ExceptionCode.ERR_PROJECT_MEMBERSHIP_REQUEST_ANOTHER_DEMAND_IS_PENDING, String.format(
                  "another request is pending for project=%s and login=%s", pProjectId, pUserLogin));
        }
      }

      // Create the request
      // Get project entity
      final Project project = projectDAO.findByProjectId(pProjectId);

      // Get user entity
      final User user = userDAO.findByLogin(pUserLogin);

      // Init a new request
      final MembershipRequest newRequest = membershipDAO.newRequest();

      newRequest.setUser(user);
      newRequest.setProject(project);
      newRequest.setMessage(pMessage);
      newRequest.setStatus(MembershipRequestStatus.IN_PROGRESS);

      // Attach request to project entity
      project.addRequest(newRequest);
      projectDAO.update(project);

      final List<Actor> listProjectAdmins = membershipService.getAllActorsForRole(pProjectId,
          forgeConfigurationService.getForgeAdministratorRoleName());

      // send an email
      sendAdminMails(pUserLogin, pProjectId, listProjectAdmins, pSendMail,
          MailDelegateEnum.MEMBERSHIP_REQUEST, pMessage);
    }
    catch (final Exception e)
    {
      if (e instanceof Exception)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ProjectServiceException
   */
  @Override
  public List<MembershipRequest> getHistoryRequests(final String pProjectId) throws ProjectServiceException
  {
    final List<MembershipRequest> returnList = new ArrayList<MembershipRequest>();
    try
    {
      final List<MembershipRequest> invalidRequest = membershipDAO.findAllRequestByProjectAndStatus(
          pProjectId, MembershipRequestStatus.INVALIDATED);
      if (invalidRequest != null)
      {
        returnList.addAll(invalidRequest);
      }
      final List<MembershipRequest> validRequest = membershipDAO.findAllRequestByProjectAndStatus(pProjectId,
          MembershipRequestStatus.VALIDATED);
      if (validRequest != null)
      {
        returnList.addAll(validRequest);
      }
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }
    return returnList;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ProjectServiceException
   */
  @Override
  public List<MembershipRequest> getInProcessRequests(final String pProjectId) throws ProjectServiceException
  {
    try
    {
      return membershipDAO.findAllRequestByProjectAndStatus(pProjectId,
          MembershipRequestStatus.IN_PROGRESS);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }

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
    try
    {
      return membershipDAO.findAllRequestByUserAndStatus(pUserLogin,
          MembershipRequestStatus.IN_PROGRESS);
    }
    catch (final Exception e)
    {
      throw new ProjectServiceException("a technical error occured", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void validateRequest(final String pProjectId, final String pUserLogin, final Set<String> pRoles,
      final String pDefaultRoleName, final String pCurrentUser, final boolean pSendMail)
      throws ProjectServiceException
  {
    try
    {
      // Check the parameters
      checkParams(pProjectId, pUserLogin);

      // Update status of the request to VALIDATED
      final MembershipRequest request = membershipDAO.findRequestByProjectAndUser(pProjectId, pUserLogin);
      request.setStatus(MembershipRequestStatus.VALIDATED);
      membershipDAO.update(request);

      membershipService.addUserMembership(pProjectId, request.getUser().getUuid(), pRoles, pDefaultRoleName,
          pCurrentUser, false);

      // send an email
      sendUserEmail(pProjectId, pUserLogin, pSendMail, MailDelegateEnum.VALIDATE_MEMBERSHIP_REQUEST, pRoles,
          null);
    }
    catch (final ProjectServiceException e)
    {
      throw new ProjectServiceException(String.format(
          "Unable to add user membership with [login=%s, projectId=%s", pUserLogin, pProjectId), e);
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void invalidateRequest(final String pProjectId, final String pUserLogin, final String pMesssage,
      final boolean pSendMail) throws ProjectServiceException
  {
    try
    {
      // Check the parameters
      checkParams(pProjectId, pUserLogin);

      // Update status of the request to VALIDATED
      final MembershipRequest request = membershipDAO.findRequestByProjectAndUser(pProjectId, pUserLogin);
      request.setStatus(MembershipRequestStatus.INVALIDATED);
      membershipDAO.update(request);

      // send an email
      sendUserEmail(pProjectId, pUserLogin, true, MailDelegateEnum.INVALIDATE_MEMBERSHIP_REQUEST, null,
          pMesssage);
    }
    catch (final Exception e)
    {
      if (e instanceof ProjectServiceException)
      {
        throw (ProjectServiceException) e;
      }
      else
      {
        throw new ProjectServiceException("a technical error occured", e);
      }
    }
  }

  private void sendUserEmail(final String pProjectId, final String pLogin, final boolean pSendMail,
      final MailDelegateEnum pMailDelegateEnum, final Set<String> pRoles, final String pMessage)
      throws ProjectServiceException
  {
    if (pSendMail)
    {
      try
      {
        final User user = userDAO.findByLogin(pLogin);
        final Locale locale = user.getLanguage().getLocale();
        final Project project = projectDAO.findByProjectId(pProjectId);
        if (pMailDelegateEnum.equals(MailDelegateEnum.VALIDATE_MEMBERSHIP_REQUEST))
        {
          mailDelegate.sendProjectMembershipRequest(pLogin, project, user, pRoles, pMailDelegateEnum, null,
              locale);
        }
        else
        // Invalidation
        {
          mailDelegate.sendProjectMembershipRequest(pLogin, project, user, null, pMailDelegateEnum, pMessage,
              locale);
        }
      }
      catch (final MailDelegateException e)
      {
        throw new ProjectServiceException(
            String.format("Unable to send email to user with [user=%s]", pLogin), e);
      }
    }
  }

  private void checkParams(final String pProjectId, final String pUserLogin) throws ProjectServiceException
  {
    if (!userDAO.existLogin(pUserLogin))
    {
      LOG.error(ExceptionCode.ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST.toString());
      throw new ProjectServiceException(ExceptionCode.ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST,
                                        String.format("the user %s does not exists", pUserLogin));
    }
    if (!projectElementDAO.existId(pProjectId))
    {
      LOG.error(ExceptionCode.ERR_PROJECT_NOT_EXIST.toString());
      throw new ProjectServiceException(ExceptionCode.ERR_PROJECT_NOT_EXIST,
                                        String.format("the project %s does not exists", pProjectId));
    }
  }

  private void sendAdminMails(final String pUserLogin, final String pProjectId,
      final List<Actor> pListProjectAdmins, final boolean pSendMail,
      final MailDelegateEnum pMailDelegateEnum, final String pMessage) throws ProjectServiceException
  {
    if (pSendMail)
    {
      // for each admin of the project
      for (final Actor actor : pListProjectAdmins)
      {
        final User user = (User) actor;
        // send the mail
        try
        {
          final Locale locale = user.getLanguage().getLocale();
          final Project project = projectDAO.findByProjectId(pProjectId);
          mailDelegate.sendProjectMembershipRequest(pUserLogin, project, user, null, pMailDelegateEnum,
              pMessage, locale);
        }
        catch (final MailDelegateException e)
        {
          throw new ProjectServiceException(
              ExceptionCode.ERR_PROJECT_MEMBERSHIP_REQUEST_SENDING_MAIL_TO_ADMINS, String.format(
                  "Unable to send email to user with [user_login=%s]", user.getLogin()), e);
        }
      }
    }
  }

  /**
   * Use by container to inject {@link ProjectDAO} implementation
   * 
   * @param pProjectDAO
   *          the projectDAO to set
   */
  public void setProjectDAO(final ProjectDAO pProjectDAO)
  {
    projectDAO = pProjectDAO;
  }

  /**
   * Use by container to inject {@link ProjectElementDAO} implementation
   * 
   * @param pProjectElementDAO
   *          the projectElementDAO to set
   */
  public void setProjectElementDAO(final ProjectElementDAO pProjectElementDAO)
  {
    projectElementDAO = pProjectElementDAO;
  }

  /**
   * Use by container to inject {@link UserDAO} implementation
   * 
   * @param pUserDAO
   *          the userDAO to set
   */
  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
  }

  /**
   * Use by container to inject {@link MembershipService} implementation
   * 
   * @param pMembershipService
   *          the membershipService to set
   */
  public void setMembershipService(final MembershipService pMembershipService)
  {
    membershipService = pMembershipService;
  }

  /**
   * Use by container to inject {@link MembershipDAO} implementation
   * 
   * @param pMembershipDAO
   *          the membershipDAO to set
   */
  public void setMembershipDAO(final MembershipDAO pMembershipDAO)
  {
    membershipDAO = pMembershipDAO;
  }

  /**
   * Use by container to inject {@link ForgeConfigurationService} implementation
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * Use by container to inject {@link MailDelegate} implementation
   * 
   * @param pMailDelegate
   *          the mailDelegate to set
   */
  public void setMailDelegate(final MailDelegate pMailDelegate)
  {
    mailDelegate = pMailDelegate;
  }

}
