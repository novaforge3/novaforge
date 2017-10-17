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
package org.novaforge.forge.tools.managementmodule.ui.client.view.burndowniteration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.managementmodule.ui.client.ressources.ManagementModuleRessources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.CustomListBox;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;

/**
 * @author FALSQUELLE-E
 */
public class BurnDownIterationViewImpl extends ResizeComposite implements
		BurnDownIterationView {

	private static final String TWENTY_PERCENT = "20%";

   private static BurnDownIterationViewImplUiBinder uiBinder = GWT
			.create(BurnDownIterationViewImplUiBinder.class);

	private static ManagementModuleRessources ressources = GWT
			.create(ManagementModuleRessources.class);
	@UiField
	Grid infoGrid;
	@UiField
	Label iterationNameLabel;
	@UiField
	Label iterationName;
	@UiField
   Label lotLabel;
   @UiField
   Label lot;
   @UiField
   Label parentLotLabel;
	@UiField
	Label parentLot;
	@UiField
	Label startDateLabel;
	@UiField
	Label startDate;
	@UiField
	Label endDateLabel;
	@UiField
   Label endDate;
   @UiField
   Label iterationLBLabel;
   @UiField
   CustomListBox<IterationDTO> iterationLB;
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
	public BurnDownIterationViewImpl() {
		ressources.css().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));

		iterationNameLabel.setText(getLabelStyle(Common.GLOBAL.iteration()));
	   lotLabel.setText(getLabelStyle(Common.GLOBAL.lot()));
	   parentLotLabel.setText(getLabelStyle(Common.GLOBAL.parentLot()));
	   startDateLabel.setText(getLabelStyle(Common.MESSAGES_PROJECT_PLAN.startDate()));
	   endDateLabel.setText(getLabelStyle(Common.MESSAGES_PROJECT_PLAN.endDate()));
	   disciplineLabel.setText(getLabelStyle(Common.MESSAGES_BACKLOG.discipline()));

	   disciplineLB.setWidth("200px");

	   iterationLBLabel.setText(getLabelStyle(Common.MESSAGES_BACKLOG.iterations()));

      iterationLB.setWidth("200px");

		buttonGenerateDiagram.setText(Common.GLOBAL.buttonApply());
		buttonHomeReturn.setText(Common.GLOBAL.homeReturn());

		infoGrid.getCellFormatter().setWidth(0, 0, TWENTY_PERCENT);
		infoGrid.getCellFormatter().setWidth(0, 1, TWENTY_PERCENT);
		infoGrid.getCellFormatter().setWidth(0, 2, TWENTY_PERCENT);
      infoGrid.getCellFormatter().setWidth(0, 3, TWENTY_PERCENT);
      infoGrid.getCellFormatter().setWidth(0, 4, TWENTY_PERCENT);
	}

	public static String getLabelStyle(final String source) {
		return source + " :";

	}

	@Override
   public Label getIterationName() {
      return iterationName;
   }

	@Override
   public Label getLot() {
      return lot;
   }

	@Override
   public Label getParentLot() {
      return parentLot;
   }

	@Override
   public Label getStartDate() {
      return startDate;
   }

	@Override
   public Label getEndDate() {
      return endDate;
   }

	@Override
	public CustomListBox<DisciplineDTO> getDisciplineLB()
	{
		return disciplineLB;
	}
	
	@Override
   public CustomListBox<IterationDTO> getIterationLB() {
      return iterationLB;
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

	interface BurnDownIterationViewImplUiBinder extends UiBinder<Widget, BurnDownIterationViewImpl>
	{
	}
}
