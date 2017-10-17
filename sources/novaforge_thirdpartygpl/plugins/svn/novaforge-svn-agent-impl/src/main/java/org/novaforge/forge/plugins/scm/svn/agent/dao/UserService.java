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
package org.novaforge.forge.plugins.scm.svn.agent.dao;

import java.util.List;

import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;
import org.novaforge.forge.plugins.scm.svn.domain.Membership;
import org.novaforge.forge.plugins.scm.svn.domain.Role;
import org.novaforge.forge.plugins.scm.svn.domain.User;

/**
 * @author sbenoist
 */

public interface UserService
{
  /**
   * add a membership for SVN repository
   * 
   * @param pMember
   * @param repositoryName
   * @return boolean
   * @throws PluginServiceException
   */
  boolean addMembership(Membership pMember, String repositoryName) throws SVNAgentException;

  /**
   * update a membership for SVN repository
   * 
   * @param pMember
   * @param repositoryName
   * @return boolean
   * @throws PluginServiceException
   */
  boolean updateMembership(Membership pMember, String repositoryName) throws SVNAgentException;

  /**
   * remove a membership for SVN repository
   * 
   * @param username
   * @param role
   * @param repositoryName
   * @return boolean
   * @throws PluginServiceException
   */
  boolean removeMembership(String username, Role role, String repositoryName) throws SVNAgentException;

  /**
   * create a SVN repository add add a collection of memberships to it
   * 
   * @param memberships
   * @param pRepositoriesPath
   * @param repositoryName
   * @return boolean
   * @throws PluginServiceException
   */
  boolean createProject(List<Membership> memberships, String pRepositoriesPath, String repositoryName)
      throws SVNAgentException;

  /**
   * delete a SVN repository
   * 
   * @param pRepositoriesPath
   * @param repositoryName
   * @return boolean
   * @throws PluginServiceException
   */
  boolean deleteProject(String pRepositoriesPath, String repositoryName) throws SVNAgentException;

  /**
   * update a user into SVN
   * 
   * @param user
   * @return boolean
   * @throws PluginServiceException
   */
  boolean updateUser(User user) throws SVNAgentException;

  /**
   * delete u user into SVN
   * 
   * @param user
   * @return boolean
   * @throws PluginServiceException
   */
  boolean deleteUser(User user) throws SVNAgentException;

  /**
   * find a µSVN user by its username
   * 
   * @param name
   * @return
   * @throws PluginServiceException
   */
  User findUserByName(String name) throws SVNAgentException;

}
