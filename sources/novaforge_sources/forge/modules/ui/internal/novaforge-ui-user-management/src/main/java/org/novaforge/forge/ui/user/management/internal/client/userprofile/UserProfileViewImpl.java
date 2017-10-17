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
package org.novaforge.forge.ui.user.management.internal.client.userprofile;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.portal.client.component.UploadFieldCustom;
import org.novaforge.forge.ui.portal.client.component.UploadImageComponent;
import org.novaforge.forge.ui.portal.data.validator.DualPasswordValidator;
import org.novaforge.forge.ui.portal.data.validator.NameValidator;
import org.novaforge.forge.ui.portal.data.validator.NewPasswordValidator;
import org.novaforge.forge.ui.portal.data.validator.PasswordValidator;
import org.novaforge.forge.ui.portal.data.validator.PhoneNumberValidator;
import org.novaforge.forge.ui.user.management.internal.client.components.SectionType;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;
import org.novaforge.forge.ui.user.management.internal.module.PrivateModule;

import java.util.Locale;

/**
 * The UserProfileView implementation
 * 
 * @author Jeremy Casery
 */
public class UserProfileViewImpl extends VerticalLayout implements UserProfileView
{
  /**
   * Min lenght for the field FirstName.
   */
  public static final int NAME_FIELD_MIN_LENGHT = 1;
  /**
   * Max lenght for the field FirstName.
   */
  public static final int NAME_FIELD_MAX_LENGHT = 255;
  /**
   * Default serial version UID
   */
  private static final long             serialVersionUID               = 4668481929070580783L;
  /** Define default popup window size */
  private static final int              WINDOW_WIDTH                   = 480;
  /** The admin actions Layout */
  private final        HorizontalLayout adminActionsLayout             = new HorizontalLayout();
  /** The admin action return to users list Layout */
  private final        HorizontalLayout adminActionReturnToUsersLayout = new HorizontalLayout();
  /** The admin action delete user layout */
  private final        HorizontalLayout adminActionDeleteUserLayout    = new HorizontalLayout();
  /** The back to users admin button */
  private final        Button           backToUsersAdminButton         = new Button();
  /** The delete user button */
  private final        Button           deleteUserButton               = new Button();
  /** The section infos layout */
  private final        HorizontalLayout sectionInfos                   = new HorizontalLayout();
  /** The section contact layout */
  private final        HorizontalLayout sectionContact                 = new HorizontalLayout();
  /** The section work layout */
  private final        HorizontalLayout sectionWork                    = new HorizontalLayout();
  /** The section projects layout */
  private final        HorizontalLayout sectionProjects                = new HorizontalLayout();
  /** The update password button */
  private final        Button           updatePasswdButton             = new Button();
  /** the user infos form */
  private final        Form             infosForm                      = new Form();
  /** the user contact form */
  private final        Form             contactForm                    = new Form();
  /** The section contact button layout */
  private final        HorizontalLayout buttonSectionContactLayout     = new HorizontalLayout();

  /** The edit section contact button */
  private final Button editSectionContactButton = new Button();

  /** The remove section contact button */
  private final Button removeSectionContactButton = new Button();

  /** The edit section contact layout */
  private final HorizontalLayout editSectionContactLayout = new HorizontalLayout();

  /** The save section contact button */
  private final Button saveSectionContactButton = new Button();

  /** The cancel edit section contact button */
  private final Button cancelSectionContactButton = new Button();

  /** The editing section contact layout */
  private final HorizontalLayout editingSectionContactLayout = new HorizontalLayout();

  /** the user email field */
  private final TextField userEmailField = new TextField();

  /** the user email field caption */
  private final Label userEmailFieldCaption = new Label();

  /** the user phone work field caption */
  private final Label userPhoneWorkFieldCaption = new Label();

  /** the user phone work field */
  private final TextField userPhoneWorkField = new TextField();

  /** the user phone work public field */
  private final CheckBox userPhoneWorkFieldPublic = new CheckBox();

  /** the user phone mobile field caption */
  private final Label userPhoneMobileFieldCaption = new Label();

  /** the user phone mobile field */
  private final TextField userPhoneMobileField = new TextField();

  /** the user phone mobile public field */
  private final CheckBox userPhoneMobileFieldPublic = new CheckBox();

  /** the user work form */
  private final Form              workForm                       = new Form();
  /** the section work buttons layout */
  private final HorizontalLayout  buttonSectionWorkLayout        = new HorizontalLayout();
  /** The edit section work button */
  private final Button            editSectionWorkButton          = new Button();
  /** The remove section work button */
  private final Button            removeSectionWorkButton        = new Button();
  /** The edit section work layout */
  private final HorizontalLayout  editSectionWorkLayout          = new HorizontalLayout();
  /** The save section work button */
  private final Button            saveSectionWorkButton          = new Button();
  /** The cancel edit section work button */
  private final Button            cancelSectionWorkButton        = new Button();
  /** The editing section work layout */
  private final HorizontalLayout  editingSectionWorkLayout       = new HorizontalLayout();
  /** The user company name field */
  private final TextField         userCompanyNameField           = new TextField();
  /** The user company name public field */
  private final CheckBox          userCompanyNameFieldPublic     = new CheckBox();
  /** The user company address field */
  private final TextArea          userCompanyAddressField        = new TextArea();
  /** The user company address public field */
  private final CheckBox          userCompanyAddressFieldPublic  = new CheckBox();
  /** The user company office field */
  private final TextField         userCompanyOfficeField         = new TextField();
  /** The user company office public field */
  private final CheckBox          userCompanyOfficeFieldPublic   = new CheckBox();
  /** The section projects form */
  private final Form              projectsForm                   = new Form();
  /** The section projects buttons layout */
  private final HorizontalLayout  buttonSectionProjectsLayout    = new HorizontalLayout();
  /** The edit section projects button */
  private final Button            editSectionProjectsButton      = new Button();
  /** The remove section projects button */
  private final Button            removeSectionProjectsButton    = new Button();
  /** The edit section projects layout */
  private final HorizontalLayout  editSectionProjectsLayout      = new HorizontalLayout();
  /** The save section projects button */
  private final Button            saveSectionProjectsButton      = new Button();
  /** The cancel edit section projects button */
  private final Button            cancelSectionProjectsButton    = new Button();
  /** The editing section projects layout */
  private final HorizontalLayout  editingSectionProjectsLayout   = new HorizontalLayout();
  /** The user projects public field */
  private final CheckBox          userProjectsFieldPublic        = new CheckBox();
  /** The section infos buttons layout */
  private final HorizontalLayout  buttonSectionInfosLayout       = new HorizontalLayout();
  /** The edit section infos button */
  private final Button            editSectionInfosButton         = new Button();
  /** The edit section infos layout */
  private final HorizontalLayout  editSectionInfosLayout         = new HorizontalLayout();
  /** The save section infos button */
  private final Button            saveSectionInfosButton         = new Button();
  /** The cancel edit section infos button */
  private final Button            cancelSectionInfosButton       = new Button();
  /** The editing section infos layout */
  private final HorizontalLayout  editingSectionInfosLayout      = new HorizontalLayout();
  /** The add section layout */
  private final HorizontalLayout  addSectionLayout               = new HorizontalLayout();
  /** The add section button */
  private final Button            addSectionButton               = new Button();
  /** The user firstname field */
  private final TextField         userFirstnameField             = new TextField();
  /** The user lastname field */
  private final TextField         userLastnameField              = new TextField();
  /** The user login field */
  private final TextField         userLoginField                 = new TextField();
  /** The user language combobox field */
  private final ComboBox          userLanguage                   = new ComboBox();
  /** The update password window */
  private final Window            updatePasswdWindow             = new Window();
  /** The password security rule label */
  private final Label             passwdSecurityRuleLabel        = new Label();
  /** The new password matching security rule label */
  private final CssLayout         newPasswdRulesIcon             = new CssLayout();
  /** The new passwords matching label */
  private final CssLayout         newPasswdsAgainMatchingIcon    = new CssLayout();
  /** The current password field */
  private final PasswordField     currentPasswd                  = new PasswordField();
  /** The new password field */
  private final PasswordField     newPasswd                      = new PasswordField();
  /** The new again password field */
  private final PasswordField     newAgainPasswd                 = new PasswordField();
  /** The confirm update password button */
  private final Button            confirmUpdatePasswdButton      = new Button();
  private final Button            cancelUpdatePasswdButton       = new Button();
  /** The password regex validator */
  private final PasswordValidator passwordRegexValidator         = new PasswordValidator();
  /** The add section window */
  private final Window            addSectionWindow               = new Window();
  /** The add section window label */
  private final Label             addSectionWindowLabel          = new Label();
  /** The add section window combobox field */
  private final ComboBox          addSectionWindowCombobox       = new ComboBox();
  /** The add section window button */
  private final Button            addSectionWindowButton         = new Button();
  /** The user firstname field caption */
  private final Label             userFirstnameFieldCaption      = new Label();
  /** The user lastname field caption */
  private final Label             userLastnameFieldCaption       = new Label();
  /** the user login field caption */
  private final Label             userLoginFieldCaption          = new Label();
  /** the user language field caption */
  private final Label             userLanguageCaption            = new Label();
  /** the user company name field caption */
  private final Label             userCompanyNameFieldCaption    = new Label();
  /** the user company address field caption */
  private final Label             userCompanyAddressFieldCaption = new Label();
  /** the user company office field caption */
  private final Label             userCompanyOfficeFieldCaption  = new Label();
  private UploadImageComponent imageComponent;
  /** the confirm delete user window */
  private DeleteConfirmWindow  confirmDeleteUserWindow;
  /** Is current user admin */
  private boolean              isAdmin;

  /**
   * Default constructor
   */
  public UserProfileViewImpl()
  {
    setMargin(new MarginInfo(true));
    setSpacing(true);
    initLayout();
  }

  /**
   * initialize Layout
   */
  private void initLayout()
  {
    initAdminActionsLayout();
    initSectionInfos();
    initSectionContact();
    initSectionWork();
    initSectionProjects();
    initAddSection();
    addComponent(sectionInfos);
    addComponent(sectionContact);
    addComponent(sectionWork);
    addComponent(sectionProjects);
    addComponent(addSectionLayout);
    initWindowUpdatePassword();
    initaddSectionWindow();
    initConfirmDeleteUserWindow();
  }

  /**
   * initialize admin actions layout
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
   * initialize section infos
   */
  private void initSectionInfos()
  {
    initSectionInfosButtons();
    sectionInfos.setWidth(100, Sizeable.Unit.PERCENTAGE);
    infosForm.setWidth(100, Unit.PERCENTAGE);
    infosForm.setStyleName(NovaForge.POSITION_RELATIVE);

    /* Max size for user image : 512Ko. */
    final int IMAGE_MAX_SIZE = 524288;
    imageComponent = new UploadImageComponent(UploadFieldCustom.ICON_SIZE, IMAGE_MAX_SIZE);
    imageComponent.setWindowTitleKeyMessage(Messages.USERMANAGEMENT_UPDATE_PICTURE);
    imageComponent.setHelpCaptionKeyMessage(Messages.USERMANAGEMENT_UPDATE_PICTURE_HELP);
    imageComponent.setUploadButtonDescriptionKeyMessage(Messages.USERMANAGEMENT_UPLOAD_DESCRIPTION);
    imageComponent.setUploadCaptionKeyMessage(Messages.USERMANAGEMENT_UPLOAD_CAPTION);
    imageComponent.setConfirmButtonCaptionKeyMessage(Messages.PROJECT_MAIN_ICON_CONFIRM);

    final VerticalLayout fieldLayout = new VerticalLayout();
    fieldLayout.setSpacing(true);
    updatePasswdButton.setStyleName(NovaForge.BUTTON_LINK);
    final GridLayout fieldGridLayout = new GridLayout(2, 5);
    fieldGridLayout.setSpacing(true);
    userFirstnameField.setRequired(true);
    userFirstnameField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    userLastnameField.setRequired(true);
    userLastnameField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    userLoginField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    userEmailField.setRequired(true);
    userEmailField.setWidth(NovaForge.FORM_FIELD_EMAIL_WIDTH);
    userLanguage.setWidth(NovaForge.FORM_FIELD_WIDTH);
    fieldGridLayout.addComponent(userFirstnameFieldCaption, 0, 0);
    fieldGridLayout.setComponentAlignment(userFirstnameFieldCaption, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userFirstnameField, 1, 0);
    fieldGridLayout.setComponentAlignment(userFirstnameField, Alignment.MIDDLE_LEFT);
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
    fieldGridLayout.addComponent(userEmailField, 1, 3);
    fieldGridLayout.setComponentAlignment(userEmailField, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userLanguageCaption, 0, 4);
    fieldGridLayout.setComponentAlignment(userLanguageCaption, Alignment.MIDDLE_LEFT);
    fieldGridLayout.addComponent(userLanguage, 1, 4);
    fieldGridLayout.setComponentAlignment(userLanguage, Alignment.MIDDLE_LEFT);
    fieldLayout.addComponent(fieldGridLayout);
    fieldLayout.addComponent(updatePasswdButton);
    final GridLayout infosGrid = new GridLayout(2, 1);
    infosGrid.setSpacing(true);
    infosGrid.addComponent(imageComponent, 0, 0);
    infosGrid.addComponent(fieldLayout, 1, 0);
    /*
    the user infos form layout
   */
    final FormLayout infosFormLayout = (FormLayout) infosForm.getLayout();
    infosFormLayout.setWidth(100, Unit.PERCENTAGE);
    infosFormLayout.addComponent(buttonSectionInfosLayout);
    infosFormLayout.addComponent(infosGrid);
    infosFormLayout.setComponentAlignment(infosGrid, Alignment.MIDDLE_CENTER);
    sectionInfos.addComponent(infosForm);
    setSectionInfosEditing(false);
  }

  /**
   * Initialize section contact
   */
  private void initSectionContact()
  {
    initSectionContactButtons();
    sectionContact.setWidth(100, Sizeable.Unit.PERCENTAGE);
    contactForm.setWidth(100, Unit.PERCENTAGE);
    contactForm.setStyleName(NovaForge.POSITION_RELATIVE);
    /*
    the user contact form layout
   */
    final FormLayout contactFormLayout = (FormLayout) contactForm.getLayout();
    contactFormLayout.addComponent(buttonSectionContactLayout);
    contactFormLayout.setWidth(100, Unit.PERCENTAGE);
    contactFormLayout.setMargin(new MarginInfo(false));
    userPhoneMobileField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    userPhoneWorkField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    final GridLayout contactGrid = new GridLayout(3, 2);
    contactGrid.setSpacing(true);
    contactGrid.addComponent(userPhoneMobileFieldCaption, 0, 0);
    contactGrid.setComponentAlignment(userPhoneMobileFieldCaption, Alignment.MIDDLE_LEFT);
    contactGrid.addComponent(userPhoneMobileField, 1, 0);
    contactGrid.setComponentAlignment(userPhoneMobileField, Alignment.MIDDLE_LEFT);
    contactGrid.addComponent(userPhoneMobileFieldPublic, 2, 0);
    contactGrid.setComponentAlignment(userPhoneMobileFieldPublic, Alignment.MIDDLE_LEFT);
    contactGrid.addComponent(userPhoneWorkFieldCaption, 0, 1);
    contactGrid.setComponentAlignment(userPhoneWorkFieldCaption, Alignment.MIDDLE_LEFT);
    contactGrid.addComponent(userPhoneWorkField, 1, 1);
    contactGrid.setComponentAlignment(userPhoneWorkField, Alignment.MIDDLE_LEFT);
    contactGrid.addComponent(userPhoneWorkFieldPublic, 2, 1);
    contactGrid.setComponentAlignment(userPhoneWorkFieldPublic, Alignment.MIDDLE_LEFT);
    contactFormLayout.addComponent(contactGrid);
    sectionContact.addComponent(contactForm);
    setSectionContactEditing(false);
  }

  /**
   * Initialize section work
   */
  private void initSectionWork()
  {
    initSectionWorkButtons();
    sectionWork.setWidth(100, Sizeable.Unit.PERCENTAGE);
    workForm.setWidth(100, Unit.PERCENTAGE);
    workForm.setStyleName(NovaForge.POSITION_RELATIVE);
    /*
    the user work form layout
   */
    final FormLayout workFormLayout = (FormLayout) workForm.getLayout();
    workFormLayout.addComponent(buttonSectionWorkLayout);
    workFormLayout.setWidth(100, Unit.PERCENTAGE);
    workFormLayout.setMargin(new MarginInfo(false));
    userCompanyNameField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    userCompanyOfficeField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    userCompanyAddressField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    userCompanyAddressField.setRows(4);
    final GridLayout workGrid = new GridLayout(3, 3);
    workGrid.setSpacing(true);
    workGrid.addComponent(userCompanyNameFieldCaption, 0, 0);
    workGrid.setComponentAlignment(userCompanyNameFieldCaption, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyNameField, 1, 0);
    workGrid.setComponentAlignment(userCompanyNameField, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyNameFieldPublic, 2, 0);
    workGrid.setComponentAlignment(userCompanyNameFieldPublic, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyAddressFieldCaption, 0, 1);
    workGrid.setComponentAlignment(userCompanyAddressFieldCaption, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyAddressField, 1, 1);
    workGrid.setComponentAlignment(userCompanyAddressField, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyAddressFieldPublic, 2, 1);
    workGrid.setComponentAlignment(userCompanyAddressFieldPublic, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyOfficeFieldCaption, 0, 2);
    workGrid.setComponentAlignment(userCompanyOfficeFieldCaption, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyOfficeField, 1, 2);
    workGrid.setComponentAlignment(userCompanyOfficeField, Alignment.MIDDLE_CENTER);
    workGrid.addComponent(userCompanyOfficeFieldPublic, 2, 2);
    workGrid.setComponentAlignment(userCompanyOfficeFieldPublic, Alignment.MIDDLE_CENTER);
    workGrid.setSizeUndefined();
    workFormLayout.addComponent(workGrid);
    sectionWork.addComponent(workForm);
    setSectionWorkEditing(false);
  }

  /**
   * Initailize section projects
   */
  private void initSectionProjects()
  {
    initSectionProjectsButtons();
    sectionProjects.setWidth(100, Sizeable.Unit.PERCENTAGE);
    projectsForm.setWidth(100, Unit.PERCENTAGE);
    projectsForm.setStyleName(NovaForge.POSITION_RELATIVE);
    /*
    The section projects form layout
   */
    final FormLayout projectsFormLayout = (FormLayout) projectsForm.getLayout();
    projectsFormLayout.addComponent(buttonSectionProjectsLayout);
    projectsFormLayout.setWidth(100, Unit.PERCENTAGE);
    projectsFormLayout.setMargin(new MarginInfo(false));
    projectsFormLayout.addComponent(userProjectsFieldPublic);
    sectionProjects.addComponent(projectsForm);
    setSectionProjectsEditing(false);

  }

  /**
   * initialize add section layout
   */
  private void initAddSection()
  {
    addSectionLayout.setWidth(100, Unit.PERCENTAGE);
    addSectionButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    addSectionButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    addSectionLayout.addComponent(addSectionButton);
    addSectionLayout.setComponentAlignment(addSectionButton, Alignment.MIDDLE_CENTER);
  }

  /**
   * initialize update password window
   */
  private void initWindowUpdatePassword()
  {
    updatePasswdWindow.setModal(true);
    updatePasswdWindow.setResizable(false);
    updatePasswdWindow.setDraggable(false);
    final VerticalLayout updatePasswdLayout = new VerticalLayout();
    updatePasswdLayout.setWidth(WINDOW_WIDTH, Unit.PIXELS);
    updatePasswdLayout.setMargin(new MarginInfo(true));
    updatePasswdLayout.setSpacing(true);
    currentPasswd.setRequired(true);
    currentPasswd.setImmediate(true);
    newPasswd.setRequired(true);
    newAgainPasswd.setRequired(true);

    newPasswdRulesIcon.setStyleName(NovaForge.TEXTFIELD_VALIDATION);
    newPasswdRulesIcon.addComponent(getEmptyIcon());
    newPasswdsAgainMatchingIcon.setStyleName(NovaForge.TEXTFIELD_VALIDATION);
    newPasswdsAgainMatchingIcon.addComponent(getEmptyIcon());

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.setMargin(new MarginInfo(true, false, false, false));
    cancelUpdatePasswdButton.setStyleName(NovaForge.BUTTON_LINK);
    confirmUpdatePasswdButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    confirmUpdatePasswdButton.setIcon(new ThemeResource(NovaForgeResources.ICON_VALIDATE));
    confirmUpdatePasswdButton.setEnabled(false);
    buttons.addComponent(cancelUpdatePasswdButton);
    buttons.addComponent(confirmUpdatePasswdButton);
    buttons.setComponentAlignment(cancelUpdatePasswdButton, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(confirmUpdatePasswdButton, Alignment.MIDDLE_CENTER);

    final HorizontalLayout newPasswdLayout = new HorizontalLayout();
    newPasswdLayout.setSpacing(true);
    newPasswdLayout.addComponent(newPasswd);
    newPasswdLayout.addComponent(newPasswdRulesIcon);
    newPasswdLayout.setComponentAlignment(newPasswd, Alignment.MIDDLE_LEFT);
    newPasswdLayout.setComponentAlignment(newPasswdRulesIcon, Alignment.MIDDLE_LEFT);

    final HorizontalLayout newAgainPasswdLayout = new HorizontalLayout();
    newAgainPasswdLayout.setSpacing(true);
    newAgainPasswdLayout.addComponent(newAgainPasswd);
    newAgainPasswdLayout.addComponent(newPasswdsAgainMatchingIcon);
    newAgainPasswdLayout.setComponentAlignment(newAgainPasswd, Alignment.MIDDLE_LEFT);
    newAgainPasswdLayout.setComponentAlignment(newPasswdsAgainMatchingIcon, Alignment.MIDDLE_LEFT);

    final GridLayout passwdGrid = new GridLayout(1, 3);
    passwdGrid.addComponent(currentPasswd, 0, 0);
    passwdGrid.addComponent(newPasswdLayout, 0, 1);
    passwdGrid.addComponent(newAgainPasswdLayout, 0, 2);
    passwdGrid.setComponentAlignment(currentPasswd, Alignment.MIDDLE_LEFT);
    passwdGrid.setComponentAlignment(newPasswdLayout, Alignment.MIDDLE_CENTER);
    passwdGrid.setComponentAlignment(newAgainPasswdLayout, Alignment.MIDDLE_CENTER);

    updatePasswdLayout.addComponent(passwdSecurityRuleLabel);
    updatePasswdLayout.addComponent(passwdGrid);
    updatePasswdLayout.addComponent(buttons);
    updatePasswdLayout.setComponentAlignment(passwdSecurityRuleLabel, Alignment.TOP_CENTER);
    updatePasswdLayout.setComponentAlignment(passwdGrid, Alignment.MIDDLE_CENTER);
    updatePasswdLayout.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);
    updatePasswdWindow.setContent(updatePasswdLayout);
  }

  /**
   * Initialize add section window
   */
  private void initaddSectionWindow()
  {
    addSectionWindow.setModal(true);
    addSectionWindow.setResizable(false);
    addSectionWindow.setWidth(425, Unit.PIXELS);
    addSectionWindow.setHeight(175, Unit.PIXELS);
    addSectionWindowButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    addSectionWindowButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    addSectionWindowCombobox.setRequired(true);
    addSectionWindowCombobox.setNullSelectionAllowed(false);
    addSectionWindowCombobox.setTextInputAllowed(false);
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setSpacing(true);
    windowLayout.setMargin(true);
    windowLayout.addComponent(addSectionWindowLabel);
    windowLayout.setComponentAlignment(addSectionWindowLabel, Alignment.TOP_CENTER);
    windowLayout.addComponent(addSectionWindowCombobox);
    windowLayout.setComponentAlignment(addSectionWindowCombobox, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(addSectionWindowButton);
    windowLayout.setComponentAlignment(addSectionWindowButton, Alignment.BOTTOM_CENTER);
    addSectionWindow.setContent(windowLayout);
  }

  /**
   * Initialize confirm delete user window
   */
  private void initConfirmDeleteUserWindow()
  {
    confirmDeleteUserWindow = new DeleteConfirmWindow(Messages.USERMANAGEMENT_DELETE_USER_CONFIRM);
  }

  /**
   * Initialize section infos buttons
   */
  private void initSectionInfosButtons()
  {
    // Buttons layout
    buttonSectionInfosLayout.setStyleName(NovaForge.FORM_TOP_RIGHT_BUTTONS);
    buttonSectionInfosLayout.setMargin(new MarginInfo(false));
    // Save and cancel buttons
    editingSectionInfosLayout.setMargin(new MarginInfo(true));
    editingSectionInfosLayout.setSpacing(true);
    saveSectionInfosButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    saveSectionInfosButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    cancelSectionInfosButton.setIcon(new ThemeResource(NovaForgeResources.ICON_BLOCKED_GREY));
    editingSectionInfosLayout.addComponent(saveSectionInfosButton);
    editingSectionInfosLayout.addComponent(cancelSectionInfosButton);
    editingSectionInfosLayout.setComponentAlignment(saveSectionInfosButton, Alignment.TOP_RIGHT);
    editingSectionInfosLayout.setComponentAlignment(cancelSectionInfosButton, Alignment.TOP_RIGHT);
    // Edit button
    editSectionInfosLayout.setMargin(new MarginInfo(true));
    editSectionInfosLayout.setSpacing(true);
    editSectionInfosButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    editSectionInfosButton.setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));
    editSectionInfosLayout.addComponent(editSectionInfosButton);
    editSectionInfosLayout.setComponentAlignment(editSectionInfosButton, Alignment.TOP_RIGHT);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSectionInfosEditing(final boolean pIsEditing)
  {
    userFirstnameField.setReadOnly(!pIsEditing);
    userLastnameField.setReadOnly(!pIsEditing);
    userEmailField.setReadOnly(!pIsEditing);
    userLanguage.setReadOnly(!pIsEditing);
    buttonSectionInfosLayout.removeAllComponents();
    setSectionContactEditable(!pIsEditing);
    setSectionWorkEditable(!pIsEditing);
    setSectionProjectsEditable(!pIsEditing);
    if (pIsEditing)
    {
      buttonSectionInfosLayout.addComponent(editingSectionInfosLayout);
    }
    else
    {
      buttonSectionInfosLayout.addComponent(editSectionInfosLayout);
    }
  }

  /**
   * Initialize section contact buttons
   */
  private void initSectionContactButtons()
  {
    // Buttons layout
    buttonSectionContactLayout.setStyleName(NovaForge.FORM_TOP_RIGHT_BUTTONS);
    buttonSectionContactLayout.setMargin(new MarginInfo(false));
    // Save and cancel buttons
    editingSectionContactLayout.setMargin(new MarginInfo(true));
    editingSectionContactLayout.setSpacing(true);
    saveSectionContactButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    saveSectionContactButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    cancelSectionContactButton.setIcon(new ThemeResource(NovaForgeResources.ICON_BLOCKED_GREY));
    editingSectionContactLayout.addComponent(saveSectionContactButton);
    editingSectionContactLayout.addComponent(cancelSectionContactButton);
    editingSectionContactLayout.setComponentAlignment(saveSectionContactButton, Alignment.TOP_RIGHT);
    editingSectionContactLayout.setComponentAlignment(cancelSectionContactButton, Alignment.TOP_RIGHT);
    // Edit button
    editSectionContactLayout.setMargin(new MarginInfo(true));
    editSectionContactLayout.setSpacing(true);
    editSectionContactButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    editSectionContactButton.setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));
    removeSectionContactButton.setIcon(new ThemeResource(NovaForgeResources.ICON_CLOSE_RED));
    editSectionContactLayout.addComponent(editSectionContactButton);
    editSectionContactLayout.addComponent(removeSectionContactButton);
    editSectionContactLayout.setComponentAlignment(editSectionContactButton, Alignment.TOP_RIGHT);
  }

  /**
   * Initialize section work buttons
   */
  private void initSectionWorkButtons()
  {
    // Buttons layout
    buttonSectionWorkLayout.setStyleName(NovaForge.FORM_TOP_RIGHT_BUTTONS);
    buttonSectionWorkLayout.setMargin(new MarginInfo(false));
    // Save and cancel buttons
    editingSectionWorkLayout.setMargin(new MarginInfo(true));
    editingSectionWorkLayout.setSpacing(true);
    saveSectionWorkButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    saveSectionWorkButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    cancelSectionWorkButton.setIcon(new ThemeResource(NovaForgeResources.ICON_BLOCKED_GREY));
    editingSectionWorkLayout.addComponent(saveSectionWorkButton);
    editingSectionWorkLayout.addComponent(cancelSectionWorkButton);
    editingSectionWorkLayout.setComponentAlignment(saveSectionWorkButton, Alignment.TOP_RIGHT);
    editingSectionWorkLayout.setComponentAlignment(cancelSectionWorkButton, Alignment.TOP_RIGHT);
    // Edit button
    editSectionWorkLayout.setMargin(new MarginInfo(true));
    editSectionWorkLayout.setSpacing(true);
    editSectionWorkButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    editSectionWorkButton.setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));
    removeSectionWorkButton.setIcon(new ThemeResource(NovaForgeResources.ICON_CLOSE_RED));
    editSectionWorkLayout.addComponent(editSectionWorkButton);
    editSectionWorkLayout.setComponentAlignment(editSectionWorkButton, Alignment.TOP_RIGHT);
    editSectionWorkLayout.addComponent(removeSectionWorkButton);
  }

  /**
   * Initialize section projects buttons
   */
  private void initSectionProjectsButtons()
  {
    // Buttons layout
    buttonSectionProjectsLayout.setStyleName(NovaForge.FORM_TOP_RIGHT_BUTTONS);
    buttonSectionProjectsLayout.setMargin(new MarginInfo(false));
    // Save and cancel buttons
    editingSectionProjectsLayout.setMargin(new MarginInfo(true));
    editingSectionProjectsLayout.setSpacing(true);
    saveSectionProjectsButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    saveSectionProjectsButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    cancelSectionProjectsButton.setIcon(new ThemeResource(NovaForgeResources.ICON_BLOCKED_GREY));
    editingSectionProjectsLayout.addComponent(saveSectionProjectsButton);
    editingSectionProjectsLayout.addComponent(cancelSectionProjectsButton);
    editingSectionProjectsLayout.setComponentAlignment(saveSectionProjectsButton, Alignment.TOP_RIGHT);
    editingSectionProjectsLayout.setComponentAlignment(cancelSectionProjectsButton, Alignment.TOP_RIGHT);
    // Edit button
    editSectionProjectsLayout.setMargin(new MarginInfo(true));
    editSectionProjectsLayout.setSpacing(true);
    editSectionProjectsButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    editSectionProjectsButton.setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));
    removeSectionProjectsButton.setIcon(new ThemeResource(NovaForgeResources.ICON_CLOSE_RED));
    editSectionProjectsLayout.addComponent(editSectionProjectsButton);
    editSectionProjectsLayout.addComponent(removeSectionProjectsButton);
    editSectionProjectsLayout.setComponentAlignment(editSectionProjectsButton, Alignment.TOP_RIGHT);
  }

  private Embedded getEmptyIcon()
  {
    return new Embedded(null, new ThemeResource(NovaForgeResources.ICON_EMPTY));
  }

  /**
   * Define if Section Contact is editable or not
   *
   * @param pIsEditable
   *          true if section is editable, false otherwise
   */
  private void setSectionContactEditable(final boolean pIsEditable)
  {
    editSectionContactButton.setEnabled(pIsEditable);
    removeSectionContactButton.setEnabled(pIsEditable);
  }

  /**
   * Define if Section Work is editable or not
   *
   * @param pIsEditable
   *          true if section is editable, false otherwise
   */
  private void setSectionWorkEditable(final boolean pIsEditable)
  {
    editSectionWorkButton.setEnabled(pIsEditable);
    removeSectionWorkButton.setEnabled(pIsEditable);
  }

  /**
   * Define if Section Projects is editable or not
   *
   * @param pIsEditable
   *          true if section is editable, false otherwise
   */
  private void setSectionProjectsEditable(final boolean pIsEditable)
  {
    editSectionProjectsButton.setEnabled(pIsEditable);
    removeSectionProjectsButton.setEnabled(pIsEditable);
  }

  /**
   * Define if Section infos is editable or not
   *
   * @param pIsEditable
   *          true if section is editable, false otherwise
   */
  private void setSectionInfosEditable(final boolean pIsEditable)
  {
    editSectionInfosButton.setEnabled(pIsEditable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditSectionInfosButton()
  {
    return editSectionInfosButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getSaveSectionInfosButton()
  {
    return saveSectionInfosButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCancelSectionInfosButton()
  {
    return cancelSectionInfosButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getUserFirstnameField()
  {
    return userFirstnameField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getUserLastnameField()
  {
    return userLastnameField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getUserLanguage()
  {
    return userLanguage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Image getUserPicture()
  {
    return imageComponent.getImage();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSectionContactEditing(final boolean pIsEditing)
  {
    userPhoneMobileField.setReadOnly(!pIsEditing);
    userPhoneMobileFieldPublic.setReadOnly(!pIsEditing);
    userPhoneWorkField.setReadOnly(!pIsEditing);
    userPhoneWorkFieldPublic.setReadOnly(!pIsEditing);
    buttonSectionContactLayout.removeAllComponents();
    setSectionInfosEditable(!pIsEditing);
    setSectionWorkEditable(!pIsEditing);
    setSectionProjectsEditable(!pIsEditing);
    if (pIsEditing)
    {
      buttonSectionContactLayout.addComponent(editingSectionContactLayout);
    }
    else
    {
      buttonSectionContactLayout.addComponent(editSectionContactLayout);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCancelSectionProjectsButton()
  {
    return cancelSectionProjectsButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getSaveSectionProjectsButton()
  {
    return saveSectionProjectsButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditSectionProjectsButton()
  {
    return editSectionProjectsButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCancelSectionWorkButton()
  {
    return cancelSectionWorkButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getSaveSectionWorkButton()
  {
    return saveSectionWorkButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditSectionWorkButton()
  {
    return editSectionWorkButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCancelSectionContactButton()
  {
    return cancelSectionContactButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getSaveSectionContactButton()
  {
    return saveSectionContactButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditSectionContactButton()
  {
    return editSectionContactButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getUserEmailField()
  {
    return userEmailField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getUserPhoneWorkField()
  {
    return userPhoneWorkField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getUserPhoneMobileField()
  {
    return userPhoneMobileField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getUserPhoneWorkFieldPublic()
  {
    return userPhoneWorkFieldPublic;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getUserPhoneMobileFieldPublic()
  {
    return userPhoneMobileFieldPublic;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    // Section Admin actions
    final PortalMessages portalMessages = PrivateModule.getPortalMessages();
    backToUsersAdminButton.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_BACK_USERS_ADMIN));
    deleteUserButton.setCaption(portalMessages
        .getMessage(pLocale, Messages.USERMANAGEMENT_ACTION_DELETE_USER));

    // Section Infos
    imageComponent.refreshLocale(portalMessages, pLocale);
    updatePasswdButton.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_UPDATE_PASSWD_BUTTON));
    infosForm.setCaption(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_SECTION_INFOS));
    userFirstnameFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_FIRSTNAME));
    userLastnameFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_LASTNAME));
    userLoginFieldCaption.setValue(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_FIELD_LOGIN));
    userEmailFieldCaption.setValue(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_FIELD_EMAIL));

    userEmailField.removeAllValidators();
    userEmailField.addValidator(new EmailValidator(portalMessages.getMessage(pLocale,
        Messages.PUBLIC_REGISTER_FORM_EMAIL_TOOLTIP)));

    userFirstnameField.removeAllValidators();
    userFirstnameField.addValidator(new NameValidator(AdminModule.getPortalMessages().getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_FIRSTNAME_REQUIRED, NAME_FIELD_MIN_LENGHT, NAME_FIELD_MAX_LENGHT),
        NAME_FIELD_MIN_LENGHT, NAME_FIELD_MAX_LENGHT));

    userLastnameField.removeAllValidators();
    userLastnameField.addValidator(new NameValidator(AdminModule.getPortalMessages().getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_LASTNAME_REQUIRED, NAME_FIELD_MIN_LENGHT, NAME_FIELD_MAX_LENGHT),
        NAME_FIELD_MIN_LENGHT, NAME_FIELD_MAX_LENGHT));

    userLanguageCaption.setValue(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_FIELD_LANGUAGE));
    editSectionInfosButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_EDIT));
    saveSectionInfosButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_SAVE));
    cancelSectionInfosButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_CANCEL));

    // Section Contact
    contactForm.setCaption(portalMessages.getMessage(pLocale, SectionType.CONTACT.getNameKey()));
    userPhoneMobileFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_PHONE_MOBILE));
    userPhoneWorkFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_PHONE_WORK));

    editSectionContactButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_EDIT));
    removeSectionContactButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_DELETE));
    saveSectionContactButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_SAVE));
    cancelSectionContactButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_CANCEL));

    userPhoneMobileFieldPublic.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_IS_FIELD_PUBLIC));
    userPhoneWorkFieldPublic.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_IS_FIELD_PUBLIC));
    userPhoneMobileField.removeAllValidators();
    userPhoneMobileField.addValidator(new PhoneNumberValidator(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_PHONE_MOBILE_VALIDATION)));
    userPhoneMobileField.setInputPrompt(portalMessages.getMessage(getLocale(),
        Messages.USERMANAGEMENT_FIELD_NULL));
    userPhoneWorkField.setInputPrompt(portalMessages.getMessage(getLocale(),
        Messages.USERMANAGEMENT_FIELD_NULL));
    userPhoneWorkField.removeAllValidators();
    userPhoneWorkField.addValidator(new PhoneNumberValidator(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_PHONE_WORK_VALIDATION)));

    // Section Work
    workForm.setCaption(portalMessages.getMessage(pLocale, SectionType.WORK.getNameKey()));
    userCompanyNameFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_COMPANY_NAME));
    userCompanyNameFieldPublic.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_IS_FIELD_PUBLIC));
    userCompanyAddressFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_COMPANY_ADDRESS));
    userCompanyAddressFieldPublic.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_IS_FIELD_PUBLIC));
    userCompanyOfficeFieldCaption.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_FIELD_COMPANY_OFFICE));
    userCompanyOfficeFieldPublic.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_IS_FIELD_PUBLIC));

    editSectionWorkButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_EDIT));
    removeSectionWorkButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_DELETE));
    saveSectionWorkButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_SAVE));
    cancelSectionWorkButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_CANCEL));

    userCompanyNameField.setInputPrompt(portalMessages.getMessage(getLocale(),
        Messages.USERMANAGEMENT_FIELD_NULL));
    userCompanyAddressField.setInputPrompt(portalMessages.getMessage(getLocale(),
        Messages.USERMANAGEMENT_FIELD_NULL));
    userCompanyOfficeField.setInputPrompt(portalMessages.getMessage(getLocale(),
        Messages.USERMANAGEMENT_FIELD_NULL));

    // Section Projects
    projectsForm.setCaption(portalMessages.getMessage(pLocale, SectionType.PROJECTS.getNameKey()));
    userProjectsFieldPublic.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_IS_MY_PROJECTS_PUBLIC));

    editSectionProjectsButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_EDIT));
    removeSectionProjectsButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_DELETE));
    saveSectionProjectsButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_SAVE));
    cancelSectionProjectsButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_CANCEL));

    // Add section
    addSectionButton.setCaption(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_ADD_SECTION));

    // Update password
    updatePasswdWindow.setCaption(portalMessages.getMessage(pLocale, Messages.CHANGEPWD_TITLE));
    passwdSecurityRuleLabel.setValue(portalMessages.getMessage(pLocale, Messages.CHANGEPWD_FORM_NEW_TOOLTIP));
    currentPasswd.setCaption(portalMessages.getMessage(pLocale, Messages.CHANGEPWD_FORM_CURRENT));
    newPasswd.setCaption(portalMessages.getMessage(pLocale, Messages.CHANGEPWD_FORM_NEW));
    newAgainPasswd.setCaption(portalMessages.getMessage(pLocale, Messages.CHANGEPWD_FORM_RENEW));
    confirmUpdatePasswdButton.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_UPDATE_PASSWD_BUTTON));
    cancelUpdatePasswdButton.setCaption(portalMessages.getMessage(pLocale, Messages.ACTIONS_CANCEL));

    newPasswd.removeAllValidators();
    if (!isAdmin)
    {
      newPasswd.addValidator(new NewPasswordValidator(portalMessages.getMessage(pLocale,
          Messages.CHANGEPWD_FORM_NEW_TOOLTIP), currentPasswd));
    }
    newPasswd.addValidator(passwordRegexValidator);
    passwordRegexValidator.setErrorMessage(portalMessages.getMessage(pLocale,
        Messages.PUBLIC_REGISTER_FORM_PASSWORD_TOOLTIP));
    newAgainPasswd.removeAllValidators();
    newAgainPasswd.addValidator(new DualPasswordValidator(portalMessages.getMessage(pLocale,
        Messages.CHANGEPWD_FORM_RENEW_ERROR), newPasswd));

    // Add section Window
    addSectionWindow.setCaption(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_ADD_SECTION));
    addSectionWindowLabel.setValue(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_ADD_SECTION_WINDOW_HELP));
    addSectionWindowCombobox.setCaption(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_ADD_SECTION_COMBOBOX));
    addSectionWindowButton
        .setCaption(portalMessages.getMessage(pLocale, Messages.USERMANAGEMENT_ADD_SECTION));
    addSectionWindowCombobox.setInputPrompt(portalMessages.getMessage(pLocale,
        Messages.USERMANAGEMENT_ADD_SECTION_COMBOBOX_INPUTPROMPT));

    // Delete user confirm window
    confirmDeleteUserWindow.refreshLocale(pLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSectionWorkEditing(final boolean pIsEditing)
  {
    userCompanyNameField.setReadOnly(!pIsEditing);
    userCompanyNameFieldPublic.setReadOnly(!pIsEditing);
    userCompanyAddressField.setReadOnly(!pIsEditing);
    userCompanyAddressFieldPublic.setReadOnly(!pIsEditing);
    userCompanyOfficeField.setReadOnly(!pIsEditing);
    userCompanyOfficeFieldPublic.setReadOnly(!pIsEditing);
    buttonSectionWorkLayout.removeAllComponents();
    setSectionContactEditable(!pIsEditing);
    setSectionInfosEditable(!pIsEditing);
    setSectionProjectsEditable(!pIsEditing);
    if (pIsEditing)
    {
      buttonSectionWorkLayout.addComponent(editingSectionWorkLayout);
    }
    else
    {
      buttonSectionWorkLayout.addComponent(editSectionWorkLayout);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSectionProjectsEditing(final boolean pIsEditing)
  {
    userProjectsFieldPublic.setReadOnly(!pIsEditing);
    buttonSectionProjectsLayout.removeAllComponents();
    setSectionContactEditable(!pIsEditing);
    setSectionWorkEditable(!pIsEditing);
    setSectionInfosEditable(!pIsEditing);
    if (pIsEditing)
    {
      buttonSectionProjectsLayout.addComponent(editingSectionProjectsLayout);
    }
    else
    {
      buttonSectionProjectsLayout.addComponent(editSectionProjectsLayout);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getUserProjectsFieldPublic()
  {
    return userProjectsFieldPublic;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getUserCompanyOfficeFieldPublic()
  {
    return userCompanyOfficeFieldPublic;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getUserCompanyOfficeField()
  {
    return userCompanyOfficeField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getUserCompanyAddressFieldPublic()
  {
    return userCompanyAddressFieldPublic;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextArea getUserCompanyAddressField()
  {
    return userCompanyAddressField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getUserCompanyNameFieldPublic()
  {
    return userCompanyNameFieldPublic;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getUserCompanyNameField()
  {
    return userCompanyNameField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getUpdatePictureWindow()
  {
    return imageComponent.getUpdateImageWindow();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PasswordField getNewAgainPasswd()
  {
    return newAgainPasswd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PasswordField getNewPasswd()
  {
    return newPasswd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PasswordField getCurrentPasswd()
  {
    return currentPasswd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getUpdatePasswdWindow()
  {
    return updatePasswdWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getUpdatePasswdButton()
  {
    return updatePasswdButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getUpdatePictureButton()
  {
    return imageComponent.getUpdateImageButton();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNewPasswdMatching(final boolean pOneEmpty, final boolean pIsMatching)
  {
    newPasswdsAgainMatchingIcon.removeAllComponents();
    if (!pOneEmpty)
    {
      if (pIsMatching)
      {
        newPasswdsAgainMatchingIcon.addComponent(getOKIcon());
      }
      else
      {
        newPasswdsAgainMatchingIcon.addComponent(getKOIcon());
      }
    }
    else
    {
      newPasswdsAgainMatchingIcon.addComponent(getEmptyIcon());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNewPasswdValid(final boolean pIsEmpty, final boolean pIsValid)
  {
    newPasswdRulesIcon.removeAllComponents();
    if (!pIsEmpty)
    {
      if (pIsValid)
      {
        newPasswdRulesIcon.addComponent(getOKIcon());
      }
      else
      {
        newPasswdRulesIcon.addComponent(getKOIcon());
      }
    }
    else
    {
      newPasswdRulesIcon.addComponent(getEmptyIcon());
      newPasswdsAgainMatchingIcon.removeAllComponents();
      newPasswdsAgainMatchingIcon.addComponent(getEmptyIcon());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getConfirmUpdatePasswdButton()
  {
    return confirmUpdatePasswdButton;
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
  public void setAdminActionsEnabled(final boolean pIsAdmin, final boolean pIsFromAdminView)
  {
    isAdmin = pIsAdmin;
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
  public Button getAddSectionButton()
  {
    return addSectionButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRemoveSectionProjectsButton()
  {
    return removeSectionProjectsButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRemoveSectionWorkButton()
  {
    return removeSectionWorkButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRemoveSectionContactButton()
  {
    return removeSectionContactButton;
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
  public TextField getUserLoginField()
  {
    return userLoginField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getAddSectionWindow()
  {
    return addSectionWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getAddSectionWindowCombobox()
  {
    return addSectionWindowCombobox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAddSectionWindowButton()
  {
    return addSectionWindowButton;
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
  public Button getDeletePictureButton()
  {
    return imageComponent.getDeleteImageButton();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getConfirmUpdatePictureButton()
  {
    return imageComponent.getUpdateImageConfirmButton();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UploadFieldCustom getUploadField()
  {
    return imageComponent.getUploadImageField();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getConfirmDeleteUserWindow()
  {
    return confirmDeleteUserWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCancelUpdatePasswdButton()
  {
    return cancelUpdatePasswdButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PasswordValidator getPasswordRegexValidator()
  {
    return passwordRegexValidator;
  }

  private Embedded getOKIcon()
  {
    return new Embedded(null, new ThemeResource(NovaForgeResources.ICON_VALIDATE_ROUND));
  }

  private Embedded getKOIcon()
  {
    return new Embedded(null, new ThemeResource(NovaForgeResources.ICON_ERROR));
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

}
