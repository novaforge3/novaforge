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
package org.novaforge.forge.ui.forge.reference.client.view.template.apps;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import org.novaforge.forge.ui.forge.reference.client.properties.TemplateMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;
import org.novaforge.forge.ui.forge.reference.client.view.project.resources.ProjectResources;
import org.novaforge.forge.ui.forge.reference.client.view.project.resources.TreeResources;
import org.novaforge.forge.ui.forge.reference.shared.NodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateSpaceNodeDTO;

/**
 * @author lamirang
 */
public class TemplateEditSpacesViewImpl extends Composite implements TemplateEditSpacesView
{
	private static TemplateEditSpacesViewImplUiBinder uiBinder         = GWT.create(TemplateEditSpacesViewImplUiBinder.class);
	private static ReferenceResources                 ressources       = GWT.create(ReferenceResources.class);
	private static ProjectResources                   projectRes       = GWT.create(ProjectResources.class);
	private static TreeResources                      treeResources    = GWT.create(TreeResources.class);
	private final  TemplateMessage                    templateMessages = (TemplateMessage) GWT.create(TemplateMessage.class);
	private final TemplateTreeModel viewModel;
	@UiField                  Label       templateSpacesTitle;
	@UiField                  ScrollPanel rightPanel;
	@UiField(provided = true) CellTree    templateTree;

	public TemplateEditSpacesViewImpl()
	{
		ressources.style().ensureInjected();
		projectRes.style().ensureInjected();
		// Init tree cell
		viewModel = new TemplateTreeModel();
		templateTree = new CellTree(viewModel, null, treeResources);
		templateTree.setAnimationEnabled(true);

		initWidget(uiBinder.createAndBindUi(this));

		templateSpacesTitle.setText(templateMessages.spacesTitle());

	}

	@Override
	public Label getSpaceTitle()
	{
		return templateSpacesTitle;
	}

	@Override
	public ScrollPanel getContentPanel()
	{
		return rightPanel;
	}

	@Override
	public ListDataProvider<TemplateSpaceNodeDTO> getSpacesDataProvider()
	{
		return viewModel.getSpacesDataProvider();
	}

	@Override
	public SingleSelectionModel<NodeDTO> getSelectionModel()
	{
		return viewModel.getSelectionModel();
	}

	@Override
	public CellTree getTemplateTree()
	{
		return templateTree;
	}

	interface TemplateEditSpacesViewImplUiBinder extends UiBinder<Widget, TemplateEditSpacesViewImpl>
	{
	}

	private static class TemplateTreeModel implements TreeViewModel
	{
		private final ListDataProvider<TemplateSpaceNodeDTO> spacesDataProvider = new ListDataProvider<TemplateSpaceNodeDTO>();
		private final SingleSelectionModel<NodeDTO>          selectionModel     = new SingleSelectionModel<NodeDTO>();

		@Override
		public <T> NodeInfo<?> getNodeInfo(final T value)
		{
			if (value == null)
			{
				return new DefaultNodeInfo<TemplateSpaceNodeDTO>(spacesDataProvider, new SpaceNodeCell(),
				    selectionModel, null);
			}
			else if (value instanceof TemplateSpaceNodeDTO)
			{
				final TemplateSpaceNodeDTO space = (TemplateSpaceNodeDTO) value;
				return new DefaultNodeInfo<TemplateApplicationNodeDTO>(
				    new ListDataProvider<TemplateApplicationNodeDTO>(space.getApplications()),
				    new ApplicationNodeCell(), selectionModel, null);
			}
			// Unhandled type.
			final String type = value.getClass().getName();
			throw new IllegalArgumentException("Unsupported object type: " + type);
		}

		// Check if the specified value represents a leaf node. Leaf nodes cannot be opened.
		@Override
		public boolean isLeaf(final Object value)
		{
			boolean isLeaf = false;
			if (value instanceof TemplateApplicationNodeDTO)
			{
				isLeaf = true;
			}
			else if ((value instanceof TemplateSpaceNodeDTO) && (((TemplateSpaceNodeDTO) value).getApplications().isEmpty()))
			{
				isLeaf = true;
			}
			return isLeaf;
		}

		/**
		 * @return the spacesDataProvider
		 */
		public ListDataProvider<TemplateSpaceNodeDTO> getSpacesDataProvider()
		{
			return spacesDataProvider;
		}

		/**
		 * @return the spaceSelectionModel
		 */
		public SingleSelectionModel<NodeDTO> getSelectionModel()
		{
			return selectionModel;
		}

		/**
		 * A Cell used to render the space.
		 */
		private static class SpaceNodeCell extends AbstractCell<TemplateSpaceNodeDTO>
		{

			@Override
			public void render(final Context context, final TemplateSpaceNodeDTO value, final SafeHtmlBuilder sb)
			{
				if (value != null)
				{
					sb.appendEscaped(value.getName());
				}
			}
		}

		/**
		 * A Cell used to render the application.
		 */
		private static class ApplicationNodeCell extends AbstractCell<TemplateApplicationNodeDTO>
		{

			@Override
			public void render(final Context context, final TemplateApplicationNodeDTO value, final SafeHtmlBuilder sb)
			{
				if (value != null)
				{
					sb.appendEscaped(value.getName());
				}
			}
		}

	}
}
