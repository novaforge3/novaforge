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

import java.io.OutputStream;
import java.io.Serializable;

/**
 * Provides an OutputStream to an internal String. Internally converts bytes to a Strings and stores them in
 * an internal StringBuffer.
 * 
 * @author <a href="mailto:.org">Felix Project Team</a>
 */
public class StringOutputStream extends OutputStream implements Serializable
{

   /**
    * Id.
    */
   private static final long serialVersionUID = -5912060965986156224L;

   /**
    * The internal destination StringBuffer.
    */
   protected StringBuffer    m_buffer         = null;

   /**
    * Creates new StringOutputStream, makes a new internal StringBuffer.
    */
   public StringOutputStream()
   {
      super();
      m_buffer = new StringBuffer();
   }

   /**
    * Returns the content of the internal StringBuffer as a String, the result of all writing to this
    * OutputStream.
    * 
    * @return returns the content of the internal StringBuffer
    */
   @Override
   public String toString()
   {
      return m_buffer.toString();
   }

   /**
    * Writes and appends a single byte to StringOutputStream.
    *
    * @param b
    *           the byte as an int to add
    */
   @Override
   public void write(int b)
   {
      m_buffer.append((char) b);
   }

   /**
    * Writes and appends a byte array to StringOutputStream.
    * 
    * @param b
    *           the byte array
    * @param off
    *           the byte array starting index
    * @param len
    *           the number of bytes from byte array to write to the stream
    */
   @Override
   public void write(byte[] b, int off, int len)
   {
      if ((off < 0) || (len < 0) || (off + len) > b.length)
      {
         throw new IndexOutOfBoundsException("StringOutputStream.write: Parameters out of bounds.");
      }
      byte[] bytes = new byte[len];
      for (int i = 0; i < len; i++)
      {
         bytes[i] = b[off];
         off++;
      }
      m_buffer.append(toCharArray(bytes));
   }

   /**
    * Sets the internal StringBuffer to null.
    */
   @Override
   public void close()
   {
      m_buffer = null;

   }

   /**
    * Converts byte array to char array.
    *
    * @param barr
    *           input byte array
    * @return the char array corresponding to the given byte array
    */
   public static char[] toCharArray(byte[] barr)
   {
      if (barr == null)
      {
         return null;
      }
      char[] carr = new char[barr.length];
      for (int i = 0; i < barr.length; i++)
      {
         carr[i] = (char) barr[i];
      }
      return carr;
   }

   /**
    * Writes and appends a String to StringOutputStream.
    *
    * @param s
    *     the String to add
    */
   public void write(String s)
   {
      m_buffer.append(s);
   }
}