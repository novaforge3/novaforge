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
package org.novaforge.forge.plugins.forum.phpbb.internal.datamapper;

import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.forum.phpbb.datamapper.PhpBBResourceBuilder;
import org.novaforge.forge.plugins.forum.phpbb.internal.model.PhpbbForumImpl;
import org.novaforge.forge.plugins.forum.phpbb.internal.model.PhpbbUserImpl;
import org.novaforge.forge.plugins.forum.phpbb.model.PhpbbForum;
import org.novaforge.forge.plugins.forum.phpbb.model.PhpbbUser;

/**
 * This class is used in order to build object used by phpbb WS.
 * 
 * @author Guillaume Lamirand
 */
public class PhpBBResourceBuilderImpl implements PhpBBResourceBuilder
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
    * Constant space
    */
   private static final String SPACE_SEPARATOR = " ";

   /**
    * {@inheritDoc}
    */
   @Override
   public PhpbbForum buildProjectData(final PluginProject pPluginProject, final String pInstanceName)
   {
      PhpbbForum projectData = new PhpbbForumImpl();
      projectData.setCategorie(pPluginProject.getName());
      projectData.setName(pPluginProject.getName() + BRACKET_OPEN + pInstanceName + BRACKET_CLOSE);
      projectData.setDescription(pPluginProject.getDescription());

      return projectData;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public PhpbbUser buildAccountData(final PluginUser pPluginUser)
   {
      final PhpbbUser account = new PhpbbUserImpl();
      account.setUserName(pPluginUser.getLogin());
      final StringBuilder realName = new StringBuilder();
      realName.append(pPluginUser.getName()).append(SPACE_SEPARATOR)
      .append(pPluginUser.getFirstName());
      account.setFullName(realName.toString());
      account.setEmail(pPluginUser.getEmail());

      if (pPluginUser.getPassword() != null)
      {
         account.setPassword(pPluginUser.getPassword());
      }
      if (pPluginUser.getLanguage() != null)
      {
         account.setLanguage(pPluginUser.getLanguage().toLowerCase());
      }
      return account;
   }
}
