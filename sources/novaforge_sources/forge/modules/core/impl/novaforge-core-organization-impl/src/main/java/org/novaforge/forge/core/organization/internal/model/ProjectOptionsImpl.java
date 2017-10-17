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
package org.novaforge.forge.core.organization.internal.model;

import org.novaforge.forge.core.organization.model.ProjectOptions;

/**
 * Implementation of {@link ProjectOptions}
 *
 * @author Guillaume Lamirand
 */
public class ProjectOptionsImpl implements ProjectOptions
{
  /**
   * Concerned the organization field
   */
  private boolean organization;
  /**
   * Concerned the image field
   */
  private boolean image;
  /**
   * Concerned the system projects
   */
  private boolean system;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRetrievedSystem()
  {
    return system;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectOptions setRetrievedSystem(final boolean pSystem)
  {
    system = pSystem;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRetrievedOrganization()
  {
    return organization;
  }

  /**
   * {@inheritDoc}
   */
  @Override public ProjectOptions setRetrievedOrganization(final boolean pOrganization)
  {
    organization = pOrganization;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRetrievedImage()
  {
    return image;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectOptions setRetrievedImage(final boolean pImage)
  {
    image = pImage;
    return this;
  }

}
