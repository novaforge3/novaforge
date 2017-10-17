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
package org.novaforge.forge.ui.publicproject.internal.client.list;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.event.OpenUserProfileEvent;
import org.novaforge.forge.ui.publicproject.internal.client.components.ProjectItemProperty;
import org.novaforge.forge.ui.publicproject.internal.client.components.ProjectsContainer;
import org.novaforge.forge.ui.publicproject.internal.module.PublicProjectModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class PublicProjectPresenter extends AbstractPortalPresenter
{
  private static final Log LOGGER = LogFactory.getLog(PublicProjectPresenter.class);
  /**
   * Content the workspace view
   */
  private final PublicProjectView view;
  private final ProjectsContainer projects = new ProjectsContainer();
  private String                  projectId;

  /**
   * Default constructor.
   * 
   * @param pView
   *          the view attached to this presenter
   * @param pPortalContext
   *          the initial context
   */
  public PublicProjectPresenter(final PublicProjectView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    // Define listeners
    addListeners();
    // Initialize projectList
    initProjectList();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getProjectsCombobox().addValueChangeListener(new ComboBox.ValueChangeListener()
    {
      /**
       *
       */
      private static final long serialVersionUID = -7733974728192377900L;

      @Override
      public void valueChange(final ValueChangeEvent event)
      {
        String selectedProjectId = null;
        if (view.getProjectsCombobox().getValue() != null)
        {
          selectedProjectId = view.getProjectsCombobox().getValue().toString();
        }
        view.setSelectedProject(selectedProjectId);
      }
    });
    view.getRequestJoiningButton().addClickListener(new ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = -5857867999271270574L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        // Update the window content
        view.showRequestWindowForProject();
      }
    });
    view.getSendJoiningRequestButton().addClickListener(new ClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 4844403879924480188L;

      @Override
      public void buttonClick(final ClickEvent event)
      {

        try
        {
          view.getJoiningRequestForm().validate();
          // close the window by removing it from the parent window
          UI.getCurrent().removeWindow(view.getJoiningRequestWindow());
          final String projectID = view.getSelectedProjectId();
          PublicProjectModule.getMembershipRequestPresenter().createRequest(projectID,
                                                                            view.getJoiningRequestTextArea().getValue(),
                                                                            true);
          TrayNotification.show(PublicProjectModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                                   Messages.PUBLIC_PROJECT_SEND_ACTION),
                                PublicProjectModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                                   Messages.PUBLIC_PROJECT_SEND_ACTION_SUCCESS),
                                TrayNotificationType.SUCCESS);
          refresh(projectID);

        }
        catch (final ProjectServiceException e)
        {
          final Notification errorNotif = new Notification(PublicProjectModule.getPortalMessages()
                                                                              .getMessage(getCurrentLocale(),
                                                                                          Messages.PUBLIC_PROJECT_SEND_ACTION),
                                                           PublicProjectModule.getPortalMessages()
                                                                              .getMessage(getCurrentLocale(),
                                                                                          Messages.PUBLIC_PROJECT_SEND_ACTION_FAILURE),
                                                           Type.ERROR_MESSAGE, true);
          errorNotif.setDelayMsec(2000);
          errorNotif.show(Page.getCurrent());
        }
        catch (final InvalidValueException e)
        {
          // Let the form manage exception
          TrayNotification.show(PublicProjectModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                                   Messages.FORM_INVALID_VALUE_TITLE),
                                PublicProjectModule.getPortalMessages().getMessage(getCurrentLocale(),
                                                                                   Messages.PUBLIC_PROJECT_REQUEST_MESSAGE_LENGTH),
                                TrayNotificationType.WARNING);
        }
      }
    });
    view.getFieldAuthorValue().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 1037185634196238514L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        getEventBus().publish(new OpenUserProfileEvent(view.getSelectedProjectAuthor()));
      }
    });

  }

  private void initProjectList()
  {
    view.getProjectsCombobox().setContainerDataSource(projects);
    view.getProjectsCombobox().setItemCaptionPropertyId(ProjectItemProperty.NAME.getPropertyId());
    view.getProjectsCombobox().setItemIconPropertyId(ProjectItemProperty.ICON.getPropertyId());
  }

  /**
   * Refresh the presenter and the view with the given project id
   *
   * @param pProjectId
   *          the project id to display
   */
  public void refresh(final String pProjectId)
  {
    if (!PublicProjectModule.getProjectPresenter().isForgeProject(pProjectId))
    {
      projectId = pProjectId;
      // hide the top panel with the projects combo
      view.hideHeader();
    }
    else
    {
      projectId = null;
    }
    // Initialize projectList
    refreshContent();
    // Refresh Label
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
    // Initialize project list
    try
    {
      final List<Project> projectList = PublicProjectModule.getPortalService().getPublicProjects();
      final List<MembershipRequest> requests = PublicProjectModule.getMembershipRequestPresenter()
                                                                  .getInProcessRequestsForUser(PublicProjectModule
                                                                                                   .getAuthentificationService()
                                                                                                   .getCurrentUser());
      final List<Project> projectsRequested = new ArrayList<Project>();
      for (final MembershipRequest request : requests)
      {
        projectsRequested.add(request.getProject());
      }
      projects.setProjects(projectList, projectsRequested, getCurrentLocale());
    }
    catch (final Exception e)
    {
      LOGGER.error(e.getLocalizedMessage(), e);
      final Notification errorNotif = new Notification(PublicProjectModule.getPortalMessages()
                                                                          .getMessage(getCurrentLocale(),
                                                                                      Messages.PUBLIC_PROJECT_ERROR_GETPUBLICPROJECT),
                                                       Type.ERROR_MESSAGE);
      errorNotif.setHtmlContentAllowed(true);
      errorNotif.setDelayMsec(2000);
      errorNotif.show(Page.getCurrent());
    }
    view.setSelectedProject(projectId);

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
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return PortalModuleId.PUBLIC_PROJECT;
  }

}
