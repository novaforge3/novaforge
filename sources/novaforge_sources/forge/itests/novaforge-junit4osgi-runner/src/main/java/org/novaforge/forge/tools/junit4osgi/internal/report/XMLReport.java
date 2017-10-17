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
package org.novaforge.forge.tools.junit4osgi.internal.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import junit.framework.Test;
import junit.framework.TestResult;

import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.PrettyPrintXMLWriter;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomWriter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * This class generates test result as XML files compatible with Surefire.
 * 
 * @author <a href="mailto:.org">Felix Project Team</a>
 */
public class XMLReport extends Report
{

   /**
    * List of results.
    */
   private final List m_results = new ArrayList();

   /**
    * A test ends successfully.
    * 
    * @param test
    *           the test executed successfully.
    */
   public void testSucceeded(Test test)
   {
      super.testSucceeded();

      long runTime = this.m_endTime - this.m_startTime;

      Xpp3Dom testCase = createTestElement(test, runTime);

      m_results.add(testCase);
   }

   /**
    * A test throws an unexpected errors.
    * 
    * @param test
    *           the test in error
    * @param e
    *           the thrown exception
    * @param out
    *           the output messages printed during the test execution
    * @param err
    *           the error messages printed during the test execution
    * @param log
    *           the messages logged during the test execution
    */
   public void testError(Test test, Throwable e, String out, String err, String log)
   {
      super.testError(test);

      writeTestProblems(test, e, "error", out, err, log);
   }

   /**
    * A test fails.
    * 
    * @param test
    *           the failing test
    * @param e
    *           the thrown failure
    * @param out
    *           the output messages printed during the test execution
    * @param err
    *           the error messages printed during the test execution
    * @param log
    *           the messages logged during the test execution
    */
   public void testFailed(Test test, Throwable e, String out, String err, String log)
   {
      super.testFailed(test);

      writeTestProblems(test, e, "failure", out, err, log);
   }

   /**
    * Utility method writing failed and in error test result in the report.
    * 
    * @param test
    *           the test
    * @param e
    *           the thrown error
    * @param name
    *           type of failure ("error" or "failure")
    * @param out
    *           the output messages printed during the test execution
    * @param err
    *           the error messages printed during the test execution
    * @param log
    *           the messages logged during the test execution
    */
   private void writeTestProblems(Test test, Throwable e, String name, String out, String err, String log)
   {

      long runTime = m_endTime - m_startTime;

      Xpp3Dom testCase = createTestElement(test, runTime);

      Xpp3Dom element = createElement(testCase, name);

      String stackTrace = getStackTrace(test, e);

      Throwable t = e;

      if (t != null)
      {

         String message = t.getMessage();

         if (message != null)
         {
            element.setAttribute("message", message);

            element.setAttribute("type",
                  stackTrace.indexOf(":") > -1 ? stackTrace.substring(0, stackTrace.indexOf(":"))
                        : stackTrace);
         }
         else
         {
            element.setAttribute("type", new StringTokenizer(stackTrace).nextToken());
         }
      }

      if (stackTrace != null)
      {
         element.setValue(stackTrace);
      }

      addOutputStreamElement(out, "system-out", testCase);

      addOutputStreamElement(err, "system-err", testCase);

      if (log != null)
      {
         addOutputStreamElement(log, "log-service", testCase);
      }

      m_results.add(testCase);
   }

   /**
    * Generates the XML reports.
    * 
    * @param test
    *           the test
    * @param tr
    *           the test result
    * @param reportsDirectory
    *           the directory in which reports are created.
    * @param bc
    *           the bundle context (to get installed bundles)
    * @param configuration
    *           the Felix configuration
    * @throws Exception
    *            when the XML report cannot be generated correctly
    */
   public void generateReport(Test test, TestResult tr, File reportsDirectory, BundleContext bc,
         Map configuration) throws Exception
   {
      long runTime = this.m_endTime - this.m_startTime;

      Xpp3Dom testSuite = createTestSuiteElement(test, runTime);

      showProperties(testSuite, bc, configuration);

      testSuite.setAttribute("tests", String.valueOf(tr.runCount()));

      testSuite.setAttribute("errors", String.valueOf(tr.errorCount()));

      testSuite.setAttribute("failures", String.valueOf(tr.failureCount()));

      for (Iterator i = m_results.iterator(); i.hasNext();)
      {
         Xpp3Dom testcase = (Xpp3Dom) i.next();
         testSuite.addChild(testcase);
      }

      File reportFile = new File(reportsDirectory, "TEST-" + getReportName(test).replace(' ', '_') + ".xml");

      File reportDir = reportFile.getParentFile();

      reportDir.mkdirs();

      PrintWriter writer = null;

      try
      {
         writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportFile),
               "UTF-8")));

         writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + NL);

         Xpp3DomWriter.write(new PrettyPrintXMLWriter(writer), testSuite);
      }
      catch (UnsupportedEncodingException e)
      {
         throw new Exception("Unable to use UTF-8 encoding", e);
      }
      catch (FileNotFoundException e)
      {
         throw new Exception("Unable to create file: " + e.getMessage(), e);
      }
      finally
      {
         IOUtil.close(writer);
      }
   }

   /**
    * Creates a XML test case element.
    * 
    * @param test
    *           the test
    * @param runTime
    *           the elapsed time to execute the test.
    * @return the XML element describing the given test.
    */
   private Xpp3Dom createTestElement(Test test, long runTime)
   {
      Xpp3Dom testCase = new Xpp3Dom("testcase");
      testCase.setAttribute("name", getReportName(test));
      double time = (double) runTime / (double) 1000;
      testCase.setAttribute("time", Double.toString(time));
      testCase.setAttribute("classname", test.getClass().getName());
      return testCase;
   }

   /**
    * Creates a XML test suite element.
    * 
    * @param test
    *           the test
    * @param runTime
    *           the elapsed time to execute the test suite.
    * @return the XML element describing the given test suite.
    */
   private Xpp3Dom createTestSuiteElement(Test test, long runTime)
   {
      Xpp3Dom testCase = new Xpp3Dom("testsuite");
      testCase.setAttribute("name", getReportName(test));
      testCase.setAttribute("time", Long.toString(runTime));
      return testCase;
   }

   /**
    * Computes report name.
    * 
    * @param test
    *           the test
    * @return the report name
    */
   private static String getReportName(Test test)
   {
      String report = test.toString();

      if (report.indexOf("(") > 0)
      {
         report = report.substring(0, report.indexOf("("));
      }
      return report;
   }

   /**
    * Creates an XML element.
    * 
    * @param element
    *           the parent element
    * @param name
    *           the name of the element to create
    * @return the resulting XML tree.
    */
   private Xpp3Dom createElement(Xpp3Dom element, String name)
   {
      Xpp3Dom component = new Xpp3Dom(name);

      element.addChild(component);

      return component;
   }

   /**
    * Adds system properties to the XML report. This method also adds installed bundles.
    * 
    * @param testSuite
    *           the XML element.
    * @param bc
    *           the bundle context
    * @param configuration
    *           the configuration of the underlying OSGi platform
    */
   private void showProperties(Xpp3Dom testSuite, BundleContext bc, Map configuration)
   {
      Xpp3Dom properties = createElement(testSuite, "properties");

      Properties systemProperties = System.getProperties();

      if (systemProperties != null)
      {
         Enumeration propertyKeys = systemProperties.propertyNames();

         while (propertyKeys.hasMoreElements())
         {
            String key = (String) propertyKeys.nextElement();

            String value = systemProperties.getProperty(key);

            if (value == null)
            {
               value = "null";
            }

            Xpp3Dom property = createElement(properties, "property");

            property.setAttribute("name", key);

            property.setAttribute("value", value);

         }
      }

      if (configuration != null)
      {
         Iterator it = configuration.keySet().iterator();

         while (it.hasNext())
         {
            String key = (String) it.next();

            Object obj = configuration.get(key);
            String value = null;
            if (obj == null)
            {
               value = "null";
            }
            else if (obj instanceof String)
            {
               value = (String) obj;
            }
            else
            {
               value = obj.toString();
            }

            Xpp3Dom property = createElement(properties, "property");

            property.setAttribute("name", key);

            property.setAttribute("value", value);

         }
      }

      Xpp3Dom bundle = createElement(properties, "property");
      Bundle[] bundles = bc.getBundles();
      for (int i = 0; i < bundles.length; i++)
      {
         String sn = bundles[i].getSymbolicName();
         String state = "UNKNOWN";
         switch (bundles[i].getState())
         {
         case Bundle.ACTIVE:
            state = "ACTIVE";
            break;
         case Bundle.INSTALLED:
            state = "INSTALLED";
            break;
         case Bundle.RESOLVED:
            state = "RESOLVED";
            break;
         case Bundle.UNINSTALLED:
            state = "UNINSTALLED";
            break;
         default:
            break;
         }
         bundle.setAttribute("name", "bundle-" + sn);
         bundle.setAttribute("value", state);
      }

   }

   /**
    * Adds messages written during the test execution in the XML tree.
    * 
    * @param stdOut
    *           the messages
    * @param name
    *           the name of the stream (out, error, log)
    * @param testCase
    *           the XML tree
    */
   private void addOutputStreamElement(String stdOut, String name, Xpp3Dom testCase)
   {
      if (stdOut != null && stdOut.trim().length() > 0)
      {
         createElement(testCase, name).setValue(stdOut);
      }
   }

}