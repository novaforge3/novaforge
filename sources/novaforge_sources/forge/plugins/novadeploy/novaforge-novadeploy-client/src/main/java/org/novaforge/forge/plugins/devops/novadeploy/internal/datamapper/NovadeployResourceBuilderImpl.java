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
package org.novaforge.forge.plugins.devops.novadeploy.internal.datamapper;

import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.devops.novadeploy.datamapper.NovadeployResourceBuilder;
import org.novaforge.forge.plugins.devops.novadeploy.internal.model.NovadeployUserImpl;
import org.novaforge.forge.plugins.devops.novadeploy.model.NovadeployUser;

/**
 * This class is used in order to build object used by Novadeploy rest server.
 * 
 * @author dekimpea
 */
public class NovadeployResourceBuilderImpl implements NovadeployResourceBuilder
{

  /**
   * Constant dot
   */
  private static final String DOT_SEPARATOR        = ":";

  /**
   * {@inheritDoc}
   */
  @Override
  public NovadeployUser buildNovadeployUser(final PluginUser pPluginUser)
  {
    final NovadeployUser account = new NovadeployUserImpl();
    account.setUserName(pPluginUser.getLogin());
    //    account.setLastName(pPluginUser.getName());
    //    account.setFisrtName(pPluginUser.getFirstName());
    //    account.setEmail(pPluginUser.getEmail());
    account.setPassword(pPluginUser.getPassword());
    account.addGroup(NovadeployUser.USER_GROUP);
    return account;
  }

  @Override
  public String buildNovadeployNameSpace(final String pPageIdPluginProject)
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


}
