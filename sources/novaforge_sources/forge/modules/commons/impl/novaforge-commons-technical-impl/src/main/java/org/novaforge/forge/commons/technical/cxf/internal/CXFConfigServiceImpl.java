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
package org.novaforge.forge.commons.technical.cxf.internal;

import org.novaforge.forge.commons.technical.cxf.CxfConfigService;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;

/**
 * Implementation of {@link CxfConfigService}
 * 
 * @author Guillaume Lamirand
 * @see CxfConfigService
 */
public class CXFConfigServiceImpl implements CxfConfigService
{

  /**
   * Reference to {@link ForgeCfgService} service injected by the
   * container
   */
  private ForgeConfigurationService forgeConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getWebserviceUri(final String pEndpointName)
  {
    final String httpServerPort = getHttpServerPort();
    final String httpServerName = getHttpServerName();
    final String cxfPath = forgeConfigurationService.getCXFEndPoint();
    final String port = ((httpServerPort == null) || httpServerPort.isEmpty()) ? "" : ":" + httpServerPort;
    return String.format("http://%s%s/%s/%s", httpServerName, port, cxfPath, pEndpointName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHttpServerName()
  {
    return forgeConfigurationService.getLocalHostName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHttpServerPort()
  {
    return forgeConfigurationService.getLocalPort();
  }

  /**
   * Use by container to inject {@link ForgeCfgService} implementation
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

}
