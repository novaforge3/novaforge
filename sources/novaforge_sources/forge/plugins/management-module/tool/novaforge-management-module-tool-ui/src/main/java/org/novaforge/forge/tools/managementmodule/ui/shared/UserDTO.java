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
package org.novaforge.forge.tools.managementmodule.ui.shared;

import java.io.Serializable;

/**
 * This DTO representes a user of the application
 */
public class UserDTO implements Serializable {

   /** The UID for serialization */
   private static final long serialVersionUID = -2110717316055947072L;

   /** The user login (and identifier) */
   private String login;
   
   /** The user last name */
   private String lastName;
   
   /** The user first name */
   private String firstName;

   /**
    * Get the login
    * @return the login
    */
   public String getLogin() {
      return login;
   }

   /**
    * Set the login
    * @param login the login to set
    */
   public void setLogin(String login) {
      this.login = login;
   }

   /**
    * Get the lastName
    * @return the lastName
    */
   public String getLastName() {
      return lastName;
   }

   /**
    * Set the lastName
    * @param lastName the lastName to set
    */
   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   /**
    * Get the firstName
    * @return the firstName
    */
   public String getFirstName() {
      return firstName;
   }

   /**
    * Set the firstName
    * @param firstName the firstName to set
    */
   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((login == null) ? 0 : login.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      UserDTO other = (UserDTO) obj;
      if (login == null) {
         if (other.login != null) {
            return false;
         }
      } else if (!login.equals(other.login)) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "UserDTO [login=" + login + ", lastName=" + lastName + ", firstName=" + firstName + "]";
   }
   
}
