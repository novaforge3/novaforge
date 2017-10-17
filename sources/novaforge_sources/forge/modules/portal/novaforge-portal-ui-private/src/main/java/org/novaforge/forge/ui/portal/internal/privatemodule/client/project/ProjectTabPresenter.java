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
package org.novaforge.forge.ui.portal.internal.privatemodule.client.project;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.AbstractSplitPanel.SplitterClickEvent;
import com.vaadin.ui.AbstractSplitPanel.SplitterClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;
import net.engio.mbassy.listener.Handler;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModule;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.portal.models.PortalSpace;
import org.novaforge.forge.portal.models.PortalURI;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.portal.services.PortalService;
import org.novaforge.forge.ui.portal.client.component.ApplicationBrowserFrame;
import org.novaforge.forge.ui.portal.client.component.TabsheetWithState.Tab;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;
import org.novaforge.forge.ui.portal.event.ModuleRefreshRequestedEvent;
import org.novaforge.forge.ui.portal.event.PortalModuleRegisteredEvent;
import org.novaforge.forge.ui.portal.event.PortalModuleUnRegisteredEvent;
import org.novaforge.forge.ui.portal.event.PortalPresenterClosedEvent;
import org.novaforge.forge.ui.portal.event.ProjectSpacesUpdateEvent;
import org.novaforge.forge.ui.portal.event.ProjectUpdateEvent;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.model.ApplicationTab;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.project.components.NavigationDescriptionGenerator;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.project.components.NavigationStyleGenerator;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.project.components.NavigationTreeContainer;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.project.components.SelectedTabPresenterChangedEvent;
import org.novaforge.forge.ui.portal.internal.privatemodule.client.project.components.TreeItemProperty;
import org.novaforge.forge.ui.portal.internal.privatemodule.module.PrivateModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This presenter handles project components displayed.
 * 
 * @author Guillaume Lamirand
 */
public class ProjectTabPresenter extends AbstractPortalPresenter implements Serializable
{
  /**
   * Serial version uid used for serialization
   */
  private static final long                 serialVersionUID = -5042299647493799344L;
  /**
   * Logger
   */
  private static final Log LOG = LogFactory.getLog(ProjectTabPresenter.class);
  /**
   * Content of project view
   */
  private final ProjectTabView              view;
  /**
   * This map contains a reference to an item id associated to its tab element.
   */
  private final Map<String, ApplicationTab> tabMapping;
  /**
   * Data source which manage tree items
   */
  private       NavigationTreeContainer     dataSource;
  /**
   * This variable contains the project Id associated to this presenter
   */
  private       String                      projectId;
  /**
   * This is updated when user selected a new {@link ProjectTabPresenter}
   */
  private       boolean                     isSelected;

  /**
   * Default constructor. It will initialize the tree component associated to the view and bind some events.
   *
   * @param pView
   *          the view
   * @param pPortalContext
   *          the initial context
   */
  public ProjectTabPresenter(final ProjectTabView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    tabMapping = new HashMap<String, ApplicationTab>();
    view = pView;

    initTree();
    addListeners();
  }

  /**
   * Initialize {@link Tree} component
   *
   * @see ProjectTabView#getProjectTree()
   */
  private void initTree()
  {
    dataSource = new NavigationTreeContainer();

    view.getProjectTree().setContainerDataSource(dataSource);
    view.getProjectTree().setItemDescriptionGenerator(new NavigationDescriptionGenerator());
    view.getProjectTree().setItemStyleGenerator(new NavigationStyleGenerator());
    view.getProjectTree().setItemIconPropertyId(TreeItemProperty.ICON.getPropertyId());
    view.getProjectTree().setItemCaptionPropertyId(TreeItemProperty.CAPTION.getPropertyId());
    view.getProjectTree().setItemCaptionMode(ItemCaptionMode.PROPERTY);

  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getSplitPanel().addSplitterClickListener(new SplitterClickListener()
    {
      /**
       * Serial version uid used for serialization
       */
      private static final long serialVersionUID = 1628899947061727176L;

      @Override
      public void splitterClick(final SplitterClickEvent pEvent)
      {
        if (pEvent.isDoubleClick())
        {
          if (view.getSplitPanel().getSplitPosition() < 5)
          {
            view.resetSplitPanel();
          }
          else
          {
            view.getSplitPanel().setSplitPosition(0, Unit.PERCENTAGE);
          }
        }
      }
    });
    view.getProjectTree().addActionHandler(new RefreshAction()
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
          updateProjectNavigation();
        }
        catch (final PortalException e)
        {
          LOG.error("Unable to refresh project navigation", e);
          ExceptionCodeHandler.showNotificationError(PrivateModule.getPortalMessages(), e, getCurrentLocale());
        }
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public PortalMessages getPortalMessages()
      {
        return PrivateModule.getPortalMessages();
      }

    });
    view.getProjectTree().addItemClickListener(new ItemClickListener()
    {

      /**
       * Serial version uid used for serialization
       */
      private static final long serialVersionUID = -102871453962303678L;

      @Override
      public void itemClick(final ItemClickEvent pEvent)
      {
        if ( pEvent.getButton().equals( MouseButton.LEFT ) )
        {
          // Get item property for Application type
          final Property<?> urlProperty = pEvent.getItem().getItemProperty(TreeItemProperty.URL.getPropertyId());
          final Property<?> internalProperty = pEvent.getItem().getItemProperty(TreeItemProperty.INTERNAL
                                                                                    .getPropertyId());
          final Property<?> availableProperty = pEvent.getItem().getItemProperty(TreeItemProperty.AVAILABILITY
                                                                                     .getPropertyId());
          final Property<?> statusProperty = pEvent.getItem().getItemProperty(TreeItemProperty.STATUS.getPropertyId());
          final Property<?> uuidProperty   = pEvent.getItem().getItemProperty(TreeItemProperty.UUID.getPropertyId());
          final String caption = pEvent.getItem().getItemProperty(TreeItemProperty.CAPTION.getPropertyId()).getValue()
                                       .toString();
          final Resource icon = (Resource) pEvent.getItem().getItemProperty(TreeItemProperty.ICON.getPropertyId())
              .getValue();

          // Check if an url has been set
          if (((urlProperty != null) && (urlProperty.getValue() != null) && (!"".equals(urlProperty.getValue())))
                  && ((availableProperty != null) && (availableProperty.getValue() != null)
                          && ((Boolean) availableProperty.getValue())) && ((statusProperty.getValue() == null)
                                                                                       || ((statusProperty.getValue()
                                                                                                != null)
                                                                                               && (statusProperty
                                                                                                       .getValue() instanceof ApplicationStatus)
                                                                                               && (!ApplicationStatus.CREATE_ON_ERROR
                                                                                                        .equals(statusProperty
                                                                                                                    .getValue()))
                                                                                               && (!ApplicationStatus.DELETE_ON_ERROR
                                                                                                        .equals(statusProperty
                                                                                                                    .getValue()))
                                                                                               && (!ApplicationStatus.DELETE_IN_PROGRESS
                                                                                                        .equals(statusProperty
                                                                                                                    .getValue())))))
          {
            // Add the item as embedded tab
            addEmbeddedTab(uuidProperty.getValue().toString(), caption, icon, urlProperty.getValue().toString());
          }
          // If no url has been set, check if it's an internal application
          else if (((internalProperty != null) && (internalProperty.getValue() != null) && ((Boolean) internalProperty
                                                                                                          .getValue()))
                       && ((availableProperty.getValue() != null) && ((Boolean) availableProperty.getValue())))
          {
            // Add the item as internal tab
            final Property<?> idProperty = pEvent.getItem().getItemProperty(
                TreeItemProperty.MODULE_ID.getPropertyId());
            addInternalTab(idProperty.getValue().toString(), uuidProperty.getValue().toString(), caption, icon,
                true);
          }
          else if (statusProperty.getValue() == null)
          {
            view.getProjectTree().expandItem(pEvent.getItemId());
          }
        }
      }
    });
    
  
      view.getApplicationTab()
          .setCloseHandler(new org.novaforge.forge.ui.portal.client.component.TabsheetWithState.CloseHandler()
      {
  
        /**
         * Serial version id
         */
        private static final long serialVersionUID = 4685350421472573467L;
  
        @Override
        public void onTabClose(final org.novaforge.forge.ui.portal.client.component.TabsheetWithState pTabsheet,
                               final Component pTabContent)
        {
          final Set<Entry<String, ApplicationTab>> entrySet = tabMapping.entrySet();
  
          for (final Entry<String, ApplicationTab> entry : entrySet)
          {
            final String key = entry.getKey();
            final ApplicationTab portalTab = entry.getValue();
            if ((portalTab.getTab() != null) && (portalTab.getTab().getComponent().equals(pTabContent)))
            {
              if (portalTab.isInternal())
              {
                getEventBus().publishAsync(new PortalPresenterClosedEvent(portalTab.getIdentifier()));
              }
              pTabsheet.removeComponent(pTabContent);
              tabMapping.remove(key);
              break;
            }
          }
        }
      });

  }

  /**
   * Allow to update the view according to the project given
   *
   * @throws PortalException
   *           thrown if navigation cannot be retrieve
   */
  protected void updateProjectNavigation() throws PortalException
  {

    // Backup expanded item
    final List<Object>  expandedItems = new ArrayList<Object>();
    final Collection<?> itemIds       = view.getProjectTree().getItemIds();
    for (final Object object : itemIds)
    {
      if ((view.getProjectTree().areChildrenAllowed(object)) && (view.getProjectTree().isExpanded(object)))
      {
        expandedItems.add(object);
      }
    }
    // Update datasource
    final List<PortalSpace> spaces = getProjectNavigation();
    dataSource.setSpaces(spaces);

    // Expanded back the item
    for (final Object expandedItem : expandedItems)
    {
      if (view.getProjectTree().containsId(expandedItem))
      {
        view.getProjectTree().expandItem(expandedItem);
      }
    }
  }

  /**
   * Will add a new embedded tab to the project tabsheet, by default it will be closable
   *
   * @param pId
   *          tabulation id
   * @param pCaption
   *          tabulation caption
   * @param pIcon
   *          tabulation icon
   * @param pUrl
   *          embedded url used to display the tabulation content
   */
  protected void addEmbeddedTab(final String pId, final String pCaption, final Resource pIcon, final String pUrl)
  {
    addEmbeddedTab(pId, pCaption, pIcon, pUrl, true);
  }

  /**
   * Will add a new internal tab to the project tabsheet
   *
   * @param pModuleId
   *          tabulation id
   * @param pId
   *          technical id
   * @param pCaption
   *          tabulation caption
   * @param pIcon
   *          tabulation icon
   * @param pIsClosable
   *          define if the tab should be closable
   */
  protected void addInternalTab(final String pModuleId, final String pId, final String pCaption,
      final Resource pIcon, final boolean pIsClosable)
  {
    final PortalModule module = PrivateModule.getPortalModuleService().getModule(pModuleId);
    final Map<PortalContext.KEY, String> attributes = new HashMap<PortalContext.KEY, String>();
    attributes.put(PortalContext.KEY.PROJECTID, projectId);
    attributes.put(PortalContext.KEY.APP_ID, pId);
    final PortalContext buildContext = PrivateModule.getPortalModuleService().buildContext(getEventBus(),
        getCurrentLocale(), attributes);
    if (module != null)
    {
      final PortalComponent createComponent = module.createComponent(buildContext);
      final Component component = createComponent.getComponent();
      ApplicationTab portalTab;
      if (tabMapping.containsKey(pId))
      {
        portalTab = tabMapping.get(pId);
        portalTab.setIdentifier(buildContext.getUuid());
        portalTab.setPortalComponent(createComponent);
      }
      else
      {
        portalTab = new ApplicationTab(buildContext.getUuid(), pModuleId, createComponent);
        tabMapping.put(pId, portalTab);
      }
      final boolean addTab = addTab(pId,
          PrivateModule.getPortalMessages().getMessage(view.getLocale(), pCaption), pIcon, component,
          pIsClosable);
      if (addTab)
      {
        createComponent.init();
      }
    }

  }

  /**
   * Retrieve the project navigation details
   *
   * @return List<PortalSpace> for the current project
   * @throws PortalException
   */
  protected List<PortalSpace> getProjectNavigation() throws PortalException
  {
    return PrivateModule.getPortalService().getProjectNavigation(projectId, getCurrentLocale());
  }

  /**
   * Will add a new embedded tab to the project tabsheet
   *
   * @param pId
   *          tabulation id
   * @param pCaption
   *          tabulation caption
   * @param pIcon
   *          tabulation icon
   * @param pUrl
   *          embedded url used to display the tabulation content
   * @param pIsClosable
   *          define if the tab is closable or not
   */
  protected void addEmbeddedTab(final String pId, final String pCaption, final Resource pIcon,
      final String pUrl, final boolean pIsClosable)
  {
    if (!tabMapping.containsKey(pId))
    {
      ApplicationTab portalTab = new ApplicationTab();
      tabMapping.put(pId, portalTab);
    }
    final ApplicationBrowserFrame e = new ApplicationBrowserFrame(new ExternalResource(pUrl));
    addTab(pId, pCaption, pIcon, e, pIsClosable);
  }

  /**
   * Will add a new tab to the project tabsheet
   *
   * @param pId
   *          tabulation id
   * @param pCaption
   *          tabulation caption
   * @param pIcon
   *          tabulation icon
   * @param pContent
   *          tabulation content
   * @param pIsClosable
   *          true for closable tab, false otherwise
   */
  protected boolean addTab(final String pId, final String pCaption, final Resource pIcon,
      final Component pContent, final boolean pIsClosable)
  {
    boolean added = false;
    if (tabMapping.containsKey(pId))
    {
      final ApplicationTab portalTab = tabMapping.get(pId);
      if (portalTab.getTab() != null)
      {
        view.getApplicationTab().setSelectedTab(portalTab.getTab());
      }
      else
      {
        final Tab addTab = view.getApplicationTab().addTab(pContent, pCaption, pIcon);
        addTab.setClosable(pIsClosable);
        portalTab.setTab(addTab);
        view.getApplicationTab().setSelectedTab(addTab);
        added = true;
      }
    }
    return added;
  }

  /**
   * Method call when a {@link ProjectSpacesUpdateEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Handler
  public void onSelectTabPresenterEvent(final SelectedTabPresenterChangedEvent pEvent)
  {
    final ProjectTabPresenter projectTabPresenterSelected = pEvent.getProjectTabPresenter();
    isSelected = equals(projectTabPresenterSelected);
  }

  /**
   * @return the projectId
   */
  public String getProjectId()
  {
    return projectId;
  }

  /**
   * @param pProjectId
   *     the projectId to set
   */
  protected void setProjectId(final String pProjectId)
  {
    projectId = pProjectId;
  }

  /**
   * Method call when a {@link ProjectSpacesUpdateEvent} is received
   *
   * @param pEvent
   *     the source event
   */
  @Handler
  public void onProjectSpacesUpdateEvent(final ProjectSpacesUpdateEvent pEvent)
  {
    final String projectIdEvent = pEvent.getProjectId();
    if ((projectId != null) && (projectId.equals(projectIdEvent)))
    {
      try
      {
        updateProjectNavigation();
      }
      catch (final PortalException e)
      {
        // Do not display any notification
        LOG.error(String.format("Unable to update project tree with [project_id=%s]", projectIdEvent), e);

      }
    }
  }

  /**
   * Method call when a {@link ProjectUpdateEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Handler
  public void onProjectUpdateEvent(final ProjectUpdateEvent pEvent)
  {
    final String projectIdEvent = pEvent.getProject().getElementId();
    if ((projectId != null) && (projectId.equals(projectIdEvent)))
    {
      updateProjectInformation(pEvent.getProject());
    }
  }

  /**
   * Allow to update the view according to the project given
   *
   * @param pProject
   */
  protected void updateProjectInformation(final Project pProject)
  {
    view.getProjectName().setCaption(pProject.getName());
    view.getProjectName().setWidth(pProject.getName().length(), Unit.EX);
    view.getProjectDescription().setCaption(pProject.getDescription());
    if ((pProject.getImage() != null) && (pProject.getImage().getFile() != null))
    {
      final StreamResource buildImageResource = ResourceUtils.buildImageResource(pProject.getImage().getFile(),
                                                                                 pProject.getName());
      buildImageResource.setMIMEType(pProject.getImage().getMimeType());
      view.getProjectImage().setSource(buildImageResource);
    }

  }

  /**
   * Method call when a {@link PortalModuleRegisteredEvent} is received
   *
   * @param pEvent
   *     the source event
   */
  @Handler
  public void onPortalModuleRegisteredEvent(final PortalModuleRegisteredEvent pEvent)
  {
    updatePortalModuleOnEvent(pEvent.getPortalModuleId());
  }

  private void updatePortalModuleOnEvent(final String pModuleId)
  {
    if (pModuleId != null)
    {
      final Set<Entry<String, ApplicationTab>> entrySet = tabMapping.entrySet();
      for (final Entry<String, ApplicationTab> entry : entrySet)
      {
        final ApplicationTab portalTab = entry.getValue();
        if (pModuleId.equals(portalTab.getModuleId()))
        {
          final Tab tab = portalTab.getTab();
          final PortalModule module = PrivateModule.getPortalModuleService().getModule(pModuleId);
          final Map<PortalContext.KEY, String> attributes = new HashMap<PortalContext.KEY, String>();
          attributes.put(PortalContext.KEY.PROJECTID, projectId);
          final PortalContext buildContext = PrivateModule.getPortalModuleService().buildContext(getEventBus(),
                                                                                                 view.getLocale(),
                                                                                                 attributes);
          if (module != null)
          {
            final PortalComponent createComponent = module.createComponent(buildContext);
            final Component component = createComponent.getComponent();
            view.getApplicationTab().replaceComponent(tab.getComponent(), component);
            getEventBus().publishAsync(new ModuleRefreshRequestedEvent(module.getId(), createComponent));
            portalTab.setTab(view.getApplicationTab().getTab(component));
            portalTab.setIdentifier(buildContext.getUuid());
            portalTab.setPortalComponent(createComponent);
          }
          break;
        }
      }
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
  public void refreshContent()
  {

    final boolean hasAccessAuthorizations = PrivateModule.getProjectPresenter().hasAccessAuthorizations(projectId);
    if ((hasAccessAuthorizations) && (isSelected))
    {
      refresh(projectId, false);
    }
    else if (!hasAccessAuthorizations)
    {
      unregisterReferences();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    view.getProjectTree().setCaption(PrivateModule.getPortalMessages().getMessage(pLocale,
                                                                                  Messages.PROJECT_NAVIGATION));
    refresh(projectId, false);

    for (final Entry<String, ApplicationTab> entry : tabMapping.entrySet())
    {
      final Item app = view.getProjectTree().getItem(entry.getKey());
      if (app != null)
      {
        final ApplicationTab portalTab = entry.getValue();
        portalTab.getTab().setCaption(app.getItemProperty(TreeItemProperty.CAPTION.getPropertyId()).getValue()
                                         .toString());
      }
    }

  }

  /**
   * Will refresh the project information. If project id is null or empty, so the main project will be used
   *
   * @param pProjectId
   *          the project id
   * @param pOpenDefault
   *          true to open default tab
   */
  public void refresh(final String pProjectId, final boolean pOpenDefault)
  {
    try
    {
      setProjectId(pProjectId);
      updateProjectNavigation();

      // Update details
      final PortalService portalFacade = PrivateModule.getPortalService();
      final Project project = portalFacade.getProject(pProjectId);
      updateProjectInformation(project);

      // open default application
      openDefaultApplication(pProjectId, pOpenDefault);

    }
    catch (final Exception e)
    {
      LOG.error(String.format("Unable to update project information with [project_id=%s]", pProjectId), e);
      ExceptionCodeHandler.showNotificationError(PrivateModule.getPortalMessages(), e, getCurrentLocale());
    }
  }

  /**
   * This method will clear and unregister all references to this presenter and its children
   */
  @Override
  public void unregisterReferences()
  {
    super.unregisterReferences();
    final Set<Entry<String, ApplicationTab>> entrySet = tabMapping.entrySet();

    for (final Entry<String, ApplicationTab> entry : entrySet)
    {
      final ApplicationTab portalTab = entry.getValue();
      if (portalTab.isInternal())
      {
        getEventBus().publishAsync(new PortalPresenterClosedEvent(portalTab.getIdentifier()));
      }
    }
    tabMapping.clear();
  }

  /**
   * Show the default application found
   *
   * @throws PortalException
   */
  protected void openDefaultApplication(final String pProjectId, final boolean pOpenDefault)
      throws PortalException
  {
    final PortalApplication defaultApplication = getDefaultApplication(pProjectId);
    if ((pOpenDefault) && (defaultApplication != null))
    {
      addApplicationTab(defaultApplication, "", false);
    }

  }

  protected PortalApplication getDefaultApplication(final String pProjectId) throws PortalException
  {
    return PrivateModule.getPortalService().getProjectDefaultApplication(pProjectId, getCurrentLocale());
  }

  /**
   * Will add a new application to a new tab to the project tabsheet
   *
   * @param pApplication
   *          application to add
   * @param pCaption
   *          tabulation caption
   * @param pIsClosable
   *          define if the tab should be closable
   */
  protected void addApplicationTab(final PortalApplication pApplication, final String pCaption,
                                   final boolean pIsClosable)
  {
    if (pApplication != null)
    {
      final PortalURI portalURI = pApplication.getPortalURI();
      final Resource iconResource = pApplication.getIcon();
      if (portalURI.isInternalModule())
      {
        addInternalTab(portalURI.getModuleId(), pApplication.getUniqueId().toString(), pCaption, iconResource,
                       pIsClosable);
      }
      else
      {
        addEmbeddedTab(pApplication.getId(), pCaption, iconResource, portalURI.getAbsoluteURL().toExternalForm(),
                       pIsClosable);
      }
    }
  }

  /**
   * Method call when a {@link PortalModuleUnRegisteredEvent} is received
   *
   * @param pEvent
   *          the source event
   */
  @Override
  @Handler
  public void onPortalModuleUnRegisteredEvent(final PortalModuleUnRegisteredEvent pEvent)
  {
    updatePortalModuleOnEvent(pEvent.getPortalModuleId());
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
   * Get the view element
   *
   * @return the view
   */
  public ProjectTabView getView()
  {
    return view;
  }

  /**
   * Get the tree container
   *
   * @return the tree container
   */
  protected NavigationTreeContainer getTreeContainer()
  {
    return dataSource;
  }

  /**
   * Get the opened tab mapping
   *
   * @return the module mapping
   */
  protected Map<String, ApplicationTab> getTabMapping()
  {
    return tabMapping;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(projectId).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object pOther)
  {
    if (this == pOther)
    {
      return true;
    }
    if (pOther == null)
    {
      return false;
    }
    if (getClass() != pOther.getClass())
    {
      return false;
    }
    if (this == pOther)
    {
      return true;
    }
    if (!(pOther instanceof ProjectTabPresenter))
    {
      return false;
    }
    final ProjectTabPresenter castOther = (ProjectTabPresenter) pOther;
    return new EqualsBuilder().append(projectId, castOther.getProjectId()).isEquals();
  }

}
