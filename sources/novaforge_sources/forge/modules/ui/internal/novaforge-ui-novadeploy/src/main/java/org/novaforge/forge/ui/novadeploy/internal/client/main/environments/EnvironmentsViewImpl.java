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

import java.util.Locale;

import org.novaforge.forge.ui.novadeploy.internal.client.components.EnvironmentBoxGenerator;
import org.novaforge.forge.ui.novadeploy.internal.client.containers.EnvironmentContainer;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Vincent Chenal
 */
public class EnvironmentsViewImpl extends VerticalLayout implements EnvironmentsView
{

	/**
	 * Serialization id
	 */
	private static final long serialVersionUID = -8751426074045820735L;

	/**
	 * Default constructor.
	 */

	private VerticalLayout environmentsLayout;
	private Label messageLabel;
	private EnvironmentBoxGenerator environmentBoxGenerator;
	private EnvironmentsPresenter environmentsPresenter;

	// Allow page refreh
	final Refresher refresher = new Refresher();

	public EnvironmentsViewImpl()
	{
		super();
		initContent();
	}

	private void initContent()
	{
		this.setSizeUndefined();
		this.setWidth("100%");
		this.setMargin(true);

		messageLabel = new Label("Default message");
		messageLabel.setStyleName("environmentListlabel");
		messageLabel.setHeight("50px");

		environmentsLayout = new VerticalLayout();
		environmentsLayout.setMargin(true);
		environmentsLayout.setSpacing(true);

		// Refresh captions according to current locale
		refreshLocale(getLocale());
		this.addComponent(messageLabel);
		this.setComponentAlignment(messageLabel, Alignment.MIDDLE_CENTER);
		this.addComponent(environmentsLayout);
		setComponentAlignment(environmentsLayout, Alignment.MIDDLE_CENTER);

		// the Refresher polls automatically

		refresher.setRefreshInterval(5000);
		refresher.addListener(new EnvironmentsListener());
		addExtension(refresher);
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

	public class EnvironmentsListener implements RefreshListener
	{
		private static final long serialVersionUID = -8765221895426102605L;

		@Override
		public void refresh(final Refresher source)
		{

			// stop polling
			// removeExtension(source);
			environmentsPresenter.refreshContent();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshLocale(final Locale pLocale)
	{
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
		 * Messages.MAILING_LISTS_NAME_REGEXP)));
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
		 * MailingModule
		 * .getPortalMessages().getMessage(pLmessageocale,
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

	@Override
	public void refresh()
	{
		environmentsLayout.markAsDirty();
	}

	@Override
	public VerticalLayout getLayout()
	{
		return environmentsLayout;
	}

	@Override
	public void setEnvironments(EnvironmentContainer environmentContainer)
	{
		environmentsLayout.removeAllComponents();
		String message;
		if (environmentBoxGenerator == null)
		{
			environmentBoxGenerator = new EnvironmentBoxGenerator(environmentsPresenter);
		}
		if (environmentContainer.getItemIds().size() != 0)
		{
			message = "Environment list";
		} else
		{
			message = "There is no environment yet";
		}
		messageLabel.setValue(message);

		HorizontalLayout horizontal = null;

		for (int i = 0; i < environmentContainer.getItemIds().size(); i++)
		{
			Object itemId = environmentContainer.getItemIds().get(i);

			Component c = environmentBoxGenerator.generateItem(environmentContainer.getItem(itemId));

			if (i % 2 == 0)
			{
				horizontal = new HorizontalLayout();
				horizontal.addComponent(c);

				// Add the horizontalLayout even if it contains a
				// unique environment
				if ((i + 1) == environmentContainer.getItemIds().size())
				{
					horizontal.addComponent(new Label(""));
					this.environmentsLayout.addComponent(horizontal);
				}
			} else
			{
				horizontal.addComponent(c);
				this.environmentsLayout.addComponent(horizontal);
			}
		}
	}

	@Override
	public void setPresenter(EnvironmentsPresenter environmentPresenter)
	{
		this.environmentsPresenter = environmentPresenter;
	}

	@Override
	public void stopRefresh()
	{
		this.removeExtension(this.refresher);
	}

}
