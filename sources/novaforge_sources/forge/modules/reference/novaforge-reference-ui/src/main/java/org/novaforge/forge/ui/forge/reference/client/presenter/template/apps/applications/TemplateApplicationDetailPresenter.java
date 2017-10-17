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
package org.novaforge.forge.ui.forge.reference.client.presenter.template.apps.applications;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateDeleteApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateEditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.helper.AbstractReferenceRPCCall;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.presenter.project.applications.RoleMappingObject;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.view.template.apps.applications.TemplateApplicationDetailView;
import org.novaforge.forge.ui.forge.reference.shared.PluginDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateApplicationNodeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lamirang
 */
public class TemplateApplicationDetailPresenter implements Presenter
{

	private final SimpleEventBus                eventBus;
	private final ProjectMessage                messages = (ProjectMessage) GWT.create(ProjectMessage.class);

	private final TemplateApplicationDetailView display;
	private TemplateApplicationNodeDTO          currentApplication;

	public TemplateApplicationDetailPresenter(final SimpleEventBus eventBus,
	    final TemplateApplicationDetailView display)
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
				eventBus.fireEvent(new TemplateEditApplicationEvent(currentApplication));
			}
		});
		display.getDeleteButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{

				display.getValidateDialogBox().show();

			}
		});
		display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getValidateDialogBox().hide();
				eventBus.fireEvent(new TemplateDeleteApplicationEvent(currentApplication));

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
	public void updateApplicationDetail(final TemplateApplicationNodeDTO pApplicationNodeDTO,
	    final List<RoleDTO> pRoles)
	{
		currentApplication = pApplicationNodeDTO;
		display.getName().setText(pApplicationNodeDTO.getName());

		setPluginInfo(pApplicationNodeDTO.getPluginUUID());
		setRoleMapping(pApplicationNodeDTO, pRoles);
	}

	private void setPluginInfo(final String pUUID)
	{
		new AbstractReferenceRPCCall<PluginDTO>()
		{
			@Override
			protected void callService(final AsyncCallback<PluginDTO> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().getPluginInfo(pUUID, pCb);
			}

			@Override
			public void onFailure(final Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(final PluginDTO pResult)
			{
				if (pResult != null)
				{
					display.getType().setText(pResult.getPluginType());
					display.getCategory().setText(pResult.getPluginCategory());
				}

			}

		}.retry(0);

	}

	private void setRoleMapping(final TemplateApplicationNodeDTO pApplicationNodeDTO, final List<RoleDTO> pRoles)
	{

		final List<RoleMappingObject> roleMappingList = new ArrayList<RoleMappingObject>();

		for (final RoleDTO roleDTO : pRoles)
		{
			final String role = roleDTO.getName();
			if (pApplicationNodeDTO.getRolesMapping().containsKey(role))
			{
				roleMappingList.add(new RoleMappingObject(role, pApplicationNodeDTO.getRolesMapping().get(role)));
			}
			else
			{
				roleMappingList.add(new RoleMappingObject(role, messages.noRole()));
			}
		}
		display.getDataProvider().setList(roleMappingList);
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
