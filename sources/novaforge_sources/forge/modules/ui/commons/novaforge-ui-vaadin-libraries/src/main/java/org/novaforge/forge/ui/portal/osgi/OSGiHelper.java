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
package org.novaforge.forge.ui.portal.osgi;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.core.security.cas.CasSecurityUrl;
import org.novaforge.forge.portal.services.PortalMessages;
import org.novaforge.forge.portal.services.PortalModuleService;
import org.novaforge.forge.portal.services.PortalService;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Guillaume Lamirand
 */
public class OSGiHelper
{

  /**
   * This method allows to obtain a {@link CasSecurityUrl} service implementation available in the
   * OSGi context.
   * 
   * @return implementation of the {@link CasSecurityUrl} service found
   */
  public static CasSecurityUrl getCasSecurityUrl()
  {
    return getService(CasSecurityUrl.class);
  }

  /**
   * This method allows to obtain a service implementation available in the
   * OSGi context.
   *
   * @param pClassService
   *          represents the instance of the service you are looking for @
   * @param <T>
   *          represents the class of the service
   * @return implementation of the service
   */
  @SuppressWarnings("unchecked")
  public static <T> T getService(final Class<T> pClassService)
  {
    final String canonicalName = pClassService.getCanonicalName();
    T            service       = null;
    try
    {
      final InitialContext initialContext = new InitialContext();
      service = (T) initialContext.lookup(String.format("osgi:service/%s", canonicalName));

    }
    catch (final NamingException e)
    {
      // We dont want to throw any exception here.
    }
    return service;
  }

  /**
   * This method allows to obtain a {@link ForgeConfigurationService} service implementation available in the
   * OSGi context.
   *
   * @return implementation of the {@link ForgeConfigurationService} service found
   */
  public static ForgeConfigurationService getForgeConfigurationService()
  {
    return getService(ForgeConfigurationService.class);
  }

  /**
   * This method allows to obtain a {@link AuthentificationService} service implementation available in the
   * OSGi context.
   *
   * @return implementation of the {@link AuthentificationService} service found
   */
  public static AuthentificationService getAuthentificationService()
  {
    return getService(AuthentificationService.class);
  }

  /**
   * This method allows to obtain a {@link PortalService} service implementation available in the OSGi
   * context.
   *
   * @return implementation of the {@link PortalService} service found
   */
  public static PortalService getPortalService()
  {
    return getService(PortalService.class);
  }

  /**
   * This method allows to obtain a {@link PortalMessages} service implementation available in the OSGi
   * context.
   *
   * @return implementation of the {@link PortalMessages} service found
   */
  public static PortalMessages getPortalMessages()
  {
    return getService(PortalMessages.class);
  }

  /**
   * This method allows to obtain a {@link PortalService} service implementation available in the OSGi
   * context.
   *
   * @return implementation of the {@link PortalService} service found
   */
  public static PortalModuleService getPortalModuleService()
  {
    return getService(PortalModuleService.class);
  }
}
