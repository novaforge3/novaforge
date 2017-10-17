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
package org.novaforge.forge.ui.applications.internal.client.global.components;

import com.vaadin.data.Property;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ItemStyleGenerator;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

/**
 * @author Guillaume Lamirand
 */
public class ApplicationsStyleGenerator implements ItemStyleGenerator
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 95827873679529335L;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStyle(final Tree pSource, final Object pItemId)
  {
    String style = null;
    final Property<?> nodeProperty = pSource.getContainerProperty(pItemId, ItemProperty.NODE.getPropertyId());

    // Check if an url has been set
    if ((nodeProperty != null) && (nodeProperty.getValue() != null)
        && (nodeProperty.getValue() instanceof ProjectApplication))
    {
      final ProjectApplication projectApplication = (ProjectApplication) nodeProperty.getValue();
      final Property<?> availability = pSource.getContainerProperty(pItemId,
          ItemProperty.AVAILABILITY.getPropertyId());

      if ((availability.getValue() != null) && (availability.getValue() instanceof Boolean) && (!((Boolean) availability
                                                                                                                .getValue())))
      {
        style = NovaForge.LABEL_STRIKE;
      }
      else if (ApplicationStatus.ACTIVE.equals(projectApplication.getStatus()))
      {
        style = NovaForge.APPLICATIONS_TREENODE_APP;
      }
      else if ((ApplicationStatus.CREATE_IN_PROGRESS.equals(projectApplication.getStatus()))
          || (ApplicationStatus.PROVIDING_PENDING.equals(projectApplication.getStatus())))
      {
        style = NovaForge.LABEL_ORANGE;
      }
      else if ((ApplicationStatus.CREATE_ON_ERROR.equals(projectApplication.getStatus()))
          || (ApplicationStatus.DELETE_ON_ERROR.equals(projectApplication.getStatus())))
      {
        style = NovaForge.LABEL_RED;
      }
      else if (ApplicationStatus.DELETE_IN_PROGRESS.equals(projectApplication.getStatus()))
      {
        style = NovaForge.LABEL_STRIKE;
      }
    }
    else
    {
      style = NovaForge.APPLICATIONS_TREENODE_SPACE;
    }
    return style;
  }

}
