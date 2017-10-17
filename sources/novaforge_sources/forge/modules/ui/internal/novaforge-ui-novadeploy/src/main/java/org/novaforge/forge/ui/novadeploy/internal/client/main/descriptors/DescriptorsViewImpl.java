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

import java.util.Locale;

import net.engio.mbassy.bus.MBassador;

import org.novaforge.forge.portal.events.PortalEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.components.DescriptorEditor;
import org.novaforge.forge.ui.novadeploy.internal.client.components.DescriptorTree;
import org.novaforge.forge.ui.novadeploy.internal.client.event.DeployEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.DescriptorEditorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.ForkEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.event.ReleaseDescriptorEvent;
import org.novaforge.forge.ui.novadeploy.internal.client.rest.data.CustomerDescriptor;

import com.google.gson.Gson;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Vincent Chenal
 */
public class DescriptorsViewImpl extends VerticalLayout implements DescriptorsView
{

	/**
	 * Serialization id
	 */
	private static final long serialVersionUID = -8751426074045820735L;

	// private ComboBox descriptorsList;
	private Button butNewDescriptor;
	private HorizontalLayout hlSelectOrNew;

	private final DescriptorTree descriptorTree;

	private MBassador<PortalEvent> eventBus;

	/**
	 * Default constructor.
	 */
	public DescriptorsViewImpl()
	{
		super();

		butNewDescriptor = new Button();
		butNewDescriptor.setStyleName(Reindeer.BUTTON_DEFAULT);
		hlSelectOrNew = new HorizontalLayout();
		descriptorTree = new DescriptorTree();

		initDescriptorTree();
		initContent();
		// Refresh captions according to current locale
		refreshLocale(getLocale());
	}

	private void initDescriptorTree()
	{
		descriptorTree.setSizeFull();

		// Set the value from server-side
		descriptorTree.setWhatToDo("initial value");
		descriptorTree.setCurrentDescriptor("initial value");

		// Process a value input by the user from the client-side
		descriptorTree.addValueChangeListener(new DescriptorTree.ValueChangeListener()
		{
			/**
					 *
					 */
			private static final long serialVersionUID = -5685744212757544805L;

			@Override
			public void valueChange()
			{
				switch (descriptorTree.getWhatToDo())
				{
				case "deploy":
					eventBus.publish(new DeployEvent(descriptorTree.getCurrentDescriptor(), descriptorTree
							.getCurrentDescriptorVersion()));
					break;
				case "read":
					eventBus.publish(new DescriptorEditorEvent(DescriptorEditor.Mode.READ.toString(), descriptorTree
							.getCurrentDescriptor(), descriptorTree.getCurrentDescriptorVersion(), null));
					break;
				case "edit":
					eventBus.publish(new DescriptorEditorEvent(DescriptorEditor.Mode.EDIT.toString(), descriptorTree
							.getCurrentDescriptor(), descriptorTree.getCurrentDescriptorVersion(), null));
					break;
				case "fork":
					eventBus.publish(new ForkEvent(descriptorTree.getCurrentDescriptor(), descriptorTree
							.getCurrentDescriptorVersion()));
					break;
				case "release":
					eventBus.publish(new ReleaseDescriptorEvent(descriptorTree.getCurrentDescriptor(), descriptorTree
							.getCurrentDescriptorVersion()));
					break;
				default:
					;
				}
			}
		});
	}

	private void initContent()
	{
		this.setSizeFull();

		hlSelectOrNew.setHeight("50px");
		hlSelectOrNew.setWidth("100%");
		hlSelectOrNew.addComponent(butNewDescriptor);
		hlSelectOrNew.setComponentAlignment(butNewDescriptor, Alignment.MIDDLE_LEFT);

		this.addComponent(hlSelectOrNew);
		this.addComponent(descriptorTree);
		this.setExpandRatio(descriptorTree, 1);
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
		// descriptorsList.setCaption("Descriptors list");

		butNewDescriptor.setCaption("New Descriptor");

		// windowDescriptor.setCaption("Descriptor Creator");

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
	}

	@Override
	public Button getNewDescriptorButton()
	{
		return butNewDescriptor;
	}

	/*
	 * public Window getWindow() { return this.windowDescriptor; }
	 * 
	 * @Override public Button getOkCreateButton() { return
	 * windowDescriptor.getOkCreateButton(); }
	 * 
	 * @Override public Button getCancelCreateButton() { return
	 * windowDescriptor.getCancelCreateButton(); }
	 */

	@Override
	public void setEventBus(MBassador<PortalEvent> eventBus)
	{
		this.eventBus = eventBus;
	}

	@Override
	public void setDescriptors(CustomerDescriptor[] descriptors)
	{
		Gson gson = new Gson();
		descriptorTree.setDescriptorList(gson.toJson(descriptors));
	}

	@Override
	public void setIsTester(boolean isTester)
	{
		this.butNewDescriptor.setVisible(!isTester);
		if (isTester)
		{
			descriptorTree.setIsTester("true");
		} else
		{
			descriptorTree.setIsTester("false");
		}

	}
}
