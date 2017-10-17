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
package org.novaforge.forge.portal.models;

import com.vaadin.server.Resource;
import org.novaforge.forge.core.organization.model.ApplicationStatus;

import java.io.Serializable;
import java.util.UUID;

/**
 * This interface describes an element inside a {@link PortalSpace} which referes to a concret application.
 * 
 * @author Guillaume Lamirand
 */
public interface PortalApplication extends Serializable, Comparable<PortalApplication>
{
  /**
   * Get the application functional identifiant.
   * 
   * @return application id
   */
  String getId();

  /**
   * Set the application functional identifiant
   * 
   * @param pId
   *          the id to set
   */
  void setId(final String pId);

  /**
   * Get the unique technical id.
   * 
   * @return unique technical id
   */
  UUID getUniqueId();

  /**
   * Set the unique technical id.
   * 
   * @param pId
   *          the id to set
   */
  void setUniqueId(final UUID pId);

  /**
   * Get the application name.
   * 
   * @return application name
   */
  String getName();

  /**
   * Set the application name.
   * 
   * @param pName
   *          the name to set
   */
  void setName(final String pName);

  /**
   * Get the application description.
   * 
   * @return application description
   */
  String getDescription();

  /**
   * Set the application description.
   * 
   * @param pDescription
   *          the description to set
   */
  void setDescription(final String pDescription);

  /**
   * Get the application portal uri.
   * 
   * @return application portal uri
   */
  PortalURI getPortalURI();

  /**
   * Set the application portal uri.
   * 
   * @param pPortalUri
   *          the portal uri to set
   */
  void setPortalURI(final PortalURI pPortalUri);

  /**
   * Get the application icon as a resource.
   * 
   * @return application icon
   */
  Resource getIcon();

  /**
   * Set the application icon.
   * 
   * @param pIcon
   *          the icon to set
   */
  void setIcon(final Resource pIcon);

  /**
   * Get the application status.
   * 
   * @return application status
   */
  ApplicationStatus getStatus();

  /**
   * Set the application status.
   * 
   * @param pStatus
   *          the availability to set
   */
  void setStatus(final ApplicationStatus pStatus);

  /**
   * Get the application availability.
   * 
   * @return application availability
   */
  boolean isAvailable();

  /**
   * Set the application availability.
   * 
   * @param pAvailability
   *          the availability to set
   */
  void setAvailability(final boolean pAvailability);

}
