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
package org.novaforge.forge.core.organization.model;

import org.novaforge.forge.commons.technical.historization.annotations.Historizable;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.model.enumerations.UserStatus;

import java.util.Date;

/**
 * This is the behavior of a user into the forge
 * 
 * @author sbenoist
 * @author BILET-JC
 * @author Guillaume Lamirand
 */
public interface User extends Actor
{

  /**
   * Returns the actor's login
   * 
   * @return actor's login
   */
  @Historizable(label = "login")
  String getLogin();

  /**
   * Allows to set the login of the actor
   * 
   * @param pLogin
   *          the login to set
   */
  void setLogin(final String pLogin);

  /**
   * This method returns the password of the user
   * 
   * @return String
   */
  String getPassword();

  /**
   * This method allows to set the password of the user
   * 
   * @param password
   */
  void setPassword(String password);

  /**
   * This method returns the first name of the user
   * 
   * @return String
   */
  @Historizable(label = "firstname")
  String getFirstName();

  /**
   * This method allows to set the first name of the user
   * 
   * @param firstName
   */
  void setFirstName(String firstName);

  /**
   * This method returns the name of the user
   * 
   * @return String
   */
  @Historizable(label = "name")
  String getName();

  /**
   * This method allows to set the name of the user
   * 
   * @param name
   */
  void setName(String name);

  /**
   * This method returns the email of the user
   * 
   * @return String
   */
  @Historizable(label = "email")
  String getEmail();

  /**
   * This method allows to set the email of the user
   * 
   * @param email
   */
  void setEmail(String email);

  /**
   * This method returns the status of the user
   * 
   * @return UserStatus
   */
  UserStatus getStatus();

  /**
   * This method allows to set the status of the user
   * 
   * @param pStatus
   */
  void setStatus(UserStatus pStatus);

  /**
   * This method returns the language of the user
   * 
   * @return Language
   */
  @Historizable(label = "language")
  Language getLanguage();

  /**
   * This method allows to set the language of the user
   * 
   * @param language
   */
  void setLanguage(Language language);

  /**
   * This method returns the date of the last connection of the user
   * 
   * @return Date
   */
  Date getLastConnected();

  /**
   * This method returns the date of the last update of the password user
   * 
   * @return Date
   */
  Date getLastPasswordUpdated();

  /**
   * This method allows to set the lastPasswordUpdated date
   * 
   * @param pLastPasswordUpdated
   */
  void setLastPasswordUpdated(Date pLastPasswordUpdated);

  /**
   * This method returns the realm of the user
   * 
   * @return PluginRealm
   */
  RealmType getRealmType();

  /**
   * This method allow to set the realm type of the user
   * 
   * @param pRealmType
   */
  void setRealmType(RealmType pRealmType);
}
