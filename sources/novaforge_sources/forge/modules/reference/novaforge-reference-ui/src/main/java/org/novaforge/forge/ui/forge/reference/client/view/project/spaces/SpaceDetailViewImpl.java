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
package org.novaforge.forge.ui.forge.reference.client.view.project.spaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;

/**
 * @author lamirang
 */
public class SpaceDetailViewImpl extends Composite implements SpaceDetailView
{
	private static ProjectEditViewImplUiBinder uiBinder        = GWT.create(ProjectEditViewImplUiBinder.class);
	private static ReferenceResources          ressources      = GWT.create(ReferenceResources.class);
	private final ProjectMessage projectMessages = (ProjectMessage) GWT.create(ProjectMessage.class);
	private final ValidateDialogBox validateDialogBox;
	@UiField
	Button                          buttonAddApp;
	@UiField
	Button                          buttonUpdateSpace;
	@UiField
	Button                          buttonDeleteSpace;
	@UiField
	Grid                            spaceGrid;
	@UiField
	Label                           spaceAddTitle;
	@UiField
	Label                           spaceNameLabel;
	@UiField
	Label                           spaceName;

	public SpaceDetailViewImpl()
	{
		ressources.style().ensureInjected();

		// Generate ui
		initWidget(uiBinder.createAndBindUi(this));

		// Set buttons' text
		buttonAddApp.setText(projectMessages.buttonAddApplication());
		buttonUpdateSpace.setText(projectMessages.buttonUpdateSpace());
		buttonDeleteSpace.setText(projectMessages.buttonDeleteSpace());

		// Initialization of group detail form
		spaceAddTitle.setText(projectMessages.spaceInfoTitle());
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
		// Initialization of validation popup
		validateDialogBox = new ValidateDialogBox(projectMessages.deleteSpaceValidationMessage());

	}

	/**
	 * @inheritDoc
	 */
	@Override
	public HasClickHandlers getDeleteButton()
	{
		return buttonDeleteSpace;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public HasClickHandlers getUpdateButton()
	{
		return buttonUpdateSpace;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HasText getName()
	{

		return spaceName;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public HasClickHandlers getAddApplicationButton()
	{
		return buttonAddApp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidateDialogBox getValidateDialogBox()
	{
		return validateDialogBox;
	}

	interface ProjectEditViewImplUiBinder extends UiBinder<Widget, SpaceDetailViewImpl>
	{
	}
}
