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
package org.novaforge.forge.ui.portal.data.validator;

import com.vaadin.data.validator.AbstractValidator;

import java.net.URL;

/**
 * @author Guillaume Lamirand
 */
public class URLValidator extends AbstractValidator<URL>
{

  /**
   * Serial version id for serialization
   */
  private static final long   serialVersionUID = 1999993607111891092L;
  private static final String HTTP_PREFIX      = "http://";
  private static final String HTTPS_PREFIX     = "https://";
  private static final String AJP_PREFIX       = "ajp://";
  private static final String URL_END          = "/";
  private boolean allowNull;
  private Integer minLength = 4;
  private Integer maxLength = 255;

  /**
   * Creates a new URLValidator with a given error message and null allowed value
   *
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   * @param pAllowNull
   *          Are null strings permissible? This can be handled better by
   *          setting a field as required or not.
   */
  public URLValidator(final String pErrorMessage, final boolean pAllowNull)
  {
    this(pErrorMessage);
    allowNull = pAllowNull;
  }

  /**
   * Creates a new URLValidator with a given error message.
   *
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   */
  public URLValidator(final String pErrorMessage)
  {
    super(pErrorMessage);
    allowNull = true;
  }

  /**
   * Creates a new URLValidator with a given error message and
   * minimum and maximum length limits.
   * 
   * @param pErrorMessage
   *          the message to display in case the value does not validate.
   * @param pMinLength
   *          the minimum permissible length of the URL or null for no
   *          limit.
   * @param pMaxLength
   *          the maximum permissible length of the URL or null for no
   *          limit.
   * @param pAllowNull
   *          Are null strings permissible? This can be handled better by
   *          setting a field as required or not.
   */
  public URLValidator(final String pErrorMessage, final Integer pMinLength, final Integer pMaxLength,
      final boolean pAllowNull)
  {
    super(pErrorMessage);
    allowNull = pAllowNull;
    minLength = pMinLength;
    maxLength = pMaxLength;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidValue(final URL pValue)
  {

    boolean isValid = true;
    if (pValue != null)
    {
      final int len = pValue.toExternalForm().length();
      if (((minLength != null) && (minLength > -1) && (len < minLength))
          || ((maxLength != null) && (maxLength > -1) && (len > maxLength)))
      {
        isValid = false;
      }
      if (isValid)
      {
        isValid = validateURL(pValue.toExternalForm());
      }
    }
    return isValid;
  }

  /**
   * Validate the URL format
   * 
   * @param pUrl
   *          the url to valid
   * @return true if pUrl is valid
   */
  private boolean validateURL(final String pUrl)
  {
    return (pUrl.startsWith(HTTP_PREFIX) || pUrl.startsWith(HTTPS_PREFIX) || pUrl.startsWith(AJP_PREFIX))
        && pUrl.endsWith(URL_END);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<URL> getType()
  {
    return URL.class;
  }

  /**
   * Returns <code>true</code> if null strings are allowed.
   * 
   * @return <code>true</code> if allows null string, otherwise <code>false</code>.
   */
  public final boolean isNullAllowed()
  {
    return allowNull;
  }

  /**
   * Sets whether null-strings are to be allowed. This can be better handled
   * by setting a field as required or not.
   */
  @Deprecated
  public void setNullAllowed(final boolean allowNull)
  {
    this.allowNull = allowNull;
  }

  /**
   * Gets the maximum permissible length of the string.
   *
   * @return the maximum length of the string or null if there is no limit
   */
  public Integer getMaxLength()
  {
    return maxLength;
  }

  /**
   * Sets the maximum permissible length of the string.
   *
   * @param maxLength
   *          the maximum length to accept or null for no limit
   */
  public void setMaxLength(final Integer maxLength)
  {
    this.maxLength = maxLength;
  }

  /**
   * Gets the minimum permissible length of the string.
   *
   * @return the minimum length of the string or null if there is no limit
   */
  public Integer getMinLength()
  {
    return minLength;
  }

  /**
   * Sets the minimum permissible length.
   * 
   * @param minLength
   *          the minimum length to accept or null for no limit
   */
  public void setMinLength(final Integer minLength)
  {
    this.minLength = minLength;
  }
}
