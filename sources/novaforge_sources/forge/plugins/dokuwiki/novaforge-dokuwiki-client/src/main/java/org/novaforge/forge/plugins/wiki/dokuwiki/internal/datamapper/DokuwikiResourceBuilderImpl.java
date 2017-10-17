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
package org.novaforge.forge.plugins.wiki.dokuwiki.internal.datamapper;

import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.wiki.dokuwiki.datamapper.DokuwikiResourceBuilder;
import org.novaforge.forge.plugins.wiki.dokuwiki.internal.model.DokuWikiUserImpl;
import org.novaforge.forge.plugins.wiki.dokuwiki.internal.model.DokuwikiAttachmentImpl;
import org.novaforge.forge.plugins.wiki.dokuwiki.internal.model.DokuwikiPageImpl;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuWikiUser;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiAttachment;
import org.novaforge.forge.plugins.wiki.dokuwiki.model.DokuwikiPage;

/**
 * This class is used in order to build object used by dokuwiki xml-rpc server.
 * 
 * @author lamirang
 */
public class DokuwikiResourceBuilderImpl implements DokuwikiResourceBuilder
{
  /**
   * Constant underscore
   */
  private static final String UNDERSCORE_SEPARATOR = "_";
  /**
   * Constant space
   */
  private static final String SPACE_SEPARATOR      = " ";
  /**
   * Constant dot
   */
  private static final String DOT_SEPARATOR        = ":";
  /**
   * Constant for first page of namespace
   */
  private static final String START_PAGE           = ":start";
  /**
   * Constant for first page of namespace
   */
  private static final String DEFAULT_CONTENT      = "This is the main page of your wiki : ";

  /**
   * {@inheritDoc}
   */
  @Override
  public DokuwikiPage buildDokuwikiMainPage(final PluginProject pPluginProject, final String pInstanceName)
  {
    final DokuwikiPage projectData = new DokuwikiPageImpl();
    final StringBuilder projectName = new StringBuilder();
    projectName.append(pPluginProject.getName()).append(UNDERSCORE_SEPARATOR).append(pInstanceName);
    projectData.setName(buildStartingPage(projectName.toString()));
    projectData.setDescription(pPluginProject.getDescription());
    projectData.setContent(DEFAULT_CONTENT + projectName + SPACE_SEPARATOR + pInstanceName);

    return projectData;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DokuWikiUser buildDokuWikiUser(final PluginUser pPluginUser)
  {
    final DokuWikiUser account = new DokuWikiUserImpl();
    account.setUserName(pPluginUser.getLogin());
    account.setLastName(pPluginUser.getName());
    account.setFisrtName(pPluginUser.getFirstName());
    account.setEmail(pPluginUser.getEmail());
    account.setPassword(pPluginUser.getPassword());
    account.addGroup(DokuWikiUser.USER_GROUP);
    return account;
  }

  @Override
  public String buildDokuwikiNameSpace(final String pPageIdPluginProject)
  {
    String returnNameSpace = "";
    final int indexOf = pPageIdPluginProject.indexOf(DOT_SEPARATOR);
    if (indexOf > 0)
    {
      returnNameSpace = pPageIdPluginProject.substring(0, indexOf);
    }
    else
    {
      returnNameSpace = pPageIdPluginProject;
    }
    return returnNameSpace;

  }

  @Override
  public String buildStartingPage(final String pNameSpace)
  {
    final StringBuilder projectName = new StringBuilder();
    projectName.append(pNameSpace);
    if (!pNameSpace.endsWith(START_PAGE))
    {
      projectName.append(START_PAGE);
    }
    return projectName.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DokuwikiPage newPage()
  {
    return new DokuwikiPageImpl();
  }

  @Override
  public DokuwikiAttachment newAttachment()
  {
    return new DokuwikiAttachmentImpl();
  }
}
