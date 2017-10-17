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
package org.novaforge.forge.ui.user.management.internal.client.components;

import com.vaadin.data.Item;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import org.vaadin.addon.itemlayout.layout.model.ItemGenerator;

/**
 * @author caseryj
 */
public class ProjectComponentGenerator implements ItemGenerator
{
  /**
   * 
   */
  private static final long serialVersionUID  = -2998921555948545299L;

  /**
   * Define project icon size
   */
  private static final long PROJECT_ICON_SIZE = 50;

  /**
   * {@inheritDoc}
   */
  @Override
  public Component generateItem(final AbstractItemLayout pSource, final Object pItemId)
  {
    final Item item = pSource.getContainerDataSource().getItem(pItemId);

    final VerticalLayout projectLayout = new VerticalLayout();
    projectLayout.setStyleName(NovaForge.CURSOR_BUTTON);
    // Icon
    final Resource projectIconResource = (Resource) item.getItemProperty(
        ProjectItemProperty.ICON.getPropertyId()).getValue();
    final Image projectIcon = new Image();
    projectIcon.setSource(projectIconResource);
    projectIcon.addStyleName(NovaForge.CURSOR_BUTTON);
    projectIcon.setWidth(PROJECT_ICON_SIZE, Unit.PIXELS);
    projectIcon.setHeight(PROJECT_ICON_SIZE, Unit.PIXELS);

    // Name
    final String projectName = (String) item.getItemProperty(ProjectItemProperty.NAME.getPropertyId())
        .getValue();
    final Label projectLabel = new Label(projectName);
    projectLabel.setSizeUndefined();
    projectLabel.setStyleName(NovaForge.LABEL_GREEN);
    projectLabel.addStyleName(NovaForge.CURSOR_BUTTON);

    // Description
    final String projectDescription = (String) item.getItemProperty(
        ProjectItemProperty.DESCRIPTION.getPropertyId()).getValue();
    projectLayout.setDescription(projectDescription);

    projectLayout.addComponent(projectIcon);
    projectLayout.addComponent(projectLabel);
    projectLayout.setComponentAlignment(projectIcon, Alignment.MIDDLE_CENTER);
    projectLayout.setComponentAlignment(projectLabel, Alignment.MIDDLE_CENTER);
    projectLayout.setSizeUndefined();
    return projectLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeGenerated(final AbstractItemLayout pSource, final Object pItemId,
      final Object pPropertyId)
  {
    boolean isNeeded = false;

    if ((ProjectItemProperty.ICON.getPropertyId().equals(pPropertyId))
        || (ProjectItemProperty.NAME.getPropertyId().equals(pPropertyId)))
    {
      final Item item = pSource.getContainerDataSource().getItem(pItemId);
      final Object itemIcon = item.getItemProperty(ProjectItemProperty.ICON.getPropertyId()).getValue();
      final Object itemName = item.getItemProperty(ProjectItemProperty.NAME.getPropertyId()).getValue();
      isNeeded = (itemIcon != null) && (itemName != null);
    }
    return isNeeded;
  }
}
