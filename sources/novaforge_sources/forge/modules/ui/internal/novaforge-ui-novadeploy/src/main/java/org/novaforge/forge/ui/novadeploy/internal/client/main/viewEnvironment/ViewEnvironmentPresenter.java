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
package org.novaforge.forge.ui.novadeploy.internal.client.main.viewEnvironment;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.novadeploy.internal.client.components.DescriptorEditor;
import org.novaforge.forge.ui.novadeploy.internal.client.containers.EnvironmentFrom;
import org.novaforge.forge.ui.novadeploy.internal.client.event.DescriptorEditorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.ReturnFromViewEnvironmentEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClient;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClientException;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.CustomerEnvironment;
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
public class ViewEnvironmentPresenter extends AbstractNovaDeployPresenter implements Serializable
{

	/**
	 * Serial version uid used for serialization
	 */
	private static final long serialVersionUID = 2704843315038570848L;
	private long timeRefId;
	private String descriptorName;
	private CustomerEnvironment environment;
	private String[] logLines;
	/**
	 * Content of project view
	 */
	private final ViewEnvironmentView view;

	private static final Log LOGGER = LogFactory.getLog(ViewEnvironmentPresenter.class);

	private NovadeployClient novadeployClient;

	/**
	 * @param pPortalContext
	 */
	public ViewEnvironmentPresenter(final ViewEnvironmentView pView, final PortalContext pPortalContext,
			NovadeployClient novadeployClient)
	{
		super(pPortalContext);
		view = pView;
		this.novadeployClient = novadeployClient;
		view.setPresenter(this);
		addListeners();
	}

	/**
	 * It will add listeners to view components
	 */
	private void addListeners()
	{
		view.getReturnButton().addClickListener(new ClickListener()
		{
			/**
			 *
			 */
			private static final long serialVersionUID = -3480801975804104217L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				getEventBus().publish(new ReturnFromViewEnvironmentEvent());
			}
		});

		view.getShowDescriptorButton().addClickListener(new ClickListener()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -7425416089448208256L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				String[] descriptorParts = view.getDescriptorName().split("-");
				EnvironmentFrom environmentFrom = new EnvironmentFrom(view.getDescriptorName(), view.getEnvironmentId());

				getEventBus().publish(
						new DescriptorEditorEvent(DescriptorEditor.Mode.READ.toString(), descriptorParts[0], Integer
								.parseInt(descriptorParts[1]), environmentFrom));
			}
		});
	}

	public void prepareView(String descriptorName, long timeRefId)
	{
		this.timeRefId = timeRefId;
		this.descriptorName = descriptorName;
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

	private boolean fetchData()
	{
		try
		{
			logLines = NovaDeployModule.getNovadeployRestClient().deploymentStatus(novadeployClient, timeRefId);
			environment = NovaDeployModule.getNovadeployRestClient().getEnvironment(novadeployClient,
					novadeployClient.getProjectId(), timeRefId);

		} catch (NovadeployClientException e)
		{
			if (e.getResponse() != null)
			{
				Notification.show("Could not retrieve logs for environment " + timeRefId, Type.ERROR_MESSAGE);
				e.printStackTrace();
			} else
			{
				Notification.show("Internal Error", Type.ERROR_MESSAGE);
				e.printStackTrace();
			}
			view.stopRefresh();
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void refreshContent()
	{

		if (fetchData())
		{
			view.prepareView(descriptorName, timeRefId, logLines, environment);
		}

		view.refreshContent();
		view.refreshLocale(getCurrentLocale());
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
