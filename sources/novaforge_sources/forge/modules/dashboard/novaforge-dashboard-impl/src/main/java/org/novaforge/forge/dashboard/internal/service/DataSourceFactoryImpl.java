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
package org.novaforge.forge.dashboard.internal.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.novaforge.forge.dashboard.internal.model.DataSourceOptionsImpl;
import org.novaforge.forge.dashboard.model.DataSourceOptions;
import org.novaforge.forge.dashboard.service.DataSourceFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public class DataSourceFactoryImpl implements DataSourceFactory
{

  /**
   * {@inheritDoc}
   */
  @Override
  public DataSourceOptions buildOptions(final boolean pNeedsProject, final boolean pIsMultiProjects,
                                        final boolean pNeedsApplication, final boolean pIsMultiApplications)
      throws IllegalArgumentException
  {
    if ((!pNeedsProject) && (pNeedsApplication))
    {
      throw new IllegalArgumentException(String
                                             .format("If widget need at least one application, you should require a project too, so the parameters are incompatible [needsProject=%s, pIsMultiProjects=%s, pNeedsApplication=%s, pIsMultiApplications=%s]",
                                                     pNeedsProject, pIsMultiProjects, pNeedsApplication,
                                                     pIsMultiApplications));

    }
    return new DataSourceOptionsImpl(pNeedsProject, pIsMultiProjects, pNeedsApplication, pIsMultiApplications);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String buildDataSource(final Map<String, List<String>> pDataSource)
  {
    return JSONObject.fromObject(pDataSource).toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, List<String>> readDataSource(final String pDataSource)
  {
    final Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
    final JSONObject  root     = JSONObject.fromObject(pDataSource);
    final Set<String> entrySet = root.keySet();
    for (final String key : entrySet)
    {
      final JSONArray jsonArray = (JSONArray) root.get(key);
      final Collection<String> collection = JSONArray.toCollection(jsonArray);
      final List<String> arrayList = new ArrayList<String>(collection);
      returnMap.put(key, arrayList);

    }
    return returnMap;
  }
}
