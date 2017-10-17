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

package org.novaforge.forge.plugins.testmanager.testlink.datamapper;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 26 juil. 2011
 */
public enum TestlinkMethod
{

   CREATE_USER("tl.createUser"), UPDATE_USER("tl.updateUser"), GET_USER("tl.getUser"), CREATE_PROJECT(
         "tl.createProject"), PROJECT_ADD_USER("tl.projectAddUser"), DELETE_USER("tl.deleteUser"), UPDATE_PROJECT(
               "tl.updateProject"), DELETE_PROJECT("tl.deleteProject"), GET_ROLES("tl.getRoles"), GET_ROLE(
                     "tl.getRole"), PROJECT_REMOVE_USER("tl.projectRemoveUser"), PROJECT_BY_ID("tl.getProjectById"), CREATE_ADMIN(
                           "tl.createAdminUser"), REQUIREMENTS_IMPORT_FROM_XML("tl.requirementsImportFromXML"), GET_REQUIREMENTS_BY_PROJECT(
                                 "tl.getRequirementsByProject"), GET_TESTS_BY_REQUIREMENT("tl.getTestsByRequirement"), UPDATE_DIRECTORY(
         "tl.updateDirectory"), UPDATE_REQUIREMENT("tl.updateRequirement"), DELETE_REQUIREMENT(
         "tl.deleteRequirement")
                                       ;

   private final String value;

   /**
    * 
    */
   TestlinkMethod(final String value)
   {
      this.value = value;
   }

   @Override
   public String toString()
   {
      return this.value;
   }

}
