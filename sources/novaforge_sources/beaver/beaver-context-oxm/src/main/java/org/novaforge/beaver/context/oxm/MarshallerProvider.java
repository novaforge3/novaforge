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
package org.novaforge.beaver.context.oxm;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;

public class MarshallerProvider
{
   private final static String UTF8_ENCODING = "UTF-8";

   public static <T> T loadObjectFromXML(final Class<T> pClazz, final String pFilePath) throws Exception
   {
      return loadObjectFromXML(pClazz, new FileInputStream(pFilePath));
   }

   public static <T> void writeObjectToXML(final Class<T> pClazz, final Object pObject, final String pFilePath)
   throws Exception
   {
      writeObjectToXML(pClazz, pObject, new FileOutputStream(pFilePath));
   }

   public static <T> T loadObjectFromXML(final Class<T> pClazz, final InputStream pInputStream) throws Exception
   {
      IBindingFactory jibxfactory = BindingDirectory.getFactory(pClazz);
      IUnmarshallingContext unMarshallingContext = jibxfactory.createUnmarshallingContext();

      Object config = unMarshallingContext.unmarshalDocument(pInputStream, null);
      return pClazz.cast(config);
   }

   public static <T> void writeObjectToXML(final Class<T> pClazz, final Object pObject, final OutputStream pOutputStream)
   throws Exception
   {
      IBindingFactory jibxfactory = BindingDirectory.getFactory(pClazz);
      IMarshallingContext unMarshallingContext = jibxfactory.createMarshallingContext();
      unMarshallingContext.setIndent(4);
      unMarshallingContext.marshalDocument(pObject, UTF8_ENCODING, null, pOutputStream);
   }

   private MarshallerProvider()
   {
      // Utility class should have private explicit constructor
   }
}
