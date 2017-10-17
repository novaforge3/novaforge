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

package org.novaforge.forge.tools.junit4osgi.internal;

import junit.framework.Test;
import junit.framework.TestResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.felix.ipojo.junit4osgi.OSGiJunitRunner;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.tools.junit4osgi.internal.report.DefaultResultPrinter;
import org.novaforge.forge.tools.junit4osgi.internal.report.LogService;
import org.novaforge.forge.tools.junit4osgi.internal.report.ReportResultListener;
import org.novaforge.forge.tools.junit4osgi.internal.report.XMLReport;

import java.io.File;
import java.util.Iterator;
import java.util.List;

@Command(scope = "novaforge-itests", name = "junit", description = "Start OSGI junit test")
public class TestStarter extends OsgiCommandSupport
{
  private static final Log           log             = LogFactory.getLog(TestStarter.class);
  private final DefaultResultPrinter consolePrinter  = new DefaultResultPrinter(System.out);
  @Option(name = "-s", aliases = "--see-list", description = "The liste test suite", required = false,
      multiValued = false)
  String                             define_see_list = null;
  private                                OSGiJunitRunner runner;
  @Argument(index = 0, name = "test_suite", description = "ID of test suite to launch", required = true,
            multiValued = false) private String          test_suite;
  private                                LogService      logService;

  public void setRunner(final OSGiJunitRunner runner)
  {
    this.runner = runner;
  }

  @Override
  protected Object doExecute() throws Exception
  {
    logService = new LogService();
    boolean isExecuted = false;
    String java_tmp_dir;

    int count = 0;
    final List tests = runner.getTests();
    if (tests.size() == 0)
    {
      System.out.println("No available OSGI test suites");
    }
    else
    {
      final Iterator iterator = tests.iterator();
      System.out.println("Available OSGI test suites:");
      java_tmp_dir = System.getProperty("java.io.tmpdir");
      final File testDir = new File(java_tmp_dir);
      while (iterator.hasNext())
      {
        final Object objTest = iterator.next();
        final Test t = (Test) objTest;
        System.out.println("[" + count + "] " + t.toString());
        if (test_suite.equals(t.toString()))
        {
          // runner.run(t);
          System.out.println("Executing test suite = " + test_suite);
          runTestWithReport(testDir, objTest);
          isExecuted = true;
        }
        count++;
      }
      if (!isExecuted)
      {
        System.out.println("No test suite matching test_suite=" + test_suite + " executed");
      }
    }
    return null;
  }

  private void runTestWithReport(final File testDir, final Object objTest)
  {
    try
    {
      final Test test = (Test) objTest;
      final XMLReport report = new XMLReport();
      final TestResult tr = new TestResult();
      tr.addListener(new ReportResultListener(report, logService));
      tr.addListener(consolePrinter);

      final long startTime = System.currentTimeMillis();

      test.run(tr);

      final long endTime = System.currentTimeMillis();
      final long runTime = endTime - startTime;
      consolePrinter.print(tr, runTime);

      report.generateReport(test, tr, testDir, bundleContext, null);

    }
    catch (final Exception e)
    {
      System.out.println("error generating the test report: " + e);
    }
  }
}
