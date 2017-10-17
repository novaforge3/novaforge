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

import org.novaforge.forge.core.organization.model.enumerations.RealmType;

import java.io.Serializable;
import java.util.List;

/**
 * This interface describes an element which is used to categorize the navigation tree inside the portal
 * 
 * @author Guillaume Lamirand
 */
public interface PortalSpace extends Serializable
{

  /**
   * Get the space id.
   * 
   * @return space id
   */
  String getId();

  /**
   * Set the space identifiant
   * 
   * @param pId
   *          the id to set
   */
  void setId(final String pId);

  /**
   * Get the space name.
   * 
   * @return space name
   */
  String getName();

  /**
   * Set the space name.
   * 
   * @param pName
   *          the name to set
   */
  void setName(final String pName);

  /**
   * Get the space decription.
   * 
   * @return space description
   */
  String getDescription();

  /**
   * Set the space description.
   * 
   * @param pDescription
   *          the description to set
   */
  void setDescription(final String pDescription);

  /**
   * Get the space realm type
   * 
   * @return RealmType
   */
  RealmType getRealmType();

  /**
   * Set the space realm type
   * 
   * @param pRealmType
   */
  void setRealmType(RealmType pRealmType);

  /**
   * Get applications available for the space
   * 
   * @return unmodifiable list of {@link PortalApplication}
   */
  List<PortalApplication> getApplications();

  /**
   * Add an application to the space.
   * 
   * @param pApplication
   *          the application to add
   * @return true or false according to the success
   * @see List#add(Object)
   */
  boolean addApplication(final PortalApplication pApplication);

  /**
   * Remove an application to the space.
   * 
   * @param pApplication
   *          the application to add
   * @return true or false according to the success
   * @see List#remove(Object)
   */
  boolean removeApplication(final PortalApplication pApplication);
}
