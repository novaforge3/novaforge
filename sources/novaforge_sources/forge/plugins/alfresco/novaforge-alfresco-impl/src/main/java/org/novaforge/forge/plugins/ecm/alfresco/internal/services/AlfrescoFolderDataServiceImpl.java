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


package org.novaforge.forge.plugins.ecm.alfresco.internal.services;

import org.novaforge.forge.core.plugins.data.AlfrescoFolderDTO;
import org.novaforge.forge.core.plugins.data.DataDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISException;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISHelper;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoFolder;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoDataType;

public class AlfrescoFolderDataServiceImpl extends AlfrescoAbstractDataServiceImpl
{
   @Override
   protected DataDTO getData(AlfrescoCMISHelper connector, ItemDTO item) throws AlfrescoCMISException
   {
      String dataId = item.getReference().getReferenceId();

      AlfrescoFolder folder = alfrescoCMISClient.getFolder(connector, dataId);

      AlfrescoFolderDTO folderDTO = new AlfrescoFolderDTO();
      folderDTO.setName(folder.getName());
      folderDTO.setPath(folder.getPath());
      folderDTO.setParentPath(folder.getParentPath());

      return new DataDTO(item, folderDTO);
   }

   @Override
   protected boolean createData(AlfrescoCMISHelper connector, DataDTO data) throws AlfrescoCMISException
   {
      return alfrescoCMISClient.createFolder(connector, convertData(data));
   }

   @Override
   protected boolean updateData(AlfrescoCMISHelper connector, DataDTO data) throws AlfrescoCMISException
   {
      return alfrescoCMISClient.updateFolder(connector, convertData(data));
   }

   @Override
   protected boolean deleteData(AlfrescoCMISHelper connector, ItemDTO item) throws AlfrescoCMISException
   {
      return alfrescoCMISClient.deleteFolder(connector, item.getReference().getReferenceId());
   }

   private AlfrescoFolder convertData(DataDTO data)
   {
      AlfrescoFolderDTO folderDTO = data.getAlfrescoFolderDTO();

      AlfrescoFolder folder = alfrescoResourceBuilder.newFolder();
      folder.setName(folderDTO.getName());
      folder.setPath(folderDTO.getPath());
      folder.setParentPath(folderDTO.getParentPath());
      folder.setPath(folderDTO.getPath());
      folder.setType(AlfrescoDataType.FOLDER.value());
      return folder;
   }
}
