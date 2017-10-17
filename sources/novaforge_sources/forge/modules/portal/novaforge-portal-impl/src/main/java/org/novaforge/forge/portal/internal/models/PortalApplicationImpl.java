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

import com.vaadin.server.Resource;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalURI;

import java.util.UUID;

/**
 * Default implementation of {@link PortalApplication}
 * 
 * @author Guillaume Lamirand
 */
public class PortalApplicationImpl implements PortalApplication
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -8182440956590559563L;
  /**
   * Contains the application functional id
   */
  private String            id;
  /**
   * Contains the technical id
   */
  private UUID              uuid;
  /**
   * Contains the application name.
   * <p>
   * It will be displayed in the navigation menu.
   * </p>
   */
  private String            name;
  /**
   * Contains the application description.
   * <p>
   * It will be displayed in the navigation menu.
   * </p>
   */
  private String            description;
  /**
   * Contains the application portal uri.
   * <p>
   * It will be used to display the application inside the portal.
   * </p>
   */
  private PortalURI         uri;
  /**
   * Contains the application icon.
   * <p>
   * It will be displayed in the navigation menu.
   * </p>
   */
  private Resource          icon;
  /**
   * Contains the application availability.
   * <p>
   * If this is true, the application will be available for the user.
   * </p>
   */
  private boolean           available        = true;
  /**
   * Contains the application status.
   */
  private ApplicationStatus status;

  /**
   * {@inheritDoc}
   * 
   * @see PortalApplicationImpl#id
   */
  @Override
  public String getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   * 
   * @see PortalApplicationImpl#id
   */
  @Override
  public void setId(final String pId)
  {
    id = pId;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUniqueId()
  {
    return uuid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUniqueId(final UUID pId)
  {
    uuid = pId;
  }

  /**
   * {@inheritDoc}
   * 
   * @see PortalApplicationImpl#name
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   * 
   * @see PortalApplicationImpl#name
   */
  @Override
  public void setName(final String pName)
  {
    name = pName;

  }

  /**
   * {@inheritDoc}
   * 
   * @see PortalApplicationImpl#description
   */
  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   * 
   * @see PortalApplicationImpl#description
   */
  @Override
  public void setDescription(final String pDescription)
  {
    description = pDescription;

  }

  /**
   * {@inheritDoc}
   * 
   * @see PortalApplicationImpl#uri
   */
  @Override
  public PortalURI getPortalURI()
  {

    return uri;
  }

  /**
   * {@inheritDoc}
   * 
   * @see PortalApplicationImpl#uri
   */
  @Override
  public void setPortalURI(final PortalURI pPortalURI)
  {
    uri = pPortalURI;
  }

  /**
   * {@inheritDoc}
   * 
   * @see PortalApplicationImpl#icon
   */
  @Override
  public Resource getIcon()
  {
    return icon;
  }

  /**
   * {@inheritDoc}
   * 
   * @see PortalApplicationImpl#icon
   */
  @Override
  public void setIcon(final Resource pIcon)
  {
    icon = pIcon;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ApplicationStatus getStatus()
  {
    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatus(final ApplicationStatus status)
  {
    this.status = status;
  }

  /**
   * {@inheritDoc}
   *
   * @see PortalApplicationImpl#available
   */
  @Override
  public boolean isAvailable()
  {
    return available;
  }

  /**
   * {@inheritDoc}
   *
   * @see PortalApplicationImpl#available
   */
  @Override
  public void setAvailability(final boolean pAvailability)
  {
    available = pAvailability;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(final PortalApplication pO)
  {
    return pO.getName().compareToIgnoreCase(name);
  }

}
