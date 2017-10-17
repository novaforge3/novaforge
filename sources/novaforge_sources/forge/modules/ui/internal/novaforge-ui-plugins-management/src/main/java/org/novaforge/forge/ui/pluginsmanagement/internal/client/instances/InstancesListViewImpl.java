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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.instances;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.AliasValidator;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.InstanceFieldFactory;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.InstancesItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components.ProjectInstanceItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components.PluginItemProperty;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.portal.data.validator.URLValidator;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class InstancesListViewImpl extends VerticalLayout implements InstancesListView
{

  /**
    * 
    */
  private static final long    serialVersionUID = 5018626211788913575L;

  private Table                instancesTable;
  private HorizontalLayout     tableLayout;
  private Button               returnToPluginsList;
  private Button               goToRequestsList;
  private Embedded             pluginIcon;
  private Label                pluginNameLabel;
  private Button               createInstance;
  private Window               createInstanceWindow;
  private Form                 createInstanceForm;
  private Button               createInstanceSubmit;
  private Window               editInstanceWindow;
  private Form                 editInstanceForm;
  private Button               editInstanceSubmit;
  private Window               showProjectsWindow;
  private Table                projectsTable;
  private DeleteConfirmWindow  projectDeleteWindow;
  private ComboBox             requestsComboBox;
  private DeleteConfirmWindow  instanceDeleteWindow;
  private Label                descLabel;
  private InstanceFieldFactory createInstanceFieldFactory;
  private InstanceFieldFactory editInstanceFieldFactory;

  /**
   * The default constructor. Initialize all components
   */
  public InstancesListViewImpl()
  {
    setMargin(true);
    initTopAction();
    initDescription();
    initTable();
    initCreateWindow();
    initEditWindow();
    initProjectsWindow();
    initDeleteWindow();
  }

  /**
   * Initialize the top action component
   */
  private void initTopAction()
  {
    final GridLayout actionsGrid = new GridLayout(3, 1);
    actionsGrid.setSpacing(true);
    actionsGrid.setMargin(true);
    returnToPluginsList = new Button();
    returnToPluginsList.setIcon(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT));
    actionsGrid.addComponent(returnToPluginsList, 0, 0);
    goToRequestsList = new Button();
    goToRequestsList.setIcon(new ThemeResource(NovaForgeResources.ICON_MESSAGE));
    actionsGrid.addComponent(goToRequestsList, 1, 0);
    createInstance = new Button();
    createInstance.setIcon(new ThemeResource(NovaForgeResources.ICON_INSTANCE_ADD));
    createInstance.setStyleName(NovaForge.BUTTON_PRIMARY);
    actionsGrid.addComponent(createInstance, 2, 0);
    addComponent(actionsGrid);
  }

  /**
   * Initialize the plugin's description component
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
   * Initialize the table component
   */
  private void initTable()
  {
    instancesTable = new Table();
    instancesTable.setPageLength(15);
    instancesTable.setStyleName(Reindeer.TABLE_STRONG);
    instancesTable.setWidth(100, Unit.PERCENTAGE);

    tableLayout = new HorizontalLayout();
    tableLayout.addComponent(instancesTable);
    tableLayout.setWidth(100, Unit.PERCENTAGE);

    addComponent(tableLayout);
  }

  /**
   * Initialize the window for createing an instance
   */
  private void initCreateWindow()
  {
    createInstanceWindow = new Window();
    createInstanceWindow.setModal(true);
    createInstanceWindow.setResizable(false);
    createInstanceWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_INSTANCE_ADD));
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    createInstanceForm = new Form();
    createInstanceForm.setImmediate(true);
    createInstanceForm.setInvalidCommitted(false);
    createInstanceForm.setWidth(375, Unit.PIXELS);
    createInstanceFieldFactory = new InstanceFieldFactory();
    createInstanceForm.setFormFieldFactory(createInstanceFieldFactory);
    requestsComboBox = new ComboBox();
    createInstanceForm.getLayout().addComponent(requestsComboBox);
    createInstanceSubmit = new Button();
    createInstanceSubmit.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));
    createInstanceSubmit.setStyleName(NovaForge.BUTTON_PRIMARY);
    final HorizontalLayout formFooter = (HorizontalLayout) createInstanceForm.getFooter();
    formFooter.setWidth(100, Unit.PERCENTAGE);
    formFooter.addComponent(createInstanceSubmit);
    formFooter.setComponentAlignment(createInstanceSubmit, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(createInstanceForm);
    windowLayout.setSizeUndefined();
    createInstanceWindow.setContent(windowLayout);

  }

  /**
   * Initialize the window for editing an instance
   */
  private void initEditWindow()
  {
    editInstanceWindow = new Window();
    editInstanceWindow.setModal(true);
    editInstanceWindow.setResizable(false);
    editInstanceWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_INSTANCE_EDIT));
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    editInstanceForm = new Form();
    editInstanceForm.setWidth(300, Unit.PIXELS);
    editInstanceFieldFactory = new InstanceFieldFactory();
    editInstanceForm.setFormFieldFactory(editInstanceFieldFactory);
    editInstanceSubmit = new Button();
    editInstanceSubmit.setIcon(new ThemeResource(NovaForgeResources.ICON_EDIT));
    editInstanceSubmit.setStyleName(NovaForge.BUTTON_PRIMARY);
    final HorizontalLayout formFooter = (HorizontalLayout) editInstanceForm.getFooter();
    formFooter.setWidth(100, Unit.PERCENTAGE);
    formFooter.addComponent(editInstanceSubmit);
    formFooter.setComponentAlignment(editInstanceSubmit, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(editInstanceForm);
    windowLayout.setSizeUndefined();
    editInstanceWindow.setContent(windowLayout);

  }

  /**
   * Initialize the window for projects
   */
  private void initProjectsWindow()
  {
    showProjectsWindow = new Window();
    showProjectsWindow.setModal(true);
    showProjectsWindow.setResizable(false);
    showProjectsWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_LIST_SUB2));
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);

    projectsTable = new Table();
    projectsTable.setPageLength(10);
    projectsTable.setStyleName(Reindeer.TABLE_STRONG);
    projectsTable.setWidth(600, Unit.PIXELS);

    windowLayout.addComponent(projectsTable);
    showProjectsWindow.setContent(windowLayout);

    projectDeleteWindow = new DeleteConfirmWindow(Messages.PLUGINSMANAGEMENT_PROJECTS_INSTANCE_DELETE_CONFIRM);

  }

  /**
   * Initialize the window for deleteing an instance
   */
  private void initDeleteWindow()
  {
    instanceDeleteWindow = new DeleteConfirmWindow(Messages.PLUGINSMANAGEMENT_DELETE_INSTANCE_CONFIRM);
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
  public Table getInstancesTable()
  {
    return instancesTable;
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
  public void showCreateInstanceWindow(final boolean pDisplay)
  {

    if (pDisplay)
    {
      final Locale locale = getLocale();

      requestsComboBox.setCaption(PluginsModule.getPortalMessages().getMessage(locale,
                                                                               Messages.PLUGINSMANAGEMENT_INSTANCE_LINK_REQUEST));
      requestsComboBox.setInputPrompt(PluginsModule.getPortalMessages().getMessage(locale,
                                                                                   Messages.PLUGINSMANAGEMENT_REQUESTS_LINK_AUTO));
      createInstanceSubmit.setCaption(PluginsModule.getPortalMessages().getMessage(locale,
                                                                                   Messages.PLUGINSMANAGEMENT_CREATE_INSTANCE_BUTTON));
      createInstanceWindow.setCaption(PluginsModule.getPortalMessages().getMessage(locale,
                                                                                   Messages.PLUGINSMANAGEMENT_CREATE_INSTANCE_BUTTON));
      refreshFieldFactory(locale, createInstanceFieldFactory);
      UI.getCurrent().addWindow(createInstanceWindow);
    }
    else
    {
      UI.getCurrent().removeWindow(createInstanceWindow);
    }
  }

  private void refreshFieldFactory(final Locale pLocale, final InstanceFieldFactory pInstanceFieldFactory)
  {
    final TextField name = pInstanceFieldFactory.getName();
    if (name != null)
    {

      name.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
          Messages.PLUGINSMANAGEMENT_FIELD_NAME));
    }
    final TextArea desc = pInstanceFieldFactory.getDesc();
    if (desc != null)
    {

      desc.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
          Messages.PLUGINSMANAGEMENT_FIELD_DESCRIPTION));
    }
    final TextField alias = pInstanceFieldFactory.getAlias();
    if (alias != null)
    {
      alias.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
          Messages.PLUGINSMANAGEMENT_FIELD_ALIAS));
      alias.removeAllValidators();
      alias.addValidator(new AliasValidator(PluginsModule.getPortalMessages().getMessage(pLocale,
          Messages.PLUGINSMANAGEMENT_FIELD_ALIAS_REQUIRED)));
      alias.setRequiredError(PluginsModule.getPortalMessages().getMessage(pLocale,
          Messages.PLUGINSMANAGEMENT_FIELD_ALIAS_REQUIRED));
    }
    final TextField baseurl = pInstanceFieldFactory.getBaseurl();
    if (baseurl != null)
    {
      baseurl.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
          Messages.PLUGINSMANAGEMENT_FIELD_BASEURL));
      baseurl.removeAllValidators();
      baseurl.addValidator(new URLValidator(PluginsModule.getPortalMessages().getMessage(pLocale,
          Messages.PLUGINSMANAGEMENT_FIELD_BASEURL_REQUIRED), false));
      baseurl.setRequiredError(PluginsModule.getPortalMessages().getMessage(pLocale,
          Messages.PLUGINSMANAGEMENT_FIELD_BASEURL_REQUIRED));

    }
    final CheckBox shareable = pInstanceFieldFactory.getShareable();
    if (shareable != null)
    {
      shareable.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
          Messages.PLUGINSMANAGEMENT_FIELD_SHAREABLE));

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCreateInstanceButton()
  {
    return createInstance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getCreateInstanceForm()
  {
    return createInstanceForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCreateInstanceButtonSubmit()
  {
    return createInstanceSubmit;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEditInstanceButtonSubmit()
  {
    return editInstanceSubmit;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getEditInstanceForm()
  {
    return editInstanceForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showEditInstanceWindow(final boolean pDisplay)
  {
    if (pDisplay)
    {
      final Locale locale = getLocale();
      editInstanceWindow.setCaption(PluginsModule.getPortalMessages().getMessage(locale,
                                                                                 Messages.PLUGINSMANAGEMENT_INSTANCE_ACTIONS_EDIT));
      editInstanceSubmit.setCaption(PluginsModule.getPortalMessages().getMessage(locale,
                                                                                 Messages.PLUGINSMANAGEMENT_INSTANCE_ACTIONS_EDIT));
      refreshFieldFactory(locale, editInstanceFieldFactory);
      UI.getCurrent().addWindow(editInstanceWindow);
    }
    else
    {
      UI.getCurrent().removeWindow(editInstanceWindow);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getInstanceDeleteWindow()
  {
    return instanceDeleteWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getGoToRequestsListButton()
  {
    return goToRequestsList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getRequestsComboBox()
  {
    return requestsComboBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detachInstancesTable()
  {
    tableLayout.removeAllComponents();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attachInstancesTable()
  {
    tableLayout.addComponent(instancesTable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    returnToPluginsList.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                                Messages.PLUGINSMANAGEMENT_RETURN_PLUGINS_LIST));
    goToRequestsList.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                             Messages.PLUGINSMANAGEMENT_GOTO_REQUESTS_LIST));
    createInstance.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.PLUGINSMANAGEMENT_CREATE_INSTANCE_BUTTON));

    descLabel.setCaption(PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                      Messages.PLUGINSMANAGEMENT_INSTANCES_TABLE_DESC));

    instancesTable.setColumnHeader(InstancesItemProperty.NAME.getPropertyId(),
                                   PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                                Messages.PLUGINSMANAGEMENT_FIELD_NAME));
    instancesTable.setColumnHeader(InstancesItemProperty.DESCRIPTION.getPropertyId(),
                                   PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                                Messages.PLUGINSMANAGEMENT_FIELD_DESCRIPTION));
    instancesTable.setColumnHeader(InstancesItemProperty.ALIAS.getPropertyId(),
                                   PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                                Messages.PLUGINSMANAGEMENT_FIELD_ALIAS));
    instancesTable.setColumnHeader(InstancesItemProperty.BASEURL.getPropertyId(),
                                   PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                                Messages.PLUGINSMANAGEMENT_FIELD_BASEURL));
    instancesTable.setColumnHeader(InstancesItemProperty.STATUS.getPropertyId(),
                                   PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                                Messages.PLUGINSMANAGEMENT_FIELD_STATUS));
    instancesTable.setColumnHeader(PluginItemProperty.ACTIONS.getPropertyId(),
                                   PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                                Messages.PLUGINSMANAGEMENT_FIELD_ACTIONS));

    projectsTable.setColumnHeader(ProjectInstanceItemProperty.ID.getPropertyId(),
                                  PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                               Messages.PROJECT_ADMIN_PROJECT_ID));
    projectsTable.setColumnHeader(ProjectInstanceItemProperty.NAME.getPropertyId(),
                                  PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                               Messages.PROJECT_ADMIN_PROJECT_NAME));
    projectsTable.setColumnHeader(ProjectInstanceItemProperty.AUTHOR.getPropertyId(),
                                  PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                               Messages.PROJECT_ADMIN_PROJECT_AUTHOR));

    projectsTable.setColumnHeader(ProjectInstanceItemProperty.APPLICATION_NAME.getPropertyId(),
                                  PluginsModule.getPortalMessages().getMessage(pLocale,
                                                                               Messages.PLUGINSMANAGEMENT_FIELD_APPLICATION));
    projectsTable.setColumnHeader(ProjectInstanceItemProperty.ACTIONS.getPropertyId(),
                                  PluginsModule.getPortalMessages().getMessage(pLocale, Messages.ACTIONS));

    projectDeleteWindow.refreshLocale(pLocale);

    instanceDeleteWindow.refreshLocale(pLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showProjectsWindow(final boolean pDisplay)
  {
    if (pDisplay)
    {
      final Locale locale = getLocale();
      showProjectsWindow.setCaption(PluginsModule.getPortalMessages().getMessage(locale,
                                                                                 Messages.PLUGINSMANAGEMENT_INSTANCE_ACTIONS_PROJECTS));
      UI.getCurrent().addWindow(showProjectsWindow);
    }
    else
    {
      UI.getCurrent().removeWindow(showProjectsWindow);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getProjectsTable()
  {
    return projectsTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getProjectDeleteWindow()
  {
    return projectDeleteWindow;
  }

}
