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

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.applications.internal.client.global.components.ItemProperty;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class AssociationsViewImpl extends VerticalLayout implements AssociationsView
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 3138433391585703787L;

  private Button            returnButton;
  private Form              appForm;

  private TreeTable         treeTable;
  private CssLayout         tableLayout;
  private Window            notificationsWindow;
  private Table             notificationsTable;
  private Label             notificationDescription;

  private Window            configurationWindow;
  private Label             configurationDescription;
  private Form              configurationNotificationParameterForm;
  private Table             configurationNotificationParameterTable;
  private Form              configurationActionParameterForm;

  private Button            previewButton;
  private Button            confirmButton;
  private Button            cancelButton;
  private Window            previewWindow;
  private Label             previewDescription;
  private Form              previewNotificationParameterForm;
  private Table             previewNotificationParameterTable;
  private Form              previewActionParameterForm;

  private Window            deleteLinkWindow;
  private Label             deleteLinkConfirmLabel;
  private Button            deleteLinkCancelButton;
  private Button            deleteLinkConfirmButton;

  /**
   * Default constructor.
   */
  public AssociationsViewImpl()
  {
    setMargin(true);

    final Component headers = initHeaders();
    final Component form = initForm();
    final Component table = initTable();

    addComponent(headers);
    addComponent(form);
    addComponent(table);

    // Init subwindows
    initNotificationsWindow();
    initConfigurationWindow();
    initPreviewWindow();
    initDeleteLinkWindow();
  }

  private Component initHeaders()
  {
    final HorizontalLayout headerButtons = new HorizontalLayout();
    headerButtons.setSpacing(true);
    headerButtons.setMargin(new MarginInfo(false, false, true, false));
    returnButton = new Button();
    returnButton.setIcon(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT));

    headerButtons.addComponent(returnButton);
    return headerButtons;
  }

  private Component initForm()
  {
    appForm = new Form();
    appForm.setImmediate(true);
    appForm.setInvalidCommitted(false);
    return appForm;
  }

  private Component initTable()
  {
    tableLayout = new CssLayout();
    tableLayout.setSizeFull();
    treeTable = new TreeTable();
    treeTable.setPageLength(15);
    treeTable.setWidth(100, Unit.PERCENTAGE);
    treeTable.setStyleName(Reindeer.TABLE_STRONG);
    tableLayout.addComponent(treeTable);
    return tableLayout;
  }

  private void initNotificationsWindow()
  {
    notificationsWindow = new Window();
    notificationsWindow.setModal(true);
    notificationsWindow.setResizable(false);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(600, Unit.PIXELS);

    notificationDescription = new Label();
    notificationsTable = new Table();
    notificationsTable.setPageLength(8);
    notificationsTable.setStyleName(Reindeer.TABLE_STRONG);

    // Set window content
    windowLayout.addComponent(notificationDescription);
    windowLayout.setComponentAlignment(notificationDescription, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(notificationsTable);
    windowLayout.setComponentAlignment(notificationsTable, Alignment.MIDDLE_CENTER);
    notificationsWindow.setContent(windowLayout);

  }

  private void initConfigurationWindow()
  {
    configurationWindow = new Window();
    configurationWindow.setModal(true);
    configurationWindow.setResizable(true);
    configurationWindow.setWidth(600, Unit.PIXELS);
    configurationWindow.setHeight(650, Unit.PIXELS);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    configurationWindow.setContent(windowLayout);

    configurationDescription = new Label();
    configurationNotificationParameterForm = new Form();
    final VerticalLayout notificationParameterFormLayout = new VerticalLayout();
    notificationParameterFormLayout.setSpacing(true);
    notificationParameterFormLayout.setMargin(new MarginInfo(true, false, true, false));
    configurationNotificationParameterForm.setLayout(notificationParameterFormLayout);
    configurationNotificationParameterForm.setWidth(100, Unit.PERCENTAGE);
    configurationNotificationParameterTable = new Table();
    configurationNotificationParameterTable.setStyleName(Reindeer.TABLE_STRONG);
    notificationParameterFormLayout.addComponent(configurationNotificationParameterTable);
    notificationParameterFormLayout.setComponentAlignment(configurationNotificationParameterTable,
        Alignment.MIDDLE_CENTER);
    configurationActionParameterForm = new Form();
    configurationActionParameterForm.setWidth(100, Unit.PERCENTAGE);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    previewButton = new Button();
    previewButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SEARCH));
    confirmButton = new Button();
    confirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SAVE_DARK));
    confirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    cancelButton = new Button();
    cancelButton.setStyleName(NovaForge.BUTTON_LINK);
    buttons.addComponent(cancelButton);
    buttons.setComponentAlignment(cancelButton, Alignment.MIDDLE_RIGHT);
    buttons.addComponent(previewButton);
    buttons.setComponentAlignment(previewButton, Alignment.MIDDLE_RIGHT);
    buttons.addComponent(confirmButton);
    buttons.setComponentAlignment(confirmButton, Alignment.BOTTOM_LEFT);

    // Set window content
    windowLayout.addComponent(configurationDescription);
    windowLayout.setComponentAlignment(configurationDescription, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(configurationNotificationParameterForm);
    windowLayout.setComponentAlignment(configurationNotificationParameterForm, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(configurationActionParameterForm);
    windowLayout.setComponentAlignment(configurationActionParameterForm, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

  }

  private void initPreviewWindow()
  {
    previewWindow = new Window();
    previewWindow.setModal(true);
    previewWindow.setResizable(false);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(550, Unit.PIXELS);

    previewDescription = new Label();
    previewNotificationParameterForm = new Form();
    final VerticalLayout notificationParameterFormLayout = new VerticalLayout();
    notificationParameterFormLayout.setSpacing(true);
    notificationParameterFormLayout.setMargin(new MarginInfo(true, false, true, false));
    previewNotificationParameterForm.setLayout(notificationParameterFormLayout);
    previewNotificationParameterForm.setWidth(100, Unit.PERCENTAGE);
    previewNotificationParameterTable = new Table();
    previewNotificationParameterTable.setStyleName(Reindeer.TABLE_STRONG);
    notificationParameterFormLayout.addComponent(previewNotificationParameterTable);
    notificationParameterFormLayout.setComponentAlignment(previewNotificationParameterTable,
        Alignment.MIDDLE_CENTER);
    previewActionParameterForm = new Form();
    previewActionParameterForm.setWidth(100, Unit.PERCENTAGE);

    // Set window content
    windowLayout.addComponent(previewDescription);
    windowLayout.setComponentAlignment(previewDescription, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(previewNotificationParameterForm);
    windowLayout.setComponentAlignment(previewNotificationParameterForm, Alignment.MIDDLE_CENTER);
    windowLayout.addComponent(previewActionParameterForm);
    windowLayout.setComponentAlignment(previewActionParameterForm, Alignment.MIDDLE_CENTER);
    previewWindow.setContent(windowLayout);

  }

  private void initDeleteLinkWindow()
  {
    deleteLinkWindow = new Window();
    deleteLinkWindow.setModal(true);
    deleteLinkWindow.setResizable(false);

    deleteLinkConfirmLabel = new Label();
    deleteLinkConfirmLabel.setContentMode(ContentMode.HTML);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(400, Unit.PIXELS);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    deleteLinkConfirmButton = new Button();
    deleteLinkCancelButton = new Button();
    deleteLinkCancelButton.setStyleName(NovaForge.BUTTON_LINK);
    deleteLinkConfirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_LINK_DELETE));
    deleteLinkConfirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(deleteLinkCancelButton);
    buttons.addComponent(deleteLinkConfirmButton);
    buttons.setComponentAlignment(deleteLinkCancelButton, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(deleteLinkConfirmButton, Alignment.MIDDLE_CENTER);
    // Set window content
    windowLayout.addComponent(deleteLinkConfirmLabel);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(deleteLinkConfirmLabel, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    deleteLinkWindow.setContent(windowLayout);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getAssociationsForm()
  {
    return appForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TreeTable getApplicationsTable()
  {
    return treeTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    returnButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_RETURN));
    appForm.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_TITLE));
    appForm.setDescription(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_DESCRIPTION));
    treeTable.setColumnHeader(ItemProperty.CAPTION.getPropertyId(), ApplicationsModule.getPortalMessages()
        .getMessage(pLocale, Messages.APPLICATIONS_LINK_NAME));
    treeTable.setColumnHeader(ItemProperty.ENABLE.getPropertyId(), ApplicationsModule.getPortalMessages()
        .getMessage(pLocale, Messages.APPLICATIONS_LINK_ENABLE));
    treeTable.setColumnHeader(CommonItemProperty.ACTIONS.getPropertyId(), ApplicationsModule
        .getPortalMessages().getMessage(pLocale, Messages.ACTIONS));
    notificationsWindow.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_NOTIFICATION_TITLE));
    notificationDescription.setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_NOTIFICATION_DESCRIPTION));
    notificationsTable.setColumnHeader(ItemProperty.SOURCE_NAME.getPropertyId(), ApplicationsModule
        .getPortalMessages().getMessage(pLocale, Messages.APPLICATIONS_LINK_NOTIFICATION_SOURCE));
    notificationsTable.setColumnHeader(ItemProperty.TARGET_NAME.getPropertyId(), ApplicationsModule
        .getPortalMessages().getMessage(pLocale, Messages.APPLICATIONS_LINK_NOTIFICATION_TARGET));
    notificationsTable.setColumnHeader(CommonItemProperty.ACTIONS.getPropertyId(), ApplicationsModule
        .getPortalMessages().getMessage(pLocale, Messages.ACTIONS));

    configurationWindow.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_CONFIGURE_TITLE));
    configurationDescription.setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_CONFIGURE_DESCRIPTION));
    previewButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_CONFIGURE_PREVIEW));
    confirmButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_CONFIGURE_CONFIRM));
    cancelButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    configurationNotificationParameterForm.setCaption(ApplicationsModule.getPortalMessages().getMessage(
        pLocale, Messages.APPLICATIONS_LINK_CONFIGURE_PARAM_TITLE));
    configurationNotificationParameterTable.setColumnHeader(
        ItemProperty.CAPTION.getPropertyId(),
        ApplicationsModule.getPortalMessages().getMessage(pLocale,
            Messages.APPLICATIONS_LINK_CONFIGURE_PARAM_NAME));
    configurationNotificationParameterTable.setColumnHeader(
        ItemProperty.DESCRIPTION.getPropertyId(),
        ApplicationsModule.getPortalMessages().getMessage(pLocale,
            Messages.APPLICATIONS_LINK_CONFIGURE_PARAM_DESCRIPTION));
    configurationNotificationParameterTable.setColumnHeader(CommonItemProperty.ACTIONS.getPropertyId(),
        ApplicationsModule.getPortalMessages().getMessage(pLocale, Messages.ACTIONS));
    configurationNotificationParameterTable.setColumnHeader(ItemProperty.ENABLE.getPropertyId(),
        ApplicationsModule.getPortalMessages().getMessage(pLocale, Messages.APPLICATIONS_LINK_ENABLE));
    configurationActionParameterForm.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_CONFIGURE_MAPPING_TITLE));

    previewWindow.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_PREVIEW_TITLE));
    previewDescription.setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_PREVIEW_DESCRIPTION));
    previewNotificationParameterForm.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_PREVIEW_PARAM_TITLE));
    previewNotificationParameterTable.setColumnHeader(
        ItemProperty.CAPTION.getPropertyId(),
        ApplicationsModule.getPortalMessages().getMessage(pLocale,
            Messages.APPLICATIONS_LINK_PREVIEW_PARAM_NAME));
    previewNotificationParameterTable.setColumnHeader(ItemProperty.VALUE.getPropertyId(), ApplicationsModule
        .getPortalMessages().getMessage(pLocale, Messages.APPLICATIONS_LINK_PREVIEW_PARAM_VALUE));
    previewActionParameterForm.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_PREVIEW_MAPPING_TITLE));

    deleteLinkWindow.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_DELETE_TITLE));
    deleteLinkConfirmLabel.setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_DELETE_CONFIRMLABEL));
    deleteLinkCancelButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    deleteLinkConfirmButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
        Messages.APPLICATIONS_LINK_DELETE_CONFIRM));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getReturnButton()
  {
    return returnButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attachTable(final boolean pAttach)
  {
    if (pAttach)
    {
      treeTable.refreshRowCache();
      tableLayout.addComponent(treeTable);
    }
    else
    {
      tableLayout.removeAllComponents();
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getNotificationsTable()
  {
    return notificationsTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getNotificationsWindow()
  {
    return notificationsWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attachNotificationTable(final boolean pAttach)
  {
    if (pAttach)
    {
      notificationsTable.refreshRowCache();
      ((AbstractOrderedLayout) notificationsWindow.getContent()).addComponent(notificationsTable);
      ((AbstractOrderedLayout) notificationsWindow.getContent()).setComponentAlignment(notificationsTable,
                                                                                       Alignment.MIDDLE_CENTER);
    }
    else
    {
      ((AbstractOrderedLayout) notificationsWindow.getContent()).removeComponent(notificationsTable);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getConfigurationActionsForm()
  {
    return configurationActionParameterForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getConfigurationParametersTable()
  {
    return configurationNotificationParameterTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getConfigurationPreviewButton()
  {
    return previewButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getConfigurationCancelButton()
  {
    return cancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getConfigurationConfirmButton()
  {
    return confirmButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getConfigurationWindow()
  {
    return configurationWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getPreviewParametersTable()
  {
    return previewNotificationParameterTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getPreviewWindow()
  {
    return previewWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getPreviewActionsForm()
  {
    return previewActionParameterForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getDeleteLinkWindow()
  {
    return deleteLinkWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getDeleteLinkCancelButton()
  {
    return deleteLinkCancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getDeleteLinkConfirmButton()
  {
    return deleteLinkConfirmButton;
  }
}
