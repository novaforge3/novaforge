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
package org.novaforge.forge.ui.portal.internal.privatemodule.client;

import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import net.engio.mbassy.listener.Handler;
import org.apache.commons.lang.StringUtils;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.TabsheetWithState.SelectedTabChangeEvent;
import org.novaforge.forge.ui.portal.client.component.TabsheetWithState.SelectedTabChangeListener;
import org.novaforge.forge.ui.portal.client.component.TabsheetWithState.Tab;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.event.OpenExternalAppEvent;
import org.novaforge.forge.ui.portal.event.OpenInternalAppEvent;
import org.novaforge.forge.ui.portal.event.OpenProjectEvent;
import org.novaforge.forge.ui.portal.event.OpenProjectProfileEvent;
import org.novaforge.forge.ui.portal.event.OpenUserProfileEvent;
import org.novaforge.forge.ui.portal.event.ProjectUpdateEvent;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.model.ProjectTab;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.project.MainProjectTabPresenter;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.project.ProjectTabPresenter;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.project.ProjectTabViewImpl;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.project.components.SelectedTabPresenterChangedEvent;
import org.novaforge.forge.ui.portal.internal.privatemodule.module.PrivateModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public class WorkspacePresenter extends AbstractPortalPresenter implements Serializable, PortalComponent
{
  /**
    * 
    */
  private static final long             serialVersionUID = 2813418005883394916L;
  /**
   * Content the workspace view
   */
  private final WorkspaceView           view;
  /**
   * This map contains a reference to an item id associated to its tab element.
   */
  private final Map<String, ProjectTab> tabMapping;
  /**
   * The initial context
   */
  private final PortalContext           portalContext;

  /**
   * Default constructor.
   * 
   * @param pView
   *          the view associated
   * @param pPortalContext
   *          the initial context
   */
  public WorkspacePresenter(final WorkspaceView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    portalContext = pPortalContext;
    tabMapping = new HashMap<String, ProjectTab>();
    view = pView;
    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {

    view.getProjectTab()
        .setCloseHandler(new org.novaforge.forge.ui.portal.client.component.TabsheetWithState.CloseHandler()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 2146865793870871821L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void onTabClose(final org.novaforge.forge.ui.portal.client.component.TabsheetWithState pTabsheet,
                             final Component pTabContent)
      {
        final Set<Entry<String, ProjectTab>> entrySet = tabMapping.entrySet();
        for (final Entry<String, ProjectTab> entry : entrySet)
        {
          final String key = entry.getKey();
          final ProjectTab projectTab = entry.getValue();
          final ProjectTabPresenter projectTabPresenter = projectTab.getProjectTabPresenter();
          if ((projectTabPresenter != null) && (projectTabPresenter.getView().equals(pTabContent)))
          {
            projectTabPresenter.unregisterReferences();
            tabMapping.remove(key);
            pTabsheet.removeComponent(pTabContent);
            break;
          }
        }

      }
    });

    view.getProjectTab().addSelectedTabChangeListener(new SelectedTabChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 925716025668403267L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void selectedTabChange(final SelectedTabChangeEvent pEvent)
      {
        final Set<Entry<String, ProjectTab>> entrySet = tabMapping.entrySet();

        for (final Entry<String, ProjectTab> entry : entrySet)
        {
          final ProjectTab projectTab = entry.getValue();
          final Component selectedTab = pEvent.getTabSheet().getSelectedTab();
          if ((projectTab != null) && (projectTab.getTab() != null)
              && (selectedTab.equals(projectTab.getTab().getComponent())))
          {
            final ProjectTabPresenter projectTabPresenter = projectTab.getProjectTabPresenter();
            getEventBus().publish(new SelectedTabPresenterChangedEvent(projectTabPresenter));
            projectTabPresenter.refreshContent();
            break;
          }
        }

      }
    });
  }

  /**
   * Returns <code>true</code> if the current tab corresponds to the given {@link ProjectTabPresenter}
   * 
   * @param pPresenter
   *          the {@link ProjectTabPresenter} to check
   * @return <code>true</code> if the current tab corresponds to the given {@link ProjectTabPresenter}
   */
  public boolean isSelectedPresenter(final ProjectTabPresenter pPresenter)
  {
    boolean isSelected = false;
    final String projectId = pPresenter.getProjectId();
    if ((StringUtils.isNotBlank(projectId)) && (tabMapping.containsKey(projectId)))
    {
      final ProjectTab projectTab = tabMapping.get(projectId);
      final Component selectedTab = view.getProjectTab().getSelectedTab();
      if ((projectTab != null) && (projectTab.getTab() != null)
          && (selectedTab.equals(projectTab.getTab().getComponent())))
      {
        isSelected = true;
      }
    }
    return isSelected;
  }

  /**
   * This method will clear and unregister all references to this presenter and its children
   */
  @Override
  public void unregisterReferences()
  {
    super.unregisterReferences();
    unregisterProjectTabReferences();
  }

  private void unregisterProjectTabReferences()
  {
    final Set<Entry<String, ProjectTab>> entrySet = tabMapping.entrySet();

    for (final Entry<String, ProjectTab> entry : entrySet)
    {
      final ProjectTab projectTab = entry.getValue();
      final ProjectTabPresenter projectTabPresenter = projectTab.getProjectTabPresenter();
      if (projectTabPresenter != null)
      {
        projectTabPresenter.unregisterReferences();
      }
    }
    tabMapping.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return PrivateModule.getPortalModuleId();
  }

  /**
   * will refresh the tab sheet element
   */
  @Override
  public void init()
  {
    // Clean existing project
    unregisterProjectTabReferences();
    view.getProjectTab().removeAllComponents();

    // Create main project presenter
    final MainProjectTabPresenter mainProjectTabPresenter = new MainProjectTabPresenter(
        new ProjectTabViewImpl(), portalContext);
    final Tab addTab = view.getProjectTab().addTab(mainProjectTabPresenter.getView(),
        PrivateModule.getPortalMessages().getMessage(view.getLocale(), Messages.WORKSPACE_HOME),
        new ThemeResource(NovaForgeResources.ICON_HOME));
    mainProjectTabPresenter.refresh(true);

    // Store main project presenter
    final ProjectTab projectTab = new ProjectTab(mainProjectTabPresenter, addTab);
    tabMapping.put(mainProjectTabPresenter.getProjectId(), projectTab);
  }

  /**
   * Method call when a {@link OpenProjectEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Handler
  public void openProject(final OpenProjectEvent pEvent)
  {
    final String projectId = pEvent.getProjectId();
    if (tabMapping.containsKey(projectId))
    {
      final ProjectTab projectTab = tabMapping.get(projectId);
      view.getProjectTab().setSelectedTab(projectTab.getTab());
      projectTab.getProjectTabPresenter().refresh(projectId, false);
    }
    else
    {
      final ProjectTabPresenter projectPresenter = new ProjectTabPresenter(new ProjectTabViewImpl(),
          portalContext);
      final Tab addTab = view.getProjectTab().addTab(projectPresenter.getView());
      addTab.setClosable(true);
      view.getProjectTab().setSelectedTab(addTab);
      getEventBus().publish(new SelectedTabPresenterChangedEvent(projectPresenter));
      projectPresenter.refresh(projectId, true);
      addTab.setCaption(projectPresenter.getView().getProjectName().getCaption());
      addTab.setIcon(projectPresenter.getView().getProjectImage().getSource());

      final ProjectTab projectTab = new ProjectTab(projectPresenter, addTab);
      tabMapping.put(projectId, projectTab);
    }
  }

  /**
   * Method call when a {@link ProjectUpdateEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Handler
  public void onRefreshProject(final ProjectUpdateEvent pEvent)
  {
    final Project projectEvent = pEvent.getProject();
    if (tabMapping.containsKey(projectEvent.getElementId()))
    {
      final ProjectTab projectTab = tabMapping.get(projectEvent.getElementId());
      final Tab tab = projectTab.getTab();
      if (tab != null)
      {
        tab.setCaption(pEvent.getProject().getName());
        if ((projectEvent.getImage() != null) && (projectEvent.getImage().getFile() != null))
        {
          final StreamResource buildImageResource = ResourceUtils.buildImageResource(projectEvent.getImage()
              .getFile(), projectEvent.getName());
          buildImageResource.setMIMEType(projectEvent.getImage().getMimeType());
          tab.setIcon(buildImageResource);
        }
      }
    }
  }

  /**
   * Method call when a {@link OpenInternalAppEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Handler
  public void openApplication(final OpenInternalAppEvent pEvent)
  {
    view.getProjectTab().setSelectedTab(0);
  }

  /**
   * Method call when a {@link OpenExternalAppEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Handler
  public void openApplication(final OpenExternalAppEvent pEvent)
  {
    view.getProjectTab().setSelectedTab(0);

  }

  /**
   * Method call when a {@link OpenUserProfileEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Handler
  public void openUserProfileEvent(final OpenUserProfileEvent pEvent)
  {
    view.getProjectTab().setSelectedTab(0);

  }

  /**
   * Method call when a {@link OpenProjectProfileEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Handler
  public void openProjectProfileEvent(final OpenProjectProfileEvent pEvent)
  {
    view.getProjectTab().setSelectedTab(0);

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
      // Unregister project on which one user has no more authorization
      final List<String> unauthorizedProject = new ArrayList<String>();
      final Set<Entry<String, ProjectTab>> entrySet = tabMapping.entrySet();
      for (final Entry<String, ProjectTab> entry : entrySet)
      {
        final String projectId = entry.getKey();
        final boolean hasAccessAuthorizations = PrivateModule.getProjectPresenter().hasAccessAuthorizations(projectId);
        if (!hasAccessAuthorizations)
        {
          unauthorizedProject.add(projectId);
        }
      }

      // remove unauthorized project from mapping
      for (final String projectId : unauthorizedProject)
      {

        if (tabMapping.containsKey(projectId))
        {
          final ProjectTab projectTab = tabMapping.get(projectId);
          tabMapping.remove(projectId);
          if (projectTab.getTab() != null)
          {
            view.getProjectTab().removeTab(projectTab.getTab());
          }
          final ProjectTabPresenter projectTabPresenter = projectTab.getProjectTabPresenter();
          if (projectTabPresenter != null)
          {
            projectTabPresenter.unregisterReferences();
          }
        }
      }
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
    refreshLocalized(getCurrentLocale());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    if (view.getProjectTab().getComponentCount() > 0)
    {
      view.getProjectTab().getTab(0).setCaption(PrivateModule.getPortalMessages().getMessage(pLocale,
                                                                                             Messages.WORKSPACE_HOME));
    }

  }

}
