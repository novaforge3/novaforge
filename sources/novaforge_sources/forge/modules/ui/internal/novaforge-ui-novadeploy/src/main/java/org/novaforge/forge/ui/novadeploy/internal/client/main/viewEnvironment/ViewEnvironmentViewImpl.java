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

import java.util.List;
import java.util.Locale;

import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.CustomerEnvironment;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.CustomerNode;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.EnvironmentState;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Vincent Chenal
 */
public class ViewEnvironmentViewImpl extends VerticalLayout implements ViewEnvironmentView
{

	private String descriptorName;
	private long environmentTimeRefId;
	private CustomerEnvironment environment;
	private ViewEnvironmentPresenter viewEnvironmentPresenter;
	private String[] logLines;

	/**
	 * Serialization id
	 */
	private static final long serialVersionUID = -8751426074045820735L;

	private HorizontalLayout hlInfos;
	private HorizontalLayout hlButtons;

	private Label labEnvironment;
	private Label labPhase;
	private Embedded envStatusIcon;
	private ProgressBar roulette;

	private Button butReturn;
	private Button butRelease;
	private Button butDelete;
	private Button butShowDescriptor;

	// NODES PANEL
	private Panel panelNodes;
	private Table tableNodes;

	// LOG PANEL
	private Panel panelLogs;
	private Table tableLogs;

	// Allow log panel refresh
	final Refresher refresher = new Refresher();

	/**
	 * Default constructor.
	 */
	public ViewEnvironmentViewImpl()
	{
		super();

		hlInfos = new HorizontalLayout();
		hlButtons = new HorizontalLayout();
		labEnvironment = new Label();
		labPhase = new Label();
		labPhase.setStyleName("environmentPhase");
		envStatusIcon = new Embedded(null, new ThemeResource(NovaForgeResources.PUCE_GREEN));
		roulette = new ProgressBar();
		roulette.setIndeterminate(true);

		butReturn = new Button();
		butRelease = new Button();
		butDelete = new Button();
		butShowDescriptor = new Button();

		// NODES PANEL
		panelNodes = new Panel();
		panelNodes.setSizeFull();

		tableNodes = new Table();
		tableNodes.setSizeFull();
		tableNodes.addContainerProperty("Name", String.class, null);
		tableNodes.addContainerProperty("DNS", Link.class, null);
		tableNodes.addContainerProperty("IP Adress", Link.class, null);

		// LOGS PANEL
		panelLogs = new Panel();
		panelLogs.setSizeFull();

		tableLogs = new Table();
		tableLogs.setSizeFull();
		tableLogs.addContainerProperty("Log event", String.class, null);

		initContent();
	}

	private void initContent()
	{
		this.setMargin(true);
		this.setSpacing(true);
		this.setStyleName("environmentView");
		this.addStyleName("isBordered");

		labEnvironment.setStyleName(NovaForge.LABEL_H1);
		labEnvironment.setWidth("600px");
		labPhase.setWidth("100px");
		envStatusIcon.setHeight("40px");
		envStatusIcon.setWidth("40px");
		butReturn.setStyleName(Reindeer.BUTTON_DEFAULT);
		butReturn.setIcon(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT));
		butRelease.setStyleName(Reindeer.BUTTON_DEFAULT);
		butDelete.setStyleName(Reindeer.BUTTON_DEFAULT);
		butShowDescriptor.setStyleName(Reindeer.BUTTON_DEFAULT);

		// Fill VerticalLayout
		hlInfos.addComponent(labEnvironment);
		hlInfos.setComponentAlignment(labEnvironment, Alignment.MIDDLE_LEFT);
		hlInfos.addComponent(labPhase);
		hlInfos.setComponentAlignment(labPhase, Alignment.MIDDLE_RIGHT);
		hlInfos.addComponent(envStatusIcon);
		hlInfos.setComponentAlignment(envStatusIcon, Alignment.MIDDLE_LEFT);
		hlInfos.addComponent(roulette);
		hlInfos.setComponentAlignment(roulette, Alignment.MIDDLE_RIGHT);

		hlButtons.setSizeFull();
		hlButtons.addComponent(butShowDescriptor);
		hlButtons.setComponentAlignment(butShowDescriptor, Alignment.MIDDLE_RIGHT);
		/*
		 * hlButtons.setComponentAlignment(butShowDescriptor,
		 * Alignment.MIDDLE_CENTER);
		 * hlButtons.addComponent(butRelease);
		 * hlButtons.setComponentAlignment(butRelease,
		 * Alignment.MIDDLE_CENTER);
		 * hlButtons.addComponent(butDelete);
		 * hlButtons.setComponentAlignment(butDelete,
		 * Alignment.MIDDLE_CENTER);
		 */

		this.addComponent(butReturn);
		this.addComponent(hlInfos);
		this.addComponent(hlButtons);
		this.addComponent(panelNodes);
		this.addComponent(panelLogs);
		this.setExpandRatio(panelLogs, 1);

		// the Refresher polls automatically

		refresher.setRefreshInterval(2000);
		refresher.addListener(new EnvironmentListener());
		addExtension(refresher);

		refreshLocale(getLocale());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void attach()
	{
		super.attach();
		refreshLocale(getLocale());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshLocale(final Locale pLocale)
	{
		this.butReturn.setCaption("Return");
		this.butRelease.setCaption("Release");
		this.butDelete.setCaption("Delete");
		this.butShowDescriptor.setCaption("Show Descriptor");

		// To implement
		/*
		 * returnButton.setCaption(MailingModule.getPortalMessages().
		 * getMessage(pLocale, Messages.MAILING_LISTS_ADD_BACK));
		 * 
		 * mailingListForm.setCaption(MailingModule.getPortalMessages()
		 * .getMessage(pLocale, Messages.MAILING_LISTS_ADD_TITLE)); if
		 * (mailingListFormFieldFactory.getName() != null) {
		 * mailingListFormFieldFactory.getName().setCaption(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME));
		 * mailingListFormFieldFactory.getName().setDescription(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME_DESCRIPTION));
		 * mailingListFormFieldFactory.getName().setRequiredError(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME_REQUIRED) + " " +
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME_REGEXP));
		 * mailingListFormFieldFactory
		 * .getName().removeAllValidators();
		 * mailingListFormFieldFactory.getName().addValidator( new
		 * ListnameValidator
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME_REtableLogsGEXP)));
		 * mailingListFormFieldFactory.getName().addValidator( new
		 * StringLengthValidator
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_NAME_REQUIRED), 3, 40, false)); } if
		 * (mailingListFormFieldFactory.getSubject() != null) {
		 * mailingListFormFieldFactory.getSubject().setCaption(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_SUBJECT));
		 * mailingListFormFieldFactory.getSubject().setRequiredError(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_SUBJECT_REQUIRED));
		 * mailingListFormFieldFactory
		 * .getSubject().removeAllValidators();
		 * mailingListFormFieldFactory.getSubject().addValidator( new
		 * StringLengthValidator
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_SUBJECT_REQUIRED), 3, 40, false)); }
		 * if (mailingListFormFieldFactory.getDescription() != null) {
		 * mailingListFormFieldFactory.getDescription().setCaption(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MEMBERSHIPS_ROLES_DESCRIPTION));
		 * mailingListFormFieldFactory
		 * .getDescription().setRequiredError(
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_DESCRIPTION_REQUIRED));
		 * mailingListFormFieldFactory
		 * .getDescription().removeAllValidators();
		 * mailingListFormFieldFactory.getDescription().addValidator(
		 * new
		 * StringLengthValidator(MailingModule.getPortalMessages().
		 * getMessage(pLocale,
		 * Messages.MAILING_LISTS_DESCRIPTION_REQUIRED), 3, 250,
		 * false)); } if (mailingListFormFieldFactory.getType() !=
		 * null) { final ComboBox typeBox =
		 * mailingListFormFieldFactory.getType();
		 * typeBox.setCaption(MailingModule
		 * .getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE));
		 * 
		 * typeBox.addItem(MailingListType.HOTLINE_LIST);
		 * typeBox.setItemCaption(MailingListType.HOTLINE_LIST,
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE_HOTLINE));
		 * typeBox.addItem(MailingListType.PRIVATE_LIST);
		 * typeBox.setItemCaption(MailingListType.PRIVATE_LIST,
		 * MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE_PRIVATE));
		 * typeBox.setInputPrompt
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE_INPUTPROMPT));
		 * typeBox.setRequiredError
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE_REQUIRED));
		 * typeBox.setDescription
		 * (MailingModule.getPortalMessages().getMessage(pLocale,
		 * Messages.MAILING_LISTS_TYPE_DESCRIPTION)); }
		 * 
		 * addButton.setCaption(MailingModule.getPortalMessages().
		 * getMessage(pLocale, Messages.MAILING_LISTS_ADD_CONFIRM));
		 * cancelButton
		 * .setCaption(MailingModule.getPortalMessages().getMessage
		 * (pLocale, Messages.MAILING_LISTS_ADD_CANCEL));
		 */
	}

	public class EnvironmentListener implements RefreshListener
	{
		private static final long serialVersionUID = -8765221895426102605L;

		@Override
		public void refresh(final Refresher source)
		{

			viewEnvironmentPresenter.refreshContent();
			// stop polling
			if (environment.getEnvironmentState() == EnvironmentState.FAILED
					|| environment.getEnvironmentState() == EnvironmentState.DEPLOYED)
			{
				removeExtension(source);
			}
		}
	}

	@Override
	public void refreshContent()
	{
		refreshLogPanel(logLines);
		refreshNodesPanel(environment.getNodeList());
		labEnvironment.setValue(this.descriptorName + "." + this.environmentTimeRefId);
		labPhase.setValue(this.environment.getEnvironmentState().toString());

		switch (this.environment.getEnvironmentState())
		{
		case CHECK:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_GREY));
			roulette.setVisible(true);
			break;
		case CONFIGURATION:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_ORANGE));
			roulette.setVisible(true);
			break;
		case DEPLOYED:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_GREEN));
			roulette.setVisible(false);
			break;
		case FAILED:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_RED));
			roulette.setVisible(false);
			break;
		case LIFECYCLE:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_ORANGE));
			roulette.setVisible(true);
			break;

		case PROVISIONING:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_YELLOW));
			roulette.setVisible(true);
			break;
		default:
			envStatusIcon.setSource(new ThemeResource(NovaForgeResources.PUCE_RED));
			roulette.setVisible(false);
		}
	}

	@Override
	public Button getReturnButton()
	{
		return this.butReturn;
	}

	private void fillNodeTable(List<CustomerNode> nodes)
	{
		tableNodes.removeAllItems();
		int lineNumber = 0;
		if (nodes != null)
		{
			for (CustomerNode node : nodes)
			{
				Link link1 = new Link(node.getDNS() + ".nd", new ExternalResource("http://" + node.getDNS() + ".nd"));
				link1.setTargetName("_blank");
				Link link2 = new Link(node.getIp(), new ExternalResource("http://" + node.getIp()));
				link2.setTargetName("_blank");
				tableNodes.addItem(new Object[]
				{ node.getNodeName(), link1, link2 }, "" + lineNumber++);
			}
			tableNodes.setPageLength(tableNodes.size());
		}
	}

	private void refreshNodesPanel(List<CustomerNode> nodes)
	{
		panelNodes.setCaption("Nodes");
		fillNodeTable(nodes);
		panelNodes.setContent(tableNodes);
	}

	private void refreshLogPanel(String[] logLines)
	{
		panelLogs.setCaption("Environment Logs");
		int nb = tableLogs.size();
		if (nb == 0 || nb != logLines.length)
		{
			fillLogTable(logLines);
			panelLogs.setContent(tableLogs);
		}
	}

	private void fillLogTable(String[] logLines)
	{
		tableLogs.removeAllItems();
		int lineNumber = 0;
		if (logLines != null)
		{
			for (String line : logLines)
			{
				tableLogs.addItem(new Object[]
				{ line }, "" + lineNumber++);
			}
		}
	}

	@Override
	public void prepareView(String descriptorName, long timeRefId, String[] logLines, CustomerEnvironment env)
	{
		environment = env;
		this.descriptorName = descriptorName;
		this.environmentTimeRefId = timeRefId;
		this.logLines = logLines;
	}

	@Override
	public long getEnvironmentId()
	{
		return this.environmentTimeRefId;
	}

	@Override
	public void setPresenter(ViewEnvironmentPresenter viewEnvironmentPresenter)
	{
		this.viewEnvironmentPresenter = viewEnvironmentPresenter;
	}

	@Override
	public Button getShowDescriptorButton()
	{
		return this.butShowDescriptor;
	}

	@Override
	public String getDescriptorName()
	{
		return this.descriptorName;
	}

	@Override
	public void stopRefresh()
	{
		this.removeExtension(this.refresher);
	}
}
