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
package org.novaforge.forge.tools.deliverymanager.exceptions;

/**
 * This exception should be used when an error occurs during the delivery managment.
 * 
 * @author vvigo
 * @author Guillaume Lamirand
 */
public class DeliveryServiceException extends Exception
{

   /**
    * Serial Version UID
    */
   private static final long   serialVersionUID = 6595581758299905321L;

   /**
    * Exception code
    * 
    * @see DeliveryServiceException#getCode()
    */
   private final ExceptionCode code;

   /**
    * Default constructor
    */
   public DeliveryServiceException()
   {
      super();
      this.code = ExceptionCode.TECHNICAL_ERROR;
   }

   /**
    * Construct an exception instance with message and cause.
    * 
    * @param pMessage
    *           represents the error message
    * @param pCause
    *           represensts original cause
    */
   public DeliveryServiceException(final String pMessage, final Throwable pCause)
   {
      super(pMessage, pCause);
      this.code = ExceptionCode.TECHNICAL_ERROR;
   }

   /**
    * Construct an exception instance with message, a cause and an exception code
    * 
    * @param pMessage
    *           represents the error message
    * @param pCause
    *           represensts original cause
    * @param pCode
    *           represents the code error
    */
   public DeliveryServiceException(final String pMessage, final Throwable pCause, final ExceptionCode pCode)
   {
      super(pMessage, pCause);
      this.code = pCode;
   }

   /**
    * Construct an exception instance with message.
    * 
    * @param pMessage
    *           represents the error message
    */
   public DeliveryServiceException(final String pMessage)
   {
      super(pMessage);
      this.code = ExceptionCode.TECHNICAL_ERROR;
   }

   /**
    * Construct an exception instance with message and a exception code
    * 
    * @param pMessage
    *           represents the error message
    * @param pCode
    *           represents the code error
    */
   public DeliveryServiceException(final String pMessage, final ExceptionCode pCode)
   {
      super(pMessage);
      this.code = pCode;
   }

   /**
    * Construct an exception instance with message.
    * 
    * @param pCause
    *           represensts original cause
    */
   public DeliveryServiceException(final Throwable pCause)
   {
      super(pCause);
      this.code = ExceptionCode.TECHNICAL_ERROR;
   }

   /**
    * Construct an exception instance with message.
    * 
    * @param cause
    *           represensts original cause
    * @param pCode
    *           represents the code error
    */
   public DeliveryServiceException(final Throwable cause, final ExceptionCode pCode)
   {
      super(cause);
      this.code = pCode;
   }

   /**
    * Get the exception code. By default it is ExceptionCode.TECHNICAL_ERROR
    * 
    * @return code
    */
   public ExceptionCode getCode()
   {
      return this.code;
   }

}
