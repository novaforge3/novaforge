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
package org.novaforge.forge.ui.historization.client.properties;

import com.google.gwt.i18n.client.Messages;

/**
 * This interface regroups all fonctions about error messages
 * 
 * @author BILET-JC
 * @author lamirang
 */
public interface ErrorMessage extends Messages
{
  /*
   * FORGE ERRORS
   */
  /*
   * project
   */
  String ERR_VALIDATION_BEAN();

  String ERR_CHANGE_PROJECT_STATUS_STATUS_ALREADY_CHANGED();

  String ERR_CREATE_PROJECT_PROJECT_NAME_ALREADY_EXIST();

  String ERR_CREATE_PROJECT_PROJECTID_ALREADY_EXIST();

  String ERR_CREATE_PROJ_AUTHOR_NOT_EXIST();

  /*
   * Application
   */
  String ERR_CREATE_APP_NAME_ALREADY_EXIST();

  /*
   * Membership
   */
  String ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST();

  String ERR_ADD_GROUP_MEMBERSHIP_GROUP_ISNT_PUBLIC();

  String ERR_ADD_USER_MEMBERSHIP_MEMBERSHIP_ALREADY_EXIST();

  String ERR_REMOVE_GROUP_MEMBERSHIP_GROUP_ISNT_PUBLIC();

  /*
   * role
   */
  String ERR_CREATE_ROLE_ROLENAME_ALREADY_EXIST();

  String ERR_DELETE_ROLE_USER_WITH_ROLE_EXIST();

  /*
   * sending mail
   */
  String ERR_CREATE_PROJECT_SENDING_MAIL_AUTHOR();

  String ERR_VALIDATE_PROJECT_SENDING_MAIL_AUTHOR();

  String ERR_CREATE_PROJECT_SENDING_MAIL_ADMINISTRATOR();

  String ERR_ADD_PROJECT_MEMBERSHIP_SENDING_MAIL();

  String ERR_UPDATE_PROJECT_MEMBERSHIP_SENDING_MAIL();

  String ERR_REMOVE_PROJECT_MEMBERSHIP_SENDING_MAIL();

  /*
   * Group errors
   */
  String ERR_CREATE_GROUP_LOGIN_ALREADY_EXISTS();

  String ERR_DELETE_GROUP_USER_WITH_MEMBERSHIP_EXIST();

  String ERR_ACTOR_LOGIN();

  /*
   * Plugin
   */
  String ERR_PLUGIN_AVAILABILY();

  /*
   * IHM() ERRORS
   */
  String TECHNICAL_ERROR();

  String INTERVAL_DATES_CONTROL();
}
