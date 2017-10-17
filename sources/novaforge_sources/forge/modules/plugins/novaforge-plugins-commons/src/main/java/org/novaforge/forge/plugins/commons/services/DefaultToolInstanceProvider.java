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
package org.novaforge.forge.plugins.commons.services;

import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.commons.webserver.configuration.model.WebServerProxyConfiguration;
import org.novaforge.forge.commons.webserver.configuration.services.WebServerConfigurator;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.dao.ToolInstanceDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstanceStatus;
import org.novaforge.forge.core.plugins.exceptions.ToolInstanceProvisioningException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.ToolInstanceProvisioningService;
import org.novaforge.forge.plugins.commons.persistence.entity.ToolInstanceEntity;
import org.novaforge.forge.plugins.commons.services.domains.PluginProxyConfigurationImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sbenoist
 */
public class DefaultToolInstanceProvider implements ToolInstanceProvisioningService
{

  protected static final Pattern ALIAS_PATTERN = Pattern.compile("^(/)[\\w-/]*");
  /**
   * Reference to service implementation of {@link ToolInstanceDAO}
   */
  private ToolInstanceDAO            toolInstanceDAO;
  /**
   * Reference to service implementation of {@link InstanceConfigurationDAO}
   */
  private InstanceConfigurationDAO   instanceConfigurationDAO;
  /**
   * Reference to service implementation of {@link WebServerConfigurator}
   */
  private WebServerConfigurator      webServerConfigurator;
  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;
  /**
   * Reference to service implementation of {@link ForgeConfigurationService}
   */
  private ForgeConfigurationService  forgeConfigurationService;
  /**
   * Service Property injected at instanciation (do not set default value to false for IPojo setting)
   */
  private boolean                    provisionable;

  /**
   * {@inheritDoc}
   */
  @Override
  public ToolInstance getToolInstanceByName(final String pName) throws ToolInstanceProvisioningException
  {

    try
    {
      return toolInstanceDAO.findInstanceByName(pName);
    }
    catch (final Exception e)
    {
      throw new ToolInstanceProvisioningException(String.format("Unable to get tool instance for [name=%s]", pName), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToolInstance getToolInstanceByUUID(final UUID pUUID) throws ToolInstanceProvisioningException
  {
    try
    {
      return toolInstanceDAO.findInstanceByUUID(pUUID);
    }
    catch (final Exception e)
    {
      throw new ToolInstanceProvisioningException(String.format("Unable to get tool instance for [uuid=%s]", pUUID), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToolInstance getToolInstanceByHost(final String pHost) throws ToolInstanceProvisioningException
  {
    try
    {
      return toolInstanceDAO.findInstanceByHost(pHost);
    }
    catch (final Exception e)
    {
      throw new ToolInstanceProvisioningException(String.format("Unable to get tool instance for [host=%s]", pHost), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToolInstance addToolInstance(final ToolInstance pToolInstance)
      throws ToolInstanceProvisioningException
  {
    try
    {
      // Validate entity
      checkConstraints(pToolInstance);
      // Persist entity
      final ToolInstance persist = toolInstanceDAO.persist(pToolInstance);
      if ((!persist.isInternal()) && (persist.getBaseURL() != null))
      {
        // Write proxy configuration
        final WebServerProxyConfiguration proxyConfiguration = getProxyConfiguration(
            pluginConfigurationService.getWebServerConfName(), persist.getName(), persist.getAlias(), persist
                .getBaseURL().toExternalForm());
        webServerConfigurator.addProxySettings(proxyConfiguration);
      }
      return persist;
    }
    catch (final Exception e)
    {
      throw new ToolInstanceProvisioningException(String.format(
          "Unable to add tool instance with [instance=%s]", pToolInstance.toString()), e);
    }
  }

  private void checkConstraints(final ToolInstance pToolInstance) throws ToolInstanceProvisioningException
  {
    // Check uuid
    if (pToolInstance.getUUID() == null)
    {
      throw new ToolInstanceProvisioningException("the tool instance uuid cannot be null.");
    }
    if (!pToolInstance.isInternal())
    {
      // check the alias
      final Matcher aliasMatcher = ALIAS_PATTERN.matcher(pToolInstance.getAlias());
      if (!aliasMatcher.matches())
      {
        throw new ToolInstanceProvisioningException(String.format(
            "the alias %s doesn't match the authorized expression", pToolInstance.getAlias()));
      }
    }
  }

  /**
   * @param pWebServerName
   * @param pProxyId
   * @param pPublicAliases
   * @param pRedirectUrl
   *
   * @return
   */
  protected WebServerProxyConfiguration getProxyConfiguration(final String pWebServerName, final String pProxyId,
                                                              final String pPublicAliases, final String pRedirectUrl)
  {
    return new PluginProxyConfigurationImpl(forgeConfigurationService.getPublicUrl().toExternalForm(), pWebServerName,
                                            pProxyId, pPublicAliases, pRedirectUrl);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeToolInstance(final ToolInstance pToolInstance) throws ToolInstanceProvisioningException
  {
    try
    {
      final long applisNbr = countApplicationsByInstance(pToolInstance.getUUID());
      if (applisNbr > 0)
      {
        throw new ToolInstanceProvisioningException(
            "this instance can't be deleted because it hosts at least one application");
      }

      toolInstanceDAO.delete(pToolInstance);
      // Delete proxy configuration
      final WebServerProxyConfiguration proxyConfiguration = getProxyConfiguration(
          pluginConfigurationService.getWebServerConfName(), pToolInstance.getName(), null, null);
      webServerConfigurator.removeProxySettings(proxyConfiguration);
    }
    catch (final Exception e)
    {
      throw new ToolInstanceProvisioningException(String.format(
          "Unable to remove tool instance with [tool_instance=%s]", pToolInstance.toString()), e);
    }

  }

  private long countApplicationsByInstance(final UUID pToolUUID) throws ToolInstanceProvisioningException
  {
    try
    {
      return toolInstanceDAO.countApplicationsByInstance(pToolUUID);
    }
    catch (final Exception e)
    {
      throw new ToolInstanceProvisioningException(String.format("Unable to find tool instance with [uuid=%s]",
                                                                pToolUUID), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToolInstance updateToolInstance(final ToolInstance pToolInstance, final String pPreviousName)
      throws ToolInstanceProvisioningException
  {
    try
    {
      // Validate entity
      checkConstraints(pToolInstance);

      // Remove previous proxy settings
      if (!pToolInstance.isInternal())
      {
        String instanceName = pPreviousName;
        if (StringUtils.isBlank(instanceName))
        {
          instanceName = pToolInstance.getName();
        }
        // Delete proxy configuration
        final WebServerProxyConfiguration proxyConfiguration = getProxyConfiguration(
            pluginConfigurationService.getWebServerConfName(), instanceName, null, null);
        webServerConfigurator.removeProxySettings(proxyConfiguration);
      }

      // Check the number of applications associated to this tool
      if (!pToolInstance.isShareable())
      {
        final long applisNbr = countApplicationsByInstance(pToolInstance.getUUID());
        if (applisNbr > 1)
        {
          throw new ToolInstanceProvisioningException(
              "this instance can't be unshareable because it already hosts more than one application");
        }
      }
      // Update entity
      final ToolInstance update = toolInstanceDAO.update(pToolInstance);

      // Write proxy configuration
      if (!update.isInternal())
      {
        String baseURL = null;
        if (update.getBaseURL() != null)
        {
          baseURL = update.getBaseURL().toExternalForm();
        }
        final WebServerProxyConfiguration proxyConfiguration = getProxyConfiguration(
            pluginConfigurationService.getWebServerConfName(), update.getName(), update.getAlias(), baseURL);
        webServerConfigurator.addProxySettings(proxyConfiguration);
      }

      return update;
    }
    catch (final Exception e)
    {
      throw new ToolInstanceProvisioningException(String.format(
          "Unable to update tool instance with [tool_instance=%s]", pToolInstance.toString()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ToolInstance> getAllToolInstances() throws ToolInstanceProvisioningException
  {
    try
    {
      final List<ToolInstance> list = toolInstanceDAO.findAllInstances();
      return new HashSet<ToolInstance>(list);
    }
    catch (final Exception e)
    {
      throw new ToolInstanceProvisioningException("Unable to get all tool instances", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<ToolInstance> getAvailableToolInstances() throws ToolInstanceProvisioningException
  {
    return getToolInstancesByStatus(ToolInstanceStatus.AVAILABLE);
  }

  private Set<ToolInstance> getToolInstancesByStatus(final ToolInstanceStatus pStatus)
      throws ToolInstanceProvisioningException
  {
    final Set<ToolInstance> toolInstancesSet = new HashSet<ToolInstance>();

    try
    {
      final List<ToolInstance> instances = toolInstanceDAO.findAllInstances();
      for (final ToolInstance instance : instances)
      {
        if (instance.getToolInstanceStatus().equals(pStatus))
        {
          toolInstancesSet.add(instance);
        }
      }
    }
    catch (final Exception e)
    {
      throw new ToolInstanceProvisioningException("Unable to get tool instances", e);
    }

    return toolInstancesSet;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAvailableToolInstance() throws ToolInstanceProvisioningException
  {
    return !getToolInstancesByStatus(ToolInstanceStatus.AVAILABLE).isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToolInstance getToolInstanceByApplication(final String pInstanceID)
      throws ToolInstanceProvisioningException
  {
    try
    {
      final InstanceConfiguration instanceConfiguration = instanceConfigurationDAO
          .findByInstanceId(pInstanceID);
      return instanceConfiguration.getToolInstance();
    }
    catch (final Exception e)
    {
      throw new ToolInstanceProvisioningException(String.format(
          "Unable to get tool instance for [application instance_id=%s]", pInstanceID), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProvisionable()
  {
    return provisionable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToolInstance newToolInstance()
  {
    final ToolInstanceEntity toolInstanceEntity = new ToolInstanceEntity();
    toolInstanceEntity.setUUID(UUID.randomUUID());
    return toolInstanceEntity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long countApplications(final UUID pUUID) throws ToolInstanceProvisioningException
  {
    return countApplicationsByInstance(pUUID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<InstanceConfiguration> getApplicationsByUUID(final UUID pUUID)
      throws ToolInstanceProvisioningException
  {
    try
    {
      return toolInstanceDAO.getApplicationsByUUID(pUUID);
    }
    catch (final Exception e)
    {
      throw new ToolInstanceProvisioningException(String.format(
          "Unable to get applications for [tool_instance_uuid=%s]", pUUID), e);
    }
  }

  /**
   * @return the toolInstanceDAO
   */
  protected ToolInstanceDAO getToolInstanceDAO()
  {
    return toolInstanceDAO;
  }

  /**
   * Use by container to inject {@link ToolInstanceDAO}
   *
   * @param pToolInstanceDAO
   *          the toolInstanceDAO to set
   */
  public void setToolInstanceDAO(final ToolInstanceDAO pToolInstanceDAO)
  {
    toolInstanceDAO = pToolInstanceDAO;
  }

  /**
   * @return the instanceConfigurationDAO
   */
  protected InstanceConfigurationDAO getInstanceConfigurationDAO()
  {
    return instanceConfigurationDAO;
  }

  /**
   * Use by container to inject {@link InstanceConfigurationDAO}
   *
   * @param pInstanceConfigurationDAO
   *          the instanceConfigurationDAO to set
   */
  public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
  {
    instanceConfigurationDAO = pInstanceConfigurationDAO;
  }

  /**
   * @return the webServerConfigurator
   */
  protected WebServerConfigurator getWebServerConfigurator()
  {
    return webServerConfigurator;
  }

  /**
   * Use by container to inject {@link WebServerConfigurator}
   *
   * @param pWebServerConfigurator
   *          the webServerConfiguratorFacadeService to set
   */
  public void setWebServerConfigurator(final WebServerConfigurator pWebServerConfigurator)
  {
    webServerConfigurator = pWebServerConfigurator;
  }

  /**
   * @return the pluginConfigurationService
   */
  protected PluginConfigurationService getPluginConfigurationService()
  {
    return pluginConfigurationService;
  }

  /**
   * Use by container to inject {@link PluginConfigurationService}
   *
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

  /**
   * @return the forgeConfigurationService
   */
  protected ForgeConfigurationService getForgeConfigurationService()
  {
    return forgeConfigurationService;
  }
  /**
   * @param forgeConfigurationService
   *          the forgeConfigurationService to set
   */

  public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
  {
    this.forgeConfigurationService = forgeConfigurationService;
  }

}
