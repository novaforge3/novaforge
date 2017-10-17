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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.template.DeleteTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.DisableTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.EditTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.EnableTemplateEvent;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.properties.TemplateMessage;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplateDetailView;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateRootNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateSpaceNodeDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lamirang
 */
public class TemplateDetailPresenter implements Presenter
{

	private final TemplateDetailView display;
	private final TemplateMessage    templateMessages = (TemplateMessage) GWT.create(TemplateMessage.class);
	private final List<Integer>      openedChild;
	private TemplateDTO currentTemplateDTO;

	public TemplateDetailPresenter(final TemplateDetailView display)
	{
		super();
		this.display = display;
		openedChild = new ArrayList<Integer>();
		this.display.getDeleteButton().setEnabled(false);
		this.display.getUpdateButton().setEnabled(false);
		this.display.getEnableButton().setEnabled(false);
		this.display.getDisableButton().setEnabled(false);

		bind();
	}

	public void bind()
	{
		display.getUpdateButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				ForgeReferenceEntryPoint.getEventBus().fireEvent(new EditTemplateEvent(currentTemplateDTO));
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
		display.getDisableButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				ForgeReferenceEntryPoint.getEventBus()
				    .fireEvent(new DisableTemplateEvent(currentTemplateDTO.getId()));
			}
		});
		display.getEnableButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				ForgeReferenceEntryPoint.getEventBus().fireEvent(new EnableTemplateEvent(currentTemplateDTO.getId()));
			}
		});

		display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getValidateDialogBox().hide();
				ForgeReferenceEntryPoint.getEventBus().fireEvent(new DeleteTemplateEvent(currentTemplateDTO.getId()));
			}
		});

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
    * 
    */
	public void updateTemplateDetails(final TemplateDTO pTemplateDTO)
	{
		if (pTemplateDTO != null)
		{
			currentTemplateDTO = pTemplateDTO;
			display.getTemplateName().setText(pTemplateDTO.getName());
			display.getTemplateDescription().setText(pTemplateDTO.getDescription());

			display.getDeleteButton().setEnabled(true);
			display.getUpdateButton().setEnabled(true);
			if (TemplateDTO.TEMPLATE_STATUS_ENABLE.equalsIgnoreCase(currentTemplateDTO.getStatus()))
			{
				display.getTemplateStatus().setText(templateMessages.statusEnable());
				display.getEnableButton().setEnabled(false);
				display.getDisableButton().setEnabled(true);
			}
			else
			{
				display.getTemplateStatus().setText(templateMessages.statusInProgress());
				display.getEnableButton().setEnabled(true);
				display.getDisableButton().setEnabled(false);
			}

			loadTemplateRoles(pTemplateDTO);
			loadTemplateApplications(pTemplateDTO);
		}
		else
		{
			display.getDeleteButton().setEnabled(false);
			display.getUpdateButton().setEnabled(false);
			display.getEnableButton().setEnabled(false);
			display.getDisableButton().setEnabled(false);
		}
	}

	/**
	 * @param pTemplateDTO
	 */
	private void loadTemplateRoles(final TemplateDTO pTemplateDTO)
	{

		if (pTemplateDTO.getRoles() != null)
		{
			TemplateDetailPresenter.this.display.getDataRoleProvider().setList(pTemplateDTO.getRoles());
		}
		else
		{
			TemplateDetailPresenter.this.display.getDataRoleProvider().setList(new ArrayList<RoleDTO>());
		}
	}

	/**
	 * @param pTemplateDTO
	 */
	private void loadTemplateApplications(final TemplateDTO pTemplateDTO)
	{
		updateSpaceList(pTemplateDTO);
		openPreviousSpaces(true);
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
		else if (Common.isDebugMode())
		{
			final List<TemplateSpaceNodeDTO> newSpace = new ArrayList<TemplateSpaceNodeDTO>();
			newSpace.add(new TemplateSpaceNodeDTO(null, "debugging space"));
			display.getSpacesDataProvider().setList(newSpace);
		}
		else
		{
			display.getSpacesDataProvider().getList().clear();
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

	/**
	 * @return the currentTemplateDTO
	 */
	public TemplateDTO getCurrentTemplateDTO()
	{
		return currentTemplateDTO;
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
