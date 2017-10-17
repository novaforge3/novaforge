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
package org.novaforge.forge.tools.managementmodule.ui.client.view.lotsettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.ProjectPlanMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.ressources.ManagementModuleRessources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.DateBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.TextAreaValidation;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.TextBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

public class LotSettingsCreationViewImpl extends Composite implements LotSettingsCreationView {
	   private static LotSettingsCreationViewImplUiBinder uiBinder        = GWT.create(LotSettingsCreationViewImplUiBinder.class);
	   private static ManagementModuleRessources        ressources      = GWT.create(ManagementModuleRessources.class);
	private final ProjectPlanMessage projectPlanMessages = (ProjectPlanMessage) GWT.create(ProjectPlanMessage.class);
	private final ValidateDialogBox validateDialogBox;
	@UiField
	   Button			buttonAddLot;
	   @UiField
	   Button			buttonCancelLot;
	   @UiField
	   Button			buttonSaveLot;
	   @UiField
	   Label			lotNameLabel;
	   @UiField
	   Label			lotStartDateLabel;
	   @UiField
	   Label			lotEndDateLabel;
	   @UiField
	   ListBox			lotParentList;
	   @UiField
	   Label			lotDescLabel;
	   @UiField
	   Label           	lotFormTitle;
	   @UiField
	   Label			lotParentListLabel;
	   @UiField
	   Grid   			lotGrid;
	   @UiField
	   DateBoxValidation      	lotStartDatePicker;
	   @UiField
	   DateBoxValidation       	lotEndDatePicker;
	   @UiField
	   TextBoxValidation	 	lotNameValidator;
	   @UiField
	   TextAreaValidation	 	lotDescValidator;
	   
	   public LotSettingsCreationViewImpl() {
		   // Generate ui
		   initWidget(uiBinder.createAndBindUi(this));
		   buttonAddLot.setText(Common.getProjectPlanMessages().buttonAddLot());
		   buttonSaveLot.setText(Common.getGlobal().buttonSave());
		   buttonCancelLot.setText(Common.getGlobal().buttonCancel());
		   lotNameLabel.setText(Common.getProjectPlanMessages().lotName());
		   lotStartDateLabel.setText(Common.getProjectPlanMessages().startDate());
		   lotEndDateLabel.setText(Common.getProjectPlanMessages().endDate());
		   lotDescLabel.setText(Common.getGlobal().description());
		   lotFormTitle.setText(Common.getProjectPlanMessages().lotFormTitleCreation());
		   lotParentListLabel.setText(Common.getProjectPlanMessages().lotParentList());

		   lotDescValidator.getElement().setAttribute("maxlength", "2000");
		   lotDescValidator.getTextArea().setCharacterWidth(60);

		   lotStartDatePicker.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yyyy")));
		   lotEndDatePicker.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yyyy")));

	      for (int row = 0; row < lotGrid.getRowCount(); row++)
	      {
	         if (row % 2 == 0)
	         {
	        	 lotGrid.getRowFormatter().addStyleName(row, ressources.css().gridRowPair());
	         }
	         lotGrid.getCellFormatter().addStyleName(row, 0, ressources.css().labelCell());
	      }

	      validateDialogBox = new ValidateDialogBox(projectPlanMessages.addValidationCreateLotMessage());
	   }
	   
	   @Override
		public Label getLotNameLabel() {
			return lotNameLabel;
		}
	   
	   @Override
		public Label getLotStartDateLabel() {
			return lotStartDateLabel;
		}
	   
	   @Override
		public Label getLotEndDateLabel() {
			return lotEndDateLabel;
		}
	   
	   @Override
		public ListBox getLotParentList() {
			return lotParentList;
		}

	@Override
	public Button getButtonAddLot()
	{
		return buttonAddLot;
	}

	@Override
		 public Button getButtonCancelLot()
		 {
			 return buttonCancelLot;
		 }

	@Override
	public Button getButtonSaveLot()
	{
		return buttonSaveLot;
	}

	@Override
		public DateBoxValidation getLotStartDatePicker() {
			return lotStartDatePicker;
		}
	   
	   @Override
		public DateBoxValidation getLotEndDatePicker() {
			return lotEndDatePicker;
		}
	   
	   @Override
		public TextBoxValidation getLotNameValidator() {
			return lotNameValidator;
		}

	@Override
		public TextAreaValidation getLotDescValidator() {
			return lotDescValidator;
		}
	   
	   @Override
	   public ValidateDialogBox getValidateDialogBox() {
		return validateDialogBox;
	}

	@Override
	public void setLotFormTitleCreation()
	{
		lotFormTitle.setText(Common.getProjectPlanMessages().lotFormTitleCreation());
	}

	@Override
	public void setLotFormTitleModification()
	{
		lotFormTitle.setText(Common.getProjectPlanMessages().lotFormTitleModification());
	}

	interface LotSettingsCreationViewImplUiBinder extends UiBinder<Widget, LotSettingsCreationViewImpl>
	{
	}
}
