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
package org.novaforge.forge.ui.forge.reference.server;

import org.novaforge.forge.commons.technical.file.FileService;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.presenters.TemplateNodePresenter;
import org.novaforge.forge.core.organization.presenters.TemplatePresenter;
import org.novaforge.forge.core.organization.presenters.TemplateRolePresenter;
import org.novaforge.forge.core.organization.services.TemplateLoaderService;
import org.novaforge.forge.core.plugins.services.PluginsCategoryManager;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authorization.AuthorizationService;
import org.novaforge.forge.core.security.authorization.PermissionHandler;
import org.novaforge.forge.reference.facade.ReferenceService;
import org.novaforge.forge.reference.tool.ReferenceToolService;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class OSGiHelper
{

  protected static ForgeConfigurationService getForgeConfigurationService()
  {
    return getService(ForgeConfigurationService.class);
  }

  @SuppressWarnings("unchecked")
  protected static <T> T getService(final Class<T> pClassService)
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
      throw new IllegalArgumentException(String.format("Unable to get OSGi service with [interface=%s]", canonicalName),
                                         e);
    }
    return service;
  }

  protected static FileService getFileService()
  {
    return getService(FileService.class);
  }

  protected static ReferenceService getReferenceService()
  {
    return getService(ReferenceService.class);
  }

  protected static ReferenceToolService getReferenceToolService()
  {
    return getService(ReferenceToolService.class);
  }

  protected static PluginsManager getPluginsManager()
  {
    return getService(PluginsManager.class);
  }

  protected static TemplatePresenter getTemplatePresenter()
  {
    return getService(TemplatePresenter.class);
  }

  protected static TemplateRolePresenter getTemplateRolePresenter()
  {
    return getService(TemplateRolePresenter.class);
  }

  protected static TemplateNodePresenter getTemplateNodePresenter()
  {
    return getService(TemplateNodePresenter.class);
  }

  protected static AuthorizationService getAuthorizationService()
  {
    return getService(AuthorizationService.class);
  }

  protected static PermissionHandler getPermissionHandler()
  {
    return getService(PermissionHandler.class);
  }

  protected static PluginsCategoryManager getPluginsCategoryManager()
  {
    return getService(PluginsCategoryManager.class);
  }

  protected static TemplateLoaderService getTemplateLoaderService()
  {
    return getService(TemplateLoaderService.class);
  }

}
