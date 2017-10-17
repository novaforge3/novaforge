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

import org.apache.camel.CamelContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.webserver.configuration.exceptions.WebServerConfigurationException;
import org.novaforge.forge.commons.webserver.configuration.services.WebServerConfigurator;
import org.novaforge.forge.core.plugins.dao.UuidDAO;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.PluginParameter;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.PluginView;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.domain.route.PluginAssociation;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.exceptions.ToolInstanceProvisioningException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginRoleMappingService;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.core.plugins.services.ToolInstanceProvisioningService;
import org.novaforge.forge.plugins.commons.services.domains.PluginUserImpl;
import org.novaforge.forge.plugins.commons.services.domains.PluginViewImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This abstract class define a generic way to implement PluginService Interface. The real plugin service
 * should declare an starting callback method to automaticaly set uuid as service property.
 * 
 * @see org.novaforge.forge.core.plugins.services.PluginService
 * @author Guillaume Lamirand
 * @author trolatf
 */
public abstract class AbstractPluginService implements PluginService
{

  /**
   * Constant for /
   */
  protected static final String SLASH                             = "/";
  /**
   * Instantiate logger
   */
  protected static final Log    LOGGER                            = LogFactory.getLog(AbstractPluginService.class);
  /**
   * Represents the default icon file
   */
  private static final   String IMG_UNKNOWN                       = "/img/unknown.png";
  /**
   * Represents a default name for a tool instance
   */
  private static final   String DEFAULT_TOOL_INSTANCE_NAME        = "Default Instance";
  /**
   * Represents a default description for a tool instance
   */
  private static final   String DEFAULT_TOOL_INSTANCE_DESCRIPTION = "Default Instance for non provisonable tool plugins";
  /**
   * Reference to service implementation of {@link UuidDAO}
   */
  private final UuidDAO                         uuidDAO;
  /**
   * Reference to service implementation of {@link PluginRoleMappingService}
   */
  private       PluginRoleMappingService        pluginRoleMappingService;
  /**
   * Reference to service implementation of {@link PluginUserService}
   */
  private       PluginUserService               pluginUserService;
  /**
   * Reference to service implementation of {@link WebServerConfigurator}
   */
  private       WebServerConfigurator           webServerConfigurator;
  /**
   * Reference to service implementation of {@link ToolInstanceProvisioningService}
   */
  private       ToolInstanceProvisioningService toolInstanceProvisioningService;
  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private       PluginConfigurationService      pluginConfigurationService;
  /**
   * Reference to service implementation of {@link ToolInstanceProvisioningService}
   */
  private       CamelContext                    camelContext;
  /**
   * Contains uuid of current plugin. Used to defined OSGi service exposed.
   */
  private       String                          serviceUUID;
  /**
   * Version of plugin
   */
  private       String                          version;
  /**
   * Type of plugin
   */
  private       String                          type;
  private       List<PluginView>                viewList;

  /**
   * This is defined in order to set plugin UUID as property service if it is existing in database
   */
  protected AbstractPluginService(final UuidDAO pUuidDAO)
  {
    uuidDAO = pUuidDAO;
    // Initialize the plugin service uuid
    initializeServiceUUID();
  }

  /**
   * Initialize the plugin service uuid
   */
  private void initializeServiceUUID()
  {
    if ((serviceUUID == null) || ("".equals(serviceUUID)))
    {
      if (uuidDAO.countAll() == 1)
      {
        setServiceUUID(uuidDAO.findAll().get(0).getPluginUUID().toString());
      }
      else
      {
        setServiceUUID(uuidDAO.generateUUID().toString());
      }
      LOGGER.info(String.format("Plugin's UUID has been set for %s with %s", this.getClass().getSimpleName(),
                                serviceUUID));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getServiceUUID()
  {
    return UUID.fromString(serviceUUID);
  }

  /**
   * Allow to set service uuid. If this uuid is updated, so the service property associated will be updated
   * too.
   *
   * @param pServiceUUID
   */
  public void setServiceUUID(final String pServiceUUID)
  {
    serviceUUID = pServiceUUID;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getPluginIcon()
  {
    String iconPath = getIconPath();
    if ((iconPath == null) || ("".equals(iconPath)))
    {
      iconPath = IMG_UNKNOWN;
    }
    byte[] byteArray = null;
    try
    {
      InputStream resource = this.getClass().getResourceAsStream(iconPath);
      if (resource == null)
      {
        resource = AbstractPluginService.class.getResourceAsStream(IMG_UNKNOWN);

      }
      byteArray = IOUtils.toByteArray(resource);
    }
    catch (final IOException e)
    {
      LOGGER.error("Unable to build a byte array from inputstream given", e);
    }
    return byteArray;
  }

  /**
   * Return the icon resource path. This will be used to get icon from classpath as stream object
   *
   * @return icon resource path
   */
  protected String getIconPath()
  {
    return IMG_UNKNOWN;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginAssociation getAssociationInfo() throws PluginServiceException
  {
    // Default implementation is null. That means the application doesn't support communication interplugin
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize(final Map<String, String> pParameter) throws PluginServiceException
  {
    // Check parameters
    checkParameter(pParameter);
    manageDefaultToolInstance();
    manageAdministrator(pParameter);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getRolesMapping(final String pInstanceId) throws PluginServiceException
  {
    return pluginRoleMappingService.getRolesMapping(pInstanceId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reactivate() throws PluginServiceException
  {
    if ((webServerConfigurator == null) || (getWebServerConfName() == null) || (getWebServerConfName().trim().length()
                                                                                    == 0))
    {
      throw new UnsupportedOperationException("webserver configurator not available");
    }

    try
    {
      webServerConfigurator.reactivate(getWebServerConfName());
    }
    catch (final WebServerConfigurationException e)
    {
      throw new PluginServiceException("Unable to reactivate WebServerConfiguration", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void desactivate(final Map<String, String> pSuperAdmins) throws PluginServiceException
  {
    if ((webServerConfigurator == null) || (getWebServerConfName() == null) || (getWebServerConfName().trim().length()
                                                                                    == 0))
    {
      throw new UnsupportedOperationException("webserver configurator not available");
    }

    try
    {
      final List<String> baseUrl = new ArrayList<String>();
      final Set<ToolInstance> allToolInstances = toolInstanceProvisioningService.getAllToolInstances();
      for (final ToolInstance toolInstance : allToolInstances)
      {
        baseUrl.add(toolInstance.getAlias());
      }
      webServerConfigurator.desactivate(getWebServerConfName(), baseUrl, pSuperAdmins);
    }
    catch (final WebServerConfigurationException e)
    {
      throw new PluginServiceException("Unable to desactivate WebServerConfiguration", e);
    }
    catch (final ToolInstanceProvisioningException e)
    {
      throw new PluginServiceException("Unable to found any instance has desactivated", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop() throws PluginServiceException
  {
    if ((webServerConfigurator == null) || (getWebServerConfName() == null) || (getWebServerConfName().trim().length()
                                                                                    == 0))
    {
      throw new UnsupportedOperationException("webserver configurator not available");
    }

    try
    {
      final List<String> baseUrl = new ArrayList<String>();
      final Set<ToolInstance> allToolInstances = toolInstanceProvisioningService.getAllToolInstances();
      for (final ToolInstance toolInstance : allToolInstances)
      {
        baseUrl.add(toolInstance.getAlias());
      }
      webServerConfigurator.stop(getWebServerConfName(), baseUrl);
    }
    catch (final WebServerConfigurationException e)
    {
      throw new PluginServiceException("Unable to stop WebServerConfiguration", e);
    }
    catch (final ToolInstanceProvisioningException e)
    {
      throw new PluginServiceException("Unable to found any instance has desactivated", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enableRoute(final boolean pStates) throws PluginServiceException
  {
    if (camelContext != null)
    {
      try
      {
        if (pStates)
        {
          camelContext.start();
        }
        else
        {
          camelContext.stop();
        }
      }
      catch (final Exception e)
      {
        throw new PluginServiceException(String.format("Unable to stop or start the plugin routes [state_required=%s]",
                                                       pStates), e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPluginDataServiceUri()
  {
    // Default implementation is null. That means the application is not spreadable.
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToolInstanceProvisioningService getToolInstanceProvisioningService()
  {
    return toolInstanceProvisioningService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URI getAccessURI(final ToolInstance pToolInstance, final PluginViewEnum pView)
      throws PluginServiceException
  {
    URI defaultURI;
    final String viewURI = getViewURI(pView);
    if (pToolInstance.isInternal())
    {
      defaultURI = createURI(viewURI);
    }
    else
    {
      final String toolAlias = pToolInstance.getAlias();
      final StringBuilder redirectAlias = new StringBuilder(toolAlias);
      if ((toolAlias.endsWith(SLASH)) && (viewURI.startsWith(SLASH)))
      {
        redirectAlias.append(viewURI.substring(1));
      }
      else if ((!toolAlias.endsWith(SLASH)) && (!viewURI.startsWith(SLASH)))
      {
        redirectAlias.append(SLASH);
        redirectAlias.append(viewURI);
      }
      else
      {
        redirectAlias.append(viewURI);
      }
      defaultURI = createURI(redirectAlias.toString());
    }
    return defaultURI;
  }

  /**
   * This will return the alias associated to the requested view
   *
   * @param pView
   *          the {@link PluginViewEnum} requsted
   * @return view alias defined
   */
  protected String getViewURI(final PluginViewEnum pView)
  {
    String viewAlias = "";
    for (final PluginView pluginView : getPluginViews())
    {
      if (pluginView.getViewId() != null)
      {
        if (pluginView.getViewId().equals(pView))
        {
          viewAlias = pluginView.getURI();
          break;
        }
      }
    }
    return viewAlias;
  }

  private URI createURI(final String pURI) throws PluginServiceException
  {
    URI uri;
    try
    {
      uri = new URI(pURI);
    }
    catch (final URISyntaxException e)
    {
      throw new PluginServiceException(String.format("Unable to build URI object from fiven string [string=%s]", pURI),
                                       e);
    }
    return uri;
  }

  /**
   * @return the plugin views list used for metadata declaration
   */
  protected final List<PluginView> getPluginViews()
  {
    // Initialize available views
    if ((viewList == null) || (viewList.isEmpty()))
    {
      viewList = buildPluginViews();
    }
    return viewList;
  }

  /**
   * @return the plugin views list used for metadata declaration
   */
  protected List<PluginView> buildPluginViews()
  {
    final List<PluginView> defaultViewList = new ArrayList<PluginView>();
    defaultViewList.add(new PluginViewImpl(PluginViewEnum.DEFAULT, getDefaultAccess()));
    return defaultViewList;
  }

  /**
   * @return this method will return value saved in configuration file
   */
  protected String getDefaultAccess()
  {
    return pluginConfigurationService.getDefaultAccess();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getMaxAllowedProjectInstances()
  {
    return pluginConfigurationService.getMaxAllowedProjectInstances();
  }

  /**
   * Use by container to inject {@link ToolInstanceProvisioningService}
   *
   * @param pToolInstanceProvisioningService
   *          the toolInstanceProvisioningService to set
   */
  public void setToolInstanceProvisioningService(final ToolInstanceProvisioningService pToolInstanceProvisioningService)
  {
    toolInstanceProvisioningService = pToolInstanceProvisioningService;
  }

  /**
   * Return name of the WebServer configuration file
   *
   * @return webServerConfName
   */
  protected String getWebServerConfName()
  {
    return pluginConfigurationService.getWebServerConfName();
  }

  private void checkParameter(final Map<String, String> pParameter) throws PluginServiceException
  {
    if ((!pParameter.containsKey(PluginParameter.SUPER_ADMIN_LOGIN)) || (!pParameter
                                                                              .containsKey(PluginParameter.SUPER_ADMIN_PWD))
            || (!pParameter.containsKey(PluginParameter.SUPER_ADMIN_EMAIL)) || (!pParameter
                                                                                     .containsKey(PluginParameter.SUPER_ADMIN_LASTNAME))
            || (!pParameter.containsKey(PluginParameter.SUPER_ADMIN_FIRSTNAME)) || (!pParameter
                                                                                         .containsKey(PluginParameter.SUPER_ADMIN_LANGUAGE)))
    {
      throw new PluginServiceException(String
                                           .format("One or more parameters needed to initialize the plugin are missing with [parameters=%s]",
                                                   Arrays.toString(pParameter.keySet().toArray())));
    }
  }

  /**
   * This method allows to add or update the default tool instance
   *
   * @throws PluginServiceException
   *           thrown if any errors occured
   */
  private void manageDefaultToolInstance() throws PluginServiceException
  {
    try
    {
      boolean isUpdated = true;
      ToolInstance toolInstance = toolInstanceProvisioningService.getToolInstanceByName(DEFAULT_TOOL_INSTANCE_NAME);
      if (toolInstance == null)
      {
        isUpdated = false;
        toolInstance = toolInstanceProvisioningService.newToolInstance();
        toolInstance.setName(DEFAULT_TOOL_INSTANCE_NAME);
        toolInstance.setDescription(DEFAULT_TOOL_INSTANCE_DESCRIPTION);
        toolInstance.setAlias(getDefaultToolAlias());
      }
      toolInstance.setInternal(pluginConfigurationService.isDefaultToolInternal());
      final URL defaultToolURL = pluginConfigurationService.getDefaultToolURL();
      if ((!pluginConfigurationService.isDefaultToolInternal()) && (defaultToolURL != null))
      {
        toolInstance.setBaseURL(defaultToolURL);
      }
      if (isUpdated)
      {
        toolInstanceProvisioningService.updateToolInstance(toolInstance, DEFAULT_TOOL_INSTANCE_NAME);
      }
      else
      {
        toolInstanceProvisioningService.addToolInstance(toolInstance);
      }
    }
    catch (final ToolInstanceProvisioningException e)
    {
      throw new PluginServiceException(String
                                           .format("Unable to add or update the default tool instance for plugin uuid=%s",
                                                   serviceUUID), e);
    }
  }

  /**
   * this method will create or update the administrator on each tool instance available
   *
   * @param pParameter
   *          source parameter
   * @throws PluginServiceException
   *           thrown if any errors occured
   */
  private void manageAdministrator(final Map<String, String> pParameter) throws PluginServiceException
  {
    final PluginUser user = getPluginUser(pParameter);

    // Check if a tool instance name is setted
    if ((pParameter.containsKey(PluginParameter.TOOL_INSTANCE_UUID)))
    {
      final String toolUUID = pParameter.get(PluginParameter.TOOL_INSTANCE_UUID);
      try
      {
        final ToolInstance toolInstance = toolInstanceProvisioningService
                                              .getToolInstanceByUUID(UUID.fromString(toolUUID));
        // Contact plugin user service
        if (toolInstance != null)
        {
          pluginUserService.createAdministratorUser(toolInstance, user);
        }
      }
      catch (final ToolInstanceProvisioningException e)
      {
        throw new PluginServiceException(String.format("Unable to get any tool instance with [uuid=%s]", toolUUID));
      }
    }
    else
    {
      try
      {
        final Set<ToolInstance> allToolInstances = toolInstanceProvisioningService.getAllToolInstances();
        for (final ToolInstance toolInstance : allToolInstances)
        {
          // Contact plugin user service
          pluginUserService.createAdministratorUser(toolInstance, user);
        }
      }
      catch (final ToolInstanceProvisioningException e)
      {
        throw new PluginServiceException(String.format("Unable to get all tools instance for this plugins [uuid=%s]",
                                                       serviceUUID));
      }
    }
  }

  /**
   * Return alias defined for the default tool instance
   *
   * @return an alias
   */
  protected String getDefaultToolAlias()
  {
    return SLASH + getType().toLowerCase() + "-default";
  }

  private PluginUser getPluginUser(final Map<String, String> pParameter)
  {
    // Get from map each admin parameter
    final String login     = pParameter.get(PluginParameter.SUPER_ADMIN_LOGIN);
    final String pwd       = pParameter.get(PluginParameter.SUPER_ADMIN_PWD);
    final String email     = pParameter.get(PluginParameter.SUPER_ADMIN_EMAIL);
    final String lastName  = pParameter.get(PluginParameter.SUPER_ADMIN_LASTNAME);
    final String firstName = pParameter.get(PluginParameter.SUPER_ADMIN_FIRSTNAME);
    final String language  = pParameter.get(PluginParameter.SUPER_ADMIN_LANGUAGE);

    // Building plugin user object
    final PluginUser user = new PluginUserImpl();
    user.setLogin(login);
    user.setPassword(pwd);
    user.setEmail(email);
    user.setName(lastName);
    user.setFirstName(firstName);
    user.setLanguage(language);
    return user;
  }

  /**
   * Should return the plugin type as String
   *
   * @return plugin's type
   */
  protected String getType()
  {
    return type;
  }

  /**
   * @param pType
   *     the type to set
   */
  public void setType(final String pType)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Type of plugin '%s' has been changed from '%s' to '%s'.", this.getClass().getName(),
                                 type, pType));
    }
    type = pType;
  }

  /**
   * Should return the plugin version as String
   *
   * @return plugin's type
   */
  protected String getVersion()
  {
    return version;
  }

  /**
   * @param pVersion
   *     the version to set
   */
  public void setVersion(final String pVersion)
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Version of plugin '%s' has been changed from '%s' to '%s'.",
                                 this.getClass().getName(), version, pVersion));
    }
    version = pVersion;
  }

  /**
   * Return description defined for configuration
   *
   * @return the description
   */
  protected String getDescription()
  {
    return pluginConfigurationService.getDescription();
  }

  /**
   * @return {@link PluginConfigurationService} as specific service configuration
   */
  @SuppressWarnings("unchecked")
  protected <T extends PluginConfigurationService> T getPluginConfigurationService(final Class<T> pClass)
      throws IllegalArgumentException
  {
    return (T) pluginConfigurationService;
  }

  /**
   * Use by container to inject {@link PluginRoleMappingService}
   *
   * @param pPluginRoleMappingService
   *          the pluginRoleMappingService to set
   */
  public void setPluginRoleMappingService(final PluginRoleMappingService pPluginRoleMappingService)
  {
    pluginRoleMappingService = pPluginRoleMappingService;
  }

  /**
   * Use by container to inject {@link PluginUserService}
   *
   * @param pPluginUserService
   *          the pluginUserService to set
   */
  public void setPluginUserService(final PluginUserService pPluginUserService)
  {
    pluginUserService = pPluginUserService;
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
   * Use by container to inject {@link CamelContext}
   *
   * @param pCamelContext
   *          the camelContext to set
   */
  public void setCamelContext(final CamelContext pCamelContext)
  {
    camelContext = pCamelContext;
  }

}
