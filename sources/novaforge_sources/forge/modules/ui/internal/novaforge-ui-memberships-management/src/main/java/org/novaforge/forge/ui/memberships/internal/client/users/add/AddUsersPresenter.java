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
package org.novaforge.forge.ui.memberships.internal.client.users.add;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowEditUserMemberViewEvent;
import org.novaforge.forge.ui.memberships.internal.module.AbstractMembershipsPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * This presenter handles user creating view.
 * 
 * @author Guillaume Lamirand
 */
public class AddUsersPresenter extends AbstractMembershipsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long serialVersionUID = -3983611978952960374L;

  /**
   * Content of project view
   */
  private final AddUserView view;

  /**
   * This variable contains the project Id associated to this presenter
   */
  private final String      projectId;

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
  public AddUsersPresenter(final AddUserView pView, final PortalContext pPortalContext,
      final String pProjectId)
  {
    super(pPortalContext);
    projectId = pProjectId;
    view = pView;
    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getReturnButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -8598060626958292438L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getAddRolesComponent().clearComponent();
        getEventBus().publish(new ShowEditUserMemberViewEvent(getUuid()));

      }
    });
    view.getUserTableSelectable().getUsersTable().addActionHandler(new RefreshAction()
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
    view.getAddButton().addClickListener(new ClickListener()
    {

      /**
       * Seriak version id
       */
      private static final long serialVersionUID = 1789892475466769544L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        if (!view.getUserTableSelectable().getSelectedUser().isEmpty())
        {
          view.getAddRolesComponent().clearComponent();
          try
          {
            view.getAddRolesComponent().attachRolesTable(false);
            // Initialize project's roles
            final List<ProjectRole> allRoles = MembershipsModule.getProjectRolePresenter().getAllRoles(
                projectId);
            view.getAddRolesComponent().getRolesContainer().setRoles(allRoles);

            view.getAddRolesComponent().attachRolesTable(true);
            UI.getCurrent().addWindow(view.getAddRolesWindow());

          }
          catch (final ProjectServiceException e)
          {
            ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
                view.getLocale());

          }
        }
        else
        {
          Notification.show(
              MembershipsModule.getPortalMessages().getMessage(view.getLocale(),
                  Messages.MEMBERSHIPS_USERS_ADD_SELECT), Type.WARNING_MESSAGE);
        }
      }
    });

    view.getAddRolesComponent().getCancelButton().addClickListener(new ClickListener()
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
        UI.getCurrent().removeWindow(view.getAddRolesWindow());
      }
    });
    view.getAddRolesComponent().getConfirmButton().addClickListener(new ClickListener()
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
        if (!view.getAddRolesComponent().getCurrentRoles().isEmpty())
        {
          for (final UUID userUUID : view.getUserTableSelectable().getSelectedUser())
          {
            try
            {
              MembershipsModule.getMembershipPresenter().addUserMembership(projectId, userUUID,
                  view.getAddRolesComponent().getCurrentRoles(),
                  view.getAddRolesComponent().getDefaultRole(), true);
            }
            catch (final ProjectServiceException e)
            {
              ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
                  view.getLocale());

            }
          }
          view.getUserTableSelectable().clear();
          view.getAddRolesComponent().clearComponent();
          UI.getCurrent().removeWindow(view.getAddRolesWindow());
          getEventBus().publish(new ShowEditUserMemberViewEvent(getUuid()));
        }
        else
        {
          Notification.show(
              MembershipsModule.getPortalMessages().getMessage(view.getLocale(),
                  Messages.MEMBERSHIPS_USERS_ADD_ROLES_SELECT), Type.WARNING_MESSAGE);
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
    view.getUserTableSelectable().clear();
    try
    {
      final List<MembershipInfo> memberships = MembershipsModule.getMembershipPresenter()
          .getAllUserMemberships(projectId, true);
      final List<User> users = MembershipsModule.getUserPresenter().getAllUsers(true);
      final List<User> availableUsers = new ArrayList<User>();
      for (final User user : users)
      {
        boolean isMember = false;
        for (final MembershipInfo member : memberships)
        {
          if (member.getActor().getUuid().equals(user.getUuid()))
          {
            isMember = true;
            break;
          }
        }
        if (!isMember)
        {
          availableUsers.add(user);
        }

      }
      view.getUserTableSelectable().getUsersContainer().setUsers(availableUsers);
    }
    catch (final Exception e)
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
