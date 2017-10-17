/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.beaver.context.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.novaforge.beaver.context.oxm.marshaller.Contexts;
import org.novaforge.beaver.context.oxm.marshaller.Contexts.Context;
import org.novaforge.beaver.context.oxm.marshaller.Properties;
import org.novaforge.beaver.context.oxm.marshaller.Properties.Property;

/**
 * @author Guillaume Lamirand
 */
public class ListWrapper
{
  private List<Property>            listProperty = new ArrayList<>();
  private List<Context>             listContext  = new ArrayList<>();
  private final Map<String, String> propertyMap  = new HashMap<>();

  private ListWrapper(final Properties pProperties)
  {
    if (pProperties.getPropertyList() != null)
    {
      listProperty = pProperties.getPropertyList();
    }
    for (final Property property : listProperty)
    {
      propertyMap.put(property.getKey(), property.getString());
    }
  }

  private ListWrapper(final Contexts pContexts)
  {
    if (pContexts.getContextList() != null)
    {
      listContext = pContexts.getContextList();
    }
    for (final Context context : listContext)
    {
      propertyMap.put(context.getKey(), context.getString());
    }
  }

  public static ListWrapper wrapProperties(final Properties pProperties)
  {
    return new ListWrapper(pProperties);
  }

  public static ListWrapper wrapContexts(final Contexts pContexts)
  {
    return new ListWrapper(pContexts);
  }

  public String get(final String pPropertyKey)
  {
    String propertyValue = null;

    if ((pPropertyKey != null) && ("".equals(pPropertyKey) == false)
        && (propertyMap.containsKey(pPropertyKey)))
    {
      propertyValue = propertyMap.get(pPropertyKey);
    }
    return propertyValue;

  }

  public void put(final String pPropertyKey, final String pPropertyValue)
  {
    if (pPropertyKey != null)
    {
      if (exist(pPropertyKey))
      {
        if (listProperty != null)
        {
          putProperty(pPropertyKey, pPropertyValue);
        }
        else if (listContext != null)
        {
          putContext(pPropertyKey, pPropertyValue);
        }
      }
      else
      {
        if (listProperty != null)
        {
          final Property newProperty = new Property();
          newProperty.setKey(pPropertyKey);
          newProperty.setString(pPropertyValue);
          listProperty.add(newProperty);
        }
        else if (listContext != null)
        {
          final Context newContext = new Context();
          newContext.setKey(pPropertyKey);
          newContext.setString(pPropertyValue);
          listContext.add(newContext);
        }
      }
      propertyMap.put(pPropertyKey, pPropertyValue);
    }
  }

  private void putProperty(final String pPropertyKey, final String pPropertyValue)
  {
    for (final Property property : listProperty)
    {
      if (property.getKey().equals(pPropertyKey))
      {
        property.setString(pPropertyValue);
        break;
      }
    }
  }

  private void putContext(final String pPropertyKey, final String pPropertyValue)
  {
    for (final Context context : listContext)
    {
      if (context.getKey().equals(pPropertyKey))
      {
        context.setString(pPropertyValue);
        break;
      }
    }
  }

  public boolean exist(final String pPropertyKey)
  {
    return propertyMap.containsKey(pPropertyKey);
  }
}
