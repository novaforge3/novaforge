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
package org.novaforge.forge.ui.dashboard.internal.client;

import com.google.common.base.Strings;
import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.dashboard.model.DashBoard.Type;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.dashboard.internal.module.DashboardModule;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;

/**
 * Used as default {@link AbstractPortalPresenter} for dashboard component
 * 
 * @author Guillaume Lamirand
 */
public abstract class AbstractDashBoardPresenter extends AbstractPortalPresenter
{

  /**
   * The type of this dashboard
   */
  private final DashBoard.Type type;
  /**
   * Current type id
   */
  private String typeId;

  /**
   * Default constructor
   * 
   * @param pPortalContext
   *          the source portal context
   */
  public AbstractDashBoardPresenter(final PortalContext pPortalContext)
  {
    super(pPortalContext);
    final String projectId = pPortalContext.getAttributes().get(PortalContext.KEY.PROJECTID);
    if ((!Strings.isNullOrEmpty(projectId))
        && (!DashboardModule.getProjectPresenter().isForgeProject(projectId)))
    {
      typeId = projectId;
      type = DashBoard.Type.PROJECT;
    }
    else
    {
      type = DashBoard.Type.USER;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return DashboardModule.getModuleId();
  }

  /**
   * Return the type Id defined for this presenter. If the type is User, the current user is retrieved in the
   * same time.
   * 
   * @return the typeId ie. projectId or user login
   */
  protected String getTypeId()
  {
    if ((Type.USER.equals(type)) && (Strings.isNullOrEmpty(typeId)))
    {
      typeId = DashboardModule.getAuthentificationService().getCurrentUser();
    }
    return typeId;
  }

  /**
   * Returns the type of dashboard wanted
   * 
   * @return the type
   */
  protected DashBoard.Type getType()
  {
    return type;
  }

  /**
   * Returns true if current has admin right on the dashboard
   * 
   * @return true if current user log on has admin right on the dashboard
   */
  protected boolean hasAdminRights()
  {
    return DashboardModule.getDashBoardService().hasAdminRight(type, typeId);
  }
}
