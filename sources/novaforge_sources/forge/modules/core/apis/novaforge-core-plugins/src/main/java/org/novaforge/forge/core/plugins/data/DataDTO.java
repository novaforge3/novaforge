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
package org.novaforge.forge.core.plugins.data;

/**
 * DTO which encapsulates all the application data.
 * 
 * @author rols-p
 * @author salvat-a
 */
public class DataDTO
{
  /**
   * DTO representing an Item.
   */
  private ItemDTO               itemDTO;
  /**
   * DTO representing a dokuwiki page.
   */
  private DokuwikiPageDTO       dokuwikiPageDTO;
  /**
   * DTO representing a dokuwiki attachment.
   */
  private DokuwikiAttachmentDTO dokuwikiAttachmentDTO;
  /**
   * DTO representing an Alfresco document
   */
  private AlfrescoDocumentDTO   alfrescoDocumentDTO;
  /**
   * DTO representing an Alfresco folder
   */
  private AlfrescoFolderDTO     alfrescoFolderDTO;

  /**
   * Default constructor needed by JAXB, should not be used.
   */
  public DataDTO()
  {
  }

  /**
   * @param itemDTO
   * @param dokuwikiPageDTO
   */
  public DataDTO(final ItemDTO itemDTO, final DokuwikiPageDTO dokuwikiPageDTO)
  {
    this.itemDTO = itemDTO;
    this.dokuwikiPageDTO = dokuwikiPageDTO;
  }

  /**
   * @param itemDTO
   * @param dokuwikiAttachmentDTO
   */
  public DataDTO(final ItemDTO itemDTO, final DokuwikiAttachmentDTO dokuwikiAttachmentDTO)
  {
    this.itemDTO = itemDTO;
    this.dokuwikiAttachmentDTO = dokuwikiAttachmentDTO;
  }

  /**
   * @param itemDTO
   * @param alfrescoDocumentDTO
   */
  public DataDTO(final ItemDTO itemDTO, final AlfrescoDocumentDTO alfrescoDocumentDTO)
  {
    this.itemDTO = itemDTO;
    this.alfrescoDocumentDTO = alfrescoDocumentDTO;
  }

  /**
   * @param itemDTO
   * @param alfrescoFolderDTO
   */
  public DataDTO(final ItemDTO itemDTO, final AlfrescoFolderDTO alfrescoFolderDTO)
  {
    this.itemDTO = itemDTO;
    this.alfrescoFolderDTO = alfrescoFolderDTO;
  }

  /**
   * @return the dokuwiki page DTO.
   */
  public DokuwikiPageDTO getDokuwikiPageDTO()
  {
    return dokuwikiPageDTO;
  }

  public void setDokuwikiPageDTO(final DokuwikiPageDTO dokuwikiPageDTO)
  {
    this.dokuwikiPageDTO = dokuwikiPageDTO;
  }

  /**
   * @return the dokuwiki attachment DTO.
   */
  public DokuwikiAttachmentDTO getDokuwikiAttachmentDTO()
  {
    return dokuwikiAttachmentDTO;
  }

  public void setDokuwikiAttachmentDTO(final DokuwikiAttachmentDTO dokuwikiAttachmentDTO)
  {
    this.dokuwikiAttachmentDTO = dokuwikiAttachmentDTO;
  }

  /**
   * @return the item DTO.
   */
  public ItemDTO getItemDTO()
  {
    return itemDTO;
  }

  public void setItemDTO(final ItemDTO itemDTO)
  {
    this.itemDTO = itemDTO;
  }

  public AlfrescoDocumentDTO getAlfrescoDocumentDTO()
  {
    return alfrescoDocumentDTO;
  }

  public void setAlfrescoDocumentDTO(final AlfrescoDocumentDTO alfrescoDocumentDTO)
  {
    this.alfrescoDocumentDTO = alfrescoDocumentDTO;
  }

  public AlfrescoFolderDTO getAlfrescoFolderDTO()
  {
    return alfrescoFolderDTO;
  }

  public void setAlfrescoFolderDTO(final AlfrescoFolderDTO alfrescoFolderDTO)
  {
    this.alfrescoFolderDTO = alfrescoFolderDTO;
  }
}
