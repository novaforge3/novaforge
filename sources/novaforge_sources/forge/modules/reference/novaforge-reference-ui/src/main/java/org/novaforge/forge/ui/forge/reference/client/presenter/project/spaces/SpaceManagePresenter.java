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
package org.novaforge.forge.ui.forge.reference.client.presenter.project.spaces;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.CancelCreateSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.CancelEditSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.SaveCreateSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.SaveEditSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ManagePresenterType;
import org.novaforge.forge.ui.forge.reference.client.view.project.spaces.SpaceManageView;
import org.novaforge.forge.ui.forge.reference.shared.RootNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.SpaceNodeDTO;

/**
 * @author lamirang
 */
public class SpaceManagePresenter implements Presenter
{

	/**
    * 
    */
	private static final String       EMPTY_STRING = "";
	private final SpaceManageView     display;
	private final ManagePresenterType type;
	private SpaceNodeDTO              currentSpaceDTO;
	private RootNodeDTO               rootNodeDTO;

	public SpaceManagePresenter(final SpaceManageView display, final ManagePresenterType pType)
	{
		super();
		this.display = display;
		type = pType;
		bind();
	}

	public void bind()
	{
		display.getSaveButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getValidateDialogBox().show();
			}
		});
		display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getValidateDialogBox().hide();

				switch (type)
				{
					case CREATE:
						final SpaceNodeDTO spaceCreate = new SpaceNodeDTO(null, display.getName().getValue());
						spaceCreate.setRootNode(rootNodeDTO);
						ForgeReferenceEntryPoint.getEventBus().fireEvent(new SaveCreateSpaceEvent(spaceCreate));
						break;
					case UPDATE:
						final SpaceNodeDTO spaceUpdate = new SpaceNodeDTO(currentSpaceDTO.getUri(), display.getName()
						    .getValue());
						spaceUpdate.setRootNode(rootNodeDTO);
						spaceUpdate.setApplications(currentSpaceDTO.getApplications());
						ForgeReferenceEntryPoint.getEventBus().fireEvent(new SaveEditSpaceEvent(spaceUpdate));
						break;
				}
			}
		});
		display.getCancelButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				switch (type)
				{
					case CREATE:
						ForgeReferenceEntryPoint.getEventBus().fireEvent(new CancelCreateSpaceEvent());
						break;
					case UPDATE:
						ForgeReferenceEntryPoint.getEventBus().fireEvent(new CancelEditSpaceEvent());
						break;
				}
			}
		});
	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());

		if (type.equals(ManagePresenterType.CREATE))
		{
			display.getName().setValue(EMPTY_STRING);
		}
	}

	/**
    * 
    */
	public void updateSpace(final SpaceNodeDTO pSpaceNodeDTO)
	{
		currentSpaceDTO = pSpaceNodeDTO;
		display.getName().setValue(pSpaceNodeDTO.getName());
	}

	/**
    * 
    */
	public void updateRootNode(final RootNodeDTO pRootNodeDTO)
	{
		rootNodeDTO = pRootNodeDTO;
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
