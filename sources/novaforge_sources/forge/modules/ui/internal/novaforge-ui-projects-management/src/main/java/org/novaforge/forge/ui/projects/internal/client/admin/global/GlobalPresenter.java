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
package org.novaforge.forge.ui.projects.internal.client.admin.global;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.projects.internal.client.admin.events.ShowProjectRequestsViewEvent;
import org.novaforge.forge.ui.projects.internal.client.admin.events.ShowProjectsRejectedViewEvent;
import org.novaforge.forge.ui.projects.internal.client.admin.events.ShowProjectsValidatedViewEvent;
import org.novaforge.forge.ui.projects.internal.client.admin.rejected.ProjectsRejectedPresenter;
import org.novaforge.forge.ui.projects.internal.client.admin.rejected.ProjectsRejectedViewImpl;
import org.novaforge.forge.ui.projects.internal.client.admin.request.ProjectsRequestPresenter;
import org.novaforge.forge.ui.projects.internal.client.admin.request.ProjectsRequestViewImpl;
import org.novaforge.forge.ui.projects.internal.client.admin.validated.ProjectsValidatedPresenter;
import org.novaforge.forge.ui.projects.internal.client.admin.validated.ProjectsValidatedViewImpl;
import org.novaforge.forge.ui.projects.internal.module.admin.AbstractAdminPresenter;

import java.io.Serializable;
import java.util.Locale;

/**
 * This presenter handles main view.
 * 
 * @author Guillaume Lamirand
 */
public class GlobalPresenter extends AbstractAdminPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long                serialVersionUID = -5042299647493799344L;
  /**
   * Content of project view
   */
  private final GlobalView                 view;

  private final ProjectsValidatedPresenter projectsValidatedPresenter;
  private final ProjectsRejectedPresenter  projectsRejectedPresenter;
  private final ProjectsRequestPresenter   projectsRequestPresenter;

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
  public GlobalPresenter(final GlobalView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    view = pView;
    projectsValidatedPresenter = new ProjectsValidatedPresenter(new ProjectsValidatedViewImpl(),
        pPortalContext);
    projectsRejectedPresenter = new ProjectsRejectedPresenter(new ProjectsRejectedViewImpl(), pPortalContext);
    projectsRequestPresenter = new ProjectsRequestPresenter(new ProjectsRequestViewImpl(), pPortalContext);

    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getProjects().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -7394860492677119821L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(projectsValidatedPresenter.getComponent());
        projectsValidatedPresenter.refresh();
      }
    });
    view.getRequests().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 3758544191489241509L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(projectsRequestPresenter.getComponent());
        projectsRequestPresenter.refresh();
      }
    });
    view.getRejected().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 3758544191489241509L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        view.setSecondComponent(projectsRejectedPresenter.getComponent());
        projectsRejectedPresenter.refresh();
      }
    });
  }

  /**
   * Will refresh the projects list
   */
  public void refresh()
  {
    view.getProjects().addStyleName(NovaForge.SELECTED);
    view.setSecondComponent(projectsValidatedPresenter.getComponent());
    projectsValidatedPresenter.refresh();
  }

  /**
   * Callback on {@link ShowProjectRequestsViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void ShowProjectRequestsViewEvent(final ShowProjectRequestsViewEvent pEvent)
  {
    view.setSecondComponent(projectsRequestPresenter.getComponent());
    projectsRequestPresenter.refresh();
  }

  /**
   * Callback on {@link ShowProjectsRejectedViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onShowProjectsRejectedViewEvent(final ShowProjectsRejectedViewEvent pEvent)
  {
    view.setSecondComponent(projectsRejectedPresenter.getComponent());
    projectsRejectedPresenter.refresh();
  }

  /**
   * Callback on {@link ShowProjectsValidatedViewEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onShowProjectsValidatedViewEvent(final ShowProjectsValidatedViewEvent pEvent)
  {
    view.setSecondComponent(projectsValidatedPresenter.getComponent());
    projectsValidatedPresenter.refresh();
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
    // Nothing to do, let's all sub-presenter handle this event
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
