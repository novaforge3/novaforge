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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

/**
 * @author BILET-JC
 *
 */
public class HomeViewImpl extends Composite implements HomeView {

   private static HomeViewImplUiBinder uiBinder = GWT.create(HomeViewImplUiBinder.class);
	@UiField
	Label adminTitle;
	@UiField
	Label projectPlanTitle;
	@UiField
	Label pilotageExecutionTitle;
	@UiField
	Anchor initialAdminLink;
	@UiField
   Anchor projectPlanAdminLink;
	@UiField
	Panel pilotageExecutionPanel;
	@UiField
	Anchor projectPlanListLink;
	@UiField
	Anchor projectPlanLotLink;
	@UiField
	Anchor projectPlanIterationLink;
	@UiField
	Anchor projectPlanMarkerLink;
	@UiField
	Anchor projectPlanScopeLink;
	@UiField
	Anchor projectPlanEstimationLink;
	@UiField
   Anchor projectPlanChargePlanLink;
	@UiField
	Anchor pilotageExecutionListLink;
	@UiField
	Anchor pilotageExecutionScopeUnitDisciplineLink;
	@UiField
	Anchor pilotageExecutionPreparationLink;
	@UiField
	Anchor pilotageExecutionReportGlobalLink;
	@UiField
	Anchor pilotageExecutionRefItLink;
	@UiField
	Anchor pilotageExecutionReportItLink;
	@UiField
	Anchor pilotageExecutionBurnDownLink;
	@UiField
	Anchor pilotageExecutionReportDrawLink;

   public HomeViewImpl()
   {
    Common.RESOURCE.css().ensureInjected();
    initWidget(uiBinder.createAndBindUi(this));
    adminTitle.setText(Common.HOME.administration());
    projectPlanTitle.setText(Common.HOME.projectPlan());
    pilotageExecutionTitle.setText(Common.HOME.refIteration());
    initialAdminLink.setText(Common.HOME.initialSettings());
    projectPlanAdminLink.setText(Common.HOME.projectPlanSettings());
    //TODO enable iteration panel
    projectPlanListLink.setText(Common.HOME.projectPlanList());
    projectPlanLotLink.setText(Common.HOME.lot());
    projectPlanIterationLink.setText(Common.HOME.iteration());
    projectPlanMarkerLink.setText(Common.HOME.marker());
    projectPlanScopeLink.setText(Common.HOME.scope());
    projectPlanEstimationLink.setText(Common.HOME.estimation());
    projectPlanChargePlanLink.setText(Common.HOME.chargePlan());
    pilotageExecutionListLink.setText(Common.HOME.backlogList());
    pilotageExecutionScopeUnitDisciplineLink.setText(Common.HOME.scopeUnitDiscipline());
    pilotageExecutionPreparationLink.setText(Common.HOME.preparation());
    pilotageExecutionReportGlobalLink.setText(Common.HOME.globalMonitoring());
    pilotageExecutionRefItLink.setText(Common.HOME.refIt());
    pilotageExecutionReportItLink.setText(Common.HOME.reportIt());
    pilotageExecutionBurnDownLink.setText(Common.HOME.burndown());
    pilotageExecutionReportDrawLink.setText(Common.HOME.reportDraw());
	}
	
   /**
    * Get the projectPlanListLink
    * @return the projectPlanListLink
    */
   @Override
   public Anchor getProjectPlanListLink() {
      return projectPlanListLink;
   }

   /**
    * Get the projectPlanLotLink
    * @return the projectPlanLotLink
    */
   @Override
   public Anchor getProjectPlanLotLink() {
      return projectPlanLotLink;
   }

   /**
    * Get the projectPlanIterationLink
    * @return the projectPlanIterationLink
    */
   @Override
   public Anchor getProjectPlanIterationLink() {
      return projectPlanIterationLink;
   }

   /**
    * Get the projectPlanMarkerLink
    * @return the projectPlanMarkerLink
    */
   @Override
   public Anchor getProjectPlanMarkerLink() {
      return projectPlanMarkerLink;
   }

   /**
    * Get the projectPlanScopeLink
    * @return the projectPlanScopeLink
    */
   @Override
   public Anchor getProjectPlanScopeLink() {
      return projectPlanScopeLink;
   }

   /**
    * Get the projectPlanEstimationLink
    * @return the projectPlanEstimationLink
    */
   @Override
   public Anchor getProjectPlanEstimationLink() {
      return projectPlanEstimationLink;
   }

   /**
    * Get the pilotageExecutionListLink
    * @return the pilotageExecutionListLink
    */
   @Override
   public Anchor getPilotageExecutionListLink() {
      return pilotageExecutionListLink;
   }

   /**
    * Get the pilotageExecutionScopeUnitDisciplineLink
    * @return the pilotageExecutionScopeUnitDisciplineLink
    */
   @Override
   public Anchor getPilotageExecutionScopeUnitDisciplineLink() {
      return pilotageExecutionScopeUnitDisciplineLink;
   }

   /**
    * Get the pilotageExecutionPreparationLink
    * @return the pilotageExecutionPreparationLink
    */
   @Override
   public Anchor getPilotageExecutionPreparationLink() {
      return pilotageExecutionPreparationLink;
   }

   /**
    * Get the pilotageExecutionReportGlobalLink
    * @return the pilotageExecutionReportGlobalLink
    */
   @Override
   public Anchor getPilotageExecutionReportGlobalLink() {
      return pilotageExecutionReportGlobalLink;
   }

   /**
    * Get the pilotageExecutionRefItLink
    * @return the pilotageExecutionRefItLink
    */
   @Override
   public Anchor getPilotageExecutionRefItLink() {
      return pilotageExecutionRefItLink;
   }

   /**
    * Get the pilotageExecutionReportItLink
    * @return the pilotageExecutionReportItLink
    */
   @Override
   public Anchor getPilotageExecutionReportItLink() {
      return pilotageExecutionReportItLink;
   }

   /**
    * Get the pilotageExecutionBurnDownLink
    * @return the pilotageExecutionBurnDownLink
    */
   @Override
   public Anchor getPilotageExecutionBurnDownLink() {
      return pilotageExecutionBurnDownLink;
   }

   /**
    * Get the pilotageExecutionReportDrawLink
    * @return the pilotageExecutionReportDrawLink
    */
   @Override
   public Anchor getPilotageExecutionReportDrawLink() {
      return pilotageExecutionReportDrawLink;
   }

   /**
    * Get the projectPlanChargePlanLink
    * @return the projectPlanChargePlanLink
    */
   @Override
   public Anchor getProjectPlanChargePlanLink() {
      return projectPlanChargePlanLink;
   }

   /**
    * Get the initialAdminLink
    * @return the initialAdminLink
    */
   @Override
   public Anchor getInitialAdminLink() {
      return initialAdminLink;
   }

   /**
    * Get the projectPlanAdminLink
    * @return the projectPlanAdminLink
    */
   @Override
   public Anchor getProjectPlanAdminLink() {
      return projectPlanAdminLink;
   }

   @Override
   public void disableLinks()
   {
      pilotageExecutionPanel.addStyleName(Common.RESOURCE.css().disabledPanel());

      pilotageExecutionListLink.setStyleName(Common.RESOURCE.css().disabledLink());
      pilotageExecutionScopeUnitDisciplineLink.setStyleName(Common.RESOURCE.css().disabledLink());
      pilotageExecutionPreparationLink.setStyleName(Common.RESOURCE.css().disabledLink());
      pilotageExecutionReportGlobalLink.setStyleName(Common.RESOURCE.css().disabledLink());
      pilotageExecutionRefItLink.setStyleName(Common.RESOURCE.css().disabledLink());
      pilotageExecutionReportItLink.setStyleName(Common.RESOURCE.css().disabledLink());
      pilotageExecutionBurnDownLink.setStyleName(Common.RESOURCE.css().disabledLink());
      pilotageExecutionReportDrawLink.setStyleName(Common.RESOURCE.css().disabledLink());
   }

   interface HomeViewImplUiBinder extends UiBinder<Widget, HomeViewImpl>
   {
   }
}
