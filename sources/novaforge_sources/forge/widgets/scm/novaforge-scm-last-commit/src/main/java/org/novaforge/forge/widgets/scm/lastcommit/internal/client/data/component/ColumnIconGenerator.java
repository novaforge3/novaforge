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
package org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.component;

import com.vaadin.data.Item;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.widgets.scm.lastcommit.internal.client.data.container.CommitItemProperty;

/**
 * @author Guillaume Lamirand
 */
public class ColumnIconGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long serialVersionUID = -2377060170281716413L;

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    Embedded icon = null;
    if (pSource.getParent() != null)
    {
      final Item item = pSource.getItem(pItemId);
      final StreamResource instanceInfo = (StreamResource) item.getItemProperty(
          CommitItemProperty.ICON.getPropertyId()).getValue();
      if (instanceInfo != null)
      {
        icon = new Embedded(null, instanceInfo);
        icon.setWidth(18, Unit.PIXELS);
        icon.setHeight(18, Unit.PIXELS);
      }
    }
    return icon;
  }
}
