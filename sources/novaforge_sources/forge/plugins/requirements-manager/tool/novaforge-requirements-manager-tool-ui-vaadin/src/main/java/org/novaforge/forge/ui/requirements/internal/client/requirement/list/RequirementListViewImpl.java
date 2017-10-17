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
package org.novaforge.forge.ui.requirements.internal.client.requirement.list;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.requirements.internal.client.containers.TreeItemProperty;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.client.requirement.details.DetailsView;
import org.novaforge.forge.ui.requirements.internal.client.requirement.details.DetailsViewImpl;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class RequirementListViewImpl extends HorizontalSplitPanel implements RequirementListView
{

  /**
   * Serialization id
   */
  private static final long      serialVersionUID               = -7934563384728725731L;
  /**
   * The DetailsView associated
   */
  private final DetailsView      detailsView                    = new DetailsViewImpl();
  /**
   * The requirement title label
   */
  private final Label            requirementsTitle              = new Label();
  /**
   * The requirement filters form
   */
  private final Form             requirementsFiltersForm        = new Form();
  /**
   * The requirement filters layout
   */
  private final HorizontalLayout requirementsFiltersLayout      = new HorizontalLayout();
  /**
   * The requirement filter Textfield
   */
  private final TextField        requirementsFilterText         = new TextField();
  /**
   * The requirement apply filters button
   */
  private final Button           requirementsFilterButton       = new Button();
  /**
   * The requirement reset filters button
   */
  private final Button           requirementsResetFilterButton  = new Button();
  /**
   * The requirement tree collapse button
   */
  private final Button           requirementsTreeCollapseButton = new Button();
  /**
   * The requirement tree expand button
   */
  private final Button           requirementsTreeExpandButton   = new Button();
  /**
   * The requirements Tree
   */
  private final Tree             requirementsTree               = new Tree();

  /**
   * Default constructor.
   */
  public RequirementListViewImpl()
  {
    final Component requirementsList = initFirstComponent();
    showDetailsLayout(false);
    setFirstComponent(requirementsList);
    setSecondComponent(detailsView);
    setStyleName(Reindeer.SPLITPANEL_SMALL);
    addStyleName(NovaForge.SPLITPANEL_FIRST_SCROLLABLE);
  }

  /**
   * Initialize the first {@link Component}
   *
   * @return The initialized Component
   */
  private Component initFirstComponent()
  {
    final VerticalLayout firstLayout = new VerticalLayout();
    firstLayout.setMargin(true);
    firstLayout.setSpacing(true);

    requirementsTitle.setStyleName(NovaForge.LABEL_H2);

    FormLayout requirementsFiltersFormLayout = (FormLayout) requirementsFiltersForm.getLayout();
    requirementsFiltersFormLayout.setSpacing(true);

    HorizontalLayout requirementsTreeButtonsLayout = new HorizontalLayout();
    requirementsTreeButtonsLayout.setMargin(new MarginInfo(false, true, false, false));
    requirementsTreeCollapseButton.setIcon(new ThemeResource(NovaForgeResources.ICON_COLLAPSE_SMALL));
    requirementsTreeCollapseButton.setStyleName(NovaForge.BUTTON_IMAGE);
    requirementsTreeExpandButton.setIcon(new ThemeResource(NovaForgeResources.ICON_EXPAND_SMALL));
    requirementsTreeExpandButton.setStyleName(NovaForge.BUTTON_IMAGE);
    requirementsTreeButtonsLayout.addComponent(requirementsTreeExpandButton);
    requirementsTreeButtonsLayout.addComponent(requirementsTreeCollapseButton);
    requirementsTreeButtonsLayout.setComponentAlignment(requirementsTreeExpandButton, Alignment.TOP_RIGHT);
    requirementsTreeButtonsLayout.setComponentAlignment(requirementsTreeCollapseButton, Alignment.TOP_RIGHT);

    VerticalLayout filterTextLayout = new VerticalLayout();
    filterTextLayout.addComponent(requirementsFilterText);
    filterTextLayout.addComponent(requirementsResetFilterButton);

    requirementsFilterButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    requirementsFilterButton.setIcon(new ThemeResource(NovaForgeResources.ICON_FILTER));
    requirementsResetFilterButton.setStyleName(NovaForge.BUTTON_LINK);
    requirementsFiltersLayout.setSpacing(true);
    requirementsFiltersLayout.setMargin(false);
    requirementsFiltersLayout.addComponent(filterTextLayout);
    requirementsFiltersLayout.addComponent(requirementsFilterButton);

    requirementsTree.setImmediate(true);
    requirementsTree.setItemCaptionPropertyId(TreeItemProperty.LABEL.getPropertyId());
    requirementsTree.setItemDescriptionGenerator(new ItemDescriptionGenerator()
    {

      /**
       *
       */
      private static final long serialVersionUID = 6205063098466561925L;

      @Override
      public String generateDescription(Component source, Object itemId, Object propertyId)
      {
        // TODO Auto-generated method stub
        return ((String) ((Tree) source).getItem(itemId)
            .getItemProperty(TreeItemProperty.DESCRIPTION.getPropertyId()).getValue());
      }
    });
    requirementsTree.setItemCaptionMode(ItemCaptionMode.PROPERTY);

    requirementsFiltersFormLayout.addComponent(requirementsFiltersLayout);

    final VerticalLayout treeLayout = new VerticalLayout();
    treeLayout.setMargin(false);
    treeLayout.setSpacing(false);
    treeLayout.setWidth(100, Unit.PERCENTAGE);
    treeLayout.addComponent(requirementsTreeButtonsLayout);
    treeLayout.addComponent(requirementsTree);
    treeLayout.setComponentAlignment(requirementsTreeButtonsLayout, Alignment.TOP_LEFT);
    treeLayout.setComponentAlignment(requirementsTree, Alignment.TOP_LEFT);
    treeLayout.setExpandRatio(requirementsTree, 1);

    firstLayout.addComponent(requirementsTitle);
    firstLayout.addComponent(requirementsFiltersForm);
    firstLayout.addComponent(treeLayout);
    firstLayout.setComponentAlignment(requirementsTitle, Alignment.TOP_LEFT);
    firstLayout.setComponentAlignment(requirementsFiltersForm, Alignment.TOP_LEFT);
    firstLayout.setComponentAlignment(treeLayout, Alignment.TOP_LEFT);

    return firstLayout;
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
    requirementsTitle.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.GLOBAL_MENU_LIST));
    requirementsFiltersForm.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.FILTER_REQUIREMENTS_FORM));
    requirementsFilterText.setInputPrompt(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.FILTER_REQUIREMENTS_INPUTPROMPT));
    requirementsFilterText.setDescription(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.LIST_FILTER_REQUIREMENTS_DESC));
    requirementsFilterButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.FROM_FILTER_SUBMIT));
    requirementsTreeCollapseButton.setDescription(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.LIST_COLLAPSE_TOOLTIP));
    requirementsTreeExpandButton.setDescription(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.LIST_EXPAND_TOOLTIP));
    requirementsResetFilterButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.FROM_FILTER_RESET));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Tree getRequirementsTree()
  {
    return requirementsTree;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showDetailsLayout(final boolean pIsVisible)
  {
    if (pIsVisible)
    {
      if (getSplitPosition() == 0)
      {
        setSplitPosition(40, Unit.PERCENTAGE, true);
      }
      setLocked(false);
    }
    else
    {
      setSplitPosition(0, true);
      setLocked(true);
    }
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
  public TextField getRequirementsFilterText()
  {
    return requirementsFilterText;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequirementsFilterButton()
  {
    return requirementsFilterButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequirementsTreeCollapseButton()
  {
    return requirementsTreeCollapseButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequirementsTreeExpandButton()
  {
    return requirementsTreeExpandButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequirementsResetFilterButton()
  {
    return requirementsResetFilterButton;
  }

}