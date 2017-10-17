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
package org.novaforge.forge.plugins.bucktracker.mantis.internal.services;

import org.novaforge.forge.plugins.bucktracker.mantis.services.MantisGetIssuesFilter;

import java.util.Locale;

/**
 * @author Gauthier Cart
 */
public class MantisGetIssuesFilterImpl implements MantisGetIssuesFilter
{

  /** Category to filter */
  private String category;

  /** Fixed in version to filter */
  private String fixedInVersion;

  /** Product version to filter */
  private String productVersion;

  /** Locale of the user */
  private Locale userLocale;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCategory()
  {
    return category;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCategory(final String pCategory)
  {
    category = pCategory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFixedInVersion()
  {
    return fixedInVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFixedInVersion(final String pFixedInVersion)
  {
    fixedInVersion = pFixedInVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProductVersion()
  {
    return productVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProductVersion(final String pProductVersion)
  {
    productVersion = pProductVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Locale getUserLocale()
  {
    return userLocale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserLocale(final Locale pUserLocale)
  {
    userLocale = pUserLocale;
  }

}
