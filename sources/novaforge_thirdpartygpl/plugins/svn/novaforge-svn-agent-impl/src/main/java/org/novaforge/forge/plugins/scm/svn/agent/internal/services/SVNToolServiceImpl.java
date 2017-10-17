/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.plugins.scm.svn.agent.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.plugins.scm.svn.services.SVNToolService;

/**
 * @author sbenoist
 */
public class SVNToolServiceImpl implements SVNToolService
{
	private static final String URL_SEPARATOR = "/";
	private static final String FILE_URL      = "file://";

	private static String       repositoriesPath;
	private static String       rootUrl;
	private static final Log    LOGGER        = LogFactory.getLog(SVNToolServiceImpl.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRepositoryPath(final String pRepositoryId, final String pRepositoryPath)
  {
    StringBuilder builder = new StringBuilder(FILE_URL).append(repositoriesPath).append(URL_SEPARATOR).append(pRepositoryId);
    if(pRepositoryPath != null) {
      builder.append(URL_SEPARATOR).append(pRepositoryPath);
    }
    return builder.toString();
  }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRepositoryUrl(final String pRepositoryId)
	{
		return new StringBuilder(rootUrl).append(URL_SEPARATOR).append(pRepositoryId).toString();
	}

	public void setRootUrl(final String pRootUrl)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(String.format("Update SVN root url configuration from '%s' to '%s'.", rootUrl, pRootUrl));
		}

		rootUrl = pRootUrl;
	}

	public void setRepositoriesPath(final String pRepositoriesPath)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(String.format("Update SVN repository path configuration from '%s' to '%s'.",
			    repositoriesPath, pRepositoriesPath));
		}
		repositoriesPath = pRepositoriesPath;
	}
}
