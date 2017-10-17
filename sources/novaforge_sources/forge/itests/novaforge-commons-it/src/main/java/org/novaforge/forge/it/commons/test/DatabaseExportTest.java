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

package org.novaforge.forge.it.commons.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.JndiDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * 
 * @author rols-p
 */
public class DatabaseExportTest extends OSGiTestCase
{
   private static final Log               log = LogFactory.getLog(DatabaseExportTest.class);

   private Map<IDatabaseTester, IDataSet> databaseTesters;
   private List<String> databasesJndiName;

   public Map<IDatabaseTester, IDataSet> getDatabaseTesters()
   {
      return databaseTesters;
   }

   /**
    * Test called in order to export data from databases to datasets
    *
    * @throws Exception
    */
   public void testExportDatabase() throws Exception
   {
      databaseTesters = new HashMap<IDatabaseTester, IDataSet>(getDatabasesJndiName().size());
      ClassLoader theGoodOne = getClass().getClassLoader();
      ClassLoader theOldOne = Thread.currentThread().getContextClassLoader();
      try
      {
         Thread.currentThread().setContextClassLoader(theGoodOne);
         for (String databaseName : getDatabasesJndiName())
         {
            IDatabaseTester databaseTester = new JndiDatabaseTester(databaseName);

            DatabaseConfig dbConfig = databaseTester.getConnection().getConfig();
            dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
            dbConfig.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER, new MySqlMetadataHandler());
            dbConfig.setProperty("http://www.dbunit.org/features/datatypeWarning", false);

            ITableFilter filter = new DatabaseSequenceFilter(databaseTester.getConnection());
            IDataSet dataset = new FilteredDataSet(filter, databaseTester.getConnection().createDataSet());
            IDataSet initialDataSetFromDb = new CachedDataSet(dataset);

            for (String name : initialDataSetFromDb.getTableNames())
            {
               log.info("DataSet: table: " + name);
               ITable table = initialDataSetFromDb.getTable(name);
               log.info("Number of rows: = " + table.getRowCount());
            }

            databaseTesters.put(databaseTester, initialDataSetFromDb);
            databaseTester.setSetUpOperation(DatabaseOperation.NONE);
            databaseTester.onSetup();

            log.info(String.format("Database name=%s successfully exported.", databaseTester.getConnection()
                  .getSchema()));
         }
      }
      finally
      {
         Thread.currentThread().setContextClassLoader(theOldOne);
         super.setUp();
      }
   }

   public List<String> getDatabasesJndiName()
   {
      return databasesJndiName;
   }

   public void setDatabasesJndiName(final List<String> databasesJndiName)
   {
      this.databasesJndiName = databasesJndiName;
   }

}
