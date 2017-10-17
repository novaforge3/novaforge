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
package org.novaforge.forge.plugins.gcl.nexus.client.internal;

import java.io.IOException;
import java.util.List;

import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.ChangePasswordParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.DockerHostedRepositoryParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.HostedRepositoryParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.JSONResponse;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.MavenHostedRepositoryParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.RoleParam;
import org.novaforge.forge.plugins.gcl.nexus.client.internal.json.UserParam;
import org.novaforge.forge.plugins.gcl.nexus.domain.Repository;
import org.novaforge.forge.plugins.gcl.nexus.domain.RepositoryFormat;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestClientCustom;
import org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestException;
import org.sonatype.nexus.repository.maven.LayoutPolicy;
import org.sonatype.nexus.repository.maven.VersionPolicy;
import org.sonatype.nexus.repository.storage.WritePolicy;
import org.sonatype.nexus.security.role.Role;
import org.sonatype.nexus.security.role.RoleIdentifier;
import org.sonatype.nexus.security.user.User;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * This class is an implementation of NexusRestClientCustom interface.
 * 
 * @see org.novaforge.forge.plugins.gcl.nexus.rest.NexusRestClientCustom
 * @author lamirang
 */
public class NexusRestClientCustomImpl implements NexusRestClientCustom
{

  private static final String USER_NOT_FOUND_EXCEPTION = "UserNotFoundException";

  private RepositoryManager   repositoryManager;

  private SecurityManager     securityManager;

  @Override
  public void initConnectionSettings(String baseUrl, String username, String password)
      throws NexusRestException
  {

    this.repositoryManager = RepositoryManager.getInstance(baseUrl, username, password);
    this.securityManager = SecurityManager.getInstance(baseUrl, username, password);

  }

  @Override
  public Repository updateRepository(final Repository repository) throws NexusRestException
  {

    return null;
  }

  @Override
  public Repository getRepository(String name) throws NexusRestException
  {

    Repository repository = null;

    try
    {

      JSONResponse jsonResponse = this.repositoryManager.getRespository(name);

      if (jsonResponse.getStatus() == 200)
      {

        repository = RestClientHelper.convertJsonToJava(jsonResponse.getResult(), Repository.class);

      }
      else
      {

        throw new NexusRestException(
            String.format("Cannot get repository [name=%s] reason is (HTTP error %s : %s)", name,
                jsonResponse.getStatus(), jsonResponse.getResult()));
      }

    }
    catch (Exception e)
    {

      throw new NexusRestException(String.format("Cannot get repository [name=%s]", name), e);
    }
    return repository;
  }

  @Override
  public void deleteRepository(String repositoryName) throws NexusRestException
  {

    try
    {

      JSONResponse jsonResponse = this.repositoryManager.deleteRepository(repositoryName);

      if (jsonResponse.getStatus() != 200)
      {

        throw new NexusRestException(
            String.format("Cannot delete repository [name=%s] reason is (HTTP error %s : %s)", repositoryName,
                jsonResponse.getStatus(), jsonResponse.getResult()));
      }

    }
    catch (Exception e)
    {

      throw new NexusRestException(String.format("Cannot get repository with [name=%s]", repositoryName), e);
    }
  }

  @Override
  public boolean existsRepository(String repositoryId) throws NexusRestException
  {

    boolean ret = false;

    try
    {

      ret = this.repositoryManager.existsRepository(repositoryId);

    }
    catch (JsonProcessingException e)
    {

      // response KO
      throw new NexusRestException(
          String.format("Cannot get user [repositoryId=%s] caused by" + e.getMessage(), repositoryId), e);

    }
    catch (IOException e)
    {

      // connection KO
      throw new NexusRestException(
          String.format("Cannot get user [repositoryId=%s] caused by" + e.getMessage(), repositoryId), e);
    }

    return ret;
  }

  @Override
  public User createUser(final String userId, String firstName, String lastName, String email, boolean active,
      String password, String... roleIds) throws NexusRestException
  {

    User ret = null;
    UserParam userParam = new UserParam(userId, firstName, lastName, email, true, password);

    for (String roleId : roleIds)
    {

      userParam.addRoleId(roleId);
    }

    try
    {

      JSONResponse jsonResponse = this.securityManager.addUser(userParam);

      if (jsonResponse.getStatus() == 200)
      {

        ret = extractUser(jsonResponse);

      }
      else
      {
        // connection OK but return KO
        throw new NexusRestException(
            String.format("Cannot create user [id=%s, name=%s, firstname=%s, email=%s, pwd=%s]", userId,
                lastName, firstName, email, password) + " -  Http error code: " + jsonResponse.getStatus()
                + " - reason : " + jsonResponse.getResult());
      }

    }
    catch (Exception e)
    {

      // IOException --> connection KO
      // JSONProcessingException --> response KO
      throw new NexusRestException(
          String.format("Cannot create user [id=%s, name=%s, firstname=%s, email=%s, pwd=%s]", userId,
              lastName, firstName, email, password),
          e);
    }

    return ret;
  }

  @Override
  public void updateUserPassword(String userId, String newPassword)
      throws NexusRestException
  {

    try
    {

      ChangePasswordParam changePasswordParam = new ChangePasswordParam(userId, newPassword);

      JSONResponse jsonResponse = this.securityManager.changePassword(changePasswordParam);

      if (jsonResponse.getStatus() != 200)
      {

        // connection OK but return KO
        throw new NexusRestException(String.format("Cannot change the user password [userId=%s]", userId)
            + " -  Http error code: " + jsonResponse.getStatus() + " - reason : " + jsonResponse.getResult());
      }

    }
    catch (Exception e)
    {

      // IOException --> connection KO
      // JSONProcessingException --> response KO
      throw new NexusRestException(String.format("Cannot change the user password [userId=%s]", userId), e);
    }

  }

  @Override
  public User updateUser(final User user) throws NexusRestException
  {

    User ret = null;

    try
    {

      JSONResponse jsonResponse = this.securityManager.updateUser(user);

      if (jsonResponse.getStatus() == 200)
      {

        ret = extractUser(jsonResponse);

      }
      else
      {
        // connection OK but return KO
        throw new NexusRestException(
            String.format("Cannot update user [id=%s, name=%s, firstname=%s, email=%s]", user.getUserId(),
                user.getLastName(), user.getFirstName(), user.getEmailAddress()) + " -  Http error code: "
                + jsonResponse.getStatus() + " - reason : " + jsonResponse.getResult());
      }

    }
    catch (Exception e)
    {

      // IOException --> connection KO
      // JSONProcessingException --> response KO
      throw new NexusRestException(
          String.format("Cannot update user [id=%s, name=%s, firstname=%s, email=%s]", user.getUserId(),
              user.getLastName(), user.getFirstName(), user.getEmailAddress()),
          e);
    }

    return ret;
  }

  @Override
  public void deleteUser(String userId) throws NexusRestException
  {

    try
    {

      JSONResponse jsonResponse = this.securityManager.deleteUser(userId);

      if (jsonResponse.getStatus() != 200)
      {

        // connection OK but return KO
        throw new NexusRestException(String.format("Cannot delete user [userId=%s]", userId)
            + " -  Http error code: " + jsonResponse.getStatus() + " - reason : " + jsonResponse.getResult());
      }

    }
    catch (Exception e)
    {

      // IOException --> connection KO
      // JSONProcessingException --> response KO
      throw new NexusRestException(String.format("Cannot delete user [userId=%s]", userId), e);
    }

  }

  @Override
  public boolean existsUser(String userId) throws NexusRestException
  {

    boolean ret = false;

    JSONResponse jsonResponse = null;

    try
    {

      jsonResponse = this.securityManager.existsUser(userId);

      if (jsonResponse.getStatus() == 200)
      {

        ret = RestClientHelper.convertJsonToJava(jsonResponse.getResult(), Boolean.class);
      }

    }
    catch (JsonProcessingException e)
    {

      // response KO
      if (jsonResponse.getStatus() != 400 || !jsonResponse.getResult().contains(USER_NOT_FOUND_EXCEPTION))
      {
        // response KO for another reason than the user existence
        throw new NexusRestException(String.format("Cannot get user [userId=%s]", userId)
            + " -  Http error code: " + jsonResponse.getStatus() + " - reason : " + jsonResponse.getResult());
      }

    }
    catch (IOException e)
    {

      // connection KO
      throw new NexusRestException(
          String.format("Cannot get user [userId=%s] caused by" + e.getMessage(), userId), e);
    }

    return ret;
  }

  @Override
  public User getUser(String userId) throws NexusRestException
  {

    User ret = null;

    JSONResponse jsonResponse = null;

    try
    {

      jsonResponse = this.securityManager.getUser(userId);

      if (jsonResponse.getStatus() == 200)
      {

        ret = extractUser(jsonResponse);
      }

    }
    catch (JsonProcessingException e)
    {

      // response KO
      if (jsonResponse.getStatus() != 400 || !jsonResponse.getResult().contains(USER_NOT_FOUND_EXCEPTION))
      {
        // response KO for another reason than the user existence
        throw new NexusRestException(String.format("Cannot get user [userId=%s]", userId)
            + " -  Http error code: " + jsonResponse.getStatus() + " - reason : " + jsonResponse.getResult());
      }

    }
    catch (IOException e)
    {

      // connection KO
      throw new NexusRestException(
          String.format("Cannot get user [userId=%s] caused by" + e.getMessage(), userId), e);
    }

    return ret;
  }

  private User extractUser(JSONResponse jsonResponse)
      throws IOException, JsonParseException, JsonMappingException
  {
    User ret;
    SimpleModule module = new SimpleModule();

    module.addDeserializer(RoleIdentifier.class, new RoleIdentifierDeserializer(RoleIdentifier.class));

    ret = RestClientHelper.convertJsonToJava(jsonResponse.getResult(), User.class, module);
    return ret;
  }

  @Override
  public Role createRole(final Role role) throws NexusRestException
  {

    Role ret = null;

    String roleId = role.getRoleId();
    String roleName = role.getName();

    RoleParam roleParam = new RoleParam(roleId, roleName, role.getDescription());

    roleParam.getRoleIds().addAll(role.getRoles());
    roleParam.getPrivilegeIds().addAll(role.getPrivileges());

    try
    {

      JSONResponse jsonResponse = this.securityManager.addRole(roleParam);

      if (jsonResponse.getStatus() == 200)
      {

        ret = RestClientHelper.convertJsonToJava(jsonResponse.getResult(), Role.class);

      }
      else
      {
        // connection OK but return KO
        throw new NexusRestException(
            String.format("Cannot create role [roleId=%s, name=%s]", roleId, roleName)
                + " -  Http error code: " + jsonResponse.getStatus() + " - reason : "
                + jsonResponse.getResult());
      }

    }
    catch (Exception e)
    {

      // IOException --> connection KO
      // JSONProcessingException --> response KO
      throw new NexusRestException(String.format("Cannot create role [roleId=%s, name=%s]", roleId, roleName),
          e);
    }

    return ret;
  }

  @Override
  public void deleteRole(String roleId) throws NexusRestException
  {

    try
    {

      JSONResponse jsonResponse = this.securityManager.deleteRole(roleId);

      if (jsonResponse.getStatus() != 200)
      {

        // connection OK but return KO
        throw new NexusRestException(String.format("Cannot delete role [roleId=%s]", roleId)
            + " -  Http error code: " + jsonResponse.getStatus() + " - reason : " + jsonResponse.getResult());
      }

    }
    catch (Exception e)
    {

      // IOException --> connection KO
      // JSONProcessingException --> response KO
      throw new NexusRestException(String.format("Cannot create role [roleId=%s]", roleId), e);
    }

  }

  @Override
  public List<Role> getRoles() throws NexusRestException
  {

    List<Role> ret = null;

    try
    {

      JSONResponse jsonResponse = this.securityManager.getRoles();

      if (jsonResponse.getStatus() == 200)
      {

        ret = RestClientHelper.convertJsonToJavaListRoles(jsonResponse.getResult());

      }
      else
      {
        // connection OK but return KO
        throw new NexusRestException("Cannot get list of roles - Http error code: " + jsonResponse.getStatus()
            + " - reason : " + jsonResponse.getResult());
      }

    }
    catch (Exception e)
    {

      // IOException --> connection KO
      // JSONProcessingException --> response KO
      throw new NexusRestException("Cannot get list of roles", e);
    }

    return ret;
  }

  @Override
  public Repository createSimpleHostedRepository(String name, String blobStoreName,
      boolean strictContentTypeValidation, WritePolicy writePolicy, RepositoryFormat repositoryFormat)
      throws NexusRestException
  {

    Repository repository = null;
    JSONResponse jsonResponse = null;

    HostedRepositoryParam hostedRepositoryParam = new HostedRepositoryParam(name, blobStoreName,
        strictContentTypeValidation, writePolicy);

    try
    {
      switch (repositoryFormat)
      {
        case BOWER:
          jsonResponse = this.repositoryManager.createBowerHosted(hostedRepositoryParam);
          break;
        case NPM:
          jsonResponse = this.repositoryManager.createNpmHosted(hostedRepositoryParam);
          break;
        case NUGET:
          jsonResponse = this.repositoryManager.createNugetHosted(hostedRepositoryParam);
          break;
        case PYPI:
          jsonResponse = this.repositoryManager.createPyPiHosted(hostedRepositoryParam);
          break;
        case RAW:
          jsonResponse = this.repositoryManager.createRawHosted(hostedRepositoryParam);
          break;
        case RUBYGEMS:
          jsonResponse = this.repositoryManager.createRubygemsHosted(hostedRepositoryParam);
          break;

        default:
          break;
      }

      repository = extractRepository(name, jsonResponse);

    }
    catch (Exception e)
    {

      throw new NexusRestException(String.format("Cannot create repository with [name=%s]", name), e);
    }

    return repository;
  }

  @Override
  public Repository createDockerHostedRepository(String name, String blobStoreName,
      boolean strictContentTypeValidation, WritePolicy writePolicy, Integer httpPort, Integer httpsPort,
      boolean v1Enabled) throws NexusRestException
  {

    Repository repository = null;

    DockerHostedRepositoryParam dockerHostedRepositoryParam = new DockerHostedRepositoryParam(name,
        blobStoreName, strictContentTypeValidation, writePolicy, httpPort, httpsPort, v1Enabled);

    try
    {

      JSONResponse jsonResponse = this.repositoryManager.createDockerHosted(dockerHostedRepositoryParam);

      repository = extractRepository(name, jsonResponse);

    }
    catch (Exception e)
    {

      throw new NexusRestException(String.format("Cannot create repository with [name=%s]", name), e);
    }

    return repository;
  }

  @Override
  public Repository createMavenHostedRepository(final String name, final String blobStoreName,
      final boolean strictContentTypeValidation, final VersionPolicy versionPolicy,
      final WritePolicy writePolicy, final LayoutPolicy layoutPolicy) throws NexusRestException
  {

    Repository repository = null;

    MavenHostedRepositoryParam mavenHostedRepositoryParam = new MavenHostedRepositoryParam(name,
        blobStoreName, strictContentTypeValidation, writePolicy, versionPolicy, layoutPolicy);

    try
    {

      JSONResponse jsonResponse = this.repositoryManager.createMavenHosted(mavenHostedRepositoryParam);

      repository = extractRepository(name, jsonResponse);

    }
    catch (Exception e)
    {

      throw new NexusRestException(String.format("Cannot create repository with [name=%s]", name), e);
    }

    return repository;
  }

  private Repository extractRepository(final String name, JSONResponse jsonResponse)
      throws IOException, JsonParseException, JsonMappingException, NexusRestException
  {

    Repository repository = null;

    if (jsonResponse.getStatus() == 200)
    {

      repository = RestClientHelper.convertJsonToJava(jsonResponse.getResult(), Repository.class);

    }
    else
    {

      throw new NexusRestException(
          String.format("Cannot create repository with [name=%s] reason is (HTTP error %s : %s)", name,
              jsonResponse.getStatus(), jsonResponse.getResult()));
    }

    return repository;
  }
}
