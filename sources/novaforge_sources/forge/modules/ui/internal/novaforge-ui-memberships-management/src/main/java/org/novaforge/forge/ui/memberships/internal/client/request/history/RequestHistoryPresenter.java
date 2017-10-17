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
package org.novaforge.forge.ui.memberships.internal.client.request.history;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.memberships.internal.client.containers.RequestItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.containers.RequestsContainer;
import org.novaforge.forge.ui.memberships.internal.client.events.ShowRequestViewEvent;
import org.novaforge.forge.ui.memberships.internal.client.request.components.RequestColumnDateGenerator;
import org.novaforge.forge.ui.memberships.internal.client.request.components.RequestStyleGenerator;
import org.novaforge.forge.ui.memberships.internal.module.AbstractMembershipsPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
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
public class RequestHistoryPresenter extends AbstractMembershipsPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long        serialVersionUID = 1114408567698194224L;

  /**
   * Content of project view
   */
  private final RequestHistoryView view;

  /**
   * This variable contains the project Id associated to this presenter
   */
  private final String             projectId;
  private RequestsContainer        requestsContainer;

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
  public RequestHistoryPresenter(final RequestHistoryView pView, final PortalContext pPortalContext,
      final String pProjectId)
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

    view.getRequestsTable().addGeneratedColumn(RequestItemProperty.DATE.getPropertyId(),
        new RequestColumnDateGenerator(this));
    view.getRequestsTable().setCellStyleGenerator(new RequestStyleGenerator());

    // Define visibles columns
    view.getRequestsTable().setVisibleColumns(RequestItemProperty.LOGIN.getPropertyId(),
        RequestItemProperty.FIRSTNAME.getPropertyId(), RequestItemProperty.LASTNAME.getPropertyId(),
        RequestItemProperty.DATE.getPropertyId(), RequestItemProperty.MESSAGE.getPropertyId());

    // Define special column width
    view.getRequestsTable().setColumnExpandRatio(RequestItemProperty.LOGIN.getPropertyId(), 0);
    view.getRequestsTable().setColumnExpandRatio(RequestItemProperty.FIRSTNAME.getPropertyId(), 0.1f);
    view.getRequestsTable().setColumnExpandRatio(RequestItemProperty.LASTNAME.getPropertyId(), 0.1f);
    view.getRequestsTable().setColumnExpandRatio(RequestItemProperty.DATE.getPropertyId(), 0.1f);
    view.getRequestsTable().setColumnExpandRatio(RequestItemProperty.MESSAGE.getPropertyId(), 0.3f);
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
      private static final long serialVersionUID = -6790086710358355818L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new ShowRequestViewEvent(getUuid()));

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

  /**
   * Get the view element
   *
   * @return the view
   */
  public RequestHistoryView getView()
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
    try
    {
      final List<MembershipRequest> membershipsRequest = MembershipsModule.getMembershipRequestPresenter()
          .getHistoryRequests(projectId);
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
