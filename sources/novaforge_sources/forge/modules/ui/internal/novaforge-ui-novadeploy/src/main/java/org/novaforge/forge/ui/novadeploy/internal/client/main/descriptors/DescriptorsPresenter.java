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
package org.novaforge.forge.ui.novadeploy.internal.client.main.descriptors;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.novadeploy.internal.client.components.DescriptorEditor;
import org.novaforge.forge.ui.novadeploy.internal.client.event.DescriptorEditorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClient;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClientException;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.CustomerDescriptor;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.Role;
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
public class DescriptorsPresenter extends AbstractNovaDeployPresenter implements Serializable
{

	/**
	 * Serial version uid used for serialization
	 */
	private static final long serialVersionUID = 2704843315038570848L;

	/**
	 * Content of project view
	 */
	private DescriptorsView view;

	private static final Log LOGGER = LogFactory.getLog(DescriptorsPresenter.class);

	// Rest connector
	private NovadeployClient novadeployClient = null;

	private CustomerDescriptor[] descriptors = null;

	private boolean isTester = false;

	/**
	 * @param pView
	 * @param pPortalContext
	 */
	public DescriptorsPresenter(final DescriptorsView pView, final PortalContext pPortalContext,
			final NovadeployClient novaDeployClient)
	{
		super(pPortalContext);
		novadeployClient = novaDeployClient;
		view = pView;
		view.setEventBus(getEventBus());
		addListeners();
	}

	public void refreshDescriptors()
	{
		try
		{
			descriptors = getProjectDescriptors();
			view.setDescriptors(descriptors);
		} catch (NovadeployClientException e)
		{
			if (e.getResponse() != null)
			{
				Notification.show("Could not retrieve descriptors list. " + e.getResponse().getError(),
						Type.ERROR_MESSAGE);
				e.printStackTrace();
			} else
			{
				Notification.show("Internal Error", Type.ERROR_MESSAGE);
				e.printStackTrace();
			}

		}

	}

	public void refreshIsTester()
	{
		try
		{
			isTester = getIsTester();
			view.setIsTester(isTester);
		} catch (NovadeployClientException e)
		{
			if (e.getResponse() != null)
			{
				Notification.show("Unable to determine user role. " + e.getResponse().getError(), Type.ERROR_MESSAGE);
				e.printStackTrace();
			} else
			{
				Notification.show("Internal Error", Type.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}

	}

	private CustomerDescriptor[] getProjectDescriptors() throws NovadeployClientException
	{
		return NovaDeployModule.getNovadeployRestClient().getDescriptorsTree(novadeployClient,
				novadeployClient.getProjectId());
	}

	private boolean getIsTester() throws NovadeployClientException
	{
		String login = NovaDeployModule.getAuthentificationService().getCurrentUser();
		Role userRole = NovaDeployModule.getNovadeployRestClient()
				.getUserMembershipFromProject(novadeployClient, novadeployClient.getProjectId()).getUserRole();

		if (userRole.equals(Role.TESTER))
		{
			return true;
		} else
		{
			return false;
		}
	}

	/**
	 * It will add listeners to view components
	 */
	private void addListeners()
	{
		view.getNewDescriptorButton().addClickListener(new ClickListener()
		{
			/**
			 *
			 */
			private static final long serialVersionUID = -8481786932586211858L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				getEventBus().publish(new DescriptorEditorEvent(DescriptorEditor.Mode.NEW.toString(), null, 0, null));
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
		refreshIsTester();
		refreshDescriptors();
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
