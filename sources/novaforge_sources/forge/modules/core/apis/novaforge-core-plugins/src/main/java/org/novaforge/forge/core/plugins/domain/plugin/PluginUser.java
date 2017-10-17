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
package org.novaforge.forge.core.plugins.domain.plugin;

import java.io.Serializable;

/**
 * This interface describe a user object used during communication between forge core and a plugin
 * 
 * @author lamirang
 */
public interface PluginUser extends Serializable
{

  /**
   * Return user login
   * 
   * @return the login
   */
  String getLogin();

  /**
   * That allows to set login
   * 
   * @param pLogin
   *          the login to set
   */
  void setLogin(final String pLogin);

  /**
   * Return user password
   * 
   * @return the password
   */
  String getPassword();

  /**
   * That allows to set user password
   * 
   * @param pPassword
   *          the password to set
   */
  void setPassword(final String pPassword);

  /**
   * Return user firstname
   * 
   * @return the firstName
   */
  String getFirstName();

  /**
   * That allows to set firstname
   * 
   * @param pFirstName
   *          the firstName to set
   */
  void setFirstName(final String pFirstName);

  /**
   * Return the user lastname
   * 
   * @return the name
   */
  String getName();

  /**
   * That allows to set user lastname
   * 
   * @param pName
   *          the name to set
   */
  void setName(final String pName);

  /**
   * Return user's email
   * 
   * @return the email
   */
  String getEmail();

  /**
   * That allows to set user email
   * 
   * @param pEmail
   *          the email to set
   */
  void setEmail(final String pEmail);

  /**
   * Return user language preference
   * 
   * @return the language
   */
  String getLanguage();

  /**
   * That allows to set user language preference
   * 
   * @param pLanguage
   *          the language to set
   */
  void setLanguage(final String pLanguage);

}