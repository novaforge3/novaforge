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
package org.novaforge.forge.ui.applications.internal.client.associations.components;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.applications.internal.client.associations.AssociationsPresenter;
import org.novaforge.forge.ui.applications.internal.client.global.components.ItemProperty;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

import java.util.Locale;

/**
 * @author caseryj
 */
public class AssociationsActionsGenerator implements ColumnGenerator
{
  /**
   * SerialUID
   */
  private static final long           serialVersionUID = -5622712099242926767L;

  private final AssociationsPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the plugins table
   */
  public AssociationsActionsGenerator(final AssociationsPresenter pPresenter)
  {
    super();
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("serial")
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.setWidth(100, Unit.PERCENTAGE);
    final Locale locale = presenter.getComponent().getLocale();

    // Get the item and the plugin service associated
    final Item item = pSource.getItem(pItemId);
    final Property<?> setupProperty = item.getItemProperty(ItemProperty.SETUP.getPropertyId());
    if ((setupProperty != null) && (setupProperty.getValue() != null) && ((Boolean) setupProperty.getValue()))
    {
      final Property<?> enableProperty = item.getItemProperty(ItemProperty.ENABLE.getPropertyId());
      final boolean isEnable = (enableProperty != null) && (enableProperty.getValue() != null)
          && ((Boolean) enableProperty.getValue());
      final boolean isAvailable = (Boolean) item.getItemProperty(ItemProperty.AVAILABILITY.getPropertyId())
          .getValue();

      // Init actions buttons
      final Embedded setupButton = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_SETTINGS));
      setupButton.setWidth(NovaForge.ACTION_ICON_SIZE);
      setupButton.setStyleName(NovaForge.BUTTON_IMAGE);
      setupButton.setDescription(ApplicationsModule.getPortalMessages().getMessage(locale,
          Messages.APPLICATIONS_LINK_SETUP));
      setupButton.setEnabled(isAvailable && isEnable);
      setupButton.addClickListener(new MouseEvents.ClickListener()
      {

        /**
         * {@inheritDoc}
         */
        @Override
        public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
        {
          presenter.manageNotifications();
        }
      });
      actionsLayout.addComponent(setupButton);
      actionsLayout.setComponentAlignment(setupButton, Alignment.MIDDLE_CENTER);
      if (!isAvailable)
      {
        setupButton.setDescription(ApplicationsModule.getPortalMessages().getMessage(locale,
            Messages.PLUGINSMANAGEMENT_PLUGIN_UNAVAILABLE));
      }
    }

    return actionsLayout;
  }

}
