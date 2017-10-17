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
package org.novaforge.forge.portal.models;

/**
 * This interface describes an object which will be used to build an concret url from the given fields.
 * <p>
 * The content of this object SHOULD depend on your needs
 * </p>
 * 
 * @author Guillaume Lamirand
 * @see PortalToken
 */
public interface PortalStringTokenized
{

  /**
   * Get the original string. It can contain some token.
   * 
   * @return source string
   */
  String getSource();

  /**
   * Get the project id. It will be used to replace project token in the source string.
   * 
   * @return project id value. It can be <code>null</code> or empty
   * @see PortalToken#PROJECT_ID
   */
  String getProjectId();

  /**
   * Set the project id. It will be used to replace project token in the source string.
   * 
   * @param pProjectId
   *          the projectId to set
   * @see PortalToken#PROJECT_ID
   */
  void setProjectId(final String pProjectId);

  /**
   * Get the instance id. It will be used to replace instance token in the source string.
   * 
   * @return project id value. It can be <code>null</code> or empty
   * @see PortalToken#INSTANCE_ID
   */
  String getInstanceId();

  /**
   * Set the instance id. It will be used to replace instance token in the source string.
   * 
   * @param pInstanceId
   *          the instanceId to set
   * @see PortalToken#INSTANCE_ID
   */
  void setInstanceId(final String pInstanceId);

  /**
   * Get the plugin view id. It will be used to replace plugin view token in the source string.
   * 
   * @return view id value. It can be <code>null</code> or empty
   * @see PortalToken#PLUGIN_VIEW
   */
  String getPluginViewId();

  /**
   * Set the plugin view id. It will be used to replace plugin view token in the source string.
   * 
   * @param pViewId
   *          the viewId to set
   * @see PortalToken#PLUGIN_VIEW
   */
  void setPluginViewId(final String pViewId);

  /**
   * Get the plugin type id. It will be used to replace plugin type token in the source string.
   * 
   * @return plugin type value. It can be <code>null</code> or empty
   * @see PortalToken#PLUGIN_TYPE
   */
  String getPluginType();

  /**
   * Set the plugin type. It will be used to replace plugin view token in the source string.
   * 
   * @param pType
   *          the pluginType to set
   * @see PortalToken#PLUGIN_TYPE
   */
  void setPluginType(final String pType);

  /**
   * Get the plugin uuid. It will be used to replace plugin uuid token in the source string.
   * 
   * @return uuid value. It can be <code>null</code> or empty
   * @see PortalToken#PLUGIN_UUID
   */
  String getPluginUuid();

  /**
   * Set the plugin uuid. It will be used to replace plugin uuid token in the source string.
   * 
   * @param pPluginUuid
   *          the pluginUuid to set
   * @see PortalToken#PLUGIN_UUID
   */
  void setPluginUuid(final String pPluginUuid);

  /**
   * Get the tool uuid. It will be used to replace tool uuid token in the source string.
   * 
   * @return uuid value. It can be <code>null</code> or empty
   * @see PortalToken#TOOL_UUID
   */
  String getToolUuid();

  /**
   * Set the tool uuid. It will be used to replace plugin uuid token in the source string.
   * 
   * @param pToolUuid
   *          the toolUuid to set
   * @see PortalToken#TOOL_UUID
   */
  void setToolUuid(final String pToolUuid);

  /**
   * Get the user name. It will be used to replace user name token in the source string.
   * 
   * @return uuid value. It can be <code>null</code> or empty
   * @see PortalToken#USER_NAME
   */
  String getUserName();

  /**
   * Set the user name. It will be used to replace user name token in the source string.
   * 
   * @param pUserName
   *          the userName to set
   * @see PortalToken#USER_NAME
   */
  void setUserName(final String pUserName);

  /**
   * Get the locale name. It will be used to replace locale name token in the source string.
   * 
   * @return locale value. It can be <code>null</code> or empty
   * @see PortalToken#LOCALE
   */
  String getLocale();

  /**
   * Set the locale name. It will be used to replace locale name token in the source string.
   * 
   * @param locale
   *          the locale to set
   */
  void setLocale(final String locale);

}
