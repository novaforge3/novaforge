/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
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
package org.novaforge.forge.commands.portal.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.portal.models.PortalModule;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * This command will display in shell all available module
 * 
 * @author Guillaume Lamirand
 */
@Command(scope = "portal", name = "modules", description = "Lists all available portal module")
public class ModulesShellCommand extends OsgiCommandSupport
{

  /**
   * Reference to implementation of {@link BundleContext}
   */
  private BundleContext context;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    final Collection<ServiceReference<PortalModule>> modules = context.getServiceReferences(
        PortalModule.class, "(module-id=*)");
    final Map<String, List<ServiceReference<PortalModule>>> bundles = new HashMap<String, List<ServiceReference<PortalModule>>>();
    for (final ServiceReference<PortalModule> serviceReference : modules)
    {
      final String bundleId = String.valueOf(serviceReference.getBundle().getBundleId());
      if (bundles.containsKey(bundleId))
      {
        bundles.get(bundleId).add(serviceReference);
      }
      else
      {
        final List<ServiceReference<PortalModule>> portalRef = new ArrayList<ServiceReference<PortalModule>>();
        portalRef.add(serviceReference);
        bundles.put(bundleId, portalRef);
      }
    }
    final Map<String, List<ServiceReference<PortalModule>>> sortedBundles = new TreeMap<String, List<ServiceReference<PortalModule>>>(
        bundles);
    final ShellTable table = new ShellTable();
    table.getHeader().add("Bundle");
    table.getHeader().add("Version");
    table.getHeader().add("Id");
    final Set<Entry<String, List<ServiceReference<PortalModule>>>> entrySet = sortedBundles.entrySet();
    for (final Entry<String, List<ServiceReference<PortalModule>>> entry : entrySet)
    {
      boolean isFirst = true;
      for (final ServiceReference<PortalModule> ref : entry.getValue())
      {
        final List<String> row = table.addRow();
        if (isFirst)
        {
          row.add(String.valueOf(ref.getBundle().getBundleId()));
          row.add(ref.getBundle().getVersion().toString());
          isFirst = false;
        }
        else
        {
          row.add(" ");
          row.add(" ");
        }
        row.add(ref.getProperty(PortalModule.MODULE_ID).toString());
      }
    }
    table.print();
    return null;
  }

  /**
   * Use by container to inject {@link BundleContext}
   * 
   * @param pContext
   *          the context to set
   */
  public void setContext(final BundleContext pContext)
  {
    context = pContext;
  }

}
