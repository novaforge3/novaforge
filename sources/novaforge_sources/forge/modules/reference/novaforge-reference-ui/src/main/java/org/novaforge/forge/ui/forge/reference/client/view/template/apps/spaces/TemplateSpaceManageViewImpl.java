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
package org.novaforge.forge.ui.forge.reference.client.view.template.apps.spaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.ui.forge.reference.client.properties.GlobalMessage;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;

/**
 * @author lamirang
 */
public class TemplateSpaceManageViewImpl extends Composite implements TemplateSpaceManageView
{
	private static ProjectEditViewImplUiBinder uiBinder   = GWT.create(ProjectEditViewImplUiBinder.class);
	private static ReferenceResources          ressources = GWT.create(ReferenceResources.class);
	private final ProjectMessage               projectMessages = (ProjectMessage) GWT
	                                                               .create(ProjectMessage.class);
	private final GlobalMessage                globalMessages  = (GlobalMessage) GWT
	                                                               .create(GlobalMessage.class);
	@UiField
	Grid                      spaceGrid;
	@UiField
	Label                     spaceAddTitle;
	@UiField
	Label                     spaceNameLabel;
	@UiField
	TextBox                   spaceName;
	@UiField
	Button                    buttonSaveGroup;
	@UiField
	Button                    buttonCancelGroup;
	private ValidateDialogBox validateDialogBox;
	public TemplateSpaceManageViewImpl(final ManagePresenterType pType)
	{
		ressources.style().ensureInjected();

		// Generate ui
		initWidget(uiBinder.createAndBindUi(this));

		// Set buttons' text
		switch (pType)
		{
			case CREATE:
				buttonSaveGroup.setText(globalMessages.buttonSaveCreate());
				spaceAddTitle.setText(projectMessages.spaceAddTitle());
				// Initialization of validation popup
				validateDialogBox = new ValidateDialogBox(projectMessages.addSpaceValidationMessage());

				break;
			case UPDATE:
				buttonSaveGroup.setText(globalMessages.buttonSave());
				spaceAddTitle.setText(projectMessages.spaceEditTitle());
				// Initialization of validation popup
				validateDialogBox = new ValidateDialogBox(projectMessages.editSpaceValidationMessage());

				break;
		}
		buttonCancelGroup.setText(globalMessages.buttonCancel());

		// Initialization of group detail form
		spaceNameLabel.setText(projectMessages.spaceName());

		// Initialization row style
		for (int row = 0; row < spaceGrid.getRowCount(); row++)
		{
			if ((row % 2) == 0)
			{
				spaceGrid.getRowFormatter().addStyleName(row, ressources.style().gridRowPair());
			}
			spaceGrid.getCellFormatter().addStyleName(row, 0, ressources.style().labelCell());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HasClickHandlers getSaveButton()
	{
		return buttonSaveGroup;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HasClickHandlers getCancelButton()
	{
		return buttonCancelGroup;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HasValue<String> getName()
	{
		return spaceName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidateDialogBox getValidateDialogBox()
	{
		return validateDialogBox;
	}

	interface ProjectEditViewImplUiBinder extends UiBinder<Widget, TemplateSpaceManageViewImpl>
	{
	}
}
