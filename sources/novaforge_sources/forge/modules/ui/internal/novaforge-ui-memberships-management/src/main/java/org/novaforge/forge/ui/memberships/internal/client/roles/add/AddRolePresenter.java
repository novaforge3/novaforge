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
package org.novaforge.forge.ui.memberships.internal.client.roles.add;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.memberships.internal.client.containers.RoleItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowEditRoleViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RolesMappingComponent.RolesMappingException;
import org.novaforge.forge.ui.memberships.internal.module.AbstractMembershipsPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
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
public class AddRolePresenter extends AbstractMembershipsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long serialVersionUID = -5042299647493799344L;
  private static final Log LOGGER = LogFactory.getLog(AddRolePresenter.class);
  /*
   * Content of project view
   */
  private final AddRoleView view;
  /**
   * This variable contains the project Id associated to this presenter
   */
  private final String      projectId;
  private ProjectRole       currentRole;

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
  public AddRolePresenter(final AddRoleView pView, final PortalContext pPortalContext, final String pProjectId)
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
      private static final long serialVersionUID = -5090013824553391627L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new ShowEditRoleViewEvent(getUuid()));

      }
    });

    view.getAddButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 4716744769773705933L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        try
        {
          view.getRoleForm().commit();
          MembershipsModule.getProjectRolePresenter().createRole(currentRole, projectId);

          view.getRolesMappingComponent().updateRoleMapping(projectId, currentRole.getName());

          getEventBus().publish(new ShowEditRoleViewEvent(getUuid()));
        }
        catch (final ProjectServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
              view.getLocale());

        }
        catch (final RolesMappingException e)
        {
          ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
              view.getLocale());

        }
        catch (final Exception e)
        {
          LOGGER.error(e.getLocalizedMessage(), e);
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
    try
    {
      final Map<Space, List<ProjectApplication>> allSpaces = MembershipsModule.getSpacePresenter()
          .getAllSpacesWithApplications(projectId);
      view.getRolesMappingComponent().getContainer().setApplications(allSpaces, null, false);
      final Set<Entry<Space, List<ProjectApplication>>> entrySet = allSpaces.entrySet();
      for (final Entry<Space, List<ProjectApplication>> entry : entrySet)
      {
        view.getRolesMappingComponent().getTable().setCollapsed(entry.getKey().getName(), false);

      }
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }

    currentRole = MembershipsModule.getProjectRolePresenter().newRole();
    final BeanItem<Role> roleItem = new BeanItem<Role>(currentRole);
    view.getRoleForm().setItemDataSource(roleItem);
    view.getRoleForm().setVisibleItemProperties(RoleItemProperty.NAME.getPropertyId(),
        RoleItemProperty.DESCRIPTION.getPropertyId());

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
