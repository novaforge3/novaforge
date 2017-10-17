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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.requests;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.requests.components.RequestsItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class RequestsListViewImpl extends VerticalLayout implements RequestsListView
{

  /**
   * SerialUID
   */
  private static final long   serialVersionUID = 8662520042329627055L;

  private Table               requestsTable;
  private HorizontalLayout    tableLayout;
  private Button              returnToPluginsList;
  private Button              goToInstancesList;
  private Embedded            pluginIcon;
  private Label               pluginNameLabel;
  private DeleteConfirmWindow deleteConfirmWindow;
  private Window              linkInstanceWindow;
  private Button              linkInstanceSubmit;
  private ComboBox            linkInstanceComboBox;
  private Label               linkRequestNameLabel;

  private Label               instanceLabel;

  private Label               requestLabel;

  private Label               descLabel;

  /**
   * Default constructor
   */
  public RequestsListViewImpl()
  {
    setMargin(true);
    initTopAction();
    initDescription();
    initTable();
    initLinkInstanceWindow();
    initDeleteWindow();
  }

  /**
   * Initailize top action component
   */
  private void initTopAction()
  {
    final GridLayout actionsGrid = new GridLayout(2, 1);
    actionsGrid.setSpacing(true);
    actionsGrid.setMargin(true);
    returnToPluginsList = new Button();
    returnToPluginsList.setIcon(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT));
    actionsGrid.addComponent(returnToPluginsList, 0, 0);
    goToInstancesList = new Button();
    goToInstancesList.setIcon(new ThemeResource(NovaForgeResources.ICON_INSTANCE_COLORED));
    actionsGrid.addComponent(goToInstancesList, 1, 0);
    addComponent(actionsGrid);
  }

  /**
   * Initialize the requests' plugin description
   */
  private void initDescription()
  {
    final GridLayout descGrid = new GridLayout(3, 1);
    descLabel = new Label();
    descGrid.addComponent(descLabel, 0, 0);
    pluginIcon = new Embedded();
    pluginIcon.setType(Embedded.TYPE_IMAGE);
    pluginIcon.setWidth(24, Unit.PIXELS);
    descGrid.addComponent(pluginIcon, 1, 0);
    pluginNameLabel = new Label();
    descGrid.addComponent(pluginNameLabel, 2, 0);
    addComponent(descGrid);
  }

  /**
   * Initialize the requests table
   */
  private void initTable()
  {
    requestsTable = new Table();
    requestsTable.setSortAscending(true);
    requestsTable.setPageLength(15);
    requestsTable.setImmediate(true);
    requestsTable.setStyleName(Reindeer.TABLE_STRONG);
    tableLayout = new HorizontalLayout();
    requestsTable.setWidth(100, Unit.PERCENTAGE);
    tableLayout.setWidth(100, Unit.PERCENTAGE);
    tableLayout.addComponent(requestsTable);
    addComponent(tableLayout);
  }

  /**
   * Initialize the link request window
   */
  private void initLinkInstanceWindow()
  {
    linkInstanceWindow = new Window();
    linkInstanceWindow.setModal(true);
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    final GridLayout grid = new GridLayout(2, 2);
    requestLabel = new Label();
    linkRequestNameLabel = new Label();
    linkRequestNameLabel.setStyleName(NovaForge.LABEL_BOLD);
    instanceLabel = new Label();
    linkInstanceComboBox = new ComboBox();
    linkInstanceComboBox.setTextInputAllowed(false);
    linkInstanceComboBox.setImmediate(true);
    grid.addComponent(requestLabel, 0, 0);
    grid.addComponent(linkRequestNameLabel, 1, 0);
    grid.addComponent(instanceLabel, 0, 1);
    grid.addComponent(linkInstanceComboBox, 1, 1);
    grid.setSpacing(true);
    windowLayout.addComponent(grid);
    linkInstanceSubmit = new Button();
    linkInstanceSubmit.setIcon(new ThemeResource(NovaForgeResources.ICON_INSTANCE_LINK));
    linkInstanceSubmit.setStyleName(NovaForge.BUTTON_PRIMARY);
    linkInstanceSubmit.setEnabled(false);
    windowLayout.addComponent(linkInstanceSubmit);
    windowLayout.setSizeUndefined();
    linkInstanceWindow.setContent(windowLayout);
  }

  /**
   * Initialize the delete request window
   */
  private void initDeleteWindow()
  {
    deleteConfirmWindow = new DeleteConfirmWindow(Messages.PLUGINSMANAGEMENT_DELETE_REQUEST_CONFIRM);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getReturnToPluginsList()
  {
    return returnToPluginsList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Embedded getPluginIcon()
  {
    return pluginIcon;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getPluginNameLabel()
  {
    return pluginNameLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getGoToInstancesList()
  {
    return goToInstancesList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getRequestsTable()
  {
    return requestsTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setRequestsTableVisibleColumns()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getDeleteRequestWindow()
  {
    return deleteConfirmWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getLinkInstanceWindow()
  {
    return linkInstanceWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getLinkInstanceButtonSubmit()
  {
    return linkInstanceSubmit;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getLinkInstanceComboBox()
  {
    return linkInstanceComboBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getLinkRequestNameLabel()
  {
    return linkRequestNameLabel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attachRequestsTable()
  {
    tableLayout.addComponent(requestsTable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detachRequestsTable()
  {
    tableLayout.removeAllComponents();
  }

  @Override
  public void refreshLocale(final Locale pLocale)
  {

    returnToPluginsList.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                                Messages.PLUGINSMANAGEMENT_RETURN_PLUGINS_LIST));
    goToInstancesList.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                              Messages.PLUGINSMANAGEMENT_GOTO_INSTANCES_LIST));

    descLabel.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                      Messages.PLUGINSMANAGEMENT_REQUESTS_TABLE_DESC));

    deleteConfirmWindow.refreshLocale(pLocale);

    linkInstanceWindow.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                               Messages.PLUGINSMANAGEMENT_REQUEST_LINK_INSTANCE));
    requestLabel.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                         Messages.PLUGINSMANAGEMENT_LINK_REQUEST));
    instanceLabel.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                          Messages.PLUGINSMANAGEMENT_LINK_INSTANCE));
    linkInstanceComboBox.setInputPrompt(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                                     Messages.PLUGINSMANAGEMENT_INSTANCE_EMPTY_ITEM));
    linkInstanceSubmit.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                               Messages.PLUGINSMANAGEMENT_REQUEST_LINK_INSTANCE));

    requestsTable.setColumnHeader(RequestsItemProperty.PROJECT.getPropertyId(),
                                  PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                               Messages.PLUGINSMANAGEMENT_FIELD_PROJECT));
    requestsTable.setColumnHeader(RequestsItemProperty.CREATEDDATE.getPropertyId(),
                                  PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                               Messages.PLUGINSMANAGEMENT_FIELD_CREATEDDATE));
    requestsTable.setColumnHeader(RequestsItemProperty.LOGIN.getPropertyId(),
                                  PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                               Messages.PLUGINSMANAGEMENT_FIELD_LOGIN));
  }

}
