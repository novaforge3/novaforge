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
package org.novaforge.forge.core.plugins.internal;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.organization.dao.UserDAO;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.categories.PluginCategoryService;
import org.novaforge.forge.core.plugins.categories.PluginRealm;
import org.novaforge.forge.core.plugins.dao.PluginMetadataDAO;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.PluginParameter;
import org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.exceptions.ToolInstanceProvisioningException;
import org.novaforge.forge.core.plugins.handlers.PluginLifeCycleHandler;
import org.novaforge.forge.core.plugins.services.PluginMetadataFactory;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsCategoryManager;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.plugins.services.ToolInstanceProvisioningService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import javax.persistence.NoResultException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 * @date Mar 22, 2011
 */
public class PluginsManagerImpl implements PluginsManager
{
  /**
   * Logger
   */
  private static final Log          LOGGER = LogFactory.getLog(PluginsManagerImpl.class);

  /**
   * {@link BundleContext} injected by container
   */
  private BundleContext             bundleContext;
  /**
   * {@link PluginMetadataDAO} injected by container
   */
  private PluginMetadataDAO         pluginMetadataDAO;
  /**
   * {@link PluginMetadataFactory} injected by container
   */
  private PluginMetadataFactory     pluginMetadataFactory;
  /**
   * {@link ForgeCfgService} injected by container
   */
  private ForgeConfigurationService forgeConfigurationService;
  /**
   * {@link PluginLifeCycleHandler} injected by container
   */
  private PluginLifeCycleHandler    lifeCycleHandler;
  /**
   * {@link UserDAO} injected by container
   */
  private UserDAO                   userDAO;
  /**
   * {@link PluginsCategoryManager} injected by container
   */
  private PluginsCategoryManager    pluginsCategoryManager;

  /**
   * Callback method for validate
   */
  public void start()
  {
    try
    {
      final List<PluginPersistenceMetadata> pluginsMetadata = pluginMetadataDAO.findAll();
      for (final PluginPersistenceMetadata pluginMetadata : pluginsMetadata)
      {
        final UUID uuid = pluginMetadata.getUUID();
        final PluginService pluginService = getPluginService(uuid.toString());
        if ((pluginMetadata.isAvailable()) && (pluginService == null))
        {
          updatePluginAvailability(uuid, false);
        }
        else if ((!pluginMetadata.isAvailable()) && (pluginService != null))
        {
          updatePluginAvailability(uuid, true);
        }
      }
    }
    catch (final PluginManagerException e)
    {
      LOGGER.error("Unable to update availability of all plugins stored in the register.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void registerPlugin(final PluginService pPluginService) throws PluginManagerException
  {
    final UUID uuid = pPluginService.getServiceUUID();
    try
    {
      if (pluginMetadataDAO.exist(uuid))
      {
        // get the pluginMetadata stored
        final PluginPersistenceMetadata findByUUID = pluginMetadataDAO.findByUUID(uuid);

        // get the pluginMetadata from plugin service
        final PluginPersistenceMetadata persistedPluginMetadata = pluginMetadataFactory
            .updatePluginPersistenceMetadata(findByUUID, pPluginService.getMetadata());
        persistedPluginMetadata.setAvailable(true);
        // update the plugin metadata with plugin service informations
        pluginMetadataDAO.update(persistedPluginMetadata);

        // get the status of the stored pluginMetadata
        final PluginStatus status = persistedPluginMetadata.getStatus();
        final PluginMetadata servicePluginMetadata = pluginMetadataFactory
            .createPluginMetadata(persistedPluginMetadata);
        if ((!status.equals(PluginStatus.ACTIVATED)) && (!status.equals(PluginStatus.DEPRECATED)))
        {
          lifeCycleHandler.disablePluginRoute(pPluginService, servicePluginMetadata.getUUID());
        }
      }
      else
      {
        // at first step, the new detected plugin is activated but next an IHM will offer the capability to
        // install aand activate the plugins present in the features repository
        final PluginServiceMetadata pluginMetadataSource = pPluginService.getMetadata();
        final PluginPersistenceMetadata newEntity = pluginMetadataDAO.newEntity();
        final PluginPersistenceMetadata persistedMetadata = pluginMetadataFactory
            .updatePluginPersistenceMetadata(newEntity, pluginMetadataSource);
        persistedMetadata.setStatus(PluginStatus.ACTIVATED);
        persistedMetadata.setAvailable(true);
        pluginMetadataDAO.create(persistedMetadata);

        // disable the plugin routes because the initial status is INSTALLED and so not active.
        // final PluginMetadata servicePluginMetadata = pluginMetadataFactory
        // .createPluginMetadata(persistedMetadata);
        // lifeCycleHandler.disablePluginRoute(pPluginService, servicePluginMetadata.getUUID());
      }
    }
    catch (final PluginServiceException e)
    {
      throw new PluginManagerException(
          String.format(
              "Problem while building a plugin metadata persistence from original plugin metadata with [uuid=%s]",
              uuid), e);
    }
    catch (final Exception e)
    {
      if (e instanceof PluginManagerException)
      {
        throw (PluginManagerException) e;
      }
      else
      {
        throw new PluginManagerException("a technical error occurred", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePluginMetadata(final UUID pUUID) throws PluginManagerException
  {
    try
    {
      // get the pluginMetadata stored
      final PluginPersistenceMetadata findByUUID = pluginMetadataDAO.findByUUID(pUUID);

      // get the pluginMetadata from plugin service
      final PluginService pluginService = getPluginService(pUUID.toString());
      final PluginPersistenceMetadata persistedPluginMetadata = pluginMetadataFactory
          .updatePluginPersistenceMetadata(findByUUID, pluginService.getMetadata());
      persistedPluginMetadata.setAvailable(true);
      // update the plugin metadata with plugin service informations
      pluginMetadataDAO.update(persistedPluginMetadata);
    }
    catch (final PluginServiceException e)
    {
      throw new PluginManagerException(
          String.format(
              "Problem while building a plugin metadata persistence from original plugin metadata with [uuid=%s]",
              pUUID), e);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePluginAvailability(final UUID pUUID, final boolean pAvailability) throws PluginManagerException
  {
    try
    {
      final PluginPersistenceMetadata metadata = pluginMetadataDAO.findByUUID(pUUID);
      metadata.setAvailable(pAvailability);
      pluginMetadataDAO.update(metadata);
    }
    catch (final NoResultException e)
    {
      throw new PluginManagerException(String
                                           .format("Problem while building a plugin metadata persistence from original plugin metadata with [uuid=%s]",
                                                   pUUID), e);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginService getPluginService(final String pId) throws PluginManagerException
  {
    PluginService pluginService = null;
    try
    {
      final Collection<ServiceReference<PluginService>> refs = bundleContext.getServiceReferences(PluginService.class,
                                                                                                  String
                                                                                                      .format("(%s=%s)",
                                                                                                              PluginService.ID_PROPERTY,
                                                                                                              pId));
      if ((refs != null) && (!refs.isEmpty()) && (refs.size() == 1))
      {
        pluginService = bundleContext.getService(Iterables.get(refs, 0));
      }
    }
    catch (final InvalidSyntaxException e)
    {
      throw new PluginManagerException(String.format("Problem while getting plugin service from OSGi registry with [uuid=%s]",
                                                     pId), e);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
    return pluginService;
  }

  /**
   * {@inheritDoc}
   *
   * @throws PluginManagerException
   */
  @Override
  public List<PluginMetadata> getAllPlugins() throws PluginManagerException
  {
    try
    {
      final List<PluginPersistenceMetadata> findAll = pluginMetadataDAO.findAll();
      return pluginMetadataFactory.createPluginMetadataList(findAll);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginMetadata> getPluginsMetadataByCategory(final String... pCategoryId) throws PluginManagerException
  {
    try
    {
      final List<PluginPersistenceMetadata> findByCategory = pluginMetadataDAO.findByCategory(pCategoryId);
      return pluginMetadataFactory.createPluginMetadataList(findByCategory);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginMetadata> getAllInstantiablePluginsMetadataByCategory(final String pLabel)
      throws PluginManagerException
  {
    try
    {
      final List<PluginPersistenceMetadata> metadatas = pluginMetadataDAO.findByCategoryAndStatusAndAvailability(pLabel,
                                                                                                                 PluginStatus.ACTIVATED,
                                                                                                                 true);
      return pluginMetadataFactory.createPluginMetadataList(metadatas);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginMetadata> getAllInstantiablePluginsMetadataByRealm(final PluginRealm pRealmType)
      throws PluginManagerException
  {
    try
    {
      final List<PluginMetadata> returned = new ArrayList<PluginMetadata>();
      final List<PluginPersistenceMetadata> metadatas = pluginMetadataDAO
                                                            .findByStatusAndAvailabitly(PluginStatus.ACTIVATED, true);
      final List<PluginMetadata> pluginMetadatas = pluginMetadataFactory.createPluginMetadataList(metadatas);
      for (final PluginMetadata pluginMetadata : pluginMetadatas)
      {
        final CategoryDefinitionService service = pluginsCategoryManager.getCategoryService(pluginMetadata
                                                                                                .getCategory());
        if (service.getRealm().equals(pRealmType))
        {
          returned.add(pluginMetadata);
        }
      }

      return returned;
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginMetadata> getPluginsMetadataByType(final String pLabel) throws PluginManagerException
  {
    try
    {
      final List<PluginPersistenceMetadata> findByType = pluginMetadataDAO.findByType(pLabel);
      return pluginMetadataFactory.createPluginMetadataList(findByType);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginMetadata> getPluginsMetadataByStatus(final PluginStatus pStatus)
      throws PluginManagerException
  {
    try
    {
      final List<PluginPersistenceMetadata> findByStatus = pluginMetadataDAO.findByStatus(pStatus);
      return pluginMetadataFactory.createPluginMetadataList(findByStatus);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginMetadata> getAllInstantiablePluginsMetadata() throws PluginManagerException
  {
    try
    {
      final List<PluginPersistenceMetadata> metadatas = pluginMetadataDAO.findByStatusAndAvailabitly(
          PluginStatus.ACTIVATED, true);
      return pluginMetadataFactory.createPluginMetadataList(metadatas);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAvailablePlugin(final String pId)
  {
    PluginService pluginService = null;
    try
    {
      pluginService = getPluginService(pId);
    }
    catch (final Exception e)
    {
      // Nothing to do
      LOGGER.error("a technical  error occurred", e);
    }
    return pluginService != null;
  }

  /**
   * {@inheritDoc}
   *
   * @throws PluginManagerException
   */
  @Override
  public PluginMetadata getPluginMetadataByUUID(final String pPluginUUID) throws PluginManagerException
  {
    final UUID     uuid                 = UUID.fromString(pPluginUUID);
    PluginMetadata createPluginMetadata = null;
    try
    {
      final PluginPersistenceMetadata findByUUID = pluginMetadataDAO.findByUUID(uuid);
      if (findByUUID != null)
      {
        createPluginMetadata = pluginMetadataFactory.createPluginMetadata(findByUUID);
      }
    }
    catch (final NoResultException e)
    {
      // nothing to do
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
    return createPluginMetadata;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getAllPluginCategories() throws PluginManagerException
  {
    try
    {
      return pluginMetadataDAO.findCategories();
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginStatus> getAllPluginStatus()
  {
    final List<PluginStatus> statusList = Arrays.asList(PluginStatus.values());
    return Collections.unmodifiableList(statusList);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changePluginStatus(final String pPluginUUID, final PluginStatus pStatus)
      throws PluginManagerException
  {
    try
    {
      final UUID uuid = UUID.fromString(pPluginUUID);
      final PluginPersistenceMetadata pluginMetadata = pluginMetadataDAO.findByUUID(uuid);
      final PluginStatus oldStatus = pluginMetadata.getStatus();

      if (oldStatus.equals(pStatus))
      {
        throw new PluginManagerException(String.format(
            "the plugin is already in the expected status [uuid=%s, status=%s", pPluginUUID,
            pStatus.toString()));
      }

      // change the plugin lifecycle
      final PluginMetadata createPluginMetadata = pluginMetadataFactory.createPluginMetadata(pluginMetadata);
      lifeCycleHandler.changePluginLifeCycle(createPluginMetadata, oldStatus, pStatus);
      createPluginMetadata.setStatus(pStatus);

      // notify the event
      lifeCycleHandler.notifyPluginLifecycleChange(createPluginMetadata);
    }
    catch (final Exception e)
    {
      if (e instanceof PluginManagerException)
      {
        throw (PluginManagerException) e;
      }
      else if (e instanceof UnsupportedOperationException)
      {
        throw e;
      }
      else
      {
        throw new PluginManagerException("a technical error occurred", e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginStatus> getAuthorizedChangesForStatus(final PluginStatus pStatus)
  {
    return lifeCycleHandler.getAuthorizedChangesForStatus(pStatus);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<PluginStatus, List<PluginStatus>> getAllAuthorizedChanges()
  {
    return lifeCycleHandler.getAllAuthorizedChanges();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends PluginCategoryService> T getPluginCategoryService(final String pId, final Class<T> pServiceClass)
      throws PluginManagerException
  {
    T functionalService = null;
    try
    {

      final Collection<ServiceReference<T>> refs = bundleContext.getServiceReferences(pServiceClass,
                                                                                      String.format("(%s=%s)",
                                                                                                    PluginCategoryService.ID_PROPERTY,
                                                                                                    pId));
      if ((refs != null) && (!refs.isEmpty()) && (refs.size() == 1))
      {
        functionalService = bundleContext.getService(Iterables.get(refs, 0));
      }
      else
      {
        throw new PluginManagerException(String
                                             .format("Problem while getting plugin functional service from OSGi registry with [uuid=%s]",
                                                     pId));
      }
    }
    catch (final InvalidSyntaxException e)
    {
      throw new PluginManagerException(String
                                           .format("Problem while getting plugin functional service from OSGi registry with [uuid=%s]",
                                                   pId), e);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
    return functionalService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URI getViewAccessForInstance(final String pPluginUUID, final String pInstanceUUID,
                                      final PluginViewEnum pPluginView) throws PluginManagerException
  {
    return getViewAccess(pPluginUUID, pInstanceUUID, null, pPluginView);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URI getViewAccessForTool(final String pPluginUUID, final String pToolUUID, final PluginViewEnum pPluginView)
      throws PluginManagerException
  {
    return getViewAccess(pPluginUUID, null, pToolUUID, pPluginView);

  }

  @Override
  public boolean hasAvailableToolInstance(final UUID pUUID) throws PluginManagerException
  {
    boolean ret = true;
    final ToolInstanceProvisioningService tooProvisioningService = getPluginService(pUUID.toString())
        .getToolInstanceProvisioningService();
    try
    {
      ret = tooProvisioningService.hasAvailableToolInstance();
    }
    catch (final ToolInstanceProvisioningException e)
    {
      throw new PluginManagerException(String.format(
          "Problem while finding available tool instance for [uuid=%s]", pUUID), e);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }

    return ret;
  }

  @Override
  public boolean isToolInstanceProvisionable(final UUID pUUID) throws PluginManagerException
  {
    try
    {
      return getPluginService(pUUID.toString()).getToolInstanceProvisioningService().isProvisionable();
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void configureToolInstance(final PluginService pPluginService, final UUID pToolUUID)
      throws PluginManagerException
  {
    final Map<String, String> parameter = new HashMap<String, String>();

    try
    {
      if (pToolUUID != null)
      {
        parameter.put(PluginParameter.TOOL_INSTANCE_UUID, pToolUUID.toString());
      }
      final User admin = userDAO.findByLogin(forgeConfigurationService.getSuperAdministratorLogin());
      parameter
          .put(PluginParameter.SUPER_ADMIN_LOGIN, forgeConfigurationService.getSuperAdministratorLogin());
      parameter.put(PluginParameter.SUPER_ADMIN_PWD, admin.getPassword());
      parameter.put(PluginParameter.SUPER_ADMIN_EMAIL, admin.getEmail());
      parameter.put(PluginParameter.SUPER_ADMIN_LASTNAME, admin.getName());
      parameter.put(PluginParameter.SUPER_ADMIN_FIRSTNAME, admin.getFirstName());
      parameter.put(PluginParameter.SUPER_ADMIN_LANGUAGE, admin.getLanguage().getName());

      pPluginService.initialize(parameter);
    }
    catch (final PluginServiceException e)
    {
      throw new PluginManagerException("Unable to initialize plugin service.", e);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException("a technical error occurred", e);
    }
  }

  private URI getViewAccess(final String pPluginUUID, final String pInstanceUUID, final String pToolUUID,
                            final PluginViewEnum pPluginView) throws PluginManagerException
  {
    URI                 returnURI     = null;
    final PluginService pluginService = getPluginService(pPluginUUID);
    if (pluginService != null)
    {
      PluginViewEnum view = pPluginView;
      if (view == null)
      {
        view = PluginViewEnum.DEFAULT;
      }
      try
      {
        ToolInstance toolInstance = null;
        if (!Strings.isNullOrEmpty(pInstanceUUID))
        {
          toolInstance = pluginService.getToolInstanceProvisioningService().getToolInstanceByApplication(pInstanceUUID);
        }
        else if (!Strings.isNullOrEmpty(pToolUUID))
        {
          toolInstance = pluginService.getToolInstanceProvisioningService()
                                      .getToolInstanceByUUID(UUID.fromString(pToolUUID));
        }
        else
        {
          throw new PluginManagerException(String
                                               .format("Either application instance ID or Tool ID have to be non null to retrieve view access with [instance_uuid=%s, tool_id=%s]",
                                                       pInstanceUUID, pToolUUID));
        }
        returnURI = pluginService.getAccessURI(toolInstance, view);
      }
      catch (final ToolInstanceProvisioningException e)
      {
        throw new PluginManagerException(String
                                             .format("Unable to find any tool instance associated to the instance id with [instance_uuid=%s]",
                                                     pInstanceUUID));
      }
      catch (final PluginServiceException e)
      {
        throw new PluginManagerException(String
                                             .format("Unable to get access URI for the given tool instance and view with [instance_uuid=%s, view=%s]",
                                                     pInstanceUUID, view));
      }
    }
    else
    {
      throw new PluginManagerException(String.format("Unable to find any plugin service with [uuid=%s]", pPluginUUID));
    }
    return returnURI;
  }

  /**
   * @param pluginsCategoryManager
   *          the pluginsCategoryManager to set
   */
  public void setPluginsCategoryManager(final PluginsCategoryManager pluginsCategoryManager)
  {
    this.pluginsCategoryManager = pluginsCategoryManager;
  }

  /**
   * Use by container to inject {@link BundleContext}
   * 
   * @param pBundleContext
   *          the bundleContext to set
   */
  public void setBundleContext(final BundleContext pBundleContext)
  {
    bundleContext = pBundleContext;
  }

  /**
   * Use by container to inject {@link PluginMetadataDAO}
   * 
   * @param pPluginMetadataDAO
   *          the pluginMetadataDAO to set
   */
  public void setPluginMetadataDAO(final PluginMetadataDAO pPluginMetadataDAO)
  {
    pluginMetadataDAO = pPluginMetadataDAO;
  }

  /**
   * Use by container to inject {@link PluginMetadataFactory}
   * 
   * @param pPluginMetadataFactory
   *          the pluginMetadataFactory to set
   */
  public void setPluginMetadataFactory(final PluginMetadataFactory pPluginMetadataFactory)
  {
    pluginMetadataFactory = pPluginMetadataFactory;
  }

  /**
   * Use by container to inject {@link ForgeCfgService}
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * Use by container to inject {@link PluginLifeCycleHandler}
   * 
   * @param pLifeCycleHandler
   *          the lifeCycleHandler to set
   */
  public void setLifeCycleHandler(final PluginLifeCycleHandler pLifeCycleHandler)
  {
    lifeCycleHandler = pLifeCycleHandler;
  }

  /**
   * Use by container to inject {@link UserDAO}
   * 
   * @param pUserDAO
   *          the userDAO to set
   */
  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
  }

}
