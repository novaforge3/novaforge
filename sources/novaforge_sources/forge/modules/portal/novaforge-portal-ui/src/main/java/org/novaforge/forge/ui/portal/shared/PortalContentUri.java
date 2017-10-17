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
package org.novaforge.forge.ui.portal.shared;

import com.google.common.base.Strings;

/**
 * This enum list the content uri available for the portal
 * 
 * @author Guillaume Lamirand
 */
public enum PortalContentUri
{
  /**
   * Define the content uri for the public domain
   */
  PUBLIC
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getRelativeUri()
    {
      return "public";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUri()
    {
      return "/public";
    }
  },
  /**
   * Define the content uri for the recovery password view
   */
  RECOVERY_PASSWORD
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getRelativeUri()
    {
      return "recovery";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUri()
    {
      return "/recovery";
    }
  },
  /**
   * Define the content uri for the private domain
   */
  PRIVATE
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getRelativeUri()
    {
      return "private";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUri()
    {
      return "/private";
    }
  },
  /**
   * Define the content uri for the shiro-cas authentification
   */
  SHIRO_CAS
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getRelativeUri()
    {
      return "shiro-cas";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUri()
    {
      return "/shiro-cas";
    }
  };
  /**
   * Get the {@link PortalContentUri} is associated the the uri given.
   *
   * @param pURI
   *          the uri relative to check
   * @return {@link PortalContentUri} found or <code>null</code>
   */
  public static PortalContentUri getPortalUri(final String pURI)
  {
    PortalContentUri uri = null;
    if (!Strings.isNullOrEmpty(pURI))
    {
      for (final PortalContentUri contentUri : PortalContentUri.values())
      {
        final StringBuilder uriToCheck = new StringBuilder("/").append(contentUri.getRelativeUri());
        if ((pURI.startsWith(uriToCheck.toString())) || (pURI.startsWith(contentUri.getRelativeUri())))
        {
          uri = contentUri;
        }
      }
    }
    return uri;
  }

  /**
   * Return the relative uri without / at the begining
   *
   * @return the relative uri string
   */
  public abstract String getRelativeUri();

  /**
   * Return the relative uri with / at the begining
   *
   * @return the relative uri string
   */
  public abstract String getUri();
}
