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

package org.novaforge.forge.tools.managementmodule.ui.client.properties;

import com.google.gwt.i18n.client.Messages;

/**
 * @author FALSQUELLE-E
 */
public interface ProjectPlanMessage extends Messages {

   String buttonReferential();

   String buttonParameters();

   String projectPlanListTitle();

   String emptyProjectPlanList();

   String emptyLotFilterMessage();

   String lotName();

   String startDate();
   
   String projectStartDate();

   String endDate();

   String lotListTitle();

   String lotResultTitle();

   String lotFormTitleCreation();
   
   String lotFormTitleModification();
   
   String buttonAddLot();

   String lotParentList();

   String projectName();

   String buttonProjectPlanList();

   String addValidationCreateLotMessage();

   String lotErrorValidation();
   
   String iterationErrorValidation();

   String confirmDeleteLotMessage();

   String validateRAZProjectPlan();

   String successLotCreation();

   String headerLotTab();

   String headerMarkerTab();

   String headerChargePlanTab();

   String headerIterationTab();
   
   String headerScopeTab();

   String projectPlanValidated();

   String createLotSuccessed();
   
   String createIterationSuccessed();

   String createMarkerSuccessed();

   String saveLotSuccessed();

   String saveMarkerSuccessed();

   String lastUpdateDate();

   String markerListTitle();

   String markerResultTitle();

   String markerCreationTitle();

   String addValidationCreateMarkerMessage();

   String confirmDeleteMarkerMessage();

   String markerErrorValidation();

   String chargePlanTitle();

   /**
    * Get the message for the button return to home
    * @return the appropriate message
    */
   String buttonHomeReturn();

   /**
    * Get the message for the label displaying the status of the project plan
    * @return the appropriate message
    */
   String statusLabel();

   
   /**
    * Get the message to display in project plan settings tab
    * @return the appropriate message
    */
   String headerProjectPlanSettingsTab();
   
   /**
    * get project plan validation message
    * @return
    */
   String projectPlanValidationMessage();
}
