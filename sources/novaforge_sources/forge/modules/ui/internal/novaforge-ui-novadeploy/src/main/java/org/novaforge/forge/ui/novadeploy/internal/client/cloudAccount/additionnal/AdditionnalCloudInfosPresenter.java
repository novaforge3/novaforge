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
package org.novaforge.forge.ui.novadeploy.internal.client.cloudAccount.additionnal;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.novadeploy.internal.client.event.AccountActivatedEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClient;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClientException;
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
public class AdditionnalCloudInfosPresenter extends AbstractNovaDeployPresenter implements Serializable
{

	/**
	 * Serial version uid used for serialization
	 */
	private static final long serialVersionUID = 2704843315038570848L;

	/**
	 * Content of project view
	 */
	private final AdditionnalCloudInfosView view;

	private static final Log LOGGER = LogFactory.getLog(AdditionnalCloudInfosPresenter.class);

	private String[] vdcs;
	private String[] networks;

	private NovadeployClient novadeployClient;

	/**
	 * @param pPortalContext
	 * @param novadeployClient
	 */
	public AdditionnalCloudInfosPresenter(final AdditionnalCloudInfosView pView, final PortalContext pPortalContext,
			NovadeployClient novadeployClient)
	{
		super(pPortalContext);
		view = pView;
		addListeners();

		this.novadeployClient = novadeployClient;

	}

	public void setData(String[] cloudName, String[] login)
	{
		this.vdcs = cloudName;
		this.networks = login;
		view.setData(this.vdcs, this.networks);
	}

	/**
	 * It will add listeners to view components
	 */
	private void addListeners()
	{
		view.getValidateButton().addClickListener(new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				String chosenVdc = view.getChosenVdc();
				String chosenNetwork = view.getChosenNetwork();

				if (chosenNetwork != null && chosenVdc != null)
				{
					try
					{

						NovaDeployModule.getNovadeployRestClient().configCloud(novadeployClient,
								novadeployClient.getProjectId(), chosenVdc, chosenNetwork);
						getEventBus().publish(new AccountActivatedEvent());

					} catch (NovadeployClientException e)
					{
						if (e.getResponse() != null)
						{
							Notification.show("Could not validate the NovaDeploy account. "
									+ e.getResponse().getError(), Type.ERROR_MESSAGE);
							e.printStackTrace();
						} else
						{
							Notification.show("Internal Error" + e.getResponse().getError(), Type.ERROR_MESSAGE);
						}
					}
				} else
				{
					Notification.show("Please choose a value in each list", Type.ERROR_MESSAGE);
					return;
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
