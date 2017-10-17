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
package org.novaforge.forge.ui.applications.internal.client.global;

import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;
import net.engio.mbassy.listener.Handler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Node;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.applications.internal.client.applications.ApplicationsPresenter;
import org.novaforge.forge.ui.applications.internal.client.applications.ApplicationsViewImpl;
import org.novaforge.forge.ui.applications.internal.client.applications.CreateApplicationsPresenter;
import org.novaforge.forge.ui.applications.internal.client.applications.CreateApplicationsViewImpl;
import org.novaforge.forge.ui.applications.internal.client.associations.AssociationsPresenter;
import org.novaforge.forge.ui.applications.internal.client.associations.AssociationsViewImpl;
import org.novaforge.forge.ui.applications.internal.client.events.CreateApplicationEvent;
import org.novaforge.forge.ui.applications.internal.client.events.DisplayDefaultRightEvent;
import org.novaforge.forge.ui.applications.internal.client.events.LinkApplicationEvent;
import org.novaforge.forge.ui.applications.internal.client.events.RefreshTreeEvent;
import org.novaforge.forge.ui.applications.internal.client.events.SelectParentEvent;
import org.novaforge.forge.ui.applications.internal.client.events.ShowApplicationEvent;
import org.novaforge.forge.ui.applications.internal.client.global.components.ApplicationsContainer;
import org.novaforge.forge.ui.applications.internal.client.global.components.ApplicationsDescriptionGenerator;
import org.novaforge.forge.ui.applications.internal.client.global.components.ApplicationsStyleGenerator;
import org.novaforge.forge.ui.applications.internal.client.global.components.ItemProperty;
import org.novaforge.forge.ui.applications.internal.client.spaces.SpacesPresenter;
import org.novaforge.forge.ui.applications.internal.client.spaces.SpacesViewImpl;
import org.novaforge.forge.ui.applications.internal.module.AbstractApplicationsPresenter;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.portal.event.ProjectSpacesUpdateEvent;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This presenter handles project components displayed.
 * 
 * @author Guillaume Lamirand
 */
public class GlobalPresenter extends AbstractApplicationsPresenter implements Serializable
{
  /**
   * Logger
   */
  static final Log                             LOG              = LogFactory.getLog(GlobalPresenter.class);
  /**
   * Serial version uid used for serialization
   */
  private static final long serialVersionUID = -5042299647493799344L;
  /**
   * Content of project view
   */
  final private GlobalView                           view;
  private final SpacesPresenter                      spacePresenter;
  private final ApplicationsPresenter                appsPresenter;
  private final AssociationsPresenter                associationsPresenter;
  /**
   * Data source which manage tree items
   */
  private       ApplicationsContainer                dataSource;
  /**
   * This variable contains the project Id associated to this presenter
   */
  private       String                               projectId;
  private       Map<Space, List<ProjectApplication>> allSpaces;
  private       CreateApplicationsPresenter          createAppsPresenter;

  /**
   * Default constructor. It will initialize the tree component associated to the view and bind some events.
   *
   * @param pView
   *          the view
   * @param pPortalContext
   *          the init context
   * @param pProjectId
   *          project id
   */
  public GlobalPresenter(final GlobalView pView, final PortalContext pPortalContext, final String pProjectId)
  {
    super(pPortalContext);
    projectId = pProjectId;
    view = pView;
    spacePresenter = new SpacesPresenter(new SpacesViewImpl(), pPortalContext, projectId);
    appsPresenter = new ApplicationsPresenter(new ApplicationsViewImpl(), pPortalContext, projectId);
    associationsPresenter = new AssociationsPresenter(new AssociationsViewImpl(), pPortalContext, projectId);
    initTree();
    addListeners();
  }

  /**
   * Initialize {@link Tree} component
   *
   * @see GlobalView#getProjectTree()
   */
  private void initTree()
  {
    dataSource = new ApplicationsContainer();

    view.getProjectTree().setContainerDataSource(dataSource);
    view.getProjectTree().setItemDescriptionGenerator(new ApplicationsDescriptionGenerator());
    view.getProjectTree().setItemStyleGenerator(new ApplicationsStyleGenerator());
    view.getProjectTree().setItemIconPropertyId(ItemProperty.ICON.getPropertyId());
    view.getProjectTree().setItemCaptionPropertyId(ItemProperty.CAPTION.getPropertyId());
    view.getProjectTree().setItemCaptionMode(ItemCaptionMode.PROPERTY);
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getAddButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -5510777733723916126L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent event)
      {
        final Space newSpace = ApplicationsModule.getSpacePresenter().newSpace();
        view.getSplitPanel().setSecondComponent(spacePresenter.getComponent());
        spacePresenter.refresh(newSpace, false);

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
        getEventBus().publish(new DisplayDefaultRightEvent(getUuid()));
        refreshContent();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public PortalMessages getPortalMessages()
      {
        return ApplicationsModule.getPortalMessages();
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
        refreshNodePresenter((String) pEvent.getItemId());
      }

    });
  }

  /**
   * Callback on {@link RefreshTreeEvent}
   *
   * @param pEvent
   *          source event
   */
  @Handler
  public void onRefreshTreeEvent(final RefreshTreeEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid()))
    {
      refreshContent();
      if (pEvent.getNode() instanceof Space)
      {
        final Space space = (Space) pEvent.getNode();
        view.getProjectTree().expandItem(space.getName());
        view.getProjectTree().select(space.getName());
        view.getSplitPanel().setSecondComponent(spacePresenter.getComponent());
        spacePresenter.refresh(space, true);
      }
      else if (pEvent.getNode() instanceof ProjectApplication)
      {
        final ProjectApplication app = (ProjectApplication) pEvent.getNode();
        final String itemId = app.getPluginInstanceUUID().toString();
        final Object parentId = dataSource.getParent(itemId);
        if (!view.getProjectTree().isExpanded(parentId))
        {
          view.getProjectTree().expandItem(parentId);
        }
        view.getProjectTree().select(itemId);
        view.getSplitPanel().setSecondComponent(appsPresenter.getComponent());
        appsPresenter.refresh(getParentSpace(itemId), app, true);
      }
      getEventBus().publish(new ProjectSpacesUpdateEvent(projectId));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    refresh();
    if (appsPresenter.getComponent().getParent() != null)
    {
      final String appItemId = appsPresenter.getCurrentApp().getPluginInstanceUUID().toString();
      refreshNodePresenter(appItemId);

    }
    else if (spacePresenter.getComponent().getParent() != null)
    {
      final String spaceItemId = spacePresenter.getCurrentSpace().getUri();
      refreshNodePresenter(spaceItemId);
    }
    else if (associationsPresenter.getComponent().getParent() != null)
    {
      final String appItemId = associationsPresenter.getCurrentApp().getPluginInstanceUUID().toString();

      final Property<?> nodeProperty = view.getProjectTree().getContainerProperty(appItemId,
          ItemProperty.NODE.getPropertyId());
      final Node node = (Node) nodeProperty.getValue();
      if (node instanceof ProjectApplication)
      {
        final ProjectApplication application = (ProjectApplication) node;
        getEventBus().publish(new LinkApplicationEvent(application, getUuid()));
      }
    }
    else
    {
      getEventBus().publish(new DisplayDefaultRightEvent(getUuid()));
    }

  }

  /**
   * Will return the parent {@link Space} of an {@link ProjectApplication}
   *
   * @param pInstanceUUID
   *          application instance uuid
   * @return {@link Space} found
   */
  private Space getParentSpace(final String pInstanceUUID)
  {
    final String parentID = (String) dataSource.getParent(pInstanceUUID);
    return (Space) dataSource.getContainerProperty(parentID, ItemProperty.NODE.getPropertyId()).getValue();
  }

  /**
   * Will refresh the project spaces and applications
   */
  public void refresh()
  {
    try
    {
      // Backup expanded item
      final List<Object> expandedItems = new ArrayList<Object>();
      final Collection<?> itemIds = view.getProjectTree().getItemIds();
      for (final Object object : itemIds)
      {
        if ((view.getProjectTree().areChildrenAllowed(object)) && (view.getProjectTree().isExpanded(object)))
        {
          expandedItems.add(object);
        }
      }
      // Update datasource
      allSpaces = ApplicationsModule.getSpacePresenter().getAllSpacesWithApplications(projectId);
      dataSource.setSpaces(allSpaces);

      // Expanded back the item
      for (final Object expandedItem : expandedItems)
      {
        if (view.getProjectTree().containsId(expandedItem))
        {
          view.getProjectTree().expandItem(expandedItem);
        }
      }
    }
    catch (final SpaceServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());
    }

  }

  private void refreshNodePresenter(final String pItemId)
  {
    // // Get item property for Application type
    final Property<?> nodeProperty = view.getProjectTree().getContainerProperty(pItemId,
        ItemProperty.NODE.getPropertyId());
    final Node node = (Node) nodeProperty.getValue();
    // Check if an url has been set
    if (node instanceof ProjectApplication)
    {
      ProjectApplication application = (ProjectApplication) node;
      if (!ApplicationStatus.DELETE_IN_PROGRESS.equals(application.getStatus()))
      {
        try
        {
          application = ApplicationsModule.getApplicationPresenter().getApplication(projectId, node.getUri());
          view.getProjectTree().getContainerProperty(pItemId, ItemProperty.NODE.getPropertyId())
              .setValue(application);
          view.getProjectTree().markAsDirtyRecursive();
          view.getProjectTree().select(pItemId);
        }
        catch (final ApplicationServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e,
              view.getLocale());

        }
      }
      view.getSplitPanel().setSecondComponent(appsPresenter.getComponent());
      appsPresenter
          .refresh(getParentSpace(application.getPluginInstanceUUID().toString()), application, true);
    }
    else
    {
      try
      {
        final Space space = ApplicationsModule.getSpacePresenter().getSpace(projectId, node.getUri());
        view.getSplitPanel().setSecondComponent(spacePresenter.getComponent());
        spacePresenter.refresh(space, true);
        if (!view.getProjectTree().isExpanded(space.getName()))
        {
          view.getProjectTree().expandItem(space.getName());
        }
      }
      catch (final SpaceServiceException e)
      {
        ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e,
            view.getLocale());

      }
    }
  }

  /**
   * Callback on {@link SelectParentEvent}
   * 
   * @param pEvent
   *          source event
   */
  @Handler
  public void onSelectParentEvent(final SelectParentEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid()))
    {
      view.getProjectTree().select(pEvent.getSpace().getName());
  
      view.getSplitPanel().setSecondComponent(spacePresenter.getComponent());
    }
  }

  /**
   * Callback on {@link DisplayDefaultRightEvent}
   * 
   * @param pEvent
   *          source event
   */
  @Handler
  public void onDisplayDefaultRightEvent(final DisplayDefaultRightEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid()))
    {
      view.getSplitPanel().setSecondComponent(view.getDefaultRight());
    }
  }

  /**
   * Callback on {@link CreateApplicationEvent}
   * 
   * @param pEvent
   *          source event
   */
  @Handler
  public void onCreateApplicationEvent(final CreateApplicationEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid()))
    {
      if (createAppsPresenter != null)
      {
        createAppsPresenter.unregisterReferences();
      }
      createAppsPresenter = new CreateApplicationsPresenter(new CreateApplicationsViewImpl(),
          getPortalContext(), projectId);
      view.getSplitPanel().setSecondComponent(createAppsPresenter.getComponent());
      createAppsPresenter.refresh(spacePresenter.getCurrentSpace());
    }
  }

  /**
   * Callback on {@link LinkApplicationEvent}
   * 
   * @param pEvent
   *          source event
   */
  @Handler
  public void onLinkApplicationEvent(final LinkApplicationEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid()))
    {
      view.getSplitPanel().setSecondComponent(associationsPresenter.getComponent());
      associationsPresenter.refresh(pEvent.getProjectApplication(), allSpaces);
    }
  }

  /**
   * Callback on {@link ShowApplicationEvent}
   * 
   * @param pEvent
   *          source event
   */
  @Handler
  public void onShowApplicationEvent(final ShowApplicationEvent pEvent)
  { 
    if (getUuid().equals(pEvent.getUuid()))
    {
      final ProjectApplication application = pEvent.getProjectApplication();
      view.getSplitPanel().setSecondComponent(appsPresenter.getComponent());
      appsPresenter.refresh(getParentSpace(application.getPluginInstanceUUID().toString()), application, true);
    }
  }

  /**
   * Get the tree container
   *
   * @return the tree container
   */
  protected ApplicationsContainer getTreeContainer()
  {
    return dataSource;
  }

  /**
   * @param projectId
   *          the projectId to set
   */
  protected void setProjectId(final String projectId)
  {
    this.projectId = projectId;
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
  protected void refreshLocalized(final Locale pLocale)
  {
    view.refreshLocale(pLocale);

  }

}
