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
package org.novaforge.forge.core.plugins.categories.quality;

import org.novaforge.forge.core.plugins.categories.PluginCategoryService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface containing all the functional services specific to this category.
 * 
 * @author sbenoist
 */
public interface QualityCategoryService extends PluginCategoryService
{

  /**
   * This method returns a map with metrics for keys and measures for values for a resourceId
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pResourceId
   * @param pMetrics
   * @param pFrom
   * @param pTo
   * @return Map<QualityMetric, List<QualityMeasureBean>>
   * @throws QualityServiceException
   */
  Map<QualityMetric, List<QualityMeasureBean>> getMeasures(String pForgeId, String pInstanceId,
      String pCurrentUser, String pResourceId, Set<QualityMetric> pMetrics, Date pFrom, Date pTo)
      throws QualityServiceException;

  /**
   * This method returns the list of resources included in the Quality Project
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @return
   * @throws QualityServiceException
   */
  List<QualityResourceBean> getResourcesByProject(String pForgeId, String pInstanceId, String pCurrentUser)
      throws QualityServiceException;
}
