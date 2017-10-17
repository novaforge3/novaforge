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
package org.novaforge.forge.tools.managementmodule.ui.client.view.projectplan;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.tools.managementmodule.ui.client.ressources.ManagementModuleRessources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

/**
 * @author FALSQUELLE-E
 */
public class ProjectPlanTabViewImpl extends ResizeComposite implements
		ProjectPlanTabView {

	private static final String TWENTY_FIVE_PERCENT = "25%";

   private static final String STYLE_TABULATION_PANEL = "tabulationPanel";

	private static ProjectPlanTabViewImplUiBinder uiBinder = GWT
			.create(ProjectPlanTabViewImplUiBinder.class);

	private static ManagementModuleRessources ressources = GWT
			.create(ManagementModuleRessources.class);
	@UiField
	Grid topGrid;
	@UiField
	Label projectNameLabel;
	@UiField
	Label projectName;
	@UiField
   Label statutLabel;
   @UiField
   Label status;
	@UiField
	Label lastUpdateLabel;
	@UiField
	Label lastUpdate;
	@UiField
	Label versionLabel;
	@UiField
	Label version;
	@UiField
	Button buttonProjectPlanList;
	@UiField
	Button buttonValidate;
	@UiField
	Button buttonReferential;
	@UiField
   Button buttonHomeReturn;
	@UiField
	TabLayoutPanel tabLayoutPanel;

  final ValidateDialogBox  validateDialogBox  = new ValidateDialogBox(Common
      .getProjectPlanMessages().projectPlanValidationMessage()); 
  
	
	public ProjectPlanTabViewImpl() {
		ressources.css().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));

		projectNameLabel.setText(getLabelStyle(Common.getProjectPlanMessages()
				.projectName()));
		statutLabel.setText(getLabelStyle(Common.getProjectPlanMessages()
            .statusLabel()));
		lastUpdateLabel.setText(getLabelStyle(Common.getProjectPlanMessages()
				.lastUpdateDate()));
		versionLabel.setText(getLabelStyle(Common.getGlobal()
				.version()));
		buttonProjectPlanList.setText(Common.getProjectPlanMessages()
				.buttonProjectPlanList());
		buttonValidate
				.setText(Common.getGlobal().buttonValidate());
		buttonReferential.setText(Common.getProjectPlanMessages()
				.buttonReferential());
      buttonHomeReturn.setText(Common.GLOBAL.homeReturn());

		tabLayoutPanel.setAnimationDuration(1000);
		tabLayoutPanel.setStyleName(STYLE_TABULATION_PANEL);
		topGrid.setWidth("100%");
		topGrid.getCellFormatter().setWidth(0, 0, TWENTY_FIVE_PERCENT);
		topGrid.getCellFormatter().setWidth(0, 1, TWENTY_FIVE_PERCENT);
		topGrid.getCellFormatter().setWidth(0, 2, TWENTY_FIVE_PERCENT);
		topGrid.getCellFormatter().setWidth(0, 3, TWENTY_FIVE_PERCENT);
	}

	public static String getLabelStyle(final String source) {

		return source + " :";

	}

	@Override
	public Label getProjectNameLabel() {

		return projectNameLabel;
	}

	@Override
	public Label getProjectName() {
		return projectName;
	}

	@Override
	public Label getLastUpdateLabel() {
		return lastUpdateLabel;
	}

	@Override
	public Label getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public Label getVersionLabel() {
		return versionLabel;
	}

	@Override
	public Label getVersion() {
		return version;
	}

	@Override
	public Button getButtonProjectPlanList() {
		return buttonProjectPlanList;
	}

	@Override
	public Button getButtonValidate() {
		return buttonValidate;
	}

	@Override
	public Button getButtonReferential() {
		return buttonReferential;
	}

	@Override
	public TabLayoutPanel getTabPanel()
	{
		return tabLayoutPanel;
	}

   /**
    * Get the buttonHomeReturn
    * @return the buttonHomeReturn
    */
	@Override
   public Button getButtonHomeReturn() {
      return buttonHomeReturn;
   }

   /**
    * Get the status
    * @return the status
    */
	@Override
   public Label getStatus() {
      return status;
   }

	interface ProjectPlanTabViewImplUiBinder extends UiBinder<Widget, ProjectPlanTabViewImpl>
	{
	}

  /**
   * {@inheritDoc}
   */
   @Override
  public ValidateDialogBox getProjectPlanValidateDialogBox()
  {
    return validateDialogBox;
  }  
}
