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
package org.novaforge.forge.core.organization.internal.handlers;

import org.novaforge.forge.commons.technical.jms.MessageServiceException;
import org.novaforge.forge.core.organization.dao.NodeDAO;
import org.novaforge.forge.core.organization.delegates.MessageDelegate;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.handlers.ApplicationHandler;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.plugins.services.ToolInstanceProvisioningService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 */
public class ApplicationHandlerImpl implements ApplicationHandler
{
  /**
   * Reference to {@link PluginsManager} service injected by the container
   */
  private PluginsManager  pluginsManager;

  /**
   * Reference to {@link MessageDelegate} service injected by the container
   */
  private MessageDelegate messageDelegate;

  /**
   * Reference to {@link NodeDAO} service injected by the container
   */
  private NodeDAO         nodeDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getRoleMapping(final String pApplicationUri) throws ApplicationServiceException
  {
    Map<String, String> returnMap = null;
    String uuid = null;
    try
    {
      final ProjectApplication application = (ProjectApplication) nodeDAO.findByUri(pApplicationUri);
      uuid = application.getPluginUUID().toString();

      if (pluginsManager.isAvailablePlugin(uuid))
      {
        returnMap = pluginsManager.getPluginService(uuid).getRolesMapping(
            application.getPluginInstanceUUID().toString());
      }
    }
    catch (final PluginManagerException e)
    {
      throw new ApplicationServiceException(String.format("Unable to get plugin availability with [uuid=%s]",
          uuid), e);
    }
    catch (final PluginServiceException e)
    {
      throw new ApplicationServiceException(String.format(
          "Unable to get plugin service instance with [uuid=%s]", uuid), e);
    }
    return returnMap;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canAddApplication(final UUID pPluginUUID) throws ApplicationServiceException
  {
    boolean canAdd = false;
    try
    {
      final PluginMetadata pluginMetadata = pluginsManager.getPluginMetadataByUUID(pPluginUUID.toString());
      if ((pluginMetadata.isAvailable()) && (pluginMetadata.getStatus().equals(PluginStatus.ACTIVATED)))
      {
        canAdd = true;
      }
    }
    catch (final PluginManagerException e)
    {
      throw new ApplicationServiceException(String.format("Unable to get plugin informatons with [uuid=%s]",
          pPluginUUID), e);
    }

    return canAdd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAvailableToolInstance(final UUID pPluginUUID) throws ApplicationServiceException
  {
    try
    {
      return pluginsManager.hasAvailableToolInstance(pPluginUUID);
    }
    catch (final PluginManagerException e)
    {
      throw new ApplicationServiceException(String.format(
          "Unable to know if plugin has available tool instance with [uuid=%s]", pPluginUUID), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ToolInstance> getAvailableToolInstances(final UUID pPluginUUID)
      throws ApplicationServiceException
  {
    try
    {
      final PluginService pluginService = pluginsManager.getPluginService(pPluginUUID.toString());
      return new ArrayList<ToolInstance>(pluginService.getToolInstanceProvisioningService()
          .getAvailableToolInstances());
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException(String.format(
          "Unable to get available tool instance for a plugin with [uuid=%s]", pPluginUUID), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendApplicationMessage(final String pPluginUUID, final String pInstanceUUID,
      final String pApplicationLabel, final String pProjectId, final Map<String, String> pRolesMapping,
      final String pToolInstanceUUID, final String pActionLabel, final String pLogin)
      throws ApplicationServiceException
  {
    try
    {
      messageDelegate.sendProjectMessage(pPluginUUID, pInstanceUUID, pApplicationLabel, pProjectId,
          castMapToSerializable(pRolesMapping), pToolInstanceUUID, pActionLabel, pLogin, false);
    }
    catch (final MessageServiceException e)
    {
      throw new ApplicationServiceException(String.format(
          "Unable to send application message with [label=%s, projectId=%s, action=%s]", pApplicationLabel,
          pProjectId, pActionLabel), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendForceApplicationDeleteMessage(final String pPluginUUID, final String pInstanceUUID,
      final String pApplicationLabel, final String pProjectId, final String pToolInstanceUUID,
      final String pLogin) throws ApplicationServiceException
  {
    try
    {
      messageDelegate
          .sendProjectMessage(pPluginUUID, pInstanceUUID, pApplicationLabel, pProjectId,
              new HashMap<String, String>(), pToolInstanceUUID, PluginQueueAction.DELETE.getLabel(), pLogin,
              true);
    }
    catch (final MessageServiceException e)
    {
      throw new ApplicationServiceException(String.format(
          "Unable to send delete application message with [label=%s, projectId=%s]", pApplicationLabel,
          pProjectId), e);
    }

  }

  /**
   * {@inheritDoc}
   * 
   * @see org.novaforge.forge.core.organization.handlers.ApplicationHandler#sendRolesMappingMessage(java.lang.
   *      String, java.lang.String, java.lang.String, java.lang.String, java.util.Map, java.lang.String,
   *      java.lang.String)
   */
  @Override
  public void sendRolesMappingMessage(final String pApplicationURI, final String pPluginUUID,
      final String pInstanceUUID, final String pProjectId, final Map<String, String> pRolesMapping,
      final String pActionLabel, final String pLogin) throws ApplicationServiceException
  {
    try
    {
      messageDelegate.sendRolesMappingMessage(pPluginUUID, pInstanceUUID, pProjectId,
          castMapToSerializable(pRolesMapping), pLogin, PluginQueueAction.UPDATE.getLabel());
    }
    catch (final MessageServiceException e)
    {
      throw new ApplicationServiceException(String.format(
          "Unable to send a message to update role mapping with [uri=%s, projectId=%s]", pApplicationURI,
          pProjectId), e);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see org.novaforge.forge.core.organization.handlers.ApplicationHandler#getToolInstanceByApplication(java.
   *      lang.String, java.lang.String)
   */
  @Override
  public ToolInstance getToolInstanceByApplication(final String pPluginUUID, final String pApplicationInstanceID)
      throws ApplicationServiceException
  {
    try
    {
      final ToolInstanceProvisioningService provisioningService = pluginsManager.getPluginService(pPluginUUID)
                                                                                .getToolInstanceProvisioningService();

      return provisioningService.getToolInstanceByApplication(pApplicationInstanceID);
    }
    catch (final Exception e)
    {
      throw new ApplicationServiceException(String
                                                .format("Unable to get tool instance by application with [plugin_uuid=%s, instance_uuid=%s]",
                                                        pPluginUUID, pApplicationInstanceID), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getPluginInfos(final UUID pPluginUUID) throws ApplicationServiceException
  {
    Map<String, String> infos = new HashMap<String, String>();
    try
    {
      PluginMetadata pluginMetadata = pluginsManager.getPluginMetadataByUUID(pPluginUUID.toString());
      infos.put("category", pluginMetadata.getCategory());
      infos.put("type", pluginMetadata.getType());
      infos.put("version", pluginMetadata.getVersion());
    }
    catch (PluginManagerException e)
    {
      throw new ApplicationServiceException(String.format("Unable to get plugin metadatas with [plugin_uuid=%s]",
                                                          pPluginUUID), e);
    }

    return infos;
  }

  /**
   * Allow to generate a {@link Serializable} from the given {@link Map}
   *
   * @param pRolesMapping
   *          original Map
   * @return an HashMap object
   */
  private Serializable castMapToSerializable(final Map<String, String> pRolesMapping)
  {
    Map<String, String> sendMap = new HashMap<String, String>();

    if (pRolesMapping != null)
    {
      if (pRolesMapping instanceof Serializable)
      {
        sendMap = pRolesMapping;
      }
      else
      {
        final Set<Entry<String, String>> entrySet = pRolesMapping.entrySet();
        for (final Entry<String, String> entry : entrySet)
        {
          sendMap.put(entry.getKey(), entry.getValue());
        }
      }
    }
    return (Serializable) sendMap;
  }

  /**
   * Use by container to inject {@link PluginsManager} implementation
   *
   * @param pPluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  /**
   * Use by container to inject {@link MessageDelegate} implementation
   *
   * @param pMessageDelegate
   *          the messageDelegate to set
   */
  public void setMessageDelegate(final MessageDelegate pMessageDelegate)
  {
    messageDelegate = pMessageDelegate;
  }

  /**
   * Use by container to inject {@link NodeDAO} implementation
   *
   * @param pNodeDAO
   *          the nodeDAO to set
   */
  public void setNodeDAO(final NodeDAO pNodeDAO)
  {
    nodeDAO = pNodeDAO;
  }
}
