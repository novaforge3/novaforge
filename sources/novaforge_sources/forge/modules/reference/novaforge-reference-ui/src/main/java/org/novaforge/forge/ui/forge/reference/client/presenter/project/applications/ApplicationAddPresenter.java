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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.validation.Validator;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.CancelCreateApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.applications.SaveCreateApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.helper.AbstractReferenceRPCCall;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.view.project.applications.ApplicationAddView;
import org.novaforge.forge.ui.forge.reference.shared.ApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.CategoryDTO;
import org.novaforge.forge.ui.forge.reference.shared.PluginDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.SpaceNodeDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lamirang
 */
public class ApplicationAddPresenter implements Presenter
{

	/**
    * 
    */
	private static final String      EMPTY_TEXT = "";

	private final ProjectMessage     messages   = (ProjectMessage) GWT.create(ProjectMessage.class);

	private final ApplicationAddView display;
	private SpaceNodeDTO             spaceNodeDTO;
	private List<RoleDTO>            rolesList;
	private List<CategoryDTO>        categoriesList;
	private List<PluginDTO>          pluginsList;
	private String                   currentUUID;
	private Map<String, String>      currentMapping;

	public ApplicationAddPresenter(final ApplicationAddView display)
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

				if (!display.getName().isValid())
				{
					final InfoDialogBox info = new InfoDialogBox(messages.projectErrorValidation());
					info.show();
				}
				else if (!ApplicationAddPresenter.this.isValidMapping())
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
				final ApplicationNodeDTO app = new ApplicationNodeDTO(null, display.getName().getValue());
				app.setSpaceParent(spaceNodeDTO);
				app.setPluginUUID(currentUUID);

				ForgeReferenceEntryPoint.getEventBus().fireEvent(new SaveCreateApplicationEvent(app, currentMapping));

			}
		});
		display.getCancelButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				ForgeReferenceEntryPoint.getEventBus().fireEvent(new CancelCreateApplicationEvent());

			}
		});
		display.getCategories().addChangeHandler(new ChangeHandler()
		{

			@Override
			public void onChange(final ChangeEvent pEvent)
			{
				if (display.getCategories().getSelectedIndex() != 0)
				{
					ApplicationAddPresenter.this.setWaintingPanel();
					display.getTypes().setEnabled(false);
					ApplicationAddPresenter.this.refreshTypes(display.getCategories().getValue(
					    display.getCategories().getSelectedIndex()));
				}
				else
				{
					display.getTypes().clear();
					display.getTypes().setEnabled(false);

					ApplicationAddPresenter.this.setWaintingPanel();
				}

			}

		});
		display.getTypes().addChangeHandler(new ChangeHandler()
		{

			@Override
			public void onChange(final ChangeEvent pEvent)
			{
				if (display.getTypes().getSelectedIndex() != 0)
				{
					ApplicationAddPresenter.this.refreshRoleList();
				}
				else
				{
					display.getDataProvider().flush();
					ApplicationAddPresenter.this.setWaintingPanel();
				}
			}
		});

		display.getName().setValidator(new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				return !((pValue == null) || EMPTY_TEXT.equals(pValue));
			}

			@Override
			public String getErrorMessage()
			{
				return messages.applicationNameValidation();
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

	/**
	 *
	 */
	private void setWaintingPanel()
	{
		display.getLoadingPanel().setVisible(false);
		display.getRolesCellTable().setVisible(false);
		display.getRolesPager().setVisible(false);
		display.getWaitingPanel().setVisible(true);
	}

	private void refreshTypes(final String pCategory)
	{
		pluginsList = new ArrayList<PluginDTO>();
		final String categoryId = getCategoryId(pCategory);
		new AbstractReferenceRPCCall<List<PluginDTO>>()
		{
			@Override
			protected void callService(final AsyncCallback<List<PluginDTO>> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().getPluginsByCategory(categoryId, pCb);
			}

			@Override
			public void onFailure(final Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(final List<PluginDTO> pResult)
			{
				if (pResult != null)
				{
					pluginsList.addAll(pResult);
					display.getTypes().clear();
					display.getTypes().addItem(messages.typeList());
					for (final PluginDTO plugin : pluginsList)
					{
						display.getTypes().addItem(plugin.getPluginType());
					}
					display.getTypes().setEnabled(true);
				}

			}



		}.retry(0);

	}

	private void refreshRoleList()
	{
		setLoadingPanel();
		rolesList = new ArrayList<RoleDTO>();
		currentMapping = new HashMap<String, String>();
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
					setApplicationRolesColumn();
					display.getDataProvider().setList(rolesList);

					ApplicationAddPresenter.this.setTable();
				}

			}

			/**
			 *
			 */
			private void setApplicationRolesColumn()
			{
				final List<String> roles = new ArrayList<String>();
				roles.add(messages.noRole());
				final String type = display.getTypes().getValue(display.getTypes().getSelectedIndex());
				for (final PluginDTO pluginDTO : pluginsList)
				{
					currentUUID = pluginDTO.getPluginId();
					if (pluginDTO.getPluginType().equals(type))
					{
						roles.addAll(pluginDTO.getRoles());
						break;
					}
				}
				// Application role column
				if (display.getRolesCellTable().getColumnCount() != 1)
				{
					display.getRolesCellTable().removeColumn(1);
				}
				final Column<RoleDTO, String> appRolecolumn = new Column<RoleDTO, String>(new SelectionCell(roles))
				{

					@Override
					public String getValue(final RoleDTO pObject)
					{
						return messages.noRole();

					}
				};
				appRolecolumn.setFieldUpdater(new FieldUpdater<RoleDTO, String>()
				{
					@Override
					public void update(final int pIndex, final RoleDTO pObject, final String pValue)
					{
						if (pValue.equals(messages.noRole()))
						{
							currentMapping.remove(pObject.getName());
						}
						else
						{
							currentMapping.put(pObject.getName(), pValue);
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
	 * @param pCategory
	 *
	 * @return
	 */
	private String getCategoryId(final String pCategory)
	{
		String categoryId = "";
		for (final CategoryDTO cate : categoriesList)
		{
			if (cate.getName().equals(pCategory))
			{
				categoryId = cate.getId();
				break;
			}
		}
		return categoryId;
	}

	/**
	 *
	 */
	private void setLoadingPanel()
	{
		display.getLoadingPanel().setVisible(true);
		display.getRolesCellTable().setVisible(false);
		display.getRolesPager().setVisible(false);
		display.getWaitingPanel().setVisible(false);
	}

	/**
	 *
	 */
	private void setTable()
	{
		display.getLoadingPanel().setVisible(false);
		display.getRolesCellTable().setVisible(true);
		display.getRolesPager().setVisible(true);
		display.getWaitingPanel().setVisible(false);
	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());
		display.getName().setValue(EMPTY_TEXT);
		display.getCategories().clear();
		display.getTypes().clear();
		display.getTypes().setEnabled(false);
		setWaintingPanel();
	}

	/**
	 *
	 */
	public void updateApplication(final SpaceNodeDTO pSpaceNodeDTO)
	{
		spaceNodeDTO = pSpaceNodeDTO;
		refreshCategories();
	}

	private void refreshCategories()
	{
		categoriesList = new ArrayList<CategoryDTO>();
		new AbstractReferenceRPCCall<List<CategoryDTO>>()
		{
			@Override
			protected void callService(final AsyncCallback<List<CategoryDTO>> pCb)
			{
				ForgeReferenceEntryPoint.getServiceAsync().getPluginCategoriesForReference(LocaleInfo.getCurrentLocale()
																																														 .getLocaleName(), pCb);
			}

			@Override
			public void onFailure(final Throwable caught)
			{

				ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(final List<CategoryDTO> pResult)
			{
				if (pResult != null)
				{
					categoriesList.addAll(pResult);
					display.getCategories().addItem(messages.categoryList());
					for (final CategoryDTO category : categoriesList)
					{
						display.getCategories().addItem(category.getName());
					}
				}

			}



		}.retry(0);

	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
