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
package org.novaforge.forge.plugins.bugtracker.mantis.ard.datamapper;

//import org.novaforge.forge.core.plugins.categories.bugtracker.BugBean;
//import org.novaforge.forge.core.plugins.categories.management.CustomFieldBean;

import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.AccountData;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.IssueData;
import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.ProjectData;

//import org.novaforge.forge.plugins.bugtracker.mantis.ard.soap.CustomFieldDefinitionData;

/**
 * This class is used to defined a service which will be user in order to build object needed to communicate
 * with mantis WS.
 * 
 * @author lamirang
 * @author BILET-JC
 */
public interface MantisARDResourceBuilder
{

   ProjectData buildProjectData(final PluginProject pPluginProject, final String pInstanceName);

   AccountData buildAccountData(final PluginUser pPluginUser);

   IssueData buildIssueData(final BugTrackerIssueBean pBugBean, final String pId, final String pUserName);
   

}