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

import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.PluginItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.StatusContainer;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.StatusItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;

import java.util.List;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class PluginsListViewImpl extends VerticalLayout implements PluginsListView
{

  private static final long serialVersionUID = -3975988024598743516L;
  private ComboBox          categoryCombobox;
  private ComboBox          statusCombobox;
  private Button            resetFilter;
  private Table             pluginsTable;
  private HorizontalLayout  pluginsTableLayout;
  private Window            changeStatusWindow;
  private Label             changeStatusinfo;
  private Button            changeStatusSubmit;
  private Label             changeStatusCurrent;
  private StatusContainer   changeStatusContainer;
  private Label             newStatusDescriptionTitle;
  private Label             newStatusDescription;
  private ComboBox          changeStatusNew;
  private Label             from;
  private Label             to;
  private Label             filtersIntro;

  /**
   * Default constructor
   */
  public PluginsListViewImpl()
  {
    setMargin(true);
    initFilters();
    initTable();
    initChangeStatusWindow();
  }

  /**
   * Initialize the plugins table filter
   */
  private void initFilters()
  {
    final GridLayout formFieldGrid = new GridLayout(4, 2);
    formFieldGrid.setHeight(112, Unit.PIXELS);
    formFieldGrid.setSpacing(true);
    formFieldGrid.setMargin(true);
    filtersIntro = new Label();
    categoryCombobox = new ComboBox();
    categoryCombobox.setTextInputAllowed(false);
    categoryCombobox.setImmediate(true);
    statusCombobox = new ComboBox();
    statusCombobox.setTextInputAllowed(false);
    statusCombobox.setImmediate(true);
    resetFilter = new Button();
    resetFilter.setStyleName(BaseTheme.BUTTON_LINK);
    formFieldGrid.addComponent(filtersIntro, 0, 0);
    formFieldGrid.addComponent(categoryCombobox, 1, 0);
    formFieldGrid.addComponent(statusCombobox, 2, 0);
    formFieldGrid.addComponent(resetFilter, 1, 1);
    formFieldGrid.setComponentAlignment(filtersIntro, Alignment.BOTTOM_LEFT);
    formFieldGrid.setComponentAlignment(resetFilter, Alignment.BOTTOM_LEFT);
    addComponent(formFieldGrid);
  }

  /**
   * Initialize the plugins table
   */
  private void initTable()
  {
    pluginsTable = new Table();
    pluginsTable.setSortAscending(true);
    pluginsTable.setPageLength(15);
    pluginsTable.setImmediate(true);
    pluginsTable.setStyleName(Reindeer.TABLE_STRONG);
    pluginsTable.setWidth(100, Unit.PERCENTAGE);

    pluginsTableLayout = new HorizontalLayout();
    pluginsTableLayout.addComponent(pluginsTable);
    pluginsTableLayout.setWidth(100, Unit.PERCENTAGE);

    addComponent(pluginsTableLayout);
  }

  /**
   * Initialize the window for changing the plugin's status
   */
  private void initChangeStatusWindow()
  {
    changeStatusWindow = new Window();
    changeStatusWindow.setModal(true);
    changeStatusWindow.setResizable(false);
    final VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    layout.setSpacing(true);
    changeStatusinfo = new Label();
    layout.addComponent(changeStatusinfo);
    final GridLayout grid = new GridLayout(2, 2);
    from = new Label();
    changeStatusCurrent = new Label();
    changeStatusCurrent.setStyleName(NovaForge.LABEL_ICON_16);
    to = new Label();
    changeStatusNew = new ComboBox();
    changeStatusNew.setTextInputAllowed(false);
    changeStatusNew.setImmediate(true);
    changeStatusContainer = new StatusContainer();
    changeStatusNew.setContainerDataSource(changeStatusContainer);
    grid.addComponent(from, 0, 0);
    grid.addComponent(changeStatusCurrent, 1, 0);
    grid.addComponent(to, 0, 1);
    grid.addComponent(changeStatusNew, 1, 1);
    layout.addComponent(grid);
    newStatusDescriptionTitle = new Label();
    newStatusDescriptionTitle.setVisible(false);
    newStatusDescriptionTitle.setStyleName(NovaForge.LABEL_ICON_16);
    layout.addComponent(newStatusDescriptionTitle);
    newStatusDescription = new Label();
    layout.addComponent(newStatusDescription);
    changeStatusSubmit = new Button();
    changeStatusSubmit.setIcon(new ThemeResource(NovaForgeResources.ICON_STATUS));
    changeStatusSubmit.setStyleName(Reindeer.BUTTON_DEFAULT);
    layout.addComponent(changeStatusSubmit);

    // Set the content of the window
    changeStatusWindow.setContent(layout);

    // Inner Listeners
    changeStatusNew.addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 717570974882368940L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {
        final String statusId = (String) changeStatusNew.getValue();
        changeStatusSubmit.setEnabled(true);
        changeStatusNew.setComponentError(null);
        if ((statusId != null) && !statusId.isEmpty())
        {
          newStatusDescriptionTitle.setIcon(new ThemeResource(NovaForgeResources.ICON_INFORMATION));
          final Item status = changeStatusContainer.getItem(statusId);
          newStatusDescription.setValue((String) status.getItemProperty(
              StatusItemProperty.DESCRIPTION.getPropertyId()).getValue());
          newStatusDescriptionTitle.setVisible(true);
          newStatusDescription.setVisible(true);
        }
        else
        {
          newStatusDescriptionTitle.setVisible(false);
          newStatusDescription.setVisible(false);
        }

      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getPluginsTable()
  {
    return pluginsTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getStatusCombobox()
  {
    return statusCombobox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getResetFiltersButton()
  {
    return resetFilter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getCategoryCombobox()
  {
    return categoryCombobox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getChangeStatusWindow()
  {
    return changeStatusWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showManageStatusPopUp(final Object pPluginId, final List<PluginStatus> pStatus)
  {
    final Locale locale = getLocale();
    changeStatusWindow.setCaption(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_NEWSTATUS_EMPTY_ITEM));
    final Item plugin = pluginsTable.getItem(pPluginId);
    changeStatusNew.setInputPrompt(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_NEWSTATUS_EMPTY_ITEM));
    changeStatusSubmit.setCaption(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_WINDOW_STATUS_INFO));
    changeStatusNew.select(changeStatusNew.getNullSelectionItemId());
    changeStatusContainer.removeAllItems();
    changeStatusContainer.setStatus(pStatus, locale);
    changeStatusNew.setItemCaptionPropertyId(StatusItemProperty.LABEL.getPropertyId());
    for (final PluginStatus status : pStatus)
    {
      changeStatusNew.setItemIcon(status.getLabel(), StatusContainer.getStatusIcon(status.getLabel()));
    }
    changeStatusWindow.setCaption(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_WINDOW_STATUS_TITLE,
        plugin.getItemProperty(PluginItemProperty.TYPE.getPropertyId()).getValue()));
    changeStatusinfo.setValue(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_WINDOW_STATUS_TITLE,
        plugin.getItemProperty(PluginItemProperty.TYPE.getPropertyId()).getValue()));
    final String statusID = (String) plugin.getItemProperty(PluginItemProperty.STATUS_ID.getPropertyId())
        .getValue();
    final String statusName = (String) plugin
        .getItemProperty(PluginItemProperty.STATUS_LABEL.getPropertyId()).getValue();
    changeStatusCurrent.setCaption(statusName);
    changeStatusCurrent.setIcon(StatusContainer.getStatusIcon(statusID));
    newStatusDescriptionTitle.setCaption(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_WINDOW_NEWSTATUS_TITLE));
    // Resize with setted values
    changeStatusWindow.getContent().setSizeUndefined();
    changeStatusWindow.setSizeUndefined();
    UI.getCurrent().addWindow(changeStatusWindow);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getNewStatusDescriptionLabel()
  {
    return newStatusDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getNewStatusComboBox()
  {
    return changeStatusNew;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getChangeStatusSubmit()
  {
    return changeStatusSubmit;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getNewStatusDescriptionTitle()
  {
    return newStatusDescriptionTitle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attachPluginsTable()
  {
    pluginsTableLayout.addComponent(pluginsTable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detachPluginsTable()
  {
    pluginsTableLayout.removeAllComponents();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    filtersIntro.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                         Messages.PLUGINSMANAGEMENT_FILTERS_INTRO));
    categoryCombobox.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                             Messages.PLUGINSMANAGEMENT_FIELD_CATEGORY));
    categoryCombobox.setInputPrompt(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                                 Messages.PLUGINSMANAGEMENT_FILTERS_EMPTY_ITEM));
    statusCombobox.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.PLUGINSMANAGEMENT_FIELD_STATUS));
    statusCombobox.setInputPrompt(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                               Messages.PLUGINSMANAGEMENT_FILTERS_EMPTY_ITEM));
    resetFilter.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                        Messages.PLUGINSMANAGEMENT_FILTERS_RESET_BUTTON));

    from.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                 Messages.PLUGINSMANAGEMENT_WINDOW_STATUS_FROM));
    to.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale, Messages.PLUGINSMANAGEMENT_WINDOW_STATUS_TO));

    pluginsTable.setColumnHeader(PluginItemProperty.TYPE.getPropertyId(),
                                 PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                              Messages.PLUGINSMANAGEMENT_FIELD_APPLICATION));
    pluginsTable.setColumnHeader(PluginItemProperty.CATEGORY_NAME.getPropertyId(),
                                 PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                              Messages.PLUGINSMANAGEMENT_FIELD_CATEGORY));
    pluginsTable.setColumnHeader(PluginItemProperty.VERSION.getPropertyId(),
                                 PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                              Messages.PLUGINSMANAGEMENT_FIELD_VERSION));
    pluginsTable.setColumnHeader(PluginItemProperty.STATUS_LABEL.getPropertyId(),
                                 PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                              Messages.PLUGINSMANAGEMENT_FIELD_STATUS));
    pluginsTable.setColumnHeader(PluginItemProperty.STATUS_DECRIPTION.getPropertyId(),
                                 PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                              Messages.PLUGINSMANAGEMENT_PLUGIN_STATUS_DESCRIPTION));
    pluginsTable.setColumnHeader(PluginItemProperty.ACTIONS.getPropertyId(),
                                 PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                              Messages.PLUGINSMANAGEMENT_FIELD_ACTIONS));
  }

}
