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

import java.util.Locale;

import net.engio.mbassy.bus.MBassador;

import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.novadeploy.internal.client.components.DescriptorEditor;
import org.novaforge.forge.ui.novadeploy.internal.client.components.DescriptorEditor.Mode;
import org.novaforge.forge.ui.novadeploy.internal.client.containers.EnvironmentFrom;
import org.novaforge.forge.ui.novadeploy.internal.client.event.CreateDescriptorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.EditDescriptorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.exceptions.DescriptorEditorException;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Vincent Chenal
 */
public class DescriptorEditorViewImpl extends VerticalLayout implements DescriptorEditorView
{

	/**
	 * Serialization id
	 */
	private static final long serialVersionUID = -8751426074045820735L;

	private Form f;
	private TextField txtDescriptorName;
	private TextField txtDescriptorVersion;
	private String descriptorName;
	private int descriptorVersion;
	private DescriptorEditor descriptorEditor;
	private Button butReturn;
	private Button butOk;
	private MBassador<PortalEvent> eventBus;
	private Mode editorMode;
	private EnvironmentFrom environmentFrom;

	/**
	 * Default constructor.
	 */
	public DescriptorEditorViewImpl()
	{
		super();
		initContent();

		// Refresh captions according to current locale
		refreshLocale(getLocale());
	}

	private void initContent()
	{
		this.setSizeFull();
		this.setMargin(true);
		this.setSpacing(true);
		this.setStyleName("descriptorEditor");
		this.addStyleName("isBordered");

		f = new Form();
		txtDescriptorName = new TextField();
		txtDescriptorVersion = new TextField();
		descriptorEditor = new DescriptorEditor();
		butOk = new Button();
		butReturn = new Button();

		initDescriptorEditor();

		// Create components
		f.getLayout().addComponent(txtDescriptorName);
		f.getLayout().addComponent(txtDescriptorVersion);
		HorizontalLayout buttonBar = new HorizontalLayout();

		butOk.setStyleName(NovaForge.BUTTON_PRIMARY);
		butReturn.setIcon(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT2));
		butReturn.setCaption("Return");

		buttonBar.addComponent(butOk);

		// Fill root layout
		this.addComponent(this.butReturn);
		this.setComponentAlignment(this.butReturn, Alignment.MIDDLE_LEFT);
		this.setExpandRatio(this.butReturn, 0.1f);

		this.addComponent(f);
		this.setComponentAlignment(f, Alignment.MIDDLE_LEFT);
		this.setExpandRatio(f, 0.2f);

		this.addComponent(descriptorEditor);
		this.setComponentAlignment(descriptorEditor, Alignment.MIDDLE_CENTER);
		this.setExpandRatio(this.descriptorEditor, 0.5f);

		this.addComponent(buttonBar);
		this.setComponentAlignment(buttonBar, Alignment.MIDDLE_RIGHT);
		this.setExpandRatio(buttonBar, 0.1f);
	}

	private void initDescriptorEditor()
	{
		descriptorEditor.setWidth("100%");
		descriptorEditor.setHeight("400px");
		descriptorEditor.setStyleName("descriptorEditor");

		// Process a value input by the user from the client-side
		descriptorEditor.addValueChangeListener(new DescriptorEditor.ValueChangeListener()
		{
			private static final long serialVersionUID = 54993721819727243L;

			@Override
			public void valueChange()
			{
				switch (editorMode)
				{
				case NEW:
					eventBus.publish(new CreateDescriptorEvent(txtDescriptorName.getValue()));
					break;
				case EDIT:
					eventBus.publish(new EditDescriptorEvent(txtDescriptorName.getValue()));
					break;
				default:
					break;
				}
			}
		});
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
		txtDescriptorName.setCaption("Descriptor Name");
		txtDescriptorVersion.setCaption("Version");
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
		 * .getDescription().setRequiredError(COUCOU
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
	public Button getReturnButton()
	{
		return this.butReturn;
	}

	@Override
	public Button getCreateButton()
	{
		return butOk;
	}

	@Override
	public void refreshContent()
	{
	}

	@Override
	public void prepareEditor(DescriptorEditor.Mode mode, String descriptorName, int descriptorVersion, String content,
			EnvironmentFrom environmentFrom) throws DescriptorEditorException
	{
		this.environmentFrom = environmentFrom;
		switch (mode)
		{
		case NEW:
			// Datas
			this.editorMode = Mode.NEW;
			// Set default content
			descriptorEditor
					.setDescriptorContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
							+ "<environment xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"novadeploy-schema-v1.xsd\">\r\n\t"
							+ "<node name=\"database\">\r\n\t\t" + "<system>\r\n\t\t\t"
							+ "<memory>1024</memory>\r\n\t\t\t" + "<catalogRef>ubuntu_12.04</catalogRef>\r\n\t\t\t"
							+ "<cpu>2</cpu>\r\n\t\t\t" + "<osfamily>DEBIAN</osfamily>\r\n\t\t\t"
							+ "<rootPassword>root</rootPassword>\r\n\t\t" + "</system>\r\n\t\t"
							+ "<components>\r\n\t\t\t"
							+ "<!--<svn path=\"https://path/install.sh\" revision=\"3\"/>-->\r\n\t\t\t"
							+ "<!--<mvn mvn-uri=\"https://path/install.sh\"/>-->\r\n\t\t\t"
							+ "<!--<http url=\"https://path/install.sh\"/>-->\r\n\t\t" + "</components>\r\n\t\t\t"
							+ "<!--Other ressources-->\r\n\t\t" + "<lifecycle> \r\n\t\t    "
							+ "<install>bash install.sh</install>\r\n\t\t    " + "<start>bash start.sh</start>\r\n\t\t"
							+ "</lifecycle>\r\n\t" + "</node>\r\n\t" + "</environment>");
			// Set name field writeable
			this.txtDescriptorName.setEnabled(true);
			this.txtDescriptorName.setValue("");
			this.txtDescriptorVersion.setEnabled(false);
			this.txtDescriptorVersion.setValue("");
			// Form description
			f.setDescription("<h2>Create New Descriptor</h2><p>Choose a descriptor name and fill its content in the corresponding area</p>");

			// Buttons
			this.butOk.setVisible(true);
			this.butOk.setCaption("Create");

			break;
		case READ:
			this.editorMode = Mode.READ;
			this.descriptorName = descriptorName;
			this.descriptorVersion = descriptorVersion;
			// Set default content
			descriptorEditor.setDescriptorContent(content);
			// Set name field read-only
			this.txtDescriptorName.setEnabled(false);
			this.txtDescriptorName.setValue(descriptorName);
			this.txtDescriptorVersion.setEnabled(false);

			if (descriptorVersion != 0)
				this.txtDescriptorVersion.setValue("" + descriptorVersion);
			else
				this.txtDescriptorVersion.setValue("current");

			// Form description
			f.setDescription("<h2>Read existing Descriptor</h2><p>This descriptor is already versionned, so it is in read-only mode</p>");

			// Buttons
			this.butOk.setVisible(false);
			break;
		case EDIT:
			this.editorMode = Mode.EDIT;
			this.descriptorName = descriptorName;
			this.descriptorVersion = descriptorVersion;

			// Set default content
			descriptorEditor.setDescriptorContent(content);

			// Set name field read-only
			this.txtDescriptorName.setEnabled(false);
			this.txtDescriptorName.setValue(descriptorName);
			this.txtDescriptorVersion.setEnabled(false);
			this.txtDescriptorVersion.setValue("current");

			// Form description
			f.setDescription("<h2>Edit existing Descriptor</h2><p>This descriptor is the current one for this branch, so you can update its content</p>");

			// Buttons
			this.butOk.setVisible(true);
			this.butOk.setCaption("Edit");
			break;
		default:
			throw new DescriptorEditorException("Editor has no mode set");
		}
	}

	@Override
	public String getDescriptorContent()
	{
		return descriptorEditor.getDescriptorContent();
	}

	@Override
	public void setEventBus(MBassador<PortalEvent> eventbus)
	{
		this.eventBus = eventbus;
	}

	@Override
	public EnvironmentFrom getEnvironmentFrom()
	{
		return environmentFrom;
	}

}
