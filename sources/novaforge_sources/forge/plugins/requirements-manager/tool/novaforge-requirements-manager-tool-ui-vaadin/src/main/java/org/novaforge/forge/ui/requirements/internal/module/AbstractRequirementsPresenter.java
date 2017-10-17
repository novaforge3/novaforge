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
package org.novaforge.forge.ui.requirements.internal.module;

import com.vaadin.ui.UI;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsManagementServiceException;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

/**
 * @author Jeremy Casery
 */
public abstract class AbstractRequirementsPresenter extends AbstractPortalPresenter
{

  /**
   * The projectID in the forge
   */
  private final String projectForgeId;
  /**
   * The projectID in the tool
   */
  private String       projectToolId;

  /**
   * Default constructor
   * 
   * @param pPortalContext
   *          the initial context
   */
  public AbstractRequirementsPresenter(final PortalContext pPortalContext)
  {
    super(pPortalContext);
    projectForgeId = pPortalContext.getAttributes().get(PortalContext.KEY.APP_ID);
    try
    {
      projectToolId = RequirementsModule.getRequirementManagerFunctionalService()
          .getProjectId(projectForgeId);
    }
    catch (RequirementsManagementServiceException e)
    {
      ExceptionCodeHandler.showNotificationError(RequirementsModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return RequirementsModule.getPortalModuleId();
  }

  /**
   * Get the Project Id in the forge
   * 
   * @return the forge project id
   */
  protected String getProjectForgeId()
  {
    return projectForgeId;
  }

  /**
   * Get the project Id in the tool
   * 
   * @return the tool project id
   */
  protected String getProjectToolId()
  {
    return projectToolId;
  }

}
