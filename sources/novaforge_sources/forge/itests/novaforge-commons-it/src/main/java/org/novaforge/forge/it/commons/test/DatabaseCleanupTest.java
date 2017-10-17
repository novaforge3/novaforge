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
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * 
 * @author rols-p
 */
public class DatabaseCleanupTest extends OSGiTestCase
{
   private static final Log               log = LogFactory.getLog(DatabaseCleanupTest.class);

   private Map<IDatabaseTester, IDataSet> databaseTesters;
   private List<String> databasesJndiName;

   public void setDatabaseTesters(Map<IDatabaseTester, IDataSet> databaseTesters)
   {
      this.databaseTesters = databaseTesters;
   }

   public List<String> getDatabasesJndiName()
   {
      return databasesJndiName;
   }

   public void setDatabasesJndiName(final List<String> databasesJndiName)
   {
      this.databasesJndiName = databasesJndiName;
   }

   /**
    * Test called in order to cleanup databases.
    * 
    * @throws Exception
    */
   public void testCleanupDatabase() throws Exception
   {
      for (IDatabaseTester databaseTester : databaseTesters.keySet())
      {
         databaseTester.setDataSet(databaseTesters.get(databaseTester));

         databaseTester.setTearDownOperation(DatabaseOperation.CLEAN_INSERT);
         databaseTester.onTearDown();
         log.info(String.format("Database name=%s successfully cleaned.", databaseTester.getConnection()
               .getSchema()));
      }

      super.tearDown();
   }
}
