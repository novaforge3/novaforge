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

package org.novaforge.forge.plugins.scm.gitlab.internal;

import org.gitlab.api.models.GitlabCommit;
import org.gitlab.api.models.GitlabCommitDiff;
import org.gitlab.api.models.GitlabProject;
import org.novaforge.forge.core.plugins.categories.ecm.ECMServiceException;
import org.novaforge.forge.core.plugins.categories.scm.SCMCategoryService;
import org.novaforge.forge.core.plugins.categories.scm.SCMCommitBean;
import org.novaforge.forge.core.plugins.categories.scm.SCMNodeEntryBean;
import org.novaforge.forge.core.plugins.categories.scm.SCMSearchResultListBean;
import org.novaforge.forge.core.plugins.categories.scm.SCMServiceException;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.categories.beans.SCMCommitBeanImpl;
import org.novaforge.forge.plugins.categories.beans.SCMSearchResultListBeanImpl;
import org.novaforge.forge.plugins.commons.services.AbstractPluginCategoryService;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestClient;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestConnector;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Gauthier Cart
 */
public class GitlabCategoryServiceImpl extends AbstractPluginCategoryService implements SCMCategoryService
{

  private static final String        PROPERTY_FILE = "gitlab";

  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;

  /**
   * Reference to service implementation of {@link InstanceConfigurationDAO}
   */
  private InstanceConfigurationDAO   instanceConfigurationDAO;

  /**
   * Reference to service implementation of {@link GitlabRestClient}
   */
  private GitlabRestClient           gitlabRestClient;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getApplicationAccessInfo(final String pInstanceId, final Locale pLocale)
      throws PluginServiceException
  {
    /*
     * #TODO
     */
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getPropertyFileName()
  {
    return PROPERTY_FILE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SCMNodeEntryBean getSourcesTree(final String pForgeId, final String pInstanceId,
      final String pCurrentUser) throws SCMServiceException

  {
    /*
     * #TODO
     */
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void copySources(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final String pJSONParameter) throws SCMServiceException
  {
    /*
     * #TODO
     */
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SCMSearchResultListBean searchInSourceCode(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pJSONParameter) throws SCMServiceException
  {
    /*
     * #TODO
     */
    return new SCMSearchResultListBeanImpl();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRepositoryId(final String pForgeId, final String pInstanceId) throws SCMServiceException
  {
    // Get instance object
    final InstanceConfiguration instanceConfiguration = getInstance(pInstanceId);

    // Default id
    String returnId = instanceConfiguration.getToolProjectId();

    // Check if instance got is mapped to the correct forge id
    checkForgeId(pForgeId, instanceConfiguration);
    try
    {
      // Obtain Gitlab connector
      final GitlabRestConnector connector = gitlabRestClient.getConnector(
          pluginConfigurationService.getClientURL(instanceConfiguration.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientPwd());

      if ((returnId != null) && (!"".equals(returnId)))
      {
        final GitlabProject project = gitlabRestClient.getProject(connector,
            instanceConfiguration.getToolProjectId());
        returnId = project.getPathWithNamespace();
      }
    }
    catch (final GitlabRestException | PluginServiceException e)
    {
      throw new SCMServiceException(String.format("Unable to get project with toolProjectId=%s", returnId), e);
    }
    // Repository id
    return returnId;
  }

  /**
   * @param pForgeId
   * @param pInstanceId
   *
   * @return
   *
   * @throws ECMServiceException
   */
  private InstanceConfiguration getInstance(final String pInstanceId) throws SCMServiceException
  {
    return instanceConfigurationDAO.findByInstanceId(pInstanceId);
  }

  /**
   * @param pForgeId
   * @param instance
   *
   * @throws ECMServiceException
   */
  private void checkForgeId(final String pForgeId, final InstanceConfiguration instance) throws SCMServiceException
  {
    if (instance != null)
    {
      if (!instance.getForgeId().equals(pForgeId))
      {
        throw new SCMServiceException("The forge id given as parameter doesn''t match with the instance id");
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SCMCommitBean> getCommits(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final int pNumberOfCommit) throws SCMServiceException
  {
    // Get instance object
    final List<SCMCommitBean> lastCommitList = new ArrayList<>();

    // Get instance object
    final InstanceConfiguration instanceConfiguration = getInstance(pInstanceId);

    // Check if instance got is mapped to the correct forge id
    checkForgeId(pForgeId, instanceConfiguration);

    final String toolProjectId = instanceConfiguration.getToolProjectId();
    try
    {
      // Obtain Gitlab connector
      final GitlabRestConnector connector = gitlabRestClient.getConnector(
          pluginConfigurationService.getClientURL(instanceConfiguration.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientPwd());
      if ((toolProjectId != null) && (!"".equals(toolProjectId)))
      {
        final int projectId = Integer.valueOf(toolProjectId);
        final List<GitlabCommit> commits = gitlabRestClient.getCommits(connector, projectId, null);
        for (final GitlabCommit gitlabCommit : commits)
        {
          final SCMCommitBean scmCommitBean = toSCMCommitBean(gitlabCommit);
          scmCommitBean.setChanges(getChanges(connector, projectId, gitlabCommit.getId()));
          lastCommitList.add(scmCommitBean);

          if (lastCommitList.size() >= pNumberOfCommit)
          {
            break;
          }
        }
      }
    }
    catch (final GitlabRestException | PluginServiceException e)
    {
      throw new SCMServiceException(String.format("Unable to get project commits with toolProjectId=%s",
          toolProjectId), e);
    }
    return lastCommitList;
  }

  private SCMCommitBean toSCMCommitBean(final GitlabCommit pGitlabCommit)
  {
    final SCMCommitBean commit = new SCMCommitBeanImpl();
    commit.setRevision(pGitlabCommit.getShortId());
    commit.setAuthorEmail(pGitlabCommit.getAuthorEmail());
    commit.setComment(pGitlabCommit.getTitle());
    commit.setDate(pGitlabCommit.getCreatedAt());
    return commit;
  }

  private long getChanges(final GitlabRestConnector pConnector, final int pProjectId, final String pCommitHash)
      throws GitlabRestException
  {
    final List<GitlabCommitDiff> commits = gitlabRestClient.getCommitDiffs(pConnector, pProjectId,
        pCommitHash);
    return commits.size();
  }

  /**
   * Use by container to inject {@link GitlabRestClient}
   * 
   * @param pGitlabRestClient
   *          the gitlabRestClient to set
   */
  public void setGitlabRestClient(final GitlabRestClient pGitlabRestClient)
  {
    gitlabRestClient = pGitlabRestClient;
  }

  /**
   * Use by container to inject {@link InstanceConfigurationDAO}
   * 
   * @param pInstanceConfigurationDAO
   *          the instanceConfigurationDAO to set
   */
  public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
  {
    instanceConfigurationDAO = pInstanceConfigurationDAO;
  }

  /**
   * Use by container to inject {@link PluginConfigurationService}
   * 
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

}
