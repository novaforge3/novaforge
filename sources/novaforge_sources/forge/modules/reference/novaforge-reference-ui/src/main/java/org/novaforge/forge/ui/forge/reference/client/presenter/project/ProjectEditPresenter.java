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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.commons.client.dialogbox.InfoDialogBox;
import org.novaforge.forge.ui.commons.client.validation.Validator;
import org.novaforge.forge.ui.forge.reference.client.ForgeReferenceEntryPoint;
import org.novaforge.forge.ui.forge.reference.client.event.project.CancelEditProjectEvent;
import org.novaforge.forge.ui.forge.reference.client.event.project.SaveEditProjectEvent;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.properties.ProjectMessage;
import org.novaforge.forge.ui.forge.reference.client.view.project.ProjectEditView;
import org.novaforge.forge.ui.forge.reference.shared.ProjectReferenceDTO;

/**
 * @author lamirang
 */
public class ProjectEditPresenter implements Presenter
{

	private static final String   EMPTY_TEXT = "";
	private final ProjectEditView display;
	private final ProjectMessage  messages   = (ProjectMessage) GWT.create(ProjectMessage.class);

	private ProjectReferenceDTO   currentProjectDTO;

	public ProjectEditPresenter(final ProjectEditView display)
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
				if (display.getName().isValid() && display.getDescription().isValid())
				{
					display.getValidateDialogBox().show();

				}
				else
				{
					final InfoDialogBox info = new InfoDialogBox(messages.projectErrorValidation());
					info.show();
				}
			}
		});
		display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getValidateDialogBox().hide();

				currentProjectDTO.setName(display.getName().getValue());
				currentProjectDTO.setDescription(display.getDescription().getValue());
				ForgeReferenceEntryPoint.getEventBus().fireEvent(new SaveEditProjectEvent(currentProjectDTO));
			}
		});
		display.getCancelButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				ForgeReferenceEntryPoint.getEventBus().fireEvent(new CancelEditProjectEvent());
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
				return messages.projectNameValidation();
			}
		});
		display.getDescription().setValidator(new Validator()
		{
			@Override
			public boolean isValid(final String pValue)
			{
				boolean valid = false;
				if ((pValue != null) && (!EMPTY_TEXT.equals(pValue)) && (pValue.length() < 250))
				{
					valid = true;
				}
				return valid;
			}

			@Override
			public String getErrorMessage()
			{
				return messages.projectDescriptionValidation();
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
		display.getName().clear();
		display.getName().setValue(currentProjectDTO.getName());
		display.getDescription().setValue(currentProjectDTO.getDescription());
		display.getIdentifiant().setText(currentProjectDTO.getId());
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
