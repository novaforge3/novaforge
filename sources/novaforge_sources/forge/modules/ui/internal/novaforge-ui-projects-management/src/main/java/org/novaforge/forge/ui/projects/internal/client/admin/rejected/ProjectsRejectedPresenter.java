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
package org.novaforge.forge.ui.projects.internal.client.admin.rejected;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.portal.event.ProjectValidatedEvent;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.projects.internal.client.admin.components.ColumnDateGenerator;
import org.novaforge.forge.ui.projects.internal.client.admin.components.ColumnIconGenerator;
import org.novaforge.forge.ui.projects.internal.client.admin.components.ColumnRejectedActionsGenerator;
import org.novaforge.forge.ui.projects.internal.client.admin.components.ColumnUserGenerator;
import org.novaforge.forge.ui.projects.internal.client.admin.components.ProjectsStyleGenerator;
import org.novaforge.forge.ui.projects.internal.client.admin.containers.ProjectItemProperty;
import org.novaforge.forge.ui.projects.internal.client.admin.containers.ProjectsContainer;
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
public class ProjectsRejectedPresenter extends AbstractAdminPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long          serialVersionUID = 1114408567698194224L;
  /**
   * Logger component
   */
  private static final Log LOGGER = LogFactory.getLog(ProjectsRejectedPresenter.class);
  /**
   * Content of project view
   */
  private final ProjectsRejectedView view;
  /**
   * Container used to store projects
   */
  private       ProjectsContainer    projectsContainer;
  /**
   * Current projectid selected
   */
  private       String               projectItemId;

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
  public ProjectsRejectedPresenter(final ProjectsRejectedView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;
    initProjectsList();
    addListeners();
  }

  private void initProjectsList()
  {
    projectsContainer = new ProjectsContainer();
    view.getProjectsTable().setContainerDataSource(projectsContainer);

    view.getProjectsTable().addGeneratedColumn(ProjectItemProperty.ACTIONS.getPropertyId(),
                                               new ColumnRejectedActionsGenerator(this));
    view.getProjectsTable().addGeneratedColumn(ProjectItemProperty.DATE.getPropertyId(), new ColumnDateGenerator());
    view.getProjectsTable().addGeneratedColumn(ProjectItemProperty.ICON.getPropertyId(), new ColumnIconGenerator());
    view.getProjectsTable().addGeneratedColumn(ProjectItemProperty.AUTHOR.getPropertyId(),
                                               new ColumnUserGenerator(getEventBus()));
    view.getProjectsTable().setCellStyleGenerator(new ProjectsStyleGenerator());

    // Define visibles columns
    view.getProjectsTable().setVisibleColumns(ProjectItemProperty.ICON.getPropertyId(),
                                              ProjectItemProperty.ID.getPropertyId(),
                                              ProjectItemProperty.NAME.getPropertyId(),
                                              ProjectItemProperty.DESCRIPTION.getPropertyId(),
                                              ProjectItemProperty.AUTHOR.getPropertyId(),
                                              ProjectItemProperty.DATE.getPropertyId(),
                                              ProjectItemProperty.ACTIONS.getPropertyId());

    // Define special column width
    view.getProjectsTable().setColumnWidth(ProjectItemProperty.ICON.getPropertyId(), 36);
    view.getProjectsTable().setColumnExpandRatio(ProjectItemProperty.ID.getPropertyId(), 0.1f);
    view.getProjectsTable().setColumnExpandRatio(ProjectItemProperty.NAME.getPropertyId(), 0.2f);
    view.getProjectsTable().setColumnExpandRatio(ProjectItemProperty.DESCRIPTION.getPropertyId(), 0.4f);
    view.getProjectsTable().setColumnExpandRatio(ProjectItemProperty.AUTHOR.getPropertyId(), 0.1f);
    view.getProjectsTable().setColumnExpandRatio(ProjectItemProperty.DATE.getPropertyId(), 0.2f);
    view.getProjectsTable().setColumnWidth(ProjectItemProperty.ACTIONS.getPropertyId(), 70);
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
    view.getValidProjectCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 4540429602114544948L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.getProjectsTable().unselect(projectItemId);
        projectItemId = null;
        UI.getCurrent().removeWindow(view.getValidProjectWindow());
      }
    });
    view.getValidProjectConfirmButton().addClickListener(new Button.ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -6293445135484028321L;

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
            ProjectServices.getProjectPresenter().validateProject(projectItemId);
            getEventBus().publish(new ProjectValidatedEvent(projectItemId));
            UI.getCurrent().removeWindow(view.getValidProjectWindow());
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
      refreshLocalized(getCurrentLocale());
    }

  }

  /**
   * @param pItemId
   */
  public void onClickActionValid(final Object pItemId)
  {
    projectItemId = (String) pItemId;
    UI.getCurrent().addWindow(view.getValidProjectWindow());

  }

  /**
   * @param pItemId
   */
  public void onClickActionDelete(final Object pItemId)
  {
    projectItemId = (String) pItemId;
    UI.getCurrent().addWindow(view.getDeleteProjectWindow());

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
      final ProjectOptions projectOptions = ProjectServices.getProjectPresenter().newProjectOptions(false,
          true, false);
      final List<Project> projects = ProjectServices.getProjectPresenter().getAllProjectsByStatus(
          projectOptions, ProjectStatus.REJECTED);
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
