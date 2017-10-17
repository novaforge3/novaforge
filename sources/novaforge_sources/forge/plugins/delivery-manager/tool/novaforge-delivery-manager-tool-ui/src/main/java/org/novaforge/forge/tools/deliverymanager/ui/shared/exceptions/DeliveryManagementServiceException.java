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
package org.novaforge.forge.tools.deliverymanager.ui.shared.exceptions;

/**
 * @author BILET-JC
 */
public class DeliveryManagementServiceException extends Exception
{
   /**
    *
    */
   private static final long serialVersionUID = 8785051955901568329L;
   private ExceptionCode code;

   /**
    * Default constructor
    */
   public DeliveryManagementServiceException()
   {
      super();
   }

   /**
    * @param pCode
    *           the exception code
    * @see Exception#Exception()
    */
   public DeliveryManagementServiceException(final ExceptionCode pCode)
   {
      super();
      this.code = pCode;
   }

   /**
    * @param pCode
    *           the exception code
    * @param pCause
    *           the exception cause
    * @see Exception#Exception(Throwable)
    */
   public DeliveryManagementServiceException(final ExceptionCode pCode, final Throwable pCause)
   {
      super(pCause);
      this.code = pCode;
   }

   /**
    * @param pMessage
    *           the exception message
    * @param pCause
    *           the exception cause
    * @see Exception#Exception(String, Throwable)
    */
   public DeliveryManagementServiceException(final String pMessage, final Throwable pCause)
   {
      super(pMessage, pCause);
      this.code = null;
   }

   /**
    * @see Exception#Exception(String, Throwable)
    * @param pCode
    *           the exception code
    * @param pMessage
    *           the exception message
    * @param pCause
    *           the exception cause
    */
   public DeliveryManagementServiceException(final ExceptionCode pCode, final String pMessage, final Throwable pCause)
   {
      super(pMessage, pCause);
      this.code = pCode;
   }

   /**
    * @see Exception#Exception(String)
    * @param pCode
    *           the exception code
    * @param pMessage
    *           the exception message
    */
   public DeliveryManagementServiceException(final ExceptionCode pCode, final String pMessage)
   {
      super(pMessage);
      this.code = pCode;
   }

   /**
    * @return exception code declared. By default, it will return {@link ExceptionCode#TECHNICAL_ERROR}
    */
   public ExceptionCode getCode()
   {
      return this.code;
   }

}
