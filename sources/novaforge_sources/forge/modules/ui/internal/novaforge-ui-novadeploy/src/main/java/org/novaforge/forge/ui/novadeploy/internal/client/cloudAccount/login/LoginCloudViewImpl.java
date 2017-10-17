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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Vincent Chenal
 */
public class LoginCloudViewImpl extends VerticalLayout implements LoginCloudView
{

	/**
	 *
	 */
	private static final long serialVersionUID = -8569071199821902811L;
	private Form form;
	private TextField txtCloudName;
	private TextField txtLogin;
	private PasswordField txtPassword;
	private Button butOK;
	private HorizontalLayout buttonBar;

	private String cloudName;
	private String login;
	private String password;

	/**
	 * Default constructor.
	 */
	public LoginCloudViewImpl()
	{
		super();

		PrintWriter out = null;
		try
		{
			out = new PrintWriter(new BufferedWriter(new FileWriter("/tmp/vaadinlog", true)));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if (out != null)
			out.println("Login Constructor");

		cloudName = null;
		login = null;
		password = null;

		form = new Form();

		txtCloudName = new TextField();
		txtLogin = new TextField();
		txtPassword = new PasswordField();

		butOK = new Button();
		butOK.setStyleName(Reindeer.BUTTON_DEFAULT);

		buttonBar = new HorizontalLayout();
		initContent();
	}

	private void initContent()
	{
		this.setSizeFull();
		// Refresh captions according to current locale
		refreshLocale(getLocale());

		// Textfields
		txtCloudName.setRequired(true);
		txtLogin.setRequired(true);
		txtPassword.setRequired(true);

		// Build Form Layout
		form.getLayout().addComponent(txtCloudName);
		form.getLayout().addComponent(txtLogin);
		form.getLayout().addComponent(txtPassword);

		form.addShortcutListener(new ShortcutListener("Validate by pressing enter", ShortcutAction.KeyCode.ENTER, null)
		{

			@Override
			public void handleAction(Object sender, Object target)
			{
				butOK.click();
			}
		});

		form.setWidth("30%");

		VerticalLayout footer = new VerticalLayout();
		footer.setWidth("100%");
		footer.addComponent(buttonBar);

		buttonBar.setHeight("25px");
		buttonBar.setWidth("100%");
		buttonBar.addComponent(this.butOK);
		buttonBar.setComponentAlignment(this.butOK, Alignment.TOP_RIGHT);

		form.setFooter(footer);
		this.addComponent(form);
		this.setComponentAlignment(form, Alignment.MIDDLE_CENTER);
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
		// To implement
		txtCloudName.setCaption("Cloud Name");
		txtCloudName.setDescription("This corresponds to your Cloud Account Name on vCloud ");

		txtLogin.setCaption("Login");
		txtLogin.setDescription("This is your vCloud account login");

		txtPassword.setCaption("Password");
		txtPassword.setDescription("This is your vCloud account password");

		butOK.setCaption("Connect");

		form.setDescription("<h2>Cloud Login</h2><p>Please connect to your vCloud Account.</p><p>To get more information on fields, just place your mouse over it</p>");
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

	@Override
	public Button getOKButton()
	{
		return this.butOK;
	}

	@Override
	public void refreshContent()
	{
		refreshLocale(getLocale());

		PrintWriter out = null;
		try
		{
			out = new PrintWriter(new BufferedWriter(new FileWriter("/tmp/vaadinlog", true)));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		if (out != null)
			out.println("Refresh content login");

		// Fill pre-filled fields
		if (cloudName != null)
			txtCloudName.setValue(cloudName);
		if (login != null)
			txtLogin.setValue(cloudName);
		if (password != null)
			txtPassword.setValue(cloudName);
	}

	@Override
	public String getCloudNameField()
	{
		return this.txtCloudName.getValue();
	}

	@Override
	public String getLoginField()
	{
		return this.txtLogin.getValue();
	}

	@Override
	public String getPasswordField()
	{
		return this.txtPassword.getValue();
	}

	@Override
	public String getDataCloudName()
	{
		return this.cloudName;
	}

	@Override
	public String getDataLogin()
	{
		return this.login;
	}

	@Override
	public String getDataPassword()
	{
		return this.password;
	}

	@Override
	public void setDataCloudName(String cloudName)
	{
		this.cloudName = cloudName;
	}

	@Override
	public void setDataLogin(String login)
	{
		this.login = login;
	}

	@Override
	public void setDataPassword(String password)
	{
		this.password = password;
	}

}
