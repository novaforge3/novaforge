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
package org.novaforge.forge.tools.requirements.common.internal.services;

import org.novaforge.forge.tools.requirements.common.dao.MembershipDAO;
import org.novaforge.forge.tools.requirements.common.dao.ProjectDAO;
import org.novaforge.forge.tools.requirements.common.dao.RoleDAO;
import org.novaforge.forge.tools.requirements.common.dao.UserDAO;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementOrganizationServiceException;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.Membership;
import org.novaforge.forge.tools.requirements.common.model.Role;
import org.novaforge.forge.tools.requirements.common.model.User;
import org.novaforge.forge.tools.requirements.common.services.RequirementOrganizationService;

import java.util.List;
import java.util.Set;

/**
 * @author sbenoist
 */
public class RequirementOrganizationServiceImpl implements RequirementOrganizationService
{
  private RoleDAO       roleDAO;

  private MembershipDAO membershipDAO;

  private UserDAO       userDAO;

  private ProjectDAO    projectDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public User buildUser(final String pLogin, final String pFirstname, final String pLastname)
  {
    User user = userDAO.newUser();
    user.setLogin(pLogin);
    user.setFirstname(pFirstname);
    user.setLastname(pLastname);
    return user;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addMembership(final User pUser, final String pRoleName, final String pProjectId)
      throws RequirementOrganizationServiceException
  {
    try
    {
      // Check that the user exists
      String login = pUser.getLogin();

      User user = null;
      if (userDAO.exist(login))
      {
        user = userDAO.findByLogin(login);
      }
      else
      {
        user = userDAO.persist(pUser);
      }

      // find the role
      Role role = roleDAO.findRoleByName(pRoleName);

      // find the project
      IProject project = projectDAO.findProjectByID(pProjectId);

      // check if exists any membership for the user
      Membership membership = null;
      if (membershipDAO.exist(login, pProjectId))
      {
        membership = membershipDAO.findByUserAndProject(login, pProjectId);
        membership.setRole(role);
        membershipDAO.update(membership);
      }
      else
      {
        // build and persist the membership
        membership = membershipDAO.newMembership(user, project, role);
        membershipDAO.persist(membership);
      }

    }
    catch (Exception e)
    {
      throw new RequirementOrganizationServiceException(String.format(
          "an error occurred during adding a membership with [login=%s, role=%s, projectId=%s]",
          pUser.getLogin(), pRoleName, pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteMembership(final String pLogin, final String pProjectId)
      throws RequirementOrganizationServiceException
  {
    try
    {
      // find and delete the membership
      Membership membership = membershipDAO.findByUserAndProject(pLogin, pProjectId);
      membershipDAO.delete(membership);
    }
    catch (Exception e)
    {
      throw new RequirementOrganizationServiceException(
          String.format("an error occurred during deleting a membership with [login=%s, projectId=%s]",
              pLogin, pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteAllProjectMemberships(final String pProjectId)
      throws RequirementOrganizationServiceException
  {
    try
    {
      // Get all memberships for the project
      List<Membership> memberships = membershipDAO.findAllByProject(pProjectId);
      for (Membership membership : memberships)
      {
        membershipDAO.delete(membership);
      }
    }
    catch (Exception e)
    {
      throw new RequirementOrganizationServiceException(String.format(
          "an error occurred during deleting all memberships with [projectId=%s]", pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteUser(final String pLogin) throws RequirementOrganizationServiceException
  {
    try
    {
      // Get all the memberships for the user and delete them
      List<Membership> memberships = membershipDAO.findAllByUser(pLogin);
      for (Membership membership : memberships)
      {
        membershipDAO.delete(membership);
      }

      // delete the user
      User user = userDAO.findByLogin(pLogin);
      userDAO.delete(user);
    }
    catch (Exception e)
    {
      throw new RequirementOrganizationServiceException(String.format(
          "an error occurred during deleting user with [login=%s]", pLogin), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateUser(final User pUser) throws RequirementOrganizationServiceException
  {
    try
    {
      User user = userDAO.findByLogin(pUser.getLogin());
      user.setFirstname(pUser.getFirstname());
      user.setLastname(pUser.getLastname());
      userDAO.update(user);
    }
    catch (Exception e)
    {
      throw new RequirementOrganizationServiceException(String.format(
          "an error occurred during updating user with [login=%s]", pUser.getLogin()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Role> findAllRoles()
  {
    return roleDAO.findAllRoles();
  }

  public void setRoleDAO(final RoleDAO pRoleDAO)
  {
    roleDAO = pRoleDAO;
  }

  public void setMembershipDAO(final MembershipDAO pMembershipDAO)
  {
    membershipDAO = pMembershipDAO;
  }

  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
  }

  public void setProjectDAO(final ProjectDAO pProjectDAO)
  {
    projectDAO = pProjectDAO;
  }
}
