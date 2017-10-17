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
package org.novaforge.forge.plugins.bugtracker.constants;

/**
 * @author Gauthier Cart
 */
public interface JiraConstant
{
  /**
   * This is a constant representing default actor type name in jira.
   */
  String ACTOR_TYPE_USER = "atlassian-user-role-actor";

  /**
   * This is a constant representing default actor type name in jira.
   */
  String ACTOR_TYPE_GROUP = "atlassian-group-role-actor";

  /**
   * This is a constant representing administrators group name in jira.
   */
  String ADMIN_GROUP = "jira-administrators";

  /**
   * This is a constant representing default users group name in jira.
   */
  String USERS_GROUP = "jira-users";

  /**
   * This is a constant representing default developer group name in jira.
   */
  String DEVELOPERS_GROUP = "jira-developers";

  /**
   * This is constants representing the number of character to trunked to build a valid jira Key. Total should
   * be equal to 10 or less.
   */
  int PROJECT_ID_KEY       = 5;
  int CONFIGURATION_ID_KEY = 5;

  /**
   * Component resources path
   */
  String REST_COMPONENT_PATH = "component";

  /**
   * Constant bracket open
   */
  String BRACKET_OPEN = " (";

  /**
   * Constant bracket close
   */
  String BRACKET_CLOSE = ")";

  /**
   * Constant space
   */
  String SPACE_SEPARATOR = " ";

  /**
   * Constant space
   */
  String SPACE = "  ";

  /**
   * Constant underscore
   */
  String UNDERSCORE = " _";

  /**
   * Id of the default security scheme id
   */
  Long SECURITY_SCHEME_ID = (long) 0;

  /**
   * Id of the default notification scheme id
   */
  Long NOTIFICATION_SCHEME_ID = (long) 10000;

  /**
   * Id of the default permission scheme id
   */
  Long PERMISSION_SCHEME_ID = (long) 0;

  /**
   * Id of the default type of issue BUG
   */
  Long BUG_TYPE_ID = (long) 1;

  /**
   * Id of the default permission scheme id
   */
  String CUSTOM_FIELD_LABELS_ID = "customfield_10006";

  /**
   * Id of the default permission scheme id
   */
  String FIELD_LABELS_NAME = "labels";

  /**
   * This is a constant representing default users group name in jira.
   */
  Integer REST_API_DEFAULT_MAX_RESULT = 1000;

}
