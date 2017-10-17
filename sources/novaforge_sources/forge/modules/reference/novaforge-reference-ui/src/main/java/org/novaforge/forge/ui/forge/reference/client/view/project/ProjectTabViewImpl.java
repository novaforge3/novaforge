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
package org.novaforge.forge.ui.forge.reference.client.view.project;

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
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;
import org.novaforge.forge.ui.forge.reference.client.view.project.resources.ProjectResources;
import org.novaforge.forge.ui.forge.reference.client.view.project.resources.TreeResources;
import org.novaforge.forge.ui.forge.reference.shared.ApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.NodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.SpaceNodeDTO;

/**
 * @author lamirang
 */
public class ProjectTabViewImpl extends Composite implements ProjectTabView
{
	private static GlobalTabViewImplUiBinder uiBinder      = GWT.create(GlobalTabViewImplUiBinder.class);
	private static ReferenceResources        ressources    = GWT.create(ReferenceResources.class);
	private static ProjectResources          projectRes    = GWT.create(ProjectResources.class);
	private static TreeResources             treeResources = GWT.create(TreeResources.class);
	private final ProjectTreeModel viewModel;
	@UiField                  ScrollPanel rightPanel;
	@UiField                  Label       projectTitle;
	@UiField(provided = true) CellTree    projectTree;

	public ProjectTabViewImpl()
	{
		ressources.style().ensureInjected();
		projectRes.style().ensureInjected();

		// Init tree cell
		viewModel = new ProjectTreeModel();
		projectTree = new CellTree(viewModel, null, treeResources);
		projectTree.setAnimationEnabled(true);
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public ScrollPanel getContentPanel()
	{
		return rightPanel;
	}

	@Override
	public ListDataProvider<SpaceNodeDTO> getSpacesDataProvider()
	{
		return viewModel.getSpacesDataProvider();
	}

	@Override
	public Label getProjectTitle()
	{
		return projectTitle;
	}

	@Override
	public SingleSelectionModel<NodeDTO> getSelectionModel()
	{
		return viewModel.getSelectionModel();
	}

	@Override
	public CellTree getProjectTree()
	{
		return projectTree;
	}

	interface GlobalTabViewImplUiBinder extends UiBinder<Widget, ProjectTabViewImpl>
	{
	}

	private static class ProjectTreeModel implements TreeViewModel
	{
		private final ListDataProvider<SpaceNodeDTO> spacesDataProvider = new ListDataProvider<SpaceNodeDTO>();
		private final SingleSelectionModel<NodeDTO>  selectionModel     = new SingleSelectionModel<NodeDTO>();

		@Override
		public <T> NodeInfo<?> getNodeInfo(final T value)
		{
			if (value == null)
			{
				return new DefaultNodeInfo<SpaceNodeDTO>(spacesDataProvider, new SpaceNodeCell(), selectionModel,
				    null);
			}
			else if (value instanceof SpaceNodeDTO)
			{
				final SpaceNodeDTO space = (SpaceNodeDTO) value;
				return new DefaultNodeInfo<ApplicationNodeDTO>(new ListDataProvider<ApplicationNodeDTO>(
				    space.getApplications()), new ApplicationNodeCell(), selectionModel, null);
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
			if (value instanceof ApplicationNodeDTO)
			{
				isLeaf = true;
			}
			else if ((value instanceof SpaceNodeDTO) && (((SpaceNodeDTO) value).getApplications().isEmpty()))
			{
				isLeaf = true;
			}
			return isLeaf;
		}

		/**
		 * @return the spacesDataProvider
		 */
		public ListDataProvider<SpaceNodeDTO> getSpacesDataProvider()
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
		private static class SpaceNodeCell extends AbstractCell<SpaceNodeDTO>
		{

			@Override
			public void render(final Context context, final SpaceNodeDTO value, final SafeHtmlBuilder sb)
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
		private static class ApplicationNodeCell extends AbstractCell<ApplicationNodeDTO>
		{

			@Override
			public void render(final Context context, final ApplicationNodeDTO value, final SafeHtmlBuilder sb)
			{
				if (value != null)
				{
					sb.appendEscaped(value.getName());
				}
			}
		}

	}
}
