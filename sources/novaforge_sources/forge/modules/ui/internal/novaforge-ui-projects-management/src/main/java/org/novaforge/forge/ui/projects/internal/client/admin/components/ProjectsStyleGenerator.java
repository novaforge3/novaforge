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
package org.novaforge.forge.ui.projects.internal.client.admin.components;

import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.projects.internal.client.admin.containers.ProjectItemProperty;

/**
 * @author Guillaume Lamirand
 */
public class ProjectsStyleGenerator implements CellStyleGenerator
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
    final Property<?> statusProperty = pSource.getContainerProperty(pItemId,
        ProjectItemProperty.STATUS.getPropertyId());

    if ((statusProperty != null) && (statusProperty.getValue() != null)
        && (statusProperty.getValue() instanceof ProjectStatus))
    {
      if ((ProjectStatus.REJECTED.equals(statusProperty.getValue()))
          || (ProjectStatus.DELETE_ON_ERROR.equals(statusProperty.getValue())))
      {
        style = NovaForge.LABEL_RED;
      }
      else if (ProjectStatus.DELETE_IN_PROGRESS.equals(statusProperty.getValue()))
      {
        style = NovaForge.LABEL_STRIKE;

      }
      else if (ProjectStatus.TO_BE_VALIDATED.equals(statusProperty.getValue()))
      {
        style = NovaForge.LABEL_BLUE;

      }
      else if (ProjectStatus.VALIDATED.equals(statusProperty.getValue()))
      {
        style = NovaForge.LABEL_GREEN;

      }
    }
    return style;
  }

}
