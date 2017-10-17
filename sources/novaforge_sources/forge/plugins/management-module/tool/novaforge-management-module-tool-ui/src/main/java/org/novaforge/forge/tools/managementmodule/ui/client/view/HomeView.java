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
package org.novaforge.forge.tools.managementmodule.ui.client.view;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author BILET-JC
 * 
 */
public interface HomeView extends IsWidget {

   /**
    * Get the projectPlanListLink
    * 
    * @return the projectPlanListLink
    */
   Anchor getProjectPlanListLink();

   /**
    * Get the projectPlanLotLink
    * 
    * @return the projectPlanLotLink
    */
   Anchor getProjectPlanLotLink();

   /**
    * Get the projectPlanIterationLink
    * 
    * @return the projectPlanIterationLink
    */
   Anchor getProjectPlanIterationLink();

   /**
    * Get the projectPlanMarkerLink
    * 
    * @return the projectPlanMarkerLink
    */
   Anchor getProjectPlanMarkerLink();

   /**
    * Get the projectPlanScopeLink
    * 
    * @return the projectPlanScopeLink
    */
   Anchor getProjectPlanScopeLink();

   /**
    * Get the projectPlanEstimationLink
    * 
    * @return the projectPlanEstimationLink
    */
   Anchor getProjectPlanEstimationLink();

   /**
    * Get the pilotageExecutionListLink
    * 
    * @return the pilotageExecutionListLink
    */
   Anchor getPilotageExecutionListLink();

   /**
    * Get the pilotageExecutionScopeUnitDisciplineLink
    * 
    * @return the pilotageExecutionScopeUnitDisciplineLink
    */
   Anchor getPilotageExecutionScopeUnitDisciplineLink();

   /**
    * Get the pilotageExecutionPreparationLink
    * 
    * @return the pilotageExecutionPreparationLink
    */
   Anchor getPilotageExecutionPreparationLink();

   /**
    * Get the pilotageExecutionReportGlobalLink
    * 
    * @return the pilotageExecutionReportGlobalLink
    */
   Anchor getPilotageExecutionReportGlobalLink();

   /**
    * Get the pilotageExecutionRefItLink
    * 
    * @return the pilotageExecutionRefItLink
    */
   Anchor getPilotageExecutionRefItLink();

   /**
    * Get the pilotageExecutionReportItLink
    * 
    * @return the pilotageExecutionReportItLink
    */
   Anchor getPilotageExecutionReportItLink();

   /**
    * Get the pilotageExecutionBurnDownLink
    * 
    * @return the pilotageExecutionBurnDownLink
    */
   Anchor getPilotageExecutionBurnDownLink();

   /**
    * Get the pilotageExecutionReportDrawLink
    * 
    * @return the pilotageExecutionReportDrawLink
    */
   Anchor getPilotageExecutionReportDrawLink();
   
   /**
    * Get the projectPlanChargePlanLink
    * @return the projectPlanChargePlanLink
    */
   Anchor getProjectPlanChargePlanLink();
   
   /**
    * Get the initialAdminLink
    * @return the initialAdminLink
    */
   Anchor getInitialAdminLink();

   /**
    * Get the projectPlanAdminLink
    * @return the projectPlanAdminLink
    */
   Anchor getProjectPlanAdminLink();

   /**
    * Change the style of the link
    * @param pDisable true if disable
    */
   void disableLinks();
}
