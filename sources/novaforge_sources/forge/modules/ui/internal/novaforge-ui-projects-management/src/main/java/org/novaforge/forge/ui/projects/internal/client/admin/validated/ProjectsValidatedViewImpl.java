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
package org.novaforge.forge.ui.projects.internal.client.admin.validated;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.projects.internal.client.admin.containers.ProjectItemProperty;
import org.novaforge.forge.ui.projects.internal.module.ProjectServices;
import org.vaadin.haijian.ExcelExporter;

import java.util.Collection;
import java.util.Locale;

/**
 * Implementation of {@link ProjectsValidatedView}
 * 
 * @author Guillaume Lamirand
 * @see ProjectsValidatedView
 */
public class ProjectsValidatedViewImpl extends VerticalLayout implements ProjectsValidatedView
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
  private Form                projectsForm;
  /**
   * The {@link Table} containing the projects list
   */
  private Table               projectsTable;
  /**
   * The excel exporter
   */
  private ExcelExporter       excelExporter;
  private Window              editWindow;
  private DeleteConfirmWindow deleteWindow;

  /**
   * Default constructor.
   */
  public ProjectsValidatedViewImpl()
  {
    // Init view
    setMargin(true);

    // Init contents
    final Component filter = initFilter();
    final Component content = initContent();
    final Component exporter = initExport();

    addComponent(filter);
    addComponent(content);
    addComponent(exporter);

    // Init subwindows
    initEditWindow();
    initDeleteWindow();
  }

  /**
   * Initialize the filter component
   * 
   * @return {@link Component} containing the filter component
   */
  private Component initFilter()
  {
    projectsForm = new Form();
    projectsForm.setImmediate(true);
    projectsForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    projectsForm.addField(FILTER_FIELD, filterTextField);
    return projectsForm;
  }

  /**
   * Initialize the content components
   * 
   * @return {@link Component} containing the content components
   */
  private Component initContent()
  {
    projectsTable = new Table();
    projectsTable.setSelectable(true);
    projectsTable.setPageLength(10);
    projectsTable.setWidth(100, Unit.PERCENTAGE);
    projectsTable.setStyleName(Reindeer.TABLE_STRONG);

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

        if ((child == null) || (!child.equals(projectsTable)))
        {
          final Collection<?> itemIds = projectsTable.getItemIds();
          for (final Object itemId : itemIds)
          {
            if (projectsTable.isSelected(itemId))
            {
              projectsTable.unselect(itemId);
              break;
            }
          }
        }
      }
    });
    return projectsTable;
  }

  /**
   * Initialize the export Layout
   */
  private Component initExport()
  {
    final HorizontalLayout layout = new HorizontalLayout();
    excelExporter = new ExcelExporter();
    excelExporter.setStyleName(NovaForge.BUTTON_LINK);
    layout.addComponent(excelExporter);
    return layout;
  }

  /**
   * Initialize the window displayed on invalidate action
   */
  private void initEditWindow()
  {
    editWindow = new Window();
    editWindow.setModal(true);
    editWindow.setResizable(false);
    editWindow.setWidth(650, Unit.PIXELS);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(100, Unit.PERCENTAGE);
    editWindow.setContent(windowLayout);
  }

  /**
   * Initialize the window displayed on invalidate action
   */
  private void initDeleteWindow()
  {
    deleteWindow = new DeleteConfirmWindow(Messages.PROJECT_ADMIN_VALIDATED_DELETE_CONFIRMLABEL);
    deleteWindow.setWidth(400, Unit.PIXELS);

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
    projectsForm.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_VALIDATED_TITLE));
    filterTextField.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_ADMIN_FILTER));
    projectsTable.setColumnHeader(ProjectItemProperty.ID.getPropertyId(), ProjectServices.getPortalMessages()
        .getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_ID));
    projectsTable.setColumnHeader(ProjectItemProperty.NAME.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_NAME));
    projectsTable.setColumnHeader(ProjectItemProperty.DESCRIPTION.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_DESCRIPTION));
    projectsTable.setColumnHeader(ProjectItemProperty.DATE.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_DATE));
    projectsTable.setColumnHeader(ProjectItemProperty.AUTHOR.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.PROJECT_ADMIN_PROJECT_AUTHOR));
    projectsTable.setColumnHeader(ProjectItemProperty.ACTIONS.getPropertyId(), ProjectServices
        .getPortalMessages().getMessage(pLocale, Messages.ACTIONS));

    excelExporter.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_EXPORT_EXCEL));
    excelExporter.setLocale(pLocale);
    editWindow.setCaption(ProjectServices.getPortalMessages().getMessage(pLocale,
        Messages.PROJECT_UPDATE_APPLY));
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
    return projectsTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExcelExporter getExcelExporter()
  {
    return excelExporter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getDeleteProjectWindow()
  {
    return deleteWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getEditProjectWindow()
  {
    return editWindow;
  }

}
