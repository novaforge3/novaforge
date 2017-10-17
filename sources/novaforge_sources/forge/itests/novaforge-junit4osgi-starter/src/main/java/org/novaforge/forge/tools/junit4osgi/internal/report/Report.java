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
package org.novaforge.forge.tools.junit4osgi.internal.report;

import junit.framework.Test;
import org.codehaus.plexus.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Test report. This class is provides the basics to support several output format.
 * 
 * @author <a href="mailto:.org">Felix Project Team</a>
 */
public class Report
{

   /**
    * New line constant.
    */
   protected static final String NL               = System.getProperty("line.separator");
   /**
    * Double format.
    */
   private final NumberFormat m_numberFormat = NumberFormat.getInstance(Locale.US);
   /**
    * Number of ran tests.
    */
   protected int  m_completedCount;
   /**
    * Number of errors.
    */
   protected int  m_errorsCount;
   /**
    * Number of failures.
    */
   protected int  m_failuresCount;
   /**
    * Time at the beginning of the test execution.
    */
   protected long m_startTime;
   /**
    * Time at the end of the test execution.
    */
   protected long m_endTime;
   /**
    * Failing tests.
    */
   private List m_failureSources = new ArrayList();
   /**
    * Tests in error.
    */
   private List m_errorSources   = new ArrayList();

   /**
    * Gets failing tests.
    *
    * @return the list of failing tests.
    */
   public List getFailureSources()
   {
      return this.m_failureSources;
   }

   /**
    * Gets tests in error.
    *
    * @return the list of test throwing unexpected exceptions
    */
   public List getErrorSources()
   {
      return this.m_errorSources;
   }

   /**
    * Callback called when a test starts.
    */
   public void testStarting()
   {
      m_startTime = System.currentTimeMillis();
   }

   /**
    * Callback called when a test ends successfully.
    */
   public void testSucceeded()
   {
      endTest();
   }

   /**
    * Callback called when a test ends. This method handles common action when a test ends.
    */
   private void endTest()
   {
      ++m_completedCount;

      m_endTime = System.currentTimeMillis();

      if (m_startTime == 0)
      {
         m_startTime = m_endTime;
      }
   }

   /**
    * Callback called when a test throws an unexpected error.
    *
    * @param test
    *           the test in error.
    */
   public void testError(Test test)
   {
      ++m_errorsCount;
      m_errorSources.add(test.toString());
      endTest();
   }

   /**
    * Callback called when a test fails.
    *
    * @param test
    *           the failing test.
    */
   public void testFailed(Test test)
   {
      ++m_failuresCount;
      m_failureSources.add(test.toString());
      endTest();
   }

   public int getNumErrors()
   {
      return m_errorsCount;
   }

   public int getNumFailures()
   {
      return m_failuresCount;
   }

   public int getNumTests()
   {
      return m_completedCount;
   }

   /**
    * Reset the report.
    */
   public void reset()
   {
      m_errorsCount = 0;

      m_failuresCount = 0;

      m_completedCount = 0;

      this.m_failureSources = new ArrayList();

      this.m_errorSources = new ArrayList();

   }

   /**
    * Returns the formatted String to display the given double.
    * 
    * @param runTime
    *           the elapsed time
    * @return the String displaying the elapsed time
    */
   protected String elapsedTimeAsString(long runTime)
   {
      return m_numberFormat.format((double) runTime / 1000);
   }

   /**
    * Returns the stack trace as String.
    * 
    * @param test
    *           the test
    * @param e
    *           the exception
    * @return stack trace as string.
    */
   protected String getStackTrace(Test test, Throwable e)
   {

      if (e == null)
      {
         return "";
      }

      StringWriter w = new StringWriter();
      if (e != null)
      {
         e.printStackTrace(new PrintWriter(w));
         w.flush();
      }
      String text = w.toString();
      String marker = "at " + test.toString();

      String[] lines = StringUtils.split(text, "\n");
      int lastLine = lines.length - 1;
      int causedByLine = -1;
      // skip first
      for (int i = 1; i < lines.length; i++)
      {
         String line = lines[i].trim();
         if (line.startsWith(marker))
         {
            lastLine = i;
         }
         else if (line.startsWith("Caused by"))
         {
            causedByLine = i;
            break;
         }
      }

      StringBuffer trace = new StringBuffer();
      for (int i = 0; i <= lastLine; i++)
      {
         trace.append(lines[i]);
         trace.append("\n");
      }

      if (causedByLine != -1)
      {
         for (int i = causedByLine; i < lines.length; i++)
         {
            trace.append(lines[i]);
            trace.append("\n");
         }
      }
      return trace.toString();
   }

}