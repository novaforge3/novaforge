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
package org.novaforge.forge.tools.managementmodule.ui.client;

import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeRightsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;

import java.util.Set;

/**
 * Contains all session datas.
 * In GWT static fields when declared in client part are only for one session
 */
public class SessionData {

   
   /**  the project id   */
   public static String projectId;
   
   /**  the user login   */
   public static String userLogin;

   /**  the plan project id */
   public static Long currentValidatedProjectPlanId;
   
   /**  the disciplines the connected user can look   */
   public static Set<DisciplineDTO> disciplinesOfConnectedUser;
   
   /**  the applicative part the connected user can access  */
   public static Set<ApplicativeRightsDTO> applicativesRightsOfConnectedUser;
   
   /**
    * private constructor
    */
   private SessionData()
   {
   }
   
   /**
    * This method returns the AccessRight on an applicative function for the connected user
    * @param applicativeFunction the applicativeFunction
    * @return the appropriate AccessRight
    */
   public static AccessRight getAccessRight(ApplicativeFunction applicativeFunction){
      AccessRight accessRight = AccessRight.NONE;
      for(final ApplicativeRightsDTO applicativeRightsDTO : applicativesRightsOfConnectedUser) {
         if(applicativeRightsDTO.getApplicativeFunction().equals(applicativeFunction)) {
            accessRight = applicativeRightsDTO.getAccessRights();
            break;
         }
      }
      return accessRight;
   }
}
