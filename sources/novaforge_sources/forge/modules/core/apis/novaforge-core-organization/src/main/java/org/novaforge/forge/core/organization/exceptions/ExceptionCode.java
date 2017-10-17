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
package org.novaforge.forge.core.organization.exceptions;

import org.novaforge.forge.commons.exceptions.Codifiable;

/**
 * @author benoists
 * @author BILET-JC
 */
public enum ExceptionCode implements Codifiable
{
  TECHNICAL_ERROR,

  ERR_VALIDATION_BEAN,

  /*
   * project
   */
  ERR_PROJECT_NOT_EXIST,

  ERR_CHANGE_PROJECT_STATUS_STATUS_ALREADY_CHANGED,

  ERR_CREATE_PROJECT_PROJECT_NAME_ALREADY_EXIST,

  ERR_CREATE_PROJECT_PROJECTID_ALREADY_EXIST,

  ERR_CREATE_PROJ_AUTHOR_NOT_EXIST,

  ERR_CREATE_PROJECT_WITH_TEMPLATE,

  /*
   * Template
   */
  ERR_TEMPLATE_NOT_EXIST,

  ERR_CHANGE_TEMPLATE_STATUS_STATUS_ALREADY_CHANGED,

  ERR_CREATE_TEMPLATE_NAME_ALREADY_EXIST,

  ERR_CREATE_TEMPLATE_ID_ALREADY_EXIST,

  /*
   * Application
   */
  ERR_CREATE_SPACE_NAME_ALREADY_EXIST, ERR_CREATE_APP_NAME_ALREADY_EXIST, ERR_CREATE_APP_COMP_EXIST,

  ERR_UPDATE_ROLES_MAPPING_INACTIVE_APPLICATION,

  /*
   * role
   */
  ERR_CREATE_ROLE_ROLENAME_ALREADY_EXIST,

  ERR_DELETE_ROLE_USER_WITH_ROLE_EXIST,

  ERR_CREATE_ROLE_ENTITY_UNDEFINED,

  ERR_UPDATE_OR_DELETE_SYSTEM_ROLE,

  ERR_SEARCH_ROLE_PROJECT_CRITERIA_NOT_DEFINED,

  /*
   * sending mail
   */
  ERR_CREATE_PROJECT_SENDING_MAIL_AUTHOR,

  ERR_VALIDATE_PROJECT_SENDING_MAIL_AUTHOR,

  ERR_CREATE_PROJECT_SENDING_MAIL_ADMINISTRATOR,

  ERR_RECOVER_PASSWORD_EMAIL_UNKNOWN,

  ERR_RECOVER_PASSWORD_MAIL_SERVICE_UNAVALAIBLE,

  ERR_PASSWORD_RECOVERY_SENDING_MAIL,

  ERR_RECOVER_PASSWORD_TOKEN_DISABLED,

  ERR_RECOVER_PASSWORD_TOKEN_EXPIRED,

  ERR_ADD_PROJECT_MEMBERSHIP_SENDING_MAIL,

  ERR_UPDATE_PROJECT_MEMBERSHIP_SENDING_MAIL,

  ERR_REMOVE_PROJECT_MEMBERSHIP_SENDING_MAIL,

  ERR_REJECT_PROJECT_SENDING_MAIL_AUTHOR,

  ERR_PROJECT_MEMBERSHIP_REQUEST_SENDING_MAIL_TO_ADMINS,

  ERR_PROJECT_MEMBERSHIP_REQUEST_SENDING_RESPONSE_MAIL_TO_USER,

  ERR_PROJECT_MEMBERSHIP_REQUEST_ANOTHER_DEMAND_IS_PENDING,

  ERR_NEW_EMAIL_ADMIN_NOTIFICATION,

  ERR_NEW_ACCOUNT_SENDING_MAIL,

  /*
   * Group
   */

  ERR_CREATE_GROUP_LOGIN_ALREADY_EXISTS,

  ERR_DELETE_GROUP_USER_WITH_MEMBERSHIP_EXIST,

  ERR_PUBLIC_GROUP_NOT_ALLOWED_FOR_USER_PROJECT,

  /*
   * User
   */
  ERR_CREATE_USER_LOGIN_ALREADY_EXISTS,

  ERR_CHANGE_USER_STATUS_STATUS_ALREADY_CHANGED,

  ERR_CREATE_USER_EMAIL_ALREADY_EXISTS,

  ERR_METHOD_NOT_ALLOWED_FOR_LDAP,

  ERR_SEARCH_LDAP,

  ERR_DIR_CONTEXT_FOR_LDAP,

  ERR_CREATE_USER_FORBIDDEN_LOGIN,

  /*
   * affectation user to a project
   */
  ERR_ADD_USER_MEMBERSHIP_MEMBERSHIP_ALREADY_EXIST,

  ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST,

  ERR_ADD_GROUP_MEMBERSHIP_GROUP_ISNT_PUBLIC,

  ERR_REMOVE_GROUP_MEMBERSHIP_GROUP_ISNT_PUBLIC,

  /*
   * Node
   */
  ERR_MANAGE_CHILD_FROM_APPLICATION_NODE,

  ERR_ADD_APPLICATION_PLUGIN_NOT_ACTIVATED,

  /*
   * Composition
   */
  ERR_COMPOSITION_GET_ALL_FOR_PROJECT,

  ERR_REQUEST_COMPOSITION_ALREADY_EXISTS,

  ERR_COMPOSITION_DO_NOT_ACCEPT_TEMPLATE,

  ERR_ASSOCIATION_TYPE_NOT_SUPPORTED,

  /*
   * Project Application Request
   */
  ERR_PROJECT_APPLICATION_REQUEST_ALREADY_EXISTS,

  ERR_MAX_ALLOWED_PROJECT_APPLICATIONS;

  @Override
  public String getName()
  {
    return name();
  }
}
