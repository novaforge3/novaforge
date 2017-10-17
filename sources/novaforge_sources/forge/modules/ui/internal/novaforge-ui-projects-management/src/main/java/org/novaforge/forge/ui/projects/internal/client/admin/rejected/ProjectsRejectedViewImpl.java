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
package org.novaforge.forge.ui.projects.internal.client.admin.rejected;

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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.projects.internal.client.admin.containers.ProjectItemProperty;
import org.novaforge.forge.ui.projects.internal.module.ProjectServices;

import java.util.Collection;
import java.util.Locale;

/**
 * Implementation of {@link ProjectsRejectedView}
 * 
 * @author Guillaume Lamirand
 * @see ProjectsRejectedView
 */
public class ProjectsRejectedViewImpl extends VerticalLayout implements ProjectsRejectedView
{

  /**
   * Filter constant for field
   */
  private static final String FILTER_FIELD     = "filter";

  /**
   * Serialization id
   */
  private static final long   serialVersionUID = -4420222948069676402L;

  /**
   * {@link Field} used to filter the table
   */
  private TextField           filterTextField;
  /**
   * {@link Form} containing filter and its table
   */
  private Form                rejectedForm;
  /**
   * The {@link Table} containing the projects list
   */
  private Table               rejectedsTable;
  private Window              validWindow;
  private Label               validConfirmLabel;
  private Button              validCancelButton;
  private Button              validConfirmButton;
  private DeleteConfirmWindow deleteWindow;

  /**
   * Default constructor.
   */
  public ProjectsRejectedViewImpl()
  {
    // Init view
    setMargin(true);

    // Init contents
    final Component filter = initFilter();
    final Component content = initContent();
    addComponent(filter);
    addComponent(content);

    // Init subwindows
    initValidWindow();
    initDeleteWindow();

  }

  /**
   * Initialize the filter component
   * 
   * @return {@link Component} containing the filter component
   */
  private Component initFilter()
  {
    rejectedForm = new Form();
    rejectedForm.setImmediate(true);
    rejectedForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    rejectedForm.addField(FILTER_FIELD, filterTextField);
    return rejectedForm;
  }

  /**
   * Initialize the content components
   * 
   * @return {@link Component} containing the content components
   */
  private Component initContent()
  {
    rejectedsTable = new Table();
    rejectedsTable.setPageLength(10);
    rejectedsTable.setWidth(100, Unit.PERCENTAGE);
    rejectedsTable.setStyleName(Reindeer.TABLE_STRONG);

    addLayoutClickListener(new LayoutClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3559455760887782118L;

      @Override
      public void layoutClick(final LayoutClickEvent event)
      {

        // Get the child component which was clicked
        final Component child = event.getChildComponent();

        if ((child == null) || (!child.equals(rejectedsTable)))
        {
          final Collection<?> itemIds = rejectedsTable.getItemIds();
          for (final Object itemId : itemIds)
          {
            if (rejectedsTable.isSelected(itemId))
            {
              rejectedsTable.unselect(itemId);
              break;
            }
          }
        }
      }
    });
    return rejectedsTable;
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
   * Initialize the window displayed on delete action
   */
  private void initDeleteWindow()
  {
    deleteWindow = new DeleteConfirmWindow(Messages.PROJECT_ADMIN_VALIDATED_DELETE_CONFIRMLABEL);
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
    rejectedForm.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_REJECTED_TITLE));
    rejectedForm.setDescription(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_REJECTED_DESCRIPTION));
    filterTextField.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_FILTER));

    rejectedsTable.setColumnHeader(ProjectItemProperty.ID.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_ID));
    rejectedsTable.setColumnHeader(ProjectItemProperty.NAME.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_NAME));
    rejectedsTable.setColumnHeader(ProjectItemProperty.DESCRIPTION.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_DESCRIPTION));
    rejectedsTable.setColumnHeader(ProjectItemProperty.DATE.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_DATE));
    rejectedsTable.setColumnHeader(ProjectItemProperty.AUTHOR.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_AUTHOR));

    validWindow.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_VALID_TITLE));
    validConfirmLabel.setValue(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_VALID_CONFIRMLABEL));
    validCancelButton.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    validConfirmButton.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_VALID_CONFIRM));

    deleteWindow.refreshLocale(pLocale);
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
    return rejectedsTable;
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
  public DeleteConfirmWindow getDeleteProjectWindow()
  {
    return deleteWindow;
  }

}
