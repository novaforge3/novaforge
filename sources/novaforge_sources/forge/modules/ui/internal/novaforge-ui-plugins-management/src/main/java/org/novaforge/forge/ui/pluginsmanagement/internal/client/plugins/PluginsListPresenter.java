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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginsCategoryManager;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.event.OpenInstancesListEvent;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.event.OpenRequestsListEvent;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.CategoryContainer;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.CategoryItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.PluginColumnActionsGenerator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.PluginColumnCategoryGenerator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.PluginColumnStatusGenerator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.PluginColumnTypeGenerator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.PluginColumnVersionGenerator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.PluginItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.PluginsContainer;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.StatusContainer;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.StatusItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.AbstractPluginsPresenter;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;
import org.novaforge.forge.ui.portal.event.actions.RefreshAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class PluginsListPresenter extends AbstractPluginsPresenter implements Serializable
{
  /**
   * SerialUID
   */
  private static final long       serialVersionUID  = -58404289842602236L;
  private static final Log        LOGGER            = LogFactory.getLog(PluginsListPresenter.class);
  /**
   * Content the workspace view
   */

  private final PluginsListView   view;
  private final PluginsContainer  pluginsContainer  = new PluginsContainer();
  private final StatusContainer   statusContainer   = new StatusContainer();
  private final CategoryContainer categoryContainer = new CategoryContainer();
  private Filter statusFilter;
  private Filter categoryFilter;
  private Filter eventFilter;
  private String                  selectedPluginID;

  /**
   * Default constructor
   * 
   * @param pView
   *          The {@link PluginsListView} to set to this presenter
   * @param pPortalContext
   *          the initial context
   */
  public PluginsListPresenter(final PluginsListView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;

    // Define listeners
    addListeners();
    // Initialize pluginsList
    initPluginsList();
  }

  /**
   * It will add listeners to view components
   */
  @SuppressWarnings("serial")
  private void addListeners()
  {
    view.getCategoryCombobox().addValueChangeListener(new ValueChangeListener()
    {
      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {
        final Filterable f = pluginsContainer;

        // Remove old filter
        if (categoryFilter != null)
        {
          f.removeContainerFilter(categoryFilter);
        }
        final String categoryComboboxValue = (String) view.getCategoryCombobox().getValue();
        if ((categoryComboboxValue != null) && !categoryComboboxValue.isEmpty())
        {
          // Set new filter for the status column
          categoryFilter = new SimpleStringFilter(PluginItemProperty.CATEGORY_ID.getPropertyId(),
              categoryComboboxValue, true, false);
          f.addContainerFilter(categoryFilter);
        }

      }
    });
    view.getStatusCombobox().addValueChangeListener(new ValueChangeListener()
    {

      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {
        final Filterable f = pluginsContainer;

        // Remove old filter
        if (statusFilter != null)
        {
          f.removeContainerFilter(statusFilter);
        }
        final String statusComboboxValue = (String) view.getStatusCombobox().getValue();
        if ((statusComboboxValue != null) && !statusComboboxValue.isEmpty())
        {
          // Set new filter for the status column
          statusFilter = new SimpleStringFilter(PluginItemProperty.STATUS_ID.getPropertyId(),
              statusComboboxValue, true, false);
          f.addContainerFilter(statusFilter);
        }

      }
    });
    view.getResetFiltersButton().addClickListener(new ClickListener()
    {
      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        final Filterable f = pluginsContainer;
        if (categoryFilter != null)
        {
          f.removeContainerFilter(categoryFilter);
        }
        view.getCategoryCombobox().setValue(null);
        if (statusFilter != null)
        {
          f.removeContainerFilter(statusFilter);
        }
        view.getStatusCombobox().setValue(null);
        if (eventFilter != null)
        {
          f.removeContainerFilter(eventFilter);
        }

      }
    });
    view.getChangeStatusSubmit().addClickListener(new ClickListener()
    {

      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        final String newStatusLabel = (String) view.getNewStatusComboBox().getValue();
        if ((newStatusLabel != null) && !newStatusLabel.isEmpty())
        {
          final PluginStatus newStatus = PluginStatus.fromLabel(newStatusLabel);
          try
          {
            PluginsModule.getPluginsManager().changePluginStatus(selectedPluginID, newStatus);
            refresh();
            selectedPluginID = null;
            UI.getCurrent().removeWindow(view.getChangeStatusWindow());
          }
          catch (final PluginManagerException e)
          {
            LOGGER.error("Unable to change plugin status", e);

            final Notification notification = new Notification(PluginsModule.getPortalMessages().getMessage(
                getCurrentLocale(), Messages.PLUGINSMANAGEMENT_ERROR_TITLE), PluginsModule
                .getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.PLUGINSMANAGEMENT_ERROR_CHANGING_STATUS), Type.ERROR_MESSAGE);
            notification.setDelayMsec(2000);
            notification.setHtmlContentAllowed(true);
            notification.show(Page.getCurrent());
          }
          catch (final UnsupportedOperationException e)
          {
            view.getNewStatusComboBox().setComponentError(
                new UserError(PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                    Messages.PLUGINSMANAGEMENT_ERROR_UNSUPPORTED_STATUS)));
            view.getChangeStatusSubmit().setEnabled(false);
          }
        }
        else
        {
          view.getNewStatusDescriptionTitle().setIcon(new ThemeResource(NovaForgeResources.ICON_ERROR));
          view.getNewStatusDescriptionTitle().setCaption(
              PluginsModule.getPortalMessages().getMessage(getCurrentLocale(),
                  Messages.PLUGINSMANAGEMENT_ERROR_NEWSTATUS_EMTPY));
          view.getNewStatusDescriptionTitle().setVisible(true);
        }
      }
    });
    view.getPluginsTable().addActionHandler(new RefreshAction()
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
        initPluginsContainer();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public PortalMessages getPortalMessages()
      {
        return PluginsModule.getPortalMessages();
      }

    });
  }

  /**
   * Initialize the plugins list
   */
  private void initPluginsList()
  {
    view.getPluginsTable().setContainerDataSource(pluginsContainer);

    view.getPluginsTable().setVisibleColumns(PluginItemProperty.TYPE.getPropertyId(),
        PluginItemProperty.CATEGORY_NAME.getPropertyId(), PluginItemProperty.VERSION.getPropertyId(),
        PluginItemProperty.STATUS_LABEL.getPropertyId(), PluginItemProperty.ACTIONS.getPropertyId());
    view.getPluginsTable().addGeneratedColumn(PluginItemProperty.TYPE.getPropertyId(),
        new PluginColumnTypeGenerator(this));
    view.getPluginsTable().addGeneratedColumn(PluginItemProperty.CATEGORY_NAME.getPropertyId(),
        new PluginColumnCategoryGenerator());
    view.getPluginsTable().addGeneratedColumn(PluginItemProperty.VERSION.getPropertyId(),
        new PluginColumnVersionGenerator());
    view.getPluginsTable().addGeneratedColumn(PluginItemProperty.STATUS_LABEL.getPropertyId(),
        new PluginColumnStatusGenerator());
    view.getPluginsTable().addGeneratedColumn(PluginItemProperty.ACTIONS.getPropertyId(),
        new PluginColumnActionsGenerator(this));

    // Define columns width
    view.getPluginsTable().setColumnExpandRatio(PluginItemProperty.TYPE.getPropertyId(), 0.3f);
    view.getPluginsTable().setColumnExpandRatio(PluginItemProperty.CATEGORY_NAME.getPropertyId(), 0.3f);
    view.getPluginsTable().setColumnExpandRatio(PluginItemProperty.VERSION.getPropertyId(), 0.3f);
    view.getPluginsTable().setColumnWidth(PluginItemProperty.STATUS_LABEL.getPropertyId(), 200);
    view.getPluginsTable().setColumnWidth(PluginItemProperty.ACTIONS.getPropertyId(), 100);

  }

  /**
   * Refresh the view
   */
  public void refresh()
  {
    refreshContent();
  }

  /**
   * Get the plugins and initialize the plugins container
   */
  private void initPluginsContainer()
  {
    try
    {
      // Initialize plugins list
      final List<PluginMetadata> pluginsList = PluginsModule.getPluginsManager().getAllPlugins();
      view.detachPluginsTable();
      pluginsContainer.setPlugins(pluginsList, getCurrentLocale());
      view.getPluginsTable().setPageLength(view.getPluginsTable().getItemIds().size() + 1);
      view.attachPluginsTable();
    }
    catch (final PluginManagerException e)
    {
      LOGGER.error("Unable to load plugins", e);
      final Notification notification = new Notification(PluginsModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                                      Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
                                                         PluginsModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                                      Messages.PLUGINSMANAGEMENT_ERROR_LOADING_PLUGINS),
                                                         Type.ERROR_MESSAGE);
      notification.setDelayMsec(2000);
      notification.setHtmlContentAllowed(true);
      notification.show(Page.getCurrent());
    }
  }

  /**
   * Initialize the catogery filter
   */
  private void initCategoryFilter(final Locale pLocale)
  {
    List<String> categoriesName;
    final List<CategoryDefinitionService> categories = new ArrayList<CategoryDefinitionService>();
    try
    {
      final PluginsCategoryManager categoryManager = PluginsModule.getPluginsCategoryManager();
      categoriesName = PluginsModule.getPluginsManager().getAllPluginCategories();
      for (final String category : categoriesName)
      {
        categories.add(categoryManager.getCategoryService(category));

      }
      categoryContainer.setCategories(categories, pLocale);
      view.getCategoryCombobox().setContainerDataSource(categoryContainer);
      view.getCategoryCombobox().setItemCaptionPropertyId(CategoryItemProperty.LABEL.getPropertyId());
    }
    catch (final PluginManagerException e)
    {
      LOGGER.error("Unable to load categories", e);

      final Notification notification = new Notification(PluginsModule.getPortalMessages().getMessage(
          pLocale, Messages.PLUGINSMANAGEMENT_ERROR_TITLE), PluginsModule.getPortalMessages().getMessage(
          pLocale, Messages.PLUGINSMANAGEMENT_ERROR_LOADING_CATEGORY), Type.ERROR_MESSAGE);
      notification.setDelayMsec(2000);
      notification.setHtmlContentAllowed(true);
      notification.show(Page.getCurrent());
    }

  }

  /**
   * Initialize the Status filter
   */
  private void initStatusFilter(final Locale pLocale)
  {
    final List<PluginStatus> statusList = PluginsModule.getPluginsManager().getAllPluginStatus();
    if ((statusList != null) && !statusList.isEmpty())
    {
      statusContainer.setStatus(statusList, pLocale);
      view.getStatusCombobox().setContainerDataSource(statusContainer);
      view.getStatusCombobox().setItemCaptionPropertyId(StatusItemProperty.LABEL.getPropertyId());
      for (final PluginStatus status : statusList)
      {
        view.getStatusCombobox().setItemIcon(status.getLabel(), StatusContainer.getStatusIcon(status.getLabel()));
      }
    }
    else
    {
      Notification.show(PluginsModule.getPortalMessages().getMessage(pLocale, Messages.PLUGINSMANAGEMENT_ERROR_TITLE),
                        PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                     Messages.PLUGINSMANAGEMENT_ERROR_LOADING_STATUS),
                        Type.ERROR_MESSAGE);
    }
  }

  /**
   * Manage the status of the plugin
   *
   * @param pItemId
   *          The plugin itemid
   */
  public void manageStatusClicked(final Object pItemId)
  {
    selectedPluginID = (String) pItemId;
    final List<PluginStatus> statusAvailableList = PluginsModule.getPluginsManager()
        .getAuthorizedChangesForStatus(
            PluginStatus.fromLabel((String) view.getPluginsTable().getItem(pItemId)
                .getItemProperty(PluginItemProperty.STATUS_ID.getPropertyId()).getValue()));
    view.getChangeStatusSubmit().setComponentError(null);
    
    //Remove the desactivate action for Alfresco
    if (((String) view.getPluginsTable().getItem(pItemId)
            .getItemProperty(PluginItemProperty.TYPE.getPropertyId()).getValue()).equalsIgnoreCase("Alfresco"))
    {
    	statusAvailableList.remove(PluginStatus.DESACTIVATED);
    }

    view.showManageStatusPopUp(pItemId, statusAvailableList);
  }

  /**
   * Manage the plugin's instances
   *
   * @param pItemId
   *          The plugin itemid
   */
  public void manageInstanceClicked(final Object pItemId)
  {
    getEventBus().publish(new OpenInstancesListEvent((String) pItemId));
  }

  /**
   * Manage the plugin's requests
   *
   * @param pItemId
   *          The plugin itemid
   */
  public void manageRequestsClicked(final Object pItemId)
  {
    getEventBus().publish(new OpenRequestsListEvent((String) pItemId));
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
    refreshLocalized(getCurrentLocale());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    // Initialize pluginsList
    initPluginsContainer();
    // Initialize list filters
    initCategoryFilter(pLocale);
    initStatusFilter(pLocale);
    view.refreshLocale(pLocale);

  }

}
