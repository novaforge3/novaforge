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
package org.novaforge.forge.plugins.cms.spip.internal.datamapper;

import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.cms.spip.datamapper.SpipResourceBuilder;
import org.novaforge.forge.plugins.cms.spip.soap.SiteInfo;
import org.novaforge.forge.plugins.cms.spip.soap.SiteInput;
import org.novaforge.forge.plugins.cms.spip.soap.UserData;
import org.novaforge.forge.plugins.cms.spip.soap.UserInfo;

/**
 * This class is used in order to build object used by spip WS.
 * 
 * @author Guillaume Lamirand, blachonm
 */
public class SpipResourceBuilderImpl implements SpipResourceBuilder
{
  /**
   * Constant bracket open
   */
  private static final String BRACKET_OPEN  = " (";
  /**
   * Constant bracket close
   */
  private static final String BRACKET_CLOSE = ")";

  /**
   * {@inheritDoc}
   */
  @Override
  public SiteInfo buildSiteInfo(final PluginProject pPluginProject, final String pInstanceName)
  {
    final SiteInfo siteInfo = new SiteInfo();
    siteInfo.setDescriptif_site(pPluginProject.getDescription());
    siteInfo.setNom_site(pPluginProject.getName());
    return siteInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SiteInput buildSiteInput(final PluginProject pPluginProject, final String pInstanceName)
  {
    final SiteInput siteInput = new SiteInput();
    // give the instanceName as site_id
    siteInput.setSite_id(pInstanceName);
    siteInput.setDescriptif_site(pPluginProject.getDescription());
    siteInput.setNom_site(pPluginProject.getName() + BRACKET_OPEN + pInstanceName + BRACKET_CLOSE);
    return siteInput;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserData buildUserData(final PluginUser pPluginUser)
  {
    final UserData userData = new UserData();
    userData.setLogin(pPluginUser.getLogin());
    userData.setNom(pPluginUser.getFirstName() + pPluginUser.getName());
    userData.setEmail(pPluginUser.getEmail());
    userData.setPass(pPluginUser.getPassword());
    return userData;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserInfo buildUserInfo(final PluginUser pPluginUser)
  {
    final UserInfo userInfo = new UserInfo();
    userInfo.setLogin(pPluginUser.getLogin());
    userInfo.setNom(pPluginUser.getName());
    userInfo.setEmail(pPluginUser.getEmail());
    return userInfo;
  }

}
