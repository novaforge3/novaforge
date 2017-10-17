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
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.applications.internal.client.associations.AssociationsPresenter;
import org.novaforge.forge.ui.applications.internal.client.global.components.ItemProperty;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;

import java.util.Locale;

/**
 * @author caseryj
 */
public class NotificationsEnableGenerator implements ColumnGenerator
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
  public NotificationsEnableGenerator(final AssociationsPresenter pPresenter)
  {
    super();
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.setWidth(100, Unit.PERCENTAGE);
    final Locale locale = presenter.getComponent().getLocale();

    final Item item = pSource.getItem(pItemId);
    final Property<?> compositionProperty = item.getItemProperty(ItemProperty.COMPOSITION.getPropertyId());

    // Check if an url has been set
    if ((compositionProperty != null) && (compositionProperty.getValue() != null))
    {
      final Composition composition = (Composition) compositionProperty.getValue();

      // Init enable box
      final CheckBox enableBox = new CheckBox();
      enableBox.setImmediate(true);
      enableBox.setWidth(24, Unit.PIXELS);
      enableBox.setDescription(ApplicationsModule.getPortalMessages().getMessage(locale,
          Messages.APPLICATIONS_LINK_ENABLE_DESCRIPTION));
      if (composition.isActivated())
      {
        enableBox.setValue(true);
        enableBox.setDescription(ApplicationsModule.getPortalMessages().getMessage(locale,
            Messages.APPLICATIONS_LINK_NOTIFICATION_DISABLE));
      }
      else
      {
        enableBox.setValue(false);
        enableBox.setDescription(ApplicationsModule.getPortalMessages().getMessage(locale,
            Messages.APPLICATIONS_LINK_NOTIFICATION_ENABLE));

      }
      enableBox.addValueChangeListener(new ValueChangeListener()
      {

        /**
         * Serial version id
         */
        private static final long serialVersionUID = 8189752575521169966L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void valueChange(final ValueChangeEvent pEvent)
        {
          presenter.changeStatus(pItemId, !composition.isActivated());

        }
      });
      actionsLayout.addComponent(enableBox);
      actionsLayout.setComponentAlignment(enableBox, Alignment.MIDDLE_CENTER);

    }

    return actionsLayout;
  }
}
