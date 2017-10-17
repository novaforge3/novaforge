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
package org.novaforge.forge.tools.mailinglist.it.test;

/**
 * @author Blachon Marc
 */
public class TestConstants
{
  // parameter for Sympa DB
  public static final String DATABASE_SYMPA                                     = "jdbc:mysql://localhost:3306/sympa";
  public static final String USER                                               = "root";
  public static final String PASSWORD                                           = "root";

  // Project
  // ************ to be adjusted according on XML import file ****************************
  // public static final String PROJECTID = "projettestmailinglist12";
  public static final String PROJECT_MAILINGLIST_ITESTS_DESC                    = "project for mailing list itests";
  public static final String FORGE_PROJECTID                                    = "forge";

  public static final String FORGE_GROUPID                                      = "mlforgegroup1";
  public static final String PROJECT_GROUPID                                    = "mlprojectgroup1";

  // ----------------- super admin ----------------------------------------------
  public static final String ADMIN1LOGIN                                        = "admin1";
  public static final String ADMIN1EMAIL                                        = "novaforge-admin@bmt.bull.net";

  // ------------- project internal users----------------------------------------
  public static final String USERTEST1LOGIN                                     = "usertest1-u";
  public static final String USERTEST1EMAIL                                     = "usertest1@bull.net";

  // added to new project or forge group
  public static final String USERTEST2LOGIN                                     = "usertest2-u";
  public static final String USERTEST2EMAIL                                     = "usertest2@bull.net";

  // ------------------ users supposed to be external to the project ------------
  // added to new project group
  public static final String USERTEST14LOGIN                                    = "usertest14-u";
  public static final String USERTEST14EMAIL                                    = "usertest14@bull.net";

  // added as user subscriber into the new mailing list
  public static final String USERTEST15LOGIN                                    = "usertest15-u";
  public static final String USERTEST15EMAIL                                    = "usertest15@bull.net";

  // external email
  public static final String EXTERNAL_USER_EMAIL                                = "externaluser@toto.fr";

  // *************** should be compatible with xml import file
  public static final String LOGGED_USER_FOR_LIST_CREATION                      = "admin1";

  public static final String ROLE_FORGE_GROUP                                   = "developpeur";
  public static final String ROLE_ADDED_PROJECT_GROUP                           = "developpeur";

  public static final int    TOTAL_PROJECT_MEMBERS_NUMBER                       = 2;
  public static final int    TOTAL_SYMPA_NEW_MAILING_SUSCRIBERS_NUMBER          = 5;
  public static final int    TOTAL_MAILING_LIST_SUBSCRIBERS_AFTER_GROUP_REMOVAL = 3;
  // admin1, user2test-u, user15test-u, group subscriber
  public static final int    TOTAL_MAILING_LIST_SUBSCRIBERS                     = 4;

  // admin1, user2test-u, user15test-u
  public static final int    TOTAL_MAILING_LIST_USER_SUBSCRIBERS                = 3;

  // user14test-u, user1test-u, user2test-u, user15test-u
  public static final int    TOTAL_PROJECT_GROUP_MEMBERS                        = 4;

  public static final int    WAIT_PROPAGATION                                   = 5000;

  // -------------- constants for project mailing list ----------------------------
  public static final String MAILINGLIST_DESCRIPTION                            = "Project team mailing-list";
  public static final String MAILINGLIST_SUBJECT                                = "Default Project Team List";

  // ------------- constants for new mailing list -------------------------------------
  public static final String MAILING_LIST_NAME_PREFIX                           = "mailinglist_name";
  public static final String MAILINGLIST1DESCRIPTION                            = "mailinglist_description";
  public static final String MAILINGLIST1SUBJECT                                = "mailinglist_subject";

}
