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
package org.novaforge.forge.portal.internal.services;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import org.apache.commons.io.IOUtils;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.internal.models.PortalApplicationImpl;
import org.novaforge.forge.portal.internal.models.PortalSpaceImpl;
import org.novaforge.forge.portal.internal.models.PortalStringTokenizedImpl;
import org.novaforge.forge.portal.internal.models.PortalURIImpl;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.models.PortalSettingId;
import org.novaforge.forge.portal.models.PortalSpace;
import org.novaforge.forge.portal.models.PortalStringTokenized;
import org.novaforge.forge.portal.models.PortalToken;
import org.novaforge.forge.portal.models.PortalURI;
import org.novaforge.forge.portal.services.PortalNavigation;
import org.novaforge.forge.portal.services.navigation.NavigationMessage;
import org.novaforge.forge.portal.services.navigation.NavigationSecurity;
import org.novaforge.forge.portal.services.navigation.NavigationToken;
import org.novaforge.forge.portal.services.navigation.NavigationXML;
import org.novaforge.forge.portal.xml.Account;
import org.novaforge.forge.portal.xml.App;
import org.novaforge.forge.portal.xml.DisplayType;
import org.novaforge.forge.portal.xml.ExternalApp;
import org.novaforge.forge.portal.xml.Link;
import org.novaforge.forge.portal.xml.PortalConfig;
import org.novaforge.forge.portal.xml.Setting;
import org.novaforge.forge.portal.xml.Space;
import org.novaforge.forge.portal.xml.UriTemplate;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Service implementation of {@link PortalNavigation}
 * 
 * @author Guillaume Lamirand
 */
public class PortalNavigationImpl implements PortalNavigation
{
  /**
   * Configuration file
   */
  private static final String PORTAL_CONFIG_XML = "portal-config.xml";
  /**
   * Reference to {@link ForgeCfgService} service injected by the container
   */
  private ForgeConfigurationService forgeConfigurationService;
  /**
   * Reference to {@link AuthentificationService} service injected by the container
   */
  private AuthentificationService   authentificationService;
  /**
   * Reference to {@link PluginsManager} service injected by the container
   */
  private PluginsManager            pluginsManager;
  /**
   * Reference to {@link UserService} service injected by the container
   */
  private UserService        userService;
  /**
   * Reference to {@link NavigationToken} service injected by the container
   */
  private NavigationToken    navigationToken;
  /**
   * Reference to {@link NavigationXML} service injected by the container
   */
  private NavigationXML      navigationXML;
  /**
   * Service injected by iPOJO
   */
  private NavigationMessage  navigationMessage;
  /**
   * Reference to {@link NavigationSecurity} service injected by the container
   */
  private NavigationSecurity navigationSecurity;
  /**
   * Current configuration
   */
  private PortalConfig       portalConfig;

  /**
   * Callback method call when the component become valid.
   * This will read the xml file and build {@link PortalConfig} associated
   *
   * @throws PortalException
   *           throw if the configuration file can be read
   */
  public void init() throws PortalException
  {
    refresh();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PortalSpace> getForgeSpaces(final Locale pLocale) throws PortalException
  {
    final List<Space> spaces = portalConfig.getNavigation().getForge().getSpace();

    final String forgeProjectId = forgeConfigurationService.getForgeProjectId();

    return getSpaces(forgeProjectId, spaces, pLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalApplication getForgeDefaultApplication(final Locale pLocale) throws PortalException
  {
    PortalApplication defaultApplication = null;
    final String      forgeProjectId     = forgeConfigurationService.getForgeProjectId();
    if (portalConfig.getNavigation().getForge().getDefaultApp() != null)
    {
      final App internalApp = portalConfig.getNavigation().getForge().getDefaultApp().getInternalApp();
      final ExternalApp externalApp = portalConfig.getNavigation().getForge().getDefaultApp().getExternalApp();
      defaultApplication = getProjectDefaultApplication(pLocale, forgeProjectId, internalApp, externalApp);
    }
    return defaultApplication;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PortalSpace> getProjectSpaces(final String pProjectId, final Locale pLocale) throws PortalException
  {
    final List<Space> spaces = portalConfig.getNavigation().getProject().getSpace();
    return getSpaces(pProjectId, spaces, pLocale);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalApplication getProjectDefaultApplication(final String pProjectId, final Locale pLocale)
      throws PortalException
  {
    PortalApplication defaultApplication = null;
    if (portalConfig.getNavigation().getProject().getDefaultApp() != null)
    {
      final App internalApp = portalConfig.getNavigation().getProject().getDefaultApp().getInternalApp();
      final ExternalApp externalApp = portalConfig.getNavigation().getProject().getDefaultApp().getExternalApp();
      defaultApplication = getProjectDefaultApplication(pLocale, pProjectId, internalApp, externalApp);
    }
    return defaultApplication;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalSpace getAccountSpaces(final Locale pLocale) throws PortalException
  {
    final Account           account           = portalConfig.getNavigation().getAccount();
    final List<PortalSpace> spaces            = getSpaces("", account.getSpace(), pLocale);
    PortalSpace             returnPortalSpace = null;
    if ((spaces != null) && !spaces.isEmpty())
    {
      returnPortalSpace = spaces.get(0);
    }
    return returnPortalSpace;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PortalApplication> getLink(final Locale pLocale) throws PortalException
  {
    final List<PortalApplication> apps = new ArrayList<PortalApplication>();
    final Link                    link = portalConfig.getNavigation().getLink();
    // Build application from external app
    apps.addAll(getExternalApps(link.getExternalApp(), "", pLocale));

    // Build application from internal app
    apps.addAll(getApps(link.getInternalApp(), "", pLocale));
    return apps;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalURI getApplicationURI(final String pUUID, final String pInstanceId, final String pToolUUID,
                                     final String pProjectId, final String pPluginView, final Locale pLocale)
      throws PortalException
  {
    final PortalURIImpl portalURI;
    try
    {
      if (!Strings.isNullOrEmpty(pInstanceId))
      {
        final URI viewURI = pluginsManager.getViewAccessForInstance(pUUID, pInstanceId,
                                                                    PluginViewEnum.valueOf(pPluginView));
        portalURI = new PortalURIImpl(viewURI);
      }
      else if (!Strings.isNullOrEmpty(pToolUUID))
      {
        final URI viewURI = pluginsManager.getViewAccessForTool(pUUID, pToolUUID, PluginViewEnum.valueOf(pPluginView));
        portalURI = new PortalURIImpl(viewURI);
      }
      else
      {
        throw new PortalException("To retrieve view alias an instance id or a tool id has to be set.");
      }
    }
    catch (final PluginManagerException e)
    {
      throw new PortalException(String.format("Unable to get plugin view alias from [uuid=%s, view_id=%s]", pUUID,
                                              pPluginView), e);
    }
    if (!portalURI.isInternalModule())
    {
      final URL absoluteURl = createAbsoluteURL(portalURI.getRelativePath(), pUUID, pToolUUID, pInstanceId,
          pProjectId, pPluginView, pLocale);
      portalURI.setAbsoluteURL(absoluteURl);
    }
    return portalURI;
  }

  private URL createAbsoluteURL(final String pRelativePath, final String pUUID, final String pToolUUID,
      final String pInstanceId, final String pProjectId, final String pPluginView, final Locale pLocale)
      throws PortalException
  {
    final PortalStringTokenized string = new PortalStringTokenizedImpl(pRelativePath);
    string.setPluginUuid(pUUID);
    string.setToolUuid(pToolUUID);
    string.setInstanceId(pInstanceId);
    string.setPluginViewId(pPluginView);
    string.setProjectId(pProjectId);
    string.setLocale(pLocale.getLanguage());
    final String resolved = navigationToken.resolved(string);
    try
    {
      final URL absoluteURL = new URL(forgeConfigurationService.getPublicUrl(), resolved);
      UUID uniqueId;
      if (!Strings.isNullOrEmpty(pInstanceId))
      {
        uniqueId = UUID.fromString(pInstanceId);
      }
      else if (!Strings.isNullOrEmpty(pUUID))
      {
        uniqueId = UUID.fromString(pUUID);
      }
      else
      {
        uniqueId = UUID.randomUUID();
      }

      return addUniqueId(absoluteURL, uniqueId);
    }
    catch (final MalformedURLException e)
    {
      throw new PortalException(String.format("Unable to build a URL object from [source=%s]", resolved), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PortalURI createPortalURI(final String pModuleId) throws PortalException
  {
    try
    {
      final URI uri = new URI(PortalURI.MODULES_SCHEME, pModuleId, null);
      return new PortalURIImpl(uri);
    }
    catch (final URISyntaxException e)
    {
      throw new PortalException(String.format("Unable to build PortalUri with given module id [module=%s]", pModuleId),
                                e);
    }
  }

  /**
   * {@inheritDoc}
   * <p>
   * This will read the xml file and build {@link PortalConfig} associated
   * </p>
   *
   * @throws PortalException
   *     throw if the configuration file can be read
   */
  @Override
  public void refresh() throws PortalException
  {
    // Get portal-config file
    final File portalXml = new File(forgeConfigurationService.getPortalConfDirectory(), PORTAL_CONFIG_XML);
    portalConfig = navigationXML.unmarshal(portalXml);

  }

  /**
   * {@inheritDoc}
   * <p>
   * This will read the xml file and build {@link PortalConfig} associated
   * </p>
   *
   * @throws PortalException
   *     throw if the configuration file can be read
   */
  @Override
  public String getSettingValue(final PortalSettingId pSettingId)
  {
    String value = pSettingId.getDefaultValue();
    if ((pSettingId != null) && (pSettingId.getId() != null))
    {
      final List<Setting> settings = portalConfig.getSettings().getSetting();
      if (settings != null)
      {
        for (final Setting setting : settings)
        {
          if (pSettingId.getId().equals(setting.getId()))
          {
            if (!Strings.isNullOrEmpty(setting.getValue()))
            {
              value = setting.getValue();
            }
            break;
          }
        }
      }
    }
    return value;
  }

  private PortalApplication getProjectDefaultApplication(final Locale pLocale, final String forgeProjectId,
                                                         final App internalApp, final ExternalApp externalApp)
      throws PortalException
  {
    PortalApplication returnApp  = null;
    App               appNotNull = null;
    if (internalApp != null)
    {
      appNotNull = internalApp;
    }
    else if (externalApp != null)
    {
      appNotNull = externalApp;
    }
    if (appNotNull != null)
    {
      final boolean isAuthorized = checkAuthorization(appNotNull, forgeProjectId, pLocale);
      if (isAuthorized)
      {
        returnApp = buildPortalApplication(appNotNull, forgeProjectId, pLocale);
      }
    }
    return returnApp;
  }

  private boolean checkAuthorization(final App pApp, final String pProjectId, final Locale pLocale)
      throws PortalException
  {
    boolean isAuthorized = true;
    if (pApp != null)
    {
      // Check if the internal application is known
      if ((!(pApp instanceof ExternalApp)) && (!PortalModuleId.isExist(pApp.getId())))
      {
        throw new PortalException(String.format("The given internal application id is unknown [id=%s]", pApp.getId()));
      }
      // Get permissions and check them
      final String permissionsRequested = pApp.getPermissions();
      isAuthorized = navigationSecurity.hasPermissions(pProjectId, permissionsRequested);

      // Get roles and check them
      final String rolesRequested = pApp.getRoles();
      if (isAuthorized)
      {
        isAuthorized = navigationSecurity.hasRoles(pProjectId, rolesRequested);
      }
    }
    return isAuthorized;
  }

  /**
   * Build an {@link PortalApplication} from an {@link App}
   *
   * @param pApp
   *     the source {@link App}
   *
   * @return {@link PortalApplication} built
   *
   * @throws PortalException
   *     if the application icon can not be got
   */
  private PortalApplication buildPortalApplication(final App pApp, final String pProjectId, final Locale pLocale)
      throws PortalException
  {
    final PortalApplication app = new PortalApplicationImpl();
    app.setId(pApp.getId());
    final UUID uniqueUUID = UUID.fromString(pApp.getUuid());
    app.setUniqueId(uniqueUUID);
    app.setName(navigationMessage.getNavigationMessage(pApp.getName(), pLocale));
    if (pApp.getDescription() != null)
    {
      app.setDescription(navigationMessage.getNavigationMessage(pApp.getDescription(), pLocale));
    }
    else
    {
      app.setDescription(navigationMessage.getNavigationMessage(pApp.getName(), pLocale));
    }
    if (pApp instanceof ExternalApp)
    {
      URL absoluteURL = resolveUri(pApp.getId(), pProjectId, pLocale);
      absoluteURL = addUniqueId(absoluteURL, uniqueUUID);
      app.setPortalURI(new PortalURIImpl(URI.create(absoluteURL.toExternalForm())));
    }
    else
    {
      app.setPortalURI(createPortalURI(pApp.getId()));
    }
    app.setAvailability(true);
    app.setIcon(getImageAsResource(pApp.getImg()));

    return app;
  }

  /**
   * This method allows to resolve an uri from configuration file according to its id and a project id
   *
   * @param pUriId
   *     the template uri id
   * @param pProjectId
   *     the current project id
   *
   * @return template uri found
   *
   * @throws PortalException
   *     thrown if the uri found is malformated
   */
  private URL resolveUri(final String pUriId, final String pProjectId, final Locale pLocale) throws PortalException
  {
    final StringBuilder uriString = new StringBuilder();
    // Get the uri from the templates section
    String uriFound = getTemplateUri(pUriId);
    if ((uriFound.startsWith("http://")) || (uriFound.startsWith("https://")))
    {
      uriString.append(uriFound);
    }
    else
    {
      uriString.append(forgeConfigurationService.getPublicUrl());
      if ((!uriString.toString().endsWith("/")) && (!uriFound.startsWith("/")))
      {
        uriString.append("/");
      }
      else if ((uriString.toString().endsWith("/")) && (uriFound.startsWith("/")))
      {
        uriFound = uriFound.substring(1);
      }
      final PortalStringTokenized string = new PortalStringTokenizedImpl(uriFound);
      string.setProjectId(pProjectId);
      string.setLocale(pLocale.getLanguage());
      uriString.append(navigationToken.resolved(string));
    }
    try
    {
      return new URL(uriString.toString());
    }
    catch (final MalformedURLException e)
    {
      throw new PortalException(String
                                    .format("The URL found in the navigation file (portal-config.xml) is mal-formated [url=%s]",
                                            uriString.toString()), e);
    }
  }

  private URL addUniqueId(final URL pURI, final UUID pUUID) throws PortalException
  {
    final String url = pURI.toString();
    final StringBuilder resultUrl = new StringBuilder(url);
    final char sep = url.contains("?") ? '&' : '?';
    resultUrl.append(sep).append("uuid=").append(pUUID.toString());
    try
    {
      return new URL(resultUrl.toString());
    }
    catch (final MalformedURLException e)
    {
      throw new PortalException(String.format(
          "Unable to build a full url with original and its uuid [url=%s, uuid=%s]", pURI, pUUID), e);
    }

  }

  /**
   * Will look for the resource stream associated to the image id
   *
   * @param pImageId
   *          the image id
   * @return byte array
   * @throws PortalException
   *           if io error occured
   */
  private Resource getImageAsResource(final String pImageId)
  {
    Resource     imgResource    = null;
    InputStream imageAsStream;
    final String ResourcePrefix = "ThemeResource=";
    try
    {
      if (pImageId != null)
      {
        if (pImageId.startsWith(ResourcePrefix))
        {
          String resourceId = pImageId.substring(ResourcePrefix.length());
          Field field = NovaForgeResources.class.getField(resourceId);
          Object themeResourceUri = field.get(String.class);
          imgResource = new ThemeResource(themeResourceUri.toString());
        }
        else if (pImageId.startsWith(File.separator))
        {
          imageAsStream = new FileInputStream(new File(pImageId));
          imgResource = ResourceUtils.buildImageResource(IOUtils.toByteArray(imageAsStream), pImageId);
        }
        else
        {
          final String iconPath = forgeConfigurationService.getPortalConfDirectory() + File.separatorChar + pImageId;
          imageAsStream = new FileInputStream(new File(iconPath));
          imgResource = ResourceUtils.buildImageResource(IOUtils.toByteArray(imageAsStream), pImageId);
        }

      }

    }
    catch (final Exception e)
    {
      imgResource = new ThemeResource(NovaForgeResources.ICON_UNKNOW);
    }

    return imgResource;
  }

  /**
   * Get the template Uri according to the parameter
   *
   * @param pUriId
   *          the uri id
   * @return template uri found
   */
  private String getTemplateUri(final String pUriId)
  {
    String uriFound = "";
    if (pUriId != null)
    {
      final List<UriTemplate> uriTemplates = portalConfig.getUriTemplates().getUriTemplate();
      for (final UriTemplate uriTemplate : uriTemplates)
      {
        if ((uriTemplate.getId() != null) && (!"".equals(uriTemplate.getId())) && (uriTemplate.getId().equals(pUriId)))
        {
          uriFound = uriTemplate.getValue();
        }
      }
    }
    return uriFound;
  }

  /**
   * This method will return a list of {@link PortalSpace} which contains {@link PortalApplication} according
   * the current user authorization.
   *
   * @param pProjectId
   *     the project id for which we want the list of {@link PortalSpace}
   * @param pSpaces
   *     the source list of {@link Space}
   *
   * @return list of {@link PortalSpace}
   *
   * @throws PortalException
   *     thrown if an error occured during the process
   */
  private List<PortalSpace> getSpaces(final String pProjectId, final List<Space> pSpaces, final Locale pLocale)
      throws PortalException
  {
    final List<PortalSpace> portalSpaces            = new ArrayList<PortalSpace>();
    final List<Space>       filteredOnAuthorization = filteredOnAuthorization(pSpaces, pProjectId);
    for (final Space space : filteredOnAuthorization)
    {
      final PortalSpace portalSpace = buildPortalSpace(space, pLocale);
      final List<PortalApplication> applications;
      applications = getApplications(space, pProjectId, pLocale);
      if (applications != null)
      {
        for (final PortalApplication portalApplication : applications)
        {
          portalSpace.addApplication(portalApplication);
        }
      }
      portalSpaces.add(portalSpace);
    }
    return portalSpaces;
  }

  /**
   * This method will filter the given list of spaces according to the permissions or roles asking.
   *
   * @param pSpaces
   *          spaces sources
   * @param pProjectId
   *          project id
   * @return {@link Space} authorized
   */
  private List<Space> filteredOnAuthorization(final List<Space> pSpaces, final String pProjectId)
  {
    final List<Space> returnSpaces = new ArrayList<Space>();
    for (final Space space : pSpaces)
    {
      boolean isAuthorized = false;
      final String currentUser = authentificationService.getCurrentUser();
      final DisplayType display = space.getDisplay();
      if ((DisplayType.AUTHENTICATED.equals(display)) && (currentUser != null))
      {
        isAuthorized = true;
      }
      else if ((DisplayType.UNAUTHENTICATED.equals(display))
          && ((currentUser == null) || ("".equals(currentUser))))
      {
        isAuthorized = true;

      }
      else if (DisplayType.ALWAYS.equals(display))
      {
        isAuthorized = true;
      }
      // Get permissions and check them
      if (isAuthorized)
      {
        final String permissionsRequested = space.getPermissions();
        isAuthorized = navigationSecurity.hasPermissions(pProjectId, permissionsRequested);
      }
      // Get roles and check them
      if (isAuthorized)
      {
        final String rolesRequested = space.getRoles();
        isAuthorized = navigationSecurity.hasRoles(pProjectId, rolesRequested);
      }

      // Build and add the space to the return list
      if (isAuthorized)
      {
        returnSpaces.add(space);
      }
    }
    return returnSpaces;
  }

  /**
   * This method will build a list of {@link PortalApplication} available for the current user according to
   * the configuration file.
   * <ul>
   * <li>Browse all available application for the space</li>
   * <li>For each application it will check the permissions and roles set up</li>
   * <li>For each application it will resolve and build the gadget url</li>
   * </ul>
   *
   * @param pSpace
   *          the current space
   * @param pProjectId
   *          the current project id
   * @return {@link PortalApplication} authorized
   * @throws PortalException
   *           throw
   */
  private List<PortalApplication> getApplications(final Space pSpace, final String pProjectId, final Locale pLocale)
      throws PortalException
  {
    final List<PortalApplication> apps = new ArrayList<PortalApplication>();
    if (pSpace.getApps() != null)
    {
      // Build application from external app
      apps.addAll(getExternalApps(pSpace.getApps().getExternalApp(), pProjectId, pLocale));

      // Build application from external app
      apps.addAll(getApps(pSpace.getApps().getInternalApp(), pProjectId, pLocale));

    }
    return apps;
  }

  /**
   * Build a @{@link List} of {@link PortalApplication} from a @{@link List} of {@link ExternalApp}
   *
   * @param pExtApps
   *          the source list of {@link ExternalApp}
   * @param pProjectId
   *          a projectId, can be empty but not null
   * @return @{@link List} of {@link PortalApplication} built
   * @throws PortalException
   */
  private List<PortalApplication> getExternalApps(final List<ExternalApp> pExtApps, final String pProjectId,
      final Locale pLocale) throws PortalException
  {
    final List<PortalApplication> apps = new ArrayList<PortalApplication>();

    for (final ExternalApp extApp : pExtApps)
    {
      final boolean isAuthorized = checkAuthorization(extApp, pProjectId, pLocale);

      if (isAuthorized)
      {
        apps.add(buildPortalApplication(extApp, pProjectId, pLocale));
      }
    }
    return apps;
  }

  /**
   * Build a @{@link List} of {@link PortalApplication} from a @{@link List} of {@link App}
   *
   * @param pApps
   *          the source list of {@link App}
   * @param pProjectId
   *          a projectId, can be empty but not null
   * @return @{@link List} of {@link PortalApplication} built
   * @throws PortalException
   */
  private List<PortalApplication> getApps(final List<App> pApps, final String pProjectId, final Locale pLocale)
      throws PortalException
  {
    final List<PortalApplication> apps = new ArrayList<PortalApplication>();
    for (final App app : pApps)
    {
      final boolean isAuthorized = checkAuthorization(app, pProjectId, pLocale);

      if (isAuthorized)
      {
        apps.add(buildPortalApplication(app, pProjectId, pLocale));
      }
    }
    return apps;
  }

  /**
   * Build an {@link PortalSpace} from an {@link Space}
   *
   * @param pSpace
   *          the source {@link Space}
   * @return {@link PortalSpace} built
   * @throws PortalException
   *           throw if user cannot be get
   */
  private PortalSpace buildPortalSpace(final Space pSpace, final Locale pLocale) throws PortalException
  {
    final PortalSpace portalSpace = new PortalSpaceImpl();
    portalSpace.setId(pSpace.getId());
    portalSpace.setDescription(navigationMessage.getNavigationMessage(pSpace.getDescription(), pLocale));
    final String userLogin = authentificationService.getCurrentUser();
    if ((userLogin != null) && (navigationToken.containsToken(pSpace.getName(), PortalToken.USER_NAME)))
    {
      final PortalStringTokenized string = new PortalStringTokenizedImpl(pSpace.getName());
      try
      {
        final User user = userService.getUser(userLogin);
        string.setUserName(user.getFirstName() + " " + user.getName());
        portalSpace
            .setName(navigationMessage.getNavigationMessage(navigationToken.resolved(string), pLocale));
      }
      catch (final UserServiceException e)
      {
        throw new PortalException(String.format(
            "The space name contains username token, but unenable to get user entity with [login=%s]",
            userLogin), e);
      }
    }
    else
    {
      portalSpace.setName(navigationMessage.getNavigationMessage(pSpace.getName(), pLocale));
    }
    return portalSpace;
  }

  /**
   * Bind method used by the container to inject {@link ForgeConfigurationManager} service.
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationManager to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * Bind method used by the container to inject {@link AuthentificationService} service.
   * 
   * @param pAuthentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  /**
   * Bind method used by the container to inject {@link PluginsManager} service.
   * 
   * @param pPluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  /**
   * Bind method used by the container to inject {@link UserService} service.
   * 
   * @param pUserService
   *          the userService to set
   */
  public void setUserService(final UserService pUserService)
  {
    userService = pUserService;
  }

  /**
   * Bind method used by the container to inject {@link NavigationToken} service.
   * 
   * @param pNavigationToken
   *          the tokenTemplate to set
   */
  public void setNavigationToken(final NavigationToken pNavigationToken)
  {
    navigationToken = pNavigationToken;
  }

  /**
   * Bind method used by the container to inject {@link NavigationMessage} service.
   * 
   * @param pNavigationMessage
   *          the navigationMessage to set
   */
  public void setNavigationMessage(final NavigationMessage pNavigationMessage)
  {
    navigationMessage = pNavigationMessage;
  }

  /**
   * Bind method used by the container to inject {@link NavigationXML} service.
   * 
   * @param pNavigationXML
   *          the navigationXML to set
   */
  public void setNavigationXML(final NavigationXML pNavigationXML)
  {
    navigationXML = pNavigationXML;
  }

  /**
   * Bind method used by the container to inject {@link NavigationSecurity} service.
   * 
   * @param pNavigationSecurity
   *          the navigationSecurity to set
   */
  public void setNavigationSecurity(final NavigationSecurity pNavigationSecurity)
  {
    navigationSecurity = pNavigationSecurity;
  }

}
