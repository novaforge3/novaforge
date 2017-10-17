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

import com.vaadin.data.Item;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.client.component.MailToLink;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.event.OpenProjectEvent;
import org.novaforge.forge.ui.portal.event.OpenProjectProfileEvent;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.user.management.internal.client.common.Utils;
import org.novaforge.forge.ui.user.management.internal.client.components.ProjectComponentGenerator;
import org.novaforge.forge.ui.user.management.internal.client.components.ProjectItemProperty;
import org.novaforge.forge.ui.user.management.internal.client.components.ProjectsContainer;
import org.novaforge.forge.ui.user.management.internal.client.event.OpenUserAdminEvent;
import org.novaforge.forge.ui.user.management.internal.module.PrivateModule;
import org.novaforge.forge.ui.user.management.internal.module.PublicModule;
import org.vaadin.addon.itemlayout.event.ItemClickEvent;
import org.vaadin.addon.itemlayout.event.ItemClickListener;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * The UserPublicProfilePresenter Presenter
 * Manage data and event of the view
 * 
 * @author caseryj
 */
public class UserPublicProfilePresenter extends AbstractPortalPresenter implements Serializable
{
  /**
   * Default serial version UID
   */
  private static final long           serialVersionUID = -7020057323474776865L;
  /**
   * Default value for French language
   */
  private static final String         FR               = "FR";
  /**
   * Content the view
   */
  private final UserPublicProfileView view;
  /**
   * Define if called from admin view or not
   */
  private final boolean               isFromAdminView;
  /**
   * Current opened UserProfile
   */
  private       UserProfile           currentUserProfile;
  private       String                userLogin;

  /**
   * Contains the projects on which one user has memberships
   */
  private ProjectsContainer projectsContainer;
  /**
   * Define if current user is super admin or not
   */
  private boolean           isSuperAdmin;

  /**
   * Default constructor
   *
   * @param pView
   *          The {@link UserPublicProfileView} to associate
   * @param pPortalContext
   *          the initial context
   */
  public UserPublicProfilePresenter(final UserPublicProfileView pView, final PortalContext pPortalContext)
  {
    this(pView, pPortalContext, false);
  }

  /**
   * Constructor
   *
   * @param pView
   *          The {@link UserPublicProfileView} to associate
   * @param pPortalContext
   *          the initial context
   * @param pIsFormAdminView
   *          true if from admin view, false otherwise
   */
  public UserPublicProfilePresenter(final UserPublicProfileView pView, final PortalContext pPortalContext,
                                    final boolean pIsFormAdminView)
  {
    super(pPortalContext);
    isFromAdminView = pIsFormAdminView;
    // init the view
    view = pView;
    // Init projects
    initProjectsList();
    // Define listeners
    addListeners();
  }

  private void initProjectsList()
  {
    // Projects List
    projectsContainer = new ProjectsContainer();
    view.getProjectList().setContainerDataSource(projectsContainer);
    view.getProjectList().setItemGenerator(new ProjectComponentGenerator());

  }

  /**
   * Add listeners to the view
   */
  private void addListeners()
  {
    view.getBackToUsersAdminButton().addClickListener(new Button.ClickListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -6582017944734466602L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new OpenUserAdminEvent());
      }
    });
    view.getDeleteUserButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 8445256062837265298L;

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
      private static final long serialVersionUID = 431003373235270582L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        deleteUserProfile();
      }
    });
    view.getProjectList().addItemClickListener(new ItemClickListener()
    {

      @Override
      public void onItemClick(final ItemClickEvent pEvent)
      {
        final String itemId = pEvent.getItemId();
        if (projectsContainer.containsId(itemId))
        {
          final Item item = projectsContainer.getItem(itemId);
          final boolean isMember = (boolean) item.getItemProperty(ProjectItemProperty.IS_MEMBER.getPropertyId())
                                                 .getValue();
          if (isMember)
          {
            getEventBus().publish(new OpenProjectEvent(itemId));
          }
          else
          {
            try
            {
              final ProjectOptions newProjectOptions = PublicModule.getProjectPresenter().newProjectOptions(false, true,
                                                                                                            false);
              final Project project = PublicModule.getProjectPresenter().getProject(itemId, newProjectOptions);
              getEventBus().publish(new OpenProjectProfileEvent(project));
            }
            catch (final ProjectServiceException e)
            {
              ExceptionCodeHandler.showNotificationError(PublicModule.getPortalMessages(), e, UI.getCurrent()
                  .getLocale());
            }
          }
        }

      }
    });
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
      ExceptionCodeHandler.showNotificationError(PublicModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
    finally
    {
      UI.getCurrent().removeWindow(view.getConfirmDeleteUserWindow());
    }
    getEventBus().post(new OpenUserAdminEvent());
  }

  /**
   * Get the associated view
   *
   * @return the view
   */
  public UserPublicProfileView getView()
  {
    return view;
  }

  /**
   * Refresh content
   *
   * @param pUserLogin
   *          the login of the UserProfile to refresh
   */
  public void refresh(final String pUserLogin)
  {
    userLogin = pUserLogin;
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  /**
   * Check if user is super admin, and enable admins features if yes
   */
  private void checkUserForAdminActions()
  {
    isSuperAdmin = false;

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
   * Set the view values
   *
   * @throws ProjectServiceException
   */
  private void setViewValues() throws ProjectServiceException
  {
    // Section infos
    setInfosSectionValues();
    // Section Contact
    setContactSectionValues();
    // Section Work
    setWorkSectionValues();
    // Section Projects
    setProjectsSectionValues();
  }

  /**
   * Set the infos section values
   */
  private void setInfosSectionValues()
  {
    if (currentUserProfile.getImage() != null)
    {
      view.getUserPicture().setSource(
          ResourceUtils.buildImageResource(currentUserProfile.getImage().getFile(), currentUserProfile
              .getImage().getName()));
    }
    else
    {
      view.getUserPicture().setSource(Utils.getUnkownPicture());
    }
    view.getUserFisrtnameField().setValue(currentUserProfile.getUser().getFirstName());
    view.getUserLastnameField().setValue(currentUserProfile.getUser().getName());
    view.getUserLoginField().setValue(currentUserProfile.getUser().getLogin());
    if (FR.equals(currentUserProfile.getUser().getLanguage().getName()))
    {
      view.getUserLanguage().setValue(
          PublicModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PUBLIC_REGISTER_FORM_LANGUAGE_FR));
    }
    else
    {
      view.getUserLanguage().setValue(
          PublicModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PUBLIC_REGISTER_FORM_LANGUAGE_EN));
    }

    view.getUserEmailLayout().removeAllComponents();
    view.getUserEmailLayout().addComponent(new MailToLink(currentUserProfile.getUser().getEmail()));
  }

  /**
   * Set the contact section values
   */
  private void setContactSectionValues()
  {

    if ((currentUserProfile.getUserProfileContact() != null)
        && (currentUserProfile.getUserProfileContact().getPhoneMobile().getIsPublic() || currentUserProfile
            .getUserProfileContact().getPhoneWork().getIsPublic()))
    {
      // Section Contact
      view.getUserPhoneMobileField().setValue(
          currentUserProfile.getUserProfileContact().getPhoneMobile().getValue());
      view.getUserPhoneMobileFieldCaption().setVisible(
          currentUserProfile.getUserProfileContact().getPhoneMobile().getIsPublic());
      view.getUserPhoneMobileField().setVisible(
          currentUserProfile.getUserProfileContact().getPhoneMobile().getIsPublic());
      // Phone Work
      view.getUserPhoneWorkField().setValue(
          currentUserProfile.getUserProfileContact().getPhoneWork().getValue());
      view.getUserPhoneWorkFieldCaption().setVisible(
          currentUserProfile.getUserProfileContact().getPhoneWork().getIsPublic());
      view.getUserPhoneWorkField().setVisible(
          currentUserProfile.getUserProfileContact().getPhoneWork().getIsPublic());
    }
    else
    {
      view.showSectionContact(false);
    }
  }

  /**
   * Set the work section values
   */
  private void setWorkSectionValues()
  {
    if ((currentUserProfile.getUserProfileWork() != null)
        && (currentUserProfile.getUserProfileWork().getCompanyName().getIsPublic()
            || currentUserProfile.getUserProfileWork().getCompanyAddress().getIsPublic() || currentUserProfile
            .getUserProfileWork().getCompanyOffice().getIsPublic()))
    {
      // Company Name
      view.getUserCompanyNameField().setValue(
          currentUserProfile.getUserProfileWork().getCompanyName().getValue());
      view.getUserCompanyNameFieldCaption().setVisible(
          currentUserProfile.getUserProfileWork().getCompanyName().getIsPublic());
      view.getUserCompanyNameField().setVisible(
          currentUserProfile.getUserProfileWork().getCompanyName().getIsPublic());
      // Company Address
      view.getUserCompanyAddressField().setValue(
          currentUserProfile.getUserProfileWork().getCompanyAddress().getValue());
      view.getUserCompanyAddressFieldCaption().setVisible(
          currentUserProfile.getUserProfileWork().getCompanyAddress().getIsPublic());
      view.getUserCompanyAddressField().setVisible(
          currentUserProfile.getUserProfileWork().getCompanyAddress().getIsPublic());
      // Company Office
      view.getUserCompanyOfficeField().setValue(
          currentUserProfile.getUserProfileWork().getCompanyOffice().getValue());
      view.getUserCompanyOfficeFieldCaption().setVisible(
          currentUserProfile.getUserProfileWork().getCompanyOffice().getIsPublic());
      view.getUserCompanyOfficeField().setVisible(
          currentUserProfile.getUserProfileWork().getCompanyOffice().getIsPublic());
      view.showSectionWork(true);
    }
    else
    {
      view.showSectionWork(false);
    }
  }

  /**
   * Set the projects section values
   *
   * @throws ProjectServiceException
   */
  private void setProjectsSectionValues() throws ProjectServiceException
  {
    if (((currentUserProfile.getUserProfileProjects() != null) && (currentUserProfile
        .getUserProfileProjects().getIsPublic())) || (isSuperAdmin))
    {
      final ProjectOptions newProjectOptions = PublicModule.getProjectPresenter().newProjectOptions(false,
          true, false);
      final List<Project> projects = PublicModule.getProjectPresenter().getValidatedProjects(
          currentUserProfile.getUser().getLogin(), newProjectOptions);
      projectsContainer.removeAllItems();
      for (final Project project : projects)
      {
        final boolean projectIsPrivate = project.isPrivateVisibility();
        final boolean projectIsForgeProject = PublicModule.getProjectPresenter().isForgeProject(
            project.getProjectId());
        if (!projectIsForgeProject && (!projectIsPrivate || isSuperAdmin))
        {
          List<MembershipInfo> userMemberships = null;
          if (isSuperAdmin)
          {
            userMemberships = PublicModule.getMembershipPresenter()
                .getAllEffectiveMembershipsForUserAndProject(project.getProjectId(),
                    currentUserProfile.getUser().getUuid());
          }

          projectsContainer.addProject(project, userMemberships, getCurrentLocale());
        }
      }
      projectsContainer.sort(new Object[] { ProjectItemProperty.NAME.getPropertyId() },
          new boolean[] { true });
      view.showSectionProjects(true);
    }
    else
    {
      view.showSectionProjects(false);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return PublicModule.getPortalModuleId();
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
      currentUserProfile = PublicModule.getUserPresenter().getUserProfile(userLogin);
      if (currentUserProfile != null)
      {
        checkUserForAdminActions();
        setViewValues();
      }
      else
      {
        final Notification notification = new Notification(PublicModule.getPortalMessages()
                                                                       .getMessage(getCurrentLocale(),
                                                                                   Messages.ERROR_TECHNICAL_TITLE),
                                                           PublicModule.getPortalMessages()
                                                                       .getMessage(getCurrentLocale(),
                                                                                   Messages.ERROR_TECHNICAL_DESC),
                                                           Type.ERROR_MESSAGE);
        notification.setHtmlContentAllowed(true);
        notification.show(Page.getCurrent());
      }
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(PublicModule.getPortalMessages(), e, view.getLocale());
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
