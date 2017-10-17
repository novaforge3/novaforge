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

package org.novaforge.forge.it.commons.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
import org.apache.felix.ipojo.junit4osgi.OSGiTestSuite;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.novaforge.forge.it.commons.test.DatabaseCleanupTest;
import org.novaforge.forge.it.commons.test.DatabaseExportTest;
import org.osgi.framework.BundleContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Kind of decorator of the class OSGiTestSuite which add a setUp test before each TestClass and a tearDown
 * test after each TestClass.
 * 
 * @author rols-p
 */
public class NonIntrusiveDBTestSuite extends OSGiTestSuite
{

   /**
    * key = Test Suite Name Value = Map (databaseTester, dataSet)
    */
   private static Map<String, Map<IDatabaseTester, IDataSet>> dbTesters = new HashMap<String, Map<IDatabaseTester, IDataSet>>();
   private List<String> databasesJndiName;

   public NonIntrusiveDBTestSuite(final BundleContext pBundleContext, final List<String> pDatabasesJndiName)
   {
      super(pBundleContext);
      this.databasesJndiName = pDatabasesJndiName;
   }

   @SuppressWarnings("rawtypes")
   public NonIntrusiveDBTestSuite(Class clazz, BundleContext bc, final List<String> pDatabasesJndiName)
   {
      super(clazz, bc);
      this.databasesJndiName = pDatabasesJndiName;
   }

   @SuppressWarnings("rawtypes")
   public NonIntrusiveDBTestSuite(Class clazz, String name, BundleContext bc,
         final List<String> pDatabasesJndiName)
   {
      super(clazz, name, bc);
      this.databasesJndiName = pDatabasesJndiName;
   }

   public NonIntrusiveDBTestSuite(String name, BundleContext bc, final List<String> pDatabasesJndiName)
   {
      super(name, bc);
      this.databasesJndiName = pDatabasesJndiName;
   }

   /**
    * Adds the tests from the given class to the suite.
    *
    * @param testClass
    *           the class to add
    */
   @SuppressWarnings("rawtypes")
   @Override
   public void addTestSuite(Class testClass)
   {
      if (NonIntrusiveDBTestSuite.class.isAssignableFrom(testClass))
      {
         addTest(new NonIntrusiveDBTestSuite(testClass, this.getName(), m_context, databasesJndiName));
      }
      else if (OSGiTestCase.class.isAssignableFrom(testClass))
      {
         addTest(new NonIntrusiveDBTestSuite(DatabaseExportTest.class, this.getName(), m_context,
               databasesJndiName));
         addTest(new OSGiTestSuite(testClass, m_context));
         addTest(new NonIntrusiveDBTestSuite(DatabaseCleanupTest.class, this.getName(), m_context,
               databasesJndiName));
      }
      else if (TestCase.class.isAssignableFrom(testClass))
      {
         addTest(new TestSuite(testClass));
      }
      else
      {
         System.out.println("Error : the " + testClass + " is not a valid test class");
      }
   }

   /**
    * Executes the given {@link Test} with the given {@link TestResult}.
    *
    * @param test
    *           the test
    * @param result
    *           the test result.
    * @see junit.framework.TestSuite#runTest(junit.framework.Test, junit.framework.TestResult)
    */
   @Override
   public void runTest(Test test, TestResult result)
   {
      if (test instanceof NonIntrusiveDBTestSuite)
      {
         ((NonIntrusiveDBTestSuite) test).setBundleContext(m_context);
         ((NonIntrusiveDBTestSuite) test).setDatabasesJndiName(databasesJndiName);
         test.run(result);
      }
      if (test instanceof OSGiTestSuite)
      {
         ((OSGiTestSuite) test).setBundleContext(m_context);
         test.run(result);
      }
      else if (test instanceof DatabaseExportTest)
      {
         ((DatabaseExportTest) test).setBundleContext(m_context);
         ((DatabaseExportTest) test).setDatabasesJndiName(databasesJndiName);
         test.run(result);
         Map<IDatabaseTester, IDataSet> testers = ((DatabaseExportTest) test).getDatabaseTesters();
         getDbTesters().put(this.getName(), testers);
      }
      else if (test instanceof DatabaseCleanupTest)
      {
         ((DatabaseCleanupTest) test).setBundleContext(m_context);
         ((DatabaseCleanupTest) test).setDatabasesJndiName(databasesJndiName);
         ((DatabaseCleanupTest) test).setDatabaseTesters(getDbTesters().get(this.getName()));
         test.run(result);
      }
      else if (test instanceof OSGiTestCase)
      {
         ((OSGiTestCase) test).setBundleContext(m_context);
         test.run(result);
      }
      else
      {
         test.run(result);
      }

   }

   public static Map<String, Map<IDatabaseTester, IDataSet>> getDbTesters()
   {
      return dbTesters;
   }

   public List<String> getDatabasesJndiName()
   {
      return databasesJndiName;
   }

   public void setDatabasesJndiName(List<String> databasesJndiName)
   {
      this.databasesJndiName = databasesJndiName;
   }

}
