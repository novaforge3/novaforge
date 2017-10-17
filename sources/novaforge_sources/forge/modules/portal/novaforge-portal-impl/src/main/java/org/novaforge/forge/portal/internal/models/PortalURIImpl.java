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
package org.novaforge.forge.portal.internal.models;

import org.novaforge.forge.portal.exceptions.PortalException;
import org.novaforge.forge.portal.models.PortalURI;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * @author Guillaume Lamirand
 */
public class PortalURIImpl implements PortalURI
{

  private final URI uri;
  private boolean   isInternal = false;
  private String    moduleId   = null;
  private URL       absoluteURL;

  /**
   * Default constructor
   * 
   * @param pURI
   *          the uri to analyse
   * @throws PortalException
   */
  public PortalURIImpl(final URI pURI) throws PortalException
  {
    uri = pURI;
    if (uri != null)
    {
      if ((uri.isOpaque()) && (uri.getScheme().equals(PortalURI.MODULES_SCHEME)))
      {
        isInternal = true;
        moduleId = uri.getSchemeSpecificPart();
      }
      else if (uri.isAbsolute())
      {
        try
        {
          absoluteURL = uri.toURL();
        }
        catch (final MalformedURLException e)
        {
          throw new PortalException(String.format("Unable to build the absolute URL from uri given [uri=%s]",
              pURI), e);
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInternalModule()
  {
    return isInternal;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getModuleId()
  {
    return moduleId;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRelativePath()
  {
    final StringBuilder path = new StringBuilder();
    if (uri != null)
    {
      path.append(uri.getPath()).append("?").append(uri.getQuery());
    }
    return path.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getAbsoluteURL()
  {
    return absoluteURL;
  }

  /**
   * Set the absolute URL containing portal URL + relative path
   * 
   * @param pAbsoluteURL
   *          the absoluteURL to set
   */
  public void setAbsoluteURL(final URL pAbsoluteURL)
  {
    absoluteURL = pAbsoluteURL;
  }

}
