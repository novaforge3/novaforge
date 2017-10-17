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
package org.novaforge.forge.ui.portal.event;

import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.portal.events.PortalEvent;

import java.util.Locale;

/**
 * This event is used when a {@link User} has been updated
 * 
 * @author Guillaume Lamirand
 */
public class UserUpdateEvent implements PortalEvent
{
  /**
   * Contains the user login
   */
  private final String  login;
  /**
   * Contains the user name
   */
  private final String  name;
  /**
   * Contains the user firstname
   */
  private final String  firstname;
  /**
   * Contains the user locale
   */
  private final Locale  locale;

  private final boolean picture;

  /**
   * Default constructor.
   * 
   * @param pLogin
   *          the user login which has been updated
   * @param pName
   *          the new user name
   * @param pFirstName
   *          the new user firstname
   * @param pLocale
   *          the new user locale
   */
  public UserUpdateEvent(final String pLogin, final String pName, final String pFirstName,
      final Locale pLocale, final boolean pPicture)
  {
    login = pLogin;
    name = pName;
    firstname = pFirstName;
    locale = pLocale;
    picture = pPicture;
  }

  /**
   * Return the user login associated
   * 
   * @return the user login associated
   */
  public String getLogin()
  {
    return login;
  }

  /**
   * Return the user name associated
   * 
   * @return the user name associated
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the user firstname associated
   * 
   * @return the user firstname associated
   */
  public String getFirstName()
  {
    return firstname;
  }

  /**
   * Return the user locale associated
   * 
   * @return the user locale associated
   */
  public Locale getLocale()
  {
    return locale;
  }

  /**
   * Return the user picture associated
   * 
   * @return the user picture associated
   */
  public boolean getPicture()
  {
    return picture;
  }

}
