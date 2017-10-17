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
package org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions;

import org.restlet.data.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * This exception occured when an error to communicate with a alfresco instance occured. It allow to get http
 * error and alfresco errors.
 * 
 * @author cadetr
 */
public class AlfrescoRestConnectionException extends Exception
{
   /**
    * 
    */
   private static final long  serialVersionUID = -1251086042194854966L;
   /**
    * Errors returned from a Alfresco instance.
    */
   private List<String>       errors           = new ArrayList<String>();
   /**
    * Status code
    */
   private StatusSerializable statusCode;

   public AlfrescoRestConnectionException(final String message)
   {
      super(message);
   }

   public AlfrescoRestConnectionException(final String message, final Throwable cause)
   {
      super(message, cause);
   }

   public AlfrescoRestConnectionException(final String message, final Status pStatus)
   {
      super(message);
      this.statusCode = new StatusSerializable(pStatus);
   }

   public AlfrescoRestConnectionException(final String message, final Status pStatus,
         final List<String> errors)
   {
      super(message);
      this.statusCode = new StatusSerializable(pStatus);
      this.errors = errors;
   }

   public AlfrescoRestConnectionException(final String message, final Status pStatus, final Throwable cause)
   {
      super(message, cause);
      this.statusCode = new StatusSerializable(pStatus);
   }

   public AlfrescoRestConnectionException(final String message, final Status pStatus,
         final List<String> errors, final Throwable cause)
   {
      super(message, cause);
      this.statusCode = new StatusSerializable(pStatus);
      this.errors = errors;

   }

   public AlfrescoRestConnectionException(final Status pStatus, final List<String> errors)
   {
      super();
      this.statusCode = new StatusSerializable(pStatus);
      this.errors = errors;
   }

   public AlfrescoRestConnectionException(Throwable cause, final Status pStatus, final List<String> errors)
   {
      super(cause);
      this.errors = errors;
      this.statusCode = new StatusSerializable(pStatus);
   }

   /**
    * A error code got from the server.
    *
    * @return the StringError
    */
   public StatusSerializable getStatusError()
   {
      return statusCode;
   }

   @Override
   public String getMessage()
   {
      StringBuffer message = new StringBuffer(super.getMessage());
      if (this.getErrors() != null)
      {
         for (String error : this.getErrors())
         {
            message.append("\n").append(error);
         }
      }

      return message.toString();
   }

   /**
    * A list of errors returned from the server, if any. Could be empty or null.
    *
    * @return A List of errors returned from the server.
    */
   public List<String> getErrors()
   {
      return errors;
   }
}
