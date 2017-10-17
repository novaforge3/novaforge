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
package org.novaforge.forge.ui.portal.client.component;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class SideBarLayout extends HorizontalLayout
{

  /**
   * Serialization id
   */
  private static final long                serialVersionUID = -569936718818055045L;
  private final        Map<String, Button> menuButton       = new HashMap<String, Button>();
  private final CssLayout secondComponent;
  private       Label     title;
  private       CssLayout menuLayout;

  /**
   * Default constructor.
   */
  public SideBarLayout()
  {
    setSizeFull();

    // Init left content
    final VerticalLayout sideBar = new VerticalLayout();
    sideBar.setStyleName(NovaForge.SIDEBAR);
    sideBar.setWidth(null);
    sideBar.setHeight(100, Unit.PERCENTAGE);

    // Init title
    final Component initTitle = initTitle();
    sideBar.addComponent(initTitle);

    // Init menu
    final Component menu = initMenu();
    sideBar.addComponent(menu);
    sideBar.setExpandRatio(menu, 1);

    // Init second component
    secondComponent = new CssLayout();
    secondComponent.setSizeFull();
    secondComponent.setStyleName(NovaForge.SIDEBAR_SECOND_COMPONENT);

    // Init global view
    addComponent(sideBar);
    setExpandRatio(sideBar, 0);
    addComponent(secondComponent);
    setExpandRatio(secondComponent, 1);
  }

  private Component initTitle()
  {
    final CssLayout titleLayout = new CssLayout();
    titleLayout.addStyleName(NovaForge.SIDEBAR_BRANDING);
    title = new Label();
    title.setSizeUndefined();
    titleLayout.addComponent(title);
    return titleLayout;
  }

  private Component initMenu()
  {
    menuLayout = new CssLayout();
    menuLayout.addStyleName(NovaForge.SIDEBAR_MENU);
    menuLayout.setHeight(100, Unit.PERCENTAGE);
    return menuLayout;
  }

  /**
   * Add a new button to the side bar menu
   *
   * @param pKey
   *     the key to identifier the button
   * @param pResource
   *     the resource to display
   *
   * @return {@link Button} added
   */
  public Button addButton(final String pKey, final Resource pResource)
  {
    final NativeButton button = new NativeButton();
    button.setIcon(pResource);
    button.addClickListener(new MenuButtonListener());
    menuLayout.addComponent(button);
    menuButton.put(pKey, button);
    return button;
  }

  /**
   * Remove a button using the key given
   *
   * @param pKey
   *     the key to identifier the button
   */
  public void removeButton(final String pKey)
  {
    if (menuButton.containsKey(pKey))
    {
      final Button button = menuButton.get(pKey);
      menuLayout.removeComponent(button);
      menuButton.remove(pKey);
    }
  }

  /**
   * Returns the mapping of buttons
   *
   * @return the list of button
   */
  public Map<String, Button> getButtons()
  {
    return menuButton;
  }

  /**
   * Set the second component with the
   *
   * @param pComponent
   */
  public void setSecondComponent(final Component pComponent)
  {
    secondComponent.removeAllComponents();
    secondComponent.addComponent(pComponent);
  }

  /**
   * Retrieve the label used for title
   *
   * @return the title
   */
  public Label getTitle()
  {
    return title;
  }

  /**
   * Default {@link ClickListener} for native button in the menu bar
   *
   * @author Guillaume Lamirand
   */
  private final class MenuButtonListener implements ClickListener
  {
    /**
     * Serial version id
     */
    private static final long serialVersionUID = -1787889800194050338L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void buttonClick(final ClickEvent event)
    {
      for (final Component next : menuLayout)
      {
        if (next instanceof NativeButton)
        {
          next.removeStyleName(NovaForge.SELECTED);
        }
      }
      event.getButton().addStyleName(NovaForge.SELECTED);

    }
  }

}
