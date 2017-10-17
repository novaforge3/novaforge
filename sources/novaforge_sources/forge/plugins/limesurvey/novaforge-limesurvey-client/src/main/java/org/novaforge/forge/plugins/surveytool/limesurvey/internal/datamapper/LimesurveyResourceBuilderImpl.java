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
package org.novaforge.forge.plugins.surveytool.limesurvey.internal.datamapper;

import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.surveytool.limesurvey.datamapper.LimesurveyResourceBuilder;
import org.novaforge.forge.plugins.surveytool.limesurvey.internal.model.LimesurveyGroupUserImpl;
import org.novaforge.forge.plugins.surveytool.limesurvey.internal.model.LimesurveyUserImpl;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyGroupUser;
import org.novaforge.forge.plugins.surveytool.limesurvey.model.LimesurveyUser;

/**
 * This class is used in order to build object used by limesurvey xml-rpc server.
 * 
 * @author lamirang
 */
public class LimesurveyResourceBuilderImpl implements LimesurveyResourceBuilder
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
   * Constant for first page of namespace
   */
  private static final String DEFAULT_CONTENT      = "This is the main page of your surveytool : ";

  /**
   * {@inheritDoc}
   */
  @Override
  public LimesurveyGroupUser buildLimesurveyGroupUser(final PluginProject pPluginProject,
      final String pInstanceName)
  {
    LimesurveyGroupUser projectData = new LimesurveyGroupUserImpl();
    projectData.setName(pPluginProject.getName() + UNDERSCORE_SEPARATOR + pInstanceName);
    projectData.setDescription(pPluginProject.getDescription());

    return projectData;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LimesurveyUser buildLimesurveyUser(final PluginUser pPluginUser)
  {
    final LimesurveyUser account = new LimesurveyUserImpl();
    account.setUserName(pPluginUser.getLogin());
    account.setFullName(pPluginUser.getFirstName() + " " + pPluginUser.getName());
    account.setEmail(pPluginUser.getEmail());
    account.setPassword(pPluginUser.getPassword());
    account.setLanguage(pPluginUser.getLanguage().toLowerCase());

    return account;
  }

}
