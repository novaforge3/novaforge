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
package org.novaforge.forge.portal.internal.services;

import org.novaforge.forge.portal.services.PortalNavigation;

/**
 * This class contains contants used to test {@link PortalNavigation} based on test resource portal-config.xml
 * 
 * @author Guillaume Lamirand
 */
public class PortalConfigTest
{

  /**
   * Default user
   */
  public static final String USER                    = "admin1";
  /**
   * Default view url
   */
  public static final String VIEW_DEFAULT            = "http://localhost/default";
  /**
   * Admin view url
   */
  public static final String VIEW_ADMIN              = "http://localhost/admin";

  /**
   * Example of uri
   */
  public static final String SAMPLE_URI              = "/sample/uri";
  /**
   * Forge url
   */
  public static final String FORGE_URL               = "http://localhost:9000";
  /**
   * Application url
   */
  public static final String APP_URL                 = FORGE_URL + SAMPLE_URI;
  /**
   * Forge project id
   */
  public static final String FORGE                   = "forge";
  /**
   * Forge test space name
   */
  public final static String FORGE_TEST_SPACE_NAME   = "Forge Test Space";
  /**
   * Forge test space id
   */
  public final static String FORGE_TEST_SPACE_ID     = "forgeTestSpace";
  /**
   * Forge test space name2
   */
  public final static String FORGE_TEST_SPACE_NAME2  = "Forge Test Space2";
  /**
   * Forge test space id2
   */
  public final static String FORGE_TEST_SPACE_ID2    = "forgeTestSpace2";
  /**
   * Project test space name
   */
  public final static String PROJECT_TEST_SPACE_NAME = "Project Test Space";
  /**
   * Project test space id
   */
  public final static String PROJECT_TEST_SPACE_ID   = "projectTestSpace";
  /**
   * Space description
   */
  public final static String SPACE_DESCRIPTION       = "Space test description";
  /**
   * My Account space id
   */
  public final static String MY_ACCOUNT_SPACE_ID     = "myaccount";
  /**
   * My Account space id
   */
  public final static String MY_ACCOUNT_SPACE_NAME   = "@userName";
  /**
   * My Account id
   */
  public final static String MY_ACCOUNT_ID           = "updateuser";
  /**
   * My Account label
   */
  public final static String MY_ACCOUNT_NAME         = "My Account";
  /**
   * Logout id
   */
  public final static String LOGOUT_ID               = "logout";
  /**
   * Logout label
   */
  public final static String LOGOUT_NAME             = "Logout";
  /**
   * Public space id
   */
  public final static String PUBLIC_ID               = "public";
  /**
   * Public space name
   */
  public final static String PUBLIC_NAME             = "Public";
  /**
   * Novaforge app id
   */
  public final static String NOVAFORGE_ID            = "novaforge";
  /**
   * Novaforge app name
   */
  public final static String NOVAFORGE_NAME          = "NovaForge";
  /**
   * Novaforge app url
   */
  public final static String NOVAFORGE_URL           = "http://www.novaforge.org/";
  /**
   * Admin project app id
   */
  public final static String ADMIN_PROJECT_ID        = "adminproject";
  /**
   * Admin project app name
   */
  public final static String ADMIN_PROJECT_NAME      = "adminproject";
  /**
   * Validation project app id and name
   */
  public final static String VALIDATION_ID           = "projectvalidation";
  /**
   * Navigation directory
   */
  public static final String NAVIGATION_DIRECTORY    = "/test";
}
