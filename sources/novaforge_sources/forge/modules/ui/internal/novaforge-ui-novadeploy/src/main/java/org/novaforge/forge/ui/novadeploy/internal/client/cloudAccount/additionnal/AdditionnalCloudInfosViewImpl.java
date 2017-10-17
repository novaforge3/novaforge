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

import java.util.Locale;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Vincent Chenal
 */
public class AdditionnalCloudInfosViewImpl extends VerticalLayout implements AdditionnalCloudInfosView
{

	/**
	 * Serialization id
	 */
	private static final long serialVersionUID = -8751426074045820735L;

	private Form form;
	private ComboBox vdcList;
	private ComboBox networkList;
	private Button butValidate;
	private HorizontalLayout buttonBar;

	private String[] vdcs;
	private String[] networks;

	/**
	 * Default constructor.
	 */
	public AdditionnalCloudInfosViewImpl()
	{
		super();
		form = new Form();
		butValidate = new Button();
		butValidate.setStyleName(Reindeer.BUTTON_DEFAULT);
		buttonBar = new HorizontalLayout();

		vdcList = new ComboBox();
		networkList = new ComboBox();
		initContent();
	}

	private void populateComboBoxes()
	{
		for (int i = 0; i < vdcs.length; i++)
		{
			vdcList.addItem(vdcs[i]);
		}

		for (int i = 0; i < vdcs.length; i++)
		{
			networkList.addItem(networks[i]);
		}
	}

	private void initContent()
	{
		this.setSizeFull();
		// Refresh captions according to current locale
		refreshLocale(getLocale());

		// Build Form Layout
		form.getLayout().addComponent(vdcList);
		form.getLayout().addComponent(networkList);
		form.setFooter(new VerticalLayout());
		form.getFooter().addComponent(buttonBar);
		form.setWidth("30%");

		form.addShortcutListener(new ShortcutListener("Validate by pressing enter", ShortcutAction.KeyCode.ENTER, null)
		{

			@Override
			public void handleAction(Object sender, Object target)
			{
				butValidate.click();
			}
		});

		buttonBar.setHeight("25px");
		buttonBar.setWidth("100%");
		buttonBar.addComponent(this.butValidate);
		buttonBar.setComponentAlignment(this.butValidate, Alignment.TOP_RIGHT);

		form.getLayout().setSizeFull();
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
		butValidate.setCaption("Validate");

		form.setDescription("<h2>vCloud Additionnal informations</h2><p>Please choose which VDC and network to use</p><p>To get more information on fields, just place your mouse over it</p>");

		vdcList.setCaption("VDC list");
		vdcList.setDescription("This is the VDC you have to choose from your vCloud account");
		networkList.setCaption("Network list");
		networkList.setDescription("This is the Network you have to choose from your vCloud account");
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
	public void refreshContent()
	{
		refreshLocale(getLocale());
	}

	@Override
	public Button getValidateButton()
	{
		return this.butValidate;
	}

	@Override
	public String getChosenVdc()
	{
		if (this.vdcList.getValue() instanceof String)
			return (String) this.vdcList.getValue();
		else
			return null;
	}

	@Override
	public String getChosenNetwork()
	{
		if (this.networkList.getValue() instanceof String)
			return (String) this.networkList.getValue();
		else
			return null;
	}

	@Override
	public void setData(String[] vdcs, String[] networks)
	{
		this.vdcs = vdcs;
		this.networks = networks;
		populateComboBoxes();
	}

}
