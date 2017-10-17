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

package org.novaforge.forge.commons.ldap.model;


/**
 * This is the behavior of a user into the forge
 * 
 * @author petrettf
 */
public interface LdapUser
{
   /**
    * This method returns the dn of the user
    * 
    * @return String
    */
   String getDn();

   /**
    * This method allows to set the dn of the user
    * 
    * @param dn
    */
   void setDn(String dn);

   /**
    * This method returns the uid of the user
    * 
    * @return String
    */
   String getUid();

   /**
    * This method allows to set the uid of the user
    * 
    * @param uid
    */
   void setUid(String uid);

   /**
    * This method returns the userPassword of the user
    * 
    * @return String
    */
   String getUserPassword();

   /**
    * This method allows to set the userPassword of the user
    * 
    * @param userPassword
    */
   void setUserPassword(String userPassword);

   /**
    * This method returns the userPasswordEncodage of the user
    * 
    * @return String
    */
   String getUserPasswordEncodage();

   /**
    * This method allows to set the userPasswordEncodage of the user
    * 
    * @param userPasswordEncodage
    */
   void setUserPasswordEncodage(String userPasswordEncodage);

   /**
    * This method returns the surName of the user
    * 
    * @return String
    */
   String getSurName();

   /**
    * This method allows to set the surName of the user
    * 
    * @param surName
    */
   void setSurName(String surName);

   /**
    * This method returns the name of the user
    * 
    * @return String
    */
   String getGivenName();

   /**
    * This method allows to set the name of the user
    * 
    * @param name
    */
   void setGivenName(String givenName);

   /**
    * This method returns the mail of the user
    * 
    * @return String
    */
   String getMail();

   /**
    * This method allows to set the mail of the user
    * 
    * @param mail
    */
   void setMail(String mail);

   /**
    * This method returns the preferred language of the user
    * 
    * @return preferredLanguage
    */
   String getPreferredLanguage();

   /**
    * This method allows to set the language of the user
    * 
    * @param language
    */
   void setPreferredLanguage(String preferredLanguage);

}
