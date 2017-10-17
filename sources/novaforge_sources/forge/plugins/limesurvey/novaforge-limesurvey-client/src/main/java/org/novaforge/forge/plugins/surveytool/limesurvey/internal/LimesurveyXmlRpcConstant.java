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
package org.novaforge.forge.plugins.surveytool.limesurvey.internal;

import java.math.BigInteger;

/**
 * Declare constants used to communication with surveytool instance.
 * 
 * @author goarzino
 */
public final class LimesurveyXmlRpcConstant
{
  /**
   * Declare XML-RPC method to login to Limesurvey
   */
  public static final String LIMESURVEY_METHOD_LOGIN                  = "get_session_key";
  /**
   * Declare XML-RPC method to logout to Limesurvey
   */
  public static final String LIMESURVEY_METHOD_LOGOUT                 = "release_session_key";
  /**
   * Declare XML-RPC method to test if a user exists in Limesurvey instance
   */
  public static final String LIMESURVEY_METHOD_IS_USER_EXIST          = "is_user_exist";
  /**
   * Declare XML-RPC method to create a user
   */
  public static final String LIMESURVEY_METHOD_CREATE_USER            = "create_user";
  /**
   * Declare XML-RPC method to create a super administrator user
   */
  public static final String LIMESURVEY_METHOD_CREATE_SUPER_ADMIN     = "create_super_admin";
  /**
   * Declare XML-RPC method to update a user
   */
  public static final String LIMESURVEY_METHOD_UPDATE_USER            = "update_user";
  /**
   * Declare XML-RPC method to update user's permission
   */
  public static final String LIMESURVEY_METHOD_UPDATE_USER_PERMISSION = "update_user_permission";
  /**
   * Declare XML-RPC method to delete a user
   */
  public static final String LIMESURVEY_METHOD_DELETE_USER            = "delete_user";
  /**
   * Declare XML-RPC method to test if a user group exists
   */
  public static final String LIMESURVEY_METHOD_IS_USER_GROUP_EXIST    = "is_user_group_exist";
  /**
   * Declare XML-RPC method to test if a user group exists regarding its ID
   */
  public static final String LIMESURVEY_METHOD_IS_USER_GROUP_EXIST_BY_ID = "is_user_group_exist_by_id";
  /**
   * Declare XML-RPC method to create a user group
   */
  public static final String LIMESURVEY_METHOD_CREATE_USER_GROUP      = "create_user_group";
  /**
   * Declare XML-RPC method to update a user group
   */
  public static final String LIMESURVEY_METHOD_UPDATE_USER_GROUP      = "update_user_group";
  /**
   * Declare XML-RPC method to delete a user group
   */
  public static final String LIMESURVEY_METHOD_DELETE_USER_GROUP      = "delete_user_group";
  /**
   * Declare XML-RPC method to add a user in a group
   */
  public static final String LIMESURVEY_METHOD_ADD_USER_IN_GROUP      = "add_user_in_group";
  /**
   * Declare XML-RPC method to add a user in a group by its id
   */
  public static final String LIMESURVEY_METHOD_ADD_USER_IN_GROUP_BY_ID = "add_user_in_group_by_id";
  /**
   * Declare XML-RPC method to remove a user from a group
   */
  public static final String LIMESURVEY_METHOD_REMOVE_USER_FROM_GROUP  = "remove_user_from_group";
}
