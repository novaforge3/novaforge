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
package org.novaforge.forge.ui.memberships.internal.client.groups.update;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.memberships.internal.client.containers.GroupItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.GroupMembersContainer;
import org.novaforge.forge.ui.memberships.internal.client.containers.RoleItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.UserItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.UsersContainer;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowAddGroupMemberViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.groups.components.GroupColumnNameGenerator;
import org.novaforge.forge.ui.memberships.internal.client.groups.components.GroupColumnUsersGenerator;
import org.novaforge.forge.ui.memberships.internal.client.groups.components.GroupColumnVisibiltyGenerator;
import org.novaforge.forge.ui.memberships.internal.client.groups.components.GroupMemberColumnActionsGenerator;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RoleColumnNameGenerator;
import org.novaforge.forge.ui.memberships.internal.module.AbstractMembershipsPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;
import org.novaforge.forge.ui.portal.event.OpenUserProfileEvent;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * This presenter handles group view.
 * 
 * @author Guillaume Lamirand
 */
public class GroupsPresenter extends AbstractMembershipsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long     serialVersionUID = -5042299647493799344L;
  /*
   * Content of project view
   */
  private final GroupsView      view;

  /**
   * This variable contains the project Id associated to this presenter
   */
  private final String          projectId;
  private final boolean isForgeProject;
  private GroupMembersContainer groupsContainer;
  private UUID                  selectedGroupId;
  private UsersContainer        viewUsersContainer;

  /**
   * Default constructor. It will initialize the tree component associated to the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pPortalContext
   *          the initial context
   * @param pProjectId
   *          project id
   */
  public GroupsPresenter(final GroupsView pView, final PortalContext pPortalContext, final String pProjectId)
  {
    super(pPortalContext);
    projectId = pProjectId;
    view = pView;
    isForgeProject = MembershipsModule.getProjectPresenter().isForgeProject(projectId);

    initGroupsList();
    addListeners();
  }

  private void initGroupsList()
  {
    groupsContainer = new GroupMembersContainer();
    view.getGroupsTable().setContainerDataSource(groupsContainer);

    view.getGroupsTable().addGeneratedColumn(GroupItemProperty.ROLES.getPropertyId(),
        new RoleColumnNameGenerator());
    view.getGroupsTable().addGeneratedColumn(CommonItemProperty.ACTIONS.getPropertyId(),
        new GroupMemberColumnActionsGenerator(this));
    view.getGroupsTable().addGeneratedColumn(GroupItemProperty.USERS.getPropertyId(),
        new GroupColumnUsersGenerator());

    // Define visibles columns
    if (isForgeProject)
    {
      view.getGroupsTable().addGeneratedColumn(GroupItemProperty.VISIBILITY.getPropertyId(),
          new GroupColumnVisibiltyGenerator());
      view.getGroupsTable().setVisibleColumns(GroupItemProperty.NAME.getPropertyId(),
          GroupItemProperty.DESCRIPTION.getPropertyId(), GroupItemProperty.VISIBILITY.getPropertyId(),
          GroupItemProperty.USERS.getPropertyId(), GroupItemProperty.ROLES.getPropertyId(),
          CommonItemProperty.ACTIONS.getPropertyId());
    }
    else
    {
      view.getGroupsTable().addGeneratedColumn(GroupItemProperty.NAME.getPropertyId(),
          new GroupColumnNameGenerator());
      view.getGroupsTable().setVisibleColumns(GroupItemProperty.NAME.getPropertyId(),
          GroupItemProperty.DESCRIPTION.getPropertyId(), GroupItemProperty.USERS.getPropertyId(),
          GroupItemProperty.ROLES.getPropertyId(), CommonItemProperty.ACTIONS.getPropertyId());

      view.getGroupsTable().setItemDescriptionGenerator(new ItemDescriptionGenerator()
      {

        /**
         * Serial version id
         */
        private static final long serialVersionUID = -7822460603540735951L;

        @Override
        public String generateDescription(final Component pSource, final Object pItemId,
            final Object pPropertyId)
        {
          if ( Boolean.FALSE == groupsContainer.getContainerProperty(pItemId,GroupItemProperty.EDITABLE.getPropertyId()).getValue() )
            //inherited forge project group 
            return MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
              Messages.MEMBERSHIPS_GROUPS_PUBLIC_INHERIT_DESCRIPTION);
          else
            return StringUtils.EMPTY;
        }
      });
    }

    // Define special column width
    view.getGroupsTable().setColumnExpandRatio(GroupItemProperty.NAME.getPropertyId(), 0.1f);
    view.getGroupsTable().setColumnExpandRatio(GroupItemProperty.DESCRIPTION.getPropertyId(), 0.3f);
    if (isForgeProject)
    {
      view.getGroupsTable().setColumnWidth(GroupItemProperty.VISIBILITY.getPropertyId(), 45);

    }
    view.getGroupsTable().setColumnWidth(GroupItemProperty.USERS.getPropertyId(), 70);
    view.getGroupsTable().setColumnExpandRatio(GroupItemProperty.ROLES.getPropertyId(), 0.3f);
    view.getGroupsTable().setColumnWidth(CommonItemProperty.ACTIONS.getPropertyId(), 150);
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getCreateButton().addClickListener(new ClickListener()
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
        getEventBus().publish(new ShowAddGroupMemberViewEvent(getUuid()));
      }
    });
    view.getGroupsTable().addActionHandler(new RefreshAction()
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
        refresh();

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
        groupsContainer.removeAllContainerFilters();
        if ((event.getText() != null) && !event.getText().isEmpty())
        {
          // Set new filter for the status column
          final Filter groupsFilter = new Or(new SimpleStringFilter(GroupItemProperty.NAME.getPropertyId(),
              event.getText(), true, false), new SimpleStringFilter(GroupItemProperty.DESCRIPTION
              .getPropertyId(), event.getText(), true, false));
          groupsContainer.addContainerFilter(groupsFilter);
        }
      }
    });
    // Adding listener for edit group window
    addEditGroupListeners();
    // Adding listener for edit users window
    addEditUsersListener();
    // Adding listener for edit roles window
    addEditRolesListener();
    // Adding listener for delete group window
    addDeleteListener();

  }

  private void addEditUsersListener()
  {
    view.getEditUsersTable().getUsersTable().addActionHandler(new RefreshAction()
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
        onClickActionEdit(selectedGroupId);

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
    view.getEditUsersCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 6683304940303317861L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getGroupsTable().unselect(selectedGroupId);
        selectedGroupId = null;
        UI.getCurrent().removeWindow(view.getEditUsersWindow());
      }
    });
    view.getViewUsersTable().addItemClickListener(new ItemClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -8284187924769570196L;

      @Override
      public void itemClick(ItemClickEvent event)
      {
        try
        {
          UserProfile userProfile = MembershipsModule.getUserPresenter().getUserProfile(
              viewUsersContainer.getLogin((UUID) event.getItemId()));
          getEventBus().publish(new OpenUserProfileEvent(userProfile));
          UI.getCurrent().removeWindow(view.getViewUsersWindow());
        }
        catch (UserServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
              getCurrentLocale());
        }
      }
    });
    view.getEditUsersConfirmButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -8723104952310319609L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        if (selectedGroupId != null)
        {
          try
          {
            final Group group = (Group) groupsContainer.getContainerProperty(selectedGroupId,
                GroupItemProperty.GROUP.getPropertyId()).getValue();
            final String oldName = (String) groupsContainer.getContainerProperty(selectedGroupId,
                GroupItemProperty.NAME.getPropertyId()).getValue();
            group.clearUsers();
            for (final UUID userUUID : view.getEditUsersTable().getSelectedUser())
            {
              final String userLogin = view.getEditUsersTable().getUsersContainer().getLogin(userUUID);
              final User user = MembershipsModule.getUserPresenter().getUser(userLogin);
              group.addUser(user);
            }
            MembershipsModule.getGroupPresenter().updateGroup(projectId, oldName, group);
            refresh();
          }
          catch (final Exception e)
          {
            ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
                view.getLocale());

          }
          finally
          {
            UI.getCurrent().removeWindow(view.getEditUsersWindow());
            view.getGroupsTable().unselect(selectedGroupId);
            selectedGroupId = null;
          }
        }

      }

    });
  }

  private void addEditGroupListeners()
  {
    view.getEditGroupCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 6720186973107596177L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getGroupsTable().unselect(selectedGroupId);
        selectedGroupId = null;
        UI.getCurrent().removeWindow(view.getEditGroupWindow());
      }
    });
    view.getEditGroupConfirmButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 7409462208026149835L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        if (selectedGroupId != null)
        {
          try
          {
            view.getEditGroupForm().commit();
            final Group group = (Group) groupsContainer.getContainerProperty(selectedGroupId,
                GroupItemProperty.GROUP.getPropertyId()).getValue();
            final String oldName = (String) groupsContainer.getContainerProperty(selectedGroupId,
                GroupItemProperty.NAME.getPropertyId()).getValue();
            MembershipsModule.getGroupPresenter().updateGroup(projectId, oldName, group);
            refresh();
          }
          catch (final GroupServiceException e)
          {
            ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
                view.getLocale());

          }
          catch (final Exception e)
          {
            // Let form manage it
          }
          finally
          {
            UI.getCurrent().removeWindow(view.getEditGroupWindow());
            view.getGroupsTable().unselect(selectedGroupId);
            selectedGroupId = null;
            refresh();
          }
        }

      }
    });
  }

  private void addEditRolesListener()
  {
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
        view.getGroupsTable().unselect(selectedGroupId);
        selectedGroupId = null;
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
          MembershipsModule.getMembershipPresenter().updateGroupMembership(projectId, selectedGroupId,
              view.getEditRolesComponent().getSelectedRoles(), view.getEditRolesComponent().getDefaultRole(),
              true);
        }
        catch (final ProjectServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
              view.getLocale());

        }
        view.getGroupsTable().unselect(selectedGroupId);
        view.getEditRolesComponent().clearComponent();
        selectedGroupId = null;
        UI.getCurrent().removeWindow(view.getEditRolesWindow());
        refresh();

      }
    });
  }

  private void addDeleteListener()
  {
    view.getDeleteGroupCancelButton().addClickListener(new ClickListener()
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
        view.getGroupsTable().unselect(selectedGroupId);
        selectedGroupId = null;
        UI.getCurrent().removeWindow(view.getDeleteGroupWindow());
      }
    });
    view.getViewUserCloseButton().addClickListener(new Button.ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = -6602504917726777077L;

      @Override
      public void buttonClick(ClickEvent event)
      {
        UI.getCurrent().removeWindow(view.getViewUsersWindow());
      }

    });
    view.getViewUsersFiltersText().addTextChangeListener(new TextChangeListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 8834238323211562858L;

      @Override
      public void textChange(TextChangeEvent event)
      {
        Filterable container = viewUsersContainer;
        container.removeAllContainerFilters();
        container.addContainerFilter(new Or(new SimpleStringFilter(
            UserItemProperty.FIRSTNAME.getPropertyId(), event.getText(), true, false),
            new SimpleStringFilter(UserItemProperty.LASTNAME.getPropertyId(), event.getText(), true, false),
            new SimpleStringFilter(UserItemProperty.LOGIN.getPropertyId(), event.getText(), true, false),
            new SimpleStringFilter(UserItemProperty.EMAIL.getPropertyId(), event.getText(), true, false)));

      }
    });
    view.getDeleteGroupConfirmButton().addClickListener(new Button.ClickListener()
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
        if (selectedGroupId != null)
        {
          try
          {
            MembershipsModule.getMembershipPresenter().removeGroupMembership(projectId, selectedGroupId,
                view.getDeleteGroupNotifCheck().getValue());
            MembershipsModule.getGroupPresenter().deleteGroup(projectId, selectedGroupId);
            refresh();
          }
          catch (final Exception e)
          {
            ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
                view.getLocale());

          }
          finally
          {
            UI.getCurrent().removeWindow(view.getDeleteGroupWindow());
            view.getGroupsTable().unselect(selectedGroupId);
            selectedGroupId = null;
          }
        }

      }
    });
  }

  /**
   * Will refresh the project information.
   */
  public void refresh()
  {
    refreshContent();
    refreshLocalized(getCurrentLocale());

  }

  /**
   * This method is called after click event on Edit button in action column
   *
   * @param pItemId
   *          container group id
   */
  public void onClickActionEdit(final Object pItemId)
  {
    selectedGroupId = (UUID) pItemId;
    final Group group = (Group) groupsContainer.getContainerProperty(pItemId,
        GroupItemProperty.GROUP.getPropertyId()).getValue();

    final BeanItem<Group> groupItem = new BeanItem<Group>(group);
    view.getEditGroupForm().setItemDataSource(groupItem);
    if (isForgeProject)
    {
      view.getEditGroupForm().setVisibleItemProperties(GroupItemProperty.GROUP_FORGE_FIELDS);
    }
    else
    {
      view.getEditGroupForm().setVisibleItemProperties(GroupItemProperty.GROUP_FIELDS);
    }
    UI.getCurrent().addWindow(view.getEditGroupWindow());
    view.refreshLocale(view.getLocale());

  }

  /**
   * This method is called after click event on view users button in action column
   *
   * @param pItemId
   *          container group id
   */
  public void onClickActionViewUsers(final Object pItemId)
  {
    selectedGroupId = (UUID) pItemId;
    final Group group = (Group) groupsContainer.getContainerProperty(pItemId,
        GroupItemProperty.GROUP.getPropertyId()).getValue();

    viewUsersContainer = new UsersContainer();
    viewUsersContainer.setUsers(group.getUsers());

    initUsersList();

    UI.getCurrent().addWindow(view.getViewUsersWindow());
    view.refreshLocale(view.getLocale());

  }

  private void initUsersList()
  {
    view.getViewUsersTable().setContainerDataSource(viewUsersContainer);

    // Define visibles columns
    view.getViewUsersTable().setVisibleColumns(UserItemProperty.LOGIN.getPropertyId(),
        UserItemProperty.FIRSTNAME.getPropertyId(), UserItemProperty.LASTNAME.getPropertyId(),
        UserItemProperty.EMAIL.getPropertyId());

    // Define special column width
    view.getViewUsersTable().setColumnExpandRatio(UserItemProperty.LOGIN.getPropertyId(), 0);
    view.getViewUsersTable().setColumnExpandRatio(UserItemProperty.FIRSTNAME.getPropertyId(), 0.1f);
    view.getViewUsersTable().setColumnExpandRatio(UserItemProperty.LASTNAME.getPropertyId(), 0.1f);
    view.getViewUsersTable().setColumnExpandRatio(UserItemProperty.EMAIL.getPropertyId(), 0.3f);
  }

  /**
   * This method is called after click event on users button in action column
   *
   * @param pItemId
   *          container group id
   */
  public void onClickActionUsers(final Object pItemId)
  {
    selectedGroupId = (UUID) pItemId;
    final Group group = (Group) groupsContainer.getContainerProperty(pItemId,
        GroupItemProperty.GROUP.getPropertyId()).getValue();

    view.getEditUsersTable().clear();
    try
    {
      final List<User> users = MembershipsModule.getUserPresenter().getAllUsers(false);
      view.getEditUsersTable().attachUsersTable(false);
      view.getEditUsersTable().getUsersContainer().setUsers(users);
      for (final User user : group.getUsers())
      {
        view.getEditUsersTable().getUsersTable().select(user.getUuid());
      }
    }
    catch (final UserServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, view.getLocale());
    }

    UI.getCurrent().addWindow(view.getEditUsersWindow());
    view.getEditUsersTable().attachUsersTable(true);
    view.refreshLocale(view.getLocale());

  }

  /**
   * Used when click is received on roles action
   *
   * @param pItemId
   *          contains the group id
   */
  @SuppressWarnings("unchecked")
  public void onClickActionRoles(final Object pItemId)
  {
    selectedGroupId = (UUID) pItemId;
    try
    {
      view.getEditRolesComponent().attachRolesTable(false);
      // Initialize project's roles
      final List<ProjectRole> allRoles = MembershipsModule.getProjectRolePresenter().getAllRoles(projectId);
      view.getEditRolesComponent().getRolesContainer().setRoles(allRoles);

      // Initialize group's membership
      final List<MembershipInfo> membershipsInfo = (List<MembershipInfo>) groupsContainer.getItem(pItemId)
          .getItemProperty(GroupItemProperty.MEMBERSHIPS.getPropertyId()).getValue();

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
   *          container group id
   */
  public void onClickActionDelete(final Object pItemId)
  {
    selectedGroupId = (UUID) pItemId;
    UI.getCurrent().addWindow(view.getDeleteGroupWindow());
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
    view.attachGroupsTable(false);
    view.getDeleteGroupNotifCheck().setValue(true);
    view.getFilterTextField().setValue(""); //$NON-NLS-1$
    groupsContainer.removeAllContainerFilters();
    try
    {
      final List<Group> groups = MembershipsModule.getGroupPresenter().getAllGroups(projectId, true);
      groupsContainer.setGroups(groups, isForgeProject);
      final List<MembershipInfo> memberships = MembershipsModule.getMembershipPresenter()
                                                                .getAllGroupMemberships(projectId);
      groupsContainer.setGroupsMember(memberships);
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, view.getLocale());

    }
    view.attachGroupsTable(true);

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
