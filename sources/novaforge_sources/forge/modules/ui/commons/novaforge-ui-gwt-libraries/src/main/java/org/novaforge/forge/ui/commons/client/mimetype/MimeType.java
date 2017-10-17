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
package org.novaforge.forge.ui.commons.client.mimetype;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * @author caseryj
 */
public class MimeType
{
   private static MimeTypeRessources RESSOURCES = GWT.create(MimeTypeRessources.class);

   public static AbstractImagePrototype getMimeTypeIcon(final String pName)
   {
      return AbstractImagePrototype.create(getMimeTypeImageResource(pName));

   }

   public static ImageResource getMimeTypeImageResource(final String pName)
   {
      ImageResource mimeTypeIcon = RESSOURCES.nc();
      if (pName.endsWith(".mp3") || pName.endsWith(".wma") || pName.endsWith(".mid")
            || pName.endsWith(".midi") || pName.endsWith(".wav") || pName.endsWith(".wave")
            || pName.endsWith(".fls") || pName.endsWith(".aif") || pName.endsWith(".iff")
            || pName.endsWith(".m3u") || pName.endsWith(".m4a") || pName.endsWith(".mpa")
            || pName.endsWith(".ra"))
      {
         mimeTypeIcon = RESSOURCES.audio();
      }
      else if (pName.endsWith(".java") || pName.endsWith(".c") || pName.endsWith(".php")
            || pName.endsWith(".class") || pName.endsWith(".html") || pName.endsWith(".htm")
            || pName.endsWith(".wma"))
      {
         mimeTypeIcon = RESSOURCES.code();
      }
      else if (pName.endsWith(".doc") || pName.endsWith(".docx"))
      {
         mimeTypeIcon = RESSOURCES.doc();
      }
      else if (pName.endsWith(".jpg") || pName.endsWith(".png") || pName.endsWith(".gif")
            || pName.endsWith(".tif") || pName.endsWith(".bmp") || pName.endsWith(".psd")
            || pName.endsWith(".tga"))
      {
         mimeTypeIcon = RESSOURCES.img();
      }
      else if (pName.endsWith(".odc"))
      {
         mimeTypeIcon = RESSOURCES.odc();
      }
      else if (pName.endsWith(".odp"))
      {
         mimeTypeIcon = RESSOURCES.odp();
      }
      else if (pName.endsWith(".odt"))
      {
         mimeTypeIcon = RESSOURCES.odt();
      }
      else if (pName.endsWith(".pdf"))
      {
         mimeTypeIcon = RESSOURCES.pdf();
      }
      else if (pName.endsWith(".ppt") || pName.endsWith(".pptx"))
      {
         mimeTypeIcon = RESSOURCES.ppt();
      }
      else if (pName.endsWith(".txt"))
      {
         mimeTypeIcon = RESSOURCES.txt();
      }
      else if (pName.endsWith(".3g2") || pName.endsWith(".3gp") || pName.endsWith(".asf")
            || pName.endsWith(".asx") || pName.endsWith(".avi") || pName.endsWith(".flv")
            || pName.endsWith(".mov") || pName.endsWith(".mp4") || pName.endsWith(".mpg")
            || pName.endsWith(".rm") || pName.endsWith(".srt") || pName.endsWith(".swf")
            || pName.endsWith(".vob") || pName.endsWith(".wmv"))
      {
         mimeTypeIcon = RESSOURCES.video();
      }
      else if (pName.endsWith(".xls") || pName.endsWith(".xlsx"))
      {
         mimeTypeIcon = RESSOURCES.xls();
      }
      else if (pName.endsWith(".7z") || pName.endsWith(".deb") || pName.endsWith(".gz")
            || pName.endsWith(".pkg") || pName.endsWith(".rar") || pName.endsWith(".rpm")
            || pName.endsWith(".sit") || pName.endsWith(".sitx") || pName.endsWith(".tar.gz")
            || pName.endsWith(".tgz") || pName.endsWith(".tar.bz2") || pName.endsWith(".tbz")
            || pName.endsWith(".zip") || pName.endsWith(".zipx"))
      {
         mimeTypeIcon = RESSOURCES.zip();
      }
      return mimeTypeIcon;
   }
}
