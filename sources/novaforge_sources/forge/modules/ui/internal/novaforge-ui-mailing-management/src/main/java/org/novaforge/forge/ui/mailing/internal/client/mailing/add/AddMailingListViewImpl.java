/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.ui.mailing.internal.client.mailing.add;

import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListType;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.mailing.internal.client.mailing.components.ListnameValidator;
import org.novaforge.forge.ui.mailing.internal.client.mailing.components.MailingListFormFieldFactory;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;

import java.util.Locale;

/**
 * @author sbenoist
 */
public class AddMailingListViewImpl extends VerticalLayout implements AddMailingListView
{

  /**
   * Serialization id
   */
  private static final long           serialVersionUID = -8751426074045820735L;

  private Button                      returnButton;

  private Form                        mailingListForm;
  private MailingListFormFieldFactory mailingListFormFieldFactory;

  private Button                      addButton;
  private Button                      cancelButton;

  /**
   * Default constructor.
   */
  public AddMailingListViewImpl()
  {
    // Init view
    setMargin(true);

    // Init contents
    final Component headers = initHeaders();
    final Component formContent = initFormContent();
    final Component footer = initFooter();
    addComponent(headers);
    addComponent(formContent);
    addComponent(footer);
  }

  private Component initHeaders()
  {
    final HorizontalLayout headerButtons = new HorizontalLayout();
    headerButtons.setSpacing(true);
    headerButtons.setMargin(new MarginInfo(false, false, true, false));
    returnButton = new Button();
    returnButton.setIcon(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT));

    headerButtons.addComponent(returnButton);
    return headerButtons;
  }

  private Component initFormContent()
  {
    mailingListForm = new Form();
    mailingListForm.setImmediate(true);
    mailingListForm.setInvalidCommitted(false);
    mailingListForm.setFooter(null);
    mailingListFormFieldFactory = new MailingListFormFieldFactory();
    mailingListForm.setFormFieldFactory(mailingListFormFieldFactory);

    return mailingListForm;
  }

  private Component initFooter()
  {
    final HorizontalLayout footerButtons = new HorizontalLayout();
    footerButtons.setSpacing(true);
    footerButtons.setMargin(new MarginInfo(true, false, true, true));
    addButton = new Button();
    addButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    addButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    cancelButton = new Button();
    cancelButton.setStyleName(NovaForge.BUTTON_LINK);
    footerButtons.addComponent(cancelButton);
    footerButtons.addComponent(addButton);
    return footerButtons;
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
    returnButton.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_ADD_BACK));

    mailingListForm.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_ADD_TITLE));
    if (mailingListFormFieldFactory.getName() != null)
    {
      mailingListFormFieldFactory.getName().setCaption(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_NAME));
      mailingListFormFieldFactory.getName().setDescription(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_NAME_DESCRIPTION));
      mailingListFormFieldFactory.getName().setRequiredError(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_NAME_REQUIRED) + " "
              + MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_NAME_REGEXP));
      mailingListFormFieldFactory.getName().removeAllValidators();
      mailingListFormFieldFactory.getName().addValidator(
          new ListnameValidator(MailingModule.getPortalMessages().getMessage(pLocale,
              Messages.MAILING_LISTS_NAME_REGEXP)));
      mailingListFormFieldFactory.getName().addValidator(
          new StringLengthValidator(MailingModule.getPortalMessages().getMessage(pLocale,
              Messages.MAILING_LISTS_NAME_REQUIRED), 3, 40, false));
    }
    if (mailingListFormFieldFactory.getSubject() != null)
    {
      mailingListFormFieldFactory.getSubject().setCaption(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_SUBJECT));
      mailingListFormFieldFactory.getSubject().setRequiredError(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_SUBJECT_REQUIRED));
      mailingListFormFieldFactory.getSubject().removeAllValidators();
      mailingListFormFieldFactory.getSubject().addValidator(
          new StringLengthValidator(MailingModule.getPortalMessages().getMessage(pLocale,
              Messages.MAILING_LISTS_SUBJECT_REQUIRED), 3, 40, false));
    }
    if (mailingListFormFieldFactory.getDescription() != null)
    {
      mailingListFormFieldFactory.getDescription().setCaption(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_ROLES_DESCRIPTION));
      mailingListFormFieldFactory.getDescription().setRequiredError(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_DESCRIPTION_REQUIRED));
      mailingListFormFieldFactory.getDescription().removeAllValidators();
      mailingListFormFieldFactory.getDescription().addValidator(
          new StringLengthValidator(MailingModule.getPortalMessages().getMessage(pLocale,
              Messages.MAILING_LISTS_DESCRIPTION_REQUIRED), 3, 250, false));
    }
    if (mailingListFormFieldFactory.getType() != null)
    {
      final ComboBox typeBox = mailingListFormFieldFactory.getType();
      typeBox.setCaption(MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_TYPE));

      typeBox.addItem(MailingListType.HOTLINE_LIST);
      typeBox.setItemCaption(MailingListType.HOTLINE_LIST,
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_TYPE_HOTLINE));
      typeBox.addItem(MailingListType.PRIVATE_LIST);
      typeBox.setItemCaption(MailingListType.PRIVATE_LIST,
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_TYPE_PRIVATE));
      typeBox.setInputPrompt(MailingModule.getPortalMessages().getMessage(pLocale,
          Messages.MAILING_LISTS_TYPE_INPUTPROMPT));
      typeBox.setRequiredError(MailingModule.getPortalMessages().getMessage(pLocale,
          Messages.MAILING_LISTS_TYPE_REQUIRED));
      typeBox.setDescription(MailingModule.getPortalMessages().getMessage(pLocale,
          Messages.MAILING_LISTS_TYPE_DESCRIPTION));
    }

    addButton.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_ADD_CONFIRM));
    cancelButton.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_ADD_CANCEL));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getReturnButton()
  {
    return returnButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAddButton()
  {
    return addButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCancelButton()
  {
    return cancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getMailingListForm()
  {
    return mailingListForm;
  }

}
