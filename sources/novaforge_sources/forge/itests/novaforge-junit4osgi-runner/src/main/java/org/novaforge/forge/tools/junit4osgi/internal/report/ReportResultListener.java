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

import java.io.PrintStream;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;

/**
 * @author <a href="mailto:.org">Felix Project Team</a>
 */
public class ReportResultListener implements TestListener
{

   /**
    * The XML Report.
    */
   private final XMLReport          m_report;

   /**
    * Check if the test has failed or thrown an error.
    */
   private boolean                  m_abort;

   /**
    * Backup of the {@link System#out} stream.
    */
   private final PrintStream        m_outBackup = System.out;

   /**
    * Backup of the {@link System#err} stream.
    */
   private final PrintStream        m_errBackup = System.err;

   /**
    * The output stream used during the test execution.
    */
   private final StringOutputStream m_out       = new StringOutputStream();

   /**
    * The error stream used during the test execution.
    */
   private final StringOutputStream m_err       = new StringOutputStream();

   private final boolean            hideOutputs = false;

   private final LogService         logService;

   /**
    * Creates a ResultListener.
    * 
    * @param report
    *           the XML report
    */
   public ReportResultListener(XMLReport report, LogService logService)
   {
      this.m_report = report;
      this.logService = logService;
   }

   /**
    * An error occurs during the test execution.
    * 
    * @param test
    *           the test in error
    * @param throwable
    *           the thrown error
    * @see junit.framework.TestListener#addError(junit.framework.Test, java.lang.Throwable)
    */
   public void addError(Test test, Throwable throwable)
   {
      m_report.testError(test, throwable, m_out.toString(), m_err.toString(), logService.getLoggedMessages());
      m_abort = true;
   }

   /**
    * An failure occurs during the test execution.
    * 
    * @param test
    *           the failing test
    * @param assertionfailederror
    *           the failure
    * @see junit.framework.TestListener#addFailure(junit.framework.Test, junit.framework.AssertionFailedError)
    */
   public void addFailure(Test test, AssertionFailedError assertionfailederror)
   {
      m_report.testFailed(test, assertionfailederror, m_out.toString(), m_err.toString(),
            logService.getLoggedMessages());
      m_abort = true;

   }

   /**
    * The test ends.
    * 
    * @param test
    *           the test
    * @see junit.framework.TestListener#endTest(junit.framework.Test)
    */
   public void endTest(Test test)
   {
      if (!m_abort)
      {
         m_report.testSucceeded(test);
      }
      System.setErr(m_errBackup);
      System.setOut(m_outBackup);
      logService.reset();
   }

   /**
    * The test starts.
    * 
    * @param test
    *           the test
    * @see junit.framework.TestListener#startTest(junit.framework.Test)
    */
   public void startTest(Test test)
   {
      m_abort = false;
      m_report.testStarting();
      System.setErr(new ReportPrintStream(m_err, m_errBackup, hideOutputs));
      System.setOut(new ReportPrintStream(m_out, m_outBackup, hideOutputs));
      logService.enableOutputStream();
   }

}