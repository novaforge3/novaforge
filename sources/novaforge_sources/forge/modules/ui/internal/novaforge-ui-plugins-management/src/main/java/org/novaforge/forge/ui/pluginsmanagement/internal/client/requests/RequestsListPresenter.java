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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.requests;

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.model.ProjectApplicationRequest;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.event.OpenInstancesListEvent;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.event.OpenPluginsListEvent;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.InstancesContainer;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.InstancesItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.requests.components.RequestColumnActionsGenerator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.requests.components.RequestsContainer;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.requests.components.RequestsItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.AbstractPluginsPresenter;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class RequestsListPresenter extends AbstractPluginsPresenter implements Serializable
{
  /**
   * SerialUID
   */
  private static final long               serialVersionUID   = 1272245875476596156L;
  /**
   * Content the workspace view
   */
  private final RequestsListView          view;
  private final RequestsContainer         requestsContainer  = new RequestsContainer();
  private final InstancesContainer        instancesContainer = new InstancesContainer();
  private String                          pluginID;
  private List<ProjectApplicationRequest> requestsAvailable;
  private String                          requestSelectedID;

  /**
   * Default constructor
   * 
   * @param pView
   *          The view to associate to this presenter
   * @param pPortalContext
   *          the initial context
   */
  public RequestsListPresenter(final RequestsListView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Init view datas
    initRequestsList();
    // Add listeners
    addListeners();

  }

  /**
   * Initialize the requests list
   */
  private void initRequestsList()
  {
    view.getRequestsTable().setContainerDataSource(requestsContainer);
    view.getRequestsTable().addGeneratedColumn(RequestsItemProperty.ACTIONS.getPropertyId(),
        new RequestColumnActionsGenerator(this));
    view.getRequestsTable().setVisibleColumns(RequestsItemProperty.PROJECT.getPropertyId(),
        RequestsItemProperty.CREATEDDATE.getPropertyId(), RequestsItemProperty.LOGIN.getPropertyId(),
        RequestsItemProperty.ACTIONS.getPropertyId());

  }

  /**
   * Add the listener to view components
   */
  @SuppressWarnings("serial")
  private void addListeners()
  {
    view.getReturnToPluginsList().addClickListener(new ClickListener()
    {

      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        getEventBus().publish(new OpenPluginsListEvent());

      }
    });
    view.getGoToInstancesList().addClickListener(new ClickListener()
    {
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        getEventBus().publish(new OpenInstancesListEvent(pluginID));
      }
    });
    view.getLinkInstanceButtonSubmit().addClickListener(new ClickListener()
    {

      @Override
      public void buttonClick(final ClickEvent pEvent)
      {

        final ProjectApplicationRequest request = findRequestInContainer(requestsContainer
            .getItem(requestSelectedID));
        try
        {
          final ToolInstance instance = PluginsModule.getPluginsManager().getPluginService(pluginID)
              .getToolInstanceProvisioningService()
              .getToolInstanceByUUID((UUID) view.getLinkInstanceComboBox().getValue());
          PluginsModule.getProjectApplicationRequestPresenter().handleRequest(request, instance.getUUID());
          view.getLinkInstanceComboBox().setValue(null);
          requestSelectedID = null;
          refresh(pluginID);
          UI.getCurrent().removeWindow(view.getLinkInstanceWindow());
        }
        catch (final Exception e)
        {
          Notification.show(
              PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
              PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.PLUGINSMANAGEMENT_ERROR_LINKING_INSTANCE), Type.ERROR_MESSAGE);
        }

      }
    });
    view.getDeleteRequestWindow().getYesButton().addClickListener(new ClickListener()
    {

      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        try
        {
          final ProjectApplicationRequest applicationRequest = findRequestInContainer(view.getRequestsTable()
              .getItem(requestSelectedID));
          PluginsModule.getProjectApplicationRequestPresenter().deleteRequest(
              applicationRequest.getProject().getElementId(),
              applicationRequest.getApplication().getPluginInstanceUUID());
          requestSelectedID = null;
          refresh(pluginID);
          UI.getCurrent().removeWindow(view.getDeleteRequestWindow());
        }
        catch (final ApplicationServiceException e)
        {
          Notification.show(
              PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
              PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.PLUGINSMANAGEMENT_ERROR_DELETING_REQUESTS), Type.ERROR_MESSAGE);
        }

      }
    });
    view.getLinkInstanceComboBox().addValueChangeListener(new ValueChangeListener()
    {

      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {
        if (view.getLinkInstanceComboBox().getValue() != null)
        {
          view.getLinkInstanceButtonSubmit().setEnabled(true);
        }
        else
        {
          view.getLinkInstanceButtonSubmit().setEnabled(false);
        }

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
        initRequestsContainer();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public PortalMessages getPortalMessages()
      {
        return PluginsModule.getPortalMessages();
      }

    });
  }

  /**
   * Find a request in the requests container
   *
   * @param requestItem
   *          The {@link Item} requestItem that represent a ProjectApplicationRequest
   * @return
   */
  private ProjectApplicationRequest findRequestInContainer(final Item requestItem)
  {
    ProjectApplicationRequest result = null;
    for (final ProjectApplicationRequest request : requestsAvailable)
    {
      if ((request.getApplication().getName().equals(requestItem.getItemProperty(
          RequestsItemProperty.APPLICATION.getPropertyId()).getValue()))
          && (request.getCreated().equals(requestItem.getItemProperty(
              RequestsItemProperty.CREATEDDATE.getPropertyId()).getValue()))
          && (request.getLogin().equals(requestItem.getItemProperty(
              RequestsItemProperty.LOGIN.getPropertyId()).getValue()))
          && (request.getProject().getName().equals(requestItem.getItemProperty(
              RequestsItemProperty.PROJECT.getPropertyId()).getValue())))
      {
        result = request;
      }
    }
    return result;
  }

  /**
   * Refresh the view for the given plugin
   *
   * @param pPluginID
   *          the id of the requests' plugin
   */
  public void refresh(final String pPluginID)
  {
    pluginID = pPluginID;
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  /**
   * Get the plugin's requests and initialize the requests container
   */
  private void initRequestsContainer()
  {
    try
    {
      if (pluginID != null)
      {
        final UUID pluginUUID = UUID.fromString(pluginID);
        requestsAvailable = PluginsModule.getProjectApplicationRequestPresenter().getByPluginUUID(pluginUUID);
        view.detachRequestsTable();
        requestsContainer.setRequests(requestsAvailable);
        view.getRequestsTable().setPageLength(view.getRequestsTable().getItemIds().size() + 1);
        view.attachRequestsTable();
      }
    }
    catch (final ApplicationServiceException e)
    {
      Notification.show(
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_LOADING_REQUESTS), Type.ERROR_MESSAGE);
    }
  }

  /**
   * Initialize the requests' plugin decription
   */
  private void initRequestDesc()
  {
    try
    {
      final PluginService pluginService = PluginsModule.getPluginsManager().getPluginService(pluginID);
      final String pluginType = pluginService.getMetadata().getType();
      view.getPluginIcon().setSource(
          ResourceUtils.buildImageResource(pluginService.getPluginIcon(), pluginType));
      view.getPluginNameLabel().setValue(pluginType);
    }
    catch (final Exception e)
    {
      Notification.show(
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_LOADING_PLUGIN_DESC), Type.ERROR_MESSAGE);
    }
  }

  /**
   * Show the window to delete the request
   *
   * @param pRequestId
   *          the id of the request to delete
   */
  public void showDeleteRequestWindow(final String pRequestId)
  {
    requestSelectedID = pRequestId;
    view.getDeleteRequestWindow().setParameterMessage(pRequestId);
    UI.getCurrent().addWindow(view.getDeleteRequestWindow());
  }

  /**
   * Show the window to link the request with an instance
   *
   * @param pRequestId
   *     the request id to link
   */
  public void showLinkRequestWindow(final String pRequestId)
  {
    requestSelectedID = pRequestId;
    try
    {
      view.getLinkRequestNameLabel().setValue(pRequestId);
      final Set<ToolInstance> instances = PluginsModule.getPluginsManager().getPluginService(pluginID)
                                                       .getToolInstanceProvisioningService()
                                                       .getAvailableToolInstances();
      instancesContainer.setInstances(instances);
      view.getLinkInstanceComboBox().setContainerDataSource(instancesContainer);
      view.getLinkInstanceComboBox().setItemCaptionPropertyId(InstancesItemProperty.NAME.getPropertyId());
      UI.getCurrent().addWindow(view.getLinkInstanceWindow());
    }
    catch (final Exception e)
    {
      Notification.show(PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                     Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
                        PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                     Messages.PLUGINSMANAGEMENT_ERROR_LOADING_INSTANCES),
                        Type.ERROR_MESSAGE);
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
    initRequestDesc();
    initRequestsContainer();

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
