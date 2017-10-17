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
package org.novaforge.forge.ui.memberships.internal.client.groups.add;

import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Action;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.memberships.internal.client.containers.GroupItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.UserItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowEditGroupMemberViewEvent;
import org.novaforge.forge.ui.memberships.internal.module.AbstractMembershipsPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.event.actions.PortalAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * This presenter handles group creating view.
 * 
 * @author Guillaume Lamirand
 */
public class AddGroupPresenter extends AbstractMembershipsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long  serialVersionUID = -5042299647493799344L;
  /*
   * Content of project view
   */
  private final AddGroupView view;

  /**
   * This variable contains the project Id associated to this presenter
   */
  private final String       projectId;
  private Group              currentGroup;

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
  public AddGroupPresenter(final AddGroupView pView, final PortalContext pPortalContext,
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
        view.getUserTableSelectable().clear();
        getEventBus().publish(new ShowEditGroupMemberViewEvent(getUuid()));

      }
    });
    view.getUserTableSelectable().getUsersTable().addActionHandler(new Action.Handler()
    {
      /**
       * Serial version id
       */
      private static final long   serialVersionUID = 8749999788468344743L;

      /**
       * Refresh action id
       */
      private static final String REFRESH          = "refresh";

      /**
       * {@inheritDoc}
       */
      @Override
      public Action[] getActions(final Object pTarget, final Object pSender)
      {
        final String message = MembershipsModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                Messages.ACTIONS_REFRESH);
        return new Action[] { new PortalAction(REFRESH, message) };
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void handleAction(final Action pAction, final Object pSender, final Object pTarget)
      {
        if ((pAction instanceof PortalAction) && (REFRESH.equals(((PortalAction) pAction).getId())))
        {
          refresh();
        }
      }
    });
    view.getAddButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 1789892475466769544L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        try
        {
          view.getGroupForm().commit();
          for (final UUID userUUID : view.getUserTableSelectable().getSelectedUser())
          {
            final String userLogin = (String) view.getUserTableSelectable().getUsersContainer()
                .getContainerProperty(userUUID, UserItemProperty.LOGIN.getPropertyId()).getValue();
            final User user = MembershipsModule.getUserPresenter().getUser(userLogin);
            currentGroup.addUser(user);
          }
          MembershipsModule.getGroupPresenter().createGroup(currentGroup, projectId);

          view.getUserTableSelectable().clear();
          getEventBus().publish(new ShowEditGroupMemberViewEvent(getUuid()));
        }
        catch (final GroupServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
              view.getLocale());
        }
        catch (final Exception e)
        {
          // Let's form handle exception
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
    final boolean isForgeProject = MembershipsModule.getProjectPresenter().isForgeProject(projectId);

    currentGroup = MembershipsModule.getGroupPresenter().newGroup();
    final BeanItem<Group> groupItem = new BeanItem<Group>(currentGroup);
    view.getGroupForm().setItemDataSource(groupItem);
    if (isForgeProject)
    {
      view.getGroupForm().setVisibleItemProperties(Arrays.asList(GroupItemProperty.GROUP_FORGE_FIELDS));
    }
    else
    {
      view.getGroupForm().setVisibleItemProperties(Arrays.asList(GroupItemProperty.GROUP_FIELDS));
    }

    view.getUserTableSelectable().clear();
    try
    {
      final List<User> users = MembershipsModule.getUserPresenter().getAllUsers(false);
      view.getUserTableSelectable().getUsersContainer().setUsers(users);
    }
    catch (final UserServiceException e)
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
