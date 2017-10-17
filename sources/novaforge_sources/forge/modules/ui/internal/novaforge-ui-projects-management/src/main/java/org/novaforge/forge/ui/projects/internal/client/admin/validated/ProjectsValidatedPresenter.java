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
package org.novaforge.forge.ui.projects.internal.client.admin.validated;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import net.engio.mbassy.listener.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.portal.event.OpenProjectEvent;
import org.novaforge.forge.ui.portal.event.ProjectCreatedEvent;
import org.novaforge.forge.ui.portal.event.ProjectValidatedEvent;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.projects.internal.client.admin.components.ColumnDateGenerator;
import org.novaforge.forge.ui.projects.internal.client.admin.components.ColumnIconGenerator;
import org.novaforge.forge.ui.projects.internal.client.admin.components.ColumnUserGenerator;
import org.novaforge.forge.ui.projects.internal.client.admin.components.ColumnValidatedActionsGenerator;
import org.novaforge.forge.ui.projects.internal.client.admin.components.ProjectsStyleGenerator;
import org.novaforge.forge.ui.projects.internal.client.admin.containers.ProjectItemProperty;
import org.novaforge.forge.ui.projects.internal.client.admin.containers.ProjectsContainer;
import org.novaforge.forge.ui.projects.internal.client.manage.view.ProjectViewImpl;
import org.novaforge.forge.ui.projects.internal.module.ProjectServices;
import org.novaforge.forge.ui.projects.internal.module.admin.AbstractAdminPresenter;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * This presenter handles main view.
 * 
 * @author Guillaume Lamirand
 */
public class ProjectsValidatedPresenter extends AbstractAdminPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long                                                                       serialVersionUID = 1114408567698194224L;
  private static final Log LOGGER = LogFactory.getLog(ProjectsValidatedPresenter.class);
  /**
   * Content of project view
   */
  private final ProjectsValidatedView                                                             view;
  private final org.novaforge.forge.ui.projects.internal.client.manage.presenter.ProjectPresenter projectPresenter;
  private ProjectsContainer                                                                       projectsContainer;
  private String                                                                                  projectItemId;

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
  public ProjectsValidatedPresenter(final ProjectsValidatedView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;

    projectPresenter = new org.novaforge.forge.ui.projects.internal.client.manage.presenter.ProjectPresenter(
        new ProjectViewImpl(false), pPortalContext);
    initProjectsList();
    addListeners();
  }

  private void initProjectsList()
  {
    projectsContainer = new ProjectsContainer();
    view.getProjectsTable().setContainerDataSource(projectsContainer);

    view.getProjectsTable().addGeneratedColumn(ProjectItemProperty.ACTIONS.getPropertyId(),
        new ColumnValidatedActionsGenerator(this));
    view.getProjectsTable().addGeneratedColumn(ProjectItemProperty.DATE.getPropertyId(),
        new ColumnDateGenerator());
    view.getProjectsTable().addGeneratedColumn(ProjectItemProperty.ICON.getPropertyId(),
        new ColumnIconGenerator());
    view.getProjectsTable().addGeneratedColumn(ProjectItemProperty.AUTHOR.getPropertyId(),
        new ColumnUserGenerator(getEventBus()));
    view.getProjectsTable().setCellStyleGenerator(new ProjectsStyleGenerator());

    // Define visibles columns
    view.getProjectsTable().setVisibleColumns(ProjectItemProperty.ICON.getPropertyId(),
        ProjectItemProperty.ID.getPropertyId(), ProjectItemProperty.NAME.getPropertyId(),
        ProjectItemProperty.DESCRIPTION.getPropertyId(), ProjectItemProperty.AUTHOR.getPropertyId(),
        ProjectItemProperty.DATE.getPropertyId(), ProjectItemProperty.ACTIONS.getPropertyId());

    // Define special column width
    view.getProjectsTable().setColumnWidth(ProjectItemProperty.ICON.getPropertyId(), 36);
    view.getProjectsTable().setColumnExpandRatio(ProjectItemProperty.ID.getPropertyId(), 0.1f);
    view.getProjectsTable().setColumnExpandRatio(ProjectItemProperty.NAME.getPropertyId(), 0.2f);
    view.getProjectsTable().setColumnExpandRatio(ProjectItemProperty.DESCRIPTION.getPropertyId(), 0.4f);
    view.getProjectsTable().setColumnExpandRatio(ProjectItemProperty.AUTHOR.getPropertyId(), 0.1f);
    view.getProjectsTable().setColumnExpandRatio(ProjectItemProperty.DATE.getPropertyId(), 0.2f);
    view.getProjectsTable().setColumnWidth(ProjectItemProperty.ACTIONS.getPropertyId(), 105);

    view.getExcelExporter().setContainerToBeExported(projectsContainer);
    view.getExcelExporter().setVisibleColumns(
        new String[] { ProjectItemProperty.ID.getPropertyId(), ProjectItemProperty.NAME.getPropertyId(),
            ProjectItemProperty.DESCRIPTION.getPropertyId(),
            ProjectItemProperty.AUTHOR_LOGIN.getPropertyId(), ProjectItemProperty.DATE.getPropertyId() });
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
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
        refresh();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public PortalMessages getPortalMessages()
      {
        return ProjectServices.getPortalMessages();
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
        projectsContainer.removeAllContainerFilters();
        if ((event.getText() != null) && !event.getText().isEmpty())
        {
          // Set new filter for the status column
          final Filter usersFilter = new Or(new SimpleStringFilter(ProjectItemProperty.ID.getPropertyId(),
              event.getText(), true, false), new SimpleStringFilter(ProjectItemProperty.NAME.getPropertyId(),
              event.getText(), true, false), new SimpleStringFilter(ProjectItemProperty.DESCRIPTION
              .getPropertyId(), event.getText(), true, false), new SimpleStringFilter(
              ProjectItemProperty.AUTHOR.getPropertyId(), event.getText(), true, false));
          projectsContainer.addContainerFilter(usersFilter);
        }
      }
    });
    view.getDeleteProjectWindow().getNoButton().addClickListener(new ClickListener()
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
        view.getProjectsTable().unselect(projectItemId);
        projectItemId = null;
      }
    });
    view.getDeleteProjectWindow().getYesButton().addClickListener(new Button.ClickListener()
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
        if (projectItemId != null)
        {
          try
          {
            ProjectServices.getProjectPresenter().deleteProject(projectItemId);
            UI.getCurrent().removeWindow(view.getDeleteProjectWindow());
            refresh();
          }
          catch (final Exception e)
          {
            ExceptionCodeHandler.showNotificationError(ProjectServices.getPortalMessages(), e,
                view.getLocale());
          }

        }

      }
    });
    projectPresenter.getView().getApplyButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 3037189906918339903L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        UI.getCurrent().removeWindow(view.getEditProjectWindow());
        refresh();
      }
    });

  }

  /**
   * Will refresh the project information.
   */
  public void refresh()
  {
    if (view.getParent() != null)
    {
      view.getFilterTextField().setValue("");
      projectsContainer.removeAllContainerFilters();
      refreshContent();
      // Has to be set when Exporter is attached to UI
      view.getExcelExporter().setDownloadFileName("project-validated-export");
      refreshLocalized(getCurrentLocale());
    }
  }

  /**
   * Called on open button in actions column
   *
   * @param pItemId
   *          project id selected
   */
  public void onClickActionOpen(final Object pItemId)
  {
    projectItemId = (String) pItemId;
    getEventBus().publish(new OpenProjectEvent(projectItemId));

  }

  /**
   * Called on delete button in actions column
   *
   * @param pItemId
   *          project id selected
   */
  public void onClickActionEdit(final Object pItemId)
  {
    projectItemId = (String) pItemId;
    Project project;
    try
    {
      project = ProjectServices.getProjectPresenter().getProject(projectItemId, true);
      UI.getCurrent().addWindow(view.getEditProjectWindow());

      view.getEditProjectWindow().setContent(projectPresenter.getComponent());
      projectPresenter.refresh(project);

    }
    catch (final ProjectServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ProjectServices.getPortalMessages(), e, view.getLocale());

    }

  }

  /**
   * Called on delete button in actions column
   *
   * @param pItemId
   *          project id selected
   */
  public void onClickActionDelete(final Object pItemId)
  {
    projectItemId = (String) pItemId;
    UI.getCurrent().addWindow(view.getDeleteProjectWindow());

  }

  /**
   * Method call when a {@link ProjectValidatedEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Handler
  public void onProjectValidatedEvent(final ProjectValidatedEvent pEvent)
  {
    refresh();
  }

  /**
   * Method call when a {@link ProjectCreatedEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Handler
  public void onCreateProject(final ProjectCreatedEvent pEvent)
  {
    refresh();
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
    if (projectItemId != null)
    {
      if (view.getProjectsTable().isSelected(projectItemId))
      {
        view.getProjectsTable().unselect(projectItemId);
      }
      projectItemId = null;
    }
    try
    {
      final ProjectOptions projectOptions = ProjectServices.getProjectPresenter().newProjectOptions(false, true, false);
      final List<Project> projects = ProjectServices.getProjectPresenter().getAllProjectsByStatus(projectOptions,
                                                                                                  ProjectStatus.VALIDATED,
                                                                                                  ProjectStatus.DELETE_IN_PROGRESS,
                                                                                                  ProjectStatus.DELETE_ON_ERROR);

      projectsContainer.setProjects(projects);
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(ProjectServices.getPortalMessages(), e, view.getLocale());
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
