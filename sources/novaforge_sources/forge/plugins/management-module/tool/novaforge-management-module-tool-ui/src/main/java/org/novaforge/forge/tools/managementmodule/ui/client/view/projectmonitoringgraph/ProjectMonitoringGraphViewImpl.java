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
package org.novaforge.forge.tools.managementmodule.ui.client.view.projectmonitoringgraph;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.managementmodule.ui.client.ressources.ManagementModuleRessources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.CustomListBox;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;

/**
 * @author FALSQUELLE-E
 */
public class ProjectMonitoringGraphViewImpl extends ResizeComposite implements ProjectMonitoringGraphView {

   private static ProjectMonitoringGraphViewImplUiBinder uiBinder = GWT
         .create(ProjectMonitoringGraphViewImplUiBinder.class);

   private static ManagementModuleRessources ressources = GWT.create(ManagementModuleRessources.class);
   @UiField
   Label lotLabel;
   @UiField
   CustomListBox<LotDTO> lotLB;
   @UiField
   Label subLotLabel;
   @UiField
   CustomListBox<LotDTO> subLotLB;
   @UiField
   Label startDateLabel;
   @UiField
   Label startDate;
   @UiField
   Label disciplineLabel;
   @UiField
   CustomListBox<DisciplineDTO> disciplineLB;
   @UiField
   Button buttonGenerateDiagram;
   @UiField
   Button buttonHomeReturn;
   @UiField
   VerticalPanel diagramsPanel;
   public ProjectMonitoringGraphViewImpl() {
      ressources.css().ensureInjected();
      initWidget(uiBinder.createAndBindUi(this));

      lotLabel.setText(getLabelStyle(Common.GLOBAL.lot()));
      subLotLabel.setText(getLabelStyle(Common.GLOBAL.subLot()));
      startDateLabel.setText(getLabelStyle(Common.MESSAGES_PROJECT_PLAN.projectStartDate()));
      disciplineLabel.setText(getLabelStyle(Common.MESSAGES_BACKLOG.discipline()));

      lotLB.setWidth("200px");
      subLotLB.setWidth("200px");
      disciplineLB.setWidth("200px");

      buttonGenerateDiagram.setText(Common.GLOBAL.buttonApply());
      buttonHomeReturn.setText(Common.GLOBAL.homeReturn());

   }

   public static String getLabelStyle(String source) {

      return source + " :";
   }

   @Override
   public CustomListBox<LotDTO> getLotLB() {
      return lotLB;
   }

   @Override
   public CustomListBox<LotDTO> getSubLotLB() {
      return subLotLB;
   }

   @Override
   public Label getStartDate() {
      return startDate;
   }

   @Override
   public CustomListBox<DisciplineDTO> getDisciplineLB() {
      return disciplineLB;
   }

   @Override
   public Button getButtonGenerateDiagram() {
      return buttonGenerateDiagram;
   }

   @Override
   public VerticalPanel getDiagramsPanel() {
      return diagramsPanel;
   }

   @Override
   public Button getButtonHomeReturn() {
      return buttonHomeReturn;
   }

   interface ProjectMonitoringGraphViewImplUiBinder extends UiBinder<Widget, ProjectMonitoringGraphViewImpl>
   {
   }

}
