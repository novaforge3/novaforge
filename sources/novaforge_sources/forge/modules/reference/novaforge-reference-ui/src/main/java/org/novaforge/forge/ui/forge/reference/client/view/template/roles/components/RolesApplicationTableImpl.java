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
package org.novaforge.forge.ui.forge.reference.client.view.template.roles.components;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.novaforge.forge.ui.forge.reference.client.properties.RoleMessage;
import org.novaforge.forge.ui.forge.reference.shared.ApplicationRoleMappingDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author lamirang
 */
public class RolesApplicationTableImpl extends Composite implements RolesApplicationTable
{
	/**
    * 
    */
	private static final String SEPARATOR = ":";
	private static RolesApplicationTableImplUiBinder uiBinder  = GWT
	                                                               .create(RolesApplicationTableImplUiBinder.class);
	private static Resources                         ressource = GWT.create(Resources.class);
	private final RoleMessage                     messages       = (RoleMessage) GWT.create(RoleMessage.class);
	private final List<ApplicationRoleMappingDTO> updatedRoleApp = new ArrayList<ApplicationRoleMappingDTO>();
	private final boolean isEnabled;
	@UiField
	ScrollPanel                                   content;
	@UiField(provided = true)
	Grid                                          grid;
	public RolesApplicationTableImpl(final Map<String, List<ApplicationRoleMappingDTO>> pApps,
	    final boolean pEnabled)
	{
		isEnabled = pEnabled;
		grid = new Grid();
		grid.resizeColumns(2);
		grid.setStyleName(ressource.rolesApplicationTableStyle().tableContent());
		ressource.rolesApplicationTableStyle().ensureInjected();
		// Generate ui
		initWidget(uiBinder.createAndBindUi(this));
		if (pApps.keySet().size() > 0)
		{
			initializeRows(pApps);
		}
		else
		{
			insertEmptyRow();
		}

		setRowStyle();
	}

	/**
	 * @param pApps
	 * @param pPluginsRole
	 */
	private void initializeRows(final Map<String, List<ApplicationRoleMappingDTO>> pApps)
	{
		int index = 0;
		final Set<Entry<String, List<ApplicationRoleMappingDTO>>> entrySet = pApps.entrySet();
		for (final Entry<String, List<ApplicationRoleMappingDTO>> entry : entrySet)
		{
			addSpaceTitle(index, entry.getKey());
			index++;
			final List<ApplicationRoleMappingDTO> value = entry.getValue();
			if (value.isEmpty())
			{
				grid.insertRow(index);
				final Label appName = new Label(messages.emptyApplicationSpace());
				appName.setStyleName(ressource.rolesApplicationTableStyle().tableCellLabel());
				grid.setWidget(index, 1, appName);
			}
			else
			{
				for (final ApplicationRoleMappingDTO roleApp : value)
				{
					addApplicationName(index, roleApp);
					addApplicationRoles(index, roleApp);
					index++;
				}
			}
		}
	}

	/**
	 * @param index
	 * @param roleApp
	 */
	private void insertEmptyRow()
	{
		grid.insertRow(0);
		final Label appName = new Label(messages.emptyApplication());
		appName.setStyleName(ressource.rolesApplicationTableStyle().tableCellLabel());
		grid.setWidget(0, 0, appName);
	}

	/**
	 *
	 */
	private void setRowStyle()
	{
		for (int row = 0; row < grid.getRowCount(); row++)
		{
			grid.getRowFormatter().addStyleName(row, ressource.rolesApplicationTableStyle().tableRow());
			if ((row % 2) == 1)
			{
				grid.getRowFormatter().addStyleName(row, ressource.rolesApplicationTableStyle().tableRowPair());
			}
		}
	}

	/**
	 * @param index
	 * @param entry
	 */
	private void addSpaceTitle(final int index, final String pSpaceName)
	{
		grid.insertRow(index);
		final SimplePanel simplePanel = new SimplePanel();
		final Label spaceTitle = new Label(pSpaceName + SEPARATOR);
		simplePanel.setStyleName(ressource.rolesApplicationTableStyle().tableTitle());
		simplePanel.add(spaceTitle);
		grid.setWidget(index, 0, simplePanel);
	}

	/**
	 * @param index
	 * @param roleApp
	 */
	private void addApplicationName(final int index, final ApplicationRoleMappingDTO roleApp)
	{
		grid.insertRow(index);
		final Label appName = new Label(roleApp.getApplicationName() + SEPARATOR);
		appName.setStyleName(ressource.rolesApplicationTableStyle().tableCellLabel());
		grid.setWidget(index, 0, appName);
	}

	/**
	 * @param pPluginsRolesMap
	 * @param index
	 * @param roleApp
	 */
	private void addApplicationRoles(final int index, final ApplicationRoleMappingDTO roleApp)
	{
		final ListBox roles = new ListBox();
		roles.setStyleName(ressource.rolesApplicationTableStyle().tableCellListBox());
		if (roleApp.isState())
		{
			initializeRolesSelection(roleApp, roles);
			updatedRoleApp.add(roleApp);
			roles.setEnabled(isEnabled);
		}
		else
		{
			roles.addItem(messages.roleUnavailable());
			roles.setEnabled(false);
		}
		grid.setWidget(index, 1, roles);
	}

	/**
	 * @param pPluginsRolesMap
	 * @param roleApp
	 * @param roles
	 */
	protected void initializeRolesSelection(final ApplicationRoleMappingDTO roleApp, final ListBox roles)
	{
		final List<String> pluginRolesList = Lists.newArrayList(roleApp.getRolelist());

		int rolesIndex = 0;
		Collections.sort(pluginRolesList);
		roles.addItem(messages.noRole());
		for (final String role : pluginRolesList)
		{
			roles.addItem(role);
			if (role.equals(roleApp.getRoleName()))
			{
				rolesIndex = roles.getItemCount() - 1;
			}
		}
		roles.setSelectedIndex(rolesIndex);
		roles.addChangeHandler(new ChangeHandler()
		{
			@Override
			public void onChange(final ChangeEvent pEvent)
			{
				for (final ApplicationRoleMappingDTO app : updatedRoleApp)
				{
					if (app.getUri().equals(roleApp.getUri()))
					{
						if (roles.getSelectedIndex() == 0)
						{
							app.setRoleName("");
						}
						else
						{
							app.setRoleName(roles.getValue(roles.getSelectedIndex()));
						}
					}
				}

			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Grid getRolesGrid()
	{
		return grid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ApplicationRoleMappingDTO> getUpdatedRoleApp()
	{
		return updatedRoleApp;
	}

	/**
	 * A ClientBundle that provides images for this widget.
	 */
	public interface Resources extends ClientBundle
	{

		/**
		 * The styles used in this widget.
		 */
		@Source(Style.DEFAULT_CSS)
		Style rolesApplicationTableStyle();
	}

	/**
	 * Styles used by this widget.
	 */
	public interface Style extends CssResource
	{
		/**
		 * The path to the default CSS styles used by this resource.
		 */
		String DEFAULT_CSS = "RolesApplicationTable.css";

		String tableTitle();

		String tableContent();

		String tableRowPair();

		String tableCellLabel();

		String tableRow();

		String tableCellListBox();

	}

	interface RolesApplicationTableImplUiBinder extends UiBinder<Widget, RolesApplicationTableImpl>
	{
	}

}
