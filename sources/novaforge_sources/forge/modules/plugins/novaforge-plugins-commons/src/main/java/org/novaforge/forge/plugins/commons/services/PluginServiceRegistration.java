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
package org.novaforge.forge.plugins.commons.services;

import org.novaforge.forge.core.plugins.categories.PluginCategoryService;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Guillaume Lamirand
 */
@SuppressWarnings("rawtypes")
public class PluginServiceRegistration
{

  private final ServiceRegistration<PluginService>         pluginServiceRegistration;
  private final ServiceRegistration<PluginCategoryService> categoryServiceRegistration;
  private final BundleContext                              bundleContext;

  /**
   * Default constructor
   * 
   * @param pBundleContext
   *          the {@link BundleContext} associated to the bundle
   * @param pPluginServiceRegistration
   *          the {@link ServiceRegistration} associated to {@link PluginService}
   * @param pCategoryServiceRegistration
   *          the {@link ServiceRegistration} associated to {@link PluginCategoryService}
   */
  @SuppressWarnings("unchecked")
  public PluginServiceRegistration(final BundleContext pBundleContext,
      final ServiceRegistration pPluginServiceRegistration,
      final ServiceRegistration pCategoryServiceRegistration)
  {
    bundleContext = pBundleContext;
    pluginServiceRegistration = pPluginServiceRegistration;
    categoryServiceRegistration = pCategoryServiceRegistration;
    setServicesProperties();
  }

  private void setServicesProperties()
  {
    final ServiceReference<PluginService> reference = pluginServiceRegistration.getReference();
    final PluginService service = bundleContext.getService(reference);
    final String pluginUUID = service.getServiceUUID().toString();
    if (reference != null)
    {
      manageService(pluginServiceRegistration, pluginUUID);
      manageService(categoryServiceRegistration, pluginUUID);
    }
  }

  private <T> void manageService(final ServiceRegistration<T> pServiceRegistration, final String pPluginUUID)
  {
    final ServiceReference<T> reference = pServiceRegistration.getReference();
    if (reference != null)
    {
      // Building new dictionary
      final Dictionary<String, Object> dictionary = new Hashtable<String, Object>();
      final String[] propertyKeys = reference.getPropertyKeys();
      for (final String key : propertyKeys)
      {
        dictionary.put(key, reference.getProperty(key));
      }
      dictionary.put(PluginService.ID_PROPERTY, pPluginUUID);
      // update properties
      pServiceRegistration.setProperties(dictionary);
    }
  }

}