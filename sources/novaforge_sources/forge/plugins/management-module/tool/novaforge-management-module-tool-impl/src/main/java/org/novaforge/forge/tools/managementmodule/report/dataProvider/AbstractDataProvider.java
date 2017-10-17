/**
 * Copyright ( c ) 2011-2016, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.tools.managementmodule.report.dataProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;
import org.novaforge.forge.tools.managementmodule.report.dataProvider.burnDown.BurnDownDataProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * @author s267533
 *
 */
public abstract class AbstractDataProvider
{

  /**
   * Logger
   */
  protected final Log LOG = LogFactory.getLog( getClass());
  
  /**
   * Used to get service at runtime
   *
   * @see BurnDownDataProvider#getService(Class)
   */
  private BundleContext bundleContext;

  /**
   * set context
   * @param pBundleContext context
   */
  public void setBundleContext(final BundleContext pBundleContext)
  {
    bundleContext = pBundleContext;
  }
  

  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected <T> T getService(final Class<T> pClassService)
  {
    T service = null;
    if (bundleContext == null)
    {
      bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
    }
    final ServiceReference serviceRef = bundleContext.getServiceReference(pClassService.getName());
    if (serviceRef != null)
    {
      service = (T) bundleContext.getService(serviceRef);
    }
    else
    {
      LOG.error(String.format("OSGi service is not avalaible with [class_name=%s]", pClassService));
    }
    
    return service;
  }

  /**
   * Return a String containing informations of iteration and discipline for reports
   * 
   * @param functionalId
   * @return
   */
  public String getMetadatas(final Long lotId, final Long subLotId, final String disciplineFunctionalId)
  {
    try
    {
      final ProjectPlanManager projectPlanManager = this.getService(ProjectPlanManager.class);

      return projectPlanManager.getLotMetadatas(lotId, subLotId, disciplineFunctionalId);
    }
    catch (ManagementModuleException e)
    {
      LOG.error("Erreur lors de la recuperation des metadatas " + disciplineFunctionalId, e);

      return "";
    }
  }
  
}
