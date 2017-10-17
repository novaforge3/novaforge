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

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.CancelEditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.SaveEditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.helper.AbstractReferenceRPCCall;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.view.project.applications.ApplicationEditView;
import org.novaforge.forge.ui.forge.reference.shared.ApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.PluginDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lamirang
 */
public class ApplicationEditPresenter implements Presenter
{

	private final ProjectMessage      messages = (ProjectMessage) GWT.create(ProjectMessage.class);

	private final ApplicationEditView display;
	private ApplicationNodeDTO        currentApplication;
	private List<RoleDTO>             rolesList;

	private Map<String, String>       currentMapping;

	public ApplicationEditPresenter(final ApplicationEditView display)
	{
		super();
		this.display = display;
		bind();
	}

	public void bind()
	{
		display.getSaveButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				if (!ApplicationEditPresenter.this.isValidMapping())
				{
					final InfoDialogBox info = new InfoDialogBox(messages.mandatoryRoleMapping());
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
				ForgeReferenceEntryPoint.getEventBus().fireEvent(
				    new SaveEditApplicationEvent(currentApplication, currentMapping));

			}
		});
		display.getCancelButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				ForgeReferenceEntryPoint.getEventBus().fireEvent(new CancelEditApplicationEvent());
			}
		});
	}

	private boolean isValidMapping()
	{
		boolean returnValue = true;
		if (rolesList != null)
		{
			for (final RoleDTO role : rolesList)
			{
				if (role.isMandatory() && (!currentMapping.containsKey(role.getName())))
				{
					returnValue = false;
					break;
				}
			}
		}
		return returnValue;

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
	public void updateApplication(final ApplicationNodeDTO pApplicationNodeDTO)
	{
		currentApplication = pApplicationNodeDTO;
		display.getName().setText(pApplicationNodeDTO.getName());
		refreshRolesMapping(pApplicationNodeDTO);
	}

	private void refreshRolesMapping(final ApplicationNodeDTO pApplicationNodeDTO)
	{
		setLoadingPanel();
		new AbstractReferenceRPCCall<PluginDTO>()
		{
			@Override
			protected void callService(final AsyncCallback<PluginDTO> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().getPluginInfo(pApplicationNodeDTO.getPluginUUID(), pCb);
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
					ApplicationEditPresenter.this.getRoles(pApplicationNodeDTO, pResult);
				}

			}



		}.retry(0);

	}

	/**
	 *
	 */
	private void setLoadingPanel()
	{
		display.getLoadingPanel().setVisible(true);
		display.getRolesCellTable().setVisible(false);
		display.getRolesPager().setVisible(false);
	}

	private void getRoles(final ApplicationNodeDTO pApplicationNodeDTO, final PluginDTO pPluginDTO)
	{
		rolesList = new ArrayList<RoleDTO>();
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
					rolesList.addAll(pResult);
					ApplicationEditPresenter.this.setRoleMapping(pApplicationNodeDTO, pPluginDTO, pResult);
				}

			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);

	}

	private void setRoleMapping(final ApplicationNodeDTO pApplicationNodeDTO, final PluginDTO pPluginDTO,
	    final List<RoleDTO> pRoles)
	{
		currentMapping = new HashMap<String, String>();
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
					currentMapping.putAll(pResult);
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
					setApplicationRolesColumn(pPluginDTO.getRoles());
					display.getDataProvider().setList(roleMappingList);
					ApplicationEditPresenter.this.setTable();
				}

			}

			/**
			 *
			 */
			private void setApplicationRolesColumn(final Set<String> pRoles)
			{
				final List<String> roles = new ArrayList<String>();
				roles.add(messages.noRole());
				roles.addAll(pRoles);

				// Application role column
				if (display.getRolesCellTable().getColumnCount() != 1)
				{
					display.getRolesCellTable().removeColumn(1);
				}
				final Column<RoleMappingObject, String> appRolecolumn = new Column<RoleMappingObject, String>(
				    new SelectionCell(roles))
				{

					@Override
					public String getValue(final RoleMappingObject pObject)
					{
						return pObject.getAppRole();

					}
				};
				appRolecolumn.setFieldUpdater(new FieldUpdater<RoleMappingObject, String>()
				{
					@Override
					public void update(final int pIndex, final RoleMappingObject pObject, final String pValue)
					{
						if (pValue.equals(messages.noRole()))
						{
							currentMapping.remove(pObject.getProjectRole());
						}
						else
						{
							currentMapping.put(pObject.getProjectRole(), pValue);
						}

					}
				});
				display.getRolesCellTable().addColumn(appRolecolumn, messages.appRoleColumn());
			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

		}.retry(0);
	}

	/**
	 *
	 */
	private void setTable()
	{
		display.getLoadingPanel().setVisible(false);
		display.getRolesCellTable().setVisible(true);
		display.getRolesPager().setVisible(true);
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
