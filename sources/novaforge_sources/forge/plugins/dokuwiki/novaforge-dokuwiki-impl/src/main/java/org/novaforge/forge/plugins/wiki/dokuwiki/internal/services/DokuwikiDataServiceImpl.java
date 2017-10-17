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
package org.novaforge.forge.plugins.wiki.dokuwiki.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.data.DataDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.data.ItemReferenceDTO;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginDataService;
import org.novaforge.forge.plugins.wiki.dokuwiki.services.DokuwikiFunctionalException;

import java.util.ArrayList;
import java.util.List;

/**
 * Service used to propagate the Dokuwiki application content from a forge to others children forges.
 * 
 * @author rols-p
 * @author salvat-a
 */
public class DokuwikiDataServiceImpl implements PluginDataService
{
  private static final Log                 LOGGER = LogFactory.getLog(DokuwikiDataServiceImpl.class);

  /**
   * Reference to service implementation of {@link DokuwikiFunctionalServiceFactory}
   */
  private DokuwikiFunctionalServiceFactory dokuwikiFunctionalServiceFactory;

  @Override
  public List<ItemReferenceDTO> getDataReferences(final String forgeId, final String applicationInstanceId)
      throws PluginServiceException
  {
    final List<ItemReferenceDTO> itemReferences = new ArrayList<ItemReferenceDTO>();
    try
    {
      final List<ItemReferenceDTO> pagesInfo = dokuwikiFunctionalServiceFactory.getPageService()
          .getDataInfos(forgeId, applicationInstanceId);
      itemReferences.addAll(pagesInfo);

      final List<ItemReferenceDTO> attachmentsInfo = dokuwikiFunctionalServiceFactory.getAttachmentService()
          .getDataInfos(forgeId, applicationInstanceId);
      itemReferences.addAll(attachmentsInfo);
    }
    catch (final DokuwikiFunctionalException e)
    {
      final String msg = String.format("Error when getting dokuwiki data references [appli UUID=%s]",
          applicationInstanceId);
      LOGGER.error(msg);
      throw new PluginServiceException(msg, e);
    }
    return itemReferences;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataDTO getData(final String forgeId, final String applicationInstanceId, final ItemDTO itemDTO)
      throws PluginServiceException
  {

    if ((itemDTO == null) || (itemDTO.getReference() == null))
    {
      throw new PluginServiceException(String.format("The item object is null for the application UUID=%s.",
          applicationInstanceId));
    }

    DataDTO dataDTO;
    try
    {
      final ItemReferenceDTO reference = itemDTO.getReference();
      final String itemType = reference.getItemType();

      dataDTO = dokuwikiFunctionalServiceFactory.getService(itemType).getData(forgeId, applicationInstanceId,
          itemDTO);
    }
    catch (final DokuwikiFunctionalException e)
    {
      final String msg = String.format("Error when getting dokuwiki data [appli UUID=%s]",
          applicationInstanceId);
      LOGGER.error(msg);
      throw new PluginServiceException(msg, e);
    }
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Dokuwiki data retrieved: %s", dataDTO));
    }
    return dataDTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void putData(final String forgeId, final String applicationInstanceId, final DataDTO data)
      throws PluginServiceException
  {
    if (data == null)
    {
      throw new PluginServiceException(String.format(
          "Cannot put data, the data object is null for the application UUID=%s.", applicationInstanceId));
    }

    final ItemDTO item = data.getItemDTO();
    if (item == null)
    {
      throw new PluginServiceException(String.format(
          "Cannot put data, the item object is null for the application UUID=%s.", applicationInstanceId));
    }

    final ItemReferenceDTO itemReference = item.getReference();
    if (itemReference == null)
    {
      throw new PluginServiceException(String.format(
          "Cannot put data, the item reference object is null for the application UUID=%s.",
          applicationInstanceId));
    }

    try
    {
      final String itemType = itemReference.getItemType();
      switch (item.getAction())
      {
        case CREATE:
          dokuwikiFunctionalServiceFactory.getService(itemType).putData(forgeId, applicationInstanceId, data);
          break;
        case UPDATE:
          dokuwikiFunctionalServiceFactory.getService(itemType).putData(forgeId, applicationInstanceId, data);
          break;
        case DELETE:
          dokuwikiFunctionalServiceFactory.getService(itemType).deleteData(forgeId, applicationInstanceId,
              data.getItemDTO());
          break;
        default:
          break;
      }
    }
    catch (final DokuwikiFunctionalException e)
    {
      final String msg = String.format("Error when putting dokuwiki data [appli UUID=%s]",
          applicationInstanceId);
      LOGGER.error(msg);
      throw new PluginServiceException(msg, e);
    }
  }

  /**
   * Use by container to inject {@link DokuwikiFunctionalServiceFactory}
   * 
   * @param pDokuwikiFunctionalServiceFactory
   *          the dokuwikiFunctionalServiceFactory to set
   */
  public void setDokuwikiFunctionalServiceFactory(
      final DokuwikiFunctionalServiceFactory pDokuwikiFunctionalServiceFactory)
  {
    dokuwikiFunctionalServiceFactory = pDokuwikiFunctionalServiceFactory;
  }

}
