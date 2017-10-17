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
package org.novaforge.forge.ui.mailing.internal.client.mailing.details;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListType;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.mailing.internal.client.mailing.components.MailingListFormFieldFactory;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;

import java.util.Locale;

/**
 * @author sbenoist
 */
public class MailingListDetailsViewImpl extends VerticalLayout implements MailingListDetailsView
{

  /**
   * Serialization id
   */
  private static final long           serialVersionUID = -8751426074045820735L;

  private Button                      returnButton;

  private HorizontalLayout            headerButtons;
  private Form                        mailingListForm;
  private MailingListFormFieldFactory mailingListFormFieldFactory;

  /**
   * Default constructor.
   */
  public MailingListDetailsViewImpl()
  {
    // Init view
    setMargin(true);

    // Init contents
    final Component headers = initHeaders();
    final Component formContent = initFormContent();
    addComponent(headers);
    addComponent(formContent);
  }

  private Component initHeaders()
  {
    headerButtons = new HorizontalLayout();
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
    mailingListForm.setReadOnly(true);

    return mailingListForm;
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
      mailingListFormFieldFactory.getName().setWidth(100, Unit.PERCENTAGE);
      mailingListFormFieldFactory.getName().setReadOnly(true);
      mailingListFormFieldFactory.getName().setCaption(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_NAME));
    }
    if (mailingListFormFieldFactory.getSubject() != null)
    {
      mailingListFormFieldFactory.getSubject().setReadOnly(true);
      mailingListFormFieldFactory.getSubject().setCaption(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_SUBJECT));
    }
    if (mailingListFormFieldFactory.getDescription() != null)
    {
      mailingListFormFieldFactory.getDescription().setReadOnly(true);
      mailingListFormFieldFactory.getDescription().setCaption(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_DESCRIPTION));
    }
    if (mailingListFormFieldFactory.getType() != null)
    {
      mailingListFormFieldFactory.getType().setReadOnly(true);
      mailingListFormFieldFactory.getType().setCaption(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_TYPE));

      mailingListFormFieldFactory.getType().addItem(MailingListType.HOTLINE_LIST);
      mailingListFormFieldFactory.getType().setItemCaption(MailingListType.HOTLINE_LIST,
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_TYPE_HOTLINE));
      mailingListFormFieldFactory.getType().addItem(MailingListType.PRIVATE_LIST);
      mailingListFormFieldFactory.getType().setItemCaption(MailingListType.PRIVATE_LIST,
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_TYPE_PRIVATE));
      mailingListFormFieldFactory.getType().setItemCaption(MailingListType.FORUM_LIST,
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_TYPE_CUSTOM));
      mailingListFormFieldFactory.getType().setItemCaption(MailingListType.INTRANET_LIST,
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_TYPE_CUSTOM));
      mailingListFormFieldFactory.getType().setItemCaption(MailingListType.NEWSLETTER_LIST,
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_TYPE_CUSTOM));
      mailingListFormFieldFactory.getType().setItemCaption(MailingListType.PUBLIC_LIST,
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_TYPE_CUSTOM));
      mailingListFormFieldFactory.getType().setDescription(
          MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_TYPE_DESCRIPTION));
    }
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
  public Form getMailingListForm()
  {
    return mailingListForm;
  }

}
