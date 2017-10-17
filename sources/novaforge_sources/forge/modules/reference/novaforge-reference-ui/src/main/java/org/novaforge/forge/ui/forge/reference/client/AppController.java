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
package org.novaforge.forge.ui.forge.reference.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import org.novaforge.forge.ui.forge.reference.client.helper.AbstractReferenceRPCCall;
import org.novaforge.forge.ui.forge.reference.client.presenter.GlobalTabPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.ui.forge.reference.client.presenter.project.ProjectTabPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.template.TemplateTabPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.tool.DownloadToolTabPresenter;
import org.novaforge.forge.ui.forge.reference.client.presenter.tool.UploadToolTabPresenter;
import org.novaforge.forge.ui.forge.reference.client.view.GlobalTabViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.project.ProjectTabViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.template.TemplateTabViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.tool.DownloadToolTabViewImpl;
import org.novaforge.forge.ui.forge.reference.client.view.tool.UploadToolTabViewImpl;

public class AppController implements ValueChangeHandler<String>
{
	private static final String      PARAMETER_NAME_ACTION = "action";

	private GlobalTabPresenter       globalTabPresenter;
	private DownloadToolTabPresenter downloadPublicToolTabPresenter;
	private DownloadToolTabPresenter downloadPrivateToolTabPresenter;

	public AppController()
	{
		bind();
	}

	private void bind()
	{
		History.addValueChangeHandler(this);
	}

	/**
	 * manage the values change in history
	 *
	 * @param history
	 *          event
	 */
	@Override
	public void onValueChange(final ValueChangeEvent<String> event)
	{
		final String token = event.getValue();
		if (token != null)
		{
			if (token.startsWith("edit project"))
			{
				globalTabPresenter.go(RootLayoutPanel.get());
			}

		}
	}

	public void go()
	{
		final String parameter = Window.Location.getParameter(PARAMETER_NAME_ACTION);
		if (parameter != null)
		{
			final ParameterAction action = ParameterAction.fromLabel(parameter);
			switch (action)
			{
				case ADMIN:
					loadPresenters();
					break;
				case USER_VIEW:
				case ADMIN_VIEW:
					loadToolDownloadPresenter(action);
					break;
			}
		}
	}

	private void loadPresenters()
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> callback)
			{
				ForgeReferenceEntryPoint.getServiceAsync().isProjectReferenceCreated(callback);
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}

			@Override
			public void onSuccess(final Boolean result)
			{
				loadAdminPresenters(result);
			}

		}.retry(0);
	}

	private void loadToolDownloadPresenter(final ParameterAction action)
	{
		new AbstractReferenceRPCCall<Boolean>()
		{
			@Override
			protected void callService(final AsyncCallback<Boolean> callback)
			{
				ForgeReferenceEntryPoint.getServiceAsync().isAuthorizedUpdateReference(callback);
			}

			@Override
			public void onSuccess(final Boolean pResult)
			{
				switch (action)
				{
					case USER_VIEW:
						downloadPublicToolTabPresenter = new DownloadToolTabPresenter(new DownloadToolTabViewImpl(),
						    true, pResult);
						downloadPublicToolTabPresenter.go(RootPanel.get());
						break;
					case ADMIN_VIEW:
						downloadPrivateToolTabPresenter = new DownloadToolTabPresenter(new DownloadToolTabViewImpl(),
						    false, pResult);
						downloadPrivateToolTabPresenter.go(RootPanel.get());
						break;
				}
			}

			@Override
			public void onFailure(final Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}

		}.retry(0);
	}

	private void loadAdminPresenters(final boolean isRefProjectCreated)
	{
		ProjectTabPresenter projectTabPresenter = null;
		if (isRefProjectCreated)
		{
			projectTabPresenter = new ProjectTabPresenter(new ProjectTabViewImpl());
		}

		final TemplateTabPresenter   templateTabPresenter   = new TemplateTabPresenter(new TemplateTabViewImpl());
		final UploadToolTabPresenter uploadToolTabPresenter = new UploadToolTabPresenter(new UploadToolTabViewImpl());
		globalTabPresenter = new GlobalTabPresenter(new GlobalTabViewImpl(), projectTabPresenter, templateTabPresenter,
																								uploadToolTabPresenter);
		globalTabPresenter.go(RootLayoutPanel.get());

	}

}