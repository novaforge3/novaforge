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
package org.novaforge.forge.ui.novadeploy.internal.module;

import net.engio.mbassy.listener.Handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.novadeploy.internal.client.cloudAccount.additionnal.AdditionnalCloudInfosPresenter;
import org.novaforge.forge.ui.novadeploy.internal.client.cloudAccount.additionnal.AdditionnalCloudInfosViewImpl;
import org.novaforge.forge.ui.novadeploy.internal.client.cloudAccount.login.LoginCloudPresenter;
import org.novaforge.forge.ui.novadeploy.internal.client.cloudAccount.login.LoginCloudViewImpl;
import org.novaforge.forge.ui.novadeploy.internal.client.event.AccountActivatedEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.LoginSuccessfulEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.main.GlobalPresenter;
import org.novaforge.forge.ui.novadeploy.internal.client.main.GlobalViewImpl;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClient;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClientException;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.AccountState;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.Role;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalComponent;

import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

/**
 * @author B-Martinelli
 */
public class MainComponent extends AbstractPortalComponent
{
	/**
	 * Presenter part for novadeploy.
	 */
	private AdditionnalCloudInfosPresenter additionnalCloudInfosPresenter;
	private LoginCloudPresenter loginCloudPresenter;
	private GlobalPresenter globalPresenter;

	private static final Log LOGGER = LogFactory.getLog(MainComponent.class);
	private NovadeployClient novadeployClient = null;
	private String login = null;
	private AccountState accountState = null;
	private Role userRole = null;
	private String projectName;
	private boolean connected;

	/**
	 * Default constructor
	 * 
	 * @param pPortalContext
	 *            the initial portal context
	 */
	public MainComponent(final PortalContext pPortalContext)
	{
		super(pPortalContext);
		this.connected = login(pPortalContext);

		if (this.connected)
		{
			loginCloudPresenter = new LoginCloudPresenter(new LoginCloudViewImpl(), pPortalContext, novadeployClient);
			additionnalCloudInfosPresenter = new AdditionnalCloudInfosPresenter(new AdditionnalCloudInfosViewImpl(),
					pPortalContext, novadeployClient);
			globalPresenter = new GlobalPresenter(new GlobalViewImpl(), pPortalContext, novadeployClient);
			init();
		}
	}

	private boolean login(final PortalContext pPortalContext)
	{
		projectName = pPortalContext.getAttributes().get(PortalContext.KEY.PROJECTID);
		User u = null;
		String password;
		try
		{
			login = NovaDeployModule.getAuthentificationService().getCurrentUser();
			u = NovaDeployModule.getUserService().getUser(login);
			password = u.getPassword();

			novadeployClient = NovaDeployModule.getNovadeployRestClient().getConnector(
					"http://novadeploy:8181/cxf/novadeploy", login, password);
			novadeployClient.setProjectId(new String(projectName));

			// Cookie[] cookies =
			// VaadinService.getCurrentRequest().getCookies();
			//
			// Notification.show(cookies.length + " cookies",
			// Type.TRAY_NOTIFICATION);
			// Cookie casCookie = getCookieByName("CASTGC");
			//
			// if(casCookie != null){
			// System.out.println(casCookie);
			// }

			try
			{
				userRole = NovaDeployModule.getNovadeployRestClient()
						.getUserMembershipFromProject(novadeployClient, projectName).getUserRole();

				try
				{
					accountState = NovaDeployModule.getNovadeployRestClient().accountStatus(novadeployClient,
							projectName);

				} catch (NovadeployClientException e)
				{
					if (e.getResponse() != null)
					{
						Notification.show("Unable to retrieve account state. " + e.getResponse().getError(),
								Type.ERROR_MESSAGE);
						e.printStackTrace();
					} else
					{
						Notification.show("Internal Error", Type.ERROR_MESSAGE);
						e.printStackTrace();
					}
					return false;
				}
			} catch (NovadeployClientException e)
			{
				if (e.getResponse() != null)
				{
					Notification
							.show("Unable to retrieve user role. " + e.getResponse().getError(), Type.ERROR_MESSAGE);
					e.printStackTrace();
				} else
				{
					Notification.show("Internal Error", Type.ERROR_MESSAGE);
					e.printStackTrace();
				}
				return false;
			}
		} catch (NovadeployClientException e)
		{
			if (e.getResponse() != null)
			{
				Notification.show("There was a problem when trying to log into NovaDeploy. "
						+ e.getResponse().getError(), Type.ERROR_MESSAGE);
			} else
			{
				Notification.show("Internal Error", Type.ERROR_MESSAGE);
			}
			return false;
		} catch (UserServiceException e1)
		{
			Notification.show("Internal Error", Type.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/** {@inheritDoc} */
	@Override
	public void init()
	{
		// If current user is not a manager
		if (!userRole.equals(Role.MANAGER))
		{
			// Account status has to be ACTIVATE to use the app
			if (accountState.equals(AccountState.ACTIVATE))
			{
				setContent(globalPresenter.getComponent());
				globalPresenter.refresh();
			} else
			{
				VerticalLayout vl = new VerticalLayout();
				vl.addComponent(new Label(
						"The Account is not fully configured and you do not have the adapted role to do it."));
				setContent(vl);
			}
		} else
		{
			// Current user is a manager and has access to all
			// possibilities
			switch (accountState)
			{
			case INIT:
				setContent(loginCloudPresenter.getComponent());
				loginCloudPresenter.refresh();
				break;
			case VALIDATE:
				setContent(additionnalCloudInfosPresenter.getComponent());
				additionnalCloudInfosPresenter.refresh();
				break;
			case ACTIVATE:
				setContent(globalPresenter.getComponent());
				globalPresenter.refresh();
				break;
			default:
				Notification.show("Internal Error", Type.ERROR_MESSAGE);
			}
		}

	}

	@Handler
	public void onLoginSuccessful(final LoginSuccessfulEvent pEvent)
	{
		setContent(additionnalCloudInfosPresenter.getComponent());
		additionnalCloudInfosPresenter.setData(pEvent.getVdcs(), pEvent.getNetworks());
		additionnalCloudInfosPresenter.refresh();
	}

	@Handler
	public void onAccountValidated(final AccountActivatedEvent pEvent)
	{
		setContent(globalPresenter.getComponent());
		globalPresenter.refresh();
	}

	// private Cookie getCookieByName(String name) {
	// // Fetch all cookies from the request
	// Cookie[] cookies =
	// VaadinService.getCurrentRequest().getCookies();
	//
	// // Iterate to find cookie by its name
	// for (Cookie cookie : cookies) {
	// if (name.equals(cookie.getName())) {
	// return cookie;
	// }
	// }
	//
	// return null;
	// }

	@Override
	protected PortalModuleId getModuleId()
	{
		return NovaDeployModule.getPortalModuleId();
	}
}