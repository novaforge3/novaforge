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
package org.novaforge.forge.core.plugins.categories.scm;

import org.novaforge.forge.core.plugins.categories.PluginCategoryService;

import java.util.List;

/**
 * Interface containing all the functional services specific to this category.
 *
 * @author rols-p
 * @author Guillaume Lamirand
 */
public interface SCMCategoryService extends PluginCategoryService
{
  /**
   * This method returns the sources tree of an SCM application
   *
   * @param pForgeId
   * @param pInstanceId
   * @param pJSONParameter
   * @param pCurrentUser
   * @return ApplicationRequestBean
   * @throws SCMServiceException
   */
  SCMNodeEntryBean getSourcesTree(final String pForgeId, final String pInstanceId, final String pCurrentUser)
      throws SCMServiceException;

  /**
   * This method copy sources form SCM repository
   *
   * @param pForgeId
   * @param pInstanceId
   * @param pJSONParameter
   * @param pCurrentUser
   * @throws SCMServiceException
   */
  void copySources(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final String pJSONParameter) throws SCMServiceException;

  /**
   * This method allows to search a regex into source repository
   *
   * @param pForgeId
   * @param pInstanceId
   * @param pRepositoryPath
   * @param pCurrentUser
   * @param pJSONParameter
   * @return SCMSearchResultListBean the result list os SCMSearchResultBeans
   * @throws SCMServiceException
   */
  SCMSearchResultListBean searchInSourceCode(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pJSONParameter) throws SCMServiceException;

  /**
   * This method returns a repository id used for a instance
   *
   * @param pForgeId
   *          the forge id
   * @param pInstanceId
   *          the instance concerned
   * @return repository id
   * @throws SCMServiceException
   */
  String getRepositoryId(final String pForgeId, final String pInstanceId) throws SCMServiceException;

  /**
   * This method returns a number of commit for a instance
   *
   * @param pForgeId
   *          the forge id
   * @param pInstanceId
   *          the instance concerned
   * @param pCurrentUser
   *          the user doing the request
   * @param pNumberOfCommit
   *          the number of commit to return
   * @return list of commit
   * @throws SCMServiceException
   */
  List<SCMCommitBean> getCommits(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final int pNumberOfCommit) throws SCMServiceException;

}
