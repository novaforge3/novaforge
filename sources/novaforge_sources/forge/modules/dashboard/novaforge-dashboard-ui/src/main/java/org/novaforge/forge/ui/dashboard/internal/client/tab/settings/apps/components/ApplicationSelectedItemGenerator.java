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
package org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps.components;

import com.google.common.base.Strings;
import com.vaadin.data.Item;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.Resource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.dashboard.internal.client.container.ApplicationItemProperty;
import org.novaforge.forge.ui.dashboard.internal.client.tab.settings.apps.ApplicationsSourcePresenter;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.event.OpenProjectEvent;
import org.vaadin.addon.itemlayout.layout.AbstractItemLayout;
import org.vaadin.addon.itemlayout.layout.model.ItemGenerator;

import java.util.UUID;

/**
 * @author Guillaume Lamirand
 */
public class ApplicationSelectedItemGenerator implements ItemGenerator
{

  /**
   * 
   */
  private static final long                 serialVersionUID = 7730429874149600097L;

  private static final int                  ICON_SIZE        = 16;

  private final ApplicationsSourcePresenter presenter;

  public ApplicationSelectedItemGenerator(final ApplicationsSourcePresenter pPresenter)
  {
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component generateItem(final AbstractItemLayout pSource, final Object pItemId)
  {
    final Item item = pSource.getContainerDataSource().getItem(pItemId);
    final String name = (String) item.getItemProperty(ApplicationItemProperty.NAME.getPropertyId())
        .getValue();
    final String desc = (String) item.getItemProperty(ApplicationItemProperty.DESCRIPTION.getPropertyId())
        .getValue();
    final Resource icon = (Resource) item.getItemProperty(ApplicationItemProperty.ICON.getPropertyId())
        .getValue();
    final String projectID = (String) item
        .getItemProperty(ApplicationItemProperty.PROJECT_ID.getPropertyId()).getValue();
    final String projectName = (String) item.getItemProperty(
        ApplicationItemProperty.PROJECT_NAME.getPropertyId()).getValue();
    final Resource projectIcon = (Resource) item.getItemProperty(
        ApplicationItemProperty.PROJECT_ICON.getPropertyId()).getValue();

    final HorizontalLayout boxLayout = new HorizontalLayout();
    boxLayout.setStyleName(NovaForge.DASHBOARD_SETTINGS_SELECTED_BOX_ITEM);
    boxLayout.setWidth(280, Unit.PIXELS);
    boxLayout.setHeight(20, Unit.PIXELS);
    boxLayout.setSpacing(true);

    if ((!Strings.isNullOrEmpty(name)) && (icon != null) && (!Strings.isNullOrEmpty(projectID)) && (!Strings
                                                                                                         .isNullOrEmpty(projectName))
        && (projectIcon != null))
    {
      boxLayout.setDescription(desc);

      final HorizontalLayout labelLayout = new HorizontalLayout();
      labelLayout.setSpacing(true);
      // Project button
      final Button projectButton = new Button();
      projectButton.setCaption(projectName);
      projectButton.setIcon(projectIcon);
      projectButton.setStyleName(NovaForge.BUTTON_LINK);
      projectButton.setDescription(DashboardModule.getPortalMessages().getMessage(
          UI.getCurrent().getLocale(), Messages.EVENT_OPEN_PROJECT, projectName));
      projectButton.addClickListener(new Button.ClickListener()
      {

        /**
           * 
           */
        private static final long serialVersionUID = 6441461561668373590L;

        @Override
        public void buttonClick(final com.vaadin.ui.Button.ClickEvent event)
        {
          presenter.getPortalContext().getEventBus().publish(new OpenProjectEvent(projectID));
        }
      });
      final Label sepLabel = new Label(">");

      labelLayout.addComponent(projectButton);
      labelLayout.setComponentAlignment(projectButton, Alignment.MIDDLE_LEFT);
      labelLayout.addComponent(sepLabel);
      labelLayout.setComponentAlignment(sepLabel, Alignment.MIDDLE_LEFT);

      // Application name
      final Label appName = new Label();
      appName.addStyleName(NovaForge.TEXT_ELLIPSIS);
      appName.setCaption(name);
      appName.setIcon(icon);

      labelLayout.addComponent(appName);
      labelLayout.setComponentAlignment(appName, Alignment.MIDDLE_LEFT);
      labelLayout.setSizeUndefined();

      final Component removeButton = initRemoveButton(pItemId);

      boxLayout.addComponent(labelLayout);
      boxLayout.setComponentAlignment(labelLayout, Alignment.MIDDLE_LEFT);
      boxLayout.addComponent(removeButton);
      boxLayout.setComponentAlignment(removeButton, Alignment.MIDDLE_RIGHT);
    }
    return boxLayout;
  }

  private Component initRemoveButton(final Object pItemId)
  {
    final HorizontalLayout buttonsLayout = new HorizontalLayout();
    buttonsLayout.setMargin(new MarginInfo(false, true, false, false));
    final Image removeButton = new Image();
    removeButton.setWidth(ICON_SIZE, Unit.PIXELS);
    removeButton.setHeight(ICON_SIZE, Unit.PIXELS);
    removeButton.setStyleName(NovaForge.BUTTON_IMAGE);
    removeButton.setSource(new ThemeResource(NovaForgeResources.ICON_CLOSE_RED));
    removeButton.addClickListener(new ClickListener()
    {

      /**
       * 
       */
      private static final long serialVersionUID = 5481507104001131183L;

      @Override
      public void click(final ClickEvent event)
      {
        presenter.removeSelectedApplication((UUID) pItemId);
      }
    });
    buttonsLayout.addComponent(removeButton);
    return buttonsLayout;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canBeGenerated(final AbstractItemLayout pSource, final Object pItemId,
      final Object pPropertyId)
  {
    boolean isNeeded = false;

    if ((ApplicationItemProperty.NAME.getPropertyId().equals(pPropertyId))
        || (ApplicationItemProperty.DESCRIPTION.getPropertyId().equals(pPropertyId))
        || (ApplicationItemProperty.ICON.getPropertyId().equals(pPropertyId))
        || (ApplicationItemProperty.PROJECT_ID.getPropertyId().equals(pPropertyId))
        || (ApplicationItemProperty.PROJECT_NAME.getPropertyId().equals(pPropertyId))
        || (ApplicationItemProperty.PROJECT_ICON.getPropertyId().equals(pPropertyId)))
    {
      final Item item = pSource.getContainerDataSource().getItem(pItemId);
      final Object name = item.getItemProperty(ApplicationItemProperty.NAME.getPropertyId()).getValue();
      final Object icon = item.getItemProperty(ApplicationItemProperty.ICON.getPropertyId()).getValue();
      final Object projectID = item.getItemProperty(ApplicationItemProperty.PROJECT_ID.getPropertyId())
          .getValue();
      final Object projectName = item.getItemProperty(ApplicationItemProperty.PROJECT_NAME.getPropertyId())
          .getValue();
      final Object projectIcon = item.getItemProperty(ApplicationItemProperty.PROJECT_ICON.getPropertyId())
          .getValue();
      isNeeded = (name != null) && (icon != null) && (projectID != null) && (projectName != null)
          && (projectIcon != null);
    }
    return isNeeded;
  }
}
