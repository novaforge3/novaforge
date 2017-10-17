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

package org.novaforge.forge.remote.services.exception;


/**
 * Exception thrown by the Remote Services Layer.
 * 
 * @author blachonm
 */
public class RemoteServiceException extends Exception
{
   /**
    *
    */
   private static final long serialVersionUID = 5700965645244196407L;
   private final ExceptionCode code;

   /**
    * Constructor.
    * 
    * @param pCode
    *           ExceptionCode
    */
   public RemoteServiceException(final ExceptionCode pCode)
   {
      this.code = pCode;
   }

   /**
    * Constructor.
    * 
    * @param pCode
    *           ExceptionCode
    * @param message
    *           Exception message
    */
   public RemoteServiceException(final ExceptionCode pCode, final String message)
   {
      super(message);
      this.code = pCode;
   }

   /**
    * Constructor.
    * 
    * @param pCode
    *           ExceptionCode
    * @param cause
    *           initial cause
    */
   public RemoteServiceException(final ExceptionCode pCode, final Throwable cause)
   {
      super(cause);
      this.code = pCode;
   }

   /**
    * Constructor.
    * 
    * @param pCode
    *           ExceptionCode
    * @param message
    *           Exception message
    * @param cause
    *           initial cause
    */
   public RemoteServiceException(final ExceptionCode pCode, final String message, final Throwable cause)
   {
      super(message, cause);
      this.code = pCode;
   }

   /**
    * Returns the Code associated to this Exception.
    * 
    * @return the ExceptionCode
    */
   public ExceptionCode getCode()
   {
      return code;
   }

}
