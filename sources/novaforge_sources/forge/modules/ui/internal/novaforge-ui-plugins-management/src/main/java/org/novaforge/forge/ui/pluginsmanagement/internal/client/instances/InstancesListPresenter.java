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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.instances;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectApplicationRequest;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.ToolInstanceProvisioningException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.event.OpenPluginsListEvent;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.event.OpenRequestsListEvent;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.InstanceColumnActionsGenerator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.InstanceColumnStatusGenerator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.InstanceFieldFactory;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.InstancesContainer;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.InstancesItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.ProjectInstanceItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.ProjectsColumnActionsGenerator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.ProjectsColumnIconGenerator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.ProjectsColumnUserGenerator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.ProjectsInstanceContainer;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.requests.components.RequestsContainer;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.requests.components.RequestsItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.AbstractPluginsPresenter;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.event.OpenPluginViewEvent;
import org.novaforge.forge.ui.portal.event.OpenProjectEvent;
import org.novaforge.forge.ui.portal.event.OpenUserProfileEvent;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * @author Jeremy Casery
 */
public class InstancesListPresenter extends AbstractPluginsPresenter implements Serializable
{
  /**
   * Contains the array of fields to display in the form
   */
  public static final  String[] INSTANCE_FIELDS  = new String[] { InstanceFieldFactory.NAME_FIELD,
                                                                  InstanceFieldFactory.DESCRIPTION_FIELD,
                                                                  InstanceFieldFactory.ALIAS_FIELD,
                                                                  InstanceFieldFactory.BASEURL_FIELD,
                                                                  InstanceFieldFactory.SHAREABLE_FIELD };
  /**
   *
   */
  private static final long     serialVersionUID = 7915932027546681916L;
  private static final Log      LOGGER           = LogFactory.getLog(InstancesListPresenter.class);
  private static final String   JENKINS_TYPE     = "jenkins";
  private static final String   SONAR_TYPE       = "sonar";
  /**
   * Content the workspace view
   */
  private final InstancesListView view;
  private final InstancesContainer instancesContainer = new InstancesContainer();
  private final RequestsContainer  requestsContainer  = new RequestsContainer();
  private String                          pluginID;
  private List<ProjectApplicationRequest> requestsAvailable;
  private ToolInstance                    instanceSelected;
  private ProjectsInstanceContainer       projectsContainer;
  private String                          projectIdSelected;
  private ProjectApplication              projectApplicationSelected;

  /**
   * Default constructor
   *
   * @param pView
   *          the view for this presenter
   * @param pPortalContext
   *          the initial context
   */
  public InstancesListPresenter(final InstancesListView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Init table
    initInstancesList();
    initProjectsList();
    // Add listeners
    addListeners();
  }

  /**
   * Initialize the list of instances
   */
  private void initInstancesList()
  {
    view.getInstancesTable().setContainerDataSource(instancesContainer);

    view.getInstancesTable().addGeneratedColumn(InstancesItemProperty.ACTIONS.getPropertyId(),
                                                new InstanceColumnActionsGenerator(this));
    view.getInstancesTable().addGeneratedColumn(InstancesItemProperty.STATUS.getPropertyId(),
                                                new InstanceColumnStatusGenerator());

    view.getInstancesTable().setVisibleColumns(new String[] { InstancesItemProperty.NAME.getPropertyId(),
                                                              InstancesItemProperty.DESCRIPTION.getPropertyId(),
                                                              InstancesItemProperty.ALIAS.getPropertyId(),
                                                              InstancesItemProperty.BASEURL.getPropertyId(),
                                                              InstancesItemProperty.STATUS.getPropertyId(),
                                                              InstancesItemProperty.ACTIONS.getPropertyId() });

    // Define columns width
    view.getInstancesTable().setColumnExpandRatio(InstancesItemProperty.NAME.getPropertyId(), 0.20F);
    view.getInstancesTable().setColumnExpandRatio(InstancesItemProperty.DESCRIPTION.getPropertyId(), 0.35F);
    view.getInstancesTable().setColumnExpandRatio(InstancesItemProperty.ALIAS.getPropertyId(), 0.20F);
    view.getInstancesTable().setColumnExpandRatio(InstancesItemProperty.BASEURL.getPropertyId(), 0.25F);
    view.getInstancesTable().setColumnWidth(InstancesItemProperty.STATUS.getPropertyId(), 45);
    view.getInstancesTable().setColumnWidth(InstancesItemProperty.ACTIONS.getPropertyId(), 140);
  }

  private void initProjectsList()
  {
    projectsContainer = new ProjectsInstanceContainer();
    view.getProjectsTable().setContainerDataSource(projectsContainer);

    view.getProjectsTable().addGeneratedColumn(ProjectInstanceItemProperty.ACTIONS.getPropertyId(),
                                               new ProjectsColumnActionsGenerator(this));
    view.getProjectsTable().addGeneratedColumn(ProjectInstanceItemProperty.ICON.getPropertyId(),
                                               new ProjectsColumnIconGenerator());
    view.getProjectsTable().addGeneratedColumn(ProjectInstanceItemProperty.AUTHOR.getPropertyId(),
                                               new ProjectsColumnUserGenerator(this));

    // Define visibles columns
    view.getProjectsTable().setVisibleColumns(ProjectInstanceItemProperty.ICON.getPropertyId(),
                                              ProjectInstanceItemProperty.ID.getPropertyId(),
                                              ProjectInstanceItemProperty.NAME.getPropertyId(),
                                              ProjectInstanceItemProperty.AUTHOR.getPropertyId(),
                                              ProjectInstanceItemProperty.APPLICATION_NAME.getPropertyId(),
                                              ProjectInstanceItemProperty.ACTIONS.getPropertyId());

    // Define special column width
    view.getProjectsTable().setColumnWidth(ProjectInstanceItemProperty.ICON.getPropertyId(), 36);
    view.getProjectsTable().setColumnExpandRatio(ProjectInstanceItemProperty.ID.getPropertyId(), 0.1f);
    view.getProjectsTable().setColumnExpandRatio(ProjectInstanceItemProperty.NAME.getPropertyId(), 0.2f);
    view.getProjectsTable().setColumnExpandRatio(ProjectInstanceItemProperty.AUTHOR.getPropertyId(), 0.2f);
    view.getProjectsTable().setColumnExpandRatio(ProjectInstanceItemProperty.APPLICATION_NAME.getPropertyId(), 0.2f);
    view.getProjectsTable().setColumnWidth(ProjectInstanceItemProperty.ACTIONS.getPropertyId(), 80);

  }

  /**
   * Add the listeners to the view
   */
  private void addListeners()
  {
    view.getReturnToPluginsList().addClickListener(new ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 8842598399536644208L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        getEventBus().publish(new OpenPluginsListEvent());

      }
    });
    view.getGoToRequestsListButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 8842598399536644208L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        getEventBus().publish(new OpenRequestsListEvent(pluginID));

      }
    });
    view.getCreateInstanceButton().addListener(ClickEvent.class, this, "showCreateInstanceWindow");
    view.getCreateInstanceButtonSubmit().addListener(ClickEvent.class, this, "createNewPluginInstance");
    view.getEditInstanceButtonSubmit().addListener(ClickEvent.class, this, "editPluginInstance");
    view.getInstancesTable().addActionHandler(new RefreshAction()
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
        initInstancesContainer();
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
    view.getInstanceDeleteWindow().getYesButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 2582229722555592956L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        try
        {
          if (instanceSelected != null)
          {
            PluginsModule.getPluginsManager().getPluginService(pluginID).getToolInstanceProvisioningService()
                .removeToolInstance(instanceSelected);
            UI.getCurrent().removeWindow(view.getInstanceDeleteWindow());
            instanceSelected = null;
            refresh(pluginID);
          }
          else
          {
            Notification.show(
                PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
                PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.PLUGINSMANAGEMENT_ERROR_DELETING_INSTANCE), Type.ERROR_MESSAGE);
          }
        }
        catch (final ToolInstanceProvisioningException e)
        {
          instanceSelected = null;
          Notification.show(
              PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
              PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.PLUGINSMANAGEMENT_ERROR_DELETING_INSTANCE), Type.ERROR_MESSAGE);
        }
        catch (final PluginManagerException e)
        {
          instanceSelected = null;
          Notification.show(
              PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
              PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.PLUGINSMANAGEMENT_ERROR_DELETING_INSTANCE), Type.ERROR_MESSAGE);
        }

      }
    });
    view.getProjectsTable().addActionHandler(new RefreshAction()
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
        try
        {
          refreshProjectsInstance(instanceSelected.getUUID());
        }
        catch (final Exception e)
        {
          ExceptionCodeHandler.showNotificationError(PluginsModule.getPortalMessages(), e, getCurrentLocale());
        }
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
    view.getProjectDeleteWindow().getYesButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 231033814538696497L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        try
        {
          PluginsModule.getApplicationPresenter().removeApplication(projectIdSelected,
              projectApplicationSelected.getUri());
          view.getProjectsTable().unselect(projectApplicationSelected.getPluginInstanceUUID().toString());
          final UUID instanceUUID = instanceSelected.getUUID();
          final int projectsInstanceAdded = refreshProjectsInstance(instanceUUID);
          if (projectsInstanceAdded == 0)
          {
            view.showProjectsWindow(false);
            initInstancesContainer();
            view.getInstancesTable().unselect(instanceUUID);
          }

        }
        catch (final Exception e)
        {
          ExceptionCodeHandler.showNotificationError(PluginsModule.getPortalMessages(), e, getCurrentLocale());
        }
        finally
        {
          projectIdSelected = null;
          projectApplicationSelected = null;
        }

      }
    });
  }

  /**
   * Get the plugin's instances and initialize the instance container
   */
  private void initInstancesContainer()
  {
    // Initialize instances list
    try
    {
      final Set<ToolInstance> toolInstances = PluginsModule.getPluginsManager().getPluginService(pluginID)
                                                           .getToolInstanceProvisioningService().getAllToolInstances();
      view.detachInstancesTable();
      instancesContainer.setInstances(toolInstances);
      view.getInstancesTable().setPageLength(view.getInstancesTable().getItemIds().size() + 1);
      view.attachInstancesTable();
    }
    catch (final Exception e)
    {
      LOGGER.error("An exception occurred during getting all instances.", e);
      Notification.show(PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                     Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
                        PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                     Messages.PLUGINSMANAGEMENT_ERROR_LOADING_INSTANCES),
                        Type.ERROR_MESSAGE);
    }
  }

  /**
   * Refresh the plugin's instances view
   *
   * @param pPluginID
   *     the id of the plugin
   */
  public void refresh(final String pPluginID)
  {
    pluginID = pPluginID;
    refreshContent();
    refreshLocalized(getCurrentLocale());
  }

  private int refreshProjectsInstance(final UUID pInstanceID)
      throws ToolInstanceProvisioningException, PluginManagerException, ProjectServiceException,
                 ApplicationServiceException, UserServiceException
  {
    int projectAppsAdded = 0;

    // Delete previous content
    projectsContainer.clean();

    // Get InstanceConfiguration for this instance
    final Set<InstanceConfiguration> instanceConfigurations = PluginsModule.getPluginsManager()
                                                                           .getPluginService(pluginID)
                                                                           .getToolInstanceProvisioningService()
                                                                           .getApplicationsByUUID(pInstanceID);

    for (final InstanceConfiguration instanceConfiguration : instanceConfigurations)
    {
      final String projectId = instanceConfiguration.getForgeProjectId();
      final Project project = PluginsModule.getProjectPresenter().getProject(projectId, true);
      final ProjectApplication application = PluginsModule.getApplicationPresenter().getApplication(projectId,
                                                                                                    UUID.fromString(instanceConfiguration
                                                                                                                        .getInstanceId()));

      final boolean added = projectsContainer.addProjectInstance(project, application);
      if (added)
      {
        projectAppsAdded++;
      }
    }
    return projectAppsAdded;
  }

  /**
   * Initialize the instance's plugin description
   */
  private void initInstanceDesc()
  {
    String pluginType;
    try
    {
      pluginType = PluginsModule.getPluginsManager().getPluginService(pluginID).getMetadata().getType();
      view.getPluginIcon().setSource(ResourceUtils.buildImageResource(PluginsModule.getPluginsManager()
                                                                                   .getPluginService(pluginID)
                                                                                   .getPluginIcon(), pluginType));
      view.getPluginNameLabel().setValue(pluginType);
    }
    catch (final Exception e)
    {
      Notification.show(PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                     Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
                        PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                     Messages.PLUGINSMANAGEMENT_ERROR_LOADING_PLUGIN_DESC),
                        Type.ERROR_MESSAGE);
    }
  }

  /**
   * Create a new plugin instance
   *
   * @param pEvent
   *          the {@link ClickEvent} source
   */
  public void createNewPluginInstance(final ClickEvent pEvent)
  {
    try
    {
      view.getCreateInstanceForm().commit();
      instanceSelected = PluginsModule.getPluginsManager().getPluginService(pluginID)
          .getToolInstanceProvisioningService().addToolInstance(instanceSelected);
      if (view.getRequestsComboBox().getValue() == null)
      {
        if (!PluginsModule.getProjectApplicationRequestPresenter().getByPluginUUID(UUID.fromString(pluginID))
            .isEmpty())
        {
          PluginsModule.getProjectApplicationRequestPresenter().handleOldestRequest(
              instanceSelected.getUUID(), UUID.fromString(pluginID));
        }
      }
      else
      {
        final Item requestItem = requestsContainer.getItem(view.getRequestsComboBox().getValue());
        final ProjectApplicationRequest request = findRequestInContainer(requestItem);
        PluginsModule.getProjectApplicationRequestPresenter().handleRequest(request,
            instanceSelected.getUUID());
      }
      refresh(pluginID);
      view.showCreateInstanceWindow(false);
      instanceSelected = null;
      requestsAvailable = null;
      view.getRequestsComboBox().setValue(null);
    }
    catch (final ToolInstanceProvisioningException e)
    {
      instanceSelected = null;
      Notification.show(
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_CREATING_INSTANCE), Type.ERROR_MESSAGE);
    }
    catch (final Exception e)
    {
      // Let's the form manage the exception
    }
  }

  /**
   * Find an application request in the container
   *
   * @param requestItem
   *          the {@link Item} that represents the ProjectApplicationRequest wanted
   * @return {@link ProjectApplicationRequest} the application request
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
   * Edit the previously selected instance
   *
   * @param pEvent
   *          the {@link ClickEvent} source
   */
  public void editPluginInstance(final ClickEvent pEvent)
  {
    try
    {
      if (instanceSelected != null)
      {
        final String previousName = instanceSelected.getName();
        view.getEditInstanceForm().commit();
        PluginsModule.getPluginsManager().getPluginService(pluginID).getToolInstanceProvisioningService()
            .updateToolInstance(instanceSelected, previousName);
        refresh(pluginID);
        view.showEditInstanceWindow(false);
        instanceSelected = null;
      }
    }
    catch (final ToolInstanceProvisioningException e)
    {
      instanceSelected = null;
      Notification.show(
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_UPDATING_INSTANCE), Type.ERROR_MESSAGE);
    }
    catch (final Exception e)
    {
      // Let's the form manage the exception
    }
  }

  /**
   * Show the create instance window
   *
   * @param pEvent
   *          the {@link ClickEvent} source
   */
  public void showCreateInstanceWindow(final ClickEvent pEvent)
  {
    try
    {
      // Init new instance
      final PluginService pluginService = PluginsModule.getPluginsManager().getPluginService(pluginID);
      instanceSelected = pluginService.getToolInstanceProvisioningService().newToolInstance();
      final BeanItem<ToolInstance> instanceItem = new BeanItem<ToolInstance>(instanceSelected);
      view.getCreateInstanceForm().setItemDataSource(instanceItem);
      view.getCreateInstanceForm().setVisibleItemProperties(INSTANCE_FIELDS);
      // Init request combobox
      final UUID pluginUUID = UUID.fromString(pluginID);
      requestsAvailable = PluginsModule.getProjectApplicationRequestPresenter().getByPluginUUID(pluginUUID);
      requestsContainer.setRequests(requestsAvailable);
      view.getRequestsComboBox().setContainerDataSource(requestsContainer);
      view.getRequestsComboBox().setItemCaptionPropertyId(RequestsItemProperty.PROJECT.getPropertyId());
      // Show popup
      view.showCreateInstanceWindow(true);
      // Notify user if it's a PIC plugin
      if (SONAR_TYPE.equalsIgnoreCase(pluginService.getMetadata().getType()))
      {
        Notification.show(
            PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                Messages.PLUGINSMANAGEMENT_WARNING_TITLE),
            PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                Messages.PLUGINSMANAGEMENT_WARNING_PIC_REMEMBER, JENKINS_TYPE), Type.WARNING_MESSAGE);
      }
      else if (JENKINS_TYPE.equalsIgnoreCase(pluginService.getMetadata().getType()))
      {
        Notification.show(
            PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                Messages.PLUGINSMANAGEMENT_WARNING_TITLE),
            PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                Messages.PLUGINSMANAGEMENT_WARNING_PIC_REMEMBER, SONAR_TYPE), Type.WARNING_MESSAGE);
      }
    }
    catch (final Exception e)
    {
      Notification.show(
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_CREATING_INSTANCE), Type.ERROR_MESSAGE);
    }
  }

  /**
   * Open the administration view for the plugin's instance
   *
   * @param pInstanceId
   *          the id of the instance to administrate
   */
  public void adminInstanceClicked(final UUID pInstanceId)
  {
    if (pInstanceId != null)
    {
      getEventBus().publish(
          new OpenPluginViewEvent(PluginViewEnum.ADMINISTRATION.name(), pluginID, pInstanceId.toString()));
    }

  }

  /**
   * Call when the edit instance button is clicked
   *
   * @param pInstanceID
   *          the id of the tool instance to edit
   */
  public void editInstanceClicked(final UUID pInstanceID)
  {
    long instanceAppsNumber = 0;
    try
    {
      instanceSelected = PluginsModule.getPluginsManager().getPluginService(pluginID)
          .getToolInstanceProvisioningService().getToolInstanceByUUID(pInstanceID);
      instanceAppsNumber = PluginsModule.getPluginsManager().getPluginService(pluginID)
          .getToolInstanceProvisioningService().countApplications(instanceSelected.getUUID());

    }
    catch (final Exception e)
    {
      Notification.show(
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_EDITING_INSTANCE), Type.ERROR_MESSAGE);
    }
    final BeanItem<ToolInstance> instanceItem = new BeanItem<ToolInstance>(instanceSelected);
    view.getEditInstanceForm().setItemDataSource(instanceItem);
    view.getEditInstanceForm().setVisibleItemProperties(INSTANCE_FIELDS);
    if ((instanceAppsNumber > 1) && instanceSelected.isShareable())
    {
      final Field<?> field = view.getEditInstanceForm().getField(InstanceFieldFactory.SHAREABLE_FIELD);
      field.setEnabled(false);
      if (field instanceof AbstractField)
      {
        ((AbstractField<?>) field).setDescription(PluginsModule.getPortalMessages().getMessage(
            getCurrentLocale(), Messages.PLUGINSMANAGEMENT_FIELD_SHAREABLE_UNAVAILABLE));
      }
    }
    view.showEditInstanceWindow(true);
  }

  /**
   * Call when the project list button is clicked
   *
   * @param pInstanceID
   *          the id of the tool instance to edit
   */
  public void showProjectsClicked(final UUID pInstanceID)
  {
    try
    {
      // Init selected instance
      instanceSelected = PluginsModule.getPluginsManager().getPluginService(pluginID)
          .getToolInstanceProvisioningService().getToolInstanceByUUID(pInstanceID);

      final int projectsInstanceAdded = refreshProjectsInstance(instanceSelected.getUUID());

      if (projectsInstanceAdded > 0)
      {
        // Display window
        view.showProjectsWindow(true);
      }
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(PluginsModule.getPortalMessages(), e, getCurrentLocale());
    }
  }

  /**
   * Show the delete instance window
   *
   * @param pInstanceID
   *          the id of the instance to delete
   */
  public void showDeleteInstanceWindow(final UUID pInstanceID)
  {
    try
    {
      instanceSelected = PluginsModule.getPluginsManager().getPluginService(pluginID)
          .getToolInstanceProvisioningService().getToolInstanceByUUID(pInstanceID);
      if (instanceSelected != null)
      {
        view.getInstanceDeleteWindow().setParameterMessage(instanceSelected.getName());
        UI.getCurrent().addWindow(view.getInstanceDeleteWindow());
      }
    }
    catch (final Exception e)
    {
      Notification.show(
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
          PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
              Messages.PLUGINSMANAGEMENT_ERROR_DELETING_INSTANCE), Type.ERROR_MESSAGE);
    }

  }

  /**
   * Get the plugin id of this instances
   *
   * @return the id of the plugin
   */
  public final String getPluginID()
  {
    return pluginID;
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
    initInstanceDesc();
    initInstancesContainer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

  /**
   * @param pItemId
   */
  public void onClickProjectInstanceOpen(final String pProjectId)
  {
    // Hide window
    view.showProjectsWindow(false);
    getEventBus().publish(new OpenProjectEvent(pProjectId));

  }

  /**
   * @param pUserProfile
   */
  public void showAuthorProfile(final UserProfile pUserProfile)
  {
    // Hide window
    view.showProjectsWindow(false);
    getEventBus().publish(new OpenUserProfileEvent(pUserProfile));

  }

  /**
   * @param pProjectId
   * @param pProjectApplication
   */
  public void onClickProjectInstanceDelete(final String pProjectId,
      final ProjectApplication pProjectApplication)
  {
    projectIdSelected = pProjectId;
    projectApplicationSelected = pProjectApplication;
    view.getProjectDeleteWindow().setParameterMessage(pProjectApplication.getName());
    UI.getCurrent().addWindow(view.getProjectDeleteWindow());

  }

}
