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
package org.novaforge.forge.plugins.delivery.deliverymanager.internal;

import org.novaforge.forge.core.plugins.categories.Category;
import org.novaforge.forge.core.plugins.dao.UuidDAO;
import org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginService;
import org.novaforge.forge.plugins.commons.services.domains.PluginMetadataImpl;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryOrganizationServiceException;
import org.novaforge.forge.tools.deliverymanager.model.Role;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryOrganizationService;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Guillaume Lamirand Mohamed IBN EL AZZOUZI, florent trolat
 * @date 25 juil. 2011
 */
public class DeliveryManagerServiceImpl extends AbstractPluginService implements PluginService
{
  private DeliveryOrganizationService deliveryOrganizationService;

  /**
   * Default constructor
   * 
   * @param pUuidDAO
   *          uuiDAO service reference
   */
  public DeliveryManagerServiceImpl(final UuidDAO pUuidDAO)
  {
    super(pUuidDAO);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getIconPath()
  {
    return "/deliveries.png";
  }

  /**
   * Return alias defined for the default tool instance
   *
   * @return an alias
   */
  @Override
  protected String getDefaultToolAlias()
  {
    return SLASH + "delivery-management";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginServiceMetadata getMetadata() throws PluginServiceException
  {
    PluginServiceMetadata pluginServiceMetadata = null;
    if (getServiceUUID() != null)
    {
      pluginServiceMetadata = new PluginMetadataImpl(getServiceUUID(), getDescription(), getType(),
          Category.DELIVERY.getId(), new DeliveryManagerQueues(), getPluginViews(), getVersion());
    }
    return pluginServiceMetadata;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> findRoles() throws PluginServiceException
  {
    try
    {
      final Set<String> returnList = new HashSet<String>();
      final Set<Role> roles = deliveryOrganizationService.findAllRoles();
      for (final Role role : roles)
      {
        returnList.add(role.getName());
      }
      return returnList;
    }
    catch (final DeliveryOrganizationServiceException e)
    {
      throw new PluginServiceException("an error occurred during getting all roles", e);
    }
  }

  public void setDeliveryOrganizationService(final DeliveryOrganizationService pDeliveryOrganizationService)
  {
    deliveryOrganizationService = pDeliveryOrganizationService;
  }

}
