/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.core.organization.internal.services.test;

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.repositories;
import static org.ops4j.pax.exam.CoreOptions.repository;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.scanFeatures;
import static org.ops4j.pax.exam.CoreOptions.workingDirectory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.karaf.tooling.exam.options.KarafDistributionConfigurationFileReplacementOption;
import org.apache.karaf.tooling.exam.options.KarafDistributionKitConfigurationOption;
import org.apache.karaf.tooling.exam.options.KarafDistributionKitConfigurationOption.Platform;
import org.apache.karaf.tooling.exam.options.KarafDistributionOption;
import org.dbunit.IDatabaseTester;
import org.dbunit.JndiDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;

/**
 * @author blachonm
 */
public abstract class BaseUtil
{
  private static final String NF_DB_OSGI_JNDI_SERVICE_NAME = "osgi:service/javax.sql.DataSource/(osgi.jndi.service.name=jdbc/novaforge)";

  @Configuration
  public Option[] config()
  {
    final MavenArtifactUrlReference karafDistribution = maven().groupId("org.novaforge.products.data")
        .artifactId("karaf-distrib").type("tar.gz").version("2.3.1_0");
    return new Option[] {

        new KarafDistributionKitConfigurationOption(karafDistribution, "Apache Karaf", "2.3.1",
            Platform.WINDOWS).executable("bin\\karaf.bat").filesToMakeExecutable("bin\\admin.bat")
            .unpackDirectory(new File("target/paxexam/unpack/")).useDeployFolder(false),
        new KarafDistributionKitConfigurationOption(karafDistribution, "Apache Karaf", "2.3.1", Platform.NIX)
            .executable("bin/karaf").filesToMakeExecutable("bin/admin")
            .unpackDirectory(new File("target/paxexam/unpack/")).useDeployFolder(false),
        workingDirectory("target/paxrunner/features/"),
        KarafDistributionOption.keepRuntimeFolder(),
        new KarafDistributionConfigurationFileReplacementOption("etc/org.ops4j.pax.logging.cfg", new File(
            "src/test/resources/etc/org.ops4j.pax.logging.cfg")),
        new KarafDistributionConfigurationFileReplacementOption(
            "etc/org.apache.felix.fileinstall-datasource.cfg", new File(
                "src/test/resources/etc/org.apache.felix.fileinstall-datasource.cfg")),
        new KarafDistributionConfigurationFileReplacementOption("etc/org.apache.felix.fileinstall-forge.cfg",
            new File("src/test/resources/etc/org.apache.felix.fileinstall-forge.cfg")),
        new KarafDistributionConfigurationFileReplacementOption("nf-conf/forge/core.configuration.cfg",
            new File("src/test/resources/nf-conf/forge/core.configuration.cfg")),
        new KarafDistributionConfigurationFileReplacementOption("nf-conf/forge/novaforge.properties",
            new File("src/test/resources/nf-conf/forge/novaforge.properties")),
        new KarafDistributionConfigurationFileReplacementOption("nf-conf/forge/novaforge-init.properties",
            new File("src/test/resources/nf-conf/forge/novaforge-init.properties")),
        new KarafDistributionConfigurationFileReplacementOption(
            "nf-conf/datasource/datasource.novaforge.cfg", new File(
                "src/test/resources/nf-conf/datasource/datasource.novaforge.cfg")),
        new KarafDistributionConfigurationFileReplacementOption(
            "nf-conf/datasource/datasource.historization.cfg", new File(
                "src/test/resources/nf-conf/datasource/datasource.historization.cfg")),
        new KarafDistributionConfigurationFileReplacementOption("nf-conf/forge/default.png", new File(
            "src/test/resources/nf-conf/forge/default.png")),
        new KarafDistributionConfigurationFileReplacementOption("etc/activemq.xml", new File(
            "src/test/resources/etc/activemq.xml")),
        new KarafDistributionConfigurationFileReplacementOption("etc/org.apache.activemq.webconsole.cfg",
            new File("src/test/resources/etc/org.apache.activemq.webconsole.cfg")),
        mavenBundle().groupId("org.apache.servicemix.bundles")
            .artifactId("org.apache.servicemix.bundles.dbunit").version("2.4.8_1"),
        scanFeatures(
            maven().groupId("org.novaforge.forge.features").artifactId("novaforge-features").type("xml")
                .versionAsInProject(), "openjpa", "novaforge-jms", "novaforge-security",
            "novaforge-configuration", "novaforge-commons", "novaforge-log", "novaforge-technical",
            "novaforge-webserver", "novaforge-dashboard-apis","novaforge-portal-apis","novaforge-dashboard",
            "novaforge-core-impl", "novaforge-reference", "novaforge-initialization"),

    };
  }

  @Before
  public void setup() throws Exception
  {
    Thread.currentThread().sleep(20000);
  }

  @After
  public void tearDown() throws Exception
  {
    System.out.println("************** tearDown() ********************");
    for (final String databaseName : getDatabasesJndiName())
    {
      final IDatabaseTester databaseTester = new JndiDatabaseTester(databaseName);
      final DatabaseConfig dbConfig = databaseTester.getConnection().getConfig();
      dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
      dbConfig.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
      dbConfig.setProperty(DatabaseConfig.FEATURE_DATATYPE_WARNING, false);
      databaseTester.getConnection();
      // exporting dataset from novaforge
      final ITableFilter filter = new DatabaseSequenceFilter(databaseTester.getConnection());
      final IDataSet dataset = new FilteredDataSet(filter, databaseTester.getConnection().createDataSet());

      System.out.println("************ databaseTester = " + databaseTester.toString());
      System.out.println(String.format("************** Database name=%s successfully cleaned.",
          databaseTester.getConnection().getSchema()));

      databaseTester.setDataSet(dataset);
      databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
      databaseTester.onTearDown();
    }
  }

  protected List<String> getDatabasesJndiName()
  {
    final List<String> databasesName = Arrays.asList(new String[] { NF_DB_OSGI_JNDI_SERVICE_NAME });
    return databasesName;
  }
}
