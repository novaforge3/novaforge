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
package org.novaforge.forge.plugins.gcl.nexus.rest;

import java.util.List;

import org.novaforge.forge.plugins.gcl.nexus.domain.Repository;
import org.novaforge.forge.plugins.gcl.nexus.domain.RepositoryFormat;
import org.sonatype.nexus.repository.maven.LayoutPolicy;
import org.sonatype.nexus.repository.maven.VersionPolicy;
import org.sonatype.nexus.repository.storage.WritePolicy;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.user.User;

/**
 * This interface describes client service used to communicate with nexus
 * instance.
 * 
 * @author lamirang
 */
public interface NexusRestClientCustom {

	/**
	 * Set the t to nexus instance with specific url, username and password.
	 * 
	 * @param baseUrl
	 *            represents the url of nexus instance
	 * @param username
	 *            represents username used to log in
	 * @param password
	 *            represents password used to log in
	 * @throws NexusRestException
	 *             can occured if connection failed or client can be built
	 */
	void initConnectionSettings(String baseUrl, String username, String password) throws NexusRestException;

	/**
	 * Allow to create a simple repository (Bower, Npm, Nuget, Pypi, Raw, Rubygems) on nexus instance.
	 * <br/>
	 * NB: the repository creation on Nexus server implicitly produces the corresponding list of privileges.
	 * 
	 * @param name
	 * @param blobStoreName
	 * @param strictContentTypeValidation
	 * @param writePolicy
	 * @param repositoryFormat
	 * @return
	 * @throws NexusRestException
	 */
	Repository createSimpleHostedRepository(final String name, final String blobStoreName,
			final boolean strictContentTypeValidation, final WritePolicy writePolicy, RepositoryFormat repositoryFormat) throws NexusRestException;
	
	/**
	 * Allow to create a docker repository on nexus instance.
	 * <br/>
	 * NB: the repository creation on Nexus server implicitly produces the corresponding list of privileges.
	 * 
	 * @param name
	 * @param blobStoreName
	 * @param strictContentTypeValidation
	 * @param writePolicy
	 * @param httpPort
	 * @param httpsPort
	 * @param v1Enabled
	 * @return
	 * @throws NexusRestException
	 */
	Repository createDockerHostedRepository(final String name, final String blobStoreName,
			final boolean strictContentTypeValidation, final WritePolicy writePolicy, Integer httpPort, Integer httpsPort, boolean v1Enabled) throws NexusRestException;
	
	/**
	 * Allow to create a maven repository on nexus instance.
	 * <br/>
	 * NB: the repository creation on Nexus server implicitly produces the corresponding list of privileges.
	 * 
	 * @param name
	 * @param blobStoreName
	 * @param strictContentTypeValidation
	 * @param versionPolicy
	 * @param writePolicy
	 * @param layoutPolicy
	 * @return
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 */
	Repository createMavenHostedRepository(final String name, final String blobStoreName,
			final boolean strictContentTypeValidation, final VersionPolicy versionPolicy, final WritePolicy writePolicy,
			final LayoutPolicy layoutPolicy) throws NexusRestException;


	/**
	 * Allow to update a repository on nexus instance.
	 * 
	 * @param repository
	 * @return
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 */
	Repository updateRepository(final Repository repository) throws NexusRestException;

	/**
	 * Allow to get a repository resource from nexus instance.
	 * 
	 * @param repositoryId
	 *            represents id used to search the repository
	 * @return repository resource regarding repository id
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 */
	Repository getRepository(String repositoryId) throws NexusRestException;

	/**
	 * Allow to delete a repository on nexus instance.
	 * 
	 * @param repositoryId
	 *            represents the repository'id used to delete a repository
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 */
	void deleteRepository(String repositoryId) throws NexusRestException;

  /**
   * Return true if repository exists
   * @param repositoryId id of repositoryId user to check
   * @return
   * @throws NexusRestException
   */
  public boolean existsRepository(String repositoryId) throws NexusRestException;

  /**
	 * Return true if user exists
	 * @param userId id of the user to check
	 * @return
	 * @throws NexusRestException
	 */
	public boolean existsUser(String userId) throws NexusRestException;
		
	/**
	 * Allow to create user on nexus instance.
	 * 
	 * @param userId
	 *            id of rhe user to create
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param active
	 * @param password
	 * @return
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 */
	User createUser(final String userId, String firstName, String lastName, String email, boolean active,
			String password, String...childRoles ) throws NexusRestException;

	/**
	 * Allow to update user on nexus instance.
	 *
	 * @param userId
	 * @param newPassword
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 * 
	 */
	void updateUserPassword(final String userId, String newPassword) throws NexusRestException;

	/**
	 * Allow to update user on nexus instance.
	 * 
	 * @param user
	 *            represents user to update
	 * @return UserResource response
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 */
	User updateUser(final User user) throws NexusRestException;

	/**
	 * Allow to delete user on nexus instance.
	 * 
	 * @param userId
	 *            represents user id to delete
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 */
	void deleteUser(String userId) throws NexusRestException;

	/**
	 * Allow to get user resource from nexus instance.
	 * 
	 * @param userId
	 *            represents user id to obtain
	 * @return UserResource got
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 */
	User getUser(final String userId) throws NexusRestException;

	/**
	 * Allow to get roles from nexus instance.
	 * 
	 * @return list of roles resource
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 */
	List<Role> getRoles() throws NexusRestException;

	/**
	 * Allow to create role on nexus instance
	 * 
	 * @param role
	 * @return
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 */
	Role createRole(final Role role) throws NexusRestException;

	/**
	 * Allow to delete role on nexus instance.
	 * 
	 * @param roleId
	 *            represents role id to delete
	 * @throws NexusRestException
	 *             can occured if the communication with nexus instance failed
	 */
	void deleteRole(final String roleId) throws NexusRestException;

}