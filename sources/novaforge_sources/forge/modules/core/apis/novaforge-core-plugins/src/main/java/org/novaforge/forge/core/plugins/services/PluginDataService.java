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
package org.novaforge.forge.core.plugins.services;

import org.novaforge.forge.core.plugins.data.DataDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.data.ItemReferenceDTO;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;

import javax.jws.WebService;
import java.util.List;

/**
 * The Object data must be compliant with general JAXB databinding: A POJO class, containing a default
 * constructor, only getter and setter with simple types or Objects containing simple types. List and Set are
 * allowed but not Map and no interfaces (except List and Set). The POJOs can be defined in the bundle
 * novaforge-plugins-commons, in the package org.novaforge.forge.plugins.commons.plugins.data.
 * 
 * @author rols-p
 */
@WebService
public interface PluginDataService
{
  /**
   * @param forgeId
   * @param applicationInstanceId
   * @return the list of item references for a given forge and application instance.
   * @throws PluginServiceException
   */
  List<ItemReferenceDTO> getDataReferences(String forgeId, String applicationInstanceId)
      throws PluginServiceException;

  /**
   * Returns an object containing the tool/application data to be propagated.
   * 
   * @param forgeId
   * @param applicationInstanceId
   * @param item
   * @return the Object containing the data to be propagated.
   * @throws PluginServiceException
   */
  DataDTO getData(String forgeId, String applicationInstanceId, ItemDTO item) throws PluginServiceException;

  /**
   * update the data of a tool/application from a propagated Object stream.
   * 
   * @param forgeId
   * @param applicationInstanceId
   * @param data
   *          the Object containing the data to be propagated.
   * @throws PluginServiceException
   */
  void putData(String forgeId, String applicationInstanceId, DataDTO data) throws PluginServiceException;
}
