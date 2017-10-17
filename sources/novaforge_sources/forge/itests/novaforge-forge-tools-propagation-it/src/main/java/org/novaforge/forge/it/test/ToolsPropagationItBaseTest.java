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
package org.novaforge.forge.it.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.novaforge.forge.commons.technical.normalize.NormalizeService;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.GroupPresenter;
import org.novaforge.forge.core.organization.presenters.MembershipPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.it.test.datas.TestConstants;
import org.novaforge.forge.it.test.datas.XmlData;
import org.novaforge.forge.plugins.bucktracker.mantis.services.MantisConfigurationService;
import org.novaforge.forge.plugins.cms.spip.services.SpipConfigurationService;
import org.novaforge.forge.plugins.continuousintegration.jenkins.services.JenkinsConfigurationService;
import org.novaforge.forge.plugins.ecm.alfresco.services.AlfrescoConfigurationService;
import org.novaforge.forge.plugins.forum.phpbb.services.PhpBBConfigurationService;
import org.novaforge.forge.plugins.gcl.nexus.services.NexusConfigurationService;
import org.novaforge.forge.plugins.quality.sonar.services.SonarConfigurationService;
import org.novaforge.forge.plugins.scm.svn.services.SVNConfigurationService;
import org.novaforge.forge.plugins.surveytool.limesurvey.services.LimesurveyConfigurationService;
import org.novaforge.forge.plugins.testmanager.testlink.services.TestlinkConfigurationService;
import org.novaforge.forge.plugins.wiki.dokuwiki.services.DokuwikiConfigurationService;
import org.osgi.framework.ServiceReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author blachonm
 */
public class ToolsPropagationItBaseTest extends OSGiTestCase
{
  private static final Log                          log = LogFactory.getLog(ToolsPropagationItBaseTest.class);
  protected ForgeConfigurationService               forgeConfigurationService;
  protected ProjectPresenter                        projectPresenter;
  protected UserPresenter                           userPresenter;
  protected GroupPresenter                          groupPresenter;
  protected PluginsManager                          pluginsManager;
  protected AuthentificationService                 securityManager;
  protected ApplicationPresenter                    applicationPresenter;
  protected MembershipPresenter                     membershipPresenter;
  protected NormalizeService                        normalizeService;
  protected String                                  uuid;
  protected XmlData                                 xmlData;

  protected SpipConfigurationService                spipConfigurationService;

  protected PhpBBConfigurationService               phpBBConfigurationService;

  protected JenkinsConfigurationService             jenkinsConfigurationService;

  protected SonarConfigurationService               sonarConfigurationService;

  protected LimesurveyConfigurationService          limesurveyConfigurationService;

  protected MantisConfigurationService              mantisConfigurationService;

  protected NexusConfigurationService               nexusConfigurationService;

  protected SVNConfigurationService                 svnConfigurationService;

  protected TestlinkConfigurationService            testlinkConfigurationService;

  protected DokuwikiConfigurationService            dokuwikiConfigurationService;

  protected Map<String, PluginConfigurationService> pluginConfigurationServiceMap;

  protected Map<String, String>                     databaseNameMap;

  protected AlfrescoConfigurationService            alfrescoConfigurationService;

  @Override
  public void setUp() throws Exception
  {
    // Initialize managers
    final ServiceReference projectPresenterReference = getServiceReference(ProjectPresenter.class.getName());
    assertNotNull(projectPresenterReference);
    projectPresenter = (ProjectPresenter) getServiceObject(projectPresenterReference);
    assertNotNull(projectPresenter);

    final ServiceReference applicationPresenterReference = getServiceReference(ApplicationPresenter.class
        .getName());
    assertNotNull(applicationPresenterReference);
    applicationPresenter = (ApplicationPresenter) getServiceObject(applicationPresenterReference);
    assertNotNull(applicationPresenter);

    final ServiceReference pluginManagerReference = getServiceReference(PluginsManager.class.getName());
    assertNotNull(pluginManagerReference);
    pluginsManager = (PluginsManager) getServiceObject(pluginManagerReference);
    assertNotNull(pluginsManager);

    final ServiceReference userPresenterReference = getServiceReference(UserPresenter.class.getName());
    assertNotNull(userPresenterReference);
    userPresenter = (UserPresenter) getServiceObject(userPresenterReference);
    assertNotNull(userPresenter);

    final ServiceReference groupPresenterReference = getServiceReference(GroupPresenter.class.getName());
    assertNotNull(groupPresenterReference);
    groupPresenter = (GroupPresenter) getServiceObject(groupPresenterReference);
    assertNotNull(groupPresenter);

    final ServiceReference membershipPresenterReference = getServiceReference(MembershipPresenter.class
        .getName());
    assertNotNull(membershipPresenterReference);
    membershipPresenter = (MembershipPresenter) getServiceObject(membershipPresenterReference);
    assertNotNull(membershipPresenter);

    // Initialize securityManager
    final ServiceReference authentificationServiceRef = getServiceReference(AuthentificationService.class
        .getName());
    assertNotNull(authentificationServiceRef);
    securityManager = (AuthentificationService) getServiceObject(authentificationServiceRef);
    assertNotNull(securityManager);

    final ServiceReference forgeConfigurationServiceRef = getServiceReference(ForgeConfigurationService.class
        .getName());
    assertNotNull(forgeConfigurationServiceRef);
    forgeConfigurationService = (ForgeConfigurationService) getServiceObject(forgeConfigurationServiceRef);
    assertNotNull(forgeConfigurationService);

    final ServiceReference generateIdentifier = getServiceReference(NormalizeService.class.getName());
    assertNotNull(generateIdentifier);
    normalizeService = (NormalizeService) getServiceObject(generateIdentifier);
    assertNotNull(normalizeService);

    // --------------- service tool configuration -------------------------------
    final ServiceReference alfrescoConfigurationServiceRef = getServiceReference(AlfrescoConfigurationService.class
        .getName());
    assertNotNull(alfrescoConfigurationServiceRef);
    alfrescoConfigurationService = (AlfrescoConfigurationService) getServiceObject(alfrescoConfigurationServiceRef);
    assertNotNull(alfrescoConfigurationService);

    final ServiceReference spipConfigurationServiceRef = getServiceReference(SpipConfigurationService.class
        .getName());
    assertNotNull(spipConfigurationServiceRef);
    spipConfigurationService = (SpipConfigurationService) getServiceObject(spipConfigurationServiceRef);
    assertNotNull(spipConfigurationService);

    final ServiceReference phpBBConfigurationServiceRef = getServiceReference(PhpBBConfigurationService.class
        .getName());
    assertNotNull(phpBBConfigurationServiceRef);
    phpBBConfigurationService = (PhpBBConfigurationService) getServiceObject(phpBBConfigurationServiceRef);
    assertNotNull(phpBBConfigurationService);

    final ServiceReference jenkinsConfigurationServiceRef = getServiceReference(JenkinsConfigurationService.class
        .getName());
    assertNotNull(jenkinsConfigurationServiceRef);
    jenkinsConfigurationService = (JenkinsConfigurationService) getServiceObject(jenkinsConfigurationServiceRef);
    assertNotNull(jenkinsConfigurationService);

    final ServiceReference sonarConfigurationServiceRef = getServiceReference(SonarConfigurationService.class
        .getName());
    assertNotNull(sonarConfigurationServiceRef);
    sonarConfigurationService = (SonarConfigurationService) getServiceObject(sonarConfigurationServiceRef);
    assertNotNull(sonarConfigurationService);

    final ServiceReference limesurveyConfigurationServiceRef = getServiceReference(LimesurveyConfigurationService.class
        .getName());
    assertNotNull(limesurveyConfigurationServiceRef);
    limesurveyConfigurationService = (LimesurveyConfigurationService) getServiceObject(limesurveyConfigurationServiceRef);
    assertNotNull(limesurveyConfigurationService);

    final ServiceReference mantisConfigurationServiceRef = getServiceReference(MantisConfigurationService.class
        .getName());
    assertNotNull(mantisConfigurationServiceRef);
    mantisConfigurationService = (MantisConfigurationService) getServiceObject(mantisConfigurationServiceRef);
    assertNotNull(mantisConfigurationService);

    final ServiceReference nexusConfigurationServiceRef = getServiceReference(NexusConfigurationService.class
        .getName());
    assertNotNull(nexusConfigurationServiceRef);
    nexusConfigurationService = (NexusConfigurationService) getServiceObject(nexusConfigurationServiceRef);
    assertNotNull(nexusConfigurationService);

    final ServiceReference svnConfigurationServiceRef = getServiceReference(SVNConfigurationService.class
        .getName());
    assertNotNull(svnConfigurationServiceRef);
    svnConfigurationService = (SVNConfigurationService) getServiceObject(svnConfigurationServiceRef);
    assertNotNull(svnConfigurationService);

    final ServiceReference testlinkConfigurationServiceRef = getServiceReference(TestlinkConfigurationService.class
        .getName());
    assertNotNull(testlinkConfigurationServiceRef);
    testlinkConfigurationService = (TestlinkConfigurationService) getServiceObject(testlinkConfigurationServiceRef);
    assertNotNull(testlinkConfigurationService);

    final ServiceReference dokuwikiConfigurationServiceRef = getServiceReference(DokuwikiConfigurationService.class
        .getName());
    assertNotNull(dokuwikiConfigurationServiceRef);
    dokuwikiConfigurationService = (DokuwikiConfigurationService) getServiceObject(dokuwikiConfigurationServiceRef);
    assertNotNull(dokuwikiConfigurationService);

    // initiate the configuration service map
    pluginConfigurationServiceMap = new HashMap<String, PluginConfigurationService>();
    pluginConfigurationServiceMap.put(TestConstants.GED_TYPE, alfrescoConfigurationService);
    pluginConfigurationServiceMap.put(TestConstants.ECM_TYPE, spipConfigurationService);
    pluginConfigurationServiceMap.put(TestConstants.FORUM_TYPE, phpBBConfigurationService);
    pluginConfigurationServiceMap.put(TestConstants.BUILDING_TYPE, jenkinsConfigurationService);
    pluginConfigurationServiceMap.put(TestConstants.QUALIMETRY_TYPE, sonarConfigurationService);
    pluginConfigurationServiceMap.put(TestConstants.SURVEY_TYPE, limesurveyConfigurationService);
    pluginConfigurationServiceMap.put(TestConstants.BUGTRACKER_TYPE, mantisConfigurationService);
    pluginConfigurationServiceMap.put(TestConstants.REPOSITORY_TYPE, nexusConfigurationService);
    pluginConfigurationServiceMap.put(TestConstants.SUBVERSION_TYPE, svnConfigurationService);
    pluginConfigurationServiceMap.put(TestConstants.TEST_TYPE, testlinkConfigurationService);
    pluginConfigurationServiceMap.put(TestConstants.WIKI_TYPE, dokuwikiConfigurationService);

    databaseNameMap = new HashMap<String, String>();
    databaseNameMap.put(TestConstants.GED_TYPE, TestConstants.DATABASE_ALFRESCO_ID);
    databaseNameMap.put(TestConstants.FORUM_TYPE, TestConstants.DATABASE_PHPBB_ID);
    databaseNameMap.put(TestConstants.QUALIMETRY_TYPE, TestConstants.DATABASE_SONAR_ID);
    databaseNameMap.put(TestConstants.SUBVERSION_TYPE, TestConstants.DATABASE_SVN_ID);
    databaseNameMap.put(TestConstants.BUGTRACKER_TYPE, TestConstants.DATABASE_MANTIS_ID);
    databaseNameMap.put(TestConstants.SUBVERSION_TYPE, TestConstants.DATABASE_SVN_ID);
    databaseNameMap.put(TestConstants.TEST_TYPE, TestConstants.DATABASE_TESTLINK_ID);
    databaseNameMap.put(TestConstants.WIKI_TYPE, TestConstants.DATABASE_WIKI_ID);
    databaseNameMap.put(TestConstants.SURVEY_TYPE, TestConstants.DATABASE_LIMESURVEY_ID);

    xmlData = new XmlData();

    // ServiceReference generateIdentifier = getServiceReference(GenerateIdentifier.class.getName());
    // assertNotNull(generateIdentifier);
    // this.generateIdentifier = (GenerateIdentifier) getServiceObject(generateIdentifier);
    // assertNotNull(this.generateIdentifier);

    // TODO check pluginsMetadata
    // ECM
    // List<PluginMetadata> pluginsMetadatas =
    // this.pluginsManager.getPluginsMetadataByCategory(TestCsvConstants.ECM_CATEGORY);
    // assertNotNull(pluginsMetadatas);
    // uuid = pluginsMetadatas.get(0).getUUID();
    // assertNotNull(uuid);
    // PluginStatus pluginStatus=pluginsMetadatas.get(0).getStatus();
    // TODO check uuid and statut for all categories:
    /*
     * GED_CATEGORY REPOSITORY_CATEGORY SURVEY_CATEGORY BUGTRACKER_CATEGORY BUILDING_CATEGORY FORUM_CATEGORY
     * QUALIMETRY_CATEGORY ECM_CATEGORY MAILING_LIST_CATEGORY TEST_CATEGORY WIKI_CATEGORY
     * SUBVERSION_CATEGORY
     */

  }

  @Override
  public void tearDown() throws Exception
  {
    // TODO if necessary

  }

  protected String getHostNameFromToolUrl(String categoryType)
  {
    String hostName = null;
    hostName = pluginConfigurationServiceMap.get(categoryType).getDefaultToolURL().getHost();
    return hostName;
  }

  protected String getMySqlJdbcUrl(String categoryType)
  {
    String url = null;
    switch (categoryType)
    {
      case TestConstants.ECM_TYPE:
        url = TestConstants.NETWORK_PROTOCOL + getHostNameFromToolUrl(categoryType) + ":"
            + TestConstants.MYSQL_PORT_NUMBER + "/";
        break;

      default:
        url = TestConstants.NETWORK_PROTOCOL + getHostNameFromToolUrl(categoryType) + ":"
            + TestConstants.MYSQL_PORT_NUMBER + "/" + databaseNameMap.get(categoryType);
        break;
    }
    return url;
  }

  /*
   * updating role mapping for an application
   */
  protected void updateApplication(final String projectId, final String applicationUri,
      final Map<String, String> roleMappings, final String appType, final String appName)
      throws PluginManagerException, ApplicationServiceException
  {
    try
    {
      securityManager.login(TestConstants.login, TestConstants.pwd);
      applicationPresenter.updateRoleMapping(projectId, applicationUri, roleMappings);
      log.info(String.format("Role mapping for application type: %s, with uri: %s updated.", appType,
          applicationUri));
    }
    finally
    {
      if (securityManager.checkLogin())
      {
        securityManager.logout();
      }
    }
  }

  protected void changeRoleForUser(final String projectTestId, final String userIdToChange,
      final String roleToChange, final String roleTarget) throws Exception
  {
    final Set<String> newRoles = new HashSet<String>();
    // String userFound = null;
    boolean thrown = false;
    if ((projectTestId != null) && !projectTestId.isEmpty() && (roleToChange != null)
        && !roleToChange.isEmpty() && (roleTarget != null) && !roleTarget.isEmpty()
        && (userIdToChange != null))
    {
      try
      {
        securityManager.login(TestConstants.login, TestConstants.pwd);
        final UUID userUUID = userPresenter.getUser(userIdToChange).getUuid();
        newRoles.add(roleTarget);
        try
        {
          membershipPresenter.updateUserMembership(projectTestId, userUUID, newRoles, null, false);
        }
        catch (final ProjectServiceException e)
        {
          thrown = true;
          throw new Exception("Error when updating user membership", e);
        }
        assertFalse("=============> No Failure when updating the membership for user = " + userIdToChange
            + " from role = " + roleToChange + " to: " + roleTarget + " for projectId = " + projectTestId,
            thrown);

        // checking user membership has been changed
        final List<Membership> memberShipList = membershipPresenter
            .getAllEffectiveUserMembershipsForUserAndProject(projectTestId, userIdToChange);
        for (final Membership mb : memberShipList)
        {
          // select membership with right project and user
          if ((mb.getProject().getName()).equals(projectTestId))
          {
            assertTrue("============> The role for the user " + userIdToChange + " is not the expected = "
                + roleTarget, roleTarget.equals(mb.getRole().getName()));
            break;
          }

        }

      }

      finally
      {
        if (securityManager.checkLogin())
        {
          securityManager.logout();
        }
      }
    }

  }

  protected String changeRoleForGroup(final String projectTestId, final String groupId,
      final String roleTarget) throws Exception
  {
    final Set<String> newRoles = new HashSet<String>();
    final String userFound = null;
    boolean thrown = false;
    if ((projectTestId != null) && !projectTestId.isEmpty() && (groupId != null) && !groupId.isEmpty()
        && (roleTarget != null) && !roleTarget.isEmpty())
    {
      try
      {
        securityManager.login(TestConstants.login, TestConstants.pwd);
        newRoles.add(roleTarget);
        try
        {
          final UUID groupUUID = groupPresenter.getGroup(projectTestId, groupId).getUuid();
          membershipPresenter.updateGroupMembership(projectTestId, groupUUID, newRoles, null, false);
        }
        catch (final ProjectServiceException e)
        {
          thrown = true;
          throw new Exception("Error when updating user membership", e);
        }
        assertFalse("=============> Failure when updating the membership for group = " + groupId + " to: "
            + roleTarget + " for projectId = " + projectTestId, thrown);

        // checking user membership has been changed
        final List<MembershipInfo> memberShipList = membershipPresenter.getAllGroupMemberships(projectTestId);
        for (final MembershipInfo mb : memberShipList)
        {
          assertTrue("============> The role for the group " + groupId + " is not the expected one = "
              + roleTarget, roleTarget.equals(mb.getRole().getName()));
          break;
        }

      }

      finally
      {
        if (securityManager.checkLogin())
        {
          securityManager.logout();
        }
      }
    }
    return userFound;
  }

  protected void changeBackRoleForUser(final String projectTestId, final String pUserLogin,
      final String roleToChange) throws Exception
  {
    final Set<String> newRoles = new HashSet<String>();

    try
    {
      boolean roleToChangeFound = false;
      securityManager.login(TestConstants.login, TestConstants.pwd);
      // List<Membership> memberships = membershipPresenter.getUserMemberships(pUserLogin);
      final List<Membership> memberships = membershipPresenter
          .getAllEffectiveUserMembershipsForUserAndProject(projectTestId, pUserLogin);
      for (final Membership membership : memberships)
      {
        if ((membership.getProject().getName()).equals(projectTestId))
        {
          // we suppose only 1 role for a member into the target projecttest.
          if (membership.getRole().getName().equals(roleToChange))
          {
            roleToChangeFound = true;
            log.info("----------> changeBackRoleForUser: projectId= " + membership.getProject().getName()
                + " role to change is already set  ");
            break;
          }
        }
      }
      if (!roleToChangeFound)
      {
        newRoles.add(roleToChange);
        final UUID userUUID = userPresenter.getUser(pUserLogin).getUuid();
        membershipPresenter.updateUserMembership(projectTestId, userUUID, newRoles, null, false);
        log.info("------------> changeBackRoleForUser for user = " + pUserLogin + " with role = "
            + roleToChange);
      }
    }
    finally
    {
      if (securityManager.checkLogin())
      {
        securityManager.logout();
      }
    }
  }

  /*
   * utility method in order to delete a project placed into a csv file.
   */
  protected void cleanProject() throws Exception
  {
    // project to clean
    // *************** commented to compile !!!
    // List<String> projectsToDelete = readerCsv.getProjectToDelete();
    // try
    // {
    // this.securityManager.login(TestCsvConstants.login, TestCsvConstants.pwd);
    // for (String projectId : projectsToDelete)
    // {
    // ProjectInfo project = projectPresenter.getProjectInfo(projectId);
    // if (project != null)
    // {
    // projectPresenter.deleteProject(projectId);
    // log.info("********************================> projetId = " + projectId
    // + " has been deleted !!!!!!!!!!!!!!!!!!!!!!!!!!");
    // }
    // }
    // }
    // catch (Exception e)
    // {
    // e.printStackTrace();
    // }
    // finally
    // {
    // this.securityManager.logout();
    // }

  }

  protected void deleteUser(final String userId) throws Exception
  {
    try
    {
      securityManager.login(TestConstants.login, TestConstants.pwd);
      userPresenter.deleteUser(userId);
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      securityManager.logout();
    }
  }

  protected String getNormalizedAppliName(final String project, final String pAppliName)
      throws ProjectServiceException, IOException
  {
    String returned = null;
    // removes all whitespaces and non visible characters such as tab, \n
    returned = pAppliName.replaceAll("\\s", "").toLowerCase();
    // remove .
    returned = returned.replaceAll("\\.", "");
    return returned;
  }

}
