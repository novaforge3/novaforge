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
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryOrganizationServiceException;
import org.novaforge.forge.tools.deliverymanager.model.User;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryOrganizationService;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 25 juil. 2011
 */
public class DeliveryManagerUserServiceImpl extends AbstractPluginUserService implements PluginUserService
{
  private DeliveryOrganizationService deliveryOrganizationService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean removeToolUser(final InstanceConfiguration instance, final PluginUser pluginUser)
      throws PluginServiceException
  {
    try
    {
      deliveryOrganizationService.deleteUser(pluginUser.getLogin());
      return true;
    }
    catch (DeliveryOrganizationServiceException e)
    {
      throw new PluginServiceException("an error occurred during removing tool user", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolUser(final InstanceConfiguration instance, final String userName,
                                   final PluginUser pluginUser) throws PluginServiceException
  {
    try
    {
      User userToUpdate = deliveryOrganizationService.buildUser(pluginUser.getLogin(), pluginUser.getFirstName(),
                                                                pluginUser.getName());
      deliveryOrganizationService.updateUser(userToUpdate);
      return true;
    }
    catch (DeliveryOrganizationServiceException e)
    {
      throw new PluginServiceException("an error occurred during updating tool user", e);
    }
  }

  @Override
  public void createAdministratorUser(final ToolInstance pInstance, final PluginUser pUser)
      throws PluginServiceException
  {
    // TODO Should be implemented later
  }

  public void setDeliveryOrganizationService(final DeliveryOrganizationService pDeliveryOrganizationService)
  {
    deliveryOrganizationService = pDeliveryOrganizationService;
  }

}
