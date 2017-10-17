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
package org.novaforge.forge.plugins.scm.gitlab.internal.client;

import org.gitlab.api.GitlabAPI;
import org.junit.Test;
import org.novaforge.forge.plugins.scm.gitlab.client.GitlabRestConnector;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Gauthier Cart
 */
public class GitlabRestConnectorImplTest
{

  private static final String BASE_URL               = "http://vm-infra-29";
  private static final String TOKEN                  = "2UvYcywZJ1u3bxvjK7pX";

  private boolean             gitlabProfileActivated = false;

  public GitlabRestConnectorImplTest()
  {
    final String property = System.getProperty("gitlab.profile");
    if ("true".equals(property))
    {
      gitlabProfileActivated = true;
    }
  }

  /**
   * @throws URISyntaxException
   * @throws IOException
   */
  @Test
  public void testGitlabRestConnectorImpl() throws URISyntaxException, IOException
  {
    if (gitlabProfileActivated)
    {
      GitlabAPI restClient = GitlabAPI.connect(BASE_URL, TOKEN);
      final GitlabRestConnector gitlabRestConnectorImpl = new GitlabRestConnectorImpl(restClient, BASE_URL);

      assertThat(gitlabRestConnectorImpl.getGitlabRestClient(), notNullValue());
    }
  }

  /**
   * @throws URISyntaxException
   * @throws IOException
   */
  @Test
  public void testGetJiraRestClient() throws URISyntaxException, IOException
  {
    if (gitlabProfileActivated)
    {
      GitlabAPI restClient = GitlabAPI.connect(BASE_URL, TOKEN);
      final GitlabRestConnector gitlabRestConnectorImpl = new GitlabRestConnectorImpl(restClient, BASE_URL);
      assertThat(gitlabRestConnectorImpl.getGitlabRestClient(), notNullValue());
    }
  }

  /**
   * @throws URISyntaxException
   * @throws IOException
   */
  @Test
  public void testGetGitlabRestBaseURL() throws URISyntaxException, IOException
  {
    if (gitlabProfileActivated)
    {
      GitlabAPI restClient = GitlabAPI.connect(BASE_URL, TOKEN);
      final GitlabRestConnector gitlabRestConnectorImpl = new GitlabRestConnectorImpl(restClient, BASE_URL);
      assertThat(gitlabRestConnectorImpl.getGitlabBaseURL(), notNullValue());
    }
  }

}
