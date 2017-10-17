/**
 * NovaForge(TM) is a web-based forge offering a Collaborative
 * Development and Project Management Environment.
 *
 * Copyright (C) 2007-2012 BULL SAS
 *
 * This program is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.ui.novadeploy.internal.client.main;

import java.io.Serializable;
import java.util.Locale;

import net.engio.mbassy.listener.Handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.novadeploy.internal.client.components.DescriptorEditor;
import org.novaforge.forge.ui.novadeploy.internal.client.event.CreateDescriptorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.DeleteEnvironmentEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.DeployEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.DescriptorEditorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.EditDescriptorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.ForkEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.ReleaseDescriptorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.ReturnFromDescriptorEditorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.ReturnFromViewEnvironmentEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.ViewEnvironmentEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.main.descriptorEditor.DescriptorEditorPresenter;
import org.novaforge.forge.ui.novadeploy.internal.client.main.descriptorEditor.DescriptorEditorViewImpl;
import org.novaforge.forge.ui.novadeploy.internal.client.main.descriptors.DescriptorsPresenter;
import org.novaforge.forge.ui.novadeploy.internal.client.main.descriptors.DescriptorsViewImpl;
import org.novaforge.forge.ui.novadeploy.internal.client.main.environments.EnvironmentsPresenter;
import org.novaforge.forge.ui.novadeploy.internal.client.main.environments.EnvironmentsViewImpl;
import org.novaforge.forge.ui.novadeploy.internal.client.main.overview.OverviewPresenter;
import org.novaforge.forge.ui.novadeploy.internal.client.main.overview.OverviewViewImpl;
import org.novaforge.forge.ui.novadeploy.internal.client.main.viewEnvironment.ViewEnvironmentPresenter;
import org.novaforge.forge.ui.novadeploy.internal.client.main.viewEnvironment.ViewEnvironmentViewImpl;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClient;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.NovadeployClientException;
import org.novaforge.forge.ui.novadeploy.internal.module.AbstractNovaDeployPresenter;
import org.novaforge.forge.ui.novadeploy.internal.module.NovaDeployModule;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Vincent Chenal
 */
public class GlobalPresenter extends AbstractNovaDeployPresenter implements Serializable
{
	/**
	 * Serial version uid used for serialization
	 */
	private static final long serialVersionUID = -5042299647493799344L;
	/**
	 * Content of project view
	 */
	private final GlobalView view;

	private static final Log LOGGER = LogFactory.getLog(DescriptorEditorPresenter.class);
	/**
	 * This variable contains the project Id associated to this
	 * presenter
	 */
	private final OverviewPresenter overviewPresenter;
	private final EnvironmentsPresenter environmentsPresenter;
	private final DescriptorsPresenter descriptorsPresenter;
	private final DescriptorEditorPresenter descriptorEditorPresenter;
	private final ViewEnvironmentPresenter viewEnvironmentPresenter;

	// Rest connector
	private NovadeployClient novadeployClient = null;

	/**
	 * Default constructor. It will initialize the tree component
	 * associated to the view and bind some events.
	 *
	 * @param pView
	 *            the view
	 * @param pEventBus
	 *            the event bus
	 * @param pProjectId
	 *            project id
	 */
	public GlobalPresenter(final GlobalView pView, final PortalContext pPortalContext,
			final NovadeployClient novaDeployClient)
	{
		super(pPortalContext);
		view = pView;
		overviewPresenter = new OverviewPresenter(new OverviewViewImpl(), pPortalContext, novaDeployClient);
		environmentsPresenter = new EnvironmentsPresenter(new EnvironmentsViewImpl(), pPortalContext, novaDeployClient);
		descriptorsPresenter = new DescriptorsPresenter(new DescriptorsViewImpl(), pPortalContext, novaDeployClient);
		descriptorEditorPresenter = new DescriptorEditorPresenter(new DescriptorEditorViewImpl(), pPortalContext,
				novaDeployClient);
		viewEnvironmentPresenter = new ViewEnvironmentPresenter(new ViewEnvironmentViewImpl(), pPortalContext,
				novaDeployClient);
		novadeployClient = novaDeployClient;

		addListeners();
	}

	/**
	 * It will add listeners to view components
	 */
	public void addListeners()
	{
		view.getOverview().addClickListener(new ClickListener()
		{
			/**
			 *
			 */
			private static final long serialVersionUID = -2595177034832916112L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				view.setSecondComponent(overviewPresenter.getComponent());
				overviewPresenter.refresh();
			}
		});

		view.getEnvironments().addClickListener(new ClickListener()
		{
			/**
			 *
			 */
			private static final long serialVersionUID = 1438858381030540140L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				view.setSecondComponent(environmentsPresenter.getComponent());
				environmentsPresenter.refresh();
			}
		});

		view.getDescriptors().addClickListener(new ClickListener()
		{
			/**
			 *
			 */
			private static final long serialVersionUID = 7144374309963340436L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				view.setSecondComponent(descriptorsPresenter.getComponent());
				descriptorsPresenter.refresh();
			}
		});
	}

	@Handler
	public void onDeleteEnvironmentEvent(final DeleteEnvironmentEvent pEvent)
	{
		final Window w = new Window();
		VerticalLayout vl = new VerticalLayout();

		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);

		final Button butConfirm = new Button();
		butConfirm.setCaption("Confirm");
		butConfirm.setStyleName(Reindeer.BUTTON_DEFAULT);

		Button butCancel = new Button();
		butCancel.setCaption("Cancel");
		butCancel.setStyleName(Reindeer.BUTTON_DEFAULT);

		hl.addComponent(butCancel);
		hl.addComponent(butConfirm);
		hl.setWidth("100%");
		hl.setExpandRatio(butConfirm, 0.2f);
		hl.setComponentAlignment(butCancel, Alignment.MIDDLE_RIGHT);

		Form f = new Form();

		f.setDescription("<h3>Delete environment " + pEvent.getDescriptorName() + "-" + pEvent.getTimeRefId()
				+ "</h3><p>Do you really want to delete this environment ? </p>");

		f.getLayout().addComponent(hl);

		f.addShortcutListener(new ShortcutListener("Validate by pressing enter", ShortcutAction.KeyCode.ENTER, null)
		{
			@Override
			public void handleAction(Object sender, Object target)
			{
				butConfirm.click();
			}
		});

		butConfirm.addClickListener(new ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				try
				{
					NovaDeployModule.getNovadeployRestClient().deleteEnvironment(novadeployClient,
							novadeployClient.getProjectId(), pEvent.getTimeRefId());
					w.close();
					Notification.show("Environment has been successfully deleted", Type.TRAY_NOTIFICATION);

				} catch (NovadeployClientException e)
				{
					if (e.getResponse() != null)
					{
						w.close();
						Notification.show("Could not delete environment. " + e.getResponse().getError(),
								Type.ERROR_MESSAGE);
					} else
					{
						w.close();
						Notification.show("Internal Error", Type.ERROR_MESSAGE);
					}
				}
			}
		});

		butCancel.addClickListener(new ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				w.close();
			}
		});

		vl.addComponent(f);
		vl.setMargin(true);

		w.setContent(vl);
		w.setSizeUndefined();
		w.setResizable(false);
		w.setModal(true);
		w.setDraggable(false);

		UI.getCurrent().addWindow(w);
	}

	@Handler
	public void onViewEnvironmentEvent(final ViewEnvironmentEvent pEvent)
	{
		viewEnvironmentPresenter.prepareView(pEvent.getDescriptorName(), pEvent.getTimeRefId());
		view.setSecondComponent(viewEnvironmentPresenter.getComponent());
		viewEnvironmentPresenter.refresh();
	}

	@Handler
	public void onDescriptorEditorEvent(final DescriptorEditorEvent pEvent)
	{
		boolean ok = descriptorEditorPresenter.prepareEditor(
				DescriptorEditor.Mode.fromString(pEvent.getDescriptorWindowMode()), pEvent.getDescriptorName(),
				pEvent.getDescriptorVersion(), pEvent.getEnvironmentFrom());

		if (ok)
		{
			view.setSecondComponent(descriptorEditorPresenter.getComponent());
			descriptorEditorPresenter.refresh();
		}
	}

	@Handler
	public void onCreateDescriptorEvent(final CreateDescriptorEvent pEvent)
	{
		try
		{
			NovaDeployModule.getNovadeployRestClient().createDescriptor(novadeployClient, pEvent.getDescriptorName(),
					descriptorEditorPresenter.getDescriptorContent(), novadeployClient.getProjectId());
			Notification.show("Descriptor " + pEvent.getDescriptorName() + " has been correctly created",
					Type.TRAY_NOTIFICATION);
			view.setSecondComponent(descriptorsPresenter.getComponent());
			descriptorsPresenter.refresh();
		} catch (NovadeployClientException e)
		{
			if (e.getResponse() != null)
			{
				if (e.getResponse().getError() != null)
				{
					Notification.show("Could not create descriptor. " + e.getResponse().getError(), Type.ERROR_MESSAGE);
				} else
				{// Maybe a parsing error
					if (e.getResponse().getParserMessageError() != null)
					{
						Notification.show("Could not create descriptor. " + e.getResponse().getParserMessageError()
								+ "[" + e.getResponse().getParserLineError() + ","
								+ e.getResponse().getParserColumnError() + "]", Type.ERROR_MESSAGE);
					}
				}
			} else
			{
				Notification.show("Internal Error", Type.ERROR_MESSAGE);
			}
		}
	}

	@Handler
	public void onDeployEvent(final DeployEvent pEvent)
	{
		final Window w = new Window();
		VerticalLayout vl = new VerticalLayout();

		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);

		final Button butConfirm = new Button();
		butConfirm.setCaption("Confirm");
		butConfirm.setStyleName(Reindeer.BUTTON_DEFAULT);

		Button butCancel = new Button();
		butCancel.setCaption("Cancel");
		butCancel.setStyleName(Reindeer.BUTTON_DEFAULT);

		hl.addComponent(butCancel);
		hl.addComponent(butConfirm);

		Form f = new Form();

		if (pEvent.getDescriptorVersion() != 0)
		{
			f.setDescription("<h3>Deploy descriptor " + pEvent.getDescriptorName() + "-"
					+ pEvent.getDescriptorVersion() + "</h3><p>Do you really want to deploy this descriptor ? </p>");
		} else
		{
			f.setDescription("<h3>Deploy descriptor " + pEvent.getDescriptorName()
					+ "-current</h3><p>Do you really want to deploy this descriptor ? </p>");
		}

		f.getLayout().addComponent(hl);

		f.addShortcutListener(new ShortcutListener("Validate by pressing enter", ShortcutAction.KeyCode.ENTER, null)
		{

			@Override
			public void handleAction(Object sender, Object target)
			{
				butConfirm.click();
			}
		});

		butConfirm.addClickListener(new ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				try
				{
					long timeRefId = NovaDeployModule.getNovadeployRestClient().deployEnvironment(novadeployClient,
							pEvent.getDescriptorName(), pEvent.getDescriptorVersion(), novadeployClient.getProjectId());
					w.close();
					Notification.show(
							"Start deploying descriptor : " + pEvent.getDescriptorName() + "-"
									+ pEvent.getDescriptorVersion(), Type.TRAY_NOTIFICATION);
					view.getEnvironments().click();

				} catch (NovadeployClientException e)
				{
					if (e.getResponse() != null)
					{
						w.close();
						Notification.show(
								"Could not deploy descriptor " + pEvent.getDescriptorName() + "-"
										+ pEvent.getDescriptorVersion() + ". " + e.getResponse().getError(),
								Type.ERROR_MESSAGE);
					} else
					{
						w.close();
						Notification.show("Internal Error", Type.ERROR_MESSAGE);
					}

				}
			}
		});

		butCancel.addClickListener(new ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				w.close();
			}
		});

		vl.addComponent(f);
		vl.setMargin(true);

		w.setContent(vl);
		w.setSizeUndefined();
		w.setResizable(false);
		w.setModal(true);
		w.setDraggable(false);

		UI.getCurrent().addWindow(w);
	}

	@Handler
	public void onReleaseDescriptorEvent(final ReleaseDescriptorEvent pEvent)
	{
		final Window w = new Window();
		VerticalLayout vl = new VerticalLayout();

		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);

		final Button butConfirm = new Button();
		butConfirm.setCaption("Confirm");
		butConfirm.setStyleName(Reindeer.BUTTON_DEFAULT);

		Button butCancel = new Button();
		butCancel.setCaption("Cancel");
		butCancel.setStyleName(Reindeer.BUTTON_DEFAULT);

		hl.addComponent(butCancel);
		hl.addComponent(butConfirm);

		Form f = new Form();

		if (pEvent.getDescriptorVersion() != 0)
		{
			f.setDescription("<h3>Release descriptor " + pEvent.getDescriptorName() + "-"
					+ pEvent.getDescriptorVersion() + "</h3><p>Do you really want to release this descriptor ? </p>");
		} else
		{
			f.setDescription("<h3>Release descriptor " + pEvent.getDescriptorName()
					+ "-current</h3><p>Do you really want to release this descriptor ? </p>");
		}

		f.getLayout().addComponent(hl);

		f.addShortcutListener(new ShortcutListener("Validate by pressing enter", ShortcutAction.KeyCode.ENTER, null)
		{

			@Override
			public void handleAction(Object sender, Object target)
			{
				butConfirm.click();
			}
		});

		butConfirm.addClickListener(new ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				try
				{
					NovaDeployModule.getNovadeployRestClient().validateDescriptor(novadeployClient,
							novadeployClient.getProjectId(), pEvent.getDescriptorName());
					if (pEvent.getDescriptorVersion() == 0)
					{
						Notification.show("Descriptor " + pEvent.getDescriptorName()
								+ "-current has been correctly released", Type.TRAY_NOTIFICATION);
					} else
					{
						Notification.show(
								"Descriptor " + pEvent.getDescriptorName() + "-" + pEvent.getDescriptorVersion()
										+ " has been correctly released", Type.TRAY_NOTIFICATION);
					}
					w.close();
					view.setSecondComponent(descriptorsPresenter.getComponent());
					descriptorsPresenter.refresh();
				} catch (NovadeployClientException e)
				{
					if (e.getResponse() != null)
					{
						w.close();
						Notification.show("Could not release descriptor " + pEvent.getDescriptorName() + ". "
								+ e.getResponse().getError(), Type.ERROR_MESSAGE);
					} else
					{
						w.close();
						Notification.show("Internal Error", Type.ERROR_MESSAGE);
					}
				}
			}
		});

		butCancel.addClickListener(new ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				w.close();
			}
		});

		vl.addComponent(f);
		vl.setMargin(true);

		w.setContent(vl);
		w.setSizeUndefined();
		w.setResizable(false);
		w.setModal(true);
		w.setDraggable(false);

		UI.getCurrent().addWindow(w);
	}

	@Handler
	public void onForkEvent(final ForkEvent pEvent)
	{
		final Window w = new Window();
		VerticalLayout vl = new VerticalLayout();

		final TextField txtForkName = new TextField();
		txtForkName.setCaption("Fork Name");

		HorizontalLayout hl = new HorizontalLayout();
		hl.setSpacing(true);

		final Button butFork = new Button();
		butFork.setCaption("Fork");
		butFork.setStyleName(Reindeer.BUTTON_DEFAULT);

		Button butCancel = new Button();
		butCancel.setCaption("Cancel");
		butCancel.setStyleName(Reindeer.BUTTON_DEFAULT);

		hl.addComponent(butCancel);
		hl.addComponent(butFork);

		Form f = new Form();

		if (pEvent.getDescriptorVersion() != 0)
		{
			f.setDescription("<h3>Fork descriptor " + pEvent.getDescriptorName() + "-" + pEvent.getDescriptorVersion()
					+ "</h3><p>Please enter the new descriptor name : </p>");
		} else
		{
			f.setDescription("<h3>Fork descriptor " + pEvent.getDescriptorName()
					+ "-current</h3><p>Please enter the new descriptor name : </p>");
		}

		f.getLayout().addComponent(txtForkName);
		f.getLayout().addComponent(hl);

		f.addShortcutListener(new ShortcutListener("Validate by pressing enter", ShortcutAction.KeyCode.ENTER, null)
		{

			@Override
			public void handleAction(Object sender, Object target)
			{
				butFork.click();
			}
		});

		butFork.addClickListener(new ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				try
				{
					if (txtForkName.getValue().equals(""))
					{
						Notification.show("You can't set an empty name on a descriptor", Type.ERROR_MESSAGE);
						return;
					}
					NovaDeployModule.getNovadeployRestClient().forkDescriptor(novadeployClient,
							novadeployClient.getProjectId(), pEvent.getDescriptorName(), pEvent.getDescriptorVersion(),
							txtForkName.getValue());
					Notification.show("Descriptor " + pEvent.getDescriptorName() + "-" + pEvent.getDescriptorVersion()
							+ " has been correctly forked", Type.TRAY_NOTIFICATION);
					w.close();
					view.setSecondComponent(descriptorsPresenter.getComponent());
					descriptorsPresenter.refresh();
				} catch (NovadeployClientException e)
				{
					if (e.getResponse() != null)
					{
						Notification.show("Could not fork descriptor " + pEvent.getDescriptorName() + ". "
								+ e.getResponse().getError(), Type.ERROR_MESSAGE);
						e.printStackTrace();
					} else
					{
						Notification.show("Internal Error", Type.ERROR_MESSAGE);
					}
				}
			}
		});

		butCancel.addClickListener(new ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				w.close();
			}
		});

		vl.addComponent(f);
		vl.setMargin(true);

		w.setContent(vl);
		w.setSizeUndefined();
		w.setResizable(false);
		w.setModal(true);
		w.setDraggable(false);

		UI.getCurrent().addWindow(w);
	}

	@Handler
	public void onEditDescriptorEvent(final EditDescriptorEvent pEvent)
	{
		try
		{
			NovaDeployModule.getNovadeployRestClient().updateDescriptor(novadeployClient, pEvent.getDescriptorName(),
					descriptorEditorPresenter.getDescriptorContent(), novadeployClient.getProjectId());
			Notification.show("Descriptor " + pEvent.getDescriptorName() + " has been correctly edited",
					Type.TRAY_NOTIFICATION);

			view.setSecondComponent(descriptorsPresenter.getComponent());
		} catch (NovadeployClientException e)
		{
			Notification.show("Could not edit descriptor " + pEvent.getDescriptorName(), Type.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@Handler
	public void onReturnFromDescriptorEditorEvent(final ReturnFromDescriptorEditorEvent pEvent)
	{
		if (pEvent.getEnvironmentFrom() != null)
		{
			viewEnvironmentPresenter.prepareView(pEvent.getEnvironmentFrom().getDescriptorName(), pEvent
					.getEnvironmentFrom().getTimeRefId());
			view.setSecondComponent(viewEnvironmentPresenter.getComponent());
			viewEnvironmentPresenter.refresh();
		} else
		{
			view.setSecondComponent(descriptorsPresenter.getComponent());
			descriptorsPresenter.refresh();
		}
	}

	@Handler
	public void onReturnFromViewEvironmentEvent(final ReturnFromViewEnvironmentEvent pEvent)
	{
		view.setSecondComponent(environmentsPresenter.getComponent());
		viewEnvironmentPresenter.refresh();
	}

	/**
	 * Will refresh the project information.
	 */
	public void refresh()
	{
		// DEFAULT VIEW
		view.getOverview().addStyleName(NovaForge.SELECTED);
		view.setSecondComponent(overviewPresenter.getComponent());
		overviewPresenter.refresh();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshLocalized(final Locale pLocale)
	{
		view.refreshLocale(pLocale);

	}

	/**
	 * Callback on {@link ShowAddUserMemberViewEvent}
	 *
	 * @param pEvent
	 *            source event
	 */

	/*
	 * @Handler public void onAddUserEvent(final
	 * ShowAddUserMemberViewEvent pEvent) {
	 * view.getSplitPanel().setSecondComponent
	 * (addUsersPresenter.getComponent());
	 * addUsersPresenter.refresh(); }
	 */
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getComponent()
	{
		return view;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void refreshContent()
	{
		// Doesn't handle it by default
	}

}
