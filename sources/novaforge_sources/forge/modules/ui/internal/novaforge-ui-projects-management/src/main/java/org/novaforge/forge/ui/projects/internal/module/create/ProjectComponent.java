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
package org.novaforge.forge.ui.projects.internal.module.create;

import com.vaadin.server.Page;
import com.vaadin.ui.UI;
import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.client.component.TrayNotification;
import org.novaforge.forge.ui.portal.client.component.TrayNotificationType;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalComponent;
import org.novaforge.forge.ui.portal.event.ProjectCreatedEvent;
import org.novaforge.forge.ui.projects.internal.client.manage.presenter.ProjectPresenter;
import org.novaforge.forge.ui.projects.internal.client.manage.presenter.ProjectView;
import org.novaforge.forge.ui.projects.internal.client.manage.view.ProjectViewImpl;
import org.novaforge.forge.ui.projects.internal.module.ProjectServices;

/**
 * @author Guillaume Lamirand
 */
public class ProjectComponent extends AbstractPortalComponent
{

  private ProjectPresenter presenter;

  /**
   * @param pPortalContext
   *          the initial context
   */
  public ProjectComponent(final PortalContext pPortalContext)
  {
    super(pPortalContext);
    final ProjectView view = new ProjectViewImpl(true);
    view.setStyleName(NovaForge.PROJECT_APPLICATION_CONTENT);
    presenter = new ProjectPresenter(view, getPortalContext())
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3351725647297135161L;

      /**
       * {@inheritDoc}
       */
      @Override
      protected PortalModuleId getModuleId()
      {
        return CreateProjectModule.getPortalModuleId();
      }
    };
    setContent(view);

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
    // Init main content
    final Project newProject = ProjectServices.getProjectPresenter().newProject();
    presenter.refresh(newProject);

    final TrayNotification notification = new TrayNotification(ProjectServices.getPortalMessages()
        .getMessage(UI.getCurrent().getLocale(), Messages.PROJECT_FORM_SUCCESS), TrayNotificationType.SUCCESS);
    notification.setDelayMsec(2000);
    notification.show(Page.getCurrent());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init()
  {
    // Init main content
    final Project newProject = ProjectServices.getProjectPresenter().newProject();
    presenter.refresh(newProject);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return CreateProjectModule.getPortalModuleId();
  }

}