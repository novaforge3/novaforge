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

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.group.subscribe.AddGroupView;
import org.novaforge.forge.ui.mailing.internal.client.mailing.subscription.inner.user.subscribe.AddUserView;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import java.util.Locale;

/**
 * @author Aimen Merkich
 */
public class AddSubscribeWizard extends Wizard
{

  protected static final String                 STEP_TWO         = "STEP_TWO";
  protected static final String                 FINAL_STEP       = "FINAL_STEP";
  /**
   *
   */
  private static final   long                   serialVersionUID = -9042491897609720249L;
  private final          AddSubscribeFirstStep  stepOne          = new AddSubscribeFirstStep(this);
  private final          AddSubscribeSecondStep stepTwo          = new AddSubscribeSecondStep(this);
  private final          AddSubscribeFinishStep stepThree        = new AddSubscribeFinishStep();
  private SubscribeType subscribeType;
  private String        step;

  public AddSubscribeWizard()
  {
    super();

    initContent();
    addListeners();
  }

  private void initContent()
  {
    // Wizard properties
    getCancelButton().setStyleName(NovaForge.BUTTON_LINK);
    getNextButton().setStyleName(NovaForge.BUTTON_PRIMARY);
    getNextButton().addStyleName(NovaForge.BUTTON_DISABLED_HIDDEN);
    getNextButton().setIcon(new ThemeResource(NovaForgeResources.ICON_GO_NEXT_COLORED));
    getBackButton().setIcon(new ThemeResource(NovaForgeResources.ICON_GO_PREVIOUS_COLORED));
    getBackButton().addStyleName(NovaForge.BUTTON_DISABLED_HIDDEN);
    getFinishButton().setStyleName(NovaForge.BUTTON_PRIMARY);
    getFinishButton().addStyleName(NovaForge.BUTTON_DISABLED_HIDDEN);
    // Add steps
    addStep(stepOne);
    addStep(stepTwo);
    addStep(stepThree);
    // Disable / hide not needed button
    getNextButton().setEnabled(false);
  }

  private void addListeners()
  {
    addListener(new WizardProgressListener()
    {

      @Override
      public void activeStepChanged(WizardStepActivationEvent event)
      {
        if (event.getActivatedStep().equals(stepTwo))
        {
          // Step 2
          subscribeType = stepOne.getSubscribeType();
          stepTwo.setViewForType(subscribeType);
          setStep(STEP_TWO);

          getBackButton().setVisible(true);
          getCancelButton().setVisible(true);
          getNextButton().setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
          getNextButton().setCaption(MailingModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                                                                                  Messages.MAILING_LISTS_WIZARD_ADD));

        }
        else if (event.getActivatedStep().equals(stepThree))
        {
          getBackButton().setVisible(false);
          getCancelButton().setVisible(false);
          setStep(FINAL_STEP);
        }
        else
        {
          getBackButton().setVisible(true);
          getCancelButton().setVisible(true);
          getNextButton().setIcon(new ThemeResource(NovaForgeResources.ICON_GO_NEXT_COLORED));
          getNextButton().setCaption(MailingModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
                                                                                  Messages.MAILING_LISTS_WIZARD_NEXT));
        }

      }

      @Override
      public void stepSetChanged(WizardStepSetChangedEvent event)
      {
        // Nothing to do
      }

      @Override
      public void wizardCompleted(WizardCompletedEvent event)
      {

      }

      @Override
      public void wizardCancelled(WizardCancelledEvent event)
      {
        // TODO Auto-generated method stub

      }
    });
  }

  /**
   * get the User view
   *
   * @return AddUserView
   *         the User view
   */
  public AddUserView getAddUserView()
  {
    return stepTwo.getAddUserView();

  }

  /**
   * get the Group view
   *
   * @return AddGroupView
   *         the Group view
   */
  public AddGroupView getAddGroupView()
  {
    return stepTwo.getAddGroupView();

  }

  /**
   * restard the wizard
   */
  public void restartWizard()
  {

    emptyStepsDatas();
    setStepActive(stepOne);
  }

  /**
   * Empty all step data
   */
  private void emptyStepsDatas()
  {
    stepOne.emptyDatas();
    stepTwo.emptyDatas();
    stepThree.emptyDatas();
  }

  /**
   * Set Step to Wizard
   *
   * @param pStep
   *     the step to set active
   */
  public void setStepActive(final WizardStep pStep)
  {
    if (getSteps().contains(pStep))
    {
      activateStep(pStep);
    }
  }

  /**
   * Set the internationalized content with given {@link Locale}
   * 
   * @param pLocale
   *          the locale to use
   */
  public void refreshLocale(final Locale pLocale)
  {
    getNextButton().setCaption(
        MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_WIZARD_NEXT));
    getBackButton().setCaption(
        MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_WIZARD_BACK));
    getFinishButton().setCaption(
        MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_WIZARD_FINISH));
    getCancelButton().setCaption(
        MailingModule.getPortalMessages().getMessage(pLocale, Messages.MAILING_LISTS_WIZARD_CANCEL));
    stepOne.refreshLocale(pLocale);
    stepTwo.refreshLocale(pLocale);
    stepThree.refreshLocale(pLocale);
  }

  /**
   * Get external user mail
   * 
   * @return TextArea
   *         of external user mails
   */
  public TextArea getExternalUserMail()
  {
    return stepTwo.getExternalUserMail();
  }

  /**
   * Get subscribe type of the current step
   * 
   * @return SubscribeType
   *         of the current step
   */
  public SubscribeType getSubscribeType()
  {
    return subscribeType;
  }

  /**
   * Set subscribe type of the current step
   * 
   * @param subscribeType
   *          to set
   */
  public void setSubscribeType(SubscribeType subscribeType)
  {
    this.subscribeType = subscribeType;
  }

  /**
   * @return the stepnumber
   */
  public String getStep()
  {
    return step;
  }

  /**
   * @return the stepnumber
   */
  public void setStep(final String pStepNumber)
  {
    step = pStepNumber;
  }

  /**
   * @return the stepOne
   */
  public AddSubscribeFirstStep getStepOne()
  {
    return stepOne;
  }

  /**
   * @return the stepTwo
   */
  public AddSubscribeSecondStep getStepTwo()
  {
    return stepTwo;
  }

  /**
   * @return the stepThree
   */
  public AddSubscribeFinishStep getStepThree()
  {
    return stepThree;
  }
}
