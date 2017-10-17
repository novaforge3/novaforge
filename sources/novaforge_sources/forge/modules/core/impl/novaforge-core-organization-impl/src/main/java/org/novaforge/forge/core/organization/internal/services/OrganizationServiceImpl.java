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
/**
 * 
 */
package org.novaforge.forge.core.organization.internal.services;

import org.novaforge.forge.core.organization.dao.OrganizationDAO;
import org.novaforge.forge.core.organization.exceptions.OrganizationServiceException;
import org.novaforge.forge.core.organization.model.Organization;
import org.novaforge.forge.core.organization.services.OrganizationService;

import java.util.List;

/**
 * Implementation of {@link OrganizationService}
 * 
 * @author sbenoist
 * @author Guillaume Lamirand
 * @see OrganizationService
 */
public class OrganizationServiceImpl implements OrganizationService
{
  /**
   * Reference to {@link OrganizationDAO} service injected by the container
   */
  private OrganizationDAO organizationDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public Organization newOrganization()
  {
    return organizationDAO.newOrganization();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Organization getOrganization(final String pName) throws OrganizationServiceException
  {
    try
    {
      return organizationDAO.findByName(pName);
    }
    catch (final Exception e)
    {
      throw new OrganizationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Organization> getAllOrganizations() throws OrganizationServiceException
  {
    try
    {
      return organizationDAO.findAll();
    }
    catch (final Exception e)
    {
      throw new OrganizationServiceException("a technical error occured", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Organization createOrganization(final Organization pOrganization)
      throws OrganizationServiceException
  {
    try
    {
      return organizationDAO.persist(pOrganization);
    }
    catch (final Exception e)
    {
      throw new OrganizationServiceException("a technical error occured", e);
    }
  }

  /**
   * Use by container to inject {@link OrganizationDAO} implementation
   * 
   * @param pOrganizationDAO
   *          the organizationDAO to set
   */
  public void setOrganizationDAO(final OrganizationDAO pOrganizationDAO)
  {
    organizationDAO = pOrganizationDAO;
  }

}
