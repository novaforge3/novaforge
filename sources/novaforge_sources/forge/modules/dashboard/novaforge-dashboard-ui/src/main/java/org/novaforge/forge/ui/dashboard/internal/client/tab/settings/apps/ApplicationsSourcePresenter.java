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
package org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps;

import com.google.common.base.Strings;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Component;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.dashboard.model.DataSourceOptions;
import org.novaforge.forge.dashboard.model.Widget;
import org.novaforge.forge.dashboard.model.WidgetModule;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.dashboard.internal.client.AbstractDashBoardPresenter;
import org.novaforge.forge.ui.dashboard.internal.client.container.ApplicationContainer;
import org.novaforge.forge.ui.dashboard.internal.client.container.ApplicationItemProperty;
import org.novaforge.forge.ui.dashboard.internal.client.container.ProjectItemProperty;
import org.novaforge.forge.ui.dashboard.internal.client.container.ProjectsContainer;
import org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps.components.ApplicationSelectedItemGenerator;
import org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps.components.ProjectForm;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.vaadin.addon.itemlayout.event.ItemClickEvent;
import org.vaadin.addon.itemlayout.event.ItemClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class ApplicationsSourcePresenter extends AbstractDashBoardPresenter implements Serializable
{
  /**
   * Serial version id
   */
  private static final long            serialVersionUID     = -3067819403919493755L;
  private final ApplicationsSourceView view;
  private final List<ProjectForm>      projectsForm         = new ArrayList<ProjectForm>();
  private final ProjectOptions         projectOptions;
  private final ProjectsContainer      projectsContainer    = new ProjectsContainer();
  private final ApplicationContainer   applicationContainer = new ApplicationContainer();
  private UUID widgetUUID;
  private DataSourceOptions            dataSourceOptions;

  /**
   * Default constructor
   * 
   * @param pPortalContext
   *          the portalContext used to initialize this module
   * @param pView
   *          the view associated to this presenter
   */
  public ApplicationsSourcePresenter(final ApplicationsSourceView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);

    // Init the view
    view = pView;
    projectOptions = DashboardModule.getProjectPresenter().newProjectOptions(false, true, true);
    // Define listeners
    addListener();
    // Initialize projectList
    initProjectList();
    initSelectedList();
  }

  private void addListener()
  {
    view.getNameFilter().addTextChangeListener(new TextChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3656821850941869636L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void textChange(final TextChangeEvent event)
      {
        for (final ProjectForm projectForm : projectsForm)
        {
          final Filterable filterableContainer = projectForm.getContainer();

          filterableContainer.removeAllContainerFilters();
          if (!Strings.isNullOrEmpty(event.getText()))
          {
            // Set new filter for the status column
            filterableContainer.addContainerFilter(new SimpleStringFilter(ApplicationItemProperty.NAME
                .getPropertyId(), event.getText(), true, false));
          }

          final boolean hasVisible = projectForm.hasVisibleApplication();
          projectForm.setVisible(hasVisible);
        }
      }
    });
    view.getProjectsBox().addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 3362371022905633480L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void valueChange(final ValueChangeEvent event)
      {
        final String projectId = (String) view.getProjectsBox().getValue();

        for (final ProjectForm projectForm : projectsForm)
        {
          if (Strings.isNullOrEmpty(projectId))
          {
            final boolean hasVisible = projectForm.hasVisibleApplication();
            projectForm.setVisible(hasVisible);
          }
          else
          {
            final boolean isSelected = projectId.equals(projectForm.getProjectId());
            final boolean hasVisible = projectForm.hasVisibleApplication();
            projectForm.setVisible((isSelected && hasVisible));
          }
        }
      }
    });
  }

  private void initProjectList()
  {
    view.getProjectsBox().setContainerDataSource(projectsContainer);
    view.getProjectsBox().setItemCaptionPropertyId(ProjectItemProperty.NAME.getPropertyId());
    view.getProjectsBox().setItemIconPropertyId(ProjectItemProperty.ICON.getPropertyId());
  }

  /**
   *
   */
  private void initSelectedList()
  {
    view.getSourcesSelected().setContainerDataSource(applicationContainer);
    view.getSourcesSelected().setItemGenerator(new ApplicationSelectedItemGenerator(this));
  }

  /**
   * Refresh the associated view according the parameter
   * 
   * @param pWidgetUUID
   *          the widget uuid
   */
  public void refresh(final UUID pWidgetUUID)
  {
    try
    {
      widgetUUID = pWidgetUUID;
      projectsForm.clear();
      applicationContainer.removeAllItems();
      view.getContentLayout().removeAllComponents();
      final Widget widget = DashboardModule.getDashBoardService().getWidget(widgetUUID);
      final WidgetModule widgetModule = DashboardModule.getWidgetModuleService().getModule(widget.getKey());
      dataSourceOptions = widgetModule.getDataSourceOptions();
      final String[] availablePlugins = getAvailablePlugins(widgetModule.getCategories(), widget.getKey());
      if (availablePlugins.length > 0)
      {
        if (DashBoard.Type.USER.equals(getType()))
        {
          final List<Project> availableProjects = handleProjectsAndApplications(availablePlugins);
          projectsContainer.setProjects(availableProjects);
        }
        else if (DashBoard.Type.PROJECT.equals(getType()))
        {
          final ProjectOptions newProjectOptions = DashboardModule.getProjectPresenter().newProjectOptions(
              false, true, true);
          final Project project = DashboardModule.getProjectPresenter().getProject(getTypeId(),
              newProjectOptions);
          handleApplicationsForProject(project, availablePlugins);

        }
        view.setDashBoardType(getType());
        handleDataSource(widget.getDataSource());
      }

    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(DashboardModule.getPortalMessages(), e, getCurrentLocale());
    }
    refreshLocalized(getCurrentLocale());
  }

  private List<Project> handleProjectsAndApplications(final String[] availablePlugins)
      throws ProjectServiceException, ApplicationServiceException
  {
    final List<Project> availableProjects = new ArrayList<Project>();
    final List<Project> userProjects = DashboardModule.getProjectPresenter().getValidatedProjects(
        projectOptions);
    for (final Project project : userProjects)
    {
      final boolean availableApps = handleApplicationsForProject(project, availablePlugins);
      if (availableApps)
      {
        availableProjects.add(project);
      }
    }
    return availableProjects;
  }

  private boolean handleApplicationsForProject(final Project pProject, final String[] availablePlugins)
      throws ProjectServiceException, ApplicationServiceException
  {
    boolean availableApps = false;
    final List<ProjectApplication> applications = DashboardModule.getApplicationPresenter()
        .getAllProjectApplications(pProject.getProjectId(), availablePlugins);
    if ((applications != null) && (!applications.isEmpty()))
    {
      final ProjectForm projectForm = new ProjectForm();
      projectForm.setMultiSelectable(dataSourceOptions.isMultiApplications());
      projectForm.setData(pProject, applications);
      projectForm.addItemClickListener(new ItemClickListener()
      {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onItemClick(final ItemClickEvent pEvent)
        {
          handleItemClick(projectForm, pEvent);
        }

      });
      projectsForm.add(projectForm);
      view.getContentLayout().addComponent(projectForm);
      availableApps = true;
    }
    return availableApps;
  }

  private void handleDataSource(final String pDataSource)
  {
    if (!Strings.isNullOrEmpty(pDataSource))
    {
      final Map<String, List<String>> readDataSource = DashboardModule.getDataSourceFactory().readDataSource(
          pDataSource);
      final Set<Entry<String, List<String>>> entrySet = readDataSource.entrySet();
      for (final Entry<String, List<String>> entry : entrySet)
      {
        for (final ProjectForm otherProjectForm : projectsForm)
        {
          final String projectId = otherProjectForm.getProjectId();
          if (projectId.equals(entry.getKey()))
          {
            for (final String appId : entry.getValue())
            {
              final UUID applicationUUID = UUID.fromString(appId);
              final Item applicationItem = otherProjectForm.getApplicationItem(applicationUUID);
              applicationContainer.addApplication(projectId, applicationItem);
              otherProjectForm.selectApplication(applicationUUID, true);
            }
            break;
          }
        }
      }
    }
  }

  private String[] getAvailablePlugins(final List<String> categories, final String pWidgetKey)
      throws PluginManagerException
  {
    final List<String> availablePlugins = new ArrayList<String>();
    final List<PluginMetadata> pluginsMetadata = DashboardModule.getPluginsManager()
        .getPluginsMetadataByCategory(categories.toArray(new String[categories.size()]));

    for (final PluginMetadata pluginMetadata : pluginsMetadata)
    {
      if (pluginMetadata.isAvailable())
      {
          availablePlugins.add(pluginMetadata.getUUID());
      }
    }
    return availablePlugins.toArray(new String[availablePlugins.size()]);
  }

  private void handleItemClick(final ProjectForm projectForm, final ItemClickEvent pEvent)
  {
    final String currentProjectId = projectForm.getProjectId();
    final UUID itemId = UUID.fromString(pEvent.getItemId());
    if (pEvent.isSelected())
    {
      if (!dataSourceOptions.isMultiProject())
      {
        for (final ProjectForm otherProjectForm : projectsForm)
        {
          final String projectId = otherProjectForm.getProjectId();
          if ((!projectId.equals(currentProjectId))
              && (applicationContainer.containsApplication(projectId)))
          {
            final List<UUID> applications = applicationContainer.getApplications(otherProjectForm
                .getProjectId());
            for (final UUID appId : applications)
            {
              applicationContainer.removeApplication(appId);
              otherProjectForm.selectApplication(appId, false);
            }
          }
        }
      }
      applicationContainer.addApplication(currentProjectId, projectForm.getApplicationItem(itemId));
    }
    else
    {
      applicationContainer.removeApplication(UUID.fromString(pEvent.getItemId()));
    }
    savaDataSource();
  }

  public void removeSelectedApplication(final UUID pApplicationId)
  {
    final String projectID = (String) applicationContainer.getContainerProperty(pApplicationId,
        ApplicationItemProperty.PROJECT_ID.getPropertyId()).getValue();
    for (final ProjectForm otherProjectForm : projectsForm)
    {
      if (otherProjectForm.getProjectId().equals(projectID))
      {
        otherProjectForm.selectApplication(pApplicationId, false);
        applicationContainer.removeApplication(pApplicationId);
      }
    }
    savaDataSource();
  }

  private void savaDataSource()
  {
    final Map<String, List<String>> dataSource = new HashMap<String, List<String>>();
    for (final Object itemId : applicationContainer.getItemIds())
    {
      final String projectId = (String) applicationContainer.getContainerProperty(itemId,
          ApplicationItemProperty.PROJECT_ID.getPropertyId()).getValue();

      final List<String> appIds = new ArrayList<String>();
      if (dataSource.containsKey(projectId))
      {
        appIds.addAll(dataSource.get(projectId));
      }
      appIds.add(itemId.toString());
      dataSource.put(projectId, appIds);
    }
    final Widget widget = DashboardModule.getDashBoardService().getWidget(widgetUUID);
    widget.setDataSource(DashboardModule.getDataSourceFactory().buildDataSource(dataSource));
    DashboardModule.getDashBoardService().updateWidget(widget);
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
  public void refreshContent()
  {
    // Do not handle refresh
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

}
