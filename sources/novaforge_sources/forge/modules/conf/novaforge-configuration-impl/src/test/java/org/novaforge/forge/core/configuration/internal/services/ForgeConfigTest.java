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
package org.novaforge.forge.core.configuration.internal.services;

import org.novaforge.forge.core.configuration.internal.services.properties.ForgeCfgServiceImpl;

/**
 * This class contains contants used to test {@link ForgeCfgServiceImpl}
 * 
 * @author Guillaume Lamirand
 */
public class ForgeConfigTest
{

  /**
   * Conf directory
   */
  public static final String  CONF_DIRECTORY                = "/tmp/datas";
  /**
   * Forge project id
   */
  public static final String  FORGE_PROJECT_ID              = "forge";
  /**
   * Forge project name
   */
  public static final String  FORGE_PROJECT_NAME            = "Forge";
  /**
   * Forge project description
   */
  public static final String  FORGE_PROJECT_DESCRIPTION     = "project description";
  /**
   * Forge project license
   */
  public static final String  FORGE_PROJECT_LICENSE         = "GPL";
  /**
   * Project id
   */
  public static final String  PROJECT_ID                    = "forge";
  /**
   * Is referentiel
   */
  public static final boolean IS_REFERENTIEL                = false;
  /**
   * Referentiel project id
   */
  public static final String  REFERENCE_PROJECT_ID          = "reference";
  /**
   * Referentiel project name
   */
  public static final String  REFERENCE_PROJECT_NAME        = "Reference";
  /**
   * Referentiel project description
   */
  public static final String  REFERENCE_PROJECT_DESCRIPTION = "project description";
  /**
   * Referentiel project license
   */
  public static final String  REFERENCE_PROJECT_LICENSE     = "GPL";
  /**
   * Referentiel project role
   */
  public static final String  REFERENCE_PROJECT_ROLE        = "member";
  /**
   * Forge project role
   */
  public static final String  FORGE_ROLE_MEMBER             = "member";
  /**
   * Forge project role
   */
  public static final String  FORGE_ROLE_SUPER_ADMIN        = "super-admin";
  /**
   * Forge project role
   */
  public static final String  FORGE_ROLE_ADMIN              = "admin";
  /**
   * Admin login
   */
  public static final String  ADMIN_LOGIN                   = "admin";
  /**
   * Admin pwd
   */
  public static final String  ADMIN_PWD                     = "pwd";
  /**
   * Admin email
   */
  public static final String  ADMIN_EMAIL                   = "email";
  /**
   * Admin first name
   */
  public static final String  ADMIN_FIRST_NAME              = "First name";
  /**
   * Admin last name
   */
  public static final String  ADMIN_LAST_NAME               = "Last name";
  /**
   * PWD life time
   */
  public static final int     PWD_LIFE_TIME                 = 10;
  /**
   * PWD modification time
   */
  public static final int     PWD_MOD_TIME                  = 40;
  /**
   * Forge url
   */
  public static final String  PUBLIC_URL                    = "http://localhost:9000";
  /**
   * CAS url
   */
  public static final String  CAS_URL                       = "https://localhost:8443/cas";
  /**
   * the portal footer information
   */
  public static final String  PORTAL_FOOTER                 = "NovaForge tm";
  /**
   * the bull web site url
   */
  public static final String  FOOTER_SITE                   = "http://localhost/";

}
