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
package org.novaforge.forge.ui.forge.reference.client.presenter.template.apps.spaces;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.applications.TemplateCreateApplicationEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.spaces.TemplateDeleteSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.event.template.apps.spaces.TemplateEditSpaceEvent;
import org.novaforge.forge.ui.forge.reference.client.presenter.Presenter;
import org.novaforge.forge.ui.forge.reference.client.view.template.apps.spaces.TemplateSpaceDetailView;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateSpaceNodeDTO;

/**
 * @author lamirang
 */
public class TemplateSpaceDetailPresenter implements Presenter
{

	private final SimpleEventBus          eventBus;

	private final TemplateSpaceDetailView display;
	private TemplateSpaceNodeDTO          currentSpaceDTO;

	public TemplateSpaceDetailPresenter(final SimpleEventBus eventBus, final TemplateSpaceDetailView display)
	{
		super();
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}

	public void bind()
	{
		display.getAddApplicationButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				eventBus.fireEvent(new TemplateCreateApplicationEvent());

			}
		});
		display.getUpdateButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				eventBus.fireEvent(new TemplateEditSpaceEvent(currentSpaceDTO));
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
		display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(final ClickEvent event)
			{
				display.getValidateDialogBox().hide();

				eventBus.fireEvent(new TemplateDeleteSpaceEvent(currentSpaceDTO));
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
	public void updateSpaceDetails(final TemplateSpaceNodeDTO pSpaceNodeDTO)
	{
		currentSpaceDTO = pSpaceNodeDTO;
		display.getName().setText(currentSpaceDTO.getName());
	}

	public IsWidget getDisplay()
	{
		return display.asWidget();
	}
}
