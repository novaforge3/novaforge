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
import java.text.NumberFormat;
import java.util.Enumeration;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestFailure;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.runner.BaseTestRunner;

/**
 * Result Printer.
 * 
 * @author <a href="mailto:.org">Felix Project Team</a>
 */
public class DefaultResultPrinter implements TestListener
{
   /**
    * the writer.
    */
   PrintStream m_fWriter;

   /**
    * The column .
    */
   int         m_fColumn = 0;

   /**
    * Creates a ResultPrinter.
    * 
    * @param writer
    *           the printer
    */
   public DefaultResultPrinter(PrintStream writer)
   {
      m_fWriter = writer;
   }

   /**
    * Prints the result.
    * 
    * @param result
    *           the test result
    * @param runTime
    *           the test duration
    */
   public synchronized void print(TestResult result, long runTime)
   {
      printHeader(runTime);
      printErrors(result);
      printFailures(result);
      printFooter(result);
   }

   /**
    * Prints message wiating for prompt.
    */
   void printWaitPrompt()
   {
      getWriter().println();
      getWriter().println("<RETURN> to continue");
   }

   /*
    * Internal methods
    */

   /**
    * Prints the result header.
    * 
    * @param runTime
    *           the test execution duration
    */
   protected void printHeader(long runTime)
   {
      getWriter().println();
      getWriter().println("Time: " + elapsedTimeAsString(runTime));
   }

   /**
    * Prints the errors.
    * 
    * @param result
    *           the test result
    */
   protected void printErrors(TestResult result)
   {
      printDefects(result.errors(), result.errorCount(), "error");
   }

   /**
    * Prints failures.
    * 
    * @param result
    *           the test result
    */
   protected void printFailures(TestResult result)
   {
      printDefects(result.failures(), result.failureCount(), "failure");
   }

   /**
    * Prints failures.
    * 
    * @param booBoos
    *           the failures
    * @param count
    *           the number of failures
    * @param type
    *           the type
    */
   protected void printDefects(Enumeration/* <TestFailure> */booBoos, int count, String type)
   {
      if (count == 0)
      {
         return;
      }

      if (count == 1)
      {
         getWriter().println("There was " + count + " " + type + ":");
      }
      else
      {
         getWriter().println("There were " + count + " " + type + "s:");
      }

      for (int i = 1; booBoos.hasMoreElements(); i++)
      {
         printDefect((TestFailure) booBoos.nextElement(), i);
      }
   }

   /**
    * Prints a failure.
    * 
    * @param booBoo
    *           the failure
    * @param count
    *           the count
    */
   public void printDefect(TestFailure booBoo, int count)
   { // only public for testing purposes
      printDefectHeader(booBoo, count);
      printDefectTrace(booBoo);
   }

   /**
    * Prints defect header.
    * 
    * @param booBoo
    *           the failure
    * @param count
    *           the count
    */
   protected void printDefectHeader(TestFailure booBoo, int count)
   {
      // I feel like making this a println, then adding a line giving the throwable a chance to print
      // something
      // before we get to the stack trace.
      getWriter().print(count + ") " + booBoo.failedTest());
   }

   /**
    * Prints the stack trace.
    * 
    * @param booBoo
    *           the failure
    */
   protected void printDefectTrace(TestFailure booBoo)
   {
      getWriter().print(BaseTestRunner.getFilteredTrace(booBoo.trace()));
   }

   /**
    * Prints the footer.
    * 
    * @param result
    *           the test result.
    */
   protected void printFooter(TestResult result)
   {
      if (result.wasSuccessful())
      {
         getWriter().println();
         getWriter().print("OK");
         getWriter().println(" (" + result.runCount() + " test" + (result.runCount() == 1 ? "" : "s") + ")");

      }
      else
      {
         getWriter().println();
         getWriter().println("FAILURES!!!");
         getWriter().println(
               "Tests run: " + result.runCount() + ",  Failures: " + result.failureCount() + ",  Errors: "
                     + result.errorCount());
      }
      getWriter().println();
   }

   /**
    * Returns the formatted string of the elapsed time.
    * 
    * @param runTime
    *           the elapsed time
    * @return the elapsed time.
    */
   protected String elapsedTimeAsString(long runTime)
   {
      return NumberFormat.getInstance().format((double) runTime / 1000);
   }

   public PrintStream getWriter()
   {
      return m_fWriter;
   }

   /**
    * Adds an error.
    * 
    * @param test
    *           the test in error.
    * @param t
    *           the thrown error
    * @see junit.framework.TestListener#addError(Test, Throwable)
    */
   public void addError(Test test, Throwable t)
   {
      getWriter().print("E");
   }

   /**
    * Adds a failure.
    * 
    * @param test
    *           the failing test.
    * @param t
    *           the thrown failure
    * @see junit.framework.TestListener#addFailure(Test, AssertionFailedError)
    */
   public void addFailure(Test test, AssertionFailedError t)
   {
      getWriter().print("F");
   }

   /**
    * A test ends. (do nothing)
    * 
    * @param test
    *           the ending test
    * @see junit.framework.TestListener#endTest(Test)
    */
   public void endTest(Test test)
   {
   }

   /**
    * A test starts.
    * 
    * @param test
    *           the starting test
    * @see junit.framework.TestListener#startTest(Test)
    */
   public void startTest(Test test)
   {
      getWriter().print(".");
      if (m_fColumn++ >= 40)
      {
         getWriter().println();
         m_fColumn = 0;
      }
   }

}
