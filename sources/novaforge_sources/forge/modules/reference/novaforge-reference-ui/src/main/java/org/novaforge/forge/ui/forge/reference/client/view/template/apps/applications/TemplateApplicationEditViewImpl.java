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
package org.novaforge.forge.ui.forge.reference.client.view.template.apps.applications;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.ui.commons.client.celltable.TableResources;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.CellKey;
import org.novaforge.forge.ui.forge.reference.client.presenter.project.applications.RoleMappingObject;
import org.novaforge.forge.ui.forge.reference.client.properties.GlobalMessage;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;

import java.util.Comparator;

/**
 * @author lamirang
 */
public class TemplateApplicationEditViewImpl extends Composite implements TemplateApplicationEditView
{
	private static ApplicationEditViewImplUiBinder uiBinder   = GWT.create(ApplicationEditViewImplUiBinder.class);
	private static ReferenceResources              ressources = GWT.create(ReferenceResources.class);
	private final ProjectMessage                   projectMessages = (ProjectMessage) GWT
	                                                                   .create(ProjectMessage.class);
	private final GlobalMessage                    globalMessages  = (GlobalMessage) GWT
	                                                                   .create(GlobalMessage.class);
	private final ValidateDialogBox validateDialogBox;
	@UiField
	Grid                                        applicationGrid;
	@UiField
	Label                                       applicationEditTitle;
	@UiField
	Label                                       applicationNameLabel;
	@UiField
	Label                                       applicationName;
	@UiField
	Label                                       categoryNameLabel;
	@UiField
	Label                                       categoryName;
	@UiField
	Label                                       typeNameLabel;
	@UiField
	Label                                       typeName;

	@UiField
	HorizontalPanel                             loadingPanel;
	@UiField(provided = true)
	Image                                       loadingImage;
	@UiField
	Label                                       loadingLabel;
	@UiField
	Label                                       rolesTitle;
	@UiField(provided = true)
	CellTable<RoleMappingObject>                rolesCellTable;
	@UiField(provided = true)
	SimplePager                                 rolesPager;
	@UiField
	Button                                      buttonSaveGroup;
	@UiField
	Button                                      buttonCancelGroup;
	private ListDataProvider<RoleMappingObject> dataProvider;
	private Column<RoleMappingObject, String>   projectRoleColumn;
	public TemplateApplicationEditViewImpl()
	{
		ressources.style().ensureInjected();
		// Init loading image
		loadingImage = new Image(ressources.bigLoading());
		initCellTable();

		// Generate ui
		initWidget(uiBinder.createAndBindUi(this));

		// Role title
		rolesTitle.setText(projectMessages.applicationRolesTitle());

		// Init loading stuff
		loadingLabel.setText(globalMessages.loadingMessage());
		loadingPanel.addStyleName(ressources.style().center());

		buttonSaveGroup.setText(globalMessages.buttonSave());
		buttonCancelGroup.setText(globalMessages.buttonCancel());

		// Initialization of application form
		applicationEditTitle.setText(projectMessages.appEditTitle());
		applicationNameLabel.setText(projectMessages.appName());
		categoryNameLabel.setText(projectMessages.appCategory());
		typeNameLabel.setText(projectMessages.appType());

		// Initialization row style
		for (int row = 0; row < applicationGrid.getRowCount(); row++)
		{
			if ((row % 2) == 0)
			{
				applicationGrid.getRowFormatter().addStyleName(row, ressources.style().gridRowPair());
			}
			applicationGrid.getCellFormatter().addStyleName(row, 0, ressources.style().labelCell());
		}
		// Initialization of validation popup
		validateDialogBox = new ValidateDialogBox(projectMessages.editAppValidationMessage());

	}

	/**
	 *
	 */
	protected void initCellTable()
	{
		rolesCellTable = new CellTable<RoleMappingObject>(8, (Resources) GWT.create(TableResources.class),
		    CellKey.ROLE_MAPPING_KEY_PROVIDER);
		rolesCellTable.setWidth("100%", true);
		// Init empty widget
		final Label emptyUsersLabel = new Label(projectMessages.emptyRolesMessage());
		emptyUsersLabel.setStyleName(ressources.style().emptyLabel());
		rolesCellTable.setEmptyTableWidget(emptyUsersLabel);

		// Create a Pager to control the CellTable
		final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		rolesPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		rolesPager.setDisplay(rolesCellTable);

		// Initialize the columns.
		initTableColumns();

		// Add the CellTable to the adapter
		dataProvider = new ListDataProvider<RoleMappingObject>();
		dataProvider.addDataDisplay(rolesCellTable);

		// Add the CellTable to the adapter
		final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(rolesCellTable);
		rolesCellTable.addColumnSortHandler(columnSortHandler);

	}

	/**
	 * Add the columns to the table.
	 */
	private void initTableColumns()
	{
		// Project role column
		projectRoleColumn = new Column<RoleMappingObject, String>(new TextCell())
		{
			@Override
			public String getValue(final RoleMappingObject object)
			{
				return object.getProjectRole();
			}
		};
		projectRoleColumn.setSortable(true);
		rolesCellTable.addColumn(projectRoleColumn, projectMessages.projectRoleColumn());

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
	public HasText getName()
	{

		return applicationName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HasText getType()
	{

		return typeName;
	}

	@Override
	public void updateSortHandler()
	{
		// Add the Sort Handler to the CellTable
		final ListHandler<RoleMappingObject> sortHandler = new ListHandler<RoleMappingObject>(dataProvider.getList());
		rolesCellTable.addColumnSortHandler(sortHandler);

		// login sort
		sortHandler.setComparator(projectRoleColumn, new Comparator<RoleMappingObject>()
		{
			@Override
			public int compare(final RoleMappingObject o1, final RoleMappingObject o2)
			{
				return o1.getProjectRole().compareTo(o2.getProjectRole());
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListDataProvider<RoleMappingObject> getDataProvider()
	{
		return dataProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CellTable<RoleMappingObject> getRolesCellTable()
	{
		return rolesCellTable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HorizontalPanel getLoadingPanel()
	{
		return loadingPanel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimplePager getRolesPager()
	{
		return rolesPager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HasText getCategory()
	{

		return categoryName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidateDialogBox getValidateDialogBox()
	{
		return validateDialogBox;
	}

	interface ApplicationEditViewImplUiBinder extends UiBinder<Widget, TemplateApplicationEditViewImpl>
	{
	}
}
