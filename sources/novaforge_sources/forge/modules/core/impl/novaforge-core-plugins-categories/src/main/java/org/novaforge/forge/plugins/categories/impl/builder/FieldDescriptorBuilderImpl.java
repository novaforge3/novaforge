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
package org.novaforge.forge.plugins.categories.impl.builder;

import org.novaforge.forge.core.plugins.categories.FieldDescription;
import org.novaforge.forge.core.plugins.categories.FieldProperty;
import org.novaforge.forge.core.plugins.services.FieldDescriptorBuilder;
import org.novaforge.forge.plugins.categories.impl.models.FieldDescriptionImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Guillaume Lamirand
 */
public class FieldDescriptorBuilderImpl implements FieldDescriptorBuilder
{

  /**
   * {@inheritDoc}
   */
  @Override
  public List<FieldDescription> buildFieldsDescription(final ResourceBundle pResourceBundle,
      final Class<?> pClazz)
  {

    final List<FieldDescription> returnField = new ArrayList<FieldDescription>();
    final Field[] fields = pClazz.getDeclaredFields();
    for (final Field field : fields)
    {
      if (!"serialVersionUID".equals(field.getName()))
      {
        final Annotation[] anno = field.getAnnotations();
        boolean isAnnoted = false;
        for (final Annotation annotation : anno)
        {
          if (annotation instanceof FieldProperty)
          {
            isAnnoted = true;
            final FieldProperty fieldProperty = (FieldProperty) annotation;
            final String lavelValue = pResourceBundle.getString(fieldProperty.label_key());
            final String descValue = pResourceBundle.getString(fieldProperty.description_key());
            final boolean isRequiredValue = fieldProperty.required();
            final String exampleValue = pResourceBundle.getString(fieldProperty.example_key());
            final boolean isID = fieldProperty.id();
            final Class<?> classType = fieldProperty.type();
            final int typeSize = fieldProperty.size();

            final FieldDescriptionImpl desc = new FieldDescriptionImpl(field.getName(), lavelValue,
                descValue, isRequiredValue, exampleValue, isID, classType, typeSize);
            returnField.add(desc);
          }
        }
        if (!isAnnoted)
        {
          final FieldDescriptionImpl desc = new FieldDescriptionImpl(field.getName());
          returnField.add(desc);
        }
      }
    }
    return returnField;

  }
}
