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


package org.novaforge.forge.tools.junit4osgi.internal;

import java.io.File;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import junit.framework.Test;
import junit.framework.TestResult;

import org.apache.felix.ipojo.junit4osgi.OSGiJunitRunner;
import org.novaforge.forge.tools.junit4osgi.internal.report.DefaultResultPrinter;
import org.novaforge.forge.tools.junit4osgi.internal.report.LogService;
import org.novaforge.forge.tools.junit4osgi.internal.report.ReportResultListener;
import org.novaforge.forge.tools.junit4osgi.internal.report.XMLReport;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * JUnit Immediate Runner executes JUnit tests in an OSGi environment when it detects new test suites. OSGi
 * bundles that holds test suites to be executed must declare in their manifest the following property: <br>
 * <code>&lt;Test-Suite&gt;com.foo.MyTestSuite&lt;/Test-Suite&gt;</code> <br>
 * where <code>com.foo.MyTestSuite</code> is the fully qualified name of your test suite.
 * 
 * @author lamirang
 */
public class ImmediateRunner implements Runnable
{

   private static final String        DEFAULT_TEST_REPORT_DIR = "/tmp/test";
   /**
    * OSGi Junit Runner service.
    */
   private OSGiJunitRunner  runner;
   private boolean          active;
   private BundleContext             bundleContext;
   private final LogService                logService;

   private final BlockingQueue<Long> bundleIdsQueue;

   /**
    * The result printer. By default, prints result on {@link System#out}
    */
   private final DefaultResultPrinter consolePrinter = new DefaultResultPrinter(System.out);

   private final String               testDirPath;
   private final boolean              writeReport;

   public ImmediateRunner(BundleContext pBundleContext)
   {
      bundleIdsQueue = new ArrayBlockingQueue<Long>(512);
      this.bundleContext = pBundleContext;
      logService = new LogService();

      testDirPath = System.getProperty("novaforge.itests.report.dir", DEFAULT_TEST_REPORT_DIR);
      String reportBoolean = System.getProperty("novaforge.itests.report.enabled", "true");
      writeReport = Boolean.parseBoolean(reportBoolean);
   }

   public void setBundleContext(BundleContext bundleContext)
   {
      this.bundleContext = bundleContext;
   }

   public void starting()
   {
      active = true;
      Thread me = new Thread(this);
      me.start();
   }

   public void stopping()
   {
      active = false;
   }

   public void onBundleArrival(Bundle bundle, String header)
   {
      long bundleId = bundle.getBundleId();
      addBunbleToQueue(bundleId);
   }

   void onBundleDeparture(Bundle bundle)
   {
      // Nothing special to do
   }

   /**
    * {@inheritDoc}
    */
   public void run()
   {
      while ((runner != null) && (active == true))
      {
         Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
         try
         {
            Long bundleId = bundleIdsQueue.take();
            Thread.sleep(5000);

            @SuppressWarnings("rawtypes")
            List tests = runner.getTests(bundleId);
            File testDir = new File(testDirPath);
            if (tests != null && !tests.isEmpty())
            {
               for (Object objTest : tests)
               {
                  if (writeReport == true)
                  {
                     runTestWithReport(testDir, objTest);
                  }
                  else
                  {
                     runTest(objTest);
                  }
               }
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      }
   }

   private void runTest(Object objTest)
   {
      Test test = (Test) objTest;
      runner.run(test);
   }

   private void runTestWithReport(File testDir, Object objTest)
   {
      try
      {
         Test test = (Test) objTest;
         XMLReport report = new XMLReport();
         TestResult tr = new TestResult();
         tr.addListener(new ReportResultListener(report, logService));
         tr.addListener(consolePrinter);

         long startTime = System.currentTimeMillis();

         test.run(tr);

         long endTime = System.currentTimeMillis();
         long runTime = endTime - startTime;
         consolePrinter.print(tr, runTime);

         report.generateReport(test, tr, testDir, bundleContext, null);

      }
      catch (Exception e)
      {
         System.out.println("error generating the test report: " + e);
      }
   }

   public synchronized void addBunbleToQueue(long pBundleId)
   {
      synchronized (bundleIdsQueue)
      {
         if (!bundleIdsQueue.contains(pBundleId))
         {
            bundleIdsQueue.offer(pBundleId);
         }
      }

   }

}
