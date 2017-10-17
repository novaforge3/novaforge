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
package org.novaforge.forge.commons.technical.historization.internal.services;

import org.novaforge.forge.commons.technical.historization.annotations.Historizable;
import org.novaforge.forge.commons.technical.historization.model.HistorizableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Guillaume Lamirand
 */
public class HistorizableObjectImpl implements HistorizableObject
{
  /**
   * Serial version id
   */
  private static final long   serialVersionUID = 304507730078057003L;
  private static final Logger LOGGER = LoggerFactory.getLogger(HistorizableObjectImpl.class);
  /**
   * Contains label describing this object
   */
  private final String        label;
  /**
   * Contains details build form object to historize
   */
  private final String        details;

  /**
   * @param pLabel
   * @param pObject
   */
  public HistorizableObjectImpl(final String pLabel, final Object pObject)
  {
    label = pLabel;
    details = buildDetails(pObject);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLabel()
  {
    return label;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDetails()
  {
    return details;
  }

  /**
   * Retrieve from object its details as full string
   * 
   * @return complet details
   */
  private String buildDetails(final Object pObject)
  {
    final StringBuilder detailsBuilt = new StringBuilder();
    try
    {
      final String builtDetails = buildDetails(label, pObject);
      detailsBuilt.append(builtDetails.replace(", ]", "]"));
    }
    catch (final Exception e)
    {
      LOGGER
          .warn(String.format("Unable to build historization details about object given with [label=%s]",
              label), e);
    }
    return detailsBuilt.toString();
  }

  private String buildDetails(final String pLabel, final Object pObject) throws IllegalAccessException,
      InvocationTargetException
  {
    final StringBuilder detailsBuilt = new StringBuilder();
    if (pObject != null)
    {
      if (isWrapperType(pObject))
      {
        detailsBuilt.append(String.format("%s=%s", pLabel, pObject));
      }
      else if (pObject instanceof Collection<?>)
      {
        final Collection<?> collection = (Collection<?>) pObject;
        if (!collection.isEmpty())
        {
          detailsBuilt.append(String.format("%s: [", pLabel));
          for (final Object object : collection)
          {
            if (isWrapperType(object))
            {
              detailsBuilt.append(String.format("%s, ", object));
            }
            else
            {
              final Class<?> clazz = object.getClass();

              final StringBuilder detailRec = new StringBuilder();
              detailRec.append(buildClassDetails(clazz, object));
              detailsBuilt.append(String.format("[%s], ", detailRec));
            }
          }
          detailsBuilt.append("]");
        }
      }
      else
      {
        final Class<?> clazz = pObject.getClass();
        final StringBuilder detailRec = new StringBuilder();
        detailRec.append(buildClassDetails(clazz, pObject));
        detailsBuilt.append(String.format("%s: [%s]", pLabel, detailRec));
      }
    }
    return detailsBuilt.toString();
  }

  private String buildClassDetails(final Class<?> pClazz, final Object pObject)
      throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    final StringBuilder detailsBuilt = new StringBuilder();
    final Method[] methods = pClazz.getMethods();
    for (final Method method : methods)
    {
      for (final Annotation annotation : method.getAnnotations())
      {
        if (annotation instanceof Historizable)
        {
          final Historizable historizable = (Historizable) annotation;
          final String label = historizable.label();
          final Object value = method.invoke(pObject);
          detailsBuilt.append(String.format("%s, ", buildDetails(label, value)));
        }
      }
    }
    for (final Class<?> interfazz : pClazz.getInterfaces())
    {
      detailsBuilt.append(buildClassDetails(interfazz, pObject));
    }
    return detailsBuilt.toString();
  }

  private boolean isWrapperType(final Object pObject)
  {
    return (pObject instanceof Boolean) || (pObject instanceof Character) || (pObject instanceof Byte)
        || (pObject instanceof Short) || (pObject instanceof Integer) || (pObject instanceof Long)
        || (pObject instanceof Float) || (pObject instanceof Double) || (pObject instanceof String);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "HistorizableObjectImpl [label=" + label + ", details=" + details + "]";
  }

}
