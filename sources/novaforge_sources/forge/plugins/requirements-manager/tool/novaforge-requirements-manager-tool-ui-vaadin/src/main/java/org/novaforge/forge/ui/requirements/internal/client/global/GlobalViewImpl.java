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
package org.novaforge.forge.ui.requirements.internal.client.global;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.requirements.internal.client.i18n.Messages;
import org.novaforge.forge.ui.requirements.internal.module.RequirementsModule;

import java.util.Iterator;
import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class GlobalViewImpl extends HorizontalSplitPanel implements GlobalView
{

  /**
   * Serialization id
   */
  private static final long serialVersionUID       = -1008524082903699749L;
  /**
   * The split default position
   */
  private static final int  SPLIT_POSITION_DEFAULT = 140;
  /**
   * The title {@link Label} of menu
   */
  private Label             title;
  /**
   * The {@link CssLayout} use for menu
   */
  private CssLayout         menuLayout;
  /**
   * The requirement list button in menu
   */
  private Button            requirementListButton;
  /**
   * The requirement codeview button in menu
   */
  private Button            requirementCodeViewButton;
  /**
   * The requirement testview button in menu
   */
  private Button            requirementTestViewButton;
  /**
   * The requirement synchronization button in menu
   */
  private Button            requirementSynchronizationButton;
  /**
   * The requirement repository button in menu
   */
  private Button            requirementRepositoryButton;
  /**
   * The requirement configuration button in menu
   */
  private Button            requirementConfigurationButton;
  /**
   * Default constructor.
   */
  public GlobalViewImpl()
  {
    setSizeFull();

    // Init left content
    final VerticalLayout vertical = new VerticalLayout();
    vertical.setStyleName(NovaForge.SIDEBAR);
    // vertical.setMargin(true, false, true, true);
    vertical.setSizeFull();

    // Init title
    final Component initTitle = initTitle();
    vertical.addComponent(initTitle);

    // Init menu
    final Component menu = initMenu();
    vertical.addComponent(menu);
    vertical.setExpandRatio(menu, 1);

    // Init global view
    setFirstComponent(vertical);
    setSplitPosition(SPLIT_POSITION_DEFAULT, Unit.PIXELS);
    setStyleName(Reindeer.SPLITPANEL_SMALL);
    setLocked(true);
  }

  /**
   * Initialize the title
   *
   * @return the initialized title {@link Component}
   */
  private Component initTitle()
  {
    final CssLayout titleLayout = new CssLayout();
    titleLayout.setStyleName(NovaForge.SIDEBAR_BRANDING);
    title = new Label();
    title.setSizeUndefined();
    titleLayout.addComponent(title);
    return titleLayout;
  }

  /**
   * Initialize the menu
   *
   * @return the initialized menu {@link Component}
   */
  private Component initMenu()
  {
    menuLayout = new CssLayout();
    menuLayout.setStyleName(NovaForge.SIDEBAR_MENU);
    menuLayout.setSizeFull();
    // Define Requirement List button
    requirementListButton = new NativeButton();
    requirementListButton.setIcon(new ThemeResource(NovaForgeResources.ICON_REQUIREMENT));
    requirementListButton.addClickListener(new MenuButtonListener());
    menuLayout.addComponent(requirementListButton);
    // Define Requirement CodeView button
    requirementCodeViewButton = new NativeButton();
    requirementCodeViewButton.setIcon(new ThemeResource(NovaForgeResources.ICON_VIEW_CODE));
    requirementCodeViewButton.addClickListener(new MenuButtonListener());
    menuLayout.addComponent(requirementCodeViewButton);
    // Define Requirement TestsView button
    requirementTestViewButton = new NativeButton();
    requirementTestViewButton.setIcon(new ThemeResource(NovaForgeResources.ICON_VIEW_TEST));
    requirementTestViewButton.addClickListener(new MenuButtonListener());
    menuLayout.addComponent(requirementTestViewButton);
    // Define Requirement Synchronization button
    requirementSynchronizationButton = new NativeButton();
    requirementSynchronizationButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SYNCHRONIZE));
    requirementSynchronizationButton.addClickListener(new MenuButtonListener());
    menuLayout.addComponent(requirementSynchronizationButton);
    // Define Requirement Repository button
    requirementRepositoryButton = new NativeButton();
    requirementRepositoryButton.setIcon(new ThemeResource(NovaForgeResources.ICON_LIST));
    requirementRepositoryButton.addClickListener(new MenuButtonListener());
    menuLayout.addComponent(requirementRepositoryButton);
    // Define Requirement Configuration button
    requirementConfigurationButton = new NativeButton();
    requirementConfigurationButton.setIcon(new ThemeResource(NovaForgeResources.ICON_SETTINGS));
    requirementConfigurationButton.addClickListener(new MenuButtonListener());
    menuLayout.addComponent(requirementConfigurationButton);

    return menuLayout;
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
   * Default {@link ClickListener} for native button in the menu bar
   *
   * @author Jeremy Casery
   */
  private final class MenuButtonListener implements ClickListener
  {
    /**
     * Serial version id
     */
    private static final long serialVersionUID = 5302240481969457265L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void buttonClick(final ClickEvent event)
    {
      for (final Iterator<Component> it = menuLayout.getComponentIterator(); it.hasNext(); )
      {
        final Component next = it.next();
        if (next instanceof NativeButton)
        {
          next.removeStyleName(NovaForge.SELECTED);
        }
      }
      event.getButton().addStyleName(NovaForge.SELECTED);

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showAdministration(final boolean pVisible)
  {
    requirementSynchronizationButton.setVisible(pVisible);
    requirementRepositoryButton.setVisible(pVisible);
    requirementConfigurationButton.setVisible(pVisible);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCodeAvailable(final boolean pIsAvailable)
  {
    requirementCodeViewButton.setEnabled(pIsAvailable);
    refreshLocale(getLocale());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTestAvailable(final boolean pIsAvailable)
  {
    requirementTestViewButton.setEnabled(pIsAvailable);
    refreshLocale(getLocale());
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    title.setValue(RequirementsModule.getPortalMessages().getMessage(pLocale, Messages.GLOBAL_TITLE));
    requirementListButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.GLOBAL_MENU_LIST));
    requirementCodeViewButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.GLOBAL_MENU_CODE));
    requirementTestViewButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.GLOBAL_MENU_TESTS));
    requirementSynchronizationButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.GLOBAL_MENU_SYNCHRONIZATION));
    requirementRepositoryButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.GLOBAL_MENU_REPOSITORY));
    requirementConfigurationButton.setCaption(RequirementsModule.getPortalMessages().getMessage(pLocale,
        Messages.GLOBAL_MENU_CONFIGURATION));
    if (!requirementCodeViewButton.isEnabled())
    {
      requirementCodeViewButton.setDescription(RequirementsModule.getPortalMessages().getMessage(pLocale,
          Messages.CODEVIEW_UNAVAILABLE));
    }
    else
    {
      requirementCodeViewButton.setDescription(null);
    }
    if (!requirementTestViewButton.isEnabled())
    {
      requirementTestViewButton.setDescription(RequirementsModule.getPortalMessages().getMessage(pLocale,
          Messages.TESTVIEW_UNAVAILABLE));
    }
    else
    {
      requirementTestViewButton.setDescription(null);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HorizontalSplitPanel getSplitPanel()
  {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSplitPositionDefault()
  {
    return SPLIT_POSITION_DEFAULT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequirementList()
  {
    return requirementListButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequirementCodeViewButton()
  {
    return requirementCodeViewButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequirementTestViewButton()
  {
    return requirementTestViewButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequirementSynchronizationButton()
  {
    return requirementSynchronizationButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequirementRepositoryButton()
  {
    return requirementRepositoryButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getRequirementConfigurationButton()
  {
    return requirementConfigurationButton;
  }

}
