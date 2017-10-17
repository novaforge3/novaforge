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
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Node;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.applications.internal.module.ApplicationsModule;

/**
 * This component will be used by navigation {@link Tree} to generate tool tip for each items.
 * 
 * @author Guillaume Lamirand
 * @see Tree#setItemDescriptionGenerator(ItemDescriptionGenerator)
 */
public class ApplicationsDescriptionGenerator implements ItemDescriptionGenerator
{

  /**
   * Serial version uid for serialization
   */
  private static final long serialVersionUID = 395553539309852145L;

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
      final Property<?> caption = tree.getContainerProperty(pItemId, ItemProperty.CAPTION.getPropertyId());
      descriptionReturned = (String) caption.getValue();
      final Node node = (Node) tree.getContainerProperty(pItemId, ItemProperty.NODE.getPropertyId())
          .getValue();
      if (node instanceof ProjectApplication)
      {
        final ProjectApplication app = (ProjectApplication) node;
        final Property<?> availability = tree.getContainerProperty(pItemId,
            ItemProperty.AVAILABILITY.getPropertyId());
        if ((availability.getValue() != null) && (availability.getValue() instanceof Boolean)
                && (!((Boolean) availability.getValue())))
        {
          descriptionReturned = ApplicationsModule.getPortalMessages().getMessage(pSource.getLocale(),
              Messages.PRIVATE_APPLICATION_UNAVAILABLE);
        }
        else if (ApplicationStatus.CREATE_ON_ERROR.equals(app.getStatus()))
        {
          descriptionReturned = ApplicationsModule.getPortalMessages().getMessage(pSource.getLocale(),
              Messages.PRIVATE_APPLICATION_ON_ERROR);

        }
        else if (ApplicationStatus.CREATE_IN_PROGRESS.equals(app.getStatus()))
        {
          descriptionReturned = ApplicationsModule.getPortalMessages().getMessage(pSource.getLocale(),
              Messages.PRIVATE_APPLICATION_INPROGRESS);

        }
        else if (ApplicationStatus.DELETE_IN_PROGRESS.equals(app.getStatus()))
        {
          descriptionReturned = ApplicationsModule.getPortalMessages().getMessage(pSource.getLocale(),
              Messages.PRIVATE_APPLICATION_DELETEINPROGRESS);

        }
        else if (ApplicationStatus.PROVIDING_PENDING.equals(app.getStatus()))
        {
          descriptionReturned = ApplicationsModule.getPortalMessages().getMessage(pSource.getLocale(),
              Messages.PRIVATE_APPLICATION_PENDING);
        }
      }
      else
      {
        final Property<?> description = tree.getContainerProperty(pItemId,
            ItemProperty.DESCRIPTION.getPropertyId());
        if ((description.getValue() != null) && (!"".equals(description.getValue())))
        {
          descriptionReturned = (String) description.getValue();
        }
      }
    }
    return descriptionReturned;
  }
}