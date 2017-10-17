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
package org.novaforge.forge.ui.portal.data.container;

import com.vaadin.ui.ComboBox;
import org.novaforge.forge.core.organization.model.UserProfile;

/**
 * @author caseryj
 */
public enum UserProfileItemProperty
{
  /**
   * Refere to item id in its container
   */
  ID
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "id";
    }
  },
  /**
   * Refere to {@link User#getLogin()}
   * 
   * @see User#getLogin()
   */
  LOGIN
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "login";
    }
  },
  /**
   * Refere to {@link User#getFirstname()}
   * 
   * @see User#getFirstname()
   */
  FIRSTNAME
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "firstName";
    }
  },
  /**
   * Refere to {@link User#getName()}
   * 
   * @see User#getName()
   */
  LASTNAME
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "lastName";
    }
  },
  /**
   * Refere to {@link User#getEmail()}
   * 
   * @see User#getEmail()
   */
  EMAIL
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "email";
    }
  },
  /**
   * Refere to {@link User#getLanguage()}
   * 
   * @see User#getLanguage()
   */
  LANGUAGE
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "language";
    }
  },
  /**
   * Refere to {@link User#getStatus()}
   * 
   * @see User#getStatus()
   */
  STATUS
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "status";
    }
  },
  /**
   * Refere to {@link User#getCreated()}
   * 
   * @see User#getCreated()
   */
  CREATED_DATE
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "created";
    }
  },
  /**
   * Refere to {@link User#getPassword()}
   * 
   * @see User#getPassword()
   */
  PASSWORD
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "password";
    }
  },
  /**
   * Refere to {@link User#getLastConnected()}
   * 
   * @see User#getLastConnected()
   */
  LAST_CONNECTED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "lastConnected";
    }
  },
  /**
   * Refere to {@link User#getLastPasswordUpdated()}
   * 
   * @see User#getLastPasswordUpdated()
   */
  LAST_PASSWORD_UPDATED
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "lastPasswordUpdated";
    }
  },
  /**
   * Refere to {@link User#getRealmType()}
   * 
   * @see User#getRealmType()
   */
  REALM_TYPE
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "realmType";
    }
  },
  /**
   * Refere to {@link UserProfile#getPicture()}
   * 
   * @see UserProfile#getPicture()
   */
  PICTURE
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "picture";
    }
  };
  /**
   * Get ItemPropertyId used by {@link ComboBox}
   * 
   * @return property id
   */
  public abstract String getPropertyId();
}
