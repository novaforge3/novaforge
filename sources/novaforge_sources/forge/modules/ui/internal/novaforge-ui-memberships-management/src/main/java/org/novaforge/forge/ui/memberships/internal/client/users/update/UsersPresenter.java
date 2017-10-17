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
package org.novaforge.forge.ui.memberships.internal.client.users.update;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.memberships.internal.client.containers.RoleItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.UserItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.UserMembersContainer;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowAddUserMemberViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RoleColumnNameGenerator;
import org.novaforge.forge.ui.memberships.internal.client.users.components.UserMemberColumnActionsGenerator;
import org.novaforge.forge.ui.memberships.internal.client.users.components.UserMemberItemGenerator;
import org.novaforge.forge.ui.memberships.internal.module.AbstractMembershipsPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;
import org.novaforge.forge.ui.portal.event.OpenUserProfileEvent;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.vaadin.addon.itemlayout.layout.model.DefaultItemGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * This presenter handles users view.
 * 
 * @author Guillaume Lamirand
 */
public class UsersPresenter extends AbstractMembershipsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long             serialVersionUID = -5042299647493799344L;
  /**
   * Content of project view
   */
  private final UserView                view;

  /**
   * This variable contains the project Id associated to this presenter
   */
  private final String                  projectId;
  private final DefaultItemGenerator    defaultGenerator = new DefaultItemGenerator();
  private final UserMemberItemGenerator userGenerator    = new UserMemberItemGenerator(this);
  private UserMembersContainer usersContainer;
  private UUID                 selectedUserId;

  /**
   * Default constructor. It will initialize the tree component associated to the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pPortalContext
   *          the initial context bus
   * @param pProjectId
   *          project id
   */
  public UsersPresenter(final UserView pView, final PortalContext pPortalContext, final String pProjectId)
  {
    super(pPortalContext);
    projectId = pProjectId;
    view = pView;
    initUsersList();
    addListeners();
  }

  private void initUsersList()
  {
    usersContainer = new UserMembersContainer();
    view.getUsersTable().setContainerDataSource(usersContainer);

    view.getUsersTable().addGeneratedColumn(UserItemProperty.ROLES.getPropertyId(),
        new RoleColumnNameGenerator());
    view.getUsersTable().addGeneratedColumn(CommonItemProperty.ACTIONS.getPropertyId(),
        new UserMemberColumnActionsGenerator(this));

    // Define visibles columns
    view.getUsersTable().setVisibleColumns(UserItemProperty.LOGIN.getPropertyId(),
        UserItemProperty.FIRSTNAME.getPropertyId(), UserItemProperty.LASTNAME.getPropertyId(),
        UserItemProperty.EMAIL.getPropertyId(), UserItemProperty.ROLES.getPropertyId(),
        CommonItemProperty.ACTIONS.getPropertyId());

    // Define special column width
    view.getUsersTable().setColumnExpandRatio(UserItemProperty.LOGIN.getPropertyId(), 0);
    view.getUsersTable().setColumnExpandRatio(UserItemProperty.FIRSTNAME.getPropertyId(), 0.1f);
    view.getUsersTable().setColumnExpandRatio(UserItemProperty.LASTNAME.getPropertyId(), 0.1f);
    view.getUsersTable().setColumnExpandRatio(UserItemProperty.EMAIL.getPropertyId(), 0.3f);
    view.getUsersTable().setColumnExpandRatio(UserItemProperty.ROLES.getPropertyId(), 0.3f);
    view.getUsersTable().setColumnWidth(CommonItemProperty.ACTIONS.getPropertyId(), 105);
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getAddButton().addClickListener(new ClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -966341600260989898L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new ShowAddUserMemberViewEvent(getUuid()));
      }
    });
    view.getUsersTable().addActionHandler(new RefreshAction()
    {

      /**
       * Serial version id
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
        return MembershipsModule.getPortalMessages();
      }

    });
    view.getFilterTextField().addTextChangeListener(new TextChangeListener()
    {
      /**
			 * 
			 */
      private static final long serialVersionUID = 1978421308920978868L;

      @Override
      public void textChange(final TextChangeEvent event)
      {
        // Clean filter
        usersContainer.removeAllContainerFilters();
        if ((event.getText() != null) && !event.getText().isEmpty())
        {
          // Set new filter for the status column
          final Filter usersFilter = new Or(new SimpleStringFilter(UserItemProperty.LOGIN.getPropertyId(),
              event.getText(), true, false), new SimpleStringFilter(UserItemProperty.FIRSTNAME
              .getPropertyId(), event.getText(), true, false), new SimpleStringFilter(
              UserItemProperty.LASTNAME.getPropertyId(), event.getText(), true, false),
              new SimpleStringFilter(UserItemProperty.EMAIL.getPropertyId(), event.getText(), true, false));
          usersContainer.addContainerFilter(usersFilter);
        }
      }
    });
    view.getDeleteUserCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 2756645480332651962L;

      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getUsersTable().unselect(selectedUserId);
        selectedUserId = null;
        UI.getCurrent().removeWindow(view.getDeleteUserWindow());
      }
    });
    view.getDeleteUserConfirmButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 2756645480332651962L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        if (selectedUserId != null)
        {
          try
          {
            MembershipsModule.getMembershipPresenter().removeUserMembership(projectId, selectedUserId,
                view.getDeleteUserNotifCheck().getValue());
            refresh();
          }
          catch (final ProjectServiceException e)
          {
            ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
                view.getLocale());

          }
          finally
          {
            UI.getCurrent().removeWindow(view.getDeleteUserWindow());
            view.getUsersTable().unselect(selectedUserId);
            selectedUserId = null;
          }
        }

      }
    });
    view.getEditRolesComponent().getCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 4501329433797831092L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getUsersTable().unselect(selectedUserId);
        selectedUserId = null;
        UI.getCurrent().removeWindow(view.getEditRolesWindow());
      }
    });
    view.getEditRolesComponent().getConfirmButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 8105985839153218549L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        try
        {
          MembershipsModule.getMembershipPresenter().updateUserMembership(projectId, selectedUserId,
              view.getEditRolesComponent().getSelectedRoles(), view.getEditRolesComponent().getDefaultRole(),
              true);
        }
        catch (final ProjectServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
              view.getLocale());

        }
        view.getUsersTable().unselect(selectedUserId);
        view.getEditRolesComponent().clearComponent();
        selectedUserId = null;
        UI.getCurrent().removeWindow(view.getEditRolesWindow());
        refresh();

      }
    });

  }

  /**
   * Will refresh the project information.
   */
  public void refresh()
  {
    view.getFilterTextField().setValue("");
    usersContainer.removeAllContainerFilters();
    refreshContent();
    refreshLocalized(getCurrentLocale());

  }

  /**
   * Used when click is received on profile action
   *
   * @param pItemId
   *          user's login
   */
  public void onClickActionProfile(final String pUserLogin)
  {
    try
    {
      final UserProfile userProfile = MembershipsModule.getUserPresenter().getUserProfile(pUserLogin);
      getEventBus().publish(new OpenUserProfileEvent(userProfile));
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, view.getLocale());
    }

  }

  /**
   * Used when click is received on delete action
   *
   * @param pItemId
   *          user's login
   */
  @SuppressWarnings("unchecked")
  public void onClickActionEdit(final Object pItemId)
  {
    try
    {
      selectedUserId = (UUID) pItemId;
      view.getEditRolesComponent().attachRolesTable(false);
      // Initialize project's roles
      final List<ProjectRole> allRoles = MembershipsModule.getProjectRolePresenter().getAllRoles(projectId);
      view.getEditRolesComponent().getRolesContainer().setRoles(allRoles);
      final String currentUser = MembershipsModule.getAuthentificationService().getCurrentUser();
      final String userLogin = (String) usersContainer.getContainerProperty(pItemId,
          UserItemProperty.LOGIN.getPropertyId()).getValue();
      if ((currentUser != null) && (currentUser.equals(userLogin)))
      {
        view.getEditRolesComponent().getRolesContainer().isCurrentUser();
      }
      // Initialize user's membership
      final List<MembershipInfo> membershipsInfo = (List<MembershipInfo>) usersContainer.getItem(pItemId)
          .getItemProperty(UserItemProperty.MEMBERSHIPS.getPropertyId()).getValue();

      if (membershipsInfo != null)
      {
        for (final MembershipInfo membershipInfo : membershipsInfo)
        {
          final String itemId = membershipInfo.getRole().getName();
          if (view.getEditRolesComponent().getRolesContainer().containsId(itemId))
          {
            view.getEditRolesComponent().getCurrentRoles().add(itemId);
            if (membershipInfo.getPriority())
            {
              view.getEditRolesComponent().getRolesContainer()
                  .getContainerProperty(itemId, RoleItemProperty.DEFAULT.getPropertyId()).setValue(true);
              view.getEditRolesComponent().setDefaultRole(itemId);
            }
            view.getEditRolesComponent().getRolesContainer()
                .getContainerProperty(itemId, RoleItemProperty.SELECT.getPropertyId()).setValue(true);
          }

        }
      }
      view.getEditRolesComponent().attachRolesTable(true);
      UI.getCurrent().addWindow(view.getEditRolesWindow());

    }
    catch (final ProjectServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, view.getLocale());

    }
  }

  /**
   * Used when click is received on delete action
   *
   * @param pItemId
   *          user's login
   */
  public void onClickActionDelete(final Object pItemId)
  {
    selectedUserId = (UUID) pItemId;
    UI.getCurrent().addWindow(view.getDeleteUserWindow());
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
    view.getDeleteUserNotifCheck().setValue(true);
    try
    {
      final List<MembershipInfo> memberships = MembershipsModule.getMembershipPresenter()
                                                                .getAllUserMemberships(projectId, false);
      usersContainer.setUsersMember(memberships);
    }
    catch (final ProjectServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, view.getLocale());

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
