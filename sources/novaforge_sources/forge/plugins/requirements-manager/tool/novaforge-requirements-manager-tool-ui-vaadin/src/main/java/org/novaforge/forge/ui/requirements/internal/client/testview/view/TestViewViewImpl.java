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
package org.novaforge.forge.ui.requirements.internal.client.testview.view;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.requirements.internal.client.containers.TestViewItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.client.requirement.details.DetailsView;
import org.novaforge.forge.ui.requirements.internal.client.requirement.details.DetailsViewImpl;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class TestViewViewImpl extends VerticalLayout implements TestViewView
{

  /**
   * Serialization id
   */
  private static final long serialVersionUID         = -4420222948069676402L;

  /**
   * Contains the title used for the default view
   */
  private final Label       title                    = new Label();
  /**
   * Filters form
   */
  private final Form        filtersForm              = new Form();
  /**
   * Filter requirement TextField
   */
  private final TextField   filterRequirementField   = new TextField();
  /**
   * Filter repository ComboBox
   */
  private final ComboBox    filterRepositoryComboBox = new ComboBox();
  /**
   * Filter status ComboBox
   */
  private final ComboBox    filterStatusComboBox     = new ComboBox();
  /**
   * Filter type ComboBox
   */
  private final ComboBox    filterTypeComboBox       = new ComboBox();
  /**
   * Button apply filters
   */
  private final Button      filterApplyButton        = new Button();
  /**
   * Button reset filters
   */
  private final Button      filterResetButton        = new Button();
  /**
   * Requirements Table
   */
  private final TreeTable   dataTable                = new TreeTable();
  /**
   * Requirement details window
   */
  private final Window      requirementDetailsWindow = new Window();
  /**
   * Requirement details view
   */
  private final DetailsView detailsView              = new DetailsViewImpl();

  /**
   * Default constructor.
   */
  public TestViewViewImpl()
  {
    setMargin(true);
    title.setStyleName(NovaForge.LABEL_H2);
    final Component filtersLayout = initFilters();
    final Component tableLayout = initTable();
    addComponent(title);
    setComponentAlignment(title, Alignment.TOP_LEFT);
    addComponent(filtersLayout);
    setComponentAlignment(filtersLayout, Alignment.TOP_CENTER);
    addComponent(tableLayout);
    setComponentAlignment(tableLayout, Alignment.TOP_CENTER);

    setExpandRatio(tableLayout, 1);

    initPopups();
  }

  /**
   * Initialize filters
   *
   * @return the initialized filters component
   */
  private Component initFilters()
  {
    HorizontalLayout filtersLayout = new HorizontalLayout();
    filtersLayout.setSpacing(true);
    VerticalLayout filterIDLayout = new VerticalLayout();
    filterRequirementField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    filterApplyButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    filterApplyButton.setIcon(new ThemeResource(NovaForgeResources.ICON_FILTER));
    filterResetButton.setStyleName(NovaForge.BUTTON_LINK);
    filterIDLayout.addComponent(filterRequirementField);
    filterIDLayout.addComponent(filterResetButton);

    filterRepositoryComboBox.setWidth(NovaForge.FORM_FIELD_WIDTH);
    filterStatusComboBox.setWidth(NovaForge.FORM_FIELD_WIDTH);
    filterTypeComboBox.setWidth(NovaForge.FORM_FIELD_WIDTH);

    filtersLayout.addComponent(filterIDLayout);
    filtersLayout.addComponent(filterRepositoryComboBox);
    filtersLayout.addComponent(filterTypeComboBox);
    filtersLayout.addComponent(filterStatusComboBox);
    filtersLayout.addComponent(filterApplyButton);

    filtersForm.getLayout().addComponent(filtersLayout);
    return filtersForm;
  }

  /**
   * Initialize the requirement table
   *
   * @return the initialized component
   */
  private Component initTable()
  {
    dataTable.setImmediate(true);
    dataTable.setWidth(100, Unit.PERCENTAGE);
    dataTable.setStyleName(Reindeer.TABLE_STRONG);
    return dataTable;
  }

  /**
   * Initialize the popups of the view
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
    title.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.GLOBAL_MENU_TESTS));
    filtersForm.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
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

    dataTable.setColumnHeader(TestViewItemProperty.REQUIREMENT_NAME.getPropertyId(), RequirementsModule
        .getPortalMessages().getMessage(pLocale, Messages.TESTVIEW_TABLE_HEADER_REQUIREMENT));
    dataTable.setColumnHeader(TestViewItemProperty.VERSION_IN_REQUIREMENT.getPropertyId(), RequirementsModule
        .getPortalMessages().getMessage(pLocale, Messages.REQUIREMENT_FIELD_VERSION));
    dataTable.setColumnHeader(TestViewItemProperty.TEST_NAME.getPropertyId(), RequirementsModule
        .getPortalMessages().getMessage(pLocale, Messages.TESTVIEW_TABLE_HEADER_IDTEST));
    dataTable.setColumnHeader(TestViewItemProperty.VERSION_IN_TEST.getPropertyId(), RequirementsModule
        .getPortalMessages().getMessage(pLocale, Messages.TESTVIEW_TABLE_HEADER_VERSIONTEST));

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
  public TreeTable getDataTable()
  {
    return dataTable;
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
