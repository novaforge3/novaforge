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
package org.novaforge.forge.ui.requirements.internal.client.codeview.view;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.requirements.internal.client.containers.CodeViewItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.client.requirement.details.DetailsView;
import org.novaforge.forge.ui.requirements.internal.client.requirement.details.DetailsViewImpl;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class CodeViewViewImpl extends VerticalLayout implements CodeViewView
{

  /**
   * Serialization id
   */
  private static final long serialVersionUID         = -4420222948069676402L;

  /**
   * Contains the title used for the default view
   */
  private final Label       title;
  /**
   * The details view
   */
  private final DetailsView detailsView              = new DetailsViewImpl();
  /**
   * The details window
   */
  private final Window      requirementDetailsWindow = new Window();
  /**
   * {@link Field} used to filter the table
   */
  private TextField filterRequirementField;
  /**
   * {@link Field} used to filter the table
   */
  private ComboBox  filterRepositoryComboBox;
  /**
   * {@link Field} used to filter the table
   */
  private ComboBox  filterStatusComboBox;
  /**
   * Filter type ComboBox
   */
  private ComboBox  filterTypeComboBox;
  /**
   * {@link Form} containing filter and its table
   */
  private Form      codeViewForm;
  /**
   * The {@link Table} containing the projects list
   */
  private TreeTable codeViewTable;
  /**
   * The filters apply button
   */
  private Button    filterApplyButton;
  /**
   * The reset filters button
   */
  private Button    filterResetButton;

  /**
   * Default constructor.
   */
  public CodeViewViewImpl()
  {
    // Init view
    setMargin(true);

    // Init title
    title = new Label();
    title.setStyleName(Reindeer.LABEL_H2);
    addComponent(title);

    // Init contents
    final Component filter  = initFilter();
    final Component content = initContent();

    addComponent(filter);
    addComponent(content);

    initPopups();
  }

  /**
   * Initialize the filter component
   *
   * @return {@link Component} containing the filter component
   */
  private Component initFilter()
  {
    codeViewForm = new Form();
    codeViewForm.setImmediate(false);
    codeViewForm.setInvalidCommitted(false);

    VerticalLayout filterIDLayout = new VerticalLayout();
    filterResetButton = new Button();
    filterResetButton.setStyleName(NovaForge.BUTTON_LINK);
    filterResetButton.setStyleName(NovaForge.BUTTON_LINK);
    filterRequirementField = new TextField();
    filterRequirementField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    filterIDLayout.addComponent(filterRequirementField);
    filterIDLayout.addComponent(filterResetButton);
    filterRepositoryComboBox = new ComboBox();
    filterRepositoryComboBox.setWidth(NovaForge.FORM_FIELD_WIDTH);
    filterTypeComboBox = new ComboBox();
    filterTypeComboBox.setWidth(NovaForge.FORM_FIELD_WIDTH);
    filterStatusComboBox = new ComboBox();
    filterStatusComboBox.setWidth(NovaForge.FORM_FIELD_WIDTH);
    filterApplyButton = new Button();
    filterApplyButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    filterApplyButton.setIcon(new ThemeResource(NovaForgeResources.ICON_FILTER));

    HorizontalLayout filtersLayout = new HorizontalLayout();
    filtersLayout.setSpacing(true);
    filtersLayout.addComponent(filterIDLayout);
    filtersLayout.addComponent(filterRepositoryComboBox);
    filtersLayout.addComponent(filterTypeComboBox);
    filtersLayout.addComponent(filterStatusComboBox);
    filtersLayout.addComponent(filterApplyButton);

    codeViewForm.getLayout().addComponent(filtersLayout);

    return codeViewForm;
  }

  /**
   * Initialize the content components
   *
   * @return {@link Component} containing the content components
   */
  private Component initContent()
  {
    codeViewTable = new TreeTable();
    codeViewTable.setWidth(100, Unit.PERCENTAGE);
    codeViewTable.setStyleName(Reindeer.TABLE_STRONG);

    return codeViewTable;
  }

  /**
   * Initialize the view popups
   */
  private void initPopups()
  {
    detailsView.setPopupMode(true);
    requirementDetailsWindow.setContent(detailsView);
    requirementDetailsWindow.setModal(true);
    requirementDetailsWindow.setResizable(false);
    requirementDetailsWindow.setWidth(500, Unit.PIXELS);
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
    title.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.GLOBAL_MENU_CODE));
    codeViewForm.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.FILTER_REQUIREMENTS_FORM));
    filterRequirementField.setInputPrompt(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.FILTER_REQUIREMENTS_INPUTPROMPT));
    filterRequirementField.setDescription(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.TESTVIEW_FILTER_REQUIREMENTS_DESC));
    filterRepositoryComboBox.setInputPrompt(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REQUIREMENT_FIELD_REPOSITORY));
    filterTypeComboBox.setInputPrompt(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REQUIREMENT_FIELD_TYPE));
    filterStatusComboBox.setInputPrompt(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.REQUIREMENT_FIELD_STATUS));
    filterApplyButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.FROM_FILTER_SUBMIT));
    filterResetButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.FROM_FILTER_RESET));

    codeViewTable.setColumnHeader(CodeViewItemProperty.ID.getPropertyId(), RequirementsModule
        .getPortalMessages().getMessage(pLocale, Messages.REQUIREMENT_FIELD_ID));
    codeViewTable.setColumnHeader(CodeViewItemProperty.REQUIREMENT_NAME.getPropertyId(), RequirementsModule
        .getPortalMessages().getMessage(pLocale, Messages.CODEVIEW_TABLE_HEADER_REQUIREMENT));
    codeViewTable.setColumnHeader(CodeViewItemProperty.REQUIREMENT_DESCRIPTION.getPropertyId(),
        RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.REQUIREMENT_FIELD_DESCRIPTION));
    codeViewTable.setColumnHeader(CodeViewItemProperty.CLASS.getPropertyId(), RequirementsModule
        .getPortalMessages().getMessage(pLocale, Messages.CODEVIEW_TABLE_HEADER_CLASS));
    codeViewTable.setColumnHeader(CodeViewItemProperty.VERSION_IN_REQUIREMENT.getPropertyId(),
        RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.REQUIREMENT_FIELD_VERSION));
    codeViewTable.setColumnHeader(CodeViewItemProperty.VERSION_IN_CODE.getPropertyId(), RequirementsModule
        .getPortalMessages().getMessage(pLocale, Messages.CODEVIEW_TABLE_HEADER_CODE));
    codeViewTable.setColumnHeader(CodeViewItemProperty.STATUS.getPropertyId(), RequirementsModule
        .getPortalMessages().getMessage(pLocale, Messages.REQUIREMENT_FIELD_STATUS));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getCodeViewTable()
  {
    return codeViewTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getFilterRequirementField()
  {
    return filterRequirementField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getFilterRepositoryComboBox()
  {
    return filterRepositoryComboBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getFilterStatusComboBox()
  {
    return filterStatusComboBox;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getFilterApplyButton()
  {
    return filterApplyButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getFilterResetButton()
  {
    return filterResetButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DetailsView getDetailsView()
  {
    return detailsView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getRequirementDetailsWindow()
  {
    return requirementDetailsWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getFilterTypeComboBox()
  {
    return filterTypeComboBox;
  }

}
