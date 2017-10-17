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
package org.novaforge.forge.core.security.internal.cas;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.security.cas.CasSecurityUrl;

import java.net.URL;

/**
 * This service is an implementation of {@link CasSecurityUrl }
 * 
 * @author Guillaume Lamirand
 */
public class CasSecurityUrlImpl implements CasSecurityUrl
{
  /**
   * Login uri for CAS
   */
  private static final String       LOGIN   = "login";
  /**
   * Logout uri for CAS
   */
  private static final String       LOGOUT  = "logout";
  /**
   * Service query for CAS
   */
  private static final String       SERVICE = "?service=";
  /**
   * {@link ForgeCfgService} injected by iPOJO
   */
  private ForgeConfigurationService forgeConfigurationService;

  /**
   * Bind method used by the container to inject {@link ForgeCfgService} service.
   *
   * @param pForgeConfigurationService
   *          the forgeConfigurationManager to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCasLogin()
  {
    final String casRootClean = getCleanUrl();
    return casRootClean + LOGIN;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCasLogin(final String pService)
  {
    final String casLogin = getCasLogin();
    return casLogin + SERVICE + pService;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCasLogout()
  {
    final String casRootClean = getCleanUrl();
    return casRootClean + LOGOUT;
  }

  /**
   * Get the clean CAS url
   * * @return url built with an ending slash
   */
  private String getCleanUrl()
  {
    final URL casRoot = forgeConfigurationService.getCasUrl();
    final String externalForm = casRoot.toExternalForm();
    final StringBuilder url = new StringBuilder(externalForm);
    if (!externalForm.endsWith("/"))
    {
      url.append("/");
    }
    return url.toString();
  }

}
