/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.plugins.scm.svn.agent.internal.facades;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.persistence.NoResultException;
import javax.xml.bind.DatatypeConverter;

import org.novaforge.forge.plugins.scm.svn.agent.dao.RepositoryDAO;
import org.novaforge.forge.plugins.scm.svn.agent.dao.RepositoryPathDAO;
import org.novaforge.forge.plugins.scm.svn.agent.dao.UserDAO;
import org.novaforge.forge.plugins.scm.svn.agent.dao.UserPermissionDAO;
import org.novaforge.forge.plugins.scm.svn.agent.dao.UserService;
import org.novaforge.forge.plugins.scm.svn.agent.entity.RepositoryEntity;
import org.novaforge.forge.plugins.scm.svn.agent.entity.RepositoryPathEntity;
import org.novaforge.forge.plugins.scm.svn.agent.entity.UserEntity;
import org.novaforge.forge.plugins.scm.svn.agent.entity.UserPermissionEntity;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNRepositoryException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNRepositoryService;
import org.novaforge.forge.plugins.scm.svn.constant.SVNConstants;
import org.novaforge.forge.plugins.scm.svn.domain.Membership;
import org.novaforge.forge.plugins.scm.svn.domain.Repository;
import org.novaforge.forge.plugins.scm.svn.domain.RepositoryPath;
import org.novaforge.forge.plugins.scm.svn.domain.Role;
import org.novaforge.forge.plugins.scm.svn.domain.User;
import org.novaforge.forge.plugins.scm.svn.domain.UserPermission;

/**
 * @author sbenoist
 */

public class UserServiceImpl implements UserService
{
  private UserDAO              userDAO;
  private UserPermissionDAO    userPermissionDAO;
  private RepositoryPathDAO    repositoryPathDAO;
  private RepositoryDAO        repositoryDAO;
  private SVNRepositoryService svnRepositoryService;

  /**
   * Convert password from hexadecimal to Base64 encoding
   * @param pPassword The password in hexadecimal encoding
   * @return The password in Base64 encoding
   */
  private static String convertPasswordToBase64( final String pPassword)
  {
    return "{SHA}" + DatatypeConverter.printBase64Binary(DatatypeConverter.parseHexBinary(pPassword));
  }
  
  @Override
  public boolean createProject(final List<Membership> pMemberships, final String pRepositoriesPath,
      final String pRepositoryName) throws SVNAgentException
  {
    // create physically the svn repository
    try
    {
      svnRepositoryService.createRepository(pRepositoriesPath, pRepositoryName);
    }
    catch (final SVNRepositoryException e)
    {
      throw new SVNAgentException("unable to create the SVN repository :" + pRepositoryName, e);
    }
    catch (final IOException e)
    {
      throw new SVNAgentException("unable to change permissions for the SVN repository :" + pRepositoryName,
          e);
    }

    final RepositoryPath repositoryPath = createRepositoryPath(pRepositoryName,
        SVNConstants.SVN_FULL_REPOSITORY_PATH);

    String name = null;
    User user = null;
    for (final Membership member : pMemberships)
    {
      // create the user if he doesn't exists

      name = member.getName();
      if (userDAO.existUser(name) == false)
      {
        user = createUser(name, member.getPassword());
      }
      else
      {
        user = findUserByName(name);
      }

      // create the permission
      createUserPermission(user, member.getRole(), repositoryPath);
    }
    return true;
  }

  @Override
  public boolean deleteProject(final String pRepositoriesPath, final String pRepositoryName)
      throws SVNAgentException
  {
    // delete the svn repository
    try
    {
      svnRepositoryService.deleteRepository(pRepositoriesPath, pRepositoryName);
    }
    catch (final IOException e)
    {
      throw new SVNAgentException("unable to delete the repository : " + pRepositoryName, e);
    }

    // get all the repository paths for this repository
    final List<RepositoryPath> repositoryPaths = repositoryPathDAO.findAllPathsByRepository(pRepositoryName);
    List<UserPermission> userPermissions = null;
    for (final RepositoryPath repositoryPath : repositoryPaths)
    {
      // get all the permissions for each path
      userPermissions = userPermissionDAO.findAllPermissionsByRepositoryPath(repositoryPath.getId());
      for (final UserPermission userPermission : userPermissions)
      {
        userPermissionDAO.delete(userPermission);
      }

      // delete the repository path
      repositoryPathDAO.delete(repositoryPath);
    }

    // delete the repository
    final Repository repository = repositoryDAO.findByName(pRepositoryName);
    repositoryDAO.delete(repository);

    return true;
  }

  @Override
  public boolean updateUser(final User pUser) throws SVNAgentException
  {
    // Get the persisted entity
    final User entity = findUserByName(pUser.getName());
    final String password = convertPasswordToBase64(pUser.getPassword());
    if (entity.getPassword().equals(password) == false)
    {
      entity.setPassword(password);
      userDAO.update(entity);
    }
    return true;
  }

  @Override
  public boolean deleteUser(final User pUser) throws SVNAgentException
  {
    // find and delete all the permissions for the user
    final List<UserPermission> permissions = userPermissionDAO.findAllPermissionsByUser(pUser.getName());
    for (final UserPermission userPermission : permissions)
    {
      userPermissionDAO.delete(userPermission);
    }

    // delete the persisted user entity
    final User entity = findUserByName(pUser.getName());
    userDAO.delete(entity);

    return true;
  }

  @Override
  public boolean addMembership(final Membership pMember, final String pRepositoryName)
      throws SVNAgentException
  {
    // Get the full repository path
    final RepositoryPath repositoryPath = repositoryPathDAO.findRepositoryPathByRepositoryAndPath(
        pRepositoryName, SVNConstants.SVN_FULL_REPOSITORY_PATH);
    // create the user if not exists
    final User userPersistent = createOrFindUser(pMember);

    // create the permission
    createUserPermission(userPersistent, pMember.getRole(), repositoryPath);

    return true;
  }

  @Override
  public boolean removeMembership(final String pUsername, final Role pRole, final String pRepositoryName)
      throws SVNAgentException
  {
    // Get the full repository path
    final RepositoryPath repositoryPath = repositoryPathDAO.findRepositoryPathByRepositoryAndPath(
        pRepositoryName, SVNConstants.SVN_FULL_REPOSITORY_PATH);
    // Get the permission for the user on the full repository path
    final UserPermission permission = userPermissionDAO.findPermissionsByUserAndPath(pUsername,
        repositoryPath.getId());

    userPermissionDAO.delete(userPermissionDAO.update(permission));

    return true;
  }

  @Override
  public boolean updateMembership(final Membership pMember, final String pRepositoryName)
      throws SVNAgentException
  {
    // Get the full repository path
    final RepositoryPath repositoryPath = repositoryPathDAO.findRepositoryPathByRepositoryAndPath(
        pRepositoryName, SVNConstants.SVN_FULL_REPOSITORY_PATH);

    // Get the permission for the user on the full repository path
    try
    {
      final UserPermission permission = userPermissionDAO.findPermissionsByUserAndPath(pMember.getName(),
          repositoryPath.getId());
      // update the permission
      permission.setRead(pMember.getRole().isRead());
      permission.setRecursive(pMember.getRole().isRecursive());
      permission.setWrite(pMember.getRole().isWrite());
      userPermissionDAO.update(permission);
    }
    catch (final NoResultException e)
    {
      // create the user if not exists
      final User userPersistent = createOrFindUser(pMember);
      // create the permission
      createUserPermission(userPersistent, pMember.getRole(), repositoryPath);
    }

    return true;
  }

  private User createOrFindUser(final Membership pMember) throws SVNAgentException
  {
    User userPersistent = null;
    final String username = pMember.getName();

    if (userDAO.existUser(username) == false)
    {
      userPersistent = createUser(username, pMember.getPassword());
    }
    else
    {
      userPersistent = findUserByName(username);
    }
    return userPersistent;
  }

  private void createUserPermission(final User pUser, final Role pRole, final RepositoryPath pRepositoryPath)
      throws SVNAgentException
  {
    final UserPermission userPermission = new UserPermissionEntity(pUser, pRepositoryPath, pRole.isRead(),
        pRole.isWrite(), pRole.isRecursive());
    try
    {
      userPermissionDAO.save(userPermission);
    }
    catch (final Exception e)
    {
      e.printStackTrace();
      throw new SVNAgentException(MessageFormat.format(
          "unable to create permission for username : {0} and repository : {1}", pUser.getName(),
          pRepositoryPath.getRepository().getName()), e);
    }
  }

  private RepositoryPath createRepositoryPath(final String pRepositoryName, final String pPath)
      throws SVNAgentException
  {
    final Repository repository = new RepositoryEntity(pRepositoryName);
    return repositoryPathDAO.save(new RepositoryPathEntity(repository, pPath));
  }

  private User createUser(final String pUsername, final String pPassword) throws SVNAgentException
  {
    final String password = convertPasswordToBase64(pPassword);
    return userDAO.persist(new UserEntity(pUsername, password));

  }

  @Override
  public User findUserByName(final String pName) throws SVNAgentException
  {
    try
    {
      return userDAO.update(userDAO.findByName(pName));
    }
    catch (final NoResultException e)
    {
      throw new SVNAgentException(MessageFormat.format("unable to get svn user with username : {0}", pName),
          e);
    }
  }

  /**
   * @param pUserDAO
   *          the userDAO to set
   */
  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
  }

  /**
   * @param pUserPermissionDAO
   *          the userPermissionDAO to set
   */
  public void setUserPermissionDAO(final UserPermissionDAO pUserPermissionDAO)
  {
    userPermissionDAO = pUserPermissionDAO;
  }

  /**
   * @param pRepositoryPathDAO
   *          the repositoryPathDAO to set
   */
  public void setRepositoryPathDAO(final RepositoryPathDAO pRepositoryPathDAO)
  {
    repositoryPathDAO = pRepositoryPathDAO;
  }

  /**
   * @param pRepositoryDAO
   *          the repositoryDAO to set
   */
  public void setRepositoryDAO(final RepositoryDAO pRepositoryDAO)
  {
    repositoryDAO = pRepositoryDAO;
  }

  /**
   * @param pSvnRepositoryService
   *          the svnRepositoryService to set
   */
  public void setSvnRepositoryService(final SVNRepositoryService pSvnRepositoryService)
  {
    svnRepositoryService = pSvnRepositoryService;
  }
}
