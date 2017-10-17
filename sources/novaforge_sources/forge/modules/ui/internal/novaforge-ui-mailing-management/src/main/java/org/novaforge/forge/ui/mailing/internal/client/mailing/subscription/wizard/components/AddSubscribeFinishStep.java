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
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.List;
import java.util.Locale;

/**
 * @author Aimen Merkich
 */
public class AddSubscribeFinishStep implements WizardStep
{

  /**
   * The main {@link VerticalLayout} used for view
   */
  private final VerticalLayout mainLayout          = new VerticalLayout();
  /**
   * The added emails label {@link Label}
   */
  private final Label          addedEmailsLabel    = new Label();

  /**
   * The added Emails Icon {@link Label}
   */
  private final Embedded       addedEmailsIcon     = new Embedded();
  /**
   * The rejected emails label {@link Label}
   */
  private final Label          rejectedEmailsLabel = new Label();
  /**
   * the rejected Emails Icon {@link Label}
   */
  private final Embedded       rejectedEmailsIcon  = new Embedded();
  /**
   * The added Emails Layout {@link Label}
   */
  private VerticalLayout addedEmailsLayout;
  /**
   * The rejected Emails Layout {@link Label}
   */
  private VerticalLayout rejectedEmailsLayout;

  /**
   * the added Email Header Layout {@link Label}
   */
  private GridLayout addedEmailHeaderLayout;

  /**
   * the rejected Email Header Layout {@link Label}
   */
  private GridLayout rejectedEmailHeaderLayout;

  /**
   * Default constructor
   */
  public AddSubscribeFinishStep()
  {
    initFinishStepLayout();
  }

  /**
   * Initialize the main layout
   */
  private void initFinishStepLayout()
  {

    mainLayout.setMargin(true);
    mainLayout.setSpacing(true);

    VerticalLayout addedMailLayout = new VerticalLayout();
    addedEmailHeaderLayout = new GridLayout(2, 1);
    addedMailLayout.setSpacing(true);
    addedMailLayout.setMargin(true);
    addedEmailsLabel.setStyleName(NovaForge.LABEL_H2);
    addedEmailsLabel.addStyleName(NovaForge.LABEL_GREEN);

    addedEmailsIcon.setSource(new ThemeResource(NovaForgeResources.ICON_VALIDATE));
    addedEmailsIcon.setWidth(30, Unit.PIXELS);
    addedEmailsIcon.setHeight(30, Unit.PIXELS);

    addedEmailHeaderLayout.addComponent(addedEmailsIcon, 0, 0);
    addedEmailHeaderLayout.addComponent(addedEmailsLabel, 1, 0);
    addedEmailHeaderLayout.setComponentAlignment(addedEmailsLabel, Alignment.MIDDLE_LEFT);

    final Component addedEmailContent = initaddedEmailsSelection();
    addedMailLayout.addComponent(addedEmailHeaderLayout);
    addedMailLayout.addComponent(addedEmailContent);

    VerticalLayout rejectedMailLayout = new VerticalLayout();
    rejectedEmailHeaderLayout = new GridLayout(2, 1);
    rejectedMailLayout.setSpacing(true);
    rejectedMailLayout.setMargin(true);

    rejectedEmailsLabel.setStyleName(NovaForge.LABEL_H2);
    rejectedEmailsLabel.addStyleName(NovaForge.LABEL_ORANGE);
    rejectedEmailsIcon.setSource(new ThemeResource(NovaForgeResources.ICON_WARNING));
    rejectedEmailsIcon.setWidth(30, Unit.PIXELS);
    rejectedEmailsIcon.setHeight(30, Unit.PIXELS);

    rejectedEmailHeaderLayout.addComponent(rejectedEmailsIcon, 0, 0);
    rejectedEmailHeaderLayout.addComponent(rejectedEmailsLabel, 1, 0);
    rejectedEmailHeaderLayout.setComponentAlignment(rejectedEmailsLabel, Alignment.MIDDLE_LEFT);

    final Component rejectedEmailContent = initRejectedEmailsSelection();
    rejectedMailLayout.addComponent(rejectedEmailHeaderLayout);
    rejectedMailLayout.addComponent(rejectedEmailContent);

    mainLayout.addComponent(rejectedMailLayout);
    mainLayout.addComponent(addedMailLayout);

  }

  /**
   * @return
   */
  private Component initaddedEmailsSelection()
  {
    addedEmailsLayout = new VerticalLayout();
    return addedEmailsLayout;
  }

  /**
   * @return Vertical Layout {@link Label} value
   */
  private Component initRejectedEmailsSelection()
  {
    rejectedEmailsLayout = new VerticalLayout();
    return rejectedEmailsLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCaption()
  {
    return MailingModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                                                        Messages.MAILING_LISTS_ADD_STEPLAST_TITLE);
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
   * Empty the view datas
   */
  public void emptyDatas()
  {
    addedEmailsLayout.removeAllComponents();
    rejectedEmailsLayout.removeAllComponents();
  }

  /**
   * Refresh the internationalized view content
   * 
   * @param pLocale
   *          the {@link Locale} to use for internationalization
   */
  public void refreshLocale(final Locale pLocale)
  {

    addedEmailsLabel.setValue(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_ADDED_EMAILS_LABEL));
    rejectedEmailsLabel.setValue(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_REJECTED_EMAILS_LABEL));

  }

  /**
   * Get the added emails layout {@link Label}
   * 
   * @return the VerticalLayout {@link Label} value
   */
  public VerticalLayout getAddedEmailsLayout()
  {
    return addedEmailsLayout;
  }

  /**
   * Get the rejected emails layout {@link Label}
   * 
   * @return the VerticalLayout {@link Label} value
   */
  public VerticalLayout getRejectedEmailsLayout()
  {
    return rejectedEmailsLayout;
  }

  /**
   * Set the rejected email layout visibility {@link Label} value
   * 
   * @param isVisbile
   */
  public void setRejectedEmailsComponentVisible(final boolean isVisbile)
  {
    rejectedEmailsLayout.setVisible(isVisbile);
    rejectedEmailHeaderLayout.setVisible(isVisbile);
  }

  /**
   * Set the added email layout visibility {@link Label} value
   * 
   * @param isVisbile
   */
  public void setAddedEmailsComponentVisible(final boolean isVisbile)
  {
    addedEmailsLayout.setVisible(isVisbile);
    addedEmailHeaderLayout.setVisible(isVisbile);
  }

  /**
   * Set =mail component value
   * 
   * @param pMailOnError
   *          list of mails to display
   * @param isAddedEmailCategorie
   *          true for addding, false for rejecting mails
   */
  public void setMailComponent(final List<String> pMailOnError, final boolean isAddedEmailCategorie)
  {

    GridLayout EmailGridLayout = new GridLayout();
    EmailGridLayout.setMargin(true);
    EmailGridLayout.setSpacing(true);
    if (isAddedEmailCategorie)
    {
      EmailGridLayout.setColumns(1);
    }
    else
    {
      EmailGridLayout.setColumns(2);
    }
    EmailGridLayout.setRows(pMailOnError.size());
    for (final String email : pMailOnError)
    {

      Label MailLabel = new Label(email);
      EmailGridLayout.addComponent(MailLabel, 0, pMailOnError.indexOf(email));
      if (!isAddedEmailCategorie)
      {
        Label rejectedMessageLabel = new Label();
        rejectedMessageLabel.addStyleName(NovaForge.LABEL_RED);
        rejectedMessageLabel.setValue(MailingModule.getPortalMessages().getMessage(
            UI.getCurrent().getLocale(), Messages.MAILING_LISTS_REJECTED_DUPLICATED_EMAILS_LABEL));
        EmailGridLayout.addComponent(rejectedMessageLabel, 1, pMailOnError.indexOf(email));
      }

    }
    if (isAddedEmailCategorie)
    {
      addedEmailsLayout.addComponent(EmailGridLayout);
    }
    else
    {
      rejectedEmailsLayout.addComponent(EmailGridLayout);
    }

  }

}
