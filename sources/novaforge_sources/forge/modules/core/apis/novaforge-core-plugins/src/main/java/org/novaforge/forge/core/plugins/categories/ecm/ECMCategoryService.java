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
package org.novaforge.forge.core.plugins.categories.ecm;

import org.novaforge.forge.core.plugins.categories.PluginCategoryService;

/**
 * Interface containing all the functional services specific to this category.
 * 
 * @author rols-p
 * @author Guillaume Lamirand
 */
public interface ECMCategoryService extends PluginCategoryService
{

  /**
   * This method allows to get the root document of a repository defined by its instance id.
   * 
   * @param pForgeId
   *          represents the forge id of the instance
   * @param pInstanceId
   *          represents the instance id of the root repository
   * @param pCurrentUser
   *          represents the current user who is asking
   * @return root document bean which contains all structure
   * @throws ECMServiceException
   */
  DocumentNodeBean getRepositoryTree(final String pForgeId, final String pInstanceId,
      final String pCurrentUser) throws ECMServiceException;

  /**
   * This method allows to copy a set of documents to a specific directory.
   * <p>
   * The parameters should be defined in that way :
   * <ul>
   * <li>First parameter : 'path' = 'target directory'</li>
   * <li>Second parameter : 'documents' = 'list of document id'</li>
   * </ul>
   * </br> { "path": "/tmp/documents", "documents": [ { "id1" }, { "id2" }, { "id3" } ] }
   * </p>
   * 
   * @param pForgeId
   *          represents the forge id of the instance
   * @param pInstanceId
   *          represents the instance id of the root repository
   * @param pCurrentUser
   *          represents the current user who is asking
   * @param pJSONParameter
   *          represents a JSON object which defines the parameters needed to copy the content asking
   * @throws ECMServiceException
   */
  void copyRepositoryContent(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final String pJSONParameter) throws ECMServiceException;
}
