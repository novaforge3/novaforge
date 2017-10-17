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
package org.novaforge.forge.tools.deliverymanager.ui.server;

import org.novaforge.forge.commons.technical.file.FileService;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPermissionService;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPluginService;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPresenter;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryRepositoryService;
import org.novaforge.forge.tools.deliverymanager.services.TemplateReportPresenter;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Guillaume Lamirand
 */
public class OSGiServiceGetter
{

  protected static DeliveryPresenter getDeliveryManager()
  {
    return getService(DeliveryPresenter.class);
  }

  @SuppressWarnings("unchecked")
  private static <T> T getService(final Class<T> pClassService)
  {

    String canonicalName = pClassService.getCanonicalName();
    T      service       = null;
    try
    {
      InitialContext initialContext = new InitialContext();
      service = (T) initialContext.lookup(String.format("osgi:service/%s", canonicalName));

    }
    catch (NamingException e)
    {
      throw new IllegalArgumentException(String.format("Unable to get OSGi service with [interface=%s]", canonicalName),
                                         e);
    }
    return service;
  }

  protected static DeliveryRepositoryService getDeliveryRepositoryService()
  {
    return getService(DeliveryRepositoryService.class);
  }

  protected static TemplateReportPresenter getTemplateReportManager()
  {
    return getService(TemplateReportPresenter.class);
  }

  protected static DeliveryPluginService getDeliveryPluginService()
  {
    return getService(DeliveryPluginService.class);
  }

  protected static FileService getCommonFileService()
  {
    return getService(FileService.class);
  }

  protected static DeliveryPermissionService getDeliveryPermissionService()
  {
    return getService(DeliveryPermissionService.class);
  }
}
