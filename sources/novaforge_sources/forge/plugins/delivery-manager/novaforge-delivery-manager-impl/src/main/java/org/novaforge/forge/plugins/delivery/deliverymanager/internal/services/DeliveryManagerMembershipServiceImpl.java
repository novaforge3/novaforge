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
package org.novaforge.forge.plugins.delivery.deliverymanager.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryOrganizationServiceException;
import org.novaforge.forge.tools.deliverymanager.model.User;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryOrganizationService;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 25 juil. 2011
 */
public class DeliveryManagerMembershipServiceImpl extends AbstractPluginMembershipService implements
    PluginMembershipService
{
  private DeliveryOrganizationService deliveryOrganizationService;

  public void setDeliveryOrganizationService(final DeliveryOrganizationService pDeliveryOrganizationService)
  {
    deliveryOrganizationService = pDeliveryOrganizationService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {
    try
    {
      User user = deliveryOrganizationService.buildUser(pUser.getLogin(), pUser.getFirstName(),
          pUser.getName());
      deliveryOrganizationService.addMembership(user, pToolRole, pInstance.getToolProjectId());
      return true;
    }
    catch (DeliveryOrganizationServiceException e)
    {
      throw new PluginServiceException("an error occurred during adding tool user membership", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {
    return addToolUserMemberships(pInstance, pUser, pToolRole);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean removeToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {
    try
    {
      deliveryOrganizationService.deleteMembership(pUser.getLogin(), pInstance.getToolProjectId());
      return true;
    }
    catch (DeliveryOrganizationServiceException e)
    {
      throw new PluginServiceException("an error occurred during removing tool user membership", e);
    }
  }

}
