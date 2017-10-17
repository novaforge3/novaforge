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
package org.novaforge.forge.ui.forge.reference.client.presenter.template;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplateEditSummaryView;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateRootNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateSpaceNodeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lamirang
 */
public class TemplateManageSummaryPresenter implements Presenter
{

	private final TemplateEditSummaryView display;

	private final List<Integer>           openedChild;

	public TemplateManageSummaryPresenter(final TemplateEditSummaryView display)
	{
		super();
		this.display = display;
		openedChild = new ArrayList<Integer>();

		bind();
	}

	public void bind()
	{
		/*
		 * Node selection
		 */
		display.getTemplateTree().addOpenHandler(new OpenHandler<TreeNode>()
		{
			@Override
			public void onOpen(final OpenEvent<TreeNode> pEvent)
			{
				openedChild.add(pEvent.getTarget().getIndex());
			}
		});
		display.getTemplateTree().addCloseHandler(new CloseHandler<TreeNode>()
		{
			@Override
			public void onClose(final CloseEvent<TreeNode> pEvent)
			{
				openedChild.remove(pEvent.getTarget().getIndex());
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
	 * Load templates' details
	 */
	public void setCurrentTemplateDTO(final TemplateDTO pTemplateDTO)
	{
		if (pTemplateDTO != null)
		{
			TemplateManageSummaryPresenter.this.display.getTemplateName().setText(pTemplateDTO.getName());
			TemplateManageSummaryPresenter.this.display.getTemplateId().setText(pTemplateDTO.getId());
			TemplateManageSummaryPresenter.this.display.getTemplateDescription().setText(
			    pTemplateDTO.getDescription());
		}

		loadTemplateRoles(pTemplateDTO);

		loadTemplateApplications(pTemplateDTO);
	}

	/**
	 * Load informations of Roles
	 */
	public void loadTemplateRoles(final TemplateDTO pTemplateDTO)
	{
		if (pTemplateDTO != null)
		{
			if (pTemplateDTO.getRoles() != null)
			{
				TemplateManageSummaryPresenter.this.display.getDataRoleProvider().setList(pTemplateDTO.getRoles());
			}
			else
			{
				TemplateManageSummaryPresenter.this.display.getDataRoleProvider().setList(new ArrayList<RoleDTO>());
			}
		}
	}

	/**
	 * Load informations of Applications
	 */
	public void loadTemplateApplications(final TemplateDTO pTemplateDTO)
	{
		if (pTemplateDTO != null)
		{
			updateSpaceList(pTemplateDTO);
			openPreviousSpaces(true);
		}
	}

	/**
    * 
    */
	private void updateSpaceList(final TemplateDTO currentTemplate)
	{
		if (currentTemplate.getRootNode() == null)
		{
			currentTemplate.setRootNode(new TemplateRootNodeDTO());
		}
		final List<TemplateSpaceNodeDTO> spaces = currentTemplate.getRootNode().getSpaces();
		if (!spaces.isEmpty())
		{
			display.getSpacesDataProvider().setList(spaces);
		}
		else
		{
			final List<TemplateSpaceNodeDTO> newSpace = new ArrayList<TemplateSpaceNodeDTO>();
			newSpace.add(new TemplateSpaceNodeDTO(null, "empty spaces"));
			display.getSpacesDataProvider().setList(newSpace);
		}
	}

	/**
    * 
    */
	private void openPreviousSpaces(final boolean pReOpened)
	{
		if (pReOpened)
		{
			// Used to open space which was previously opened
			final TreeNode rootNode = display.getTemplateTree().getRootTreeNode();
			for (final Integer integer : openedChild)
			{
				rootNode.setChildOpen(integer, true, false);
			}
		}
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
