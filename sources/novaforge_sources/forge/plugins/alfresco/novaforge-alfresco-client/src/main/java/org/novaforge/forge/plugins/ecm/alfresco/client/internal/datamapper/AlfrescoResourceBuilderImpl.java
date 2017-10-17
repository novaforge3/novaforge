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
package org.novaforge.forge.plugins.ecm.alfresco.client.internal.datamapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.ecm.alfresco.client.internal.model.AlfrescoDocumentContentImpl;
import org.novaforge.forge.plugins.ecm.alfresco.client.internal.model.AlfrescoDocumentImpl;
import org.novaforge.forge.plugins.ecm.alfresco.client.internal.model.AlfrescoFolderImpl;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocument;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocumentContent;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoFolder;
import org.novaforge.forge.plugins.ecm.alfresco.datamapper.AlfrescoResourceBuilder;
import org.novaforge.forge.plugins.ecm.alfresco.datamapper.AlfrescoResourceBuilderException;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an implementation of AlfrescoResourceBuilder interface.
 * 
 * @see org.novaforge.forge.plugins.ecm.alfresco.datamapper.AlfrescoResourceBuilder
 * @author cadetr
 */
public class AlfrescoResourceBuilderImpl implements AlfrescoResourceBuilder
{

  /**
   * Constant underscore
   */
  private static final String UNDERSCORE_SEPARATOR = "_";

  /**
   * {@inheritDoc}
   */
  @Override
  public JSONObject createProject(final PluginProject pPluginProject, final String pInstanceName)
      throws AlfrescoResourceBuilderException
  {
    final JSONObject project = new JSONObject();

    try
    {
      project.put("shortName", pPluginProject.getProjectId() + UNDERSCORE_SEPARATOR + pInstanceName);
      project.put("sitePreset", "site-dashboard");
      project.put("title", pInstanceName);
      project.put("description", pPluginProject.getDescription());
      project.put("visibility", "PRIVATE");
    }
    catch (final JSONException e)
    {
      throw new AlfrescoResourceBuilderException("Unable to build JSON object for a project", e);
    }

    return project;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> createUser(final PluginUser pPluginUser)
  {
    final Map<String, String> user = new HashMap<String, String>();
    user.put("login", pPluginUser.getLogin());
    user.put("firstName", pPluginUser.getFirstName());
    user.put("lastName", pPluginUser.getName());
    user.put("password", pPluginUser.getPassword());
    user.put("mail", pPluginUser.getEmail());
    user.put("language", pPluginUser.getLanguage());
    return user;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JSONObject inviteUser(final PluginUser pPluginUser, final String projectId, final String roleId)
      throws AlfrescoResourceBuilderException
  {
    final JSONObject userMembership = new JSONObject();

    final JSONObject authorityObject = new JSONObject();
    try
    {
      authorityObject.put("userName", pPluginUser.getLogin());
      userMembership.put("role", roleId);
      userMembership.put("person", authorityObject);
      userMembership.put("shortName", projectId);
    }
    catch (final JSONException e)
    {
      throw new AlfrescoResourceBuilderException("Unable to build JSON object for an user", e);
    }

    return userMembership;

  }

  @Override
  public AlfrescoDocument newDocument()
  {
    return new AlfrescoDocumentImpl();
  }

  @Override
  public AlfrescoDocumentContent newDocumentContent()
  {
    return new AlfrescoDocumentContentImpl();
  }

  @Override
  public AlfrescoFolder newFolder()
  {
    return new AlfrescoFolderImpl();
  }
}
