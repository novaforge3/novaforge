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
package org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.vaadin.addon.itemlayout.vertical.ItemVertical;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class ApplicationsSourceViewImpl extends VerticalLayout implements ApplicationsSourceView
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 3529814710722435066L;
  private TextField         filter;
  private ComboBox          projects;
  private VerticalLayout    contentSourceLayout;
  private Label             filterLabel;
  private Label             projectsLabel;
  private ItemVertical      selectedList;
  private Label             sourcesSelectedTitle;

  /**
   * Default constructor
   */
  public ApplicationsSourceViewImpl()
  {
    setSizeFull();
    setMargin(true);

    final Component header = initHeader();
    final Component content = initContent();

    addComponent(header);
    setExpandRatio(header, 0);
    addComponent(content);
    setExpandRatio(content, 1);
  }

  private Component initHeader()
  {
    final HorizontalLayout layout = new HorizontalLayout();
    layout.setSpacing(true);
    layout.setWidth(100, Unit.PERCENTAGE);
    layout.setStyleName(NovaForge.DASHBOARD_SETTINGS_HEADER);

    final HorizontalLayout filterLayout = new HorizontalLayout();
    filterLayout.setSpacing(true);
    filterLabel = new Label();
    filterLabel.setStyleName(NovaForge.LABEL_BOLD);
    filter = new TextField();
    filter.setImmediate(true);
    filterLayout.addComponent(filterLabel);
    filterLayout.addComponent(filter);
    filterLayout.setComponentAlignment(filterLabel, Alignment.MIDDLE_CENTER);
    filterLayout.setComponentAlignment(filter, Alignment.MIDDLE_CENTER);

    final HorizontalLayout projectsLayout = new HorizontalLayout();
    projectsLayout.setSpacing(true);
    projectsLabel = new Label();
    projectsLabel.setStyleName(NovaForge.LABEL_BOLD);
    projects = new ComboBox();
    projects.setImmediate(true);
    projects.setTextInputAllowed(true);
    projectsLayout.addComponent(projectsLabel);
    projectsLayout.addComponent(projects);
    projectsLayout.setComponentAlignment(projectsLabel, Alignment.MIDDLE_CENTER);
    projectsLayout.setComponentAlignment(projects, Alignment.MIDDLE_CENTER);

    layout.addComponent(filterLayout);
    layout.addComponent(projectsLayout);
    layout.setComponentAlignment(filterLayout, Alignment.MIDDLE_RIGHT);
    layout.setComponentAlignment(projectsLayout, Alignment.MIDDLE_LEFT);
    return layout;
  }

  private Component initContent()
  {
    final HorizontalLayout contentLayout = new HorizontalLayout();
    contentLayout.setSizeFull();
    contentLayout.setSpacing(true);
    // Source Layout
    final Panel sourcePanel = new Panel();
    sourcePanel.setSizeFull();
    sourcePanel.addStyleName(NovaForge.PANEL_LIGHT);
    sourcePanel.addStyleName(NovaForge.DASHBOARD_SETTINGS_CONTENT);
    contentSourceLayout = new VerticalLayout();
    sourcePanel.setContent(contentSourceLayout);
    contentLayout.addComponent(sourcePanel);
    // Selected Layout
    final Component selectedLayout = initSelected();
    contentLayout.addComponent(selectedLayout);
    contentLayout.setExpandRatio(sourcePanel, 1);
    return contentLayout;
  }

  private Component initSelected()
  {
    final VerticalLayout selectedLayout = new VerticalLayout();
    selectedLayout.setWidth(300, Unit.PIXELS);
    selectedLayout.setHeight(100, Unit.PERCENTAGE);
    selectedLayout.addStyleName(NovaForge.DASHBOARD_SETTINGS_SELECTED_BOX);
    // Selected Title
    final HorizontalLayout selectedTitleLayout = new HorizontalLayout();
    selectedTitleLayout.setMargin(new MarginInfo(false, false, false, true));
    selectedTitleLayout.setWidth(100, Unit.PERCENTAGE);
    selectedTitleLayout.setHeight(30, Unit.PIXELS);
    selectedTitleLayout.setStyleName(NovaForge.DASHBOARD_SETTINGS_SELECTED_BOX_TITLE);
    sourcesSelectedTitle = new Label();
    selectedTitleLayout.addComponent(sourcesSelectedTitle);
    selectedTitleLayout.setComponentAlignment(sourcesSelectedTitle, Alignment.MIDDLE_LEFT);
    selectedLayout.addComponent(selectedTitleLayout);
    // Selected list
    selectedList = new ItemVertical();
    selectedList.setWidth(100, Unit.PERCENTAGE);
    selectedList.setHeight(100, Unit.PERCENTAGE);
    selectedList.setSelectable(false);
    selectedLayout.addComponent(selectedList);
    // Alignement
    selectedLayout.setExpandRatio(selectedTitleLayout, 0);
    selectedLayout.setExpandRatio(selectedList, 1);
    selectedLayout.setComponentAlignment(selectedList, Alignment.TOP_CENTER);
    selectedLayout.setComponentAlignment(selectedTitleLayout, Alignment.TOP_CENTER);
    return selectedLayout;
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
    final PortalMessages portalMessages = DashboardModule.getPortalMessages();
    filterLabel.setValue(portalMessages.getMessage(pLocale, Messages.DASHBOARD_SETTINGS_SOURCE_FILTER_APPS));
    projectsLabel.setValue(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_SETTINGS_SOURCE_FILTER_PROJECTS));
    projects.setInputPrompt(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_SETTINGS_SOURCE_FILTER_PROJECTS_INPUT));
    sourcesSelectedTitle.setValue(portalMessages.getMessage(pLocale,
        Messages.DASHBOARD_SETTINGS_SOURCE_SELECTED_TITLE));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public VerticalLayout getContentLayout()
  {
    return contentSourceLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getNameFilter()
  {
    return filter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getProjectsBox()
  {
    return projects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ItemVertical getSourcesSelected()
  {
    return selectedList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getSourcesSelectedTitle()
  {
    return sourcesSelectedTitle;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDashBoardType(final DashBoard.Type pType)
  {
    final boolean isUser = DashBoard.Type.USER.equals(pType);
    projectsLabel.setVisible(isUser);
    projects.setVisible(isUser);
  }

}
