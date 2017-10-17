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
package org.novaforge.forge.tools.managementmodule.ui.client.view.iteration;

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
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.DateBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.TextBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

/**
 * @author fdemange
 *
 */

public class IterationFormViewImpl extends Composite implements IterationFormView
{

	private static IterationFormViewImplUiBinder uiBinder = GWT.create(IterationFormViewImplUiBinder.class);
	@UiField
	Label 			iterationCreationTitle;
	@UiField
	Grid  			iterationGrid;
	@UiField
	Label          	libelleLabel;
	@UiField
	Label          	startDateLabel;
	@UiField
	Label          	endDateLabel;
	@UiField
	Label          	phaseLabel;
	@UiField
	Label          	lotLabel;
	@UiField
	TextBoxValidation			libelle;
	@UiField
	DateBoxValidation			startDateBox;
	@UiField
	DateBoxValidation			endDateBox;
	@UiField
	ListBox         phase;
	@UiField
	ListBox         lot;
	@UiField
	Button          cancelIterationButton;
	@UiField
	Button          createIterationButton;
	private ValidateDialogBox validateDialogBox;
	private ValidateDialogBox validateDialogBoxUpdate;

	public IterationFormViewImpl()
	{
		Common.getResource().css().ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));

		iterationCreationTitle.setText(formStyle(Common.MESSAGES_CHARGE_PLAN.createOneIterationTitle()));
		libelleLabel.setText(formStyle(Common.MESSAGES_CHARGE_PLAN.iterationLabel()));
		startDateLabel.setText(formStyle(Common.MESSAGES_CHARGE_PLAN.startDate()));
		endDateLabel.setText(formStyle(Common.MESSAGES_CHARGE_PLAN.endDate()));
		phaseLabel.setText(formStyle(Common.MESSAGES_CHARGE_PLAN.phase()));
		lotLabel.setText(formStyle(Common.MESSAGES_CHARGE_PLAN.lot()));

		startDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yyyy")));
		endDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd/MM/yyyy")));

		cancelIterationButton.setText(Common.MESSAGES_CHARGE_PLAN.returnButtonLabel());
		createIterationButton.setText(Common.MESSAGES_CHARGE_PLAN.iterationCreationButtonLabel());

		// Initialization of validation popup
		validateDialogBox = new ValidateDialogBox(Common.MESSAGES_CHARGE_PLAN.addValidationMessage());

		validateDialogBoxUpdate = new ValidateDialogBox(Common.MESSAGES_CHARGE_PLAN.updateValidationMessage());

		libelle.setFocus();

		// Initialization row style
		for (int row = 0; row < iterationGrid.getRowCount(); row++)
		{
			if (row % 2 == 0)
			{
				iterationGrid.getRowFormatter().addStyleName(row, Common.getResource().css().gridRowPair());
			}
			iterationGrid.getCellFormatter().addStyleName(row, 0, Common.getResource().css().labelCell());
		}
	}

	private String formStyle(String pText)
	{
		return pText + " : ";
	}

	@Override
	public TextBoxValidation getLibelleTB() {
		return libelle;
	}

	@Override
	public DateBoxValidation getStartDateDB()
	{
		return startDateBox;
	}

	@Override
	public DateBoxValidation getEndDateDB()
	{
		return endDateBox;
	}

	@Override
	public Button getCreateIterationButton() {
		return createIterationButton;
	}

	@Override
	public Button getCancelIterationButton() {
		return cancelIterationButton;
	}

	@Override
	public ValidateDialogBox getInfoDialogBox() {
		return validateDialogBox;
	}

	@Override
	public ListBox getPhaseLB()
	{
		return phase;
	}

	@Override
	public ValidateDialogBox getValidateDialogBox() {
		return validateDialogBox;
	}

	@Override
	public Label getIterationCreationTitle()
	{
		return iterationCreationTitle;
	}

	@Override
	public void setIterationCreationTitle(Label iterationCreationTitle)
	{
		this.iterationCreationTitle = iterationCreationTitle;
	}

	@Override
	public void setValidateDialogBox(ValidateDialogBox validateDialogBox) {
		this.validateDialogBox = validateDialogBox;
	}

	@Override
	public ValidateDialogBox getValidateDialogBoxUpdate() {
		return validateDialogBoxUpdate;
	}

	@Override
	public void setValidateDialogBoxUpdate(ValidateDialogBox validateDialogBoxUpdate) {
		this.validateDialogBoxUpdate = validateDialogBoxUpdate;
	}

	@Override
	public ListBox getLotLB()
	{
		return lot;
	}

	@Override
	public void setLotLB(ListBox lot)
	{
		this.lot = lot;
	}

	interface IterationFormViewImplUiBinder extends UiBinder<Widget, IterationFormViewImpl>
	{
	}
	
}
