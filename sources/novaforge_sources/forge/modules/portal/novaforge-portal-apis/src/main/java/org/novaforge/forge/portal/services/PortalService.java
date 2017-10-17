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

import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectInfo;
import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.models.PortalSpace;

import java.util.List;
import java.util.Locale;

/**
 * This interface describes a service used by the portal ui to display element.
 * 
 * @author Guillaume Lamirand
 */
public interface PortalService
{

  /**
   * Find a {@link ProjectInfo} identified by the given id.
   * 
   * @param pProjectId
   *          the project identifiant
   * @return The {@link ProjectInfo} object if it is found, else <code>null</code>
   * @throws PortalException
   *           if an error occured during the process
   */
  Project getProject(final String pProjectId) throws PortalException;

  /**
   * Find all {@link Project} object related to the current user.
   * 
   * @return {@link List} which contains {@link Project} objects.
   * @throws PortalException
   *           if an error occured during the process
   */
  List<Project> getUserProjects() throws PortalException;

  /**
   * Find {@link Project} object which have a public visibility.
   * 
   * @return {@link List} which contains public {@link Project}.
   * @throws PortalException
   *           if an error occured during the process
   */
  List<Project> getPublicProjects() throws PortalException;

  /**
   * Find the main {@link ProjectInfo}.
   * 
   * @return The {@link ProjectInfo} object if it is found, else <code>null</code>
   * @throws PortalException
   *           if an error occured during the process
   */
  Project getMainProject() throws PortalException;

  /**
   * Find the navigation for the main {@link Project}.
   * 
   * @param pLocale
   *          The locale of current user for name
   * @return {@link List} which contains {@link PortalSpace}.
   * @throws PortalException
   *           if an error occured during the process
   */
  List<PortalSpace> getMainNavigation(final Locale pLocale) throws PortalException;

  /**
   * Find the navigation for {@link Project} given as paremeter.
   * 
   * @param pProjectId
   *          the project id, cannot be null or empty
   * @param pLocale
   *          The locale of current user for name
   * @return {@link List} which contains {@link PortalSpace}.
   * @throws PortalException
   *           if an error occured during the process
   */
  List<PortalSpace> getProjectNavigation(final String pProjectId, final Locale pLocale)
      throws PortalException;

  /**
   * Get specific {@link ApplicationPortal} according the the {@link PortalModuleId} given
   * 
   * @param pId
   *          the application id
   * @param pLocale
   *          The locale of current user for name
   * @return the {@link PortalApplication} found or <code>null</code>
   * @throws PortalException
   *           if error occured when getting the configuration file elements.
   */
  PortalApplication getLinkFromId(final PortalModuleId pId, final Locale pLocale) throws PortalException;

  /**
   * Find the account spaces defined in portal configuration file.
   * 
   * @param pLocale
   *          The locale of current user for name
   * @return {@link PortalSpace}.
   * @throws PortalException
   *           if an error occured during the process
   */
  PortalSpace getAccountSpaces(final Locale pLocale) throws PortalException;

  /**
   * Get the user locale
   * 
   * @param pUser
   *          the user
   * @return the locale for this user or <code>null</code> if no user is authenticated
   */
  Locale getCurrentUserLocale();

  /**
   * Get the portal application for a plugin
   * 
   * @param pPluginUUID
   *          The plugin UUID
   * @param pToolUUID
   *          The tool UUID
   * @param pInstnaceId
   *          The instance id
   * @param pProjectId
   *          The project id
   * @param pPluginView
   *          The plugin view
   * @return the PortalApplication
   * @throws PortalException
   */
  PortalApplication getPortalApplication(String pPluginUUID, String pToolUUID, String pInstnaceId,
      String pProjectId, String pPluginView) throws PortalException;

  /**
   * Get the default application set up for forge
   * 
   * @param pLocale
   *          The locale of current user for name
   * @return {@link ProjectApplication} retrieved from configuration file. It can be <code>null</code>
   * @throws PortalException
   *           throw when errors occured to get configuration element
   */
  PortalApplication getMainDefaultApplication(final Locale pLocale) throws PortalException;

  /**
   * Get the default application for a project set up for forge.
   * 
   * @param pProjectId
   *          the project id for which we are looking for its default application
   * @param pLocale
   *          The locale of current user for name
   * @return {@link ProjectApplication} retrieved from configuration file. It can be <code>null</code>
   * @throws PortalException
   *           throw when errors occured to get configuration element
   */
  PortalApplication getProjectDefaultApplication(final String pProjectId, final Locale pLocale)
      throws PortalException;

  /**
   * Get the theme name
   * 
   * @return theme name as string object
   */
  String getTheme();

}
