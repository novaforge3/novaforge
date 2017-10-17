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
package org.novaforge.forge.portal.internal.models;

import org.novaforge.forge.portal.models.PortalStringTokenized;

/**
 * Default implementation of {@link PortalStringTokenized}
 * 
 * @author Guillaume Lamirand
 */
public class PortalStringTokenizedImpl implements PortalStringTokenized
{

  /**
   * Contains the source string with tokens. Cannot be null or empty.
   */
  private final String source;
  /**
   * Contains the project id value. Can be null or empty.
   */
  private String       projectId;
  /**
   * Contains the locale name value. Can be null or empty.
   */
  private String       locale;
  /**
   * Contains the instance id value. Can be null or empty.
   */
  private String       instanceId;
  /**
   * Contains the plugin type value. Can be null or empty.
   */
  private String       pluginType;
  /**
   * Contains the view id value. Can be null or empty.
   */
  private String       viewId;
  /**
   * Contains the plugin uuid value. Can be null or empty.
   */
  private String       pluginUuid;
  /**
   * Contains the tool uuid value. Can be null or empty.
   */
  private String       toolUuid;
  /**
   * Contains the user name value. Can be null or empty.
   */
  private String       userName;

  /**
   * Default constructor. The source string is a mandatory element.
   * 
   * @param pSource
   *          source string
   */
  public PortalStringTokenizedImpl(final String pSource)
  {
    super();
    source = pSource;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSource()
  {
    return source;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProjectId()
  {
    return projectId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProjectId(final String pProjectId)
  {
    projectId = pProjectId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getInstanceId()
  {
    return instanceId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInstanceId(final String pInstanceId)
  {
    instanceId = pInstanceId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPluginViewId()
  {
    return viewId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPluginViewId(final String pViewId)
  {
    viewId = pViewId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPluginType()
  {
    return pluginType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPluginType(final String pType)
  {
    pluginType = pType;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPluginUuid()
  {
    return pluginUuid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPluginUuid(final String pPluginUuuid)
  {
    pluginUuid = pPluginUuuid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getToolUuid()
  {
    return toolUuid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setToolUuid(final String pToolUuid)
  {
    toolUuid = pToolUuid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUserName()
  {

    return userName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserName(final String pUserName)
  {
    userName = pUserName;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLocale()
  {
    return locale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLocale(final String locale)
  {
    this.locale = locale;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "PortalStringTokenizedImpl [source=" + source + ", projectId=" + projectId + ", locale=" + locale
               + ", instanceId=" + instanceId + ", pluginType=" + pluginType + ", viewId=" + viewId + ", pluginUuid="
               + pluginUuid + ", userName=" + userName + "]";
  }

}
