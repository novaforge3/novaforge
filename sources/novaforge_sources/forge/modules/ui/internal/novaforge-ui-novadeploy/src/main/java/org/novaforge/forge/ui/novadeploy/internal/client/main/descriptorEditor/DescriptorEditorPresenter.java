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
package org.novaforge.forge.ui.novadeploy.internal.client.main.descriptorEditor;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.novadeploy.internal.client.components.DescriptorEditor;
import org.novaforge.forge.ui.novadeploy.internal.client.containers.EnvironmentFrom;
import org.novaforge.forge.ui.novadeploy.internal.client.event.ReturnFromDescriptorEditorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.exceptions.DescriptorEditorException;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClient;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClientException;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.DescriptorResponse;
import org.novaforge.forge.ui.novadeploy.internal.module.AbstractNovaDeployPresenter;
import org.novaforge.forge.ui.novadeploy.internal.module.NovaDeployModule;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

/**
 * @author Vincent Chenal
 */
public class DescriptorEditorPresenter extends AbstractNovaDeployPresenter implements Serializable
{

	/**
	 * Serial version uid used for serialization
	 */
	private static final long serialVersionUID = 2704843315038570848L;

	/**
	 * Content of project view
	 */
	private final DescriptorEditorView view;
	private String projectId;
	// Rest connector
	private NovadeployClient novadeployClient = null;

	private static final Log LOGGER = LogFactory.getLog(DescriptorEditorPresenter.class);

	/**
	 * @param pPortalContext
	 */
	public DescriptorEditorPresenter(final DescriptorEditorView pView, final PortalContext pPortalContext,
			NovadeployClient novadeployClient)
	{
		super(pPortalContext);
		view = pView;
		view.setEventBus(getEventBus());
		this.novadeployClient = novadeployClient;
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
				if (view.getEnvironmentFrom() != null)
				{
					getEventBus().publish(new ReturnFromDescriptorEditorEvent(view.getEnvironmentFrom()));
				} else
				{
					getEventBus().publish(new ReturnFromDescriptorEditorEvent(null));
				}
			}
		});
		view.getCreateButton().addClickListener(new ClickListener()
		{

			/**
			 *
			 */
			private static final long serialVersionUID = 8602837871287517469L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				JavaScript.getCurrent().execute("$('#serverUpdate').click();");
			}
		});
	}

	public boolean prepareEditor(DescriptorEditor.Mode mode, String descriptorName, int descriptorVersion,
			EnvironmentFrom environmentFrom)
	{
		if (mode.equals(DescriptorEditor.Mode.NEW))
		{
			try
			{
				view.prepareEditor(mode, null, 0, null, environmentFrom);
			} catch (DescriptorEditorException e)
			{
				e.printStackTrace();
				return false;
			}
		} else
		{
			DescriptorResponse descriptor = null;
			try
			{
				descriptor = NovaDeployModule.getNovadeployRestClient().getDescriptor(novadeployClient,
						descriptorVersion, descriptorName, novadeployClient.getProjectId());
				if (descriptor != null)
				{
					try
					{
						view.prepareEditor(mode, descriptorName, descriptorVersion, descriptor.content, environmentFrom);
					} catch (DescriptorEditorException e)
					{
						e.printStackTrace();
						return false;
					}
				}
				return true;
			} catch (NovadeployClientException e)
			{
				Notification.show("Could not retrieve descriptor " + descriptorName + "-" + descriptorVersion
						+ " content", Type.ERROR_MESSAGE);
				e.printStackTrace();
				return false;
			}
		}
		return true;
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

	public void setEnvironmentId(String projectId)
	{
		this.projectId = projectId;
	}

	public String getDescriptorContent()
	{
		return view.getDescriptorContent();
	}
}
