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
package org.novaforge.forge.ui.novadeploy.internal.client.main.environments;

import java.io.Serializable;
import java.util.Locale;

import net.engio.mbassy.bus.MBassador;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.novadeploy.internal.client.containers.EnvironmentContainer;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClient;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClientException;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.CustomerEnvironment;
import org.novaforge.forge.ui.novadeploy.internal.module.AbstractNovaDeployPresenter;
import org.novaforge.forge.ui.novadeploy.internal.module.NovaDeployModule;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * @author Vincent Chenal
 */
public class EnvironmentsPresenter extends AbstractNovaDeployPresenter implements Serializable
{

	/**
	 * Serial version uid used for serialization
	 */
	private static final long serialVersionUID = 2704843315038570848L;

	/**
	 * Content of project view
	 */
	private final EnvironmentsView view;
	private NovadeployClient novadeployClient;
	private static final Log LOGGER = LogFactory.getLog(EnvironmentsPresenter.class);
	private CustomerEnvironment[] environments = null;
	private EnvironmentContainer environmentContainer = null;

	public static int counter = 10;

	/**
	 * @param pPortalContext
	 * @param novaDeployClient
	 */
	public EnvironmentsPresenter(final EnvironmentsView pView, final PortalContext pPortalContext,
			NovadeployClient novaDeployClient)
	{
		super(pPortalContext);
		view = pView;
		novadeployClient = novaDeployClient;
		view.setPresenter(this);

	}

	/*
	 * @Handler public void onViewEnvironmentEvent(final
	 * ViewEnvironmentEvent pEvent) { //
	 * Notification.show("ViewEnvironment id=\"" + //
	 * pEvent.getEnvironmentId() + "\"", //
	 * Notification.Type.ERROR_MESSAGE); //
	 * this.environmentContainer.removeAllItems(); Item i =
	 * this.environmentContainer.addItem("" +
	 * EnvironmentsPresenter.counter);
	 * i.getItemProperty(EnvironmentItemProperty
	 * .ENVIRONMENT_ID.getPropertyId()).setValue( "" +
	 * EnvironmentsPresenter.counter);
	 * i.getItemProperty(EnvironmentItemProperty
	 * .ENVIRONMENT_DESCRIPTOR.getPropertyId()).setValue("DESC");
	 * i.getItemProperty
	 * (EnvironmentItemProperty.ENVIRONMENT_STATE.getPropertyId
	 * ()).setValue("STATE"); EnvironmentsPresenter.counter++;
	 * view.refresh(); // OPEN POPUP }
	 */

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void refreshContent()
	{
		// Get project descriptors
		try
		{
			environments = getProjectEnvironments();

			if (environmentContainer == null)
			{
				environmentContainer = new EnvironmentContainer(environments);
				view.setEnvironments(environmentContainer);
			} else
			{
				if (environmentContainer.removeAllItems())
				{
					environmentContainer = new EnvironmentContainer(environments);
					view.setEnvironments(environmentContainer);
				}
			}

		} catch (NovadeployClientException e)
		{
			if (e.getResponse() != null)
			{
				Notification.show("Could not retrieve environment list. " + e.getResponse().getError(),
						Type.ERROR_MESSAGE);
				e.printStackTrace();
			} else
			{
				Notification.show("Internal Error", Type.ERROR_MESSAGE);
				e.printStackTrace();
			}
			view.stopRefresh();
		}
	}

	/**
	 * Return list of environments
	 * 
	 * @return
	 * @throws NovadeployClientException
	 */
	private CustomerEnvironment[] getProjectEnvironments() throws NovadeployClientException
	{
		return NovaDeployModule.getNovadeployRestClient().listEnvironments(novadeployClient,
				novadeployClient.getProjectId());
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
	@Override
	public Component getComponent()
	{
		return view;
	}

	public EnvironmentContainer getEnvironmentContainer()
	{
		return this.environmentContainer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLocalized(final Locale pLocale)
	{
		view.refreshLocale(pLocale);
	}

	public MBassador<PortalEvent> getEventBusFromPresenter()
	{
		return getEventBus();
	}
}
