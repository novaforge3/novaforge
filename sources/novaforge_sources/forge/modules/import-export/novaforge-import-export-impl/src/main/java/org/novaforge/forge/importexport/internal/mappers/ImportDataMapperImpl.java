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
package org.novaforge.forge.importexport.internal.mappers;

import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.LanguagePresenter;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.importexport.datas.model.Groups.GroupElement;
import org.novaforge.forge.importexport.datas.model.LanguageType;
import org.novaforge.forge.importexport.datas.model.ProjectInfo;
import org.novaforge.forge.importexport.datas.model.Roles.RoleElement;
import org.novaforge.forge.importexport.datas.model.RolesMapping;
import org.novaforge.forge.importexport.datas.model.RolesMapping.RoleMapped;
import org.novaforge.forge.importexport.datas.model.Users.UserElement;
import org.novaforge.forge.importexport.exceptions.ImportExportServiceException;
import org.novaforge.forge.importexport.mappers.ImportDataMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service implementation of {@link ImportDataMapper}
 * 
 * @author Guillaume Lamirand
 */
public class ImportDataMapperImpl implements ImportDataMapper
{
  private LanguagePresenter       languagePresenter;

  private AuthentificationService authentificationService;

  private MembershipPresenter     membershipPresenter;

  /**
   * {@inheritDoc}
   */
  @Override
  public User updateEntity(final UserElement pUser, final User pUserEntity)
      throws ImportExportServiceException
  {
    pUserEntity.setLogin(pUser.getLogin());
    if (StringUtils.isNotEmpty(pUser.getLastname()))
    {
      pUserEntity.setName(pUser.getLastname());
    }
    else
    {
      pUserEntity.setName(pUserEntity.getLogin());
    }
    if (StringUtils.isNotEmpty(pUser.getFirstname()))
    {
      pUserEntity.setFirstName(pUser.getFirstname());
    }
    else
    {
      pUserEntity.setFirstName(pUserEntity.getName());
    }
    pUserEntity.setEmail(pUser.getEmail());
    String pwd = pUser.getPasswd();
    if (!StringUtils.isNotEmpty(pwd))
    {
      pwd = authentificationService.generateRandomPassword();
    }

    // Do not hash the password cause it's already hashed
    pUserEntity.setPassword(pwd);
    // pUserEntity.setPassword(authentificationService.hashPassword(pwd));
    try
    {
      if (pUser.getLanguage() != null)
      {
        pUserEntity.setLanguage(languagePresenter.getLanguage(pUser.getLanguage().value()));
      }
      else
      {
        pUserEntity.setLanguage(languagePresenter.getLanguage(LanguageType.FR.value()));
      }
    }
    catch (final LanguageServiceException e)
    {
      throw new ImportExportServiceException(String.format("Unable to retrieve language object from [id=%s]",
          pUser.getLanguage().name()), e);
    }
    return pUserEntity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Group updateEntity(final GroupElement pGroup, final Group pGroupEntity)
  {
    pGroupEntity.setName(pGroup.getId());
    pGroupEntity.setDescription(pGroup.getId());
    pGroupEntity.setVisible(pGroup.isPublic());
    return pGroupEntity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project updateEntity(final String pProjectId, final ProjectInfo pProjectInfo,
      final Project pProjectGot) throws ImportExportServiceException
  {
    pProjectGot.setElementId(pProjectId);
    pProjectGot.setName(pProjectInfo.getName());
    pProjectGot.setOrganization(null);
    if (StringUtils.isBlank(pProjectInfo.getAuthor()))
    {
      try
      {
        final List<User> allSuperAdmin = membershipPresenter.getAllSuperAdmin();
        if (!allSuperAdmin.isEmpty())
        {
          pProjectGot.setAuthor(allSuperAdmin.get(0).getLogin());
        }
        else
        {
          throw new ImportExportServiceException("Project author is empty but there is not super-admin users");
        }
      }
      catch (final ProjectServiceException e)
      {
        throw new ImportExportServiceException(
            "Project author is empty but unable to retrieve super-admin users", e);
      }
    }
    else
    {
      pProjectGot.setAuthor(pProjectInfo.getAuthor());
    }
    pProjectGot.setDescription(pProjectInfo.getDescription());
    pProjectGot.setLicenceType(pProjectInfo.getLicenceType());
    pProjectGot.setPrivateVisibility(pProjectInfo.isPrivate());
    return pProjectGot;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectRole updateEntity(final RoleElement pRole, final ProjectRole pRoleGot)
  {
    pRoleGot.setName(pRole.getName());
    pRoleGot.setDescription(pRole.getDescription());
    return pRoleGot;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> buildMapping(final RolesMapping pRolesMapping)
  {
    final Map<String, String> maps = new HashMap<String, String>();
    if (pRolesMapping != null)
    {
      final List<RoleMapped> roles = pRolesMapping.getRoleMapped();
      if (roles != null)
      {
        for (final RoleMapped role : roles)
        {
          maps.put(role.getForgeId(), role.getApplicationId());
        }
      }
    }
    return maps;
  }

  public void setLanguagePresenter(final LanguagePresenter pLanguagePresenter)
  {
    languagePresenter = pLanguagePresenter;
  }

  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  public void setMembershipPresenter(final MembershipPresenter pMembershipPresenter)
  {
    membershipPresenter = pMembershipPresenter;
  }

}
