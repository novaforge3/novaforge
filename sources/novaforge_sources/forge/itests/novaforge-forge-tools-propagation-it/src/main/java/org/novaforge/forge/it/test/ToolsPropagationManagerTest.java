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
import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.Factory;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.it.test.datas.ReportTest;
import org.novaforge.forge.it.test.datas.TestConstants;
import org.osgi.framework.ServiceReference;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author blachonm
 */
public class ToolsPropagationManagerTest extends ToolsPropagationItBaseTest
{
  private static final Log  log = LogFactory.getLog(ToolsPropagationManagerTest.class);
  private ComponentInstance instance;

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    // search for factory with name=ImportDatas
    ServiceReference factoryReference = getServiceReference(Factory.class.getName(),
        "(factory.name=ImportDatas)");
    assertNotNull(factoryReference);
    Factory factory = (Factory) getServiceObject(factoryReference);
    assertNotNull(factory);
    instance = factory.createComponentInstance(null);
  }

  @Override
  public void tearDown() throws Exception
  {
    // delete the iPOJO component instance
    instance.dispose();
  }

  public void testImportCSVApplicationData() throws Exception
  {
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), false);
    log.info("***************************************************************************");
    log.info("****************** checking import data  **************************************");
    log.info("***************** for applications (plugin DB/tool DB)  ***********************");
    log.info("***************************************************************************");

    // Needed because of authorization checking
    this.securityManager.login(TestConstants.login, TestConstants.pwd);

    // requirement and delivery (2) are added the list of external ones (12).
    List<PluginMetadata> allPluginsMetaDatas = this.pluginsManager.getAllPlugins();
    assertTrue("number of installed plugin category is not 14", allPluginsMetaDatas.size() == 12 + 2);

    log.info("starting check of the metadata ....");
    checkMetaDataNovaForge(allPluginsMetaDatas);

    log.info("starting the plugin instances ......");
    checkPluginInstance();
  }

  private void checkMetaDataNovaForge(List<PluginMetadata> allPluginsMetaDatas) throws PluginManagerException
  {
    // *** BUILDING_CATEGORY
    List<PluginMetadata> pluginsMetadatas = this.pluginsManager
        .getPluginsMetadataByCategory(TestConstants.BUILDING_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // status
    PluginStatus pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.BUILDING_TYPE + " is not activated", "ACTIVATED",
        pluginStatus.name());
    // availability
    assertTrue("Plugin with type: " + TestConstants.BUILDING_TYPE + " is not available", pluginsMetadatas
        .get(0).isAvailable());

    // *** ECM_CATEGORY
    pluginsMetadatas = this.pluginsManager.getPluginsMetadataByCategory(TestConstants.ECM_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // status
    pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.ECM_TYPE + " is not activated", "ACTIVATED",
        pluginStatus.name());
    // availability
    assertTrue("Plugin with type: " + TestConstants.ECM_TYPE + " is not available", pluginsMetadatas
        .get(0).isAvailable());

    // *** GED_CATEGORY
    pluginsMetadatas = this.pluginsManager.getPluginsMetadataByCategory(TestConstants.GED_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // status
    pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.GED_TYPE + " is not activated", "ACTIVATED",
        pluginStatus.name());
    // availability
    assertTrue("Plugin with type: " + TestConstants.GED_TYPE + " is not available", pluginsMetadatas
        .get(0).isAvailable());

    // *** REPOSITORY_CATEGORY
    pluginsMetadatas = this.pluginsManager.getPluginsMetadataByCategory(TestConstants.REPOSITORY_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // status
    pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.REPOSITORY_TYPE + " is not activated", "ACTIVATED",
        pluginStatus.name());
    // availability
    assertTrue("Plugin with type: " + TestConstants.REPOSITORY_TYPE + " is not available",
        pluginsMetadatas.get(0).isAvailable());

    // *** SURVEY_CATEGORY
    pluginsMetadatas = this.pluginsManager.getPluginsMetadataByCategory(TestConstants.SURVEY_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // status
    pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.SURVEY_TYPE + " is not activated", "ACTIVATED",
        pluginStatus.name());
    // availability
    assertTrue("Plugin with type: " + TestConstants.SURVEY_TYPE + " is not available", pluginsMetadatas
        .get(0).isAvailable());

    // *** BUGTRACKER_CATEGORY
    pluginsMetadatas = this.pluginsManager.getPluginsMetadataByCategory(TestConstants.BUGTRACKER_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // // status
    pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.BUGTRACKER_TYPE + " is not activated", "ACTIVATED",
        pluginStatus.name());
    // // availability
    assertTrue("Plugin with type: " + TestConstants.BUGTRACKER_TYPE + " is not available",
        pluginsMetadatas.get(0).isAvailable());

    // *** FORUM_CATEGORY
    pluginsMetadatas = this.pluginsManager.getPluginsMetadataByCategory(TestConstants.FORUM_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // status
    pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.FORUM_TYPE + " is not activated", "ACTIVATED",
        pluginStatus.name());
    // availability
    assertTrue("Plugin with type: " + TestConstants.FORUM_TYPE + " is not available", pluginsMetadatas
        .get(0).isAvailable());

    // *** QUALIMETRY_CATEGORY
    pluginsMetadatas = this.pluginsManager.getPluginsMetadataByCategory(TestConstants.QUALIMETRY_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // status
    pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.QUALIMETRY_TYPE + " is not activated", "ACTIVATED",
        pluginStatus.name());
    // availability
    assertTrue("Plugin with type: " + TestConstants.QUALIMETRY_TYPE + " is not available",
        pluginsMetadatas.get(0).isAvailable());

    // *** MAILING_LIST_CATEGORY
    pluginsMetadatas = this.pluginsManager
        .getPluginsMetadataByCategory(TestConstants.MAILING_LIST_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // status
    pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.MAILING_LIST_TYPE + " is not activated",
        "ACTIVATED", pluginStatus.name());
    // availability
    assertTrue("Plugin with type: " + TestConstants.MAILING_LIST_TYPE + " is not available",
        pluginsMetadatas.get(0).isAvailable());

    // *** TEST_CATEGORY
    pluginsMetadatas = this.pluginsManager.getPluginsMetadataByCategory(TestConstants.TEST_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // status
    pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.TEST_TYPE + " is not activated", "ACTIVATED",
        pluginStatus.name());
    // availability
    assertTrue("Plugin with type: " + TestConstants.TEST_TYPE + " is not available",
        pluginsMetadatas.get(0).isAvailable());

    // *** WIKI_CATEGORY
    pluginsMetadatas = this.pluginsManager.getPluginsMetadataByCategory(TestConstants.WIKI_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // status
    pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.WIKI_TYPE + " is not activated", "ACTIVATED",
        pluginStatus.name());
    // availability
    assertTrue("Plugin with type: " + TestConstants.WIKI_TYPE + " is not available",
        pluginsMetadatas.get(0).isAvailable());

    // *** SUBVERSION_CATEGORY
    pluginsMetadatas = this.pluginsManager.getPluginsMetadataByCategory(TestConstants.SUBVERSION_CATEGORY);
    assertNotNull(pluginsMetadatas);
    // uuid
    uuid = pluginsMetadatas.get(0).getUUID();
    assertNotNull(uuid);
    // status
    pluginStatus = pluginsMetadatas.get(0).getStatus();
    assertEquals("Plugin with type: " + TestConstants.SUBVERSION_TYPE + " is not activated", "ACTIVATED",
        pluginStatus.name());
    // availability
    assertTrue("Plugin with type: " + TestConstants.SUBVERSION_TYPE + " is not available",
        pluginsMetadatas.get(0).isAvailable());

    this.securityManager.logout();
    ReportTest.writeTestResult(this.getClass().getName(), this.getName(), true);
  }

  /*
   * **********************************************************************************
   * 21/02/2013: suppress check for not tested tools: jenkins, sonar, spip, limesurvey
   * **********************************************************************************
   */
  private void checkPluginInstance() throws Exception
  {
    // WARNING !!!!:
    // Asserts are designed with following rules:
    // - only one project exists within csv files
    // - only one application per project is defined

    // check plugin instances for all tools
    // check there's one and only one instance declared into plugin_instance table

    // QUALIMETRY_TYPE DATABASE_PLUGIN_SONAR
    // Map<String, String> returned = getPluginInstance(TestConstants.QUALIMETRY_TYPE,
    // TestConstants.DATABASE_PLUGIN_SONAR);
    // assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    // String normalizedAppliName = returned.keySet().iterator().next();
    // assertEquals(
    // TestConstants.QUALIMETRY_TYPE
    // +
    // ": The name of the application found into cvs file is not the same as one into plugin_instance table",
    // normalizedAppliName, returned.get(normalizedAppliName));

    // GED_TYPE DATABASE_PLUGIN_ALFRESCO
    Map<String, String> returned = getPluginInstance(TestConstants.GED_TYPE,
        TestConstants.DATABASE_PLUGIN_ALFRESCO);
    assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    String normalizedAppliName = returned.keySet().iterator().next();
    assertEquals(
        TestConstants.GED_TYPE
            + ": The name of the application found into cvs file is not the same as one into plugin_instance table",
        normalizedAppliName, returned.get(normalizedAppliName));

    // REPOSITORY_TYPE DATABASE_PLUGIN_NEXUS
    returned = getPluginInstance(TestConstants.REPOSITORY_TYPE, TestConstants.DATABASE_PLUGIN_NEXUS);
    assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    normalizedAppliName = returned.keySet().iterator().next();
    assertEquals(
        TestConstants.REPOSITORY_TYPE
            + ": The name of the application found into cvs file is not the same as one into plugin_instance table",
        normalizedAppliName, returned.get(normalizedAppliName));

    // SURVEY_TYPE DATABASE_PLUGIN_LIMESURVEY
    // returned = getPluginInstance(TestConstants.SURVEY_TYPE,
    // TestConstants.DATABASE_PLUGIN_LIMESURVEY);
    // assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    // normalizedAppliName = returned.keySet().iterator().next();
    // assertEquals(
    // TestConstants.SURVEY_TYPE
    // +
    // ": The name of the application found into cvs file is not the same as one into plugin_instance table",
    // normalizedAppliName, returned.get(normalizedAppliName));

    // BUGTRACKER_TYPE DATABASE_PLUGIN_MANTIS
    returned = getPluginInstance(TestConstants.BUGTRACKER_TYPE, TestConstants.DATABASE_PLUGIN_MANTIS);
    assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    normalizedAppliName = returned.keySet().iterator().next();
    assertEquals(
        TestConstants.BUGTRACKER_TYPE
            + ": The name of the application found into cvs file is not the same as one into plugin_instance table",
        normalizedAppliName, returned.get(normalizedAppliName));

    // BUILDING_TYPE DATABASE_PLUGIN_JENKINS
    // returned = getPluginInstance(TestConstants.BUILDING_TYPE,
    // TestConstants.DATABASE_PLUGIN_JENKINS);
    // assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    // normalizedAppliName = returned.keySet().iterator().next();
    // assertEquals(
    // TestConstants.BUILDING_TYPE
    // +
    // ": The name of the application found into cvs file is not the same as one into plugin_instance table",
    // normalizedAppliName, returned.get(normalizedAppliName));

    // FORUM_TYPE DATABASE_PLUGIN_PHPBB
    returned = getPluginInstance(TestConstants.FORUM_TYPE, TestConstants.DATABASE_PLUGIN_PHPBB);
    assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    normalizedAppliName = returned.keySet().iterator().next();
    assertEquals(
        TestConstants.FORUM_TYPE
            + ": The name of the application found into cvs file is not the same as one into plugin_instance table",
        normalizedAppliName, returned.get(normalizedAppliName));

    // ECM_TYPE DATABASE_PLUGIN_SPIP
    // returned = getPluginInstance(TestConstants.ECM_TYPE, TestConstants.DATABASE_PLUGIN_SPIP);
    // assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    // normalizedAppliName = returned.keySet().iterator().next();
    // assertEquals(
    // TestConstants.ECM_TYPE
    // +
    // ": The name of the application found into cvs file is not the same as one into plugin_instance table",
    // normalizedAppliName, returned.get(normalizedAppliName));

    // MAILING_LIST_TYPE DATABASE_PLUGIN_SYMPA
    returned = getPluginInstance(TestConstants.MAILING_LIST_TYPE, TestConstants.DATABASE_PLUGIN_SYMPA);
    assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    normalizedAppliName = returned.keySet().iterator().next();
    assertEquals(
        TestConstants.MAILING_LIST_TYPE
            + ": The name of the application found into cvs file is not the same as one into plugin_instance table",
        normalizedAppliName, returned.get(normalizedAppliName));

    // WIKI_TYPE DATABASE_PLUGIN_WIKI
    returned = getPluginInstance(TestConstants.WIKI_TYPE, TestConstants.DATABASE_PLUGIN_WIKI);
    assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    normalizedAppliName = returned.keySet().iterator().next();
    assertEquals(
        TestConstants.WIKI_TYPE
            + ": The name of the application found into cvs file is not the same as one into plugin_instance table",
        normalizedAppliName, returned.get(normalizedAppliName));

    // TEST_TYPE DATABASE_PLUGIN_TESTLINK
    returned = getPluginInstance(TestConstants.TEST_TYPE, TestConstants.DATABASE_PLUGIN_TESTLINK);
    assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    normalizedAppliName = returned.keySet().iterator().next();
    assertEquals(
        TestConstants.TEST_TYPE
            + ": The name of the application found into cvs file is not the same as one into plugin_instance table",
        normalizedAppliName, returned.get(normalizedAppliName));

    // SUBVERSION_TYPE DATABASE_PLUGIN_SVN
    returned = getPluginInstance(TestConstants.SUBVERSION_TYPE, TestConstants.DATABASE_PLUGIN_SVN);
    assertTrue("The number of instances into the plugin base is not 1", returned.size() == 1);
    normalizedAppliName = returned.keySet().iterator().next();
    assertEquals(
        TestConstants.SUBVERSION_TYPE
            + ": The name of the application found into cvs file is not the same as one into plugin_instance table",
        normalizedAppliName, returned.get(normalizedAppliName));
  }

  private Map<String, String> getPluginInstance(String appliType, String pluginDatabase) throws Exception
  {
    Map<String, String> returned = new HashMap<String, String>();
    // for (Iterator<String> iterator = readerCsv.getProjects().keySet().iterator(); iterator.hasNext();)
    for (Iterator<String> iterator = xmlData.getProjects().keySet().iterator(); iterator.hasNext();)
    {
      String project = iterator.next();
      log.info(" projet = " + project);
      String normalizedSonarAppliName = getNormalizedAppliName(project, appliType);
      Connection con = DriverManager.getConnection(pluginDatabase, TestConstants.USER,
          TestConstants.PASSWORD);

      String query = " SELECT p.configuration_id from plugin_instance AS p where p.forge_project_id= \""
          + project + "\"";
      Statement stmt = con.createStatement();
      ResultSet resultSet = stmt.executeQuery(query);

      while (resultSet.next())
      {
        String appli = resultSet.getString(1);
        returned.put(normalizedSonarAppliName, appli);
      }
      con.close();
    }
    return returned;
  }

}