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
package org.novaforge.forge.ui.memberships.internal.client.roles.update;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.memberships.internal.client.containers.GroupItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.RoleItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.RolesContainer;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowAddRoleViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RoleColumnActionsGenerator;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RoleColumnOrderGenerator;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RolesMappingComponent.RolesMappingException;
import org.novaforge.forge.ui.memberships.internal.module.AbstractMembershipsPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This presenter handles role view.
 * 
 * @author Guillaume Lamirand
 */
public class RolesPresenter extends AbstractMembershipsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long serialVersionUID = -5042299647493799344L;
  /*
   * Content of project view
   */
  private final RolesView   view;

  /**
   * This variable contains the project Id associated to this presenter
   */
  private final String      projectId;
  private RolesContainer    rolesContainer;
  private String            selectedRoleId;

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
  public RolesPresenter(final RolesView pView, final PortalContext pPortalContext, final String pProjectId)
  {
    super(pPortalContext);
    projectId = pProjectId;
    view = pView;

    initRolesList();
    addListeners();
  }

  private void initRolesList()
  {
    rolesContainer = new RolesContainer();
    view.getRolesTable().setContainerDataSource(rolesContainer);

    view.getRolesTable().addGeneratedColumn(CommonItemProperty.ACTIONS.getPropertyId(),
        new RoleColumnActionsGenerator(this));
    view.getRolesTable().addGeneratedColumn(RoleItemProperty.ORDER.getPropertyId(),
        new RoleColumnOrderGenerator(this));

    // Define visibles columns
    view.getRolesTable().setVisibleColumns(RoleItemProperty.NAME.getPropertyId(),
        RoleItemProperty.DESCRIPTION.getPropertyId(), RoleItemProperty.ORDER.getPropertyId(),
        CommonItemProperty.ACTIONS.getPropertyId());

    // Define special column width
    view.getRolesTable().setColumnExpandRatio(RoleItemProperty.NAME.getPropertyId(), 0.1f);
    view.getRolesTable().setColumnExpandRatio(RoleItemProperty.DESCRIPTION.getPropertyId(), 0.3f);
    view.getRolesTable().setColumnWidth(RoleItemProperty.ORDER.getPropertyId(), 65);
    view.getRolesTable().setColumnWidth(CommonItemProperty.ACTIONS.getPropertyId(), 105);

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
        getEventBus().publish(new ShowAddRoleViewEvent(getUuid()));
      }
    });
    view.getRolesTable().addActionHandler(new RefreshAction()
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

      /**
       * {@inheritDoc}
       */
      @Override
      public void textChange(final TextChangeEvent event)
      {
        // Clean filter
        rolesContainer.removeAllContainerFilters();
        if ((event.getText() != null) && !event.getText().isEmpty())
        {
          // Set new filter for the status column
          final Filter rolesFilter = new Or(new SimpleStringFilter(GroupItemProperty.NAME.getPropertyId(),
              event.getText(), true, false), new SimpleStringFilter(GroupItemProperty.DESCRIPTION
              .getPropertyId(), event.getText(), true, false));
          rolesContainer.addContainerFilter(rolesFilter);
        }
      }
    });
    view.getEditRoleCancelButton().addClickListener(new ClickListener()
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
        view.getRolesTable().unselect(selectedRoleId);
        selectedRoleId = null;
        UI.getCurrent().removeWindow(view.getEditRoleWindow());
      }
    });
    view.getEditRoleConfirmButton().addClickListener(new Button.ClickListener()
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
        if (selectedRoleId != null)
        {
          try
          {
            view.getEditRoleForm().commit();
            final Role role = (Role) rolesContainer.getContainerProperty(selectedRoleId,
                RoleItemProperty.ROLE.getPropertyId()).getValue();
            final String oldName = (String) rolesContainer.getContainerProperty(selectedRoleId,
                RoleItemProperty.NAME.getPropertyId()).getValue();
            MembershipsModule.getProjectRolePresenter().updateRole(oldName, role, projectId);
            refresh();
          }
          catch (final ProjectServiceException e)
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
            UI.getCurrent().removeWindow(view.getEditRoleWindow());
            view.getRolesTable().unselect(selectedRoleId);
            selectedRoleId = null;
          }
        }

      }
    });
    view.getEditRolesMappingCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -6375497362323527982L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getRolesTable().unselect(selectedRoleId);
        selectedRoleId = null;
        UI.getCurrent().removeWindow(view.getEditRolesMappingWindow());
      }
    });
    view.getEditRolesMappingConfirmButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 3959858785744220241L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        if (selectedRoleId != null)
        {
          try
          {
            view.getEditRolesMappingComponent().updateRoleMapping(projectId, selectedRoleId);
          }
          catch (final RolesMappingException e)
          {
            ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
                view.getLocale());

          }
        }
        view.getRolesTable().unselect(selectedRoleId);
        selectedRoleId = null;
        UI.getCurrent().removeWindow(view.getEditRolesMappingWindow());
      }
    });
    view.getDeleteRoleWindow().getYesButton().addClickListener(new Button.ClickListener()
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
        if (selectedRoleId != null)
        {
          try
          {
            MembershipsModule.getProjectRolePresenter().deleteRole(selectedRoleId, projectId);
            refresh();
          }
          catch (final ProjectServiceException e)
          {
            ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
                view.getLocale());

          }
          finally
          {
            UI.getCurrent().removeWindow(view.getDeleteRoleWindow());
            view.getRolesTable().unselect(selectedRoleId);
            selectedRoleId = null;
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
    view.getFilterTextField().setValue("");
    rolesContainer.removeAllContainerFilters();
    refreshContent();
    refreshLocalized(getCurrentLocale());

  }

  /**
   * This method is called after click event on Edit button in action column
   *
   * @param pItemId
   *          container role id
   */
  public void onClickActionEdit(final Object pItemId)
  {
    selectedRoleId = (String) pItemId;
    final Role role = (Role) rolesContainer.getContainerProperty(pItemId,
        RoleItemProperty.ROLE.getPropertyId()).getValue();

    UI.getCurrent().addWindow(view.getEditRoleWindow());
    final BeanItem<Role> roleItem = new BeanItem<Role>(role);
    view.getEditRoleForm().setItemDataSource(roleItem);
    view.getEditRoleForm().setVisibleItemProperties(RoleItemProperty.NAME.getPropertyId(),
        RoleItemProperty.DESCRIPTION.getPropertyId());
    if (RealmType.SYSTEM.equals(role.getRealmType()))
    {
      final Field<?> field = view.getEditRoleForm().getField(RoleItemProperty.NAME.getPropertyId());
      field.setEnabled(false);
      if (field instanceof AbstractField)
      {
        ((AbstractField<?>) field).setDescription(MembershipsModule.getPortalMessages().getMessage(
            view.getLocale(), Messages.MEMBERSHIPS_ROLES_EDIT_INFO_SYSTEM));
      }
    }
    view.refreshLocale(view.getLocale());

  }

  /**
   * This method is called after click event on users button in action column
   *
   * @param pItemId
   *          container role id
   */
  public void onClickActionApp(final Object pItemId)
  {
    selectedRoleId = (String) pItemId;
    final Role role = (Role) rolesContainer.getContainerProperty(pItemId,
        RoleItemProperty.ROLE.getPropertyId()).getValue();

    UI.getCurrent().addWindow(view.getEditRolesMappingWindow());
    try
    {
      final Map<Space, List<ProjectApplication>> allSpaces = MembershipsModule.getSpacePresenter()
          .getAllSpacesWithApplications(projectId);
      view.getEditRolesMappingComponent().setSelectedRole(role);
      view.getEditRolesMappingComponent().getContainer().setApplications(allSpaces, selectedRoleId, true);
      final Set<Entry<Space, List<ProjectApplication>>> entrySet = allSpaces.entrySet();
      for (final Entry<Space, List<ProjectApplication>> entry : entrySet)
      {
        view.getEditRolesMappingComponent().getTable().setCollapsed(entry.getKey().getName(), false);

      }
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, view.getLocale());

    }

  }

  /**
   * Used when click is received on delete action
   *
   * @param pItemId
   *          container role id
   */
  public void onClickActionDelete(final Object pItemId)
  {
    selectedRoleId = (String) pItemId;
    try
    {
      final List<Actor> allActorsForRole = MembershipsModule.getMembershipPresenter().getAllActorsForRole(
          projectId, selectedRoleId);
      if ((allActorsForRole != null) && (!allActorsForRole.isEmpty()))
      {
        // TODO i18n
        Notification
            .show("Vous devez supprimer toutes les affiliations utilisant ce rôle avant de pouvoir le supprimer.");
      }
      else
      {
        UI.getCurrent().addWindow(view.getDeleteRoleWindow());
      }
    }
    catch (final ProjectServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, view.getLocale());
    }
  }

  /**
   * Used when click is received on down action
   *
   * @param pItemId
   *          container role id
   */
  public void onClickOrderDown(final Object pItemId)
  {
    try
    {
      MembershipsModule.getProjectRolePresenter().changeOrder(projectId, (String) pItemId, true);
      refresh();
    }
    catch (final ProjectServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, view.getLocale());

    }

  }

  /**
   * Used when click is received on up action
   *
   * @param pItemId
   *          container role id
   */
  public void onClickOrderUp(final Object pItemId)
  {
    try
    {
      MembershipsModule.getProjectRolePresenter().changeOrder(projectId, (String) pItemId, false);
      refresh();
    }
    catch (final ProjectServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, view.getLocale());

    }

  }

  /**
   * Get the view element
   *
   * @return the view
   */
  public RolesView getView()
  {
    return view;
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
    view.attachRolesTable(false);
    try
    {
      final List<ProjectRole> roles = MembershipsModule.getProjectRolePresenter().getAllRoles(projectId);
      rolesContainer.setRoles(roles);
      rolesContainer.sort(new Object[] { RoleItemProperty.ORDER.getPropertyId() }, new boolean[] { true });
    }
    catch (final ProjectServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, view.getLocale());

    }
    view.attachRolesTable(true);

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
