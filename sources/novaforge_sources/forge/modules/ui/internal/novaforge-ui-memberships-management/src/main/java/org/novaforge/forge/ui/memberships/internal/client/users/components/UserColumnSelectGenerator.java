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
package org.novaforge.forge.ui.memberships.internal.client.users.components;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

/**
 * @author Guillaume Lamirand
 */
public class UserColumnSelectGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long serialVersionUID = 8736106711161577296L;

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {

    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.setWidth(100, Unit.PERCENTAGE);

    // Init actions buttons
    final CheckBox selectBox = new CheckBox();
    selectBox.setImmediate(true);
    selectBox.setValue(pSource.isSelected(pItemId));
    selectBox.setWidth(24, Unit.PIXELS);
    selectBox.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MEMBERSHIPS_USERS_SELECT_DESCRIPTION));
    selectBox.addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {
        if (selectBox.getValue())
        {
          pSource.select(pItemId);
        }
        else
        {
          pSource.unselect(pItemId);
        }

      }
    });
    actionsLayout.addComponent(selectBox);
    actionsLayout.setComponentAlignment(selectBox, Alignment.MIDDLE_CENTER);

    return actionsLayout;
  }
}
