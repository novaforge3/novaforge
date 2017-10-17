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
package org.novaforge.forge.ui.user.management.internal.client.userpublicprofile;

import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.user.management.internal.client.common.Utils;
import org.novaforge.forge.ui.user.management.internal.client.components.SectionType;
import org.novaforge.forge.ui.user.management.internal.module.PublicModule;
import org.vaadin.addon.itemlayout.horizontal.ItemHorizontal;

import java.util.Locale;

/**
 * The UserPublicProfileView implementation
 * 
 * @author caseryj
 */
public class UserPublicProfileViewImpl extends VerticalLayout implements UserPublicProfileView
{

  /**
   * Default serial verion UID
   */
  private static final long      serialVersionUID               = 1334324603205118572L;

  /**
   * Define default popup window size
   */
  private static final int       WINDOW_SIZE                    = 480;
  /**
   * The admin actions layout
   */
  private final HorizontalLayout adminActionsLayout             = new HorizontalLayout();
  /**
   * The admin action return to users admin layout
   */
  private final HorizontalLayout adminActionReturnToUsersLayout = new HorizontalLayout();
  /**
   * The admin action delete user layout
   */
  private final HorizontalLayout adminActionDeleteUserLayout    = new HorizontalLayout();
  /**
   * The back to users admin button
   */
  private final Button           backToUsersAdminButton         = new Button();
  /**
   * The delete user button
   */
  private final Button           deleteUserButton               = new Button();
  /**
   * The section infos layout
   */
  private final HorizontalLayout sectionInfos                   = new HorizontalLayout();
  /**
   * The section contact layout
   */
  private final HorizontalLayout sectionContact                 = new HorizontalLayout();
  /**
   * The section work layout
   */
  private final HorizontalLayout sectionWork                    = new HorizontalLayout();
  /**
   * The section projects layout
   */
  private final HorizontalLayout sectionProjects                = new HorizontalLayout();
  /**
   * The section infos form
   */
  private final Form             infosForm                      = new Form();
  /**
   * The section contact form
   */
  private final Form             contactForm                    = new Form();
  /**
   * The user picture
   */
  private final Embedded         userPicture                    = new Embedded();
  /**
   * The user firstname field caption
   */
  private final Label            userFisrtnameFieldCaption      = new Label();
  /**
   * The user lastnmae field caption
   */
  private final Label            userLastnameFieldCaption       = new Label();
  /**
   * The user login field caption
   */
  private final Label            userLoginFieldCaption          = new Label();
  /**
   * The user language field caption
   */
  private final Label            userLanguageCaption            = new Label();
  /**
   * The user language field
   */
  private final Label            userLanguage                   = new Label();
  /**
   * The user company name field caption
   */
  private final Label            userCompanyNameFieldCaption    = new Label();
  /**
   * The user company address field caption
   */
  private final Label            userCompanyAddressFieldCaption = new Label();
  /**
   * The user company office field caption
   */
  private final Label            userCompanyOfficeFieldCaption  = new Label();
  /**
   * The user firstname field
   */
  private final Label            userFisrtnameField             = new Label();
  /**
   * The user lastname field
   */
  private final Label            userLastnameField              = new Label();
  /**
   * The user login field
   */
  private final Label            userLoginField                 = new Label();
  /**
   * The user email field
   */
  private final HorizontalLayout userEmailFieldLayout           = new HorizontalLayout();
  /**
   * The user email field caption
   */
  private final Label            userEmailFieldCaption          = new Label();
  /**
   * The user phone work field caption
   */
  private final Label            userPhoneWorkFieldCaption      = new Label();
  /**
   * The user phone work field
   */
  private final Label            userPhoneWorkField             = new Label();
  /**
   * The user phone mobile field caption
   */
  private final Label            userPhoneMobileFieldCaption    = new Label();
  /**
   * The user phone mobile field
   */
  private final Label            userPhoneMobileField           = new Label();
  /**
   * The user company name field
   */
  private final Label            userCompanyNameField           = new Label();
  /**
   * The company address field
   */
  private final Label            userCompanyAddressField        = new Label();
  /**
   * The company office field
   */
  private final Label            userCompanyOfficeField         = new Label();
  /**
   * The section work form
   */
  private final Form             workForm                       = new Form();
  /**
   * The section projects form
   */
  private final Form             projectsForm                   = new Form();
  /**
   * The projects layout
   */
  private final ItemHorizontal   projectsLayout                 = new ItemHorizontal();
  /**
   * the confirm delete user window
   */
  private DeleteConfirmWindow confirmDeleteUserWindow;

  /**
   * Default constructor
   */
  public UserPublicProfileViewImpl()
  {
    setMargin(new MarginInfo(true));
    setSpacing(true);
    initLayout();
  }

  /**
   * Initialize the layout
   */
  private void initLayout()
  {
    initAdminActionsLayout();
    initSectionInfos();
    initSectionContact();
    initSectionWork();
    initSectionProjects();
    addComponent(sectionInfos);
    addComponent(sectionContact);
    addComponent(sectionWork);
    addComponent(sectionProjects);
    initPopups();
  }

  /**
   * Initialize the admin features layout
   */
  private void initAdminActionsLayout()
  {

    adminActionsLayout.setSpacing(true);
    backToUsersAdminButton.setIcon(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT));
    deleteUserButton.setIcon(new ThemeResource(NovaForgeResources.ICON_TRASH_RED));
    adminActionsLayout.addComponent(adminActionReturnToUsersLayout);
    adminActionsLayout.addComponent(adminActionDeleteUserLayout);
    adminActionsLayout.setComponentAlignment(adminActionReturnToUsersLayout, Alignment.MIDDLE_LEFT);
    adminActionsLayout.setComponentAlignment(adminActionDeleteUserLayout, Alignment.MIDDLE_LEFT);
    addComponent(adminActionsLayout);
  }

  /**
   * Initialize the section infos layout
   */
  private void initSectionInfos()
  {
    sectionInfos.setWidth(100, Sizeable.Unit.PERCENTAGE);
    infosForm.setWidth(100, Unit.PERCENTAGE);
    infosForm.setStyleName(NovaForge.POSITION_RELATIVE);
    final VerticalLayout pictureLayout = new VerticalLayout();
    userPicture.setWidth(Utils.USER_PROFILE_PICTURE_SIZE, Unit.PIXELS);
    userPicture.setHeight(Utils.USER_PROFILE_PICTURE_SIZE, Unit.PIXELS);
    userPicture.setType(Embedded.TYPE_IMAGE);
    pictureLayout.addComponent(userPicture);
    pictureLayout.setComponentAlignment(userPicture, Alignment.TOP_CENTER);
    final VerticalLayout fieldLayout = new VerticalLayout();
    fieldLayout.setSpacing(true);
    final GridLayout fieldGridLayout = new GridLayout(2, 5);
    fieldGridLayout.setSpacing(true);
    fieldGridLayout.addComponent(userFisrtnameFieldCaption, 0, 0);
    fieldGridLayout.setComponentAlignment(userFisrtnameFieldCaption, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userFisrtnameField, 1, 0);
    fieldGridLayout.setComponentAlignment(userFisrtnameField, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userLastnameFieldCaption, 0, 1);
    fieldGridLayout.setComponentAlignment(userLastnameFieldCaption, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userLastnameField, 1, 1);
    fieldGridLayout.setComponentAlignment(userLastnameField, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userLoginFieldCaption, 0, 2);
    fieldGridLayout.setComponentAlignment(userLoginFieldCaption, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userLoginField, 1, 2);
    fieldGridLayout.setComponentAlignment(userLoginField, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userEmailFieldCaption, 0, 3);
    fieldGridLayout.setComponentAlignment(userEmailFieldCaption, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userEmailFieldLayout, 1, 3);
    fieldGridLayout.setComponentAlignment(userEmailFieldLayout, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userLanguageCaption, 0, 4);
    fieldGridLayout.setComponentAlignment(userLanguageCaption, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userLanguage, 1, 4);
    fieldGridLayout.setComponentAlignment(userLanguage, Alignment.MIDDLE_LEFT);
    fieldLayout.addComponent(fieldGridLayout);
    userPicture.setType(Embedded.TYPE_IMAGE);
    final GridLayout infosGrid = new GridLayout(2, 1);
    infosGrid.setSpacing(true);
    infosGrid.addComponent(pictureLayout, 0, 0);
    infosGrid.addComponent(fieldLayout, 1, 0);
    /*
    The section infos form layout
   */
    final FormLayout infosFormLayout = (FormLayout) infosForm.getLayout();
    infosFormLayout.setWidth(100, Unit.PERCENTAGE);
    infosFormLayout.addComponent(infosGrid);
    infosFormLayout.setComponentAlignment(infosGrid, Alignment.MIDDLE_CENTER);
    sectionInfos.addComponent(infosForm);
  }

  /**
   * Initialze the section contact layout
   */
  private void initSectionContact()
  {
    sectionContact.setWidth(100, Sizeable.Unit.PERCENTAGE);
    contactForm.setWidth(100, Unit.PERCENTAGE);
    contactForm.setStyleName(NovaForge.POSITION_RELATIVE);
    /*
    The section contact form layout
   */
    final FormLayout contactFormLayout = (FormLayout) contactForm.getLayout();
    contactFormLayout.setWidth(100, Unit.PERCENTAGE);
    contactFormLayout.setMargin(new MarginInfo(false));
    final GridLayout contactGrid = new GridLayout(3, 2);
    contactGrid.setSpacing(true);
    contactGrid.addComponent(userPhoneMobileFieldCaption, 0, 0);
    contactGrid.setComponentAlignment(userPhoneMobileFieldCaption, Alignment.MIDDLE_LEFT);
    contactGrid.addComponent(userPhoneMobileField, 1, 0);
    contactGrid.setComponentAlignment(userPhoneMobileField, Alignment.MIDDLE_LEFT);
    contactGrid.addComponent(userPhoneWorkFieldCaption, 0, 1);
    contactGrid.setComponentAlignment(userPhoneWorkFieldCaption, Alignment.MIDDLE_LEFT);
    contactGrid.addComponent(userPhoneWorkField, 1, 1);
    contactGrid.setComponentAlignment(userPhoneWorkField, Alignment.MIDDLE_LEFT);
    contactFormLayout.addComponent(contactGrid);
    sectionContact.addComponent(contactForm);
  }

  /**
   * Initialize the section work
   */
  private void initSectionWork()
  {
    sectionWork.setWidth(100, Sizeable.Unit.PERCENTAGE);
    workForm.setWidth(100, Unit.PERCENTAGE);
    workForm.setStyleName(NovaForge.POSITION_RELATIVE);
    /*
    The section work form layout
   */
    final FormLayout workFormLayout = (FormLayout) workForm.getLayout();
    workFormLayout.setWidth(100, Unit.PERCENTAGE);
    workFormLayout.setMargin(new MarginInfo(false));
    final GridLayout workGrid = new GridLayout(3, 3);
    workGrid.setSpacing(true);
    workGrid.addComponent(userCompanyNameFieldCaption, 0, 0);
    workGrid.setComponentAlignment(userCompanyNameFieldCaption, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyNameField, 1, 0);
    workGrid.setComponentAlignment(userCompanyNameField, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyAddressFieldCaption, 0, 1);
    workGrid.setComponentAlignment(userCompanyAddressFieldCaption, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyAddressField, 1, 1);
    workGrid.setComponentAlignment(userCompanyAddressField, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyOfficeFieldCaption, 0, 2);
    workGrid.setComponentAlignment(userCompanyOfficeFieldCaption, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyOfficeField, 1, 2);
    workGrid.setComponentAlignment(userCompanyOfficeField, Alignment.MIDDLE_CENTER);
    workFormLayout.addComponent(workGrid);
    sectionWork.addComponent(workForm);
  }

  /**
   * Initialize the section projects layout
   */
  private void initSectionProjects()
  {
    sectionProjects.setWidth(100, Sizeable.Unit.PERCENTAGE);
    projectsForm.setWidth(100, Unit.PERCENTAGE);
    projectsForm.setStyleName(NovaForge.POSITION_RELATIVE);
    /*
    The section projects form layout
   */
    final FormLayout projectsFormLayout = (FormLayout) projectsForm.getLayout();
    projectsFormLayout.setWidth(100, Unit.PERCENTAGE);
    projectsFormLayout.setMargin(new MarginInfo(false));
    projectsLayout.setWidth(100, Unit.PERCENTAGE);
    projectsLayout.setSelectable(true);
    projectsLayout.addStyleName(NovaForge.USER_PROFILE_PROJECTS);
    projectsFormLayout.addComponent(projectsLayout);
    sectionProjects.addComponent(projectsForm);
  }

  /**
   * Initialize the view popups
   */
  private void initPopups()
  {
    initConfirmDeleteUserWindow();
  }

  /**
   * Initialize confirm delete user window
   */
  private void initConfirmDeleteUserWindow()
  {
    confirmDeleteUserWindow = new DeleteConfirmWindow(Messages.USERMANAGEMENT_DELETE_USER_CONFIRM);
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
  public Button getBackToUsersAdminButton()
  {
    return backToUsersAdminButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    // Get messages service
    final PortalMessages portalMessages = PublicModule.getPortalMessages();
    // Section Admin actions
    backToUsersAdminButton.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_BACK_USERS_ADMIN));
    deleteUserButton.setCaption(portalMessages
        .getMessage(pLocale, Messages.USERMANAGEMENT_ACTION_DELETE_USER));
    // Section Infos
    infosForm.setCaption(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_SECTION_INFOS));
    userFisrtnameFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_FIRSTNAME));
    userLastnameFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_LASTNAME));
    userLoginFieldCaption.setValue(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_FIELD_LOGIN));
    userEmailFieldCaption.setValue(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_FIELD_EMAIL));
    userLanguageCaption.setValue(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_FIELD_LANGUAGE));
    // Section Contact
    contactForm.setCaption(portalMessages.getMessage(pLocale, SectionType.CONTACT.getNameKey()));
    userPhoneMobileFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_PHONE_MOBILE));
    userPhoneWorkFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_PHONE_WORK));
    // Section Work
    workForm.setCaption(portalMessages.getMessage(pLocale, SectionType.WORK.getNameKey()));
    userCompanyNameFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_COMPANY_NAME));
    userCompanyAddressFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_COMPANY_ADDRESS));
    userCompanyOfficeFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_COMPANY_OFFICE));
    // Section Projects
    projectsForm.setCaption(portalMessages.getMessage(pLocale, SectionType.PROJECTS.getNameKey()));
    // Delete user confirm window
    confirmDeleteUserWindow.refreshLocale(pLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getDeleteUserButton()
  {
    return deleteUserButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Embedded getUserPicture()
  {
    return userPicture;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserLanguage()
  {
    return userLanguage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserFisrtnameField()
  {
    return userFisrtnameField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserLastnameField()
  {
    return userLastnameField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserLoginField()
  {
    return userLoginField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HorizontalLayout getUserEmailLayout()
  {
    return userEmailFieldLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserPhoneWorkField()
  {
    return userPhoneWorkField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserPhoneMobileField()
  {
    return userPhoneMobileField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserCompanyNameField()
  {
    return userCompanyNameField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserCompanyAddressField()
  {
    return userCompanyAddressField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserCompanyOfficeField()
  {
    return userCompanyOfficeField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showSectionContact(final boolean pIsShow)
  {
    sectionContact.removeAllComponents();
    if (pIsShow)
    {
      sectionContact.addComponent(contactForm);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showSectionWork(final boolean pIsShow)
  {
    sectionWork.removeAllComponents();
    if (pIsShow)
    {
      sectionWork.addComponent(workForm);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showSectionProjects(final boolean pIsShow)
  {
    sectionProjects.removeAllComponents();
    if (pIsShow)
    {
      sectionProjects.addComponent(projectsForm);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAdminActionsEnabled(final boolean pIsAdmin, final boolean pIsFromAdminView)
  {
    adminActionReturnToUsersLayout.removeAllComponents();
    adminActionDeleteUserLayout.removeAllComponents();
    if (pIsAdmin)
    {
      adminActionDeleteUserLayout.addComponent(deleteUserButton);
    }
    if (pIsFromAdminView)
    {
      adminActionReturnToUsersLayout.addComponent(backToUsersAdminButton);
    }
    adminActionsLayout.setSizeUndefined();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemHorizontal getProjectList()
  {
    return projectsLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserPhoneMobileFieldCaption()
  {
    return userPhoneMobileFieldCaption;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserPhoneWorkFieldCaption()
  {
    return userPhoneWorkFieldCaption;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserCompanyOfficeFieldCaption()
  {
    return userCompanyOfficeFieldCaption;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserCompanyAddressFieldCaption()
  {
    return userCompanyAddressFieldCaption;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getUserCompanyNameFieldCaption()
  {
    return userCompanyNameFieldCaption;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getConfirmDeleteUserWindow()
  {
    return confirmDeleteUserWindow;
  }

}
