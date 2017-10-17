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

package org.novaforge.forge.tools.managementmodule.ui.client.helper;

import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.ErrorCodeMapping;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ErrorEnumeration;
import org.novaforge.forge.ui.commons.client.helper.AbstractRPCCall;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.StatusCodeException;

public abstract class AbstractManagementRPCCall<T> extends AbstractRPCCall<T>
{

  private static final String PARAM_CAS_TICKET = "ticket";
  
   protected EventBus getEventBus()
   {
      return Common.GLOBAL_EVENT_BUS;
   }

   protected abstract void callService(AsyncCallback<T> cb);

   private void call(final int retriesLeft)
   {
      this.callService(new AsyncCallback<T>()
      {
         @Override
         public void onFailure(final Throwable caught)
         {
            GWT.log(caught.toString(), caught);
            try
            {
               throw caught;
            }
            catch (final StatusCodeException statusCodeException)
            {
               if (statusCodeException.getStatusCode() == 401 || statusCodeException.getStatusCode()  == 400)
               {
                 Window.alert(ErrorCodeMapping.getLocalizedMessage(ErrorEnumeration.ERROR_SERVER_ERROR));
                 //remove cas ticket if present
                 if ( Window.Location.getParameter(PARAM_CAS_TICKET) != null &&  !ManagementModuleConstants.COMMON_EMPTY_TEXT.equals(Window.Location.getParameter(PARAM_CAS_TICKET)) )
                 {
                   UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
                   urlBuilder.removeParameter(PARAM_CAS_TICKET);
                   Window.Location.assign(urlBuilder.buildString());
                 } 
                 else
                 {
                   Window.Location.assign(Window.Location.getHref());
                 }
               }
               else
               {
                 AbstractManagementRPCCall.this.onFailure(statusCodeException);
               }
            }
            catch (final InvocationException invocationException)
            {
               if (retriesLeft <= 0)
               {
                 AbstractManagementRPCCall.this.onFailure(invocationException);
               }
               else
               {
                 AbstractManagementRPCCall.this.call(retriesLeft - 1); // retry call
               }
            }
            catch (final IncompatibleRemoteServiceException remoteServiceException)
            {
              Window.alert(ErrorCodeMapping.getLocalizedMessage(ErrorEnumeration.ERROR_OUT_OF_DATE));
            }
            catch (final SerializationException serializationException)
            {
              Window.alert(ErrorCodeMapping.getLocalizedMessage(ErrorEnumeration.ERROR_SERIALIZATION));
            }
            catch (final RequestTimeoutException e)
            {
               Window.alert(ErrorCodeMapping.getLocalizedMessage(ErrorEnumeration.ERROR_SERVER_TIMEOUT));
            }
            catch (final Throwable e)
            {// application exception
              AbstractManagementRPCCall.this.onFailure(e);
            }
         }

         @Override
         public void onSuccess(final T result)
         {
           AbstractManagementRPCCall.this.onSuccess(result);
         }
      });
   }

   public void retry(final int retryCount)
   {
      this.call(retryCount);
   }
}
