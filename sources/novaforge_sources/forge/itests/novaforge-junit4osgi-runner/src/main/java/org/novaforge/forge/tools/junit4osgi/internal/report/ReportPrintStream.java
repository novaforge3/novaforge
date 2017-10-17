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

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Print stream dispatching on a given one and storing written data in a output stream.
 * 
 * @author <a href="mailto:.org">Felix Project Team</a>
 */
public class ReportPrintStream extends PrintStream
{

   private final PrintStream m_stream;

   private final boolean     m_duplicate;

   public ReportPrintStream(OutputStream out, PrintStream def, boolean hideOutput)
   {
      super(out);
      m_stream = def;
      m_duplicate = !hideOutput;
   }

   @Override
   public void println()
   {
      if (m_duplicate)
      {
         m_stream.println();
      }
      super.println();
   }

   @Override
   public void println(boolean x)
   {
      if (m_duplicate)
      {
         m_stream.println(x);
      }
      super.println(x);
   }

   @Override
   public void println(char x)
   {
      if (m_duplicate)
      {
         m_stream.println(x);
      }
      super.println(x);
   }

   @Override
   public void println(char[] x)
   {
      if (m_duplicate)
      {
         m_stream.println(x);
      }
      super.println(x);
   }


   @Override
   public void println(double x)
   {
      if (m_duplicate)
      {
         m_stream.println(x);
      }
      super.println(x);
   }


   @Override
   public void println(float x)
   {
      if (m_duplicate)
      {
         m_stream.println(x);
      }
      super.println(x);
   }


   @Override
   public void println(int x)
   {
      if (m_duplicate)
      {
         m_stream.println(x);
      }
      super.println(x);
   }



   @Override
   public void println(long x)
   {
      if (m_duplicate)
      {
         m_stream.println(x);
      }
      super.println(x);
   }


   @Override
   public void println(Object x)
   {
      if (m_duplicate)
      {
         m_stream.println(x);
      }
      super.println(x);
   }


   @Override
   public void println(String s)
   {
      if (m_duplicate)
      {
         m_stream.println(s);
      }
      super.println(s);
   }

}