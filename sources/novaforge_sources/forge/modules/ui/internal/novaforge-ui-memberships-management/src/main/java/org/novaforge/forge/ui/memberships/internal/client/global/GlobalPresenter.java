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
package org.novaforge.forge.ui.memberships.internal.client.global;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowAddGroupMemberViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowAddRoleViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowAddUserMemberViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowEditGroupMemberViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowEditRoleViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowEditUserMemberViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowRequestViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowRequestsHistoryViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.groups.add.AddGroupPresenter;
import org.novaforge.forge.ui.memberships.internal.client.groups.add.AddGroupViewImpl;
import org.novaforge.forge.ui.memberships.internal.client.groups.update.GroupsPresenter;
import org.novaforge.forge.ui.memberships.internal.client.groups.update.GroupsViewImpl;
import org.novaforge.forge.ui.memberships.internal.client.request.history.RequestHistoryPresenter;
import org.novaforge.forge.ui.memberships.internal.client.request.history.RequestHistoryViewImpl;
import org.novaforge.forge.ui.memberships.internal.client.request.inprocess.RequestPresenter;
import org.novaforge.forge.ui.memberships.internal.client.request.inprocess.RequestViewImpl;
import org.novaforge.forge.ui.memberships.internal.client.roles.add.AddRolePresenter;
import org.novaforge.forge.ui.memberships.internal.client.roles.add.AddRoleViewImpl;
import org.novaforge.forge.ui.memberships.internal.client.roles.update.RolesPresenter;
import org.novaforge.forge.ui.memberships.internal.client.roles.update.RolesViewImpl;
import org.novaforge.forge.ui.memberships.internal.client.users.add.AddUsersPresenter;
import org.novaforge.forge.ui.memberships.internal.client.users.add.AddUsersViewImpl;
import org.novaforge.forge.ui.memberships.internal.client.users.update.UsersPresenter;
import org.novaforge.forge.ui.memberships.internal.client.users.update.UsersViewImpl;
import org.novaforge.forge.ui.memberships.internal.module.AbstractMembershipsPresenter;

import java.io.Serializable;
import java.util.Locale;

/**
 * This presenter handles main view.
 * 
 * @author Guillaume Lamirand
 */
public class GlobalPresenter extends AbstractMembershipsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long             serialVersionUID = -5042299647493799344L;
  /**
   * Content of project view
   */
  private final GlobalView              view;

  private final UsersPresenter          usersPresenter;
  private final AddUsersPresenter       addUsersPresenter;
  private final GroupsPresenter         groupsPresenter;
  private final AddGroupPresenter       addGroupPresenter;
  private final RequestPresenter        requestPresenter;
  private final RequestHistoryPresenter requestHistoryPresenter;
  private final RolesPresenter          rolesPresenter;
  private final AddRolePresenter        addRolePresenter;

  /**
   * Default constructor. It will initialize the tree component associated to the view and bind some events.
   * 
   * @param pView
   *          the view
   * @param pEventBus
   *          the event bus
   * @param pProjectId
   *          project id
   */
  public GlobalPresenter(final GlobalView pView, final PortalContext pPortalContext, final String pProjectId)
  {
    super(pPortalContext);
    view = pView;
    usersPresenter = new UsersPresenter(new UsersViewImpl(), pPortalContext, pProjectId);
    addUsersPresenter = new AddUsersPresenter(new AddUsersViewImpl(), pPortalContext, pProjectId);
    groupsPresenter = new GroupsPresenter(new GroupsViewImpl(), pPortalContext, pProjectId);
    addGroupPresenter = new AddGroupPresenter(new AddGroupViewImpl(), pPortalContext, pProjectId);
    requestPresenter = new RequestPresenter(new RequestViewImpl(), pPortalContext, pProjectId);
    requestHistoryPresenter = new RequestHistoryPresenter(new RequestHistoryViewImpl(), pPortalContext, pProjectId);
    rolesPresenter = new RolesPresenter(new RolesViewImpl(), pPortalContext, pProjectId);
    addRolePresenter = new AddRolePresenter(new AddRoleViewImpl(), pPortalContext, pProjectId);

    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {

    view.getUser().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7394860492677119821L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(usersPresenter.getComponent());
        usersPresenter.refresh();
      }
    });
    view.getGroup().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 3758544191489241509L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(groupsPresenter.getComponent());
        groupsPresenter.refresh();
      }
    });
    view.getRequest().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 3758544191489241509L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(requestPresenter.getComponent());
        requestPresenter.refresh();
      }
    });
    view.getRoles().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 8346222048499795569L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(rolesPresenter.getComponent());
        rolesPresenter.refresh();
      }
    });
  }

  /**
   * Will refresh the project information.
   */
  public void refresh()
  {
    view.getUser().addStyleName(NovaForge.SELECTED);
    view.setSecondComponent(usersPresenter.getComponent());
    usersPresenter.refresh();
  }

  /**
   * Callback on {@link ShowAddUserMemberViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onAddUserEvent(final ShowAddUserMemberViewEvent pEvent)
  {
    if (getUuid().equals(pEvent.getUuid())) 
    {
      view.setSecondComponent(addUsersPresenter.getComponent());
      addUsersPresenter.refresh();
    }
  }

  /**
   * Callback on {@link ShowEditUserMemberViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onEditUserEvent(final ShowEditUserMemberViewEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid())) 
    {
      view.setSecondComponent(usersPresenter.getComponent());
      usersPresenter.refresh();
    }
  }

  /**
   * Callback on {@link ShowAddUserMemberViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onAddGroupEvent(final ShowAddGroupMemberViewEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid())) 
    {
      view.setSecondComponent(addGroupPresenter.getComponent());
      addGroupPresenter.refresh();
    }
  }

  /**
   * Callback on {@link ShowEditGroupMemberViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onEditGroupEvent(final ShowEditGroupMemberViewEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid())) 
    {
      view.setSecondComponent(groupsPresenter.getComponent());
      groupsPresenter.refresh();
    }
  }

  /**
   * Callback on {@link ShowRequestsHistoryViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onRequestHistoryEvent(final ShowRequestsHistoryViewEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid())) 
    {
      view.setSecondComponent(requestHistoryPresenter.getComponent());
      requestHistoryPresenter.refresh();
    }
  }

  /**
   * Callback on {@link ShowRequestViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onRequestEvent(final ShowRequestViewEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid())) 
    {
      view.setSecondComponent(requestPresenter.getComponent());
      requestPresenter.refresh();
    }
  }

  /**
   * Callback on {@link ShowEditRoleViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onShowEditRoleViewEvent(final ShowEditRoleViewEvent pEvent)
  {
    if (getUuid().equals(pEvent.getUuid())) 
    {
      view.setSecondComponent(rolesPresenter.getComponent());
      rolesPresenter.refresh();
    }
  }

  /**
   * Callback on {@link ShowAddRoleViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onShowAddRoleViewEvent(final ShowAddRoleViewEvent pEvent)
  {
    if (getUuid().equals(pEvent.getUuid())) 
    {
      view.setSecondComponent(addRolePresenter.getComponent());
      addRolePresenter.refresh();
    }
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
    // Doesn't handle it by default
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
