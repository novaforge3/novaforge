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

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import de.nlh.graphics2dimages.FixedWidthGraphics2DImage;
import org.apache.camel.converter.IOConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.entity.BinaryFileEntity;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.client.util.UserIconGenerator;
import org.novaforge.forge.ui.portal.event.UserUpdateEvent;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.user.management.internal.client.common.Utils;
import org.novaforge.forge.ui.user.management.internal.client.components.SectionContainer;
import org.novaforge.forge.ui.user.management.internal.client.components.SectionItemProperty;
import org.novaforge.forge.ui.user.management.internal.client.components.SectionType;
import org.novaforge.forge.ui.user.management.internal.client.event.OpenUserAdminEvent;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;
import org.novaforge.forge.ui.user.management.internal.module.PrivateModule;
import org.novaforge.forge.ui.user.management.internal.module.PublicModule;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * The UserProfilePresenter Presenter
 * Manage data and event of the view
 * 
 * @author Jeremy Casery
 */
public class UserProfilePresenter extends AbstractPortalPresenter implements Serializable
{

  /**
   * Default serial version UID
   */
  private static final long      serialVersionUID = 7849007242425598955L;
  /**
   * Log element
   */
  private static final Log       LOGGER           = LogFactory.getLog(UserProfilePresenter.class);
  /**
   * Default value for French language
   */
  private static final String FR            = "FR";
  final                String lineBreakHtml = "<br />";
  /**
   * Content the view
   */
  private final UserProfileView view;
  /**
   * The sections container
   */
  private final SectionContainer sections = new SectionContainer();
  /**
   * Define if opened from admin view
   */
  private final boolean     isFromAdminView;
  /**
   * The {@link UserProfile} associated
   */
  private       UserProfile currentUserProfile;
  private       String      login;

  /**
   * Default constructor
   *
   * @param pView
   *          The {@link UserProfileView} to associate
   * @param pPortalContext
   *          the initial contexte
   */
  public UserProfilePresenter(final UserProfileView pView, final PortalContext pPortalContext)
  {
    this(pView, pPortalContext, false);
  }

  /**
   * Constructor
   *
   * @param pView
   *          The {@link UserProfileView} to associate
   * @param pPortalContext
   *          the initial context
   * @param pIsFromAdminView
   *          true if from admin view, false otherwise
   */
  public UserProfilePresenter(final UserProfileView pView, final PortalContext pPortalContext,
                              final boolean pIsFromAdminView)
  {

    super(pPortalContext);
    // init the view
    view = pView;
    isFromAdminView = pIsFromAdminView;
    // Define listeners
    addListeners();
    addSaveEditCancelSectionsListeners();
    // Init the view
    initView();
  }

  /**
   * Initialize the view
   */
  private void initView()
  {
    view.getAddSectionWindowCombobox().setContainerDataSource(sections);
    view.getAddSectionWindowCombobox().setItemCaptionPropertyId(SectionItemProperty.NAME.getPropertyId());
  }

  /**
   * Check if user is a superadmin, and enable admin features if true
   */
  private void checkUserForAdminActions()
  {
    boolean isSuperAdmin = false;

    final String currentUserLogin = PublicModule.getAuthentificationService().getCurrentUser();
    if ((currentUserLogin != null) && (currentUserLogin.equals(currentUserProfile.getUser().getLogin())))
    {
      // Don't allow user to delete himself
      isSuperAdmin = false;
    }
    else
    {
      isSuperAdmin = PublicModule.getMembershipPresenter().isCurrentSuperAdmin();
    }

    view.setAdminActionsEnabled(isSuperAdmin, isFromAdminView);
  }

  /**
   * Add listeners to the view elements
   */
  private void addListeners()
  {
    view.getUpdatePasswdButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 6813517129310373292L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        // TODO SET password format description
        view.getCurrentPasswd().setVisible(!isFromAdminView);
        UI.getCurrent().addWindow(view.getUpdatePasswdWindow());
      }
    });
    view.getConfirmUpdatePictureButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 4972032507461947475L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        updateUserProfilePicture();
      }
    });

    view.getNewPasswd().addTextChangeListener(new TextChangeListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 45171870510918700L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void textChange(final TextChangeEvent event)
      {
        checkNewPassword(event.getText());
      }
    });
    view.getNewAgainPasswd().addTextChangeListener(new TextChangeListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -1097799932417970269L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void textChange(final TextChangeEvent event)
      {
        checkNewPwdAgainMatchNewPwd(event.getText());
      }
    });
    view.getCancelUpdatePasswdButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 4972032507461947475L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        UI.getCurrent().removeWindow(view.getUpdatePasswdWindow());
      }
    });
    view.getConfirmUpdatePasswdButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -289003081199452810L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        updateUserPwd();
      }
    });
    view.getBackToUsersAdminButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -8200817136258181368L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new OpenUserAdminEvent());
      }
    });
    view.getAddSectionButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -7859093286926410764L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        showAddSectionWindow();
      }
    });
    view.getAddSectionWindowButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -2612095854958516029L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        addSelectedSectionToUserProfile();
      }
    });
    view.getRemoveSectionContactButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 1844384922471660010L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        removeSectionContactOfUserProfile();
      }
    });
    view.getRemoveSectionWorkButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 5595450689049617660L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        removeSectionWorkOfUserProfile();
      }
    });
    view.getRemoveSectionProjectsButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 1454500510743603427L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        removeSectionProjectsOfUserProfile();
      }
    });
    view.getDeletePictureButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -7861649120755681975L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        deleteUserProfilePicture();
      }
    });
    view.getDeleteUserButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -450144870121827289L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getConfirmDeleteUserWindow().setParameterMessage(currentUserProfile.getUser().getLogin());
        UI.getCurrent().addWindow(view.getConfirmDeleteUserWindow());
      }
    });
    view.getConfirmDeleteUserWindow().getYesButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -7869300560150111416L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        deleteUserProfile();
      }
    });
  }

  /**
   * Add save/edit/cancel listerners for all sections
   */
  private void addSaveEditCancelSectionsListeners()
  {
    view.getEditSectionInfosButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 6813517129310373292L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSectionInfosEditing(true);
        view.getAddSectionButton().setEnabled(false);
      }
    });
    view.getSaveSectionInfosButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -785301028393713434L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        updateUserProfileSectionInfos();
      }
    });
    view.getCancelSectionInfosButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -7146550812099597870L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        cancelEditingSectionInfos();
      }
    });
    view.getEditSectionContactButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -6281235161764336266L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSectionContactEditing(true);
        view.getAddSectionButton().setEnabled(false);
      }
    });
    view.getSaveSectionContactButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 8515130403585070507L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {

        updateUserProfileSectionContact();
      }
    });
    view.getCancelSectionContactButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 2272299581489713986L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        cancelEditingSectionContact();
      }
    });
    view.getEditSectionWorkButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -1148598527646976712L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSectionWorkEditing(true);
        view.getAddSectionButton().setEnabled(false);
      }
    });
    view.getSaveSectionWorkButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 7692275570165342998L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        updateUserProfileSectionWork();
      }
    });
    view.getCancelSectionWorkButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -6159000279668923241L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        cancelEditingSectionWork();
      }
    });
    view.getEditSectionProjectsButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -3401682049125881519L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSectionProjectsEditing(true);
        view.getAddSectionButton().setEnabled(false);
      }
    });
    view.getSaveSectionProjectsButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -5594095994326093466L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        updateUserProfileSectionProjects();
      }
    });
    view.getCancelSectionProjectsButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 7205596038516328396L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        cancelEditingSectionProject();
      }
    });
  }

  /**
   * Check if new password match security rules
   */
  private void checkNewPassword(final String pNewPasswd)
  {
    boolean isNewPasswordValid = true;
    try
    {
      view.getNewPasswd().setValue(pNewPasswd);
      view.getNewPasswd().validate();
    }
    catch (final Exception e)
    {
      isNewPasswordValid = false;
      // Let's form handle this
    }
    final boolean isNewPasswordEmpty = (pNewPasswd == null) || ("".equals(pNewPasswd));

    view.setNewPasswdValid(isNewPasswordEmpty, isNewPasswordValid);

    final String newPasswordAgainValue = view.getNewAgainPasswd().getValue();
    final boolean isNewPasswordAgainEmpty = (newPasswordAgainValue == null)
        || ("".equals(newPasswordAgainValue));
    boolean isNewPasswordAgainValid = false;
    if (!isNewPasswordAgainEmpty)
    {
      try
      {
        // Need to send change event
        view.getNewAgainPasswd().setValue("");
        view.getNewAgainPasswd().setValue(newPasswordAgainValue);
        view.getNewAgainPasswd().validate();
        isNewPasswordAgainValid = true;
      }
      catch (final Exception e)
      {
        isNewPasswordAgainValid = false;
        // Let's form handle this
      }
    }
    view.setNewPasswdMatching(isNewPasswordAgainEmpty, isNewPasswordAgainValid);
    final boolean isUpdatable = isNewPasswordValid && isNewPasswordAgainValid;
    view.getConfirmUpdatePasswdButton().setEnabled(isUpdatable);
  }

  /**
   * Check if new passwd again match new passwd
   */
  private void checkNewPwdAgainMatchNewPwd(final String pNewPwdAgain)
  {
    boolean isNewPasswordAgainValid = true;
    try
    {
      view.getNewAgainPasswd().setValue(pNewPwdAgain);
      view.getNewAgainPasswd().validate();
    }
    catch (final Exception e)
    {
      // Let's form handle this
      isNewPasswordAgainValid = false;
    }

    final boolean isNewPasswordValid = view.getNewPasswd().isValid();
    final String newPasswordValue = view.getNewPasswd().getValue();
    final boolean isNewPasswordEmpty = (newPasswordValue == null) || ("".equals(newPasswordValue));

    final boolean isNewPasswordAgainEmpty = (pNewPwdAgain == null) || ("".equals(pNewPwdAgain));

    view.setNewPasswdMatching(isNewPasswordEmpty && isNewPasswordAgainEmpty, isNewPasswordAgainValid);
    final boolean isUpdatable = isNewPasswordValid && isNewPasswordAgainValid;
    view.getConfirmUpdatePasswdButton().setEnabled(isUpdatable);
  }

  /**
   * Show add section window
   */
  private void showAddSectionWindow()
  {
    setAvailableSections();
    view.getAddSectionWindow().center();
    UI.getCurrent().addWindow(view.getAddSectionWindow());
  }

  /**
   * Cancel editing section infos
   */
  private void cancelEditingSectionInfos()
  {
    refresh(currentUserProfile.getUser().getLogin());
    view.setSectionInfosEditing(false);
    refreshEnableAddSection();
  }

  /**
   * Cancel editing section contact
   */
  private void cancelEditingSectionContact()
  {
    refresh(currentUserProfile.getUser().getLogin());
    view.setSectionContactEditing(false);
    refreshEnableAddSection();
  }

  /**
   * Cancel editing section contact
   */
  private void cancelEditingSectionWork()
  {
    refresh(currentUserProfile.getUser().getLogin());
    view.setSectionWorkEditing(false);
    refreshEnableAddSection();
  }

  /**
   * Cancel editing section contact
   */
  private void cancelEditingSectionProject()
  {
    refresh(currentUserProfile.getUser().getLogin());
    view.setSectionProjectsEditing(false);
    refreshEnableAddSection();
  }

  /**
   * Delete UserProfile
   */
  private void deleteUserProfile()
  {
    try
    {
      PrivateModule.getUserPresenter().deleteUser(currentUserProfile.getUser().getLogin());
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    finally
    {
      UI.getCurrent().removeWindow(view.getConfirmDeleteUserWindow());
    }
    getEventBus().publish(new OpenUserAdminEvent());
  }

  /**
   * Delete UserProfile picture
   */
  private void deleteUserProfilePicture()
  {
    FixedWidthGraphics2DImage iconFileForUser = UserIconGenerator.getIconFileForUser(currentUserProfile
        .getUser().getFirstName(), currentUserProfile.getUser().getName());

    try
    {
      currentUserProfile.getImage().setFile(IOConverter.toBytes(iconFileForUser.getStream()));
      currentUserProfile.getImage().setMimeType(iconFileForUser.getResource().getMIMEType());
      currentUserProfile.getImage().setName(iconFileForUser.getResource().getFilename());
      currentUserProfile = PrivateModule.getUserPresenter().updateUserProfile(currentUserProfile);
      sendUserUpdatedEvent(true);

      setViewValues();
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    catch (IOException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  /**
   * Remove section contact from userprofile
   */
  private void removeSectionContactOfUserProfile()
  {
    view.showSectionContact(false);
    view.getUserPhoneMobileField().setReadOnly(false);
    view.getUserPhoneMobileField().setValue("");
    view.getUserPhoneMobileField().setReadOnly(true);
    view.getUserPhoneMobileFieldPublic().setReadOnly(false);
    view.getUserPhoneMobileFieldPublic().setValue(null);
    view.getUserPhoneMobileFieldPublic().setReadOnly(false);
    view.getUserPhoneWorkField().setReadOnly(false);
    view.getUserPhoneWorkField().setValue("");
    view.getUserPhoneWorkField().setReadOnly(true);
    view.getUserPhoneWorkFieldPublic().setReadOnly(false);
    view.getUserPhoneWorkFieldPublic().setValue(null);
    view.getUserPhoneWorkFieldPublic().setReadOnly(true);
    currentUserProfile.setUserProfileContact(null);
    try
    {
      currentUserProfile = PrivateModule.getUserPresenter().updateUserProfile(currentUserProfile);
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    refreshEnableAddSection();
  }

  /**
   * Remove section work from userprofile
   */
  private void removeSectionWorkOfUserProfile()
  {
    view.showSectionWork(false);
    view.getUserCompanyNameField().setReadOnly(false);
    view.getUserCompanyNameField().setValue("");
    view.getUserCompanyNameField().setReadOnly(true);
    view.getUserCompanyNameFieldPublic().setReadOnly(false);
    view.getUserCompanyNameFieldPublic().setValue(null);
    view.getUserCompanyNameFieldPublic().setReadOnly(true);
    view.getUserCompanyAddressField().setReadOnly(false);
    view.getUserCompanyAddressField().setValue("");
    view.getUserCompanyAddressField().setReadOnly(true);
    view.getUserCompanyAddressFieldPublic().setReadOnly(false);
    view.getUserCompanyAddressFieldPublic().setValue(null);
    view.getUserCompanyAddressFieldPublic().setReadOnly(true);
    view.getUserCompanyOfficeField().setReadOnly(false);
    view.getUserCompanyOfficeField().setValue("");
    view.getUserCompanyOfficeField().setReadOnly(true);
    view.getUserCompanyOfficeFieldPublic().setReadOnly(false);
    view.getUserCompanyOfficeFieldPublic().setValue(null);
    view.getUserCompanyOfficeFieldPublic().setReadOnly(true);
    currentUserProfile.setUserProfileWork(null);
    try
    {
      currentUserProfile = PrivateModule.getUserPresenter().updateUserProfile(currentUserProfile);
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    refreshEnableAddSection();
  }

  /**
   * Remove section projects from userprofile
   */
  private void removeSectionProjectsOfUserProfile()
  {

    view.showSectionProjects(false);
    view.getUserProjectsFieldPublic().setReadOnly(false);
    view.getUserProjectsFieldPublic().setValue(null);
    view.getUserProjectsFieldPublic().setReadOnly(true);
    currentUserProfile.setUserProfileProjects(null);
    try
    {
      currentUserProfile = PrivateModule.getUserPresenter().updateUserProfile(currentUserProfile);
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    refreshEnableAddSection();
  }

  /**
   * Add selected section to UserProfile
   */
  private void addSelectedSectionToUserProfile()
  {
    view.getAddSectionWindowCombobox().validate();
    final String sectionId = (String) view.getAddSectionWindowCombobox().getValue();
    if (SectionType.CONTACT.getId().equals(sectionId))
    {
      currentUserProfile.setUserProfileContact(PrivateModule.getUserPresenter().newUserProfileContact());
      view.showSectionContact(true);
    }
    else if (SectionType.WORK.getId().equals(sectionId))
    {
      currentUserProfile.setUserProfileWork(PrivateModule.getUserPresenter().newUserProfileWork());
      view.showSectionWork(true);
    }
    else if (SectionType.PROJECTS.getId().equals(sectionId))
    {
      currentUserProfile.setUserProfileProjects(PrivateModule.getUserPresenter().newUserProfileProjects());
      view.showSectionProjects(true);
    }
    try
    {
      currentUserProfile = PrivateModule.getUserPresenter().updateUserProfile(currentUserProfile);
      refreshEnableAddSection();
      UI.getCurrent().removeWindow(view.getAddSectionWindow());
    }
    catch (final UserServiceException e)
    {
      LOGGER.error(String.format("Unable to update userprofile for [user=%s]", currentUserProfile.getUser()
          .getLogin()));
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    catch (final Exception e)
    {
      // let the form manage
    }

  }

  /**
   * Update the section projects of userprofile
   */
  private void updateUserProfileSectionProjects()
  {
    currentUserProfile.getUserProfileProjects().setIsPublic(
        view.getUserProjectsFieldPublic().getValue().booleanValue());
    try
    {
      currentUserProfile = PrivateModule.getUserPresenter().updateUserProfile(currentUserProfile);
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    view.setSectionProjectsEditing(false);
    refreshEnableAddSection();
  }

  /**
   * Update the section work of userprofile
   */
  private void updateUserProfileSectionWork()
  {
    currentUserProfile.getUserProfileWork().getCompanyName()
        .setValue(view.getUserCompanyNameField().getValue());
    currentUserProfile.getUserProfileWork().getCompanyName()
        .setIsPublic(view.getUserCompanyNameFieldPublic().getValue().booleanValue());
    currentUserProfile.getUserProfileWork().getCompanyAddress()
        .setValue(view.getUserCompanyAddressField().getValue());
    currentUserProfile.getUserProfileWork().getCompanyAddress()
        .setIsPublic(view.getUserCompanyAddressFieldPublic().getValue().booleanValue());
    currentUserProfile.getUserProfileWork().getCompanyOffice()
        .setValue(view.getUserCompanyOfficeField().getValue());
    currentUserProfile.getUserProfileWork().getCompanyOffice()
        .setIsPublic(view.getUserCompanyOfficeFieldPublic().getValue().booleanValue());
    try
    {
      currentUserProfile = PrivateModule.getUserPresenter().updateUserProfile(currentUserProfile);
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    view.setSectionWorkEditing(false);
    refreshEnableAddSection();
  }

  /**
   * Update the section contact of userprofile
   */
  private void updateUserProfileSectionContact()
  {
    if (view.getUserPhoneWorkField().isValid() && view.getUserPhoneMobileField().isValid())
    {

      currentUserProfile.getUserProfileContact().getPhoneWork()
          .setValue(view.getUserPhoneWorkField().getValue());
      currentUserProfile.getUserProfileContact().getPhoneWork()
          .setIsPublic(view.getUserPhoneWorkFieldPublic().getValue().booleanValue());
      currentUserProfile.getUserProfileContact().getPhoneMobile()
          .setValue(view.getUserPhoneMobileField().getValue());
      currentUserProfile.getUserProfileContact().getPhoneMobile()
          .setIsPublic(view.getUserPhoneMobileFieldPublic().getValue().booleanValue());
      try
      {
        currentUserProfile = PrivateModule.getUserPresenter().updateUserProfile(currentUserProfile);
      }
      catch (final UserServiceException e)
      {
        ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
            .getLocale());
      }
      view.setSectionContactEditing(false);
      refreshEnableAddSection();
    }
    else
    {
      final StringBuilder errorMessage = new StringBuilder(lineBreakHtml);
      if (!view.getUserPhoneWorkField().isValid())
      {
        errorMessage
            .append(
                PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.USERMANAGEMENT_FIELD_PHONE_WORK))
            .append(" ")
            .append(
                PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.FORM_INVALID_VALUE_GENERIC));
        errorMessage.append(lineBreakHtml);
      }
      if (!view.getUserPhoneMobileField().isValid())
      {
        errorMessage
            .append(
                PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.USERMANAGEMENT_FIELD_PHONE_MOBILE))
            .append(" ")
            .append(
                PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.FORM_INVALID_VALUE_GENERIC));
        errorMessage.append(lineBreakHtml);
      }
      errorMessage.append(PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
          Messages.FORM_INVALID_VALUE_HELP));
      final Notification notification = new Notification(PrivateModule.getPortalMessages().getMessage(
          getCurrentLocale(), Messages.FORM_INVALID_VALUE_TITLE), errorMessage.toString(), Type.ERROR_MESSAGE);
      notification.setHtmlContentAllowed(true);
      notification.show(Page.getCurrent());
    }
  }

  /**
   * Update the section infos of userprofile
   */
  private void updateUserProfileSectionInfos()
  {
    if (view.getUserEmailField().isValid() && view.getUserFirstnameField().isValid()
        && view.getUserLastnameField().isValid())
    {
      currentUserProfile.getUser().setFirstName(view.getUserFirstnameField().getValue());
      currentUserProfile.getUser().setName(view.getUserLastnameField().getValue());
      currentUserProfile.getUser().setEmail(view.getUserEmailField().getValue());
      currentUserProfile.getUser().setLanguage((Language) view.getUserLanguage().getValue());
      try
      {
        currentUserProfile = PrivateModule.getUserPresenter().updateUserProfile(currentUserProfile);
        sendUserUpdatedEvent(false);

        view.setSectionInfosEditing(false);
        refreshEnableAddSection();
      }
      catch (final UserServiceException e)
      {
        ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
            .getLocale());
      }
    }
    else
    {
      final StringBuilder errorMessage = new StringBuilder();
      if (view.getUserEmailField().getValue().isEmpty())
      {
        errorMessage
            .append(lineBreakHtml)
            .append(
                PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.USERMANAGEMENT_FIELD_EMAIL))
            .append(" ")
            .append(
                PrivateModule.getPortalMessages()
                    .getMessage(getCurrentLocale(), Messages.FORM_REQUIRED_VALUE));
      }
      if (!view.getUserEmailField().isValid())
      {
        errorMessage
            .append(lineBreakHtml)
            .append(
                PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.USERMANAGEMENT_FIELD_EMAIL))
            .append(" ")
            .append(
                PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.FORM_INVALID_VALUE_GENERIC));
      }
      if (!view.getUserFirstnameField().isValid())
      {
        errorMessage
            .append(lineBreakHtml)
            .append(
                PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.USERMANAGEMENT_FIELD_FIRSTNAME))
            .append(" ")
            .append(
                PrivateModule.getPortalMessages()
                    .getMessage(getCurrentLocale(), Messages.FORM_REQUIRED_VALUE));
      }
      if (!view.getUserLastnameField().isValid())
      {
        errorMessage
            .append(lineBreakHtml)
            .append(
                PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.USERMANAGEMENT_FIELD_LASTNAME))
            .append(" ")
            .append(
                PrivateModule.getPortalMessages()
                    .getMessage(getCurrentLocale(), Messages.FORM_REQUIRED_VALUE));
      }

      errorMessage.append(lineBreakHtml);
      errorMessage.append(PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
          Messages.FORM_INVALID_VALUE_HELP));
      final Notification notification = new Notification(PrivateModule.getPortalMessages().getMessage(
          getCurrentLocale(), Messages.FORM_INVALID_VALUE_TITLE), errorMessage.toString(), Type.ERROR_MESSAGE);
      notification.setHtmlContentAllowed(true);
      notification.show(Page.getCurrent());
    }
  }

  /**
   * Update the user profile picture with uploaded one.
   */
  private void updateUserProfilePicture()
  {
    final byte[] value = (byte[]) view.getUploadField().getValue();
    if ((value != null) && (value.length > 0))
    {
      BinaryFile newUserImage;
      if (currentUserProfile.getImage() == null)
      {
        newUserImage = new BinaryFileEntity();
      }
      else
      {
        newUserImage = currentUserProfile.getImage();
      }
      newUserImage.setFile(value);
      newUserImage.setMimeType(view.getUploadField().getMimeType());
      newUserImage.setName(currentUserProfile.getUser().getLogin() + UUID.randomUUID().toString());
      currentUserProfile.setImage(newUserImage);
      try
      {
        currentUserProfile = PrivateModule.getUserPresenter().updateUserProfile(currentUserProfile);
      }
      catch (final UserServiceException e)
      {
        ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
            .getLocale());
      }
      sendUserUpdatedEvent(true);
      setViewValues();
      UI.getCurrent().removeWindow(view.getUpdatePictureWindow());
      view.getUploadField().discard();
    }
    else
    {
      LOGGER.error(String.format("Unable to update userprofile image for [user=%s]",
          currentUserProfile.getUser()));

      TrayNotification.show(
          PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.COMPONENT_IMAGE_ERROR_NULL), TrayNotificationType.WARNING);

    }

  }

  private void sendUserUpdatedEvent(final boolean pPictureUpdated)
  {
    final String currentUser = PrivateModule.getAuthentificationService().getCurrentUser();
    if (!isFromAdminView || (isFromAdminView && login.equals(currentUser)))
    {
      getEventBus().publish(
          new UserUpdateEvent(currentUserProfile.getUser().getLogin(),
              currentUserProfile.getUser().getName(), currentUserProfile.getUser().getFirstName(),
              new Locale(currentUserProfile.getUser().getLanguage().getName()), pPictureUpdated));
    }
  }

  /**
   * Refresh the add section button, enable or not if needed
   */
  private void refreshEnableAddSection()
  {
    if ((currentUserProfile.getUserProfileContact() != null)
        && (currentUserProfile.getUserProfileProjects() != null)
        && (currentUserProfile.getUserProfileWork() != null))
    {
      view.getAddSectionButton().setEnabled(false);
    }
    else
    {
      view.getAddSectionButton().setEnabled(true);
    }
  }

  /**
   * Set availables (and not already added by user) sections to sections container
   */
  private void setAvailableSections()
  {
    final List<SectionType> availableSection = new ArrayList<SectionType>();
    if (currentUserProfile.getUserProfileContact() == null)
    {
      availableSection.add(SectionType.CONTACT);
    }
    if (currentUserProfile.getUserProfileWork() == null)
    {
      availableSection.add(SectionType.WORK);
    }
    if (currentUserProfile.getUserProfileProjects() == null)
    {
      availableSection.add(SectionType.PROJECTS);
    }
    sections.setSection(availableSection, getCurrentLocale());
  }

  /**
   * Update the user password
   */
  private void updateUserPwd()
  {
    final String currentPwdHashed = PrivateModule.getAuthentificationService().hashPassword(
        view.getCurrentPasswd().getValue());

    if ((currentUserProfile.getUser() != null)
        && ((isFromAdminView) || (currentUserProfile.getUser().getPassword().equals(currentPwdHashed))))
    {
      final String newPwdHashed = PrivateModule.getAuthentificationService().hashPassword(
          view.getNewPasswd().getValue());
      try
      {

        PrivateModule.getUserPresenter()
            .updatePassword(currentUserProfile.getUser().getLogin(), newPwdHashed);
        currentUserProfile.getUser().setPassword(newPwdHashed);
        TrayNotification.show(
            PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
                Messages.USERMANAGEMENT_PWD_UPDATE_SUCCESS), TrayNotificationType.SUCCESS);
        UI.getCurrent().removeWindow(view.getUpdatePasswdWindow());
      }
      catch (final UserServiceException e)
      {
        ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
            .getLocale());
      }
    }
    else
    {
      TrayNotification.show(
          PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.USERMANAGEMENT_PWD_UPDATE_OLDKO_TITLE),
          PrivateModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.USERMANAGEMENT_PWD_UPDATE_OLDKO_TEXT), TrayNotificationType.WARNING);
    }
  }

  /**
   * Refresh contents
   * 
   * @param pLogin
   *          the user login associated to UserProfile to refresh
   */
  public void refresh(final String pLogin)
  {
    login = pLogin;
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  /**
   * Set the view values
   */
  private void setViewValues()
  {
    final User user = currentUserProfile.getUser();
    if (currentUserProfile.getImage() != null)
    {
      view.getUserPicture().setSource(
          ResourceUtils.buildImageResource(currentUserProfile.getImage().getFile(), currentUserProfile
              .getImage().getName()));
      view.getDeletePictureButton().setVisible(true);
    }
    else
    {
      view.getUserPicture().setSource(Utils.getUnkownPicture());
      view.getDeletePictureButton().setVisible(false);
    }
    view.getUserFirstnameField().setReadOnly(false);
    view.getUserFirstnameField().setValue(user.getFirstName());
    view.getUserFirstnameField().setReadOnly(true);
    view.getUserLastnameField().setReadOnly(false);
    view.getUserLastnameField().setValue(currentUserProfile.getUser().getName());
    view.getUserLastnameField().setReadOnly(true);
    view.getUserLoginField().setReadOnly(false);
    view.getUserLoginField().setValue(currentUserProfile.getUser().getLogin());
    view.getUserLoginField().setReadOnly(true);
    view.getUserLanguage().setReadOnly(false);
    initLanguages(getCurrentLocale());
    view.getUserLanguage().select(currentUserProfile.getUser().getLanguage());
    view.getUserLanguage().setReadOnly(true);
    view.getUserEmailField().setReadOnly(false);
    view.getUserEmailField().setValue(currentUserProfile.getUser().getEmail());
    view.getUserEmailField().setReadOnly(true);
    // Section Contact
    if (currentUserProfile.getUserProfileContact() != null)
    {
      view.getUserPhoneMobileField().setReadOnly(false);
      view.getUserPhoneMobileField().setValue(
          currentUserProfile.getUserProfileContact().getPhoneMobile().getValue());
      view.getUserPhoneMobileField().setReadOnly(true);
      view.getUserPhoneMobileFieldPublic().setReadOnly(false);
      view.getUserPhoneMobileFieldPublic().setValue(
          currentUserProfile.getUserProfileContact().getPhoneMobile().getIsPublic());
      view.getUserPhoneMobileFieldPublic().setReadOnly(true);
      view.getUserPhoneWorkField().setReadOnly(false);
      view.getUserPhoneWorkField().setValue(
          currentUserProfile.getUserProfileContact().getPhoneWork().getValue());
      view.getUserPhoneWorkField().setReadOnly(true);
      view.getUserPhoneWorkFieldPublic().setReadOnly(false);
      view.getUserPhoneWorkFieldPublic().setValue(
          currentUserProfile.getUserProfileContact().getPhoneWork().getIsPublic());
      view.getUserPhoneWorkFieldPublic().setReadOnly(true);
      view.showSectionContact(true);
    }
    else
    {
      view.showSectionContact(false);
    }
    // Section Work
    if (currentUserProfile.getUserProfileWork() != null)
    {
      view.getUserCompanyNameField().setReadOnly(false);
      view.getUserCompanyNameField().setValue(
          currentUserProfile.getUserProfileWork().getCompanyName().getValue());
      view.getUserCompanyNameField().setReadOnly(true);
      view.getUserCompanyNameFieldPublic().setReadOnly(false);
      view.getUserCompanyNameFieldPublic().setValue(
          currentUserProfile.getUserProfileWork().getCompanyName().getIsPublic());
      view.getUserCompanyNameFieldPublic().setReadOnly(true);
      view.getUserCompanyAddressField().setReadOnly(false);
      view.getUserCompanyAddressField().setValue(
          currentUserProfile.getUserProfileWork().getCompanyAddress().getValue());
      view.getUserCompanyAddressField().setReadOnly(true);
      view.getUserCompanyAddressFieldPublic().setReadOnly(false);
      view.getUserCompanyAddressFieldPublic().setValue(
          currentUserProfile.getUserProfileWork().getCompanyAddress().getIsPublic());
      view.getUserCompanyAddressFieldPublic().setReadOnly(true);
      view.getUserCompanyOfficeField().setReadOnly(false);
      view.getUserCompanyOfficeField().setValue(
          currentUserProfile.getUserProfileWork().getCompanyOffice().getValue());
      view.getUserCompanyOfficeField().setReadOnly(true);
      view.getUserCompanyOfficeFieldPublic().setReadOnly(false);
      view.getUserCompanyOfficeFieldPublic().setValue(
          currentUserProfile.getUserProfileWork().getCompanyOffice().getIsPublic());
      view.getUserCompanyOfficeFieldPublic().setReadOnly(true);
      view.showSectionWork(true);
    }
    else
    {
      view.showSectionWork(false);
    }
    // Section Projects
    if (currentUserProfile.getUserProfileProjects() != null)
    {
      view.getUserProjectsFieldPublic().setReadOnly(false);
      view.getUserProjectsFieldPublic().setValue(currentUserProfile.getUserProfileProjects().getIsPublic());
      view.getUserProjectsFieldPublic().setReadOnly(true);
      view.showSectionProjects(true);
    }
    else
    {
      view.showSectionProjects(false);
    }
    refreshEnableAddSection();
  }

  /**
   * Initialize the language combobox
   *
   * @param pLocale
   *          The {@link Locale} used for language name
   */
  private void initLanguages(final Locale pLocale)
  {
    try
    {

      final List<Language> languages = PrivateModule.getLanguagePresenter().getAllLanguages();
      for (final Language langue : languages)
      {
        final String languageName = langue.getName();
        final ComboBox comboBox = view.getUserLanguage();
        comboBox.addItem(langue);
        if (FR.equals(languageName))
        {
          comboBox.setItemCaption(langue,
              PrivateModule.getPortalMessages()
                  .getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LANGUAGE_FR));
          comboBox.setItemIcon(langue, new ThemeResource(NovaForgeResources.FLAG_FR));
          comboBox.setValue(langue);
        }
        else
        {
          comboBox.setItemCaption(langue,
              PrivateModule.getPortalMessages()
                  .getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LANGUAGE_EN));
          comboBox.setItemIcon(langue, new ThemeResource(NovaForgeResources.FLAG_EN));
        }
      }
    }
    catch (final LanguageServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return PrivateModule.getPortalModuleId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    // Retrieve user profile
    try
    {
      currentUserProfile = PrivateModule.getUserPresenter().getUserProfile(login);

    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(PrivateModule.getPortalMessages(), e, view.getLocale());
    }
    // Update view
    if (currentUserProfile != null)
    {
      setViewValues();
      checkUserForAdminActions();
    }
    else
    {
      LOGGER.error(String.format("Unable to get userprofile for [user=%s]", login));
      final Notification notification = new Notification(PrivateModule.getPortalMessages()
                                                                      .getMessage(getCurrentLocale(),
                                                                                  Messages.ERROR_TECHNICAL_TITLE),
                                                         PrivateModule.getPortalMessages()
                                                                      .getMessage(getCurrentLocale(),
                                                                                  Messages.ERROR_TECHNICAL_DESC),
                                                         Type.ERROR_MESSAGE);
      notification.setHtmlContentAllowed(true);
      notification.show(Page.getCurrent());
    }
    // Update password reg ex
    view.getPasswordRegexValidator().setValidationRegex(PrivateModule.getForgeConfigurationService()
                                                                     .getPasswordValidationRegex());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

}
