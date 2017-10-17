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
package org.novaforge.forge.ui.publicproject.internal.client.components;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

/**
 * @author Jeremy Casery
 */
public class ProjectFieldFactory extends DefaultFieldFactory
{
  /**
    * 
    */
  private static final long serialVersionUID = 691257272134331001L;
  private TextField         licenseField;
  private TextField         descriptionField;
  private TextField         authorField;
  private TextField         organizationField;
  private DateField         dateField;

  /**
   * @return the serialversionuid
   */
  public static long getSerialversionuid()
  {
    return serialVersionUID;
  }

  @Override
  public Field createField(final Item item, final Object propertyId, final Component uiContext)
  {
    final Field f = super.createField(item, propertyId, uiContext);
    f.setReadOnly(true);
    if (ProjectItemProperty.LICENCE.getPropertyId().equals(propertyId))
    {
      licenseField = (TextField) f;
    }
    else if (ProjectItemProperty.DESCRIPTION.getPropertyId().equals(propertyId))
    {
      descriptionField = (TextField) f;
    }
    else if (ProjectItemProperty.AUTHOR.getPropertyId().equals(propertyId))
    {
      authorField = (TextField) f;
    }
    else if (ProjectItemProperty.ORGANIZATION.getPropertyId().equals(propertyId))
    {
      organizationField = (TextField) f;
    }
    else if (ProjectItemProperty.CREATEDDATE.getPropertyId().equals(propertyId))
    {
      dateField = (DateField) f;
    }
    return f;
  }

  /**
   * @return the licenseField
   */
  public TextField getLicenseField()
  {
    return licenseField;
  }

  /**
   * @return the descriptionField
   */
  public TextField getDescriptionField()
  {
    return descriptionField;
  }

  /**
   * @return the authorField
   */
  public TextField getAuthorField()
  {
    return authorField;
  }

  /**
   * @return the organizationField
   */
  public TextField getOrganizationField()
  {
    return organizationField;
  }

  /**
   * @return the dateField
   */
  public DateField getDateField()
  {
    return dateField;
  }
}
