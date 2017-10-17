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
package org.novaforge.forge.ui.forge.reference.client.view.template.roles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.forge.reference.client.properties.RoleMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;

/**
 * @author lamirang
 */
public class RoleDetailViewImpl extends Composite implements RoleDetailView
{
	private static RoleDetailViewImplUiBinder uiBinder   = GWT.create(RoleDetailViewImplUiBinder.class);
	private static ReferenceResources         ressources = GWT.create(ReferenceResources.class);
	private final RoleMessage messages = (RoleMessage) GWT.create(RoleMessage.class);
	private final ValidateDialogBox validateDialogBox;
	@UiField
	Button                          buttonUpdateGroup;
	@UiField
	Button                          buttonDeleteGroup;
	@UiField
	Label                           roleDetailTitle;
	@UiField
	Grid                            roleGrid;
	@UiField
	Label                           roleNameLabel;
	@UiField
	Label                           roleName;
	@UiField
	Label                           roleDescLabel;
	@UiField
	Label                           roleDesc;

	HorizontalPanel                 loadingPanel;
	Image                           loadingImage;
	Label                           loadingLabel;

	public RoleDetailViewImpl()
	{
		ressources.style().ensureInjected();

		// Generate ui
		initWidget(uiBinder.createAndBindUi(this));
		// Set buttons' text
		buttonUpdateGroup.setText(messages.buttonUpdate());
		buttonDeleteGroup.setText(messages.buttonDelete());

		// Init loading label
		loadingPanel = new HorizontalPanel();
		loadingPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		loadingLabel = new Label(messages.loadingMessage());
		loadingImage = new Image(ressources.bigLoading());
		loadingPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		loadingPanel.add(loadingImage);
		loadingPanel.add(loadingLabel);
		loadingPanel.addStyleName(ressources.style().center());

		// Initialization of group detail form
		roleDetailTitle.setText(messages.infoTitle());
		roleNameLabel.setText(messages.nameForm());
		roleDescLabel.setText(messages.desForm());

		// Initialization row style
		for (int row = 0; row < roleGrid.getRowCount(); row++)
		{
			if ((row % 2) == 0)
			{
				roleGrid.getRowFormatter().addStyleName(row, ressources.style().gridRowPair());
			}
			roleGrid.getCellFormatter().addStyleName(row, 0, ressources.style().labelCell());

		}

		// Initialization of validation popup
		validateDialogBox = new ValidateDialogBox(messages.deleteValidationMessage());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HasClickHandlers getDeleteButton()
	{
		return buttonDeleteGroup;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HasClickHandlers getUpdateButton()
	{
		return buttonUpdateGroup;
	}

	@Override
	public HasText getRoleName()
	{
		return roleName;
	}

	@Override
	public HasText getRoleDescription()
	{
		return roleDesc;
	}

	@Override
	public HorizontalPanel getRoleLoadingPanel()
	{
		return loadingPanel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidateDialogBox getValidateDialogBox()
	{
		return validateDialogBox;
	}

	interface RoleDetailViewImplUiBinder extends UiBinder<Widget, RoleDetailViewImpl>
	{
	}
}
