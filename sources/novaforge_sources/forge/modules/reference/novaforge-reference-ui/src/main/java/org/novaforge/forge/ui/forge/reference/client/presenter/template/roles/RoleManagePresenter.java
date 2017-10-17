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
package org.novaforge.forge.ui.forge.reference.client.presenter.template.roles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.validation.Validator;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateCancelCreateRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateCancelEditRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateSaveCreateRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateSaveEditRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.ui.forge.reference.client.properties.RoleMessage;
import org.novaforge.forge.ui.forge.reference.client.view.template.roles.RoleManageView;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;

/**
 * @author lamirang
 */
public class RoleManagePresenter implements Presenter
{
	private static final String       EMPTY_STRING = "";
	private final RoleMessage messages = (RoleMessage) GWT.create(RoleMessage.class);
	private final SimpleEventBus      eventBus;
	private final RoleManageView      display;
	private final ManagePresenterType type;
	private RoleDTO currentRoleDTO;

	public RoleManagePresenter(final SimpleEventBus eventBus, final RoleManageView display,
	    final ManagePresenterType pType)
	{
		super();
		this.eventBus = eventBus;
		this.display = display;
		type = pType;
		bind();
	}

	public void bind()
	{
		display.getSaveButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				if ((!display.getRoleName().isValid()) && (!display.getRoleDescription().isValid()))
				{
					final InfoDialogBox info = new InfoDialogBox(messages.roleErrorValidation());
					info.show();
				}
				else if ((currentRoleDTO.isMandatory()) && (!display.getRoleName().getValue().equals(currentRoleDTO.getName())))
				{

					final InfoDialogBox info = new InfoDialogBox(messages.mandatoryUpdate());
					info.show();
				}
				else
				{

					display.getValidateDialogBox().show();

				}
			}
		});
		display.getCancelButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				switch (type)
				{
					case CREATE:
						eventBus.fireEvent(new TemplateCancelCreateRoleEvent());
						break;
					case UPDATE:
						eventBus.fireEvent(new TemplateCancelEditRoleEvent());
						break;
				}
			}
		});
		display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getValidateDialogBox().hide();

				final RoleDTO newRole = new RoleDTO();
				newRole.setName(display.getRoleName().getValue());
				newRole.setDescription(display.getRoleDescription().getValue());
				switch (type)
				{
					case CREATE:
						newRole.setMandatory(false);
						eventBus.fireEvent(new TemplateSaveCreateRoleEvent(newRole));
						break;
					case UPDATE:
						newRole.setMandatory(currentRoleDTO.isMandatory());
						eventBus.fireEvent(new TemplateSaveEditRoleEvent(currentRoleDTO.getName(), newRole));

						break;
				}
			}
		});

		display.getRoleName().setValidator(new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				return !((pValue == null) || EMPTY_STRING.equals(pValue));
			}

			@Override
			public String getErrorMessage()
			{
				return messages.roleNameValidation();
			}
		});
		display.getRoleDescription().setValidator(new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				return pValue.length() < 1024;
			}

			@Override
			public String getErrorMessage()
			{
				return messages.roleDescValidation();
			}
		});

	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());
		display.getRoleName().clear();
		display.getRoleDescription().clear();
	}

	/**
    * 
    */
	public void updateRoleDetails(final RoleDTO pRoleDTO)
	{
		if (pRoleDTO != null)
		{
			currentRoleDTO = pRoleDTO;
			display.getRoleName().setValue(pRoleDTO.getName());
			display.getRoleDescription().setValue(pRoleDTO.getDescription());
			if (pRoleDTO.isMandatory())
			{
				display.getRoleName().setEnable(false);
			}
		}
		else
		{
			currentRoleDTO = new RoleDTO();
			currentRoleDTO.setMandatory(false);
		}

	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}

}
