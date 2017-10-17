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
package org.novaforge.forge.dashboard.internal.model;

import org.novaforge.forge.core.organization.model.AuthorizationResource;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.dashboard.model.DashBoard;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link AuthorizationResource} for DashBoard resource
 * 
 * @author Guillaume Lamirand
 */
public class DashBoardResourceImpl implements AuthorizationResource
{
  private final Class<?>               clazz;
  private final List<PermissionAction> actions;

  /**
   * Default constructor for DashBoard resource
   * 
   * @param pActions
   *          the actions wanted
   */
  public DashBoardResourceImpl(final PermissionAction... pActions)
  {
    clazz = DashBoard.class;
    actions = Arrays.asList(pActions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getResource()
  {
    return clazz;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PermissionAction> getActions()
  {
    return actions;
  }

}
