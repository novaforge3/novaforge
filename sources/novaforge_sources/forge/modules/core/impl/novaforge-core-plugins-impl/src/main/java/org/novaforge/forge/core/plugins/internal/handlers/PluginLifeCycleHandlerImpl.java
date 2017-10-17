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
package org.novaforge.forge.core.plugins.internal.handlers;

import com.google.common.collect.Iterables;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.delegates.MailDelegate;
import org.novaforge.forge.core.organization.delegates.MembershipDelegate;
import org.novaforge.forge.core.organization.exceptions.MailDelegateException;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum;
import org.novaforge.forge.core.plugins.dao.PluginMetadataDAO;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.handlers.PluginLifeCycleHandler;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsCategoryManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * @author sbenoist
 * @author Guillaume Lamirand
 */
public class PluginLifeCycleHandlerImpl implements PluginLifeCycleHandler
{
  private static final Log LOGGER = LogFactory.getLog(PluginLifeCycleHandlerImpl.class);
  private final BundleContext bundleContext;
  private MailDelegate              mailDelegate;
  private MembershipDelegate        membershipDelegate;
  private PluginsCategoryManager    pluginsCategoryManager;
  private PluginMetadataDAO         pluginMetadataDAO;
  private ForgeConfigurationService forgeConfigurationService;

  /**
   * Default constructor
   * 
   * @param pBundleContext
   *          the bundle context
   */
  public PluginLifeCycleHandlerImpl(final BundleContext pBundleContext)
  {
    bundleContext = pBundleContext;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.CHANGE_PLUGIN_STATUS)
  public void changePluginLifeCycle(
      @HistorizableParam(label = "plugin") final PluginMetadata pPluginMetadata, @HistorizableParam(
          label = "old status") final PluginStatus pOldStatus,
      @HistorizableParam(label = "new status") final PluginStatus pNewStatus) throws PluginManagerException
  {
    final String uuid = pPluginMetadata.getUUID();
    try
    {

      checkAuthorizedStatus(uuid, pOldStatus, pNewStatus);

      // change the plugin bundle lifecycle
      switch (pOldStatus)
      {
        case INSTALLED:
          enablePluginRoute(null, uuid);
          break;

        case ACTIVATED:
          if (PluginStatus.DESACTIVATED.equals(pNewStatus))
          {
            desactivateTool(uuid);
            disablePluginRoute(null, uuid);
          }

          break;

        case DEPRECATED:
          if (PluginStatus.DESACTIVATED.equals(pNewStatus))
          {
            desactivateTool(uuid);
            disablePluginRoute(null, uuid);
          }

          break;

        case DESACTIVATED:
          if ((PluginStatus.ACTIVATED.equals(pNewStatus)) || (PluginStatus.DEPRECATED.equals(pNewStatus)))
          {
            reactivateTool(uuid);
            enablePluginRoute(null, uuid);
          }
          break;

        case STOPPED:
          break;
        default:
          break;
      }

      final PluginPersistenceMetadata pluginPersistenceMetadata = pluginMetadataDAO.findByUUID(UUID
          .fromString(uuid));
      // update the status
      pluginPersistenceMetadata.setStatus(pNewStatus);
      pluginMetadataDAO.update(pluginPersistenceMetadata);
    }
    catch (final Exception e)
    {
      throw new PluginManagerException(String.format("Problem while getting PluginMetadata with [uuid=%s",
          uuid), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginStatus> getAuthorizedChangesForStatus(final PluginStatus pStatus)
  {
    final List<PluginStatus> authorizedStatus = new ArrayList<PluginStatus>();
    switch (pStatus)
    {
      case INSTALLED:
        authorizedStatus.add(PluginStatus.ACTIVATED);
        break;

      case ACTIVATED:
        authorizedStatus.add(PluginStatus.DEPRECATED);
        authorizedStatus.add(PluginStatus.DESACTIVATED);
        break;

      case DEPRECATED:
        authorizedStatus.add(PluginStatus.ACTIVATED);
        authorizedStatus.add(PluginStatus.DESACTIVATED);
        break;

      case DESACTIVATED:
        authorizedStatus.add(PluginStatus.ACTIVATED);
        authorizedStatus.add(PluginStatus.DEPRECATED);
        authorizedStatus.add(PluginStatus.STOPPED);
        break;

      case STOPPED:
        authorizedStatus.add(PluginStatus.DESACTIVATED);
        authorizedStatus.add(PluginStatus.UNINSTALLED);

        break;
      default:
      case UNINSTALLED:
        break;
    }
    return authorizedStatus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<PluginStatus, List<PluginStatus>> getAllAuthorizedChanges()
  {
    final Map<PluginStatus, List<PluginStatus>> allAuthorizedChanges = new HashMap<PluginStatus, List<PluginStatus>>();

    final List<PluginStatus> statusList = Arrays.asList(PluginStatus.values());
    for (final PluginStatus pluginStatus : statusList)
    {
      allAuthorizedChanges.put(pluginStatus, getAuthorizedChangesForStatus(pluginStatus));
    }
    return allAuthorizedChanges;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void disablePluginRoute(final PluginService pPluginService, final String pUUID) throws PluginManagerException
  {
    try
    {
      PluginService pluginService = pPluginService;
      if (pluginService == null)
      {
        pluginService = getPluginService(pUUID);
      }
      if (pluginService == null)
      {

        throw new PluginManagerException(String
                                             .format("Problem while getting plugin service from OSGi registry with [uuid=%s]",
                                                     pUUID));
      }
      pluginService.enableRoute(false);
    }
    catch (final InvalidSyntaxException e)
    {
      throw new PluginManagerException(String.format("Problem while getting plugin service from OSGi registry with [uuid=%s]",
                                                     pUUID));
    }
    catch (final PluginServiceException e)
    {
      throw new PluginManagerException(String.format("Unable to disable route for plugin with [uuid=%s]", pUUID), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enablePluginRoute(final PluginService pPluginService, final String pUUID) throws PluginManagerException
  {
    try
    {
      PluginService pluginService = pPluginService;
      if (pluginService == null)
      {
        pluginService = getPluginService(pUUID);
      }
      if (pluginService == null)
      {

        throw new PluginManagerException(String
                                             .format("Problem while getting plugin service from OSGi registry with [uuid=%s]",
                                                     pUUID));
      }
      pluginService.enableRoute(true);
    }
    catch (final InvalidSyntaxException e)
    {
      throw new PluginManagerException(String.format("Problem while getting plugin service from OSGi registry with [uuid=%s]",
                                                     pUUID), e);
    }
    catch (final PluginServiceException e)
    {
      throw new PluginManagerException(String.format("Unable to enable route for plugin with [uuid=%s]", pUUID), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void notifyPluginLifecycleChange(final PluginMetadata pPluginMetadata) throws PluginManagerException
  {
    if (mailDelegate != null)
    {
      if (PluginStatus.STOPPED.equals(pPluginMetadata.getStatus()) || PluginStatus.UNINSTALLED.equals(pPluginMetadata
                                                                                                          .getStatus()))
      {
        sendRequestForPluginLifeCycleChange(pPluginMetadata);
      }

      notifyPluginLifecycleChanges(pPluginMetadata);
    }
  }

  private void sendRequestForPluginLifeCycleChange(final PluginMetadata pPluginMetadata)
      throws PluginManagerException
  {
    final String pluginUUID = pPluginMetadata.getUUID();
    final PluginStatus pluginNewStatus = pPluginMetadata.getStatus();
    final String pluginCategory = pPluginMetadata.getCategory();
    final String pluginType = pPluginMetadata.getType();
    final String pluginVersion = pPluginMetadata.getVersion();

    final String systemAdministratorEmail = forgeConfigurationService.getSystemAdministratorEMail();
    final Locale locale = Locale.ENGLISH;
    final String statusLabel = pluginNewStatus.getLabel(locale);
    final String categoryName = pluginsCategoryManager.getCategoryService(pluginCategory).getName(locale);

    if (PluginStatus.STOPPED.equals(pPluginMetadata.getStatus()))
    {
      try
      {
        mailDelegate.sendLifeCycleChangeRequest(systemAdministratorEmail, categoryName, pluginType,
            pluginVersion, statusLabel, MailDelegateEnum.PLUGIN_STOP_REQUEST, locale);
      }
      catch (final MailDelegateException e)
      {
        throw new PluginManagerException(
            String.format(
                "Error occured during sending email to administrator for stopping plugin [plugin uuid=%s, type=%s]",
                pluginUUID, pluginType), e);
      }
    }
    else if (PluginStatus.UNINSTALLED.equals(pPluginMetadata.getStatus()))
    {
      try
      {
        mailDelegate.sendLifeCycleChangeRequest(systemAdministratorEmail, categoryName, pluginType,
            pluginVersion, statusLabel, MailDelegateEnum.PLUGIN_UNINSTALL_REQUEST, locale);
      }
      catch (final MailDelegateException e)
      {
        throw new PluginManagerException(
            String.format(
                "Error occured during sending email to system administrator for uninstall plugin [plugin uuid=%s, type=%s]",
                pluginUUID, pluginType), e);
      }
    }
  }

  /**
   * @param pPluginMetadata
   * @throws PluginManagerException
   */
  public void notifyPluginLifecycleChanges(final PluginMetadata pPluginMetadata)
      throws PluginManagerException
  {
    final String       pluginUUID      = pPluginMetadata.getUUID();
    final PluginStatus pluginNewStatus = pPluginMetadata.getStatus();
    final String       pluginCategory  = pPluginMetadata.getCategory();
    final String       pluginType      = pPluginMetadata.getType();
    final String       pluginVersion   = pPluginMetadata.getVersion();

    final List<User> administrators = membershipDelegate.getAllProjectAdministrators();
    for (final User user : administrators)
    {
      final Locale locale = user.getLanguage().getLocale();
      final String categoryName = pluginsCategoryManager.getCategoryService(pluginCategory).getName(locale);
      final String statusLabel = pluginNewStatus.getLabel(locale);
      try
      {
        mailDelegate.sendLifeCycleChangeNotification(user.getEmail(), categoryName, pluginType, pluginVersion,
                                                     statusLabel, locale);
      }
      catch (final MailDelegateException e)
      {
        LOGGER.error(String
                         .format("Error occured during sending email to notify changes [plugin=%s, new_status=%s, type=%s,] and [email=%s]",
                                 pluginUUID, pluginNewStatus, pluginType, user.getEmail()), e);
      }
    }

  }

  private void checkAuthorizedStatus(final String pPluginUUID, final PluginStatus pOldStatus,
                                     final PluginStatus pNewStatus) throws PluginManagerException
  {
    final List<PluginStatus> authorizedStatus = getAuthorizedChangesForStatus(pOldStatus);
    if (!authorizedStatus.contains(pNewStatus))
    {
      throw new PluginManagerException(String
                                           .format("Unauthorized status change in plugin lifecycle [uuid=%s, from %s to %s",
                                                   pPluginUUID, pOldStatus.getLabel(), pNewStatus.getLabel()));
    }
  }

  private void desactivateTool(final String pPluginUUID) throws PluginManagerException
  {
    try
    {
      getPluginService(pPluginUUID).desactivate(getAllSuperAdmins());
    }
    catch (final InvalidSyntaxException e)
    {
      throw new PluginManagerException(String.format("Problem while getting plugin service from OSGi registry with [uuid=%s]",
                                                     pPluginUUID), e);
    }
    catch (final PluginServiceException e)
    {
      throw new PluginManagerException(String.format("an error occured during desactivating the tool [plugin uuid=%s]",
                                                     pPluginUUID), e);
    }
  }

  private void reactivateTool(final String pPluginUUID) throws PluginManagerException
  {
    try
    {
      getPluginService(pPluginUUID).reactivate();
    }
    catch (final InvalidSyntaxException e)
    {
      throw new PluginManagerException(String.format("Problem while getting plugin service from OSGi registry with [uuid=%s]",
                                                     pPluginUUID), e);
    }
    catch (final PluginServiceException e)
    {
      throw new PluginManagerException(String.format("an error occured during reactivating the tool [plugin uuid=%s]",
                                                     pPluginUUID), e);
    }
  }

  private PluginService getPluginService(final String pId) throws InvalidSyntaxException
  {
    PluginService pluginService = null;
    final Collection<ServiceReference<PluginService>> refs = bundleContext.getServiceReferences(PluginService.class,
                                                                                                String.format("(%s=%s)",
                                                                                                              PluginService.ID_PROPERTY,
                                                                                                              pId));
    if ((refs != null) && (!refs.isEmpty()) && (refs.size() == 1))
    {
      pluginService = bundleContext.getService(Iterables.get(refs, 0));
    }
    return pluginService;
  }

  private Map<String, String> getAllSuperAdmins()
  {
    final Map<String, String> superAdmins = new HashMap<String, String>();

    final List<User> users = membershipDelegate.getAllSuperAdmin();
    for (final User user : users)
    {
      superAdmins.put(user.getLogin(), user.getPassword());
    }
    return superAdmins;
  }

}
