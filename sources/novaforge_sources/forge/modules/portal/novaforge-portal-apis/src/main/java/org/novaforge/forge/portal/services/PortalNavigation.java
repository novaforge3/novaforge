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
package org.novaforge.forge.portal.services;

import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalSettingId;
import org.novaforge.forge.portal.models.PortalSpace;
import org.novaforge.forge.portal.models.PortalURI;

import java.net.URI;
import java.util.List;
import java.util.Locale;

/**
 * Definition of a service which manage portal navigation.
 * 
 * @author Guillaume Lamirand
 */
public interface PortalNavigation
{

  /**
   * Get the forge spaces and application set up.
   * 
   * @param pLocale
   *          The locale of current user for name
   * @return {@link List} of {@link PortalSpace}
   * @throws PortalException
   *           throw when errors occured to get configuration element
   */
  List<PortalSpace> getForgeSpaces(final Locale pLocale) throws PortalException;

  /**
   * Get the default application set up for forge
   * 
   * @param pLocale
   *          The locale of current user for name
   * @return {@link ProjectApplication} built from configuration file. It can be <code>null</code>
   * @throws PortalException
   *           throw when errors occured to get configuration element
   */
  PortalApplication getForgeDefaultApplication(final Locale pLocale) throws PortalException;

  /**
   * Get the project spaces and applications set up.
   * 
   * @param pProjectId
   *          the project id for which we are looking for its spaces
   * @param pLocale
   *          The locale of current user for name
   * @return {@link List} of {@link PortalSpace}
   * @throws PortalException
   *           throw when errors occured to get configuration element
   */
  List<PortalSpace> getProjectSpaces(final String pProjectId, final Locale pLocale) throws PortalException;

  /**
   * Get the default application for a project set up for forge
   * 
   * @param pProjectId
   *          the project id for which we are looking for its default application
   * @param pLocale
   *          The locale of current user for name
   * @return {@link ProjectApplication} built from configuration file. It can be <code>null</code>
   * @throws PortalException
   *           throw when errors occured to get configuration element
   */
  PortalApplication getProjectDefaultApplication(final String pProjectId, final Locale pLocale)
      throws PortalException;

  /**
   * Get the account spaces set up.
   * 
   * @param pLocale
   *          The locale of current user for name
   * @return {@link PortalSpace}
   * @throws PortalException
   *           throw when errors occured to get configuration element
   */
  PortalSpace getAccountSpaces(final Locale pLocale) throws PortalException;

  /**
   * Get list of {@link ApplicationPortal} defined as link element
   * 
   * @param pLocale
   *          The locale of current user for name
   * @return {@link List} of {@link ApplicationPortal}
   * @throws PortalException
   *           throw when errors occured to get configuration element
   */
  List<PortalApplication> getLink(final Locale pLocale) throws PortalException;

  /**
   * Get the application URL according the parameters given.
   * 
   * @param pUUID
   *          the plugin uuid
   * @param pInstanceId
   *          the plugin instance id. Can be <code>null<code>
   * @param pToolUid
   *          the tool instance uuid. Can be <code>null<code>
   * @param pProjectId
   *          the project id
   * @param pPluginView
   *          the view wanted
   * @param pLocale
   *          the user locale
   * @return {@link URI} got
   * @throws PortalException
   *           throw when errors occured to get plugin metadata information
   */
  PortalURI getApplicationURI(final String pUUID, final String pInstanceId, final String pToolUid,
      final String pProjectId, final String pPluginView, final Locale pLocale) throws PortalException;

  /**
   * This method will build {@link PortalURI} using the given module id
   * 
   * @param pModuleId
   *          the module id
   * @return {@link PortalURI} build from the given module id
   * @throws PortalException
   *           if an error occured during the process
   */
  PortalURI createPortalURI(final String pModuleId) throws PortalException;

  /**
   * This method should be used to refresh navigation element.
   * 
   * @throws PortalException
   *           this can be thrown if refresh failed
   */
  void refresh() throws PortalException;

  /**
   * Get the setting value according the ID given
   * 
   * @param pSettingId
   *          the setting id to find
   * @return string value found can be <code>null</code> or empty
   */
  String getSettingValue(final PortalSettingId pSettingId);

}