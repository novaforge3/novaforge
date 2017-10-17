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
package org.novaforge.forge.ui.portal.client.util;

import com.google.common.base.Strings;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.UUID;

/**
 * Util class to manage vaadin resource
 * 
 * @author Guillaume Lamirand
 */
public class ResourceUtils
{

  /**
   * Default extension
   */
  private static final String PNG   = ".png";
  /**
   * Reg ex to replace special char
   */
  private final static String REGEX = "[^\\w]";

  /**
   * Will build a {@link StreamResource} from an array of byte and a filename
   *
   * @param pBytes
   *          image binary arrays
   * @param pFileName
   *          fileName used to manage cache
   * @return {@link StreamResource} build
   */
  public static StreamResource buildImageResource(final byte[] pBytes, final String pFileName)
  {
    return buildImageResource(pBytes, pFileName, PNG);
  }

  /**
   * Will build a {@link StreamResource} from an array of byte and a filename
   *
   * @param pBytes
   *          image binary arrays
   * @param pFileName
   *          fileName used to manage cache
   * @param pExtension
   *          the image extension such as png,jpg used to build mimetype
   * @return {@link StreamResource} build
   */
  public static StreamResource buildImageResource(final byte[] pBytes, final String pFileName,
      final String pExtension)
  {

    final StreamSource streamSource = new StreamSource()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = 4762638623469893204L;

      /**
       * {@inheritDoc}
       */
      @Override
      public InputStream getStream()
      {
        return (pBytes == null) ? null : new ByteArrayInputStream(pBytes);
      }
    };
    final StringBuilder fileName = new StringBuilder();
    if (Strings.isNullOrEmpty(pFileName))
    {
      fileName.append(UUID.randomUUID());
    }
    else
    {
      // Normalize the given filename to avoid special characters or spaces
      final String normalized = Normalizer.normalize(pFileName, Normalizer.Form.NFD);
      fileName.append((normalized.replaceAll(REGEX, "").toLowerCase()));
    }
    fileName.append(pExtension);
    return new StreamResource(streamSource, fileName.toString());
  }
}
