/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.plugins.scm.svn.agent.internal.helpers;

import java.util.HashSet;
import java.util.Set;

/**
 * @author sbenoist
 *
 */
/**
 * This class is used for exporting directories from svn
 * 
 * @author sbenoist
 */
public class SVNExportDirectory
{
   /**
    * This is the path of the directory
    */
   private String       path;

   /**
    * these are the files we want to export in the directory in case of no recursive export
    */
   private Set<String> files;

   /**
    * This is the way we export the directory
    */
   private boolean      recursive = false;

   public SVNExportDirectory(final String pPath, final boolean pRecursive)
   {
      super();
      path = pPath;
      recursive = pRecursive;
   }

   public String getPath()
   {
      return path;
   }

   public Set<String> getFiles()
   {
      return files;
   }

   public void addFile(final String pFile)
   {
      if (files == null)
      {
         files = new HashSet<String>();
      }

      files.add(pFile);
   }

   public void addFiles(final Set<String> pFiles)
   {
      if (files == null)
      {
         files = new HashSet<String>();
      }

      files.addAll(pFiles);
   }

   public void removeFile(final String pFile)
   {
      if (files != null && files.isEmpty() == false)
      {
         files.remove(pFile);
      }
   }

   public boolean isRecursive()
   {
      return recursive;
   }

   public void setRecursive(final boolean pRecursive)
   {
      recursive = pRecursive;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((path == null) ? 0 : path.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      SVNExportDirectory other = (SVNExportDirectory) obj;
      if (path == null)
      {
         if (other.path != null)
         {
            return false;
         }
      }
      else if (!path.equals(other.path))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append("SVNExportDirectory [files=");
      builder.append(files);
      builder.append(", path=");
      builder.append(path);
      builder.append(", recursive=");
      builder.append(recursive);
      builder.append("]");
      return builder.toString();
   }

}
