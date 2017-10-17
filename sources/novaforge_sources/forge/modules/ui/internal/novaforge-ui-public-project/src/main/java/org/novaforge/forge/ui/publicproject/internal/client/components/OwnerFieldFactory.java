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
import com.vaadin.data.util.PropertyFormatter;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

/**
 * @author Jeremy Casery
 */
public class OwnerFieldFactory extends DefaultFieldFactory
{

  /**
    * 
    */
  private static final long   serialVersionUID = 2398201391738793010L;

  private static final String AT_CHAR          = " AT ";

  private TextField           firstNameField;

  private TextField           lastNameField;

  private TextField           emailField;

  @SuppressWarnings("unchecked")
  @Override
  public Field createField(final Item pItem, final Object pPropertyId, final Component pComponent)
  {
    final Field f = super.createField(pItem, pPropertyId, pComponent);
    f.setReadOnly(true);
    if (OwnerItemProperty.FIRSTNAME.getPropertyId().equals(pPropertyId))
    {
      firstNameField = (TextField) f;
    }
    else if (OwnerItemProperty.LASTNAME.getPropertyId().equals(pPropertyId))
    {
      lastNameField = (TextField) f;
    }
    else if (OwnerItemProperty.EMAIL.getPropertyId().equals(pPropertyId))
    {
      emailField = (TextField) f;
      emailField.setWidth(200, Unit.PIXELS);
      emailField.setPropertyDataSource(new PropertyFormatter()
      {

        /**
             * 
             */
        private static final long serialVersionUID = -8259947221868416525L;

        /**
         * {@inheritDoc}
         */
        @Override
        public String format(final Object pValue)
        {
          final StringBuilder sb = new StringBuilder();
          if (pValue instanceof String)
          {
            final String email = (String) pValue;
            final String[] emailSplitted = email.split("@");
            sb.append(emailSplitted[0]).append(AT_CHAR).append(emailSplitted[1]);
          }
          return sb.toString();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object parse(final String formattedValue) throws Exception
        {
          return formattedValue;
        }
      });
    }
    return f;
  }

  /**
   * @return the firstNameField
   */
  public TextField getFirstNameField()
  {
    return firstNameField;
  }

  /**
   * @return the lastNameField
   */
  public TextField getLastNameField()
  {
    return lastNameField;
  }

  /**
   * @return the emailField
   */
  public TextField getEmailField()
  {
    return emailField;
  }
}
