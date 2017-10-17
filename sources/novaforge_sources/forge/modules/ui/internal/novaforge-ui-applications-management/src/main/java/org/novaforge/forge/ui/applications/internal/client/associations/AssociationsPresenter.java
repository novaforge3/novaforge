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
package org.novaforge.forge.ui.applications.internal.client.associations;

import com.google.common.base.Strings;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.CompositionServiceException;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.CompositionType;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.presenters.CompositionPresenter;
import org.novaforge.forge.core.plugins.categories.Association;
import org.novaforge.forge.core.plugins.categories.AssociationType;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.categories.FieldDescription;
import org.novaforge.forge.core.plugins.categories.Parameter;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.ui.applications.internal.client.associations.components.AssociationsActionsGenerator;
import org.novaforge.forge.ui.applications.internal.client.associations.components.AssociationsContainer;
import org.novaforge.forge.ui.applications.internal.client.associations.components.AssociationsDescriptionGenerator;
import org.novaforge.forge.ui.applications.internal.client.associations.components.AssociationsEnableGenerator;
import org.novaforge.forge.ui.applications.internal.client.associations.components.AssociationsStyleGenerator;
import org.novaforge.forge.ui.applications.internal.client.associations.components.NotificationsActionsGenerator;
import org.novaforge.forge.ui.applications.internal.client.associations.components.NotificationsContainer;
import org.novaforge.forge.ui.applications.internal.client.associations.components.NotificationsEnableGenerator;
import org.novaforge.forge.ui.applications.internal.client.associations.components.NotificationsStyleGenerator;
import org.novaforge.forge.ui.applications.internal.client.associations.components.ParametersContainer;
import org.novaforge.forge.ui.applications.internal.client.associations.models.CompatibleComposition;
import org.novaforge.forge.ui.applications.internal.client.events.ShowApplicationEvent;
import org.novaforge.forge.ui.applications.internal.client.global.components.ItemProperty;
import org.novaforge.forge.ui.applications.internal.module.AbstractApplicationsPresenter;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * This presenter handles spaces components displayed.
 *
 * @author Guillaume Lamirand
 */
public class AssociationsPresenter extends AbstractApplicationsPresenter implements Serializable
{
  /**
   * Logger
   */
  static final         Log  LOGGER           = LogFactory.getLog(AssociationsPresenter.class);
  /**
   * Serial version uid used for serialization
   */
  private static final long serialVersionUID = 975076226266863458L;
  /**
   * Content of associations view
   */
  final private AssociationsView       view;
  private final String                 projectId;
  private       ProjectApplication     currentApp;
  private       AssociationsContainer  associationsContainer;
  private       NotificationsContainer notificationsContainer;

  private List<FieldDescription> currentFields;

  private Composition currentComposition;

  private Item currentItem;

  /**
   * Default constructor. It will initialize the tree component associated to the view and bind some events.
   *
   * @param pView
   *     the view
   * @param pPortalContext
   *     the init context
   * @param pProjectId
   *     the project id
   */
  public AssociationsPresenter(final AssociationsView pView, final PortalContext pPortalContext,
                               final String pProjectId)
  {
    super(pPortalContext);
    view = pView;
    projectId = pProjectId;
    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  public void addListeners()
  {
    view.getReturnButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 8249786971137899604L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        getEventBus().publish(new ShowApplicationEvent(currentApp, getUuid()));
      }
    });
    view.getConfigurationWindow().addCloseListener(new CloseListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = 3184982732616186474L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void windowClose(final CloseEvent pEvent)
      {
        currentFields = null;
        currentComposition = null;

      }
    });
    view.getConfigurationCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3359437019763918747L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        if (Strings.isNullOrEmpty(currentComposition.getTemplate()))
        {
          try
          {
            final UUID compositionId = currentComposition.getUUID();
            ApplicationsModule.getCompositionPresenter().setCompositionStatus(projectId, compositionId.toString(),
                                                                              false);
            view.attachNotificationTable(false);
            final Composition composition = (Composition) view.getNotificationsTable()
                                                              .getContainerProperty(compositionId,
                                                                                    ItemProperty.COMPOSITION
                                                                                        .getPropertyId()).getValue();
            composition.setActivated(false);
            view.attachNotificationTable(true);
          }
          catch (final CompositionServiceException e)
          {
            ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());

          }
        }

        currentFields = null;
        currentComposition = null;
        UI.getCurrent().removeWindow(view.getConfigurationWindow());

      }
    });
    view.getConfigurationConfirmButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3359437019763918747L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        if ((currentFields != null) && (currentComposition != null))
        {
          try
          {
            view.getConfigurationActionsForm().commit();
            final Map<String, String> template = new HashMap<String, String>();
            for (final FieldDescription fieldDescription : currentFields)
            {
              if (!fieldDescription.isID())
              {
                final Field<?> field = view.getConfigurationActionsForm().getField(fieldDescription
                                                                                       .getClassFieldName());
                String value = "";
                if (field.getValue() != null)
                {
                  value = field.getValue().toString();
                }
                template.put(fieldDescription.getClassFieldName(), value);
              }
            }
            ApplicationsModule.getCompositionPresenter().updateCompositionTemplate(projectId,
                                                                                   currentComposition.getUUID()
                                                                                                     .toString(),
                                                                                   template);
            UI.getCurrent().removeWindow(view.getConfigurationWindow());
            manageNotifications();
          }
          catch (final CompositionServiceException e)
          {
            ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());

          }
          catch (final Exception e)
          {
            // Let form handle exception
            LOGGER.error("Unable update the composition template", e);
          }
        }

      }
    });
    view.getConfigurationPreviewButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3359437019763918747L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        try
        {
          // Attach window to parent
          UI.getCurrent().addWindow(view.getPreviewWindow());
          // initialize notification parameters
          final ParametersContainer parametersContainer = initParametersContainer(currentComposition);
          view.getPreviewParametersTable().setContainerDataSource(parametersContainer);
          view.getPreviewParametersTable().setVisibleColumns(ItemProperty.CAPTION.getPropertyId(),
                                                             ItemProperty.VALUE.getPropertyId());
          view.getPreviewParametersTable().setPageLength(parametersContainer.size());

          // Init action preview
          view.getPreviewActionsForm().removeAllProperties();
          final UUID pluginUUID = currentComposition.getTarget().getPluginUUID();
          currentFields = getFieldDescriptions(pluginUUID, AssociationType.ACTION, currentComposition.getTargetName(),
                                               view.getLocale());
          for (final FieldDescription fieldDescription : currentFields)
          {
            if (!fieldDescription.isID())
            {
              final Field tokenizedField = view.getConfigurationActionsForm().getField(fieldDescription
                                                                                           .getClassFieldName());
              String value = "";
              if (tokenizedField.getValue() != null)
              {
                value = tokenizedField.getValue().toString();
              }
              final Field field = convertToField(fieldDescription, parametersContainer.replaceKeys(value));
              field.setReadOnly(true);
              view.getPreviewActionsForm().addField(fieldDescription.getClassFieldName(), field);
            }
          }
        }
        catch (final PluginManagerException e)
        {
          ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());
        }
      }
    });

    view.getDeleteLinkCancelButton().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 7056680340970983123L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {

        view.attachTable(false);
        currentItem.getItemProperty(ItemProperty.ENABLE.getPropertyId()).setValue(true);
        view.attachTable(true);
        UI.getCurrent().removeWindow(view.getDeleteLinkWindow());

      }
    });
    view.getDeleteLinkConfirmButton().addClickListener(new ClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -1711231951651805713L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        try
        {
          final List<Composition> compositions = ApplicationsModule.getCompositionPresenter()
                                                                   .getCompositionFromSource(projectId, currentApp
                                                                                                            .getPluginInstanceUUID()
                                                                                                            .toString());

          for (final Composition composition : compositions)
          {
            deleteComposition(composition);
          }
          UI.getCurrent().removeWindow(view.getDeleteLinkWindow());
        }
        catch (final CompositionServiceException e)
        {
          ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, getCurrentLocale());
        }

      }

    });
  }

  /**
   * Refresh the view associated with the given parameters
   *
   * @param pApp
   *     the source application
   * @param allSpaces
   *     the project applications
   */
  public void refresh(final ProjectApplication pApp, final Map<Space, List<ProjectApplication>> allSpaces)
  {

    initTablecontainer();

    // Set current app
    currentApp = pApp;

    // FIXME [2250] temporary fix : Dont show GIT plugins for delivery link
    try
    {
      Map<Space, List<ProjectApplication>> allSpacesWithoutGit = new HashMap<Space, List<ProjectApplication>>();
      final String currentAppType = ApplicationsModule.getPluginsManager().getPluginMetadataByUUID(pApp.getPluginUUID()
                                                                                                       .toString())
                                                      .getType();
      if ("Deliveries".equals(currentAppType) || "Requirements".equals(currentAppType))
      {
        // Remove all GIT Plugins
        for (Map.Entry<Space, List<ProjectApplication>> spaceListEntry : allSpaces.entrySet())
        {
          Space currentSpace = spaceListEntry.getKey();
          List<ProjectApplication> currentSpaceAppsWithoutGit = new ArrayList<>();

          for (ProjectApplication projectApp : spaceListEntry.getValue())
          {
            if (!"Gitlab".equals(ApplicationsModule.getPluginsManager().getPluginMetadataByUUID(projectApp
                                                                                                    .getPluginUUID()
                                                                                                    .toString())
                                                   .getType()))
            {
              currentSpaceAppsWithoutGit.add(projectApp);
            }
          }
          if (!currentSpaceAppsWithoutGit.isEmpty())
          {
            allSpacesWithoutGit.put(currentSpace, currentSpaceAppsWithoutGit);
          }
        }
        associationsContainer.setDatas(pApp, allSpacesWithoutGit);
      }
      else
      {
        associationsContainer.setDatas(pApp, allSpaces);
      }
      final List<Composition> appCompositions = ApplicationsModule.getCompositionPresenter()
                                                                  .getCompositionFromSource(projectId,
                                                                                            pApp.getPluginInstanceUUID()
                                                                                                .toString());
      associationsContainer.setExistingCompositions(appCompositions);
    }

    catch (PluginManagerException e)
    {
      ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, getCurrentLocale());
    }
    catch (CompositionServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, getCurrentLocale());
    }
    // END FIXME [2250]

    /*
     * ORIGINAL FIXME CODE
     * // Init tree content
     * try
     * {
     * associationsContainer.setDatas(pApp, allSpacesWithoutGit);
     * final List<Composition> appCompositions = ApplicationsModule.getCompositionPresenter()
     * .getCompositionFromSource(projectId, pApp.getPluginInstanceUUID().toString());
     * associationsContainer.setExistingCompositions(appCompositions);
     * }
     * catch (final Exception e)
     * {
     * ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e,
     * view.getLocale());
     * }
     */

  }

  /**
   * Init the tree table container
   */
  private void initTablecontainer()
  {
    associationsContainer = new AssociationsContainer(this);
    view.getApplicationsTable().setContainerDataSource(associationsContainer);
    view.getApplicationsTable().setItemDescriptionGenerator(new AssociationsDescriptionGenerator());
    view.getApplicationsTable().setCellStyleGenerator(new AssociationsStyleGenerator());
    view.getApplicationsTable().removeGeneratedColumn(ItemProperty.ENABLE.getPropertyId());
    view.getApplicationsTable().addGeneratedColumn(ItemProperty.ENABLE.getPropertyId(),
                                                   new AssociationsEnableGenerator(this));
    view.getApplicationsTable().removeGeneratedColumn(CommonItemProperty.ACTIONS.getPropertyId());
    view.getApplicationsTable().addGeneratedColumn(CommonItemProperty.ACTIONS.getPropertyId(),
                                                   new AssociationsActionsGenerator(this));
    view.getApplicationsTable().setItemIconPropertyId(ItemProperty.ICON.getPropertyId());
    view.getApplicationsTable().setVisibleColumns(ItemProperty.CAPTION.getPropertyId(),
                                                  ItemProperty.ENABLE.getPropertyId(),
                                                  CommonItemProperty.ACTIONS.getPropertyId());
    view.getApplicationsTable().setColumnAlignment(ItemProperty.ENABLE.getPropertyId(), Align.CENTER);
    view.getApplicationsTable().setColumnWidth(ItemProperty.ENABLE.getPropertyId(), 50);
    view.getApplicationsTable().setColumnAlignment(CommonItemProperty.ACTIONS.getPropertyId(), Align.CENTER);
    view.getApplicationsTable().setColumnWidth(CommonItemProperty.ACTIONS.getPropertyId(), 50);
  }

  /**
   * Called when association is enabled
   *
   * @param pItem
   *     the item selected
   */
  @SuppressWarnings("unchecked")
  public void manageAssociation(final Item pItem)
  {
    currentItem = pItem;
    view.attachTable(false);
    final Property<?> enableProperty = pItem.getItemProperty(ItemProperty.ENABLE.getPropertyId());
    final boolean isEnable =
        (enableProperty != null) && (enableProperty.getValue() != null) && ((Boolean) enableProperty.getValue());
    pItem.getItemProperty(ItemProperty.ENABLE.getPropertyId()).setValue(!isEnable);
    final ProjectApplication targetApplication = (ProjectApplication) pItem.getItemProperty(ItemProperty.NODE
                                                                                                .getPropertyId())
                                                                           .getValue();
    if (isEnable)
    {
      try
      {
        final List<Composition> compositions = ApplicationsModule.getCompositionPresenter()
                                                                 .getCompositionFromSource(projectId, currentApp
                                                                                                          .getPluginInstanceUUID()
                                                                                                          .toString());

        boolean containsNotification = false;
        for (final Composition composition : compositions)
        {
          if ((CompositionType.NOTIFICATION.equals(composition.getType())) && (!Strings.isNullOrEmpty(composition
                                                                                                          .getTemplate())))
          {
            containsNotification = true;
            break;
          }
        }
        if (containsNotification)
        {
          UI.getCurrent().addWindow(view.getDeleteLinkWindow());
        }
        else
        {
          for (final Composition composition : compositions)
          {
            deleteComposition(composition);
          }
        }
      }
      catch (final Exception e)
      {
        ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());

      }
    }
    else
    {
      final List<CompatibleComposition> targeAssociations = (List<CompatibleComposition>) pItem
                                                                                              .getItemProperty(ItemProperty.ASSOCIATIONS
                                                                                                                   .getPropertyId())
                                                                                              .getValue();
      boolean hasNotification = false;
      for (final CompatibleComposition compatibleComposition : targeAssociations)
      {
        if (targetApplication.getPluginInstanceUUID().equals(compatibleComposition.getInstanceUUID()))
        {
          createComposition(targetApplication, compatibleComposition);
          currentItem.getItemProperty(ItemProperty.ENABLE.getPropertyId()).setValue(true);
          if ((!hasNotification) && (CompositionType.NOTIFICATION.equals(compatibleComposition
                                                                                     .getCompositionType())))
          {
            hasNotification = true;
          }
        }
      }
      if (hasNotification)
      {
        manageNotifications();
      }
    }
    view.attachTable(true);

  }

  private void deleteComposition(final Composition composition)
  {
    try
    {
      ApplicationsModule.getCompositionPresenter().deleteComposition(projectId, composition.getUUID().toString());
    }
    catch (final CompositionServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());
    }
  }

  private void createComposition(final ProjectApplication targetApplication,
                                 final CompatibleComposition compatibleComposition)
  {
    try
    {
      final CompositionPresenter compositionPresenter = ApplicationsModule.getCompositionPresenter();
      final Composition newComposition = compositionPresenter.newComposition();
      newComposition.setSourceName(compatibleComposition.getSource().getName());
      newComposition.setType(compatibleComposition.getCompositionType());
      newComposition.setTargetName(compatibleComposition.getTarget().getName());
      if (CompositionType.NOTIFICATION.equals(compatibleComposition.getCompositionType()))
      {
        newComposition.setActivated(false);
      }
      else
      {
        newComposition.setActivated(true);
      }
      compositionPresenter.createComposition(projectId, newComposition, currentApp.getUri(),
                                             targetApplication.getUri());
    }
    catch (final CompositionServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());
    }
  }

  /**
   * Called when configuration is asked
   */
  public void manageNotifications()
  {

    // first attach the window to its parent
    if (view.getNotificationsWindow().getParent() == null)
    {
      UI.getCurrent().addWindow(view.getNotificationsWindow());
    }

    // Init the container
    initNotificationsContainer();

    // Retrieve the datas
    try
    {
      final List<Composition> compositions = ApplicationsModule.getCompositionPresenter()
                                                               .getCompositionFromSource(projectId, currentApp
                                                                                                        .getPluginInstanceUUID()
                                                                                                        .toString());
      final List<Composition> notifications = new ArrayList<Composition>();
      for (final Composition composition : compositions)
      {
        if (CompositionType.NOTIFICATION.equals(composition.getType()))
        {
          notifications.add(composition);
        }
      }
      view.attachNotificationTable(false);
      notificationsContainer.setDatas(notifications);
      view.attachNotificationTable(true);
    }
    catch (final CompositionServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());

    }
  }

  /**
   * Init the table container
   */
  private void initNotificationsContainer()
  {
    notificationsContainer = new NotificationsContainer();
    view.getNotificationsTable().setContainerDataSource(notificationsContainer);
    view.getNotificationsTable().setCellStyleGenerator(new NotificationsStyleGenerator());
    view.getNotificationsTable().removeGeneratedColumn(CommonItemProperty.ACTIONS.getPropertyId());
    view.getNotificationsTable().addGeneratedColumn(CommonItemProperty.ACTIONS.getPropertyId(),
                                                    new NotificationsActionsGenerator(this));
    view.getNotificationsTable().removeGeneratedColumn(ItemProperty.ENABLE.getPropertyId());
    view.getNotificationsTable().addGeneratedColumn(ItemProperty.ENABLE.getPropertyId(),
                                                    new NotificationsEnableGenerator(this));
    view.getNotificationsTable().setVisibleColumns(ItemProperty.SOURCE_NAME.getPropertyId(),
                                                   ItemProperty.TARGET_NAME.getPropertyId(),
                                                   ItemProperty.ENABLE.getPropertyId(),
                                                   CommonItemProperty.ACTIONS.getPropertyId());
    view.getNotificationsTable().setColumnAlignment(CommonItemProperty.ACTIONS.getPropertyId(), Align.CENTER);
    view.getNotificationsTable().setColumnAlignment(ItemProperty.ENABLE.getPropertyId(), Align.CENTER);
    view.getNotificationsTable().setColumnWidth(ItemProperty.ENABLE.getPropertyId(), 50);
    view.getNotificationsTable().setColumnWidth(CommonItemProperty.ACTIONS.getPropertyId(), 50);
  }

  /**
   * Change the status of a composition
   *
   * @param pItemId
   *     the item id
   * @param pNewStatus
   *     the new status
   */
  public void changeStatus(final Object pItemId, final boolean pNewStatus)
  {
    try
    {
      ApplicationsModule.getCompositionPresenter().setCompositionStatus(projectId, pItemId.toString(), pNewStatus);
      view.attachNotificationTable(false);
      final Composition composition = (Composition) view.getNotificationsTable().getContainerProperty(pItemId,
                                                                                                      ItemProperty.COMPOSITION
                                                                                                          .getPropertyId())
                                                        .getValue();
      composition.setActivated(pNewStatus);
      view.attachNotificationTable(true);
      if ((pNewStatus) && (Strings.isNullOrEmpty(composition.getTemplate())))
      {
        configureTemplate(composition);
      }
    }
    catch (final CompositionServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());
    }
  }

  /**
   * Used when user wants to configure the composition template
   *
   * @param pComposition
   *     the composition to update
   */
  public void configureTemplate(final Composition pComposition)
  {
    currentComposition = pComposition;
    // first attach the window to its parent
    UI.getCurrent().addWindow(view.getConfigurationWindow());
    view.getConfigurationWindow().center();
    try
    {
      // initialize notification parameters
      final ParametersContainer parametersContainer = initParametersContainer(pComposition);
      view.getConfigurationParametersTable().setContainerDataSource(parametersContainer);
      view.getConfigurationParametersTable().setVisibleColumns(new String[] { ItemProperty.CAPTION.getPropertyId(),
                                                                              ItemProperty.DESCRIPTION
                                                                                  .getPropertyId() });
      view.getConfigurationParametersTable().setPageLength(parametersContainer.size());
      // initialize action form
      initActionsForm(pComposition);
    }
    catch (final Exception e)
    {
      ExceptionCodeHandler.showNotificationError(ApplicationsModule.getPortalMessages(), e, view.getLocale());
    }
  }

  /**
   * Init the parameters container
   *
   * @param pComposition
   *     the composition
   *
   * @throws PluginManagerException
   */
  private ParametersContainer initParametersContainer(final Composition pComposition) throws PluginManagerException
  {
    final ParametersContainer parametersContainer = new ParametersContainer();
    final UUID pluginUUID = pComposition.getSource().getPluginUUID();
    final PluginMetadata pluginMetadata = ApplicationsModule.getPluginsManager().getPluginMetadataByUUID(pluginUUID
                                                                                                             .toString());
    final List<FieldDescription> fieldDescriptions = getFieldDescriptions(pluginUUID, AssociationType.NOTIFICATION,
                                                                          pComposition.getSourceName(),
                                                                          view.getLocale());
    parametersContainer.setDatas(pluginMetadata.getType(), fieldDescriptions);
    return parametersContainer;
  }

  /**
   * INit the form using action fields
   *
   * @param pComposition
   *     the composition
   */
  private void initActionsForm(final Composition pComposition) throws PluginManagerException
  {
    view.getConfigurationActionsForm().removeAllProperties();
    final Map<String, String> template   = convertTemplate(pComposition.getTemplate());
    final UUID                pluginUUID = pComposition.getTarget().getPluginUUID();
    currentFields = getFieldDescriptions(pluginUUID, AssociationType.ACTION, pComposition.getTargetName(),
                                         view.getLocale());
    for (final FieldDescription fieldDescription : currentFields)
    {
      if (!fieldDescription.isID())
      {
        final String fieldKey = fieldDescription.getClassFieldName();
        final Field field = convertToField(fieldDescription, template.get(fieldKey));
        view.getConfigurationActionsForm().addField(fieldDescription.getClassFieldName(), field);
      }
    }

  }

  private Field convertToField(final FieldDescription pFieldDescription, final String pValue)
  {
    AbstractField returnField;
    if (Boolean.class.equals(pFieldDescription.getType()))
    {
      returnField = new CheckBox();
      returnField.setValue(Boolean.parseBoolean(pValue));
    }
    else
    {
      AbstractTextField field;
      if (pFieldDescription.getSize() > 200)
      {
        field = new TextArea();
      }
      else
      {
        field = new TextField();
      }
      field.setWidth(300, Unit.PIXELS);
      field.setValue(pValue);
      field.setNullRepresentation("");
      returnField = field;
    }
    if (pFieldDescription.isRequired())
    {
      returnField.setRequired(true);
      returnField.setRequiredError(ApplicationsModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                     Messages.APPLICATIONS_LINK_CONFIGURE_MAPPING_MANDATORY));
    }
    returnField.setCaption(pFieldDescription.getName());
    returnField.setDescription(pFieldDescription.getDescription());
    return returnField;
  }

  private Map<String, String> convertTemplate(final String pTemplate)
  {
    final Map<String, String> params = new HashMap<String, String>();
    if (!Strings.isNullOrEmpty(pTemplate))
    {
      final JSONObject jsonParameters = JSONObject.fromObject(pTemplate);
      for (final Iterator<?> iterator = jsonParameters.keys(); iterator.hasNext(); )
      {
        final String cle = iterator.next().toString();
        params.put(cle, jsonParameters.getString(String.valueOf(cle)));
      }
    }

    return params;
  }

  private List<FieldDescription> getFieldDescriptions(final UUID pPluginId, final AssociationType pType,
                                                      final String pName, final Locale pLocale)
      throws PluginManagerException
  {
    List<FieldDescription> fieldDescriptions = null;
    final Parameter paramater = getParameterForAssociation(pName, pType, pLocale, pPluginId);
    if (paramater != null)
    {
      fieldDescriptions = paramater.getFieldsDescription();
    }

    return fieldDescriptions;
  }

  private Parameter getParameterForAssociation(final String pNotificationName, final AssociationType pType,
                                               final Locale pLocale, final UUID pPluginId) throws PluginManagerException
  {
    final CategoryDefinitionService categoryDefinitionService = ApplicationsModule
                                                                    .getCategoryDefinitionService(pPluginId);
    final Association association = categoryDefinitionService.getAssociation(pType, pNotificationName);
    return categoryDefinitionService.getParamater(association, pLocale);

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

  /**
   * @return the currentApp
   */
  public ProjectApplication getCurrentApp()
  {
    return currentApp;
  }

}
