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
import org.novaforge.forge.core.plugins.data.DokuwikiAttachmentDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.data.ItemReferenceDTO;
import org.novaforge.forge.plugins.wiki.dokuwiki.client.DokuwikiClientException;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiAttachment;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiAttachmentInfo;
import org.novaforge.forge.plugins.wiki.dokuwiki.services.DokuwikiItemType;

import java.util.ArrayList;
import java.util.List;

public class DokuwikiAttachmentFunctionalServiceImpl extends AbstractDokuwikiFunctionalServiceImpl
{

  private static final String SEPARATOR = "--";

  /**
   * {@inheritDoc}
   */
  @Override
  protected void deleteData(final XmlRpcClient connector, final ItemDTO item) throws DokuwikiClientException
  {
    final String dataId = item.getReference().getReferenceId();
    dokuwikiXmlRpcClient.deleteAttachment(connector, dataId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void createData(final XmlRpcClient connector, final DataDTO data) throws DokuwikiClientException
  {
    final DokuwikiAttachmentDTO attachmentDTO = data.getDokuwikiAttachmentDTO();
    final DokuwikiAttachment attachment = dokuwikiResourceBuilder.newAttachment();
    attachment.setId(attachmentDTO.getName());
    attachment.setContent(attachmentDTO.getContent());

    dokuwikiXmlRpcClient.putAttachment(connector, attachment);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected DataDTO getDataContent(final XmlRpcClient connector, final ItemDTO item) throws DokuwikiClientException
  {
    final String dataId = item.getReference().getReferenceId();
    final DokuwikiAttachment attachment = dokuwikiXmlRpcClient.getAttachment(connector, dataId);
    final DokuwikiAttachmentDTO attachmentDTO = new DokuwikiAttachmentDTO(attachment.getId(), attachment.getContent());
    return new DataDTO(item, attachmentDTO);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected List<ItemReferenceDTO> getDataInfos(final XmlRpcClient connector, final String projectId)
      throws DokuwikiClientException
  {
    final List<ItemReferenceDTO> itemReferences = new ArrayList<ItemReferenceDTO>();
    final List<DokuwikiAttachmentInfo> infos = dokuwikiXmlRpcClient.getAttachments(connector, projectId, true);
    for (final DokuwikiAttachmentInfo info : infos)
    {
      final String modificationComparator = buildAttachmentModificationComparator(info);
      itemReferences.add(new ItemReferenceDTO(info.getId(), modificationComparator,
                                              DokuwikiItemType.ATTACHMENT.name()));
    }

    return itemReferences;
  }

  private String buildAttachmentModificationComparator(final DokuwikiAttachmentInfo attachmentInfo)
  {
    return attachmentInfo.getSize() + SEPARATOR + attachmentInfo.getPermissions() + SEPARATOR + attachmentInfo.isImg()
               + SEPARATOR + attachmentInfo.isWritable();
  }
}
