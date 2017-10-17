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
package org.novaforge.forge.plugins.ecm.alfresco.datamapper;

import org.json.JSONObject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocument;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocumentContent;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoFolder;

import java.util.Map;

/**
 * This interface defines resource builder service. It should use to build alfresco resource.
 * 
 * @author cadetr
 */
public interface AlfrescoResourceBuilder
{

   /**
    * Allow to create project from plugin project object.
    * 
    * @param pPluginProject
    *           represents project plugin object
    * @param pInstanceName
    *           represents the specific name of current instance
    * @return JSONObject built
    * @throws AlfrescoResourceBuilderException
    */
   JSONObject createProject(final PluginProject pPluginProject, final String pInstanceName)
   throws AlfrescoResourceBuilderException;

   /**
    * Allow to create user resource from plugin user object.
    * 
    * @param pPluginUser
    *           represents plugin user source
    * @return Map<String, String> built
    */
   Map<String, String> createUser(PluginUser pPluginUser);

   /**
    * Allow to create inviteUser resource from plugin user object.
    * 
    * @param pPluginUser
    *           represents plugin user source
    * @return JSONObject built
    * @throws AlfrescoResourceBuilderException
    */
   JSONObject inviteUser(final PluginUser pPluginUser, String projectId, String roleId)
         throws AlfrescoResourceBuilderException;

   AlfrescoDocument newDocument();

   AlfrescoDocumentContent newDocumentContent();

   AlfrescoFolder newFolder();
}