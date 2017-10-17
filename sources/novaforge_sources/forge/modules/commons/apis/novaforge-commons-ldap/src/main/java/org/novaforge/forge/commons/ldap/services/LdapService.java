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

package org.novaforge.forge.commons.ldap.services;

import org.novaforge.forge.commons.ldap.exceptions.LdapException;
import org.novaforge.forge.commons.ldap.model.LdapUser;

public interface LdapService
{

   /**
    * do an authentification LDAP and return true if success
    * 
    * @param user
    * @param passwd
    * @return True if the authentification is success, else False
    * @throws LdapException
    */
   boolean authentification(String user, String passwd, String pDn) throws LdapException;

   /**
    * Get a user LDAP
    * 
    * @param name
    *           the name to find
    * @return the user User
    * @throws LdapException
    */
   LdapUser searchUser(String name) throws LdapException;

   /**
    * Get a user LDAP
    * 
    * @param name
    *           the user name
    * @param passwd
    *           the user passwd
    * @param dn
    *           the user dn
    * @return the user User
    * @throws LdapException
    */
   LdapUser getUser(final String pLogin, final String pPasswd) throws LdapException;

   /**
    * Get if we can check user in DB when LDAP is down
    * 
    * @return true/false
    */
   boolean getAuthorizeJpaAccessWhenLdapDown();

   /**
    * Return true if user can be created
    * 
    * @return true/false
    */
   boolean getAuthorizeCreateJpaAccess();

   /**
    * Return true if we can update language user
    * 
    * @return true/false
    */
   boolean getAuthorizeToUpdateLanguage();

}
