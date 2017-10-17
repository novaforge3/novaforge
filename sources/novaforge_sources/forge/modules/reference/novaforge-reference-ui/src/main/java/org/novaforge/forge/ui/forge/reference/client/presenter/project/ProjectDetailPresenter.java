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
package org.novaforge.forge.ui.forge.reference.client.presenter.project;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.project.EditProjectEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.spaces.CreateSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.view.project.ProjectDetailView;
import org.novaforge.forge.ui.forge.reference.shared.ProjectReferenceDTO;

/**
 * @author lamirang
 */
public class ProjectDetailPresenter implements Presenter
{

	private final ProjectDetailView display;
	private ProjectReferenceDTO     currentProjectDTO;

	public ProjectDetailPresenter(final ProjectDetailView display)
	{
		super();
		this.display = display;

		bind();
	}

	public void bind()
	{

		display.getUpdateButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				ForgeReferenceEntryPoint.getEventBus().fireEvent(new EditProjectEvent(currentProjectDTO));
			}
		});
		display.getAddSpace().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				ForgeReferenceEntryPoint.getEventBus().fireEvent(new CreateSpaceEvent());
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
	public void updateProjectDetails(final ProjectReferenceDTO pProjectDTO)
	{
		currentProjectDTO = pProjectDTO;

		display.getName().setText(currentProjectDTO.getName());
		display.getIdentifiant().setText(currentProjectDTO.getId());
		display.getDescription().setText(currentProjectDTO.getDescription());
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
