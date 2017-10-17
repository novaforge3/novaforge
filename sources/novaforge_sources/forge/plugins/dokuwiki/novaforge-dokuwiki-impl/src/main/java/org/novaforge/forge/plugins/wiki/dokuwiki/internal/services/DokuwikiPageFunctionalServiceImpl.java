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

import org.apache.xmlrpc.client.XmlRpcClient;
import org.novaforge.forge.core.plugins.data.DataDTO;
import org.novaforge.forge.core.plugins.data.DokuwikiPageDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.data.ItemReferenceDTO;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiClientException;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiPage;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiPageInfo;
import org.novaforge.forge.plugins.wiki.dokuwiki.services.DokuwikiItemType;

import java.util.ArrayList;
import java.util.List;

public class DokuwikiPageFunctionalServiceImpl extends AbstractDokuwikiFunctionalServiceImpl
{

  @Override
  protected void deleteData(final XmlRpcClient connector, final ItemDTO item) throws DokuwikiClientException
  {
    final String dataId = item.getReference().getReferenceId();
    dokuwikiXmlRpcClient.deletePage(connector, dataId);
  }

  @Override
  protected void createData(final XmlRpcClient connector, final DataDTO data) throws DokuwikiClientException
  {
    final DokuwikiPageDTO pageDTO = data.getDokuwikiPageDTO();
    final DokuwikiPage page = dokuwikiResourceBuilder.newPage();
    page.setName(pageDTO.getName());
    page.setDescription(pageDTO.getDescription());
    page.setContent(pageDTO.getContent());

    dokuwikiXmlRpcClient.createPage(connector, page);
  }

  @Override
  protected DataDTO getDataContent(final XmlRpcClient connector, final ItemDTO item) throws DokuwikiClientException
  {
    final String dataId = item.getReference().getReferenceId();

    final DokuwikiPage page = dokuwikiXmlRpcClient.getPageContent(connector, dataId);
    final DokuwikiPageDTO pageDTO = new DokuwikiPageDTO(page.getName(), page.getDescription(), page.getContent());
    return new DataDTO(item, pageDTO);
  }

  @Override
  protected List<ItemReferenceDTO> getDataInfos(final XmlRpcClient connector, final String projectId)
      throws DokuwikiClientException
  {
    final List<ItemReferenceDTO> itemReferences = new ArrayList<ItemReferenceDTO>();

    final List<DokuwikiPageInfo> pagesInfo = dokuwikiXmlRpcClient.getNameSpacePages(connector, projectId);
    for (final DokuwikiPageInfo pageInfo : pagesInfo)
    {
      final String modificationComparator = buildPageModificationComparator(pageInfo);
      itemReferences.add(new ItemReferenceDTO(pageInfo.getId(), modificationComparator, DokuwikiItemType.PAGE.name()));
    }
    return itemReferences;
  }

  private String buildPageModificationComparator(final DokuwikiPageInfo pageInfo)
  {
    return pageInfo.getSize();
  }
}
