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
package org.novaforge.forge.ui.portal.internal.header.client;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.internal.header.module.HeaderModule;

import java.util.Locale;

/**
 * This vie define the top element of the portal layout
 * 
 * @author Guillaume Lamirand
 */
public class HeaderViewImpl extends HorizontalLayout implements HeaderView
{

  /**
   * Serial version id used for serialization
   */
  private static final long      serialVersionUID = -569936718818055045L;

  /**
   * Represents the select element with user's project
   */
  private final ComboBox         projectCombo;
  /**
   * Represents a button to open other projects list
   */
  private final Button           otherProject;
  /**
   * Represents the account menu used to open user application
   */
  private final MenuBar          accountMenu;
  /**
   * Contains the account icon
   */
  private final Image            accountIcon;
  /**
   * Contains the project components
   */
  private final VerticalLayout   projects;
  /**
   * Languages layout
   */
  private final HorizontalLayout languages;
  /**
   * Button to switch to english
   */
  private final Button           enButton;
  /**
   * Button to switch to french
   */
  private final Button           frButton;

  /**
   * Default constructor
   */
  public HeaderViewImpl()
  {
    final Image logoNovaForge = new Image();
    logoNovaForge.setSource(new ThemeResource(NovaForgeResources.LOGO_NOVAFORGE));
    logoNovaForge.setAlternateText(Messages.PORTAL_TITLE);
    addComponent(logoNovaForge);

    projects = new VerticalLayout();
    projects.setSizeUndefined();
    projects.setStyleName(NovaForge.HEADER_PROJECT);
    projectCombo = new ComboBox();

    // Set the appropriate filtering mode for this example
    projectCombo.setFilteringMode(FilteringMode.CONTAINS);

    // Disallow null selections
    projectCombo.setImmediate(true);
    projectCombo.setNullSelectionAllowed(false);
    projectCombo.setTextInputAllowed(true);
    projectCombo.setVisible(false);

    // Button to display projets list
    otherProject = new Button();
    otherProject.setStyleName(BaseTheme.BUTTON_LINK);
    otherProject.setVisible(false);
    projects.addComponent(projectCombo);
    projects.setComponentAlignment(projectCombo, Alignment.TOP_CENTER);
    projects.addComponent(otherProject);
    projects.setComponentAlignment(otherProject, Alignment.TOP_CENTER);
    projects.setVisible(false);
    addComponent(projects);
    final VerticalLayout account = new VerticalLayout();
    languages = new HorizontalLayout();

    enButton = new Button();
    enButton.setStyleName(NovaForge.BUTTON_LINK);
    enButton.setIcon(new ThemeResource(NovaForgeResources.FLAG_EN));
    languages.addComponent(enButton);
    frButton = new Button();
    frButton.setStyleName(NovaForge.BUTTON_LINK);
    frButton.setIcon(new ThemeResource(NovaForgeResources.FLAG_FR));
    languages.addComponent(frButton);
    final HorizontalLayout accountMenuLayout = new HorizontalLayout();
    accountMenuLayout.setSpacing(false);
    accountMenuLayout.setMargin(new MarginInfo(false, true, false, false));
    accountMenu = new MenuBar();
    accountMenu.addStyleName(NovaForge.MENUBAR_LIGHT);
    accountMenu.setVisible(false);
    accountIcon = new Image();
    accountIcon.setVisible(false);
    accountIcon.setImmediate(true);
    accountIcon.setWidth(HeaderView.ACCOUNT_ICON_SIZE, Unit.PIXELS);
    accountIcon.setHeight(HeaderView.ACCOUNT_ICON_SIZE, Unit.PIXELS);
    accountIcon.setStyleName(NovaForge.HEADER_USER_ICON);
    accountMenuLayout.addComponent(accountMenu);
    accountMenuLayout.setComponentAlignment(accountMenu, Alignment.MIDDLE_RIGHT);
    accountMenuLayout.addComponent(accountIcon);
    accountMenuLayout.setComponentAlignment(accountIcon, Alignment.MIDDLE_RIGHT);
    account.addComponent(languages);
    account.setComponentAlignment(languages, Alignment.MIDDLE_RIGHT);
    account.addComponent(accountMenuLayout);
    account.setComponentAlignment(accountMenuLayout, Alignment.MIDDLE_RIGHT);
    addComponent(account);

    // Set up layout
    setExpandRatio(logoNovaForge, 1f);
    setComponentAlignment(logoNovaForge, Alignment.MIDDLE_LEFT);
    setExpandRatio(projects, 1f);
    setComponentAlignment(projects, Alignment.TOP_CENTER);
    setExpandRatio(account, 1f);
    setComponentAlignment(account, Alignment.MIDDLE_RIGHT);
    setSizeFull();
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
  public Button getOtherProject()
  {
    return otherProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ComboBox getProjectCombo()
  {
    return projectCombo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MenuBar getAccountMenu()
  {
    return accountMenu;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Layout getLanguagesLayout()
  {
    return languages;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Layout getProjectsLayout()
  {
    return projects;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getEnButton()
  {
    return enButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getFrButton()
  {
    return frButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {

    final PortalMessages portalMessages = HeaderModule.getPortalMessages();
    projectCombo.setDescription(portalMessages.getMessage(pLocale, Messages.PROJECT_SELECT_DESCRIPTION));
    projectCombo.setInputPrompt(portalMessages.getMessage(pLocale, Messages.PROJECT_SELECT_INPUT));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Image getAccountIcon()
  {
    return accountIcon;
  }
}
