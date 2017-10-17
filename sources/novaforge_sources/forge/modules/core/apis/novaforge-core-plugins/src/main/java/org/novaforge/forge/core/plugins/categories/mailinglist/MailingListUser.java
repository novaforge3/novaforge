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
package org.novaforge.forge.core.plugins.categories.mailinglist;


/**
 * @author sbenoist
 */
public interface MailingListUser extends MailingListSubscriber
{
  /**
   * This method gets the external property of the mailinglist user, ie if the user belongs to the
   * forge
   * 
   * @return boolean
   */
  boolean isExternal();

  /**
   * This method sets the external property of the mailinglist user, ie if the user belongs to the
   * forge
   * 
   * @param pExternal
   */
  void setExternal(boolean pExternal);

  /**
   * This method gets the login of the mailinglist user for an internal user
   * 
   * @return String
   */
  String getLogin();

  /**
   * This method sets the login of the mailinglist user for an internal user
   * 
   * @param pLogin
   */
  void setLogin(String pLogin);

  /**
   * This method sets the email of the mailinglist user for an external user
   * 
   * @return String
   */
  String getEmail();

  /**
   * This method sets the email of the mailinglist user for an external user
   * 
   * @param pEmail
   */
  void setEmail(String pEmail);

}
