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

import com.google.common.base.Strings;
import com.vaadin.data.util.HierarchicalContainer;
import org.novaforge.forge.core.plugins.categories.FieldDescription;
import org.novaforge.forge.ui.applications.internal.client.global.components.ItemProperty;

import java.util.List;

/**
 * This component describes a specific {@link HierarchicalContainer} used to build association tree table.
 * 
 * @author Guillaume Lamirand
 */
public class ParametersContainer extends HierarchicalContainer
{

  /**
   * Property label
   */
  private static final String PROPERTY_START   = "@";
  /**
   * Plugin type separator
   */
  private static final String SEPARATOR        = ".";
  /**
   * Serial version id for Serialization
   */
  private static final long   serialVersionUID = 3439109343778568310L;

  /**
   * Default constructor. It will initialize table item property
   * 
   * @see ItemProperty
   * @see HierarchicalContainer#HierarchicalContainer()
   */
  public ParametersContainer()
  {
    super();
    addContainerProperty(ItemProperty.CAPTION.getPropertyId(), String.class, null);
    addContainerProperty(ItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(ItemProperty.VALUE.getPropertyId(), String.class, null);

  }

  /**
   * Will initialize table with the given {@link FieldDescription}
   * 
   * @param pPluginType
   *          the plugin type label
   * @param pFieldDescription
   *          the field description
   */
  public void setDatas(final String pPluginType, final List<FieldDescription> pFieldDescription)
  {
    if (pFieldDescription != null)
    {
      for (final FieldDescription field : pFieldDescription)
      {
        if ((field.getType()!=null) && (!field.getType().isAssignableFrom(Boolean.class)))
        {
          final String name = field.getName();
          addItem(name);
          final String append = buildKey(pPluginType, field.getClassFieldName());
          getContainerProperty(name, ItemProperty.CAPTION.getPropertyId()).setValue(append);
          getContainerProperty(name, ItemProperty.DESCRIPTION.getPropertyId()).setValue(
              field.getDescription());
          getContainerProperty(name, ItemProperty.VALUE.getPropertyId()).setValue(field.getExampleValue());
        }
      }
    }
  }

  /**
   * Build a key value from plugin type and field
   * 
   * @param pPluginType
   *          plugin type
   * @param pFieldName
   *          the source field name
   * @return the key built
   */
  private String buildKey(final String pPluginType, final String pFieldName)
  {
    return PROPERTY_START + pPluginType + SEPARATOR + pFieldName;
  }

  /**
   * This method will return the value given with keys replaced
   * 
   * @param pValue
   *          the source value
   * @return result without key
   */
  public String replaceKeys(final String pValue)
  {
    String result = pValue;
    if (!Strings.isNullOrEmpty(pValue))
    {
      final List<Object> allItemIds = getAllItemIds();
      for (final Object fieldName : allItemIds)
      {
        final String key = (String) getContainerProperty(fieldName, ItemProperty.CAPTION.getPropertyId())
            .getValue();
        final String value = (String) getContainerProperty(fieldName, ItemProperty.VALUE.getPropertyId())
            .getValue();
        result = result.replaceAll(key, value);
      }
    }
    return result;
  }
}
