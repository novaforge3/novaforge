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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellList.Resources;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import org.novaforge.forge.ui.commons.client.celllist.ListResources;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.CellKey;
import org.novaforge.forge.ui.forge.reference.client.properties.RoleMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;

/**
 * @author lamirang
 */
public class RoleTabViewImpl extends Composite implements RoleTabView
{
	private static RoleTabViewImplUiBinder uiBinder   = GWT.create(RoleTabViewImplUiBinder.class);
	private static ReferenceResources      ressources = GWT.create(ReferenceResources.class);
	private final RoleMessage messages = (RoleMessage) GWT.create(RoleMessage.class);
	@UiField
	Button                        buttonAddRole;
	@UiField
	Label                         rolesTitle;
	@UiField(provided = true)
	Image                         reloadImage;
	@UiField(provided = true)
	Image                         topImage;
	@UiField(provided = true)
	Image                         bottomImage;
	@UiField(provided = true)
	CellList<RoleDTO>             rolesList;
	ListDataProvider<RoleDTO>     provider;
	SingleSelectionModel<RoleDTO> selectionModel;
	@UiField
	ScrollPanel                   rightPanel;

	public RoleTabViewImpl()
	{
		ressources.style().ensureInjected();

		// Initialization of groups list
		rolesList = new CellList<RoleDTO>(new RoleCell(), (Resources) GWT.create(ListResources.class),
		    CellKey.ROLE_KEY_PROVIDER);
		final Label emptyLabel = new Label(messages.emptyMessage());
		emptyLabel.setStyleName(ressources.style().emptyLabel());
		rolesList.setEmptyListWidget(emptyLabel);

		reloadImage = new Image(ressources.refresh());
		topImage = new Image(ressources.arrow_top());
		bottomImage = new Image(ressources.arrow_bottom());

		// Generate ui
		initWidget(uiBinder.createAndBindUi(this));

		// Set data provider used to display data into celllist
		provider = new ListDataProvider<RoleDTO>(CellKey.ROLE_KEY_PROVIDER);
		provider.addDataDisplay(rolesList);

		rolesList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		rolesList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

		// Add a selection model so we can select cells.
		selectionModel = new SingleSelectionModel<RoleDTO>();
		rolesList.setSelectionModel(selectionModel);

		// Set title of group label
		rolesTitle.setText(messages.listTitle());

		buttonAddRole.setText(messages.buttonAdd());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SingleSelectionModel<RoleDTO> getListSelectionModel()
	{
		return selectionModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CellList<RoleDTO> getCellList()
	{
		return rolesList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListDataProvider<RoleDTO> getListDataProvider()
	{
		return provider;
	}

	@Override
	public ScrollPanel getContentPanel()
	{
		return rightPanel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Button getAddButton()
	{
		return buttonAddRole;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getReloadImage()
	{
		return reloadImage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getArrowTop()
	{
		return topImage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Image getArrowBottom()
	{
		return bottomImage;
	}

	interface RoleTabViewImplUiBinder extends UiBinder<Widget, RoleTabViewImpl>
	{
	}

	/**
	 * The Cell used to render a {@link RoleDTO}.
	 */
	static class RoleCell extends AbstractCell<RoleDTO>
	{

		@Override
		public void render(final Context context, final RoleDTO value, final SafeHtmlBuilder sb)
		{
			// Value can be null, so do a null check..
			if (value == null)
			{
				return;
			}
			sb.appendHtmlConstant("<strong>");
			sb.appendEscaped(String.valueOf(value.getOrder()));
			sb.appendEscaped(". ");
			sb.appendHtmlConstant("</strong>");
			sb.appendEscaped(value.getName());
		}
	}
}
