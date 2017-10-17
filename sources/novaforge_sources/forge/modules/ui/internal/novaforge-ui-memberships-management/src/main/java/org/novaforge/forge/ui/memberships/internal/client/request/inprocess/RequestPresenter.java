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
package org.novaforge.forge.ui.memberships.internal.client.request.inprocess;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.memberships.internal.client.containers.RequestItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.RequestsContainer;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowRequestsHistoryViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.request.components.RequestColumnActionsGenerator;
import org.novaforge.forge.ui.memberships.internal.module.AbstractMembershipsPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * This presenter handles main view.
 * 
 * @author Guillaume Lamirand
 */
public class RequestPresenter extends AbstractMembershipsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long serialVersionUID = 1114408567698194224L;

  /**
   * Content of project view
   */
  private final RequestView view;

  /**
   * This variable contains the project Id associated to this presenter
   */
  private final String      projectId;
  private RequestsContainer requestsContainer;
  private String            requestItemId;

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
  public RequestPresenter(final RequestView pView, final PortalContext pPortalContext, final String pProjectId)
  {
    super(pPortalContext);
    projectId = pProjectId;
    view = pView;
    initRequestsList();
    addListeners();
  }

  private void initRequestsList()
  {
    requestsContainer = new RequestsContainer();
    view.getRequestsTable().setContainerDataSource(requestsContainer);

    view.getRequestsTable().addGeneratedColumn(CommonItemProperty.ACTIONS.getPropertyId(),
        new RequestColumnActionsGenerator(this));

    // Define visibles columns
    view.getRequestsTable().setVisibleColumns(RequestItemProperty.LOGIN.getPropertyId(),
        RequestItemProperty.FIRSTNAME.getPropertyId(), RequestItemProperty.LASTNAME.getPropertyId(),
        RequestItemProperty.MESSAGE.getPropertyId(), CommonItemProperty.ACTIONS.getPropertyId());

    // Define special column width
    view.getRequestsTable().setColumnExpandRatio(RequestItemProperty.LOGIN.getPropertyId(), 0);
    view.getRequestsTable().setColumnExpandRatio(RequestItemProperty.FIRSTNAME.getPropertyId(), 0.1f);
    view.getRequestsTable().setColumnExpandRatio(RequestItemProperty.LASTNAME.getPropertyId(), 0.1f);
    view.getRequestsTable().setColumnExpandRatio(RequestItemProperty.MESSAGE.getPropertyId(), 0.3f);
    view.getRequestsTable().setColumnWidth(CommonItemProperty.ACTIONS.getPropertyId(), 70);
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getHistoryButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -8596098234975305833L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new ShowRequestsHistoryViewEvent(getUuid()));

      }
    });
    view.getRequestsTable().addActionHandler(new RefreshAction()
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
       * Serial version id
       */
      private static final long serialVersionUID = 1978421308920978868L;

      @Override
      public void textChange(final TextChangeEvent event)
      {
        // Clean filter
        requestsContainer.removeAllContainerFilters();
        if ((event.getText() != null) && !event.getText().isEmpty())
        {
          // Set new filter for the status column
          final Filter usersFilter = new Or(new SimpleStringFilter(RequestItemProperty.LOGIN.getPropertyId(),
              event.getText(), true, false), new SimpleStringFilter(RequestItemProperty.FIRSTNAME
              .getPropertyId(), event.getText(), true, false), new SimpleStringFilter(
              RequestItemProperty.LASTNAME.getPropertyId(), event.getText(), true, false));
          requestsContainer.addContainerFilter(usersFilter);
        }
      }
    });
    view.getInValidRequestCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 487663220547102115L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getRequestsTable().unselect(requestItemId);
        requestItemId = null;
        UI.getCurrent().removeWindow(view.getInValidRequestWindow());
      }
    });
    view.getInValidRequestConfirmButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 3006562226727030339L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        if (requestItemId != null)
        {
          try
          {
            final String userLogin = (String) requestsContainer.getContainerProperty(requestItemId,
                RequestItemProperty.LOGIN.getPropertyId()).getValue();
            MembershipsModule.getMembershipRequestPresenter().invalidateRequest(projectId, userLogin,
                view.getInValidRequestText().getValue(), true);
          }
          catch (final ProjectServiceException e)
          {
            ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
                view.getLocale());

          }
          view.getRequestsTable().unselect(requestItemId);
          view.getInValidRequestText().setValue("");
          requestItemId = null;
          UI.getCurrent().removeWindow(view.getInValidRequestWindow());
          refresh();
        }

      }
    });
    view.getValidRequestComponent().getCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -4243706532842772516L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getRequestsTable().unselect(requestItemId);
        view.getValidRequestComponent().clearComponent();
        requestItemId = null;
        UI.getCurrent().removeWindow(view.getValidRequestWindow());
      }
    });
    view.getValidRequestComponent().getConfirmButton().addClickListener(new ClickListener()
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
        if (!view.getValidRequestComponent().getCurrentRoles().isEmpty())
        {
          try
          {
            MembershipsModule.getMembershipRequestPresenter().validateRequest(projectId, requestItemId,
                view.getValidRequestComponent().getCurrentRoles(),
                view.getValidRequestComponent().getDefaultRole(), true);
          }
          catch (final ProjectServiceException e)
          {
            ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e,
                view.getLocale());

          }
          view.getRequestsTable().unselect(requestItemId);
          view.getValidRequestComponent().clearComponent();
          requestItemId = null;
          UI.getCurrent().removeWindow(view.getValidRequestWindow());
          refresh();
        }
        else
        {
          // TODO i18n
          Notification.show("Vous devez sélectionner au moins un rôle.", Type.WARNING_MESSAGE);
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
    requestsContainer.removeAllContainerFilters();
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  public void onClickActionValid(final Object pItemId)
  {
    requestItemId = (String) pItemId;
    view.getValidRequestComponent().clearComponent();
    try
    {
      view.getValidRequestComponent().attachRolesTable(false);
      // Initialize project's roles
      final List<ProjectRole> allRoles = MembershipsModule.getProjectRolePresenter().getAllRoles(projectId);
      view.getValidRequestComponent().getRolesContainer().setRoles(allRoles);

      view.getValidRequestComponent().attachRolesTable(true);
      UI.getCurrent().addWindow(view.getValidRequestWindow());

    }
    catch (final ProjectServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(MembershipsModule.getPortalMessages(), e, view.getLocale());

    }

  }

  public void onClickActionInValid(final Object pItemId)
  {
    requestItemId = (String) pItemId;
    view.getInValidRequestText().setValue("");
    UI.getCurrent().addWindow(view.getInValidRequestWindow());

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
    for (final Object itemId : requestsContainer.getItemIds())
    {
      if (view.getRequestsTable().isSelected(itemId))
      {
        view.getRequestsTable().unselect(itemId);
      }
    }
    try
    {
      final List<MembershipRequest> membershipsRequest = MembershipsModule.getMembershipRequestPresenter()
                                                                          .getInProcessRequests(projectId);
      requestsContainer.setRequests(membershipsRequest);
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
