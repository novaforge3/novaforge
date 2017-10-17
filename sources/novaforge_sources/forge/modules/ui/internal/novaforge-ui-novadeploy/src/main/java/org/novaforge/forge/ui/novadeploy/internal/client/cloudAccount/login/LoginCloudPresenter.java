/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 of the
 * License.
 *
 * This file is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details. You should have
 * received a copy of the GNU Affero General Public License along with
 * this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with libraries listed in COPYRIGHT file at the
 * top-level directof of this distribution (or a modified version of
 * that libraries), containing parts covered by the terms of licenses
 * cited in the COPYRIGHT file, the licensors of this Program grant
 * you additional permission to convey the resulting work.
 */
package org.novaforge.forge.ui.novadeploy.internal.client.cloudAccount.login;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.novadeploy.internal.client.event.LoginSuccessfulEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClient;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClientException;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.CloudConfigResponse;
import org.novaforge.forge.ui.novadeploy.internal.module.AbstractNovaDeployPresenter;
import org.novaforge.forge.ui.novadeploy.internal.module.NovaDeployModule;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * @author Vincent Chenal
 */
public class LoginCloudPresenter extends AbstractNovaDeployPresenter implements Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = 2523449373300815899L;

	/**
	 * Content of projbuttonClickect view
	 */
	private final LoginCloudView view;
	private NovadeployClient novadeployClient;

	private static final Log LOGGER = LogFactory.getLog(LoginCloudPresenter.class);

	/**
	 * @param pPortalContext
	 * @param novadeployClient2
	 */
	public LoginCloudPresenter(final LoginCloudView pView, final PortalContext pPortalContext,
			NovadeployClient novadeployClient)
	{
		super(pPortalContext);
		view = pView;

		addListeners();

		this.novadeployClient = novadeployClient;

	}

	/**
	 * It will add listeners to view components
	 */
	private void addListeners()
	{
		view.getOKButton().addClickListener(new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{

				boolean successfullyConnected;
				CloudConfigResponse response = null;
				try
				{

					view.setDataCloudName(view.getCloudNameField());
					view.setDataLogin(view.getLoginField());
					view.setDataPassword(view.getPasswordField());

					if (view.getCloudNameField().equals("") || view.getLoginField().equals("")
							|| view.getPasswordField().equals(""))
					{
						Notification.show("Please fill all login fields", Type.ERROR_MESSAGE);
						return;
					}

					response = NovaDeployModule.getNovadeployRestClient().validateCloud(novadeployClient,
							novadeployClient.getProjectId(), view.getLoginField(), view.getCloudNameField(),
							view.getPasswordField(), "vCloud");

					// Send message to MainComponent including
					// connections infos
					getEventBus().publish(new LoginSuccessfulEvent(response.getVdc(), response.getNetwork()));
				} catch (NovadeployClientException e)
				{
					if (e.getResponse() != null)
					{
						Notification.show("Unable to log into NovaDeploy with these credentials. "
								+ e.getResponse().getError(), Type.ERROR_MESSAGE);
					} else
					{
						Notification.show("Internal Error", Type.ERROR_MESSAGE);
					}
					successfullyConnected = false;
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getComponent()
	{
		return view;
	}

	/**
	 * Will refresh the project information.
	 */
	public void refresh()
	{
		refreshContent();
		refreshLocalized(getCurrentLocale());
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void refreshContent()
	{
		view.refreshContent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLocalized(final Locale pLocale)
	{
		view.refreshLocale(pLocale);
	}

}
