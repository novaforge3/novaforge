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
package org.novaforge.forge.plugins.scm.gitlab.internal.datamapper;

import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabNamespace;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.scm.constants.GitlabConstant;
import org.novaforge.forge.plugins.scm.gitlab.datamapper.GitlabResourceBuilder;

/**
 * This class is used in order to build object used by Gitlab WS.
 * 
 * @author Gauthier Cart
 */
public class GitlabResourceBuilderImpl implements GitlabResourceBuilder
{

  /*
   * PRIVATE = 0 unless const_defined?(:PRIVATE)
   * INTERNAL = 10 unless const_defined?(:INTERNAL)
   * PUBLIC = 20 unless const_defined?(:PUBLIC)
   */
  private static final Integer VISIBILITY_LEVEL = 0;

  @Override
  public GitlabUser buildGitlabUser(PluginUser pPluginUser)
  {
    GitlabUser gitlabUser = new GitlabUser();

    // Set User full name
    gitlabUser.setName(pPluginUser.getName() + " " + pPluginUser.getFirstName());

    // Set User e-mail
    gitlabUser.setEmail(pPluginUser.getEmail());

    // Set User username
    gitlabUser.setUsername(pPluginUser.getLogin());

    // Set default configuration
    gitlabUser.setCanCreateGroup(false);
    gitlabUser.setCanCreateProject(false);
    gitlabUser.setCanCreateTeam(false);
    gitlabUser.setAdmin(false);

    // Set Cas provider
    gitlabUser.setExternProviderName(GitlabConstant.GITLAB_CAS_PROVIDER);
    gitlabUser.setExternUid(pPluginUser.getLogin());

    return gitlabUser;
  }

  @Override
  public GitlabProject buildGitlabProject(PluginProject pPluginProject, String pInstanceName,
      GitlabUser pOwner, GitlabNamespace pNamespace)
  {

    GitlabProject gitlabProject = new GitlabProject();

    // Set project configuration
    gitlabProject.setName(pInstanceName);
    gitlabProject.setNamespace(pNamespace);
    gitlabProject.setOwner(pOwner);

    // Set description as null because we cannont get Instance description
    gitlabProject.setDescription(null);

    // Set default project configuration
    gitlabProject.setVisibilityLevel(VISIBILITY_LEVEL);
    gitlabProject.setMergeRequestsEnabled(false);
    gitlabProject.setIssuesEnabled(false);
    gitlabProject.setWallEnabled(false);
    gitlabProject.setWikiEnabled(false);
    gitlabProject.setPublic(false);
    gitlabProject.setSnippetsEnabled(false);

    return gitlabProject;
  }

  @Override
  public GitlabGroup buildGitlabGroup(final String pForgeProjectId, final String pInstanceName)
  {

    GitlabGroup gitlabGroup = new GitlabGroup();

    final StringBuilder groupName = new StringBuilder();
    groupName.append(pForgeProjectId).append("-").append(pInstanceName);

    gitlabGroup.setName(groupName.toString());
    gitlabGroup.setPath(groupName.toString());

    return gitlabGroup;
  }
}
