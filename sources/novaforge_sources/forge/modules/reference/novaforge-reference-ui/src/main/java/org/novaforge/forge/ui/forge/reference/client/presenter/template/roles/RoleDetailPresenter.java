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
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateDeleteRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.roles.TemplateEditRoleEvent;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.properties.RoleMessage;
import org.novaforge.forge.ui.forge.reference.client.view.template.roles.RoleDetailView;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;

/**
 * @author lamirang
 */
public class RoleDetailPresenter implements Presenter
{

	private final SimpleEventBus eventBus;
	private final RoleDetailView display;
	private final RoleMessage    messages = (RoleMessage) GWT.create(RoleMessage.class);
	private RoleDTO currentRoleDTO;

	public RoleDetailPresenter(final SimpleEventBus eventBus, final RoleDetailView display)
	{
		super();
		this.eventBus = eventBus;
		this.display = display;

		bind();
	}

	public void bind()
	{
		display.getUpdateButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				eventBus.fireEvent(new TemplateEditRoleEvent(currentRoleDTO));
			}
		});

		display.getDeleteButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				if (currentRoleDTO.isMandatory())
				{
					final InfoDialogBox info = new InfoDialogBox(messages.mandatoryDelete());
					info.show();
				}
				else
				{

					display.getValidateDialogBox().show();
				}
			}
		});
		display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getValidateDialogBox().hide();
				eventBus.fireEvent(new TemplateDeleteRoleEvent(currentRoleDTO));
			}
		});
	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());

	}

	/**
    * 
    */
	protected void updateRoleDetails(final RoleDTO pRoleDTO)
	{
		currentRoleDTO = pRoleDTO;
		display.getRoleName().setText(pRoleDTO.getName());
		display.getRoleDescription().setText(pRoleDTO.getDescription());
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
