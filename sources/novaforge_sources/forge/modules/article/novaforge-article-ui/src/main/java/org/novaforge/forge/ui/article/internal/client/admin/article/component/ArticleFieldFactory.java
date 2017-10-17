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
package org.novaforge.forge.ui.article.internal.client.admin.article.component;

import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroupFieldFactory;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;

/**
 * @author Jeremy Casery
 */
public class ArticleFieldFactory implements FieldGroupFieldFactory
{

  /**
   * 
   */
  private static final long      serialVersionUID    = 7845914808272166084L;

  private FieldGroupFieldFactory defaultFieldFactory = DefaultFieldGroupFieldFactory.get();

  /**
   * {@inheritDoc}
   */
  @Override
  public <T extends Field> T createField(Class<?> dataType, Class<T> fieldType)
  {
    if (DateField.class.isAssignableFrom(dataType))
    {
      return createDateField();
    }
    else if (Boolean.class.isAssignableFrom(dataType))
    {
      return createCheckBoxField();
    }
    else
    {
      return defaultFieldFactory.createField(dataType, fieldType);
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected <T extends Field> T createDateField()
  {
    DateField field = new DateField();
    field.setImmediate(true);
    return (T) field;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected <T extends Field> T createCheckBoxField()
  {
    CheckBox field = new CheckBox();
    field.setImmediate(true);
    return (T) field;
  }
}
