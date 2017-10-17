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
package org.novaforge.forge.it.test.datas;

import org.novaforge.forge.it.test.HelperTest;

/**
 * @author germainc, blachon-m
 */
public class TestConstants
{
  // url for tool databases
  public static final String DEFAULT_DATABASE_MANTIS             = "jdbc:mysql://localhost:3306/bugtracker";
  public static final String DEFAULT_DATABASE_LIMESURVEY         = "jdbc:mysql://localhost:3306/limesurvey";
  public static final String DEFAULT_DATABASE_PHPBB              = "jdbc:mysql://localhost:3306/phpbb";
  public static final String DEFAULT_DATABASE_WIKI               = "jdbc:mysql://localhost:3306/dokuwiki";
  // public static final String DEFAULT_DATABASE_SYMPA = "jdbc:mysql://localhost:3306/sympa";
  public static final String DEFAULT_DATABASE_TESTLINK           = "jdbc:mysql://localhost:3306/testlink";
  // partial url for spip because 1DB/project
  public static final String DEFAULT_DATABASE_SPIP               = "jdbc:mysql://localhost:3306/";
  public static final String DEFAULT_DATABASE_ALFRESCO           = "jdbc:mysql://localhost:3306/alfresco";
  public static final String DEFAULT_DATABASE_SONAR              = "jdbc:mysql://localhost:3306/sonar";
  public static final String DEFAULT_DATABASE_SVN                = "jdbc:mysql://localhost:3306/plugin_svn_agent";

  // MySql port
  public static final String MYSQL_PORT_NUMBER                   = "3306";
  public static final String NETWORK_PROTOCOL                    = "jdbc:mysql://";

  // Database instanceId
  public static final String DATABASE_MANTIS_ID                  = "bugtracker";
  public static final String DATABASE_LIMESURVEY_ID              = "limesurvey";
  public static final String DATABASE_PHPBB_ID                   = "phpbb";
  public static final String DATABASE_WIKI_ID                    = "dokuwiki";
  // public static final String DATABASE_SYMPA_ID = "sympa";
  public static final String DATABASE_TESTLINK_ID                = "testlink";
  public static final String DATABASE_ALFRESCO_ID                = "alfresco";
  public static final String DATABASE_SONAR_ID                   = "sonar";
  public static final String DATABASE_SVN_ID                     = "plugin_svn_agent";

  // url for plugin databases
  public static final String DATABASE_PLUGIN_MANTIS              = "jdbc:mysql://localhost:3306/plugin_mantis";
  public static final String DATABASE_PLUGIN_LIMESURVEY          = "jdbc:mysql://localhost:3306/plugin_limesurvey";
  public static final String DATABASE_PLUGIN_PHPBB               = "jdbc:mysql://localhost:3306/plugin_phpbb";
  public static final String DATABASE_PLUGIN_WIKI                = "jdbc:mysql://localhost:3306/plugin_dokuwiki";
  public static final String DATABASE_PLUGIN_SYMPA               = "jdbc:mysql://localhost:3306/plugin_sympa";
  public static final String DATABASE_PLUGIN_TESTLINK            = "jdbc:mysql://localhost:3306/plugin_testlink";
  public static final String DATABASE_PLUGIN_SPIP                = "jdbc:mysql://localhost:3306/plugin_spip";
  public static final String DATABASE_PLUGIN_ALFRESCO            = "jdbc:mysql://localhost:3306/plugin_alfresco";
  public static final String DATABASE_PLUGIN_SONAR               = "jdbc:mysql://localhost:3306/plugin_sonar";
  public static final String DATABASE_PLUGIN_SVN                 = "jdbc:mysql://localhost:3306/plugin_svn";
  public static final String DATABASE_PLUGIN_NEXUS               = "jdbc:mysql://localhost:3306/plugin_nexus";
  public static final String DATABASE_PLUGIN_JENKINS             = "jdbc:mysql://localhost:3306/plugin_jenkins";

  public static final String login                               = "admin1";
  public static final String pwd                                 = "c1dc19afe59631de1e5f5408d083cc55e9dcdf65";
  public static final String USER                                = "root";
  public static final String PASSWORD                            = "root";
  public final static String ALFRESCO_ADMIN                      = "admin_forge";
  public final static String ALFRESCO_PWD                        = "forge";

  public static final String ROOT_LOGIN                          = "root";
  public static final String ROOT_PWD                            = "novadmin";
  public static final int    SSH_PORT                            = 22;

  // alfresco end point
  public final static String ALFRESCO_ENDPOINT_PROTOCOL          = "http://";
  public final static String ALFRESCO_ENDPOINT_PORT_NUMBER       = "8082";
  public final static String ALFRESCO_ENDPOINT_URI               = "/alfresco-default/alfresco/wcs";

  // get datas/<safran or novaforge3>/datas/karaf/tmp
  public final static String DIRECTORY_XML                       = System.getProperty("java.io.tmpdir");

  public final static String SPECIFIC_NOVA_HOME                  = HelperTest
                                                                     .getSpecificNovaHome(DIRECTORY_XML);

  public final static String IMPORT_ITESTS_VALIDATION_XML        = "import_itests_validation.xml";
  public final static String IMPORT_ITESTS_VALIDATION2_XML       = "import_itests_validation2.xml";

  public final static String DOKUWIKI_SECURITY_FILE              = "/datas/" + SPECIFIC_NOVA_HOME
                                                                     + "/engines/dokuwiki/conf/acl.auth.php";
  public final static String NEXUS_SECURITY_FILE                 = "/datas/" + SPECIFIC_NOVA_HOME
                                                                     + "/datas/nexus/conf/security.xml";
  // jenkins security file
  public final static String JENKINS_JOBS_PATH                   = "datas/" + SPECIFIC_NOVA_HOME
                                                                     + "/datas/jenkins/jobs/";
  public final static String JENKINS_DEFAULT_JOB_NAME            = "integrationcontappli_default";
  // public final static String JENKINS_DEFAULT_CONFIG = "integrationcontappli_default/config.xml";
  public final static String JENKINS_CONFIG_FILE_NAME            = "config.xml";

  // 2nd appli type is used to search/compare into xml data from IMPORT_ITESTS_VALIDATION2_XML
  public static final String GED_CATEGORY                        = "ecm";
  public static final String GED_TYPE                            = "Alfresco";
  public static final String GED_TYPE2                           = "Alfresco2";

  public static final String REPOSITORY_CATEGORY                 = "repositorymanagement";
  public static final String REPOSITORY_TYPE                     = "Nexus";
  public static final String REPOSITORY_TYPE2                    = "Nexus2";

  public static final String SURVEY_CATEGORY                     = "survey";
  public static final String SURVEY_TYPE                         = "Limesurvey";
  public static final String SURVEY_TYPE2                        = "Limesurvey2";

  public static final String BUGTRACKER_CATEGORY                 = "Bugtracker";
  public static final String BUGTRACKER_TYPE                     = "Mantis";
  public static final String BUGTRACKER_TYPE2                    = "Mantis2";

  public static final String BUILDING_CATEGORY                   = "ci";
  public static final String BUILDING_TYPE                       = "Jenkins";
  public static final String BUILDING_TYPE2                      = "Jenkins2";

  public static final String FORUM_CATEGORY                      = "forum";
  public static final String FORUM_TYPE                          = "PhpBB";
  public static final String FORUM_TYPE2                         = "PhpBB2";

  public static final String QUALIMETRY_CATEGORY                 = "quality";
  public static final String QUALIMETRY_TYPE                     = "Sonar";
  public static final String QUALIMETRY_TYPE2                    = "Sonar2";

  public static final String ECM_CATEGORY                        = "cms";
  public static final String ECM_TYPE                            = "Spip";
  public static final String ECM_TYPE2                           = "Spip2";

  public static final String MAILING_LIST_CATEGORY               = "mailinglist";
  public static final String MAILING_LIST_TYPE                   = "Sympa";
  public static final String MAILING_LIST_TYPE2                  = "Sympa2";
  public static final String TOPICS_CONF                         = "/datas/" + SPECIFIC_NOVA_HOME
                                                                     + "/engines/sympa/etc/topics.conf";

  public static final String TEST_CATEGORY                       = "testmanagement";
  public static final String TEST_TYPE                           = "Testlink";
  public static final String TEST_TYPE2                          = "Testlink2";

  public static final String WIKI_CATEGORY                       = "wiki";
  public static final String WIKI_TYPE                           = "Dokuwiki";
  public static final String WIKI_TYPE2                          = "Dokuwiki2";

  public static final String SUBVERSION_CATEGORY                 = "scm";
  public static final String SUBVERSION_TYPE                     = "SVN";
  public static final String SUBVERSION_TYPE2                    = "SVN2";

  public static final int    WAIT_CHANGE_ROLE_PROPAGATION        = 20000;
  public static final int    WAIT_CHANGE_ROLEMAPPING_PROPAGATION = 25000;

  public static final String RESULT_FILENANE                     = "/tmp/tools-propagation-itests.result";

  // added constants required for suite test: SpecificGroupPropagationTest
  public static final String USERTEST12LOGIN                     = "usertest12-u";                                 // must
                                                                                                                    // be
                                                                                                                    // declared
                                                                                                                    // into
                                                                                                                    // the
                                                                                                                    // xml
                                                                                                                    // !
  public static final String USERTEST13LOGIN                     = "usertest13-u";                                 // must
                                                                                                                    // be
                                                                                                                    // declared
                                                                                                                    // into
                                                                                                                    // the
                                                                                                                    // xml
                                                                                                                    // !
  public static final String FORGE_PROJECTID                     = "forge";
  public static final String FORGE_GROUP                         = "forgegroup1";                                  // must
                                                                                                                    // be
                                                                                                                    // declared
                                                                                                                    // into
                                                                                                                    // the
                                                                                                                    // xml
                                                                                                                    // !
  public static final String ADDED_PROJECT_GROUP                 = "grouptest3";
  public static final String ROLE_FORGE_GROUP                    = "developpeur";
  public static final String ROLE_ADDED_PROJECT_GROUP            = "developpeur";

}