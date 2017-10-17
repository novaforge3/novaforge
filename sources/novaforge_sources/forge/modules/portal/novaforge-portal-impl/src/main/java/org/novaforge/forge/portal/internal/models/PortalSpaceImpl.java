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
package org.novaforge.forge.portal.internal.models;

import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalSpace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of {@link PortalSpace}
 * 
 * @author Guillaume Lamirand
 */
public class PortalSpaceImpl implements PortalSpace
{

  /**
   * Serial version id used for serialization
   */
  private static final long             serialVersionUID = -7030217329954200763L;
  /**
   * Contains the list of {@link PortalApplication} available for the space
   */
  private final List<PortalApplication> apps;
  /**
   * Contains the space id
   */
  private       String                  id;
  /**
   * Contains the space name
   */
  private       String                  name;
  /**
   * Contains the space description
   */
  private       String                  description;
  /**
   * Contains the space realmtype
   */
  private       RealmType               realmType;

  /**
   * Default constructor.
   */
  public PortalSpaceImpl()
  {
    apps = new ArrayList<PortalApplication>();
  }

  /**
   * {@inheritDoc}
   *
   * @see PortalSpaceImpl#id
   */
  @Override
  public String getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   *
   * @see PortalSpaceImpl#id
   */
  @Override
  public void setId(final String pId)
  {
    id = pId;
  }

  /**
   * {@inheritDoc}
   *
   * @see PortalSpaceImpl#name
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   *
   * @see PortalSpaceImpl#name
   */
  @Override
  public void setName(final String pName)
  {
    name = pName;
  }

  /**
   * {@inheritDoc}
   *
   * @see PortalSpaceImpl#description
   */
  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   *
   * @see PortalSpaceImpl#description
   */
  @Override
  public void setDescription(final String pDescription)
  {
    description = pDescription;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RealmType getRealmType()
  {
    return realmType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRealmType(final RealmType pRealmType)
  {
    realmType = pRealmType;
  }

  /**
   * {@inheritDoc}
   *
   * @see PortalSpaceImpl#apps
   */
  @Override
  public List<PortalApplication> getApplications()
  {
    return Collections.unmodifiableList(apps);
  }

  /**
   * {@inheritDoc}
   *
   * @see PortalSpaceImpl#apps
   */
  @Override
  public boolean addApplication(final PortalApplication pApplication)
  {
    return apps.add(pApplication);
  }

  /**
   * {@inheritDoc}
   *
   * @see PortalSpaceImpl#apps
   */
  @Override
  public boolean removeApplication(final PortalApplication pApplication)
  {
    return apps.remove(pApplication);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "PortalSpaceImpl [id=" + id + ", name=" + name + ", description=" + description + ", apps=" + apps + "]";
  }

}
