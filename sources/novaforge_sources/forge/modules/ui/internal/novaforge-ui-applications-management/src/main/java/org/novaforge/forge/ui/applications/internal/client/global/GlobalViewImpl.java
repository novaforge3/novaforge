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
package org.novaforge.forge.ui.applications.internal.client.global;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class GlobalViewImpl extends HorizontalSplitPanel implements GlobalView
{

  /**
   * Serialization id
   */
  private static final long    serialVersionUID       = -569936718818055045L;
  /**
   * The split default position in percent
   */
  private static final int     SPLIT_POSITION_DEFAULT = 20;
  /**
   * The project tabsheet used to display project spaces and applicaations
   */
  private final Tree  tree;
  /**
   * Contains the title used for the default view on the right
   */
  private final Label title;

  /**
   * Contains the description used for the default view on the right
   */
  private final Label description;

  /**
   * Contains the default view on the right
   */
  private final VerticalLayout defaultRight;

  /**
   * The button on the top of the tree
   */
  private final Button addButton;

  /**
   * Default constructor.
   */
  public GlobalViewImpl()
  {
    // Init left content
    final CssLayout layout = new CssLayout();
    layout.setStyleName(NovaForge.SIDEBAR);
    layout.setSizeFull();
    final VerticalLayout vertical = new VerticalLayout();
    vertical.setMargin(new MarginInfo(true, false, true, true));
    vertical.setSizeUndefined();

    addButton = new Button();
    addButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    addButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));

    tree = new Tree();
    vertical.addComponent(addButton);
    vertical.setExpandRatio(addButton, 0);
    vertical.setComponentAlignment(addButton, Alignment.TOP_LEFT);
    vertical.addComponent(tree);
    vertical.setExpandRatio(tree, 1);
    vertical.setComponentAlignment(tree, Alignment.TOP_LEFT);
    layout.addComponent(vertical);

    defaultRight = new VerticalLayout();
    defaultRight.setSpacing(true);
    defaultRight.setMargin(true);
    title = new Label();
    title.setStyleName(Reindeer.LABEL_H2);
    description = new Label();
    defaultRight.setSpacing(true);
    defaultRight.setSizeFull();
    defaultRight.addComponent(title);
    defaultRight.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
    defaultRight.addComponent(description);
    defaultRight.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
    defaultRight.setExpandRatio(description, 1);

    // Init global view
    setFirstComponent(layout);
    setSecondComponent(defaultRight);
    setSplitPosition(SPLIT_POSITION_DEFAULT, Unit.PERCENTAGE);
    setStyleName(Reindeer.SPLITPANEL_SMALL);
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
  public Button getAddButton()
  {
    return addButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Tree getProjectTree()
  {
    return tree;
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
  public VerticalLayout getDefaultRight()
  {
    return defaultRight;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    addButton.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale, Messages.APPLICATIONS_TREE_CREATE));
    tree.setCaption(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                      Messages.APPLICATIONS_TREE_APPLICATIONS));
    title.setDescription(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.APPLICATIONS_TREE_APPLICATIONS_DESCRIPTION));
    title.setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale, Messages.APPLICATIONS_GLOBAL_TITLE));
    description.setValue(ApplicationsModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.APPLICATIONS_GLOBAL_DESCRIPTION));
  }

}
