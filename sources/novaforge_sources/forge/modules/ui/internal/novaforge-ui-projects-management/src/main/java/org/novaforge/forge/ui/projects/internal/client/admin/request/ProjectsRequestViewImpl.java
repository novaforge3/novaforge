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
package org.novaforge.forge.ui.projects.internal.client.admin.request;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.projects.internal.client.admin.containers.ProjectItemProperty;
import org.novaforge.forge.ui.projects.internal.module.ProjectServices;

import java.util.Collection;
import java.util.Locale;

/**
 * Implementation of {@link ProjectsRequestView}
 * 
 * @author Guillaume Lamirand
 * @see ProjectsRequestView
 */
public class ProjectsRequestViewImpl extends VerticalLayout implements ProjectsRequestView
{

  /**
   * Serialization id
   */
  private static final long   serialVersionUID = -4420222948069676402L;

  /**
   * Filter constant for field
   */
  private static final String FILTER_FIELD     = "filter";

  /**
   * {@link Field} used to filter the table
   */
  private TextField           filterTextField;
  /**
   * {@link Form} containing filter and its table
   */
  private Form                requestForm;
  /**
   * The {@link Table} containing the projects list
   */
  private Table               requestsTable;
  private Window              invalidWindow;
  private Label               invalidConfirmLabel;
  /**
   * 
   */
  private TextArea            invalidText;
  /**
   * 
   */
  private Button              invalidCancelButton;
  /**
   * 
   */
  private Button              invalidConfirmButton;
  /**
   * 
   */
  private Window              validWindow;
  /**
   * 
   */
  private Label               validConfirmLabel;
  /**
   * 
   */
  private Button              validCancelButton;
  /**
   * 
   */
  private Button              validConfirmButton;

  /**
   * 
   */

  /**
   * Default constructor.
   */
  public ProjectsRequestViewImpl()
  {
    // Init view
    setMargin(true);

    // Init contents
    final Component filter = initFilter();
    final Component content = initContent();
    addComponent(filter);
    addComponent(content);

    // Init subwindows
    initInValidWindow();
    initValidWindow();
  }

  /**
   * Initialize the filter component
   * 
   * @return {@link Component} containing the filter component
   */
  private Component initFilter()
  {
    requestForm = new Form();
    requestForm.setImmediate(true);
    requestForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    requestForm.addField(FILTER_FIELD, filterTextField);
    return requestForm;
  }

  /**
   * Initialize the content components
   * 
   * @return {@link Component} containing the content components
   */
  private Component initContent()
  {
    requestsTable = new Table();
    requestsTable.setSelectable(true);
    requestsTable.setPageLength(10);
    requestsTable.setWidth(100, Unit.PERCENTAGE);
    requestsTable.setStyleName(Reindeer.TABLE_STRONG);

    addLayoutClickListener(new LayoutClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3559455760887782118L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void layoutClick(final LayoutClickEvent event)
      {

        // Get the child component which was clicked
        final Component child = event.getChildComponent();

        if ((child == null) || (!child.equals(requestsTable)))
        {
          final Collection<?> itemIds = requestsTable.getItemIds();
          for (final Object itemId : itemIds)
          {
            if (requestsTable.isSelected(itemId))
            {
              requestsTable.unselect(itemId);
              break;
            }
          }
        }
      }
    });
    return requestsTable;
  }

  /**
   * Initialize the window displayed on invalidate action
   */
  private void initInValidWindow()
  {
    invalidWindow = new Window();
    invalidWindow.setModal(true);
    invalidWindow.setResizable(false);
    invalidWindow.setWidth(400, Unit.PIXELS);
    invalidWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_BLOCKED_ROUND));

    invalidConfirmLabel = new Label();
    invalidConfirmLabel.setContentMode(ContentMode.HTML);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(100, Unit.PERCENTAGE);

    // Init notification bos
    invalidText = new TextArea();
    invalidText.setWidth(250, Unit.PIXELS);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.setMargin(new MarginInfo(true, false, false, false));
    invalidConfirmButton = new Button();
    invalidCancelButton = new Button();
    invalidCancelButton.setStyleName(NovaForge.BUTTON_LINK);
    invalidConfirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_BLOCKED_ROUND));
    invalidConfirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(invalidCancelButton);
    buttons.addComponent(invalidConfirmButton);
    buttons.setComponentAlignment(invalidCancelButton, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(invalidConfirmButton, Alignment.MIDDLE_CENTER);
    // Set window content
    windowLayout.addComponent(invalidConfirmLabel);
    windowLayout.addComponent(invalidText);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(invalidConfirmLabel, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(invalidText, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    invalidWindow.setContent(windowLayout);

  }

  /**
   * Initialize the window displayed on validate action
   */
  private void initValidWindow()
  {
    validWindow = new Window();
    validWindow.setModal(true);
    validWindow.setResizable(false);
    validWindow.setWidth(300, Unit.PIXELS);
    validWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_VALIDATE_ROUND));

    validConfirmLabel = new Label();
    validConfirmLabel.setContentMode(ContentMode.HTML);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(100, Unit.PERCENTAGE);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.setMargin(new MarginInfo(true, false, false, false));
    validConfirmButton = new Button();
    validCancelButton = new Button();
    validCancelButton.setStyleName(NovaForge.BUTTON_LINK);
    validConfirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_VALIDATE_ROUND));
    validConfirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(validCancelButton);
    buttons.addComponent(validConfirmButton);
    buttons.setComponentAlignment(validCancelButton, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(validConfirmButton, Alignment.MIDDLE_CENTER);
    // Set window content
    windowLayout.addComponent(validConfirmLabel);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(validConfirmLabel, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    validWindow.setContent(windowLayout);
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
  public void refreshLocale(final Locale pLocale)
  {
    requestForm.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_WAITING_TITLE));
    filterTextField.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_FILTER));
    requestsTable.setColumnHeader(ProjectItemProperty.ID.getPropertyId(), ProjectServices.getPortalMessages()
        .getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_ID));
    requestsTable.setColumnHeader(ProjectItemProperty.NAME.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_NAME));
    requestsTable.setColumnHeader(ProjectItemProperty.DESCRIPTION.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_DESCRIPTION));
    requestsTable.setColumnHeader(ProjectItemProperty.DATE.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_DATE));
    requestsTable.setColumnHeader(ProjectItemProperty.AUTHOR.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_AUTHOR));
    requestsTable.setColumnHeader(ProjectItemProperty.ACTIONS.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.ACTIONS));

    invalidWindow.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_INVALID_TITLE));
    invalidConfirmLabel.setValue(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_INVALID_CONFIRMLABEL));
    invalidText.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_INVALID_EXPLANATION));
    invalidCancelButton.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    invalidConfirmButton.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_INVALID_CONFIRM));

    validWindow.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_VALID_TITLE));
    validConfirmLabel.setValue(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_VALID_CONFIRMLABEL));
    validCancelButton.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    validConfirmButton.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_VALID_CONFIRM));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getFilterTextField()
  {
    return filterTextField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getProjectsTable()
  {
    return requestsTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getValidProjectWindow()
  {
    return validWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getValidProjectConfirmButton()
  {
    return validConfirmButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getValidProjectCancelButton()
  {
    return validCancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getInValidProjectWindow()
  {
    return invalidWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getInValidProjectConfirmButton()
  {
    return invalidConfirmButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getInValidProjectCancelButton()
  {
    return invalidCancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextArea getInValidProjectText()
  {
    return invalidText;
  }

}
