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

import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.presenters.TemplatePresenter;
import org.novaforge.forge.core.organization.presenters.TemplateRolePresenter;
import org.novaforge.forge.core.organization.services.TemplateRoleService;

import java.util.List;

/**
 * Implementation of {@link TemplatePresenter}
 * 
 * @author Guillaume Lamirand
 * @see TemplatePresenter
 */
public class TemplateRolePresenterImpl implements TemplateRolePresenter
{
  // FIXME We should manage some authorizations
  /**
   * Reference to {@link TemplateRoleService} service injected by the container
   */
  private TemplateRoleService templateRoleService;

  /**
   * {@inheritDoc}
   */
  @Override
  public Role newRole()
  {
    return templateRoleService.newRole();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createRole(final Role pRole, final String pTemplateId) throws TemplateServiceException
  {
    templateRoleService.createRole(pRole, pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createSystemRole(final Role pRole, final String pTemplateId) throws TemplateServiceException
  {
    templateRoleService.createSystemRole(pRole, pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteRole(final String pRoleName, final String pTemplateId) throws TemplateServiceException
  {
    templateRoleService.deleteRole(pRoleName, pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Role> getAllRoles(final String pTemplateId) throws TemplateServiceException
  {
    return templateRoleService.getAllRoles(pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Role getRole(final String pRoleName, final String pTemplateId) throws TemplateServiceException
  {
    return templateRoleService.getRole(pRoleName, pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateRole(final String pOldName, final Role pRole, final String pTemplateId)
      throws TemplateServiceException
  {
    templateRoleService.updateRole(pOldName, pRole, pTemplateId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Role changeOrder(final String pTemplateId, final String pRoleName, final boolean pIncrease)
      throws TemplateServiceException
  {
    return templateRoleService.changeOrder(pTemplateId, pRoleName, pIncrease);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Role> getDefaultRoles()
  {
    return templateRoleService.getDefaultRoles();
  }

  /**
   * Use by container to inject {@link TemplateTemplateRoleServiceService} implementation
   * 
   * @param pTemplateRoleService
   *          the templateRoleService to set
   */
  public void setTemplateRoleService(final TemplateRoleService pTemplateRoleService)
  {
    templateRoleService = pTemplateRoleService;
  }
}
