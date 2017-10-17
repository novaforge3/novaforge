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
import org.novaforge.forge.commons.technical.validation.ValidationService;
import org.novaforge.forge.commons.technical.validation.ValidatorResponse;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.dao.RoleDAO;
import org.novaforge.forge.core.organization.dao.TemplateDAO;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.services.TemplateRoleService;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link TemplateRoleService}
 * 
 * @author Guillaume Lamirand
 * @author sbenoist
 * @see TemplateRoleService
 */
public class TemplateRoleServiceImpl implements TemplateRoleService
{

  private static final Log LOGGER = LogFactory.getLog(TemplateRoleServiceImpl.class);
  /**
   * Reference to {@link RoleDAO} service injected by the container
   */
  private RoleDAO                   roleDAO;
  /**
   * Reference to {@link TemplateDAO} service injected by the container
   */
  private TemplateDAO               templateDAO;
  /**
   * Reference to {@link ValidationService} service injected by the container
   */
  private ValidationService         validationService;
  /**
   * Reference to {@link ForgeCfgService} service injected by the container
   */
  private ForgeConfigurationService forgeConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public Role newRole()
  {
    return roleDAO.newRole();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createRole(final Role pRole, final String pTemplateId) throws TemplateServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input : role = " + pRole.toString());
      LOGGER.debug("Input : pTemplateId = " + pTemplateId);
    }

    final Role role = checkRole("", pRole, pTemplateId);
    try
    {
      final Template template = templateDAO.findByTemplateId(pTemplateId);
      role.setRealmType(RealmType.USER);
      template.addRole(pRole);
      templateDAO.update(template);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createSystemRole(final Role pRole, final String pTemplateId) throws TemplateServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input : role = " + pRole.toString());
      LOGGER.debug("Input : pTemplateId = " + pTemplateId);
    }

    final Role role = checkRole("", pRole, pTemplateId);
    try
    {
      final Template template = templateDAO.findByTemplateId(pTemplateId);
      role.setRealmType(RealmType.SYSTEM);
      template.addRole(pRole);
      templateDAO.update(template);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws TemplateServiceException
   */
  @Override
  public void deleteRole(final String pRoleName, final String pTemplateId) throws TemplateServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input : pTemplateId = " + pTemplateId);
    }

    try
    {
      // find the template entity
      final Template template = templateDAO.findByTemplateId(pTemplateId);

      // find the role
      final Role role = roleDAO.findByNameAndElement(pTemplateId, pRoleName);

      // delete the association role - project
      template.removeRole(role);
      templateDAO.update(template);

      // update role orders bigger than its deleted
      roleDAO.updateOrdersBiggerThan(pTemplateId, role.getOrder());
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Role> getAllRoles(final String pTemplateId) throws TemplateServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input : pTemplateId = " + pTemplateId);
    }

    try
    {
      return roleDAO.findAllRole(pTemplateId);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Role getRole(final String pRoleName, final String pTemplateId) throws TemplateServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input : roleName = " + pRoleName);
      LOGGER.debug("Input : tempalteId = " + pTemplateId);
    }

    try
    {
      return roleDAO.findByNameAndElement(pTemplateId, pRoleName);
    }
    catch (final NoResultException e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateRole(final String pOldName, final Role pRole, final String pTemplateId)
      throws TemplateServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input : role = " + pRole.toString());
      LOGGER.debug("Input : pTemplateId = " + pTemplateId);
    }
    final Role role = checkRole(pOldName, pRole, pTemplateId);

    try
    {
      roleDAO.update(role);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Role changeOrder(final String pTemplateId, final String pRoleName, final boolean pIncrease)
      throws TemplateServiceException
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Input : roleName = " + pRoleName);
      LOGGER.debug("Input : pTemplateId=" + pTemplateId);
    }

    try
    {
      return roleDAO.changeOrder(pTemplateId, pRoleName, pIncrease);
    }
    catch (final Exception e)
    {
      throw new TemplateServiceException("a technical error occured", e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Role> getDefaultRoles()
  {
    final List<Role> roles = new ArrayList<Role>();

    // Getting the administrator role name
    final String administratorName = forgeConfigurationService.getForgeAdministratorRoleName();

    // Instantiate new role and set the name and system type
    final Role newRole = roleDAO.newRole();
    newRole.setName(administratorName);
    newRole.setRealmType(RealmType.SYSTEM);
    newRole.setOrder(1);
    roles.add(newRole);

    return roles;
  }

  private Role checkRole(final String pOldRoleName, final Role pRole, final String pTemplateId)
      throws TemplateServiceException
  {

    // check the role doesn't already exists for the project
    if ((!pOldRoleName.equals(pRole.getName())) && (roleDAO.existRole(pRole.getName(), pTemplateId)))
    {
      throw new TemplateServiceException(ExceptionCode.ERR_CREATE_ROLE_ROLENAME_ALREADY_EXIST,
                                         String.format("The role name=%s already exists.", pRole.getName()));
    }

    // validate the bean
    final ValidatorResponse response = validationService.validate(Role.class, pRole);
    if (!response.isValid())
    {
      LOGGER.error(ExceptionCode.ERR_VALIDATION_BEAN.toString() + " : " + response.getMessage());
      throw new TemplateServiceException(ExceptionCode.ERR_VALIDATION_BEAN, response.getMessage());
    }

    // add an order (the less important) if there is not
    if (pRole.getOrder() == null)
    {
      final int lastOrder = roleDAO.getMaxOrder(pTemplateId);
      final int newOrder = lastOrder + 1;
      pRole.setOrder(newOrder);
    }
    return pRole;
  }

  /**
   * Use by container to inject {@link RoleDAO} implementation
   * 
   * @param pRoleDAO
   *          the roleDAO to set
   */
  public void setRoleDAO(final RoleDAO pRoleDAO)
  {
    roleDAO = pRoleDAO;
  }

  /**
   * Use by container to inject {@link TemplateDAO} implementation
   * 
   * @param pTemplateDAO
   *          the templateDAO to set
   */
  public void setTemplateDAO(final TemplateDAO pTemplateDAO)
  {
    templateDAO = pTemplateDAO;
  }

  /**
   * Use by container to inject {@link ValidationService} implementation
   * 
   * @param pValidationService
   *          the validationService to set
   */
  public void setValidationService(final ValidationService pValidationService)
  {
    validationService = pValidationService;
  }

  /**
   * Use by container to inject {@link ForgeCfgService} implementation
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

}
