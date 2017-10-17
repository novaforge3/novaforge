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
package org.novaforge.forge.ui.forge.reference.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ViewEnum;
import org.novaforge.forge.ui.forge.reference.client.presenter.project.ProjectTabPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.template.TemplateTabPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.tool.UploadToolTabPresenter;
import org.novaforge.forge.ui.forge.reference.client.properties.GlobalMessage;
import org.novaforge.forge.ui.forge.reference.client.view.GlobalTabView;

/**
 * presenter of the ProjectAdministrationView
 * 
 * @author lamirang
 */
public class GlobalTabPresenter implements Presenter
{
	private final GlobalMessage          messages            = (GlobalMessage) GWT.create(GlobalMessage.class);
	private final GlobalTabView          display;
	private final ProjectTabPresenter    projectTabPresenter;
	private final TemplateTabPresenter   templateTabPresenter;
	private final UploadToolTabPresenter uploadToolTabPresenter;
	private boolean                      isRefProjectCreated = true;

	public GlobalTabPresenter(final GlobalTabView display, final ProjectTabPresenter pProjectTabPresenter,
	    final TemplateTabPresenter pTemplateTabPresenter, final UploadToolTabPresenter pUploadToolTabPresenter)
	{
		super();
		this.display = display;
		projectTabPresenter = pProjectTabPresenter;
		templateTabPresenter = pTemplateTabPresenter;
		uploadToolTabPresenter = pUploadToolTabPresenter;

		isRefProjectCreated = (projectTabPresenter != null);
		if (isRefProjectCreated)
		{
			bind();
		}
		else
		{
			bindWithoutReferenceProjet();
		}
	}

	public void bind()
	{

		display.getTabPanel().addSelectionHandler(new SelectionHandler<Integer>()
		{
			@Override
			public void onSelection(final SelectionEvent<Integer> pEvent)
			{
				GlobalTabPresenter.this.changeTab(pEvent);
			}
		});
	}

	public void bindWithoutReferenceProjet()
	{

		display.getTabPanel().addSelectionHandler(new SelectionHandler<Integer>()
		{
			@Override
			public void onSelection(final SelectionEvent<Integer> pEvent)
			{
				GlobalTabPresenter.this.changeTabWithoutReferenceProjet(pEvent);
			}
		});

	}

	private void changeTab(final SelectionEvent<Integer> p_Event)
	{
		switch (p_Event.getSelectedItem())
		{
			case 0:
				if ((!(ViewEnum.EDIT == GlobalTabPresenter.this.projectTabPresenter.getActivatedView())) && (!(ViewEnum.ADD
																																																					 == GlobalTabPresenter.this.projectTabPresenter
																																																									.getActivatedView())))
				{
					GlobalTabPresenter.this.projectTabPresenter.refreshProjectTree(null, false, true, false);
				}
				break;
			case 1:
				if (!GlobalTabPresenter.this.templateTabPresenter.isModify())
				{
					GlobalTabPresenter.this.templateTabPresenter.refreshTemplateTab(true, null);
				}
				break;
			case 2:
				GlobalTabPresenter.this.uploadToolTabPresenter.refreshTab();
				break;
		}
	}

	private void changeTabWithoutReferenceProjet(final SelectionEvent<Integer> p_Event)
	{
		switch (p_Event.getSelectedItem())
		{
			case 0:
				if (!GlobalTabPresenter.this.templateTabPresenter.isModify())
				{
					GlobalTabPresenter.this.templateTabPresenter.refreshTemplateTab(true, null);
				}
				break;
			case 1:
				GlobalTabPresenter.this.uploadToolTabPresenter.refreshTab();
				break;
		}
	}

	@Override
	public void go(final HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());
		int tabPosition = 0;
		if (isRefProjectCreated)
		{
			display.getTabPanel().insert(projectTabPresenter.getDisplay(), messages.headerProjectTab(), tabPosition++);
		}

		display.getTabPanel().insert(templateTabPresenter.getDisplay(), messages.headerTemplateTab(), tabPosition++);
		display.getTabPanel().insert(uploadToolTabPresenter.getDisplay(), messages.headerUploadToolTab(), tabPosition++);
	}
}