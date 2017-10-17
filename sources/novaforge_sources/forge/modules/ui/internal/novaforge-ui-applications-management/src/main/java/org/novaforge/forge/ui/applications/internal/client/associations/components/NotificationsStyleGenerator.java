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

import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.ui.applications.internal.client.global.components.ItemProperty;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

/**
 * @author Guillaume Lamirand
 */
public class NotificationsStyleGenerator implements CellStyleGenerator
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 95827873679529335L;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStyle(final Table pSource, final Object pItemId, final Object pPropertyId)
  {
    String style = null;
    final Property<?> compositionProperty = pSource.getContainerProperty(pItemId,
        ItemProperty.COMPOSITION.getPropertyId());

    // Check if an url has been set
    if ((compositionProperty != null) && (compositionProperty.getValue() != null))
    {
      final Composition composition = (Composition) compositionProperty.getValue();

      if (!composition.isActivated())
      {
        style = NovaForge.LABEL_DISABLE;
      }
      else
      {
        style = NovaForge.LABEL_GREEN;

      }
    }
    return style;
  }

}
