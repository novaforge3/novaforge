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
package org.novaforge.forge.ui.portal.internal.privatemodule.client.project;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.portal.client.util.DebugIdGenerator;
import org.novaforge.forge.ui.portal.internal.privatemodule.module.PrivateModule;

/**
 * @author Guillaume Lamirand
 */
public class ProjectTabViewImpl extends HorizontalSplitPanel implements ProjectTabView
{

  /**
   * Serialization id
   */
  private static final long   serialVersionUID       = -569936718818055045L;
  /**
   * The split default position
   */
  private static final int SPLIT_POSITION_DEFAULT = 20;
  /**
   * The project image
   */
  private final Embedded                                                         projectImage;
  /**
   * The project name label
   */
  private final Label                                                            projectName;
  /**
   * The project description label
   */
  private final Label                                                            projectDescription;
  /**
   * The project tabsheet used to display a application by tab
   */
  private final Tree                                                             tree;
  /**
   * The project tabsheet used to display a application by tab
   */
  private final org.novaforge.forge.ui.portal.client.component.TabsheetWithState tabSheet;

  /**
   * Default constructor.
   */
  public ProjectTabViewImpl()
  {
    final VerticalLayout leftContent = new VerticalLayout();
    leftContent.setWidth(null);
    leftContent.setMargin(new MarginInfo(true, false, true, true));
    leftContent.setSpacing(true);
    final HorizontalLayout horiLayout = new HorizontalLayout();
    horiLayout.setSpacing(true);
    horiLayout.setMargin(new MarginInfo(false, false, true, false));
    projectImage = new Embedded();
    projectImage.setWidth(100, Unit.PIXELS);
    projectImage.setHeight(100, Unit.PIXELS);
    horiLayout.addComponent(projectImage);
    horiLayout.setComponentAlignment(projectImage, Alignment.MIDDLE_LEFT);

    projectName = new Label();
    projectName.setContentMode(ContentMode.HTML);
    projectName.setStyleName(Reindeer.LABEL_H2);
    horiLayout.addComponent(projectName);
    horiLayout.setComponentAlignment(projectName, Alignment.MIDDLE_LEFT);

    projectDescription = new Label();
    projectDescription.setContentMode(ContentMode.HTML);
    projectDescription.addStyleName(NovaForge.PRIVATE_PROJECT_DESCRIPTION);

    tree = new Tree();
    tree.setId(DebugIdGenerator.getDynamicComponentId(PrivateModule.getPortalModuleId(), "tree"));
    leftContent.addComponent(horiLayout);
    leftContent.addComponent(projectDescription);
    leftContent.addComponent(tree);
    leftContent.setExpandRatio(horiLayout, 0);
    leftContent.setComponentAlignment(horiLayout, Alignment.TOP_LEFT);
    leftContent.setExpandRatio(projectDescription, 0);
    leftContent.setComponentAlignment(projectDescription, Alignment.TOP_LEFT);
    leftContent.setExpandRatio(tree, 1);
    leftContent.setComponentAlignment(tree, Alignment.TOP_LEFT);
    // Init application tabsheet
    tabSheet = new org.novaforge.forge.ui.portal.client.component.TabsheetWithState();
    tabSheet.setSizeFull();
    tabSheet.setStyleName(NovaForge.TABSHEET_APPLICATION);

    // Init global view
    resetSplitPanel();
    setFirstComponent(leftContent);
    setSecondComponent(tabSheet);
  }

  @Override
  public void attach()
  {
    super.attach();
    tree.setCaption(PrivateModule.getPortalMessages().getMessage(getLocale(), Messages.PROJECT_NAVIGATION));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public org.novaforge.forge.ui.portal.client.component.TabsheetWithState getApplicationTab()
  {
    return tabSheet;
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
  public Embedded getProjectImage()
  {
    return projectImage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getProjectName()
  {
    return projectName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getProjectDescription()
  {
    return projectDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void resetSplitPanel()
  {
    setSplitPosition(SPLIT_POSITION_DEFAULT, Unit.PERCENTAGE);

  }

}
