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

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateCancelEditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateSaveEditApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.helper.AbstractReferenceRPCCall;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.presenter.project.applications.RoleMappingObject;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.view.template.apps.applications.TemplateApplicationEditView;
import org.novaforge.forge.ui.forge.reference.shared.PluginDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateApplicationNodeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lamirang
 */
public class TemplateApplicationEditPresenter implements Presenter
{

	private final SimpleEventBus              eventBus;
	private final ProjectMessage              messages = (ProjectMessage) GWT.create(ProjectMessage.class);

	private final TemplateApplicationEditView display;
	private TemplateApplicationNodeDTO        currentApplication;
	private List<RoleDTO>                     rolesList;

	private String                            projectId;
	private Map<String, String>               currentMapping;

	public TemplateApplicationEditPresenter(final SimpleEventBus eventBus,
	    final TemplateApplicationEditView display)
	{
		super();
		this.eventBus = eventBus;
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
				if (!TemplateApplicationEditPresenter.this.isValidMapping())
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
				currentApplication.setRolesMapping(currentMapping);
				eventBus.fireEvent(new TemplateSaveEditApplicationEvent(currentApplication));

			}
		});
		display.getCancelButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				eventBus.fireEvent(new TemplateCancelEditApplicationEvent());
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
	public void updateApplication(final TemplateApplicationNodeDTO pApplicationNodeDTO,
	    final List<RoleDTO> pRoles)
	{
		currentApplication = pApplicationNodeDTO;
		display.getName().setText(pApplicationNodeDTO.getName());
		rolesList = new ArrayList<RoleDTO>(pRoles);
		refreshRolesMapping(pApplicationNodeDTO);
	}

	private void refreshRolesMapping(final TemplateApplicationNodeDTO pApplicationNodeDTO)
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
					TemplateApplicationEditPresenter.this.setRoleMapping(pApplicationNodeDTO, pResult);
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

	private void setRoleMapping(final TemplateApplicationNodeDTO pApplicationNodeDTO, final PluginDTO pPluginDTO)
	{
		currentMapping = pApplicationNodeDTO.getRolesMapping();

		TemplateApplicationEditPresenter.this.currentMapping.putAll(pApplicationNodeDTO.getRolesMapping());
		final List<RoleMappingObject> roleMappingList = new ArrayList<RoleMappingObject>();

		for (final RoleDTO roleDTO : rolesList)
		{
			final String role = roleDTO.getName();
			if (pApplicationNodeDTO.getRolesMapping().containsKey(role))
			{
				roleMappingList.add(new RoleMappingObject(role, pApplicationNodeDTO.getRolesMapping().get(role)));
			}
			else
			{
				roleMappingList.add(new RoleMappingObject(role, TemplateApplicationEditPresenter.this.messages
				    .noRole()));
			}
		}
		setApplicationRolesColumn(pPluginDTO.getRoles());
		TemplateApplicationEditPresenter.this.display.getDataProvider().setList(roleMappingList);
		setTable();
	}

	/**
	 *
	 */
	private void setApplicationRolesColumn(final Set<String> pRoles)
	{
		final List<String> roles = new ArrayList<String>();
		roles.add(TemplateApplicationEditPresenter.this.messages.noRole());
		roles.addAll(pRoles);

		// Application role column
		if (TemplateApplicationEditPresenter.this.display.getRolesCellTable().getColumnCount() != 1)
		{
			TemplateApplicationEditPresenter.this.display.getRolesCellTable().removeColumn(1);
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
		TemplateApplicationEditPresenter.this.display.getRolesCellTable().addColumn(appRolecolumn,
		    TemplateApplicationEditPresenter.this.messages.appRoleColumn());
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
