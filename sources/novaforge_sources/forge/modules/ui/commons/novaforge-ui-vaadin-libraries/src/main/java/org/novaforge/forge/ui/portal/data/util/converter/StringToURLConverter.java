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
package org.novaforge.forge.ui.portal.data.util.converter;

import com.vaadin.data.util.converter.Converter;

import java.net.URL;
import java.util.Locale;

/**
 * @author Guillaume Lamirand
 *
 */

/**
 * A converter that converts from {@link URL} to {@link String} and back.
 * The String representation is given by URL.toExternalForm().
 * 
 * @author Guillaume Lamirand
 */
public class StringToURLConverter implements Converter<String, URL>
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 8654095498412980865L;

  /*
   * (non-Javadoc)
   * @see
   * com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object,
   * java.lang.Class, java.util.Locale)
   */
  @Override
  public URL convertToModel(final String value, final Class<? extends URL> targetType, final Locale locale)
      throws ConversionException
  {
    URL returnValue = null;
    try
    {
      if (value != null)
      {
        returnValue = new URL(value);
      }
    }
    catch (final Exception e)
    {
      throw new ConversionException("Cannot convert " + value + " to " + getModelType().getName());
    }
    return returnValue;
  }

  /*
   * (non-Javadoc)
   * @see
   * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
   * .Object, java.lang.Class, java.util.Locale)
   */
  @Override
  public String convertToPresentation(final URL value, final Class<? extends String> targetType,
      final Locale locale) throws ConversionException
  {
    String returnValue = null;
    try
    {
      if (value != null)
      {
        returnValue = value.toExternalForm();
      }
    }
    catch (final Exception e)
    {
      throw new ConversionException("Cannot convert " + value + " to " + getModelType().getName());
    }
    return returnValue;
  }

  /*
   * (non-Javadoc)
   * @see com.vaadin.data.util.converter.Converter#getModelType()
   */
  @Override
  public Class<URL> getModelType()
  {
    return URL.class;
  }

  /*
   * (non-Javadoc)
   * @see com.vaadin.data.util.converter.Converter#getPresentationType()
   */
  @Override
  public Class<String> getPresentationType()
  {
    return String.class;
  }

}
