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
package org.novaforge.forge.ui.memberships.internal.client.roles.components;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeTable;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Node;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.memberships.internal.client.containers.RolesMappingItemProperty;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

/**
 * This component will be used by {@link TreeTable} to generate tool tip for each items.
 * 
 * @author Guillaume Lamirand
 * @see TreeTable#setItemDescriptionGenerator(ItemDescriptionGenerator)
 */
public class RolesMappingDescriptionGenerator implements ItemDescriptionGenerator
{

  /**
   * Serial version uid for serialization
   */
  private static final long serialVersionUID = -4133057423746426408L;

  /**
   * {@inheritDoc}
   */
  @Override
  public String generateDescription(final Component pSource, final Object pItemId, final Object pPropertyId)
  {
    String descriptionReturned = null;
    if (pSource instanceof Tree)
    {
      final Tree tree = (Tree) pSource;
      final Property<?> caption = tree.getContainerProperty(pItemId,
          RolesMappingItemProperty.CAPTION.getPropertyId());
      descriptionReturned = (String) caption.getValue();
      final Node node = (Node) tree.getContainerProperty(pItemId,
          RolesMappingItemProperty.NODE.getPropertyId()).getValue();
      if (node instanceof ProjectApplication)
      {
        final ProjectApplication app = (ProjectApplication) node;
        final Property<?> availability = tree.getContainerProperty(pItemId,
            RolesMappingItemProperty.AVAILABILITY.getPropertyId());
        if ((availability.getValue() != null) && (availability.getValue() instanceof Boolean)
                && (!((Boolean) availability.getValue())))
        {
          descriptionReturned = MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
              Messages.PRIVATE_APPLICATION_UNAVAILABLE);
        }
        else if (ApplicationStatus.CREATE_IN_PROGRESS.equals(app.getStatus()))
        {
          descriptionReturned = MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
              Messages.PRIVATE_APPLICATION_INPROGRESS);

        }
        else if (ApplicationStatus.PROVIDING_PENDING.equals(app.getStatus()))
        {
          descriptionReturned = MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
              Messages.PRIVATE_APPLICATION_PENDING);
        }
      }
      else
      {
        final Property<?> description = tree.getContainerProperty(pItemId,
            RolesMappingItemProperty.DESCRIPTION.getPropertyId());
        if ((description.getValue() != null) && (!"".equals(description.getValue())))
        {
          descriptionReturned = (String) description.getValue();
        }
      }
    }
    return descriptionReturned;
  }
}
