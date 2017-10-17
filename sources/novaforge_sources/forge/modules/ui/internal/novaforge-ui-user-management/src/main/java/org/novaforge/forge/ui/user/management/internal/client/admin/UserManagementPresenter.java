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
package org.novaforge.forge.ui.user.management.internal.client.admin;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import de.nlh.graphics2dimages.FixedWidthGraphics2DImage;
import org.apache.camel.converter.IOConverter;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.client.util.UserIconGenerator;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.portal.data.container.UserProfileContainer;
import org.novaforge.forge.ui.portal.data.container.UserProfileItemProperty;
import org.novaforge.forge.ui.portal.event.OpenUserProfileEvent;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.user.management.internal.client.admin.components.UserColumnActionsGenerator;
import org.novaforge.forge.ui.user.management.internal.client.admin.components.UserFieldFactory;
import org.novaforge.forge.ui.user.management.internal.client.event.OpenEditUserProfileEvent;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * The UserManagement Presenter
 * Manage data and event of the view
 * 
 * @author Jeremy Casery
 */
public class UserManagementPresenter extends AbstractPortalPresenter implements Serializable
{
  /**
   * Contains the array of fields to display in the form
   */
  public static final  String[] USER_FIELDS      = new String[] { UserFieldFactory.FIRSTNAME_FIELD,
                                                                  UserFieldFactory.LASTNAME_FIELD,
                                                                  UserFieldFactory.EMAIL_FIELD,
                                                                  UserFieldFactory.LANGUAGE_FIELD,
                                                                  UserFieldFactory.PASSWORD_FIELD };
  /**
   * Default serial version UID
   */
  private static final long     serialVersionUID = -8232070101156508438L;
  /**
   * Default value for French language
   */
  private static final String   FR               = "FR";
  /**
   * The users container
   */
  // private final CustomUsersContainer usersContainer = new CustomUsersContainer();
  /**
   * Content the view
   */
  private final UserManagementView view;
  /**
   * The user profile container
   */
  private final UserProfileContainer userProfilesContainer = new UserProfileContainer();
  /**
   * Represent the selected user id
   */
  private String selectedUserId;
  /**
   * The users container filter
   */
  private Filter usersFilter;
  /**
   * New user for creation
   */
  private User   newUser;

  /**
   * Default constructor
   *
   * @param pView
   *          the {@link UserManagementView} to associate
   * @param pPortalContext
   *          the initial context
   */
  public UserManagementPresenter(final UserManagementView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Define listeners
    addListeners();
    // Initialize projectList
    initUsersList();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getUsersTable().addActionHandler(new RefreshAction()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 3657198735744388874L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void refreshAction()
      {
        refreshContent();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public PortalMessages getPortalMessages()
      {
        return AdminModule.getPortalMessages();
      }

    });
    view.getFilterTextField().addTextChangeListener(new TextChangeListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 1978421308920978868L;

      @Override
      public void textChange(final TextChangeEvent event)
      {
        final Filterable f = userProfilesContainer;

        // Remove old filter
        if (usersFilter != null)
        {
          f.removeContainerFilter(usersFilter);
          usersFilter = null;
        }
        if ((event.getText() != null) && !event.getText().isEmpty())
        {
          // Set new filter for the status column
          usersFilter = new Or(new SimpleStringFilter(UserItemProperty.LOGIN.getPropertyId(), event.getText(), true,
                                                      false), new SimpleStringFilter(UserItemProperty.FIRSTNAME
                                                                                         .getPropertyId(),
                                                                                     event.getText(), true, false),
                               new SimpleStringFilter(UserItemProperty.LASTNAME.getPropertyId(), event.getText(), true,
                                                      false), new SimpleStringFilter(UserItemProperty.EMAIL
                                                                                         .getPropertyId(),
                                                                                     event.getText(), true, false));
          f.addContainerFilter(usersFilter);
        }
      }
    });
    view.getDeleteUserWindow().getYesButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -1889845996942887120L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        if (selectedUserId != null)
        {
          try
          {
            AdminModule.getUserPresenter().deleteUser(selectedUserId);
          }
          catch (final UserServiceException e)
          {
            ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent().getLocale());
          }
          finally
          {
            refreshContent();
            selectedUserId = null;
            UI.getCurrent().removeWindow(getView().getDeleteUserWindow());

          }
        }
      }
    });
    view.getCreateUserButton().addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 2624107802415373213L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        newUser = AdminModule.getUserPresenter().newUser();
        initCreateUserForm();
        ((UserFieldFactory) view.getCreateUserForm().getFormFieldFactory()).getFirstName().focus();
        view.refreshLocale(getCurrentLocale());
        UI.getCurrent().addWindow(view.getCreateUserWindow());
      }
    });
    view.getCreateUserWindowButton().addClickListener(new Button.ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = -3328061850031501430L;

      @Override
      public void buttonClick(final ClickEvent event)
      {

        final Locale locale          = getCurrentLocale();
        String       newUserPassword = "";
        try
        {
          view.getCreateUserForm().commit();
          newUserPassword = newUser.getPassword();
          newUser.setPassword(AdminModule.getAuthentificationService().hashPassword(newUserPassword));
          AdminModule.getUserPresenter().createUser(newUser);
          // Generate User Icon
          UserProfile userProfile = AdminModule.getUserPresenter().getUserProfile(newUser.getLogin());
          BinaryFile userIcon = AdminModule.getUserPresenter().newUserIcon();
          FixedWidthGraphics2DImage generatedIcon = UserIconGenerator.getIconFileForUser(newUser.getFirstName(),
                                                                                         newUser.getName());
          userIcon.setFile(IOConverter.toBytes(generatedIcon.getStream()));
          userIcon.setMimeType(generatedIcon.getResource().getMIMEType());
          userIcon.setName(generatedIcon.getResource().getFilename());
          userProfile.setImage(userIcon);
          AdminModule.getUserPresenter().updateUserProfile(userProfile);

          final String notifSuccess = AdminModule.getPortalMessages().getMessage(locale,
                                                                                 Messages.PUBLIC_REGISTER_CONFIRM_WINDOW_LABEL_INFO)
                                          + " " + AdminModule.getPortalMessages().getMessage(locale,
                                                                                             Messages.PUBLIC_REGISTER_CONFIRM_WINDOW_LABEL_LOGIN,
                                                                                             newUser.getLogin());

          TrayNotification.show(notifSuccess);
          refreshContent();
          UI.getCurrent().removeWindow(view.getCreateUserWindow());
        }
        catch (final InvalidValueException e)
        {
          TrayNotification.show(e.getLocalizedMessage(), TrayNotificationType.WARNING);
        }
        catch (final Exception e)
        {
          ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, view.getLocale());
          newUser.setPassword(newUserPassword);
        }
      }
    });
  }

  /**
   * Initialize users list
   */
  private void initUsersList()
  {
    view.getUsersTable().setContainerDataSource(userProfilesContainer);
    generateUserTableActionsColumn();
    view.setUserTableVisibleColumns();

    view.getExcelExporter().setContainerToBeExported(userProfilesContainer);
    view.getExcelExporter().setVisibleColumns(new String[] { UserProfileItemProperty.LOGIN.getPropertyId(),
                                                             UserProfileItemProperty.FIRSTNAME.getPropertyId(),
                                                             UserProfileItemProperty.LASTNAME.getPropertyId(),
                                                             UserProfileItemProperty.EMAIL.getPropertyId() });
  }

  /**
   * Get the associate view
   *
   * @return the {@link UserManagementView} view
   */
  public UserManagementView getView()
  {
    return view;
  }

  /**
   * It will init the create user form
   */
  private void initCreateUserForm()
  {
    final BeanItem<User> userItem = new BeanItem<User>(newUser);
    view.getCreateUserForm().setItemDataSource(userItem);
    final ArrayList<String> visibleItemProperties = new ArrayList<String>(Arrays.asList(USER_FIELDS));
    if (!AdminModule.getForgeConfigurationService().isUserLoginGenerated())
    {
      // Add login field if login can be customized
      visibleItemProperties.add(0, UserFieldFactory.LOGIN_FIELD);
    }
    view.getCreateUserForm().setVisibleItemProperties(visibleItemProperties);
    initLanguages(getCurrentLocale());
  }

  /**
   * Add a generated "Actions" column to the users table
   */
  private void generateUserTableActionsColumn()
  {
    view.getUsersTable().addGeneratedColumn(CommonItemProperty.ACTIONS.getPropertyId(),
                                            new UserColumnActionsGenerator(this));
  }

  /**
   * Initialize language field
   *
   * @param pLocale
   */
  private void initLanguages(final Locale pLocale)
  {
    try
    {

      final List<Language> languages = AdminModule.getLanguagePresenter().getAllLanguages();
      for (final Language langue : languages)
      {
        final String languageName = langue.getName();
        final ComboBox comboBox = view.getUserFieldFactory().getLanguages();
        comboBox.addItem(langue);
        if (FR.equals(languageName))
        {
          comboBox.setItemCaption(langue, AdminModule.getPortalMessages().getMessage(pLocale,
                                                                                     Messages.PUBLIC_REGISTER_FORM_LANGUAGE_FR));
          comboBox.setItemIcon(langue, new ThemeResource(NovaForgeResources.FLAG_FR));
          comboBox.setValue(langue);
          comboBox.select(langue);
        }
        else
        {
          comboBox.setItemCaption(langue, AdminModule.getPortalMessages().getMessage(pLocale,
                                                                                     Messages.PUBLIC_REGISTER_FORM_LANGUAGE_EN));
          comboBox.setItemIcon(langue, new ThemeResource(NovaForgeResources.FLAG_EN));
        }
      }
    }
    catch (final LanguageServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }
  }

  /**
   * Action when view profile is clicked
   * 
   * @param pUserLogin
   *          the user login
   */
  public void actionViewProfileClicked(final String pUserLogin)
  {
    selectedUserId = pUserLogin;
    try
    {
      UserProfile userProfile = AdminModule.getUserPresenter().getUserProfile(pUserLogin);
      getEventBus().publish(new OpenUserProfileEvent(userProfile));
    }
    catch (UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, getCurrentLocale());
    }

  }

  /**
   * Action when edit profile is clicked
   * 
   * @param pUserLogin
   *          the user login
   */
  public void actionEditProfileClicked(final String pUserLogin)
  {
    selectedUserId = pUserLogin;
    // Post show edit profile event
    getEventBus().publish(new OpenEditUserProfileEvent(pUserLogin, true));
  }

  /**
   * Action when delete user is clicked
   * 
   * @param pUserLogin
   *          the user login
   */
  public void actionDeleteUserClicked(final String pUserLogin)
  {
    selectedUserId = pUserLogin;

    view.getDeleteUserWindow().setParameterMessage(pUserLogin);
    UI.getCurrent().addWindow(view.getDeleteUserWindow());
  }

  /**
   * Will refresh users information
   */
  public void refresh()
  {
    refreshContent();
    // Has to be set when Exporter is attached to UI
    view.getExcelExporter().setDownloadFileName("users-export");
    refreshLocalized(getCurrentLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return AdminModule.getPortalModuleId();
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
    try
    {
      // Remove old filter
      if (usersFilter != null)
      {
        userProfilesContainer.removeContainerFilter(usersFilter);
      }
      final List<UserProfile> userProfiles = AdminModule.getUserPresenter().getAllUserProfiles(true);
      userProfilesContainer.setUserProfiles(userProfiles);
      // Add previous filter
      if (usersFilter != null)
      {
        userProfilesContainer.addContainerFilter(usersFilter);
      }
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(AdminModule.getPortalMessages(), e, view.getLocale());
    }

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
