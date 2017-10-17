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
package org.novaforge.forge.plugins.wiki.dokuwiki.services;

import org.novaforge.forge.core.plugins.data.DataDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.data.ItemReferenceDTO;

import java.util.List;

/**
 * @author lamirang
 * @author salvat-a
 */
public interface DokuwikiFunctionalService
{

   /**
    * @param forgeId
    * @param instanceId
    * @return the list of dokuwiki data informations
    * @throws DokuwikiFunctionalException
    */
   List<ItemReferenceDTO> getDataInfos(final String forgeId, final String instanceId)
         throws DokuwikiFunctionalException;

   /**
    * @param forgeId
    * @param instanceId
    * @param item
    * @return the data fetched from dokuwiki corresponding to the data ID.
    * @throws DokuwikiFunctionalException
    */
   DataDTO getData(final String forgeId, final String instanceId, final ItemDTO item)
         throws DokuwikiFunctionalException;

   /**
    * @param forgeId
    * @param instanceId
    * @param data
    * @throws DokuwikiFunctionalException
    */
   void putData(final String forgeId, final String instanceId, final DataDTO data)
         throws DokuwikiFunctionalException;

   /**
    * @param forgeId
    * @param instanceId
    * @param item
    * @throws DokuwikiFunctionalException
    */
   void deleteData(final String forgeId, final String instanceId, final ItemDTO item)
         throws DokuwikiFunctionalException;
}