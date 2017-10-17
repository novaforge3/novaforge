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
package org.novaforge.forge.distribution.register.initialization.internal;

import org.novaforge.forge.distribution.register.client.DistributionInitializerService;
import org.novaforge.forge.distribution.register.exceptions.ForgeDistributionException;

/**
 * Component created only if the administrator wants to manually re-initialize the distribution by starting
 * the hosting bundle.
 * 
 * @author Mohamed IBN EL AZZOUZI, rols-p
 * @date 25 janv. 2012
 */
public class ForgeDistributionManualInitializer
{

  private DistributionInitializerService distributionInitializerService;

  public void init()
  {
    try
    {
      distributionInitializerService.initializeDistribution();
    }
    catch (final ForgeDistributionException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * @param distributionInitializerService
   *          the distributionInitializerService to set
   */
  public void setDistributionInitializerService(
      final DistributionInitializerService distributionInitializerService)
  {
    this.distributionInitializerService = distributionInitializerService;
  }

}
