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
package org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.wizard.components;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.group.subscribe.AddGroupView;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.group.subscribe.AddGroupViewImpl;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.inner.user.subscribe.AddUserView;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.inner.user.subscribe.AddUsersViewImpl;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Locale;

/**
 * @author Jeremy Casery
 * @author Aimen Merkich
 */
public class AddSubscribeSecondStep implements WizardStep
{
  /**
   * Define default max size for image. (1536000 octets for 1.5mo)
   */
  public final static int MAX_SIZE = 1536000;
  /**
   * The {@link AddSubscribeWizard} which contains this {@link WizardStep}
   */
  private final AddSubscribeWizard wizard;
  /**
   * The main layout
   */
  private final VerticalLayout mainLayout         = new VerticalLayout();
  /**
   * The External user type layout
   */
  private final VerticalLayout externalUserLayout = new VerticalLayout();
  /**
   * The External email adress field
   */
  private final TextArea       ExternalMailsUsers = new TextArea();
  /**
   * The inner user layout
   */
  private final VerticalLayout innerUserLayout    = new VerticalLayout();

  /**
   * The add user view to set
   */
  private final AddUserView addUserView = new AddUsersViewImpl();

  /**
   * The group user layout
   */
  private final VerticalLayout groupUserLayout = new VerticalLayout();

  /**
   * The add group user view to set
   */
  private final AddGroupView addGroupView = new AddGroupViewImpl();
  /**
   * Contains the {@link Form} used to show external user {@link TextField}
   */
  private Form ExternalUserForm;

  /**
   * Default constructor, will initialize the content
   */
  public AddSubscribeSecondStep(final AddSubscribeWizard pWizard)
  {
    wizard = pWizard;
    initExternalUserLayout();
    innerUserLayout();
    groupUserLayout();

  }

  /**
   * Initialize the external User layout
   */
  private void initExternalUserLayout()
  {
    externalUserLayout.setMargin(true);
    externalUserLayout.setSpacing(true);
    ExternalUserForm = new Form();
    ExternalMailsUsers.setWidth(700, Unit.PIXELS);
    ExternalMailsUsers.setHeight(300, Unit.PIXELS);
    externalUserLayout.addComponent(ExternalUserForm);
    externalUserLayout.addComponent(ExternalMailsUsers);
  }

  /**
   * Initialize the User forge layout
   */
  private void innerUserLayout()
  {
    innerUserLayout.setMargin(false);
    innerUserLayout.setSpacing(true);
    innerUserLayout.addComponent(addUserView);
  }

  /**
   * Initialize the Group layout
   */
  private void groupUserLayout()
  {
    groupUserLayout.setMargin(false);
    groupUserLayout.setSpacing(true);
    groupUserLayout.addComponent(addGroupView);
  }

  /**
   * Set the content of view for given {@link RepositoryType}
   *
   * @param pType
   *          the type to determine which content to show
   */
  public void setViewForType(final SubscribeType pType)
  {
    mainLayout.removeAllComponents();
    if (pType.equals(SubscribeType.EXTERNAL))
    {
      mainLayout.addComponent(externalUserLayout);
    }
    else if (pType.equals(SubscribeType.GROUP))
    {
      mainLayout.addComponent(groupUserLayout);
    }
    else
    {
      mainLayout.addComponent(innerUserLayout);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption()
  {
    return MailingModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                                                        Messages.MAILING_LISTS_ADD_STEPTWO_TITLE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getContent()
  {
    return mainLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onAdvance()
  {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean onBack()
  {
    return true;
  }

  /**
   * Empty the content datas
   */
  public void emptyDatas()
  {

    getExternalUserMail().setValue("");
  }

  /**
   * Get external Users mails To add {@link TextArea}
   *
   * @return the field
   */
  public TextArea getExternalUserMail()
  {
    return ExternalMailsUsers;
  }

  /**
   * Refresh internationalized content with given {@link Locale}
   *
   * @param pLocale
   *          the locale to use
   */
  public void refreshLocale(final Locale pLocale)
  {
    ExternalUserForm.setCaption(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_ADD_EXTERNAL_USER_LABEL));
    ExternalUserForm.setDescription(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_ADD_EXTERNAL_USER_TITLE));
  }

  /**
   * @return the addUserView
   */
  public AddUserView getAddUserView()
  {
    return addUserView;
  }
  
  /**
   * @return the addGroupView
   */
  public AddGroupView getAddGroupView()
  {
    return addGroupView;
  }
}
