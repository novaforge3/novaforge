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

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.mailing.internal.module.MailingModule;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Locale;

/**
 * @author Jeremy Casery
 * @author Aimen Merkich
 */
public class AddSubscribeFirstStep implements WizardStep
{
  /**
   * The default logo width and height
   */
  private final static float LOGO__WIDTH_HEIGHT = 100;
  /**
   * The {@link AddSubscribeWizard} which contains this {@link WizardStep}
   */
  private final AddSubscribeWizard wizard;
  /**
   * The choose type {@link Label}
   */
  private final Label          chooseTypeLabel    = new Label();
  /**
   * The content {@link VerticalLayout}
   */
  private final VerticalLayout contentLayout      = new VerticalLayout();
  /**
   * The User Project layout
   */
  private final VerticalLayout projectUserLayout  = new VerticalLayout();
  /**
   * The User Project {@link Embedded} logo
   */
  private final Embedded       projectUserLogo    = new Embedded();
  /**
   * The Project User label {@link Label}
   */
  private final Label          projectUserLabel   = new Label();
  /**
   * The Forge User layout
   */
  private final VerticalLayout forgeUserLayout    = new VerticalLayout();
  /**
   * The Forge User {@link Embedded} logo
   */
  private final Embedded       forgeUserLogo      = new Embedded();
  /**
   * The Forge User label {@link Label}
   */
  private final Label          forgeUserLabel     = new Label();
  /**
   * The External User layout
   */
  private final VerticalLayout externalUserLayout = new VerticalLayout();
  /**
   * The External User {@link Embedded} logo
   */
  private final Embedded       externalUserLogo   = new Embedded();
  /**
   * The External User label {@link Label}
   */
  private final Label          externalUserLabel  = new Label();
  /**
   * The Group User layout
   */
  private final VerticalLayout groupUserLayout    = new VerticalLayout();
  /**
   * The Group User {@link Embedded} logo
   */
  private final Embedded       groupUserLogo      = new Embedded();
  /**
   * The Group User label {@link Label}
   */
  private final Label          groupUserLabel     = new Label();
  /**
   * The gridLayout containing all selections choices
   */

  GridLayout gridLayout = new GridLayout(4, 4);
  /**
   * The subscribe type, used to remember the user choice
   */
  private SubscribeType subscribeType;

  /**
   * Default constructor
   *
   * @param pWizard
   *          the {@link AddSubscribeyWizard} associated
   */
  public AddSubscribeFirstStep(final AddSubscribeWizard pWizard)
  {
    wizard = pWizard;

    wizard.getNextButton().setEnabled(false);
    gridLayout.setSpacing(true);

    projectUserLogo.setWidth(LOGO__WIDTH_HEIGHT, Unit.PIXELS);
    projectUserLogo.setHeight(LOGO__WIDTH_HEIGHT, Unit.PIXELS);
    projectUserLabel.setStyleName(NovaForge.LABEL_H1);
    projectUserLabel.setSizeUndefined();
    projectUserLabel.addStyleName(NovaForge.LABEL_GREEN);
    projectUserLayout.setWidth(355, Unit.PIXELS);
    projectUserLayout.setHeight(190, Unit.PIXELS);
    projectUserLayout.setStyleName(NovaForge.LAYOUT_SELECTABLE);
    projectUserLayout.setMargin(true);
    projectUserLayout.setSpacing(true);

    forgeUserLogo.setWidth(LOGO__WIDTH_HEIGHT, Unit.PIXELS);
    forgeUserLogo.setHeight(LOGO__WIDTH_HEIGHT, Unit.PIXELS);
    forgeUserLabel.setStyleName(NovaForge.LABEL_H1);
    forgeUserLabel.setSizeUndefined();
    forgeUserLabel.addStyleName(NovaForge.LABEL_GREEN);
    forgeUserLayout.setWidth(355, Unit.PIXELS);
    forgeUserLayout.setHeight(190, Unit.PIXELS);
    forgeUserLayout.setStyleName(NovaForge.LAYOUT_SELECTABLE);
    forgeUserLayout.setMargin(true);
    forgeUserLayout.setSpacing(true);

    externalUserLogo.setSource(new ThemeResource(NovaForgeResources.ICON_USER_MAIL));
    externalUserLogo.setWidth(LOGO__WIDTH_HEIGHT, Unit.PIXELS);
    externalUserLogo.setHeight(LOGO__WIDTH_HEIGHT, Unit.PIXELS);
    externalUserLabel.setStyleName(NovaForge.LABEL_H1);
    externalUserLabel.setSizeUndefined();
    externalUserLabel.addStyleName(NovaForge.LABEL_GREEN);
    externalUserLayout.setWidth(355, Unit.PIXELS);
    externalUserLayout.setHeight(190, Unit.PIXELS);
    externalUserLayout.setStyleName(NovaForge.LAYOUT_SELECTABLE);
    externalUserLayout.setMargin(true);
    externalUserLayout.setSpacing(true);

    //TODO
    groupUserLogo.setSource(new ThemeResource(NovaForgeResources.ICON_GROUP_BIG));
    groupUserLogo.setWidth(LOGO__WIDTH_HEIGHT, Unit.PIXELS);
    groupUserLogo.setHeight(LOGO__WIDTH_HEIGHT, Unit.PIXELS);
    groupUserLabel.setStyleName(NovaForge.LABEL_H1);
    groupUserLabel.setSizeUndefined();
    groupUserLabel.addStyleName(NovaForge.LABEL_GREEN);
    groupUserLayout.setWidth(355, Unit.PIXELS);
    groupUserLayout.setHeight(190, Unit.PIXELS);
    groupUserLayout.setStyleName(NovaForge.LAYOUT_SELECTABLE);
    groupUserLayout.setMargin(true);
    groupUserLayout.setSpacing(true);

    projectUserLayout.addComponent(projectUserLogo);
    projectUserLayout.addComponent(projectUserLabel);
    projectUserLayout.setComponentAlignment(projectUserLabel, Alignment.MIDDLE_CENTER);
    projectUserLayout.setComponentAlignment(projectUserLogo, Alignment.MIDDLE_CENTER);
    gridLayout.addComponent(projectUserLayout, 0, 0);

    forgeUserLayout.addComponent(forgeUserLogo);
    forgeUserLayout.addComponent(forgeUserLabel);
    forgeUserLayout.setComponentAlignment(forgeUserLabel, Alignment.MIDDLE_CENTER);
    forgeUserLayout.setComponentAlignment(forgeUserLogo, Alignment.MIDDLE_CENTER);
    gridLayout.addComponent(forgeUserLayout, 1, 0);

    externalUserLayout.addComponent(externalUserLogo);
    externalUserLayout.addComponent(externalUserLabel);
    externalUserLayout.setComponentAlignment(externalUserLabel, Alignment.MIDDLE_CENTER);
    externalUserLayout.setComponentAlignment(externalUserLogo, Alignment.MIDDLE_CENTER);
    gridLayout.addComponent(externalUserLayout, 1, 1);

    groupUserLayout.addComponent(groupUserLogo);
    groupUserLayout.addComponent(groupUserLabel);
    groupUserLayout.setComponentAlignment(groupUserLabel, Alignment.MIDDLE_CENTER);
    groupUserLayout.setComponentAlignment(groupUserLogo, Alignment.MIDDLE_CENTER);
    gridLayout.addComponent(groupUserLayout, 0, 1);

    contentLayout.addComponent(chooseTypeLabel);
    contentLayout.addComponent(gridLayout);

    contentLayout.setMargin(true);
    contentLayout.setSpacing(true);

    addListeners();

  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    getProjectUserLayout().addLayoutClickListener(new LayoutClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = -4282705521251262945L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void layoutClick(LayoutClickEvent event)
      {
        if (getProjectUserLayout().getStyleName().equals(NovaForge.LAYOUT_SELECTABLE))
        {
          getForgeUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getForgeUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getExternalUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getExternalUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getGroupUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getGroupUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getProjectUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTABLE);
          getProjectUserLayout().setStyleName(NovaForge.LAYOUT_SELECTED);
          subscribeType = SubscribeType.PROJECT;
          wizard.getNextButton().setEnabled(true);
        }
        else
        {
          getProjectUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getProjectUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          subscribeType = null;
          wizard.getNextButton().setEnabled(false);
        }
      }
    });
    getForgeUserLayout().addLayoutClickListener(new LayoutClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = -3099336288991870456L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void layoutClick(LayoutClickEvent event)
      {
        if (getForgeUserLayout().getStyleName().equals(NovaForge.LAYOUT_SELECTABLE))
        {
          getProjectUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getProjectUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getExternalUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getExternalUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getGroupUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getGroupUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getForgeUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTABLE);
          getForgeUserLayout().setStyleName(NovaForge.LAYOUT_SELECTED);
          subscribeType = SubscribeType.FORGE;
          wizard.getNextButton().setEnabled(true);
        }
        else
        {
          getForgeUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getForgeUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          subscribeType = null;
          wizard.getNextButton().setEnabled(false);
        }
      }
    });
    getExternalUserLayout().addLayoutClickListener(new LayoutClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = -3099336288991870456L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void layoutClick(LayoutClickEvent event)
      {
        if (getExternalUserLayout().getStyleName().equals(NovaForge.LAYOUT_SELECTABLE))
        {
          getProjectUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getProjectUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getForgeUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getForgeUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getGroupUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getGroupUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getExternalUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTABLE);
          getExternalUserLayout().setStyleName(NovaForge.LAYOUT_SELECTED);
          subscribeType = SubscribeType.EXTERNAL;
          wizard.getNextButton().setEnabled(true);
        }
        else
        {
          getExternalUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getExternalUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          subscribeType = null;
          wizard.getNextButton().setEnabled(false);
        }
      }
    });
    
    getGroupUserLayout().addLayoutClickListener(new LayoutClickListener()
    {

      /**
       * Default serial UID
       */
      private static final long serialVersionUID = -3099336288991870456L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void layoutClick(LayoutClickEvent event)
      {
        if (getGroupUserLayout().getStyleName().equals(NovaForge.LAYOUT_SELECTABLE))
        {
          getProjectUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getProjectUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getForgeUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getForgeUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getExternalUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getExternalUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          getGroupUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTABLE);
          getGroupUserLayout().setStyleName(NovaForge.LAYOUT_SELECTED);
          subscribeType = SubscribeType.GROUP;
          wizard.getNextButton().setEnabled(true);
        }
        else
        {
          getGroupUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
          getGroupUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
          subscribeType = null;
          wizard.getNextButton().setEnabled(false);
        }
      }
    });
  }

  /**
   * Get the Project User {@link HorizontalLayout}
   *
   * @return the layout
   */
  public VerticalLayout getProjectUserLayout()
  {
    return projectUserLayout;
  }

  /**
   * Get the Forge User {@link HorizontalLayout}
   *
   * @return the layout
   */
  public VerticalLayout getForgeUserLayout()
  {
    return forgeUserLayout;
  }

  /**
   * Get the External User {@link HorizontalLayout}
   *
   * @return the layout
   */
  public VerticalLayout getExternalUserLayout()
  {
    return externalUserLayout;
  }

  /**
   * @return the groupUserLayout
   */
  public VerticalLayout getGroupUserLayout()
  {
    return groupUserLayout;
  }

  /**
   * {@inheritDoc}
   */

  @Override
  public String getCaption()
  {
    return MailingModule.getPortalMessages().getMessage(UI.getCurrent().getLocale(),
        Messages.MAILING_LISTS_ADD_STEPONE_TITLE);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getContent()
  {
    return contentLayout;
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
    return false;
  }

  /**
   * Empty the content datas
   */
  public void emptyDatas()
  {
    getProjectUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
    getProjectUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
    getForgeUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
    getForgeUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
    getExternalUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
    getExternalUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);
    getGroupUserLayout().removeStyleName(NovaForge.LAYOUT_SELECTED);
    getGroupUserLayout().setStyleName(NovaForge.LAYOUT_SELECTABLE);

  }

  /**
   * Get the choose type {@link Label}
   *
   * @return the label
   */
  public Label getChooseTypeLabel()
  {
    return chooseTypeLabel;
  }

  /**
   * Refresh internationalized content with given {@link Locale}
   * 
   * @param pLocale
   *          the locale to use
   */
  public void refreshLocale(final Locale pLocale)
  {
    chooseTypeLabel.setValue(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_ADD_SUBSCRIBTION_TYPE));
    projectUserLabel.setValue(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_PROJECT_USER_LABEL));
    forgeUserLabel.setValue(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_FORGE_USER_LABEL));
    externalUserLabel.setValue(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_EXTERNAL_USER_LABEL));
    //TODO
    groupUserLabel.setValue(MailingModule.getPortalMessages().getMessage(pLocale,
        Messages.MAILING_LISTS_GROUP_USER_LABEL));

  }

  /**
   * Get the {@link SubscribeType} choosen by user
   * 
   * @return
   */
  public SubscribeType getSubscribeType()
  {
    return subscribeType;
  }

  /**
   * Set the {@link SubscribeType}
   * 
   * @param subscribe
   *          Type
   *          to set
   */
  public void setSubscribeType(SubscribeType subscribeType)
  {
    this.subscribeType = subscribeType;
  }

  /**
   * @return the projectUserLogo
   */
  public Embedded getProjectUserLogo()
  {
    return projectUserLogo;
  }

  /**
   * @return the forgeUserLogo
   */
  public Embedded getForgeUserLogo()
  {
    return forgeUserLogo;
  }

}
