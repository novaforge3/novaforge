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
package org.novaforge.forge.distribution.reference.internal.scheduling;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.distribution.reference.service.DiffusionService;

/**
 * @author sbenoist
 */
public class DiffusionProcessor implements Processor
{
  private static final Log LOGGER = LogFactory.getLog(DiffusionProcessor.class);
  private DiffusionService          diffusionService;
  private UserPresenter             userPresenter;
  private ForgeConfigurationService forgeConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void process(final Exchange exchange) throws Exception
  {
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Starting the diffusion of the reference project.");
    }

    diffusionService.propagateReferenceProject();
    diffusionService.propagateTemplates();
    diffusionService.propagateReferenceTools();
  }

  /**
   * @param diffusionService
   *          the diffusionService to set
   */
  public void setDiffusionService(final DiffusionService diffusionService)
  {
    this.diffusionService = diffusionService;
  }

  /**
   * @param userPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter userPresenter)
  {
    this.userPresenter = userPresenter;
  }

  /**
   * @param forgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
  {
    this.forgeConfigurationService = forgeConfigurationService;
  }

}
