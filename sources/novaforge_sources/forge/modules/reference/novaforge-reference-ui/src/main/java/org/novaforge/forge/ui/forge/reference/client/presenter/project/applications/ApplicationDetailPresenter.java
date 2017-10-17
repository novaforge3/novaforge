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
package org.novaforge.forge.ui.forge.reference.client.presenter.project.applications;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.DeleteApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.EditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.helper.AbstractReferenceRPCCall;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.properties.ErrorMapping;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.view.project.applications.ApplicationDetailView;
import org.novaforge.forge.ui.forge.reference.shared.ApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.PluginDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.exceptions.ErrorEnumeration;
import org.novaforge.forge.ui.forge.reference.shared.exceptions.ReferenceServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lamirang
 */
public class ApplicationDetailPresenter implements Presenter
{

	private final ProjectMessage        messages = (ProjectMessage) GWT.create(ProjectMessage.class);

	private final ApplicationDetailView display;
	private ApplicationNodeDTO          currentApplication;

	public ApplicationDetailPresenter(final ApplicationDetailView display)
	{
		super();
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
				ForgeReferenceEntryPoint.getEventBus().fireEvent(new EditApplicationEvent(currentApplication));
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
				ForgeReferenceEntryPoint.getEventBus().fireEvent(new DeleteApplicationEvent(currentApplication));

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
	public void updateApplicationDetail(final ApplicationNodeDTO pApplicationNodeDTO)
	{
		currentApplication = pApplicationNodeDTO;
		display.getName().setText(pApplicationNodeDTO.getName());

		setPluginInfo(pApplicationNodeDTO.getPluginUUID());
		getRoles(pApplicationNodeDTO);
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

	private void getRoles(final ApplicationNodeDTO pApplicationNodeDTO)
	{
		new AbstractReferenceRPCCall<List<RoleDTO>>()
		{
			@Override
			protected void callService(final AsyncCallback<List<RoleDTO>> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().getRoles(pCb);
			}

			@Override
			public void onSuccess(final List<RoleDTO> pResult)
			{
				if (pResult != null)
				{
					ApplicationDetailPresenter.this.setRoleMapping(pApplicationNodeDTO, pResult);
				}

			}

			@Override
			public void onFailure(final Throwable caught)
			{
				if ((caught instanceof ReferenceServiceException)
				    && (((ReferenceServiceException) caught).getCode() != null))
				{
					final ErrorEnumeration code = ((ReferenceServiceException) caught).getCode();
					final InfoDialogBox info = new InfoDialogBox(ErrorMapping.getMessage(code));
					info.show();
				}
				else
				{
					final InfoDialogBox info = new InfoDialogBox(
					    ErrorMapping.getMessage(ErrorEnumeration.TECHNICAL_ERROR));
					info.show();
				}
			}

		}.retry(0);

	}

	private void setRoleMapping(final ApplicationNodeDTO pApplicationNodeDTO, final List<RoleDTO> pRoles)
	{
		new AbstractReferenceRPCCall<Map<String, String>>()
		{
			@Override
			protected void callService(final AsyncCallback<Map<String, String>> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().getRolesMapping(pApplicationNodeDTO.getPluginUUID(),
				    pApplicationNodeDTO.getInstanceId(), pCb);
			}

			@Override
			public void onSuccess(final Map<String, String> pResult)
			{
				if (pResult != null)
				{
					final List<RoleMappingObject> roleMappingList = new ArrayList<RoleMappingObject>();

					for (final RoleDTO roleDTO : pRoles)
					{
						final String role = roleDTO.getName();
						if (pResult.containsKey(role))
						{
							roleMappingList.add(new RoleMappingObject(role, pResult.get(role)));
						}
						else
						{
							roleMappingList.add(new RoleMappingObject(role, messages.noRole()));
						}
					}
					display.getDataProvider().setList(roleMappingList);
				}

			}

			@Override
			public void onFailure(final Throwable caught)
			{
				if ((caught instanceof ReferenceServiceException)
				    && (((ReferenceServiceException) caught).getCode() != null))
				{
					final ErrorEnumeration code = ((ReferenceServiceException) caught).getCode();
					final InfoDialogBox info = new InfoDialogBox(ErrorMapping.getMessage(code));
					info.show();
				}
				else
				{
					final InfoDialogBox info = new InfoDialogBox(
					    ErrorMapping.getMessage(ErrorEnumeration.TECHNICAL_ERROR));
					info.show();
				}
			}

		}.retry(0);
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
