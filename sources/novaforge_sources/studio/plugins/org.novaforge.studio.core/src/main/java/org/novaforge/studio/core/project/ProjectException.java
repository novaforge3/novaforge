/**
 * NovaForge(TM) is a web-based forge offering a Collaborative Development and 
 * Project Management Environment.
 *
 * Copyright (C) 2007-2012  BULL SAS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.studio.core.project;

import org.novaforge.studio.core.client.ExceptionCode;

/**
 * @author blachonm
 *
 */
public class ProjectException extends Exception
{
   private final ExceptionCode code;
   /**
    * 
    */
   private static final long serialVersionUID = -4100240678443437424L;

   public ProjectException(ExceptionCode pCode)
   {
      this.code = pCode;
   }

   public ProjectException(ExceptionCode pCode, String message)
   {
      super(message);
      this.code = pCode;
   }

   public ProjectException(ExceptionCode pCode, Throwable cause)
   {
      super(cause);
      this.code = pCode;

   }

   public ProjectException(ExceptionCode pCode, String message, Throwable cause)
   {
      super(message, cause);
      this.code = pCode;

   }

   public ExceptionCode getCode()
   {
      return code;
   }
}
